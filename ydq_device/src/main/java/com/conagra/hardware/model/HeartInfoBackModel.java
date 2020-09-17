package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 2016/12/14.
 */

public class HeartInfoBackModel implements Serializable {


    /**
     * LSH : 0520a2de-f4bd-4aa5-b6b3-0ce4feccf7f6
     * CustomerNo : d53e60a9-da8e-4318-a58e-66d07499119c
     * Piont : 0,0,0,0,0,0,0,0,0,0,0,141,132,132,185,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
     * FetalHeartCode : 301ee8c6-f4a6-4180-bae5-f5f6a97eedb1
     * Category : 0
     * HospitalID : 0539SSL
     * RecorderID : d53e60a9-da8e-4318-a58e-66d07499119c
     * RecordDate : 2016-12-14 15:56:31
     */

    private String LSH;
    private String CustomerNo;
    private String Piont;
    private String FetalHeartCode;
    private String Category;
    private String HospitalID;
    private String RecorderID;
    private String RecordDate;

    public String getLSH() {
        return LSH;
    }

    public void setLSH(String LSH) {
        this.LSH = LSH;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String CustomerNo) {
        this.CustomerNo = CustomerNo;
    }

    public String getPiont() {
        return Piont;
    }

    public void setPiont(String Piont) {
        this.Piont = Piont;
    }

    public String getFetalHeartCode() {
        return FetalHeartCode;
    }

    public void setFetalHeartCode(String FetalHeartCode) {
        this.FetalHeartCode = FetalHeartCode;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getRecorderID() {
        return RecorderID;
    }

    public void setRecorderID(String RecorderID) {
        this.RecorderID = RecorderID;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String RecordDate) {
        this.RecordDate = RecordDate;
    }
}
