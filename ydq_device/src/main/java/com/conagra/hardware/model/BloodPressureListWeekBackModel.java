package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/30.
 */

public class BloodPressureListWeekBackModel implements Serializable {


    /**
     * LSH : a22b5b6d-f658-46ed-bf78-cd5095db320e
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * SystolicPressure : 70
     * DiastolicPressure : 60
     * HeartRate : 0
     * State : 12
     * HospitalID : 001
     * RecorderID : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * RecordDate : 2016-10-30T18:32:57.687
     */

    private DataInfoBean data_Info;
    /**
     * UpRecordDate : null
     * DownRecordDate : null
     */

    private DataDateBean data_Date;
    /**
     * LSH : a22b5b6d-f658-46ed-bf78-cd5095db320e
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * SystolicPressure : 70
     * DiastolicPressure : 60
     * HeartRate : 0
     * State : 12
     * HospitalID : 001
     * RecorderID : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * RecordDate : 2016-10-30T18:32:57.687
     */

    private List<DataWeekBean> data_Week;

    public DataInfoBean getData_Info() {
        return data_Info;
    }

    public void setData_Info(DataInfoBean data_Info) {
        this.data_Info = data_Info;
    }

    public DataDateBean getData_Date() {
        return data_Date;
    }

    public void setData_Date(DataDateBean data_Date) {
        this.data_Date = data_Date;
    }

    public List<DataWeekBean> getData_Week() {
        return data_Week;
    }

    public void setData_Week(List<DataWeekBean> data_Week) {
        this.data_Week = data_Week;
    }

    public static class DataInfoBean {
        private String LSH;
        private String CustomerNo;
        private int SystolicPressure;
        private int DiastolicPressure;
        private int HeartRate;
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

    public static class DataDateBean {
        private Object UpRecordDate;
        private Object DownRecordDate;

        public Object getUpRecordDate() {
            return UpRecordDate;
        }

        public void setUpRecordDate(Object UpRecordDate) {
            this.UpRecordDate = UpRecordDate;
        }

        public Object getDownRecordDate() {
            return DownRecordDate;
        }

        public void setDownRecordDate(Object DownRecordDate) {
            this.DownRecordDate = DownRecordDate;
        }
    }

    public static class DataWeekBean {
        private String LSH;
        private String CustomerNo;
        private int SystolicPressure;
        private int DiastolicPressure;
        private int HeartRate;
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
