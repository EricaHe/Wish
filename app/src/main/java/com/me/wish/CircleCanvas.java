package com.me.wish;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyy on 2016/4/28.
 */

public class CircleCanvas extends View {
    //keep the stars to draw
    public List<CircleInfo> mCircleInfos=new ArrayList<CircleInfo>();

    //save x,y and radius
    public static class CircleInfo
    {
        private float x;
        private float y;
        private float radius;
        private float deltax;
        private float deltay;

        public CircleInfo(float x,float y,float radius)
        {
            this.x=x;
            this.y=y;
            this.radius=radius;
        }

        public float getX()
        {
            return x;
        }

        public void setX(float x)
        {
            this.x=x;
        }

        public float getY()
        {
            return y;
        }

        public void setY(float y)
        {
            this.y=y;
        }

        public float getRadius()
        {
            return radius;
        }

        public void addRadius(float r)
        {
            this.radius+=r;
        }
    }

    public CircleCanvas(Context context)
    {
        super(context);
    }

    @Override
    protected  void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRGB(0,0,0);
        for(CircleInfo circleInfo:mCircleInfos)
        {
            canvas.drawCircle(circleInfo.getX(),circleInfo.getY(),circleInfo.getRadius(),paint);
        }
    }
}

