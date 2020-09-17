package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/11/3.
 */

public class BloodSugarListBackModel implements Serializable {


    /**
     * CurrentPageIndex : 1
     * TotalPageCount : 1
     * TotalItemCount : 2
     * PageData : [{"LSH":"1a181dfb-0816-45a9-9d2c-56f9e7c40a1a","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","Category":"2","CategoryName":"午餐前","BloodSugarValue":1.5,"State":"0","StateName":"正常","RecordDateTime":"16:08","RecorderID":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","RecordDate":"2016-11-03T16:08:33.483","HospitalID":"001"},{"LSH":"280b2883-e428-4875-8f45-0a5ff101d004","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","Category":"0","CategoryName":"早餐前","BloodSugarValue":2.2,"State":"0","StateName":"正常","RecordDateTime":"16:06","RecorderID":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","RecordDate":"2016-11-03T16:06:55.337","HospitalID":"001"}]
     */

    private int CurrentPageIndex;
    private int TotalPageCount;
    private int TotalItemCount;
    /**
     * LSH : 1a181dfb-0816-45a9-9d2c-56f9e7c40a1a
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * Category : 2
     * CategoryName : 午餐前
     * BloodSugarValue : 1.5
     * State : 0
     * StateName : 正常
     * RecordDateTime : 16:08
     * RecorderID : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * RecordDate : 2016-11-03T16:08:33.483
     * HospitalID : 001
     */

    private List<PageDataBean> PageData;

    public int getCurrentPageIndex() {
        return CurrentPageIndex;
    }

    public void setCurrentPageIndex(int CurrentPageIndex) {
        this.CurrentPageIndex = CurrentPageIndex;
    }

    public int getTotalPageCount() {
        return TotalPageCount;
    }

    public void setTotalPageCount(int TotalPageCount) {
        this.TotalPageCount = TotalPageCount;
    }

    public int getTotalItemCount() {
        return TotalItemCount;
    }

    public void setTotalItemCount(int TotalItemCount) {
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
}
