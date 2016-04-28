package com.me.wish;

import java.util.Date;
import java.util.List;

/**
 * Created by Erica on 2016/4/23.
 */

public class Wish {
    public int id;
    public int expr;

    public String title;
    public String description;
    public String comment;
    public String[] photo_path;

    public Integer parent_id;
    public List<Integer> children_ids;

    public Date dueDate;
    public Date createDate;
    public Date finishDate;

    public boolean isFinished;
    public boolean isDaily;

    public Wish() {
        this.title = "";
        this.description = "";
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent_id = null;
        this.children_ids = null;

        this.dueDate = null;
        this.createDate = null;
        this.finishDate = null;

        this.isFinished = false;
        this.isDaily = false;
    }

    public Wish(String title) {
        this.title = title;
        this.description = "";
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent_id = null;
        this.children_ids = null;

        this.dueDate = null;
        this.createDate = null;
        this.finishDate = null;

        this.isFinished = false;
        this.isDaily = false;
    }

    public Wish(String title, String description, Date dueDate, Date createDate) {
        this.title = title;
        this.description = description;
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent_id = null;
        this.children_ids = null;

        this.dueDate = dueDate;
        this.createDate = createDate;
        this.finishDate = null;

        this.isFinished = false;
        this.isDaily = false;
    }

    public Wish(String title, String description, Date dueDate, Date createDate, Integer parent_id, List<Integer> children_ids) {
        this.title = title;
        this.description = description;
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent_id = parent_id;
        this.children_ids = children_ids;

        this.dueDate = dueDate;
        this.createDate = createDate;
        this.finishDate = null;

        this.isFinished = false;
        this.isDaily = false;
    }

    public String childrenIdToString() {
        String result = "";
        if (this.children_ids == null) {
            return null;
        } else {
            for (Integer childId : this.children_ids) {
                result = result + "," + Integer.toString(childId);
            }
            if (!result.equals("")) {
                result = result.substring(1);
                return result;
            } else {
                return null;
            }
        }
    }
}
