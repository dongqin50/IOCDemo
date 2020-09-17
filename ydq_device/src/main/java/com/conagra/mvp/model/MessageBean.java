package com.conagra.mvp.model;

import android.graphics.Bitmap;

import com.conagra.mvp.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by Dongqin.Ye on 2018/5/11.
 */

public class MessageBean implements Serializable {

    private Bitmap bitmap;  //  头像
    private int num;  // 未读数量
    private String  title;  // 标题
    private String  time;  // 时间
    private Object content;  //  文本
    private int type;   //文本类型

    public MessageBean(int num,String title, String  content) {
        this.num = num;
        this.title = title;
        this.time = TimeUtils.getYMDData(0);
        this.content = content;
        this.type = 1;
    }

    public MessageBean() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}


