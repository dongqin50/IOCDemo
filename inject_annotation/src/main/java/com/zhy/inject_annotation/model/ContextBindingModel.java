package com.zhy.inject_annotation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextBindingModel {

    private int layoutId;

    private Map<Integer,FieldBindingModel> fieldBindingModels = new HashMap<>();

    private List<EventListener> eventListeners = new ArrayList<>();

    public ContextBindingModel(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public Map<Integer,FieldBindingModel> getFieldBindingModels() {
        return fieldBindingModels;
    }

    public void setFieldBindingModels(Map<Integer,FieldBindingModel> fieldBindingModels) {
        this.fieldBindingModels = fieldBindingModels;
    }

    public List<EventListener> getEventListeners() {
        return eventListeners;
    }

    public void setEventListeners(List<EventListener> eventListeners) {
        this.eventListeners = eventListeners;
    }
}
