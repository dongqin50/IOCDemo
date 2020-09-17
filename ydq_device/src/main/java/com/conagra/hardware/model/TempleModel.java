package com.conagra.hardware.model;

/**
 * Created by Dongqin.Ye on 2017/11/12.
 */

public class TempleModel  {

    /**
     * tb_Temperature_ID : b7dbbfca-b0d3-4e48-a8b0-98680e545398
     * CustomerID : 0154a9ea-06a2-4460-85fa-e394099faf62
     * HospitalID : HospitalID
     * TemperatureValue : 35.0
     * State : 0
     * CreateTime : 2017-11-12
     */

    private String tb_Temperature_ID;
    private String CustomerID;
    private String HospitalID;
    private double TemperatureValue;
    private int State;
    private String CreateTime;

    public String getTb_Temperature_ID() {
        return tb_Temperature_ID;
    }

    public void setTb_Temperature_ID(String tb_Temperature_ID) {
        this.tb_Temperature_ID = tb_Temperature_ID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String HospitalID) {
        this.HospitalID = HospitalID;
    }

    public double getTemperatureValue() {
        return TemperatureValue;
    }

    public void setTemperatureValue(double TemperatureValue) {
        this.TemperatureValue = TemperatureValue;
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
}
