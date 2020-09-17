package com.conagra.mvp.model;

import android.util.Log;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.constant.MsgStatus;
import com.conagra.mvp.utils.LogMessage;
import com.google.gson.GsonBuilder;

import rx.Subscriber;

/**
 * Created by yedongqin on 2018/3/14.
 */

public abstract class MySubscriber<T> extends Subscriber<BaseBackBean<T>> {

    @Override
    public void onStart() {
        super.onStart();
        LogMessage.doLogMessage("MySubscriber1", "onStart被调用了");
    }

    @Override
    public void onCompleted() {
        LogMessage.doLogMessage("MySubscriber1", "onCompleted被调用了");
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        LogMessage.doLogMessage("Error", e.getMessage());
    }

    public void onBackError(String message){
        LogMessage.doLogMessage("Error", message);
    };

    public abstract void onBackNext(T model);
    @Override
    public void onNext(BaseBackBean<T> baseBackBean) {
        Log.v("ccccc11111"," dddd " + new GsonBuilder().create().toJson(baseBackBean));
        if(baseBackBean != null){
            if(baseBackBean.getStatus() != MsgStatus.STATUS_SUCCESS){
                onBackError(baseBackBean.getMessage());
            }else {
                onBackNext(baseBackBean.getData());
            }
        }
    }

}

