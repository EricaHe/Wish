package com.me.wish;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;

public class StarActivity extends AppCompatActivity {
    private CircleCanvas mCircleCanvas;
    private float deltaWidth;
    private float deltaHeight;
    private float radius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup)getLayoutInflater().inflate(R.layout.activity_star,null);
        mCircleCanvas = new CircleCanvas(this);
        viewGroup.addView(mCircleCanvas,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(viewGroup);

//get the data of the finished wishes

        //get the width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float xdpi=getResources().getDisplayMetrics().xdpi;
        float ydpi=getResources().getDisplayMetrics().ydpi;

        deltaWidth = xdpi/13;
        deltaHeight= ydpi/32;
        Log.d("Width",Float.toString(deltaWidth));
        Log.d("Height",Float.toString(deltaHeight));
        radius=Math.min(deltaWidth,deltaHeight)/4;
        mCircleCanvas.mCircleInfos.add(new CircleCanvas.CircleInfo(4*deltaWidth, 28*deltaHeight, radius));
        mCircleCanvas.mCircleInfos.add(new CircleCanvas.CircleInfo(4*deltaWidth, 29*deltaHeight, radius));
        mCircleCanvas.mCircleInfos.add(new CircleCanvas.CircleInfo(100, 200, 10));
        mCircleCanvas.invalidate();
    }
}

