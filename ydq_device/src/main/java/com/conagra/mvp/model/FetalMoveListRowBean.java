package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by Dongqin.Ye on 2018/3/22.
 */

public class FetalMoveListRowBean {

    private String tb_FetalMovement_ID;
    private Object CustomerID;
    private Object HospitalID;
    private int ClickNumber;
    private String StartTime;
    private String State;
    private String CreateTime;
    private List<String> RecordTimePoint;

    public String getTb_FetalMovement_ID() {
        return tb_FetalMovement_ID;
    }

    public void setTb_FetalMovement_ID(String tb_FetalMovement_ID) {
        this.tb_FetalMovement_ID = tb_FetalMovement_ID;
    }

    public Object getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(Object CustomerID) {
        this.CustomerID = CustomerID;
    }

    public Object getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(Object HospitalID) {
        this.HospitalID = HospitalID;
    }

    public int getClickNumber() {
        return ClickNumber;
    }

    public void setClickNumber(int ClickNumber) {
        this.ClickNumber = ClickNumber;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public List<String> getRecordTimePoint() {
        return RecordTimePoint;
    }

    public void setRecordTimePoint(List<String> RecordTimePoint) {
        this.RecordTimePoint = RecordTimePoint;
    }
}
