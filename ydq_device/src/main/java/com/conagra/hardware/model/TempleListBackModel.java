package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/12/2.
 */

public class TempleListBackModel implements Serializable {


    /**
     * CurrentPageIndex : 1.0
     * TotalPageCount : 1.0
     * TotalItemCount : 9.0
     * PageData : [{"LSH":"6426af9c-c665-4664-a167-8fa9a937c2b2","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":38.7,"State":"1","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:35:31"},{"LSH":"4de23e17-bf7c-45dc-b66c-441b5e651c65","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":36.2,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:06:24"},{"LSH":"55d5124c-80cc-4603-a16c-5b1f00e00c9c","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":36.2,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:06:20"},{"LSH":"a351e98e-b3e5-4a34-954d-3cb9936f3555","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":36.2,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:06:15"},{"LSH":"f9930bdf-dd66-4dfd-a184-50e3ea077008","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":38.2,"State":"1","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:00:38"},{"LSH":"2830716a-13f3-4ff5-8612-094330cc56ab","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":38.2,"State":"1","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:00:35"},{"LSH":"3890a9e3-0f46-4ffb-98ae-65de50010ece","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":38.2,"State":"1","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 16:00:29"},{"LSH":"d70ffe16-1903-4448-b1eb-5213ba689bda","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":35.9,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 15:59:16"},{"LSH":"5988ce42-2a30-4690-a268-e40ce470e4e9","CustomerNo":"d53e60a9-da8e-4318-a58e-66d07499119c","TemperatureValue":35,"State":"0","RecorderID":"d53e60a9-da8e-4318-a58e-66d07499119c","HospitalID":"0539SSL","RecordDate":"2016-12-02 15:44:31"}]
     */

    private double CurrentPageIndex;
    private double TotalPageCount;
    private double TotalItemCount;
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
        /**
         * LSH : 6426af9c-c665-4664-a167-8fa9a937c2b2
         * CustomerNo : d53e60a9-da8e-4318-a58e-66d07499119c
         * TemperatureValue : 38.7
         * State : 1
         * RecorderID : d53e60a9-da8e-4318-a58e-66d07499119c
         * HospitalID : 0539SSL
         * RecordDate : 2016-12-02 16:35:31
         */

        private String LSH;
        private String CustomerNo;
        private double TemperatureValue;
        private String State;
        private String RecorderID;
        private String HospitalID;
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

        public double getTemperatureValue() {
            return TemperatureValue;
        }

        public void setTemperatureValue(double TemperatureValue) {
            this.TemperatureValue = TemperatureValue;
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

        public String getHospitalID() {
            return HospitalID;
        }

        public void setHospitalID(String HospitalID) {
            this.HospitalID = HospitalID;
        }

        public String getRecordDate() {
            return RecordDate;
        }

        public void setRecordDate(String RecordDate) {
            this.RecordDate = RecordDate;
        }
    }
}
