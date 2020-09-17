package com.zhy.inject_annotation.model;

import javax.lang.model.type.TypeMirror;

public class FieldBindingModel {

    private String fieldName;
    private TypeMirror fieldType;
    private int id;

    public FieldBindingModel(String fieldName, TypeMirror fieldType, int id) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public TypeMirror getFieldType() {
        return fieldType;
    }

    public void setFieldType(TypeMirror fieldType) {
        this.fieldType = fieldType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
