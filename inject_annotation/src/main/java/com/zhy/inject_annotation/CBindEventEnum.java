package com.zhy.inject_annotation;


import com.zhy.inject_annotation.model.EventListener;

import java.util.ArrayList;
import java.util.List;



public enum  CBindEventEnum {

    COnClick,COnLongClick;

    public static EventListener analysisCBindEventEnum(CBindEventEnum bindEventEnum){

        EventListener listener = new EventListener();
        List<String> list = new ArrayList<>();
        switch (bindEventEnum){
            case COnClick:
                listener.setListenerMethod("onClick");
                listener.setListenerType("android.view.View.OnClickListener");
                listener.setSetterListener("setOnClickListener");
                list.add("android.view.View");
                listener.setReturnType(null);
                listener.setParamters(list);
                break;
            case COnLongClick:
                listener.setListenerMethod("onLongClick");
                listener.setListenerType("android.view.View.OnLongClickListener");
                listener.setSetterListener("setOnLongClickListener");
                listener.setReturnType(Boolean.class);
                list.add("android.view.View");
                listener.setParamters(list);
                break;
        }
        return listener;
    }

}
