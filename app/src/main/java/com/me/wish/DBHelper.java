package com.me.wish;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Erica on 2016/4/23.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wishPlus.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS wish" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, description TEXT, parent_id INTEGER, " +
                "child_id TEXT, comment TEXT, photo_path TEXT, expr INTEGER, due_date TEXT, create_date TEXT, " +
                "finish_date TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, level INTEGER, current_expr INTEGER, " +
                "max_expr INTEGER, honors TEXT)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }
}
