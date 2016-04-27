package com.me.wish;

/**
 * Created by zhangyy on 2016/4/27.
 */
public class Achieve {
    private String name;
    private int imageID;
    private String condition;

    public Achieve(String inputName,int inputID,String inputCondition)
    {
        name=inputName;
        imageID=inputID;
        condition=inputCondition;
    }

    public String getName()
    {
        return name;
    }

    public int getImageID()
    {
        return imageID;
    }

    public String getCondition()
    {
        return condition;
    }
}
