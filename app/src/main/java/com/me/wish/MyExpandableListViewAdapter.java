package com.me.wish;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Erica on 2016/4/27.
 */

// TODO:界面排版
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<SparseBooleanArray> isSelected;
    private List<Wish> wishes;
    private List<List<Wish>> childWishes;
    private WishDBManager wishDBM;
    private User user;
    private TextView user_level;
    private TextView exp;

    public MyExpandableListViewAdapter(Context context, List<Wish> wishes, List<List<Wish>> childWishes,
                                       WishDBManager wishMgr, TextView user_level, TextView exp) {
        this.context = context;
        this.wishes = wishes;
        this.childWishes = childWishes;
        this.isSelected = new ArrayList<SparseBooleanArray>();
        this.wishDBM = wishMgr;
        this.user = new User(context);
        this.user_level = user_level;
        this.exp = exp;
        if (!user.exist()) user.addUser();

        // initialize
        for (List<Wish> child : childWishes) {
            SparseBooleanArray sba = new SparseBooleanArray();
            for (int i = 0; i < child.size(); i++) {
                sba.append(i, wishDBM.queryIsFinishedById("child_wish", child.get(i).id));
            }
            isSelected.add(sba);
        }
    }

    /**
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return wishes.size();
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return childWishes.get(groupPosition).size();
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition 父级index
     * @return 第groupPosition个父心愿
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition) {
        return wishes.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition 父级index
     * @param childPosition 子级index
     * @return 第groupPosition个父心愿的第childPosition个子心愿
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childWishes.get(groupPosition).get(childPosition);
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition 父级index
     * @return groupPosition
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition 父级index
     * @param childPosition 子级index
     * @return childPosition    子级index
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return true
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return convertView
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.wish_expand_list, null);
            groupHolder = new GroupHolder();
            groupHolder.txt = (TextView) convertView.findViewById(R.id.wishItemTxtView);
            groupHolder.img = (ImageView) convertView.findViewById(R.id.wishItemCaret);
            groupHolder.date = (TextView) convertView.findViewById(R.id.dueDateOfParent);
            convertView.setTag(groupHolder);
        } else
            groupHolder = (GroupHolder) convertView.getTag();

        if (wishes.get(groupPosition).children_ids == null) {
            groupHolder.img.setVisibility(View.INVISIBLE);
        } else if (!isExpanded) {
            groupHolder.img.setVisibility(View.VISIBLE);
            groupHolder.img.setBackgroundResource(android.R.drawable.arrow_down_float);
        } else {
            groupHolder.img.setVisibility(View.VISIBLE);
            groupHolder.img.setBackgroundResource(android.R.drawable.arrow_up_float);
        }

        groupHolder.txt.setText(wishes.get(groupPosition).title);
        groupHolder.date.setText((wishes.get(groupPosition).dueDate == null) ?
                "" : sdf.format(wishes.get(groupPosition).dueDate));
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_wish_expand_list, null);
            itemHolder = new ItemHolder();
            itemHolder.txt = (TextView) convertView.findViewById(R.id.child_wish_item);
            itemHolder.checkBox = (CheckBox) convertView.findViewById(R.id.finishWishChkBox);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.txt.setText(childWishes.get(groupPosition).get(childPosition).title);
        if (isSelected.get(groupPosition).get(childPosition)) {
            itemHolder.checkBox.setChecked(true);
            itemHolder.txt.getPaint().setStrikeThruText(true);
            itemHolder.txt.setTextColor(0xffbdbdbd);
        } else {
            itemHolder.checkBox.setChecked(false);
            itemHolder.txt.getPaint().setStrikeThruText(false);
            itemHolder.txt.setTextColor(0xff616161);
        }
        final ItemHolder finalItemHolder = itemHolder;
        itemHolder.checkBox.setOnClickListener(new ExpandableListView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected.get(groupPosition).get(childPosition)) {
                    // not finished yet
                    isSelected.get(groupPosition).put(childPosition, false);
                    finalItemHolder.txt.getPaint().setStrikeThruText(false);
                    finalItemHolder.txt.setTextColor(0xff616161);
                    wishDBM.updateIsFinishedById("child_wish", childWishes.get(groupPosition).get(childPosition).id, 0);
                    childWishes.get(groupPosition).get(childPosition).isFinished = false;
                    wishDBM.updateFinishDateById("child_wish", childWishes.get(groupPosition).get(childPosition).id, null);
                    childWishes.get(groupPosition).get(childPosition).finishDate = null;

                    Toast.makeText(MainActivity.mAct, "减少10点经验", Toast.LENGTH_SHORT).show();
                    user.updateCurrentExpr(user.getCurrentExpr() - childWishes.get(groupPosition).get(childPosition).expr);
                    int level = user.getLevel();
                    if (user.getCurrentExpr() < level * level * 10) {
                        user.updateLevel(level - 1);
                        user.updateMaxExpr(level * level * 10);
                    }
                } else {
                    if (childWishes.get(groupPosition).get(childPosition).isDaily) {
                        Date currentDate = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                        if (childWishes.get(groupPosition).get(childPosition).finishDate == null ||
                                sdf.format(childWishes.get(groupPosition).get(childPosition).finishDate) == sdf.format(currentDate)) {
                            Toast.makeText(MainActivity.mAct, "增加10点经验", Toast.LENGTH_SHORT).show();
                            user.updateCurrentExpr(user.getCurrentExpr() + childWishes.get(groupPosition).get(childPosition).expr);
                            wishDBM.updateFinishDateById("child_wish", childWishes.get(groupPosition).get(childPosition).id,
                                    new Date());
                            childWishes.get(groupPosition).get(childPosition).finishDate = new Date();
                        }
                    } else {
                        // is finished
                        isSelected.get(groupPosition).put(childPosition, true);
                        finalItemHolder.txt.getPaint().setStrikeThruText(true);
                        finalItemHolder.txt.setTextColor(0xffbdbdbd);
                        wishDBM.updateIsFinishedById("child_wish", childWishes.get(groupPosition).get(childPosition).id, 1);
                        childWishes.get(groupPosition).get(childPosition).isFinished = true;
                        wishDBM.updateFinishDateById("child_wish", childWishes.get(groupPosition).get(childPosition).id,
                                new Date());
                        childWishes.get(groupPosition).get(childPosition).finishDate = new Date();
                        Toast.makeText(MainActivity.mAct, "增加10点经验", Toast.LENGTH_SHORT).show();
                        user.updateCurrentExpr(user.getCurrentExpr() + childWishes.get(groupPosition).get(childPosition).expr);
                    }
                    //update level
                    if (user.getCurrentExpr() >= user.getMaxExpr()) {
                        int level = user.getLevel();
                        level++;
                        user.updateLevel(level);
                        user.updateMaxExpr((level + 1) * (level + 1) * 10);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle("Level up!");
                        dialog.setMessage("当前等级： " + level);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        //get a new achievement every 2 levels
                        if (level % 2 == 0) {
                            user.updateHonors("plus1");
                            Log.d("in", Integer.toString(user.getHonors().length));
                            AlertDialog.Builder achievedialog = new AlertDialog.Builder(context);
                            achievedialog.setTitle("An Achievement!");
                            achievedialog.setMessage("您获得了一个新成就，请前往荣誉称号页面查看");
                            achievedialog.setCancelable(false);
                            achievedialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            achievedialog.show();
                        }
                    }
                }
                notifyDataSetChanged();
                user_level.setText("Level:" + Integer.toString(user.getLevel()));
                exp.setText("经验值：" + Integer.toString(user.getCurrentExpr()) + "/" + Integer.toString(user.getMaxExpr()));
            }
        });
        return convertView;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition 父级index
     * @param childPosition 子级index
     * @return true
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

class GroupHolder {
    public TextView txt;
    public ImageView img;
    public TextView date;
}

class ItemHolder {
    public TextView txt;
    public CheckBox checkBox;
}
