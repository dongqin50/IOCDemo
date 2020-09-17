package com.conagra.mvp.model;

/**
 * Created by yedongqin on 2017/10/31.
 */

public class BloodSugarListRowModel {
    /**
     * tb_BloodSugar_ID : 71fd5148-4e9b-4bec-b500-d101fa5219a6
     * CustomerID : 0154a9ea-06a2-4460-85fa-e394099faf62
     * TimeSlot : 0
     * BloodSugarValue : 1.5
     * BloodSugarClass : 低
     * CreateTime : 2017-10-31
     */

    private String tb_BloodSugar_ID;
    private String CustomerID;
    private int TimeSlot;
    private double BloodSugarValue;
    private String BloodSugarClass;
    private String CreateTime;

    public String getTb_BloodSugar_ID() {
        return tb_BloodSugar_ID;
    }

    public void setTb_BloodSugar_ID(String tb_BloodSugar_ID) {
        this.tb_BloodSugar_ID = tb_BloodSugar_ID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(int TimeSlot) {
        this.TimeSlot = TimeSlot;
    }

    public double getBloodSugarValue() {
        return BloodSugarValue;
    }

    public void setBloodSugarValue(double BloodSugarValue) {
        this.BloodSugarValue = BloodSugarValue;
    }

    public String getBloodSugarClass() {
        return BloodSugarClass;
    }

    public void setBloodSugarClass(String BloodSugarClass) {
        this.BloodSugarClass = BloodSugarClass;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
