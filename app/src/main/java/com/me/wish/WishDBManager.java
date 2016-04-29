package com.me.wish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public void addChildWishes(List<Wish> childWishes) {
        db.beginTransaction();  //开始事务
        try {
            for (Wish wish : childWishes) {
                /* get format of date */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                Log.e("param", "wish:" + Integer.toString(wish.parent_id));
                Object[] item = new Object[]{wish.title, wish.description, wish.parent_id, wish.expr,
                        (wish.dueDate == null) ? "" : sdf.format(wish.dueDate),
                        (wish.createDate == null) ? "" : sdf.format(wish.createDate),
                        (wish.finishDate == null) ? "" : sdf.format(wish.finishDate),
                        (wish.isFinished) ? 1 : 0, (wish.isDaily) ? 1 : 0};
                db.execSQL("INSERT INTO child_wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", item);
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<Integer> addChildWishesReturnIds(List<Wish> childWishes) {
        db.beginTransaction();  //开始事务
        List<Integer> newChildrenIds = new ArrayList<>();
        try {
            for (Wish wish : childWishes) {
                /* get format of date */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                Object[] item = new Object[]{wish.title, wish.description, wish.parent_id, wish.expr,
                        (wish.dueDate == null) ? "" : sdf.format(wish.dueDate),
                        (wish.createDate == null) ? "" : sdf.format(wish.createDate),
                        (wish.finishDate == null) ? "" : sdf.format(wish.finishDate),
                        (wish.isFinished) ? 1 : 0, (wish.isDaily) ? 1 : 0};
                db.execSQL("INSERT INTO child_wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", item);
                Cursor cursor = db.rawQuery("select last_insert_rowid() from parent_wish", null);
                if (cursor.moveToFirst())
                    newChildrenIds.add(cursor.getInt(0));
                cursor.close();
            }
            db.setTransactionSuccessful();  //设置事务成功完成
            return newChildrenIds;
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public int addParentWishReturnId(Wish parentWish) {
        db.beginTransaction();  //开始事务
        int newParentId;
        try {
            /* get format of date */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            Object[] item = new Object[]{parentWish.title, parentWish.description, parentWish.childrenIdToString(),
                    parentWish.comment, parentWish.photo_path, parentWish.expr,
                    (parentWish.dueDate == null) ? "" : sdf.format(parentWish.dueDate),
                    (parentWish.createDate == null) ? "" : sdf.format(parentWish.createDate),
                    (parentWish.finishDate == null) ? "" : sdf.format(parentWish.finishDate),
                    (parentWish.isFinished) ? 1 : 0};
            db.execSQL("INSERT INTO parent_wish VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", item);
            Cursor cursor = db.rawQuery("select last_insert_rowid() from parent_wish", null);
            if (cursor.moveToFirst())
                newParentId = cursor.getInt(0);
            else
                newParentId = -1;
            cursor.close();
            db.setTransactionSuccessful();  //设置事务成功完成
            return newParentId;
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void addParentWish(Wish parentWish) {
        db.beginTransaction();  //开始事务
        try {
            /* get format of date */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            Object[] item = new Object[]{parentWish.title, parentWish.description, parentWish.childrenIdToString(),
                    parentWish.comment, parentWish.photo_path, parentWish.expr,
                    (parentWish.dueDate == null) ? "" : sdf.format(parentWish.dueDate),
                    (parentWish.createDate == null) ? "" : sdf.format(parentWish.createDate),
                    (parentWish.finishDate == null) ? "" : sdf.format(parentWish.finishDate),
                    (parentWish.isFinished) ? 1 : 0};
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

    public void updateDescription(String table, Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("description", wish.description);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateParentId(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("parent_id", wish.parent_id);
        db.update("child_wish", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateChildId(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("child_id", wish.childrenIdToString());
        db.update("parent_wish", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateChildIdByParentId(String sChildrenIds, Integer parentId) {
        ContentValues cv = new ContentValues();
        cv.put("child_id", sChildrenIds);
        db.update("parent_wish", cv, "id = ?", new String[]{Integer.toString(parentId)});
    }

    public void updateComment(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("comment", wish.comment);
        db.update("parent_wish", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updatePhotoPath(Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("photo_path", wish.photo_path.toString());
        db.update("parent_wish", cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateExpr(String table, Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("expr", wish.expr);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateDueDate(String table, Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("due_date", sdf.format(wish.dueDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateCreateDate(String table, Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("create_date", sdf.format(wish.createDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateFinishDate(String table, Wish wish) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("finish_date", (wish.finishDate == null) ? "" : sdf.format(wish.finishDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateFinishDateById(String table, Integer wishId, Date finishDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        ContentValues cv = new ContentValues();
        cv.put("finish_date", (finishDate == null) ? "" : sdf.format(finishDate));
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wishId)});
    }

    public void updateIsFinished(String table, Wish wish) {
        ContentValues cv = new ContentValues();
        cv.put("is_finished", (wish.isFinished) ? 1 : 0);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wish.id)});
    }

    public void updateIsFinishedById(String table, Integer wishId, Integer isFinished) {
        ContentValues cv = new ContentValues();
        cv.put("is_finished", isFinished);
        db.update(table, cv, "id = ?", new String[]{Integer.toString(wishId)});
    }

    public void updateIsDailyById(Integer wishId, Integer isDaily) {
        ContentValues cv = new ContentValues();
        cv.put("is_daily", isDaily);
        db.update("child_wish", cv, "id = ?", new String[]{Integer.toString(wishId)});
    }

    // delete part
    public void deleteWishes(String table, List<Wish> wishes) throws ParseException {
        List<Integer> parentIdList = new ArrayList<>();
        List<Integer> deletedChildIdList = new ArrayList<>();
        if (table.equals("child_wish")) {
            // 删除子心愿后，还需更新父心愿的child_id字段
            for (Wish childWish : wishes) {
                // delete child wish from database
                deletedChildIdList.add(childWish.id);
                db.delete(table, "id = ?", new String[]{Integer.toString(childWish.id)});
                if (!parentIdList.contains(childWish.parent_id))
                    parentIdList.add(childWish.parent_id);
            }
            // update wish's field of child_id
            for (Integer parentId : parentIdList) {
                Wish parent = queryParentWishById(parentId);
                parent.children_ids.removeAll(deletedChildIdList);
                updateChildId(parent);
            }
        }
        // 删除父心愿时，要同时删除子心愿
        else if (table.equals("parent_wish")) {
            for (Wish parentWish : wishes) {
                for (Integer childId : parentWish.children_ids) {
                    db.delete("child_wish", "id = ?", new String[]{Integer.toString(childId)});
                }
                db.delete(table, "id = ?", new String[]{Integer.toString(parentWish.id)});
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
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null) ?
                    new String[]{""} : c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            String sChildIds = c.getString(c.getColumnIndex("child_id"));
            if (sChildIds == null) {
                wish.children_ids = null;
            } else {
                for (String sChildId : sChildIds.split(",")) {
                    wish.children_ids = new ArrayList<>();
                    Integer childId;
                    if (sChildId.equals(""))
                        childId = null;
                    else
                        childId = Integer.parseInt(sChildId);
                    wish.children_ids.add(childId);
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("finish_date")));

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
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            wish.parent_id = c.getInt(c.getColumnIndex("parent_id"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;
            wish.isDaily = c.getInt(c.getColumnIndex("is_daily")) == 1;
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
            wish.photo_path = (c.getString(c.getColumnIndex("photo_path")) == null) ?
                    new String[]{""} : c.getString(c.getColumnIndex("photo_path")).split(",");
            wish.expr = c.getInt(c.getColumnIndex("expr"));

            String sChildIds = c.getString(c.getColumnIndex("child_id"));
            if (sChildIds == null || sChildIds.equals("")){
                wish.children_ids = null;
            } else {
                wish.children_ids = new ArrayList<Integer>();
                for(String sci : sChildIds.split(",")){
                    wish.children_ids.add(Integer.parseInt(sci));
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;

            wishes.add(wish);
        }
        c.close();
        return wishes;
    }

    public List<Wish> queryChildWishesByParentId(Integer parentId) throws ParseException {
        ArrayList<Wish> wishes = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM child_wish where parent_id = ?", new String[]{Integer.toString(parentId)});
        while (c.moveToNext()) {
            Wish wish = new Wish();
            wish.id = c.getInt(c.getColumnIndex("id"));
            wish.title = c.getString(c.getColumnIndex("title"));
            wish.description = c.getString(c.getColumnIndex("description"));
            wish.expr = c.getInt(c.getColumnIndex("expr"));
            wish.parent_id = c.getInt(c.getColumnIndex("parent_id"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            wish.createDate = (c.getString(c.getColumnIndex("create_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("create_date")));
            wish.dueDate = (c.getString(c.getColumnIndex("due_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("due_date")));
            wish.finishDate = (c.getString(c.getColumnIndex("finish_date")).equals("")) ?
                    null : sdf.parse(c.getString(c.getColumnIndex("finish_date")));

            wish.isFinished = c.getInt(c.getColumnIndex("is_finished")) == 1;
            wish.isDaily = c.getInt(c.getColumnIndex("is_daily")) == 1;

            wishes.add(wish);
        }
        c.close();
        return wishes;
    }

    public boolean queryIsFinishedById(String table, Integer id) {
        boolean result;
        Cursor c = db.rawQuery("SELECT * FROM " + table + " where id = ?", new String[]{Integer.toString(id)});
        if (c.moveToFirst()) {
            result = c.getInt(c.getColumnIndex("is_finished")) == 1;
        } else {
            result = Boolean.parseBoolean(null);
        }
        c.close();
        return result;
    }

    public void getFinishedDate(List<CircleCanvas.CircleInfo> circleInfos,float deltax,float deltay,float r)
    {
        Cursor c=db.rawQuery("select * from child_wish where is_finished = ? order by finish_date asc",new String[]{"1"});
        String finishtime;
        int i=0;
        if(c.moveToFirst())
        {
            finishtime=c.getString(c.getColumnIndex("finish_date"));
            String a=Integer.toString(finishtime.length());
            circleInfos.add(new CircleCanvas.CircleInfo(Float.parseFloat(finishtime.substring(5, 7))*deltax, Float.parseFloat(finishtime.substring(8, 10))*deltay, r));
        }
        while(c.moveToNext())
        {
            finishtime=c.getString(c.getColumnIndex("finish_date"));
            if(Float.parseFloat(finishtime.substring(5, 7))*deltax==circleInfos.get(i).getX()&&Float.parseFloat(finishtime.substring(8,10))*deltay==circleInfos.get(i).getY())
            {
                //That day finish more than 1 wish
                circleInfos.get(i).addRadius(r);
            }
            else
            {
                circleInfos.add(new CircleCanvas.CircleInfo(Float.parseFloat(finishtime.substring(5, 7))*deltax,Float.parseFloat(finishtime.substring(8, 10))*deltay,r));
                i++;
            }
        }
    }

    public void closeDB() {
        db.close();
    }
}