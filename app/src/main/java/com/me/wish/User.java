package com.me.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhangyy on 2016/4/23.
 */
public class User {
    private DBHelper dbhelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String name;
    private int level;
    private int current_expr;
    private int max_expr;
    private String honors;

    public User(Context context)
    {
        dbhelper = new DBHelper(context);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("user", null, null, null, null, null, null);
    }

    //judge if a user has been created
    public boolean exist()
    {
        if(cursor.moveToFirst()) return true;
        else return false;
    }

    public void addUser()
    {
        ContentValues values = new ContentValues();
        values.put("_id",1);
        values.put("name","无名小卒");
        values.put("level",0);
        values.put("current_expr",0);
        values.put("max_expr",10);
        values.put("honors","初出茅庐");
        db.insert("user",null,values);
    }

    public void readDB()
    {
        name=cursor.getString(cursor.getColumnIndex("name"));
        level=cursor.getInt(cursor.getColumnIndex("level"));
        current_expr=cursor.getInt(cursor.getColumnIndex("current_expr"));
        max_expr=cursor.getInt(cursor.getColumnIndex("max_expr"));
        honors=cursor.getString(cursor.getColumnIndex("honors"));
    }

    //get information
    public String getUserName()
    {
        return name;
    }

    public int getLevel()
    {
        return level;
    }

    public int getCurrentExpr()
    {
        return current_expr;
    }

    public int getMaxExpr()
    {
        return max_expr;
    }

    public String[] getHonors()
    {
        return honors.split(",");//The honors are joined with ','
    }


    //update information
    public void updateUserName(String newName)
    {
        name=newName;
        ContentValues values = new ContentValues();
        values.put("name",newName);
        db.update("user",values,null,null);
    }

    public void updateLevel(int newLevel)
    {
        level=newLevel;
        ContentValues values = new ContentValues();
        values.put("level",newLevel);
        db.update("user",values,null,null);
    }

    public void updateCurrentExpr(int newCurrentExpr)
    {
        current_expr=newCurrentExpr;
        ContentValues values = new ContentValues();
        values.put("current_expr",newCurrentExpr);
        db.update("user",values,null,null);
    }

    public void updateMaxExpr(int newMaxExpr)
    {
        max_expr = newMaxExpr;
        ContentValues values = new ContentValues();
        values.put("max_expr",newMaxExpr);
        db.update("user",values,null,null);
    }

    public void updateHonors(String newHonor)
    {
        if(honors=="") honors=newHonor;
        else honors += ","+newHonor;
        ContentValues values = new ContentValues();
        values.put("honors",honors);
        db.update("user",values,null,null);
    }








}

