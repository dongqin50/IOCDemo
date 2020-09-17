package com.conagra.mvp.model;

/**
 * Created by Dongqin.Ye on 2018/3/22.
 */

public class BloodPressureRowBean{

    /**
     * tb_BloodPressure_ID : eb898b2d-7680-48cf-ac4a-d73685c7194b
     * CustomerID : 0154a9ea-06a2-4460-85fa-e394099faf62
     * SystolicPressure : 69
     * DiastolicPressure : 57
     * HeartRate : 0
     * State : 12
     * CreateTime : 2018-03-10 16:46:53
     * HospitalID : null
     */

    private String tb_BloodPressure_ID;
    private String CustomerID;
    private int SystolicPressure;
    private int DiastolicPressure;
    private int HeartRate;
    private int State;
    private String CreateTime;
    private Object HospitalID;

    public String getTb_BloodPressure_ID() {
        return tb_BloodPressure_ID;
    }

    public void setTb_BloodPressure_ID(String tb_BloodPressure_ID) {
        this.tb_BloodPressure_ID = tb_BloodPressure_ID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getSystolicPressure() {
        return SystolicPressure;
    }

    public void setSystolicPressure(int SystolicPressure) {
        this.SystolicPressure = SystolicPressure;
    }

    public int getDiastolicPressure() {
        return DiastolicPressure;
    }

    public void setDiastolicPressure(int DiastolicPressure) {
        this.DiastolicPressure = DiastolicPressure;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int HeartRate) {
        this.HeartRate = HeartRate;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public Object getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(Object HospitalID) {
        this.HospitalID = HospitalID;
    }
}
