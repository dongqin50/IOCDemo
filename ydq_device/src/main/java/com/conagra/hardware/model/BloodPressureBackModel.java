package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/11/1.
 */

public class BloodPressureBackModel implements Serializable {


    /**
     * CurrentPageIndex : 1.0
     * TotalPageCount : 1.0
     * TotalItemCount : 3.0
     * PageData : [{"LSH":"abb2b7d1-53cd-46d5-8cad-6195b5e0a663","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","SystolicPressure":109,"DiastolicPressure":69,"HeartRate":85,"State":"0","HospitalID":"001","RecorderID":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","RecordDate":"2016-11-01T11:36:22.3"},{"LSH":"16cd8f71-900f-4687-91bf-42d928a41bf7","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","SystolicPressure":106,"DiastolicPressure":59,"HeartRate":65,"State":"0","HospitalID":"001","RecorderID":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","RecordDate":"2016-10-31T18:44:24.447"},{"LSH":"a22b5b6d-f658-46ed-bf78-cd5095db320e","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","SystolicPressure":70,"DiastolicPressure":60,"HeartRate":0,"State":"12","HospitalID":"001","RecorderID":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","RecordDate":"2016-10-30T18:32:57.687"}]
     */

    private double CurrentPageIndex;
    private double TotalPageCount;
    private double TotalItemCount;
    /**
     * LSH : abb2b7d1-53cd-46d5-8cad-6195b5e0a663
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * SystolicPressure : 109.0
     * DiastolicPressure : 69.0
     * HeartRate : 85.0
     * State : 0
     * HospitalID : 001
     * RecorderID : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * RecordDate : 2016-11-01T11:36:22.3
     */

    private List<PageDataBean> PageData;

    public double getCurrentPageIndex() {
        return CurrentPageIndex;
    }

    public void setCurrentPageIndex(double CurrentPageIndex) {
        this.CurrentPageIndex = CurrentPageIndex;
    }

    public double getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(double TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public double getTotalItemCount() {
        return TotalItemCount;
    }

    public void setTotalItemCount(double TotalItemCount) {
        this.TotalItemCount = TotalItemCount;
    }

    public List<PageDataBean> getPageData() {
        return PageData;
    }

    public void setPageData(List<PageDataBean> PageData) {
        this.PageData = PageData;
    }

    public static class PageDataBean {
        private String LSH;
        private String CustomerNo;
        private double SystolicPressure;
        private double DiastolicPressure;
        private double HeartRate;
        private String State;
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

        public double getSystolicPressure() {
            return SystolicPressure;
        }

        public void setSystolicPressure(double SystolicPressure) {
            this.SystolicPressure = SystolicPressure;
        }

        public double getDiastolicPressure() {
            return DiastolicPressure;
        }

        public void setDiastolicPressure(double DiastolicPressure) {
            this.DiastolicPressure = DiastolicPressure;
        }

        public double getHeartRate() {
            return HeartRate;
        }

        public void setHeartRate(double HeartRate) {
            this.HeartRate = HeartRate;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
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
}
