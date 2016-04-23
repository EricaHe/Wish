package com.me.wish;

import java.util.Date;
import java.util.List;

/**
 * Created by Erica on 2016/4/23.
 */

public class Wish {
    public int _id;
    public int expr;

    public String title;
    public String description;
    public String comment;
    public String[] photo_path;

    public Wish parent;
    public List<Wish> children;

    public Date dueDate;
    public Date createDate;
    public Date finishDate;

    public Wish(){
        this.title = "";
        this.description = "";
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent = null;
        this.children = null;

        this.dueDate = null;
        this.createDate = null;
        this.finishDate = null;
    }

    public Wish(String title,String description, Date dueDate, Date createDate){
        this.title = title;
        this.description = description;
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent = null;
        this.children = null;

        this.dueDate = dueDate;
        this.createDate = createDate;
        this.finishDate = null;
    }

    public Wish(String title,String description, Date dueDate, Date createDate, Wish parent, List<Wish> children){
        this.title = title;
        this.description = description;
        this.comment = "";
        this.photo_path = null;

        this.expr = 10;

        this.parent = parent;
        this.children = children;

        this.dueDate = dueDate;
        this.createDate = createDate;
        this.finishDate = null;
    }
}
