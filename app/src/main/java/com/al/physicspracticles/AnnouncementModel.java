package com.al.physicspracticles;

public class AnnouncementModel {
    String date,title,msg;

    public AnnouncementModel(String date, String title, String msg) {
        this.date = date;
        this.title = title;
        this.msg = msg;
    }

    public AnnouncementModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
