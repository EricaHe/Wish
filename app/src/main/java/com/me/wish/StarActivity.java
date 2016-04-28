package com.me.wish;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StarActivity extends AppCompatActivity {
    private CircleCanvas mCircleCanvas;
    private float deltaWidth;
    private float deltaHeight;
    private float radius;
    private WishDBManager wishManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.activity_star,null);
        mCircleCanvas = new CircleCanvas(this);
        viewGroup.addView(mCircleCanvas,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(viewGroup);

        //get the width and height of the screen
        WindowManager windowManager = getWindowManager();
        Point widthHeight = new Point();
        windowManager.getDefaultDisplay().getSize(widthHeight);

        //count information of the stars
        deltaWidth = widthHeight.x/13;
        deltaHeight= widthHeight.y/40;
        radius=Math.min(deltaWidth,deltaHeight)/4;


        //draw the stars
        wishManager=new WishDBManager(this);
        wishManager.getFinishedDate(mCircleCanvas.mCircleInfos,deltaWidth,deltaHeight,radius);
        mCircleCanvas.invalidate();
    }

    protected  void onDestroy()
    {
        super.onDestroy();
        wishManager.closeDB();
    }
}

