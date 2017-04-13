package com.example.android.tripIntent.chat;



public class Group {

    private String title;
    private String desc;
    private String image;



    private String username;



    private String date;



    private String uid;



    public Group(){

    }

    public Group(String title, String desc, String image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;
        this.date = date;
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return "Created on "+date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}


