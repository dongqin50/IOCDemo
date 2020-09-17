package com.conagra.hardware.model;

import android.support.annotation.DrawableRes;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/16.
 */
public class MonitorModel implements Serializable{

    private @DrawableRes
    int iconResId;
    private String title;
    private String content;
//    private String status;
    private JkgjListBackModel jkgjListBackBean;

    public MonitorModel() {
    }

    public MonitorModel(int iconResId, String title, String content) {
        this.iconResId = iconResId;
        this.title = title;
        this.content = content;
    }
    public MonitorModel(int iconResId, String title, String content, JkgjListBackModel jkgjListBackBean) {
            this.iconResId = iconResId;
            this.title = title;
            this.content = content;
        this.jkgjListBackBean = jkgjListBackBean;
    }

    public JkgjListBackModel getJkgjListBackBean() {
        return jkgjListBackBean;
    }

    public void setJkgjListBackBean(JkgjListBackModel jkgjListBackBean) {
        this.jkgjListBackBean = jkgjListBackBean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
}
