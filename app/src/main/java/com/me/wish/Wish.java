package com.me.wish;

/**
 * Created by Erica on 2016/4/23.
 */
public class Wish {
    public int id;
    public String title;
    public String description;

    public Wish(){}

    public Wish(String title,String description){
        this.title = title;
        this.description = description;
    }
}
