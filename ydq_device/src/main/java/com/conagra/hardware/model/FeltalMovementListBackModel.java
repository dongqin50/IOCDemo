package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/14.
 *
 * 胎动列表请求返回数据
 */
public class FeltalMovementListBackModel implements Serializable {


    /**
     * CurrentPageIndex : 1
     * TotalPageCount : 1
     * TotalItemCount : 3
     * PageData : [{"LSH":"4DCA91DE-2C99-47F8-81E1-3A64B6881588","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","FetalMovementDateTime":"2016-10-14T11:36:01.45","FetalMovementDate":"2016-10-14T00:00:00","FetalMovementTime":"","FetalMovementNumberDay":10,"TwelveHour":40,"Morning":0,"Afternoon":0,"Night":0,"FetalMovementState":"良好"},{"LSH":"C4C9FD30-760D-4F35-ADB7-5F7C64B7B7A6","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","FetalMovementDateTime":"2016-10-14T11:36:01.45","FetalMovementDate":"2016-10-13T00:00:00","FetalMovementTime":"","FetalMovementNumberDay":2,"TwelveHour":8,"Morning":0,"Afternoon":0,"Night":0,"FetalMovementState":"宫内缺氧"},{"LSH":"5D46EF23-9AC5-4D23-A1A6-D0CF6FED0B5E","CustomerNo":"00045ee4-fb55-4f7f-a3f1-a81776f36e14","FetalMovementDateTime":"2016-10-14T11:36:01.45","FetalMovementDate":"2016-10-12T00:00:00","FetalMovementTime":"","FetalMovementNumberDay":24,"TwelveHour":96,"Morning":0,"Afternoon":0,"Night":0,"FetalMovementState":"良好"}]
     */

    private int CurrentPageIndex;
    private int TotalPageCount;
    private int TotalItemCount;
    /**
     * LSH : 4DCA91DE-2C99-47F8-81E1-3A64B6881588
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * FetalMovementDateTime : 2016-10-14T11:36:01.45
     * FetalMovementDate : 2016-10-14T00:00:00
     * FetalMovementTime :
     * FetalMovementNumberDay : 10
     * TwelveHour : 40
     * Morning : 0
     * Afternoon : 0
     * Night : 0
     * FetalMovementState : 良好
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
        private String FetalMovementDateTime;
        private String FetalMovementDate;
        private String FetalMovementTime;
        private int FetalMovementNumberDay;
        private int TwelveHour;
        private int Morning;
        private int Afternoon;
        private int Night;
        private String FetalMovementState;
        private String StartTime;

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

        public String getFetalMovementTime() {
            return FetalMovementTime;
        }

        public void setFetalMovementTime(String FetalMovementTime) {
            this.FetalMovementTime = FetalMovementTime;
        }

        public int getFetalMovementNumberDay() {
            return FetalMovementNumberDay;
        }

        public void setFetalMovementNumberDay(int FetalMovementNumberDay) {
            this.FetalMovementNumberDay = FetalMovementNumberDay;
        }

        public int getTwelveHour() {
            return TwelveHour;
        }

        public void setTwelveHour(int TwelveHour) {
            this.TwelveHour = TwelveHour;
        }

        public int getMorning() {
            return Morning;
        }

        public void setMorning(int Morning) {
            this.Morning = Morning;
        }

        public int getAfternoon() {
            return Afternoon;
        }

        public void setAfternoon(int Afternoon) {
            this.Afternoon = Afternoon;
        }

        public int getNight() {
            return Night;
        }

        public void setNight(int Night) {
            this.Night = Night;
        }

        public String getFetalMovementState() {
            return FetalMovementState;
        }

        public void setFetalMovementState(String FetalMovementState) {
            this.FetalMovementState = FetalMovementState;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }
    }
}
