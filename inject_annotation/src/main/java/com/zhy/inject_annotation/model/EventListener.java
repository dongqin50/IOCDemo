package com.zhy.inject_annotation.model;

import java.util.List;

public class EventListener {

    private String setterListener;

    private String methodName;

    private String  listenerType;

    private String listenerMethod;

    private List<String> paramters;

    private Class returnType;

    private int[] ids;

    public String getSetterListener() {
        return setterListener;
    }

    public void setSetterListener(String setterListener) {
        this.setterListener = setterListener;
    }

    public String getListenerMethod() {
        return listenerMethod;
    }

    public void setListenerMethod(String listenerMethod) {
        this.listenerMethod = listenerMethod;
    }

    public String getListenerType() {
        return listenerType;
    }

    public void setListenerType(String listenerType) {
        this.listenerType = listenerType;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public List<String> getParamters() {
        return paramters;
    }

    public void setParamters(List<String> paramters) {
        this.paramters = paramters;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
