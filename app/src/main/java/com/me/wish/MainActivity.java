package com.me.wish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<Wish> wishes;
    private List<List<Wish>> childWishes;
    private WishDBManager wishMgr;
    private User user;
    private TextView user_level;
    private TextView exp;

    public static MainActivity mAct;

    protected void dataInitialization(WishDBManager dbm) throws ParseException {
        wishes = new ArrayList<Wish>();
        childWishes = new ArrayList<List<Wish>>();

        wishes = dbm.queryAllParentWishes();
        for (Wish wish : wishes) {
            childWishes.add(dbm.queryChildWishesByParentId(wish.id));
        }
    }

    private void showPersonalCenter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAct = this;
        wishMgr = new WishDBManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toAddActivity = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(toAddActivity);
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        //show personal center
        View headerView = navigationView.getHeaderView(0);
        final EditText user_name = (EditText) headerView.findViewById(R.id.textName);
        user_level = (TextView) headerView.findViewById(R.id.textLevel);
        exp = (TextView) headerView.findViewById(R.id.textExp);
        user = new User(this);
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

    @Override
    protected void onStart() {
        super.onStart();
        try {
            dataInitialization(wishMgr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        if (expandableListView != null) {
            expandableListView.setGroupIndicator(null);
        }
        // 监听组点击
        if (expandableListView != null) {
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @SuppressLint("NewApi")
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    boolean cannotExpand = groupPosition >= childWishes.size() || childWishes.get(groupPosition).isEmpty();
                    if (cannotExpand) {
                        Intent goToDetailIntent = new Intent();
                        goToDetailIntent.putExtra("wish_id", wishes.get(groupPosition).id);
                        goToDetailIntent.setClass(MainActivity.this, DetailActivity.class);
                        startActivity(goToDetailIntent);
                    }
                    return cannotExpand;
                }
            });
        }

        // 监听每个分组里子控件的点击事件
        if (expandableListView != null) {
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Intent goToDetailIntent = new Intent();
                    goToDetailIntent.putExtra("wish_id", wishes.get(groupPosition).id);
                    goToDetailIntent.setClass(MainActivity.this, DetailActivity.class);
                    startActivity(goToDetailIntent);
                    return false;
                }
            });
        }

        MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(this, wishes, childWishes, wishMgr, user_level, exp);
        if (expandableListView != null) {
            expandableListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        wishMgr.closeDB();
        user.closeDB();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(MainActivity.this, AchieveActivity.class);
            user.readDB();//update the achievements
            Log.d("enter", Integer.toString(user.getHonors().length));
            intent.putExtra("achieve", user.getHonors());
            startActivity(intent);
        } else if (id == R.id.nav_star) {
            Intent intent = new Intent(MainActivity.this, StarActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
