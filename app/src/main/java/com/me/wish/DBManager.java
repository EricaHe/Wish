package com.me.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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
                    childrenId.add(childWish._id);
                }
                //
                Object[] item = new Object[]{wish.title, wish.description, wish.parent._id, childrenId.toString(),
                wish.comment,wish.photo_path,wish.expr,wish.dueDate,wish.createDate,wish.finishDate};

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
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateDescription(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("description", wish.description);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateParentId(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("parent_id", wish.parent._id);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateChildId(Wish wish) {
        ContentValues cv = new ContentValues();
        // get child wishes' id
        List<Integer> childrenId = new ArrayList<Integer>();
        for(Wish childWish : wish.children){
            childrenId.add(childWish._id);
        }
        cv.put("child_id", childrenId.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateComment(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("comment", wish.comment);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updatePhotoPath(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("photo_path", wish.photo_path.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateExpr(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("expr", wish.expr);
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateDueDate(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("due_date", wish.dueDate.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateCreateDate(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("create_date", wish.createDate.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }
    public void updateFinishDate(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("finish_date", wish.finishDate.toString());
        db.update("id", cv, "id = ?", new String[]{Integer.toString(wish._id)});
    }

    // delete part
    public void deleteWish(Wish wish) {
        db.delete("wish", "id = ?", new String[]{Integer.toString(wish._id)});
    }

    // TODO:query part
}
