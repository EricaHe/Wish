package com.me.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Erica on 2016/4/23.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    // add part
    public void add(List<Wish> wishes) {
        db.beginTransaction();  //开始事务
        try {
            for (Wish wish : wishes) {
                // get child wishes' id
                List<Integer> childrenId = new ArrayList<Integer>();
                for(Wish childWish : wish.children){
                    childrenId.add(childWish.id);
                }
                /* get format of date */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Object[] item = new Object[]{wish.title, wish.description, wish.parent.id, childrenId.toString(),
                        wish.comment,wish.photo_path,wish.expr,sdf.format(wish.dueDate),sdf.format(wish.createDate),
                        sdf.format(wish.finishDate)};

                db.execSQL("INSERT INTO wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",item);
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    // update part
    // table wish
    // TODO: table user
    public void update(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("title", wish.title);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateDescription(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("description", wish.description);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateParentId(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("parentid", wish.parent.id);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateChildId(Wish wish) {
        ContentValues cv = new ContentValues();
        // get child wishes' id
        List<Integer> childrenId = new ArrayList<Integer>();
        for(Wish childWish : wish.children){
            childrenId.add(childWish.id);
        }
        cv.put("childid", childrenId.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateComment(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("comment", wish.comment);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updatePhotoPath(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("photo_path", wish.photo_path.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateExpr(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("expr", wish.expr);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateDueDate(Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContentValues cv = new ContentValues();
        cv.put("due_date", sdf.format(wish.dueDate));
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateCreateDate(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("create_date", wish.createDate.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }
    public void updateFinishDate(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("finish_date", wish.finishDate.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    // delete part
    public void deleteWish(Wish wish) {
        db.delete("wish", "id = ?", new String[]{Integer.toString(wish.id)});
    }

    // query part
    public List<Wish> queryAll() throws ParseException {
        ArrayList<Wish> wishes = new ArrayList<>();
        Cursor c = queryTheCursor();
        while(c.moveToNext()){
            Wish wish = new Wish();
            wish.id = c.getInt(c.getColumnIndex("id"));
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.comment = c.getString(c.getColumnIndex("comment"));
            wish.photo_path = c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            wish.createDate = sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wishes.add(wish);
        }
        c.close();
        return wishes;
    }

    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor() {
        return db.rawQuery("SELECT * FROM person", null);
    }
}
