package com.me.wish;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int wishID;
    private Wish wish = new Wish();
    private WishDBManager wishMgr;
    private User user;
    private List<SparseBooleanArray> isSelected;

    private TextView titleTxtView;
    private TextView descriptionTxtView;
    private TextView dueDateTxtView;
    private ListView childWishListView;
    private TextView commentTxtView;
    private TextView invisibleAddingTxtView;
    private TextView invisibleEditingTxtView;

    private List<Wish> childWishList;
    private List<String> childWishListTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        wishMgr = new WishDBManager(DetailActivity.this);

        Intent intent = getIntent();
        wishID = intent.getIntExtra("wish_id", -1);

        titleTxtView = (TextView) findViewById(R.id.detailWishTitleTxtView);
        descriptionTxtView = (TextView) findViewById(R.id.detailWishDescriptionTxtView);
        dueDateTxtView = (TextView) findViewById(R.id.detailWishDueDate);
        commentTxtView = (TextView) findViewById(R.id.detailWishCommentTxtView);

        // 如果心愿id不对，直接退出
        if (wishID == -1) {
            Toast.makeText(DetailActivity.this, "未正确指定查看的心愿", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // id正确后，设置各项显示内容
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                wish = wishMgr.queryParentWishById(wishID);
                titleTxtView.setText(wish.title);
                titleTxtView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SetTextDialogUtil setTextDialog = new SetTextDialogUtil(DetailActivity.this);
                        setTextDialog.setTextDialog(titleTxtView);
                    }
                });
                titleTxtView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        wish.title = titleTxtView.getText().toString();
                        wishMgr.updateTitle("parent_wish", wish);
                    }
                });

                descriptionTxtView.setText(wish.description);
                descriptionTxtView.setTextColor(0xffaaaaaa);
                descriptionTxtView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SetTextDialogUtil setTextDialog = new SetTextDialogUtil(DetailActivity.this);
                        setTextDialog.setTextDialog(descriptionTxtView);
                    }
                });
                descriptionTxtView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        wish.description = descriptionTxtView.getText().toString();
                        wishMgr.updateDescription("parent_wish", wish);
                    }
                });

                if (wish.dueDate == null) {
                    dueDateTxtView.setText("");
                } else {
                    dueDateTxtView.setText(sdf.format(wish.dueDate));
                }
                // date输入框
                dueDateTxtView = (TextView) findViewById(R.id.detailWishDueDate);
                dueDateTxtView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(DetailActivity.this);
                            dateTimePicKDialog.dateTimePicKDialog(dueDateTxtView);
                            try {
                                wish.dueDate = sdf.parse(dueDateTxtView.getText().toString());
                                wishMgr.updateDueDate("parent_wish", wish);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                dueDateTxtView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(DetailActivity.this);
                        dateTimePicKDialog.dateTimePicKDialog(dueDateTxtView);
                        try {
                            wish.dueDate = (dueDateTxtView.getText().toString().equals("")) ?
                                    null : sdf.parse(dueDateTxtView.getText().toString());
                            wishMgr.updateDueDate("parent_wish", wish);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dueDateTxtView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String modifiedDate = dueDateTxtView.getText().toString();
                        if (modifiedDate.equals("")) {
                            wish.dueDate = null;
                        } else {
                            try {
                                wish.dueDate = sdf.parse(modifiedDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        wishMgr.updateDueDate("parent_wish", wish);
                    }
                });

                childWishListView = (ListView) findViewById(R.id.detailWishChildWishList);
                invisibleAddingTxtView = (TextView) findViewById(R.id.detailWishInvisibleTextForAdding);
                invisibleEditingTxtView = (TextView) findViewById(R.id.detailWishInvisibleTextForEditing);
                if (wish.children_ids == null) {
                    childWishListTitle = new ArrayList<>();
                    childWishListTitle.add("点击添加子心愿");
                } else {
                    childWishList = wishMgr.queryChildWishesByParentId(wishID);
                    childWishListTitle = new ArrayList<>();
                    childWishListTitle.add("点击添加子心愿");
                    for (Wish w : childWishList) {
                        childWishListTitle.add(w.title);
                    }
                }
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        childWishListTitle);
                if (childWishListView != null) {
                    childWishListView.setAdapter(adapter);
                }
                // TODO:修改
                childWishListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == childWishListView.getFirstVisiblePosition()) {
                            SetTextDialogUtil setTextDialog = new SetTextDialogUtil(DetailActivity.this);
                            setTextDialog.setTextDialog(invisibleAddingTxtView);
                        }
                    }
                });
                invisibleAddingTxtView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String extraChildWishTitle = invisibleAddingTxtView.getText().toString();
                        Wish extraChildWish = new Wish(extraChildWishTitle);
                        extraChildWish.parent_id = wishID;
                        Integer childId = wishMgr.addOneChildWishReturnId(extraChildWish);
                        wish.children_ids.add(childId);
                        wishMgr.updateChildId(wish);
                        childWishListTitle.add(extraChildWishTitle);
                        childWishList.add(extraChildWish);
                        adapter.notifyDataSetChanged();
                    }
                });

                commentTxtView.setText(wish.comment);
                commentTxtView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SetTextDialogUtil setTextDialog = new SetTextDialogUtil(DetailActivity.this);
                        setTextDialog.setTextDialog(commentTxtView);
                    }
                });
                commentTxtView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        wish.comment = commentTxtView.getText().toString();
                        wishMgr.updateComment(wish);
                    }
                });
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //show personal center
        View headerView = navigationView.getHeaderView(0);
        final EditText user_name = (EditText) headerView.findViewById(R.id.textName);
        TextView user_level = (TextView) headerView.findViewById(R.id.textLevel);
        TextView exp = (TextView) headerView.findViewById(R.id.textExp);
        user = new User(DetailActivity.this);
        if (!user.exist()) user.addUser();
        user_name.setText(user.getUserName());
        user_level.setText("Level:" + Integer.toString(user.getLevel()));
        exp.setText("经验值：" + Integer.toString(user.getCurrentExpr()) + "/" + Integer.toString(user.getMaxExpr()));

        //update the name
        user_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) user.updateUserName(user_name.getText().toString());
            }
        });

    }

    protected void onStop() {
        super.onStop();
        wishMgr.closeDB();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_achieve) {
            Intent intent = new Intent(DetailActivity.this, AchieveActivity.class);
            intent.putExtra("achieve", user.getHonors());
            startActivity(intent);
        } else if (id == R.id.nav_star) {
            Intent intent = new Intent(DetailActivity.this, StarActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
