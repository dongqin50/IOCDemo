package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/23.
 */

public class LiSiBackBean implements Serializable {


    /**
     * CurrentPageIndex : 10.0
     * TotalPageCount : 23.0
     * TotalItemCount : 23.0
     * PageData : [{"LSH":"DF8D5EC3-109D-4948-BC91-E9563F1A9575","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","FetalMovementDateTime":"2016-10-23T21:46:19.967","FetalMovementDate":"2016-10-22T00:00:00","StartTime":"2016-10-22T12:42:45","FetalMovementTime":"","FetalMovementNumberDay":1,"TwelveHour":4,"Morning":0,"Afternoon":0,"Night":0,"FetalMovementState":"宫内缺氧"}]
     */

    private double CurrentPageIndex;
    private double TotalPageCount;
    private double TotalItemCount;
    /**
     * LSH : DF8D5EC3-109D-4948-BC91-E9563F1A9575
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * FetalMovementDateTime : 2016-10-23T21:46:19.967
     * FetalMovementDate : 2016-10-22T00:00:00
     * StartTime : 2016-10-22T12:42:45
     * FetalMovementTime :
     * FetalMovementNumberDay : 1.0
     * TwelveHour : 4.0
     * Morning : 0.0
     * Afternoon : 0.0
     * Night : 0.0
     * FetalMovementState : 宫内缺氧
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

    public  class PageDataBean {
        private String LSH;
        private String CustomerNo;
        private String FetalMovementDateTime;
        private String FetalMovementDate;
        private String StartTime;
        private String FetalMovementTime;
        private double FetalMovementNumberDay;
        private double TwelveHour;
        private double Morning;
        private double Afternoon;
        private double Night;
        private String FetalMovementState;

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

        public String getFetalMovementDateTime() {
            return FetalMovementDateTime;
        }

        public void setFetalMovementDateTime(String FetalMovementDateTime) {
            this.FetalMovementDateTime = FetalMovementDateTime;
        }

        public String getFetalMovementDate() {
            return FetalMovementDate;
        }

        public void setFetalMovementDate(String FetalMovementDate) {
            this.FetalMovementDate = FetalMovementDate;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getFetalMovementTime() {
            return FetalMovementTime;
        }

        public void setFetalMovementTime(String FetalMovementTime) {
            this.FetalMovementTime = FetalMovementTime;
        }

        public double getFetalMovementNumberDay() {
            return FetalMovementNumberDay;
        }

        public void setFetalMovementNumberDay(double FetalMovementNumberDay) {
            this.FetalMovementNumberDay = FetalMovementNumberDay;
        }

        public double getTwelveHour() {
            return TwelveHour;
        }

        public void setTwelveHour(double TwelveHour) {
            this.TwelveHour = TwelveHour;
        }

        public double getMorning() {
            return Morning;
        }

        public void setMorning(double Morning) {
            this.Morning = Morning;
        }

        public double getAfternoon() {
            return Afternoon;
        }

        public void setAfternoon(double Afternoon) {
            this.Afternoon = Afternoon;
        }

        public double getNight() {
            return Night;
        }

        public void setNight(double Night) {
            this.Night = Night;
        }

        public String getFetalMovementState() {
            return FetalMovementState;
        }

        public void setFetalMovementState(String FetalMovementState) {
            this.FetalMovementState = FetalMovementState;
        }
    }
}
