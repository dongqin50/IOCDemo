package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/11/3.
 */

public class BloodSugarTimeListBackModel implements Serializable {


    /**
     * data_Time : [{"LSH":"8fee9141-3170-4c87-b915-a988d29ec493","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"3","CategoryName":"午餐后","BloodSugarValue":6,"State":"2","StateName":"低血糖","RecordDateTime":"15:32","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 15:32:40","HospitalID":"0539SSL"},{"LSH":"a990776d-14c4-4382-afcc-b94a4963d121","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"2","CategoryName":"午餐前","BloodSugarValue":4.6,"State":"0","StateName":"正常","RecordDateTime":"15:32","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 15:32:20","HospitalID":"0539SSL"},{"LSH":"d16c37aa-24aa-4a95-8178-cf7a54f5aac4","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"0","CategoryName":"早餐前","BloodSugarValue":5.7,"State":"0","StateName":"正常","RecordDateTime":"10:30","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 10:30:34","HospitalID":"0539SSL"}]
     * data_Info : [{"LSH":"8fee9141-3170-4c87-b915-a988d29ec493","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"3","BloodSugarValue":6,"State":"2","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 15:32:40","HospitalID":"0539SSL"},{"LSH":"a990776d-14c4-4382-afcc-b94a4963d121","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"2","BloodSugarValue":4.6,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 15:32:20","HospitalID":"0539SSL"},{"LSH":"d16c37aa-24aa-4a95-8178-cf7a54f5aac4","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","Category":"0","BloodSugarValue":5.7,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","RecordDate":"2016-11-28 10:30:34","HospitalID":"0539SSL"}]
     * data_Date : {"UpRecordDate":"2016-11-25","DownRecordDate":"2016-12-02"}
     * data_CurrentDate : {"data_CurrentDate":"2016-11-28"}
     */

    private DataDateBean data_Date;
    private DataCurrentDateBean data_CurrentDate;
    private List<DataTimeBean> data_Time;
    private List<DataInfoBean> data_Info;

    public DataDateBean getData_Date() {
        return data_Date;
    }

    public void setData_Date(DataDateBean data_Date) {
        this.data_Date = data_Date;
    }

    public DataCurrentDateBean getData_CurrentDate() {
        return data_CurrentDate;
    }

    public void setData_CurrentDate(DataCurrentDateBean data_CurrentDate) {
        this.data_CurrentDate = data_CurrentDate;
    }

    public List<DataTimeBean> getData_Time() {
        return data_Time;
    }

    public void setData_Time(List<DataTimeBean> data_Time) {
        this.data_Time = data_Time;
    }

    public List<DataInfoBean> getData_Info() {
        return data_Info;
    }

    public void setData_Info(List<DataInfoBean> data_Info) {
        this.data_Info = data_Info;
    }

    public static class DataDateBean {
        /**
         * UpRecordDate : 2016-11-25
         * DownRecordDate : 2016-12-02
         */

        private String UpRecordDate;
        private String DownRecordDate;

        public String getUpRecordDate() {
            return UpRecordDate;
        }

        public void setUpRecordDate(String UpRecordDate) {
            this.UpRecordDate = UpRecordDate;
        }

        public String getDownRecordDate() {
            return DownRecordDate;
        }

        public void setDownRecordDate(String DownRecordDate) {
            this.DownRecordDate = DownRecordDate;
        }
    }

    public static class DataCurrentDateBean {
        /**
         * data_CurrentDate : 2016-11-28
         */

        private String data_CurrentDate;

        public String getData_CurrentDate() {
            return data_CurrentDate;
        }

        public void setData_CurrentDate(String data_CurrentDate) {
            this.data_CurrentDate = data_CurrentDate;
        }
    }

    public static class DataTimeBean {
        /**
         * LSH : 8fee9141-3170-4c87-b915-a988d29ec493
         * CustomerNo : d53e60a9-da8e-4318-a58e-66d07499119c
         * Category : 3
         * CategoryName : 午餐后
         * BloodSugarValue : 6.0
         * State : 2
         * StateName : 低血糖
         * RecordDateTime : 15:32
         * RecorderID : d53e60a9-da8e-4318-a58e-66d07499119c
         * RecordDate : 2016-11-28 15:32:40
         * HospitalID : 0539SSL
         */

        private String LSH;
        private String CustomerNo;
        private String Category;
        private String CategoryName;
        private double BloodSugarValue;
        private String State;
        private String StateName;
        private String RecordDateTime;
        private String RecorderID;
        private String RecordDate;
        private String HospitalID;

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

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String CategoryName) {
            this.CategoryName = CategoryName;
        }

        public double getBloodSugarValue() {
            return BloodSugarValue;
        }

        public void setBloodSugarValue(double BloodSugarValue) {
            this.BloodSugarValue = BloodSugarValue;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }

        public String getRecordDateTime() {
            return RecordDateTime;
        }

        public void setRecordDateTime(String RecordDateTime) {
            this.RecordDateTime = RecordDateTime;
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

        public String getHospitalID() {
            return HospitalID;
        }

        public void setHospitalID(String HospitalID) {
            this.HospitalID = HospitalID;
        }
    }

    public static class DataInfoBean {
        /**
         * LSH : 8fee9141-3170-4c87-b915-a988d29ec493
         * CustomerNo : d53e60a9-da8e-4318-a58e-66d07499119c
         * Category : 3
         * BloodSugarValue : 6.0
         * State : 2
         * RecorderID : d53e60a9-da8e-4318-a58e-66d07499119c
         * RecordDate : 2016-11-28 15:32:40
         * HospitalID : 0539SSL
         */

        private String LSH;
        private String CustomerNo;
        private String Category;
        private double BloodSugarValue;
        private String State;
        private String RecorderID;
        private String RecordDate;
        private String HospitalID;

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

        public String getCategory() {
            return Category;
        }

        public void setCategory(String Category) {
            this.Category = Category;
        }

        public double getBloodSugarValue() {
            return BloodSugarValue;
        }

        public void setBloodSugarValue(double BloodSugarValue) {
            this.BloodSugarValue = BloodSugarValue;
        }

        public String getState() {
            return State;
        }

        public void setState(String State) {
            this.State = State;
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

        public String getHospitalID() {
            return HospitalID;
        }

        public void setHospitalID(String HospitalID) {
            this.HospitalID = HospitalID;
        }
    }
}
