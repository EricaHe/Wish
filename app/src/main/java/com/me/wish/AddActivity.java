package com.me.wish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */
    private ListView childWishListView;
    private ImageButton addChildWishImgBtn;
    private EditText dueDateTime;
    private EditText childWishEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        childWishListView = (ListView)findViewById(R.id.addChildWishListView);
        childWishListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                childWish));
        fixListViewHeight(childWishListView);

        addChildWishImgBtn = (ImageButton)findViewById(R.id.addChildWishImgBtn);
        childWishEditTxt = (EditText)findViewById(R.id.childWishEditText);
        addChildWishImgBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String extraWish = childWishEditTxt.getText().toString();
                if(extraWish.equals("")){
                    Toast noEmptyHint = Toast.makeText(AddActivity.this, "心愿不能为空", Toast.LENGTH_SHORT);
                    noEmptyHint.show();
                } else{
                    childWish.add(extraWish);
                    childWishListView.setAdapter(new ArrayAdapter<String>(AddActivity.this,
                            android.R.layout.simple_list_item_checked, childWish));
                    fixListViewHeight(childWishListView);
                    childWishEditTxt.setText("");
                }
            }
        });

        // date输入框
        dueDateTime = (EditText) findViewById(R.id.dueDateEditText);

        dueDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            AddActivity.this);
                    dateTimePicKDialog.dateTimePicKDialog(dueDateTime);
                }
            }
        });

        dueDateTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        AddActivity.this);
                dateTimePicKDialog.dateTimePicKDialog(dueDateTime);
            }
        });
    }
    private List<String> childWish = new ArrayList<String>();

    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index , null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
