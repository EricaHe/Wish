package com.me.wish;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Erica on 2016/4/23.
 */
public class WishDBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public WishDBManager(Context context) {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    // add part
    // TODO:解决子心愿id与父心愿id在添加记录时的问题
    public void addChildWishes(List<Wish> childWishes) {
        db.beginTransaction();  //开始事务
        try {
            for (Wish wish : childWishes) {
                /* get format of date */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
                Object[] item = new Object[]{wish.title, wish.description, wish.parent.id,
                        wish.comment, wish.photo_path, wish.expr,
                        (wish.dueDate == null)?"":sdf.format(wish.dueDate),
                        (wish.createDate == null)?"":sdf.format(wish.createDate),
                        (wish.finishDate == null)?"":sdf.format(wish.finishDate), wish.isFinished};
                db.execSQL("INSERT INTO child_wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", item);
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addParentWish(Wish parentWish) {
        db.beginTransaction();  //开始事务
        try {
            /* get format of date */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
            Object[] item = new Object[]{parentWish.title, parentWish.description, parentWish.childrenIdToString(),
                    parentWish.comment, parentWish.photo_path, parentWish.expr,
                    (parentWish.dueDate == null)?"":sdf.format(parentWish.dueDate),
                    (parentWish.createDate == null)?"":sdf.format(parentWish.createDate),
                    (parentWish.finishDate == null)?"":sdf.format(parentWish.finishDate), parentWish.isFinished};
            db.execSQL("INSERT INTO parent_wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", item);
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    // update part
    // table wish
    public void updateTitle(String table, Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("title", wish.title);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateDescription(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("description", wish.description);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateParentId(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("parent_id", wish.parent.id);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateChildId(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("child_id", wish.childrenIdToString());
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateComment(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("comment", wish.comment);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updatePhotoPath(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("photo_path", wish.photo_path.toString());
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateExpr(String table,Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("expr", wish.expr);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateDueDate(String table,Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("due_date", sdf.format(wish.dueDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateCreateDate(String table,Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("create_date", sdf.format(wish.createDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateFinishDate(String table,Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("finish_date", sdf.format(wish.finishDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateIsFinished(String table, Wish wish){
        ContentValues cv = new ContentValues();
        cv.put("is_finished", (wish.isFinished)?1:0);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    // delete part
    public void deleteWishes(String table, List<Wish> wishes) {
        // 删除子心愿后，还需更新父心愿的child_id字段
        List<Wish> parentList = new ArrayList<>();
        if(table.equals("child_wish")){
            for(Wish childWish : wishes){
                db.delete(table, "id = ?", new String[]{Integer.toString(childWish.id)});
                if(!parentList.contains(childWish.parent))
                    parentList.add(childWish.parent);
            }
            for(Wish parent : parentList){
                parent.children.removeAll(wishes);
                updateChildId("parent_wish", parent);
            }
        }
        // 删除父心愿时，要同时删除子心愿
        else if (table.equals("parent_wish")){
            for (Wish parentWish:wishes){
                for (Wish childWish:parentWish.children){
                    db.delete("child_wish","id = ?",new String[]{Integer.toString(childWish.id)});
                }
                db.delete(table,"id = ?",new String[]{Integer.toString(parentWish.id)});
            }
        }
    }

    // query part
    public Wish queryParentWishById(int id) throws ParseException {
        Wish wish = new Wish();
        Cursor c = db.rawQuery("select * from parent_wish where id = ?", new String[]{Integer.toString(id)});
        if (c.moveToFirst()) {
            wish.id = id;
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.comment = c.getString(c.getColumnIndex("comment"));
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null)?
                    new String[]{""}:c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            for(String sChildId : c.getString(c.getColumnIndex("child_id")).split(",")){
                Integer childId = Integer.parseInt(sChildId);
                wish.children.add(queryChildWishById(childId));
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;
        }
        c.close();
        return wish;
    }

    public Wish queryChildWishById(int id) throws ParseException {
        Wish wish = new Wish();
        Cursor c = db.rawQuery("select * from child_wish where id = ?", new String[]{Integer.toString(id)});
        if (c.moveToFirst()) {
            wish.id = id;
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.comment = c.getString(c.getColumnIndex("comment"));
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null)?
                    new String[]{""}:c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            wish.parent = queryParentWishById(c.getInt(c.getColumnIndex("parent_id")));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;
        }
        c.close();
        return wish;
    }

    public List<Wish> queryAllParentWishes() throws ParseException {
        ArrayList<Wish> wishes = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM parent_wish", null);
        while (c.moveToNext()) {
            Wish wish = new Wish();
            wish.id = c.getInt(c.getColumnIndex("id"));
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.comment = c.getString(c.getColumnIndex("comment"));
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null)?
                    new String[]{""}:c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;

            wishes.add(wish);
        }
        c.close();
        return wishes;
    }

    public List<Wish> queryChildWishesByParent(Wish parent) throws ParseException {
        ArrayList<Wish> wishes = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM child_wish where parent_id = ?", new String[]{Integer.toString(parent.id)});
        while (c.moveToNext()) {
            Wish wish = new Wish();
            wish.id = c.getInt(c.getColumnIndex("id"));
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.comment = c.getString(c.getColumnIndex("comment"));
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null)?
                    new String[]{""}:c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));
            wish.parent = queryParentWishById(c.getInt(c.getColumnIndex("parent_id")));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals(""))?
                    null:sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;

            wishes.add(wish);
        }
        c.close();
        return wishes;
    }

    public void closeDB() {
        db.close();
    }
}