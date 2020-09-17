package com.conagra.mvp.bean;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/18.
 */
public class BaseBackBean<T> implements Serializable {


    /**
     * status : 1
     * data : {"tb_BloodSugar_ID":"0235e38b-54de-4fcf-b96d-978de7ea193d","IsExist":true}
     * message : 成功
     */

    private int status;
    private T data;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {

    }
}
