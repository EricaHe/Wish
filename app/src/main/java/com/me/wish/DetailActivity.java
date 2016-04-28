package com.me.wish;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableRow;
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
    private List<SparseBooleanArray> isSelected;

    private TextView titleTxtView;
    private TextView descriptionTxtView;
    private TextView dueDateTxtView;
    private ListView childWishListView;

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

        // 如果心愿id不对，直接退出
        if (wishID == -1) {
            Toast.makeText(DetailActivity.this, "未正确指定查看的心愿", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // id正确后，设置各项显示内容
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                wish = wishMgr.queryParentWishById(wishID);
                titleTxtView.setText(wish.title);
                descriptionTxtView.setText(wish.description);
                descriptionTxtView.setTextColor(0xffaaaaaa);
                if (wish.dueDate == null) {
                    dueDateTxtView.setText("");
                } else {
                    dueDateTxtView.setText(sdf.format(wish.dueDate));
                }

                childWishListView = (ListView) findViewById(R.id.detailWishChildWishList);
                if (wish.children_ids == null){
                    childWishListTitle = new ArrayList<>();
                    childWishListTitle.add("无子任务");
                }
                else {
                    childWishList = wishMgr.queryChildWishesByParentId(wishID);
                    childWishListTitle = new ArrayList<>();
                    for(Wish w : childWishList){
                        childWishListTitle.add(w.title);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        childWishListTitle);
                if(childWishListView != null)
                    childWishListView.setAdapter(adapter);
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
        navigationView.setNavigationItemSelectedListener(this);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
