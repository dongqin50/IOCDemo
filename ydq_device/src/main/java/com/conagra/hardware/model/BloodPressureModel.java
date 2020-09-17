package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 2016/10/30.
 * 血压
 */

public class BloodPressureModel implements Serializable {

    /**
     * 3.9.2添加血压
     * @param CustomerNo  客户编号
     * @param SystolicPressure         收缩压
     * @param DiastolicPressure  舒张压
     * @param HeartRate  心率
     * @param State 状态
     * @param RecorderID 操作人代码
     * @param HospitalID   医院编码
     * @param isDelete   是否清空       0：不清空     1：清空
     * @return
     */

    private String CustomerNo;
    private int SystolicPressure;
    private int DiastolicPressure;
    private int HeartRate;
    private String State;
    private String RecorderID;
    private String HospitalID;
    private String isDelete;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public int getSystolicPressure() {
        return SystolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        SystolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return DiastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        DiastolicPressure = diastolicPressure;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        HeartRate = heartRate;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getRecorderID() {
        return RecorderID;
    }

    public void setRecorderID(String recorderID) {
        RecorderID = recorderID;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
