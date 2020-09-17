package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/20.
 */

public class FetalMovementListBakeModel implements Serializable {

    private DataInfoBean data_Info;
    private List<DataWeekBean> data_Week;

    public DataInfoBean getData_Info() {
        return data_Info;
    }

    public void setData_Info(DataInfoBean data_Info) {
        this.data_Info = data_Info;
    }

    public List<DataWeekBean> getData_Week() {
        return data_Week;
    }

    public void setData_Week(List<DataWeekBean> data_Week) {
        this.data_Week = data_Week;
    }


    /**
     * UpFetalMovementDate : 2016-10-12
     */

    private DataDateBean data_Date;
    /**
     * LSH : A091CBBF-09CB-4AC1-8C73-9C51D8640622
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * FetalMovementDateTime : 2016-10-21T10:27:05.69
     * FetalMovementDate : 2016-10-17T00:00:00
     * StartTime : 2016-10-17T09:45:09
     * FetalMovementTime :
     * FetalMovementNumberDay : 1.0
     * TwelveHour : 4.0
     * Morning : 0.0
     * Afternoon : 0.0
     * Night : 0.0
     * FetalMovementState :
     */

    /**
     * UpFetalMovementDate : 2016-10-12
     * DownFetalMovementDate : null
     */
//
    public DataDateBean getData_Date() {
        return data_Date;
    }

    public void setData_Date(DataDateBean data_Date) {
        this.data_Date = data_Date;
    }


    public static class DataDateBean {
        private String UpFetalMovementDate;

        public String getUpFetalMovementDate() {
            return UpFetalMovementDate;
        }

        public void setUpFetalMovementDate(String UpFetalMovementDate) {
            this.UpFetalMovementDate = UpFetalMovementDate;
        }

        private String  DownFetalMovementDate;

        public String getDownFetalMovementDate() {
            return DownFetalMovementDate;
        }

        public void setDownFetalMovementDate(String downFetalMovementDate) {
            DownFetalMovementDate = downFetalMovementDate;
        }
    }


    public static class DataInfoBean {
        private String LSH;
        private String CustomerNo;
        private String FetalMovementDateTime;
        private String FetalMovementDate;
        private String StartTime;
        private String FetalMovementTime;
        private int FetalMovementNumberDay;
        private int TwelveHour;
        private int Morning;
        private int Afternoon;
        private int Night;
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
    }

    public static class DataWeekBean {
        private String LSH;
        private String CustomerNo;
        private String FetalMovementDateTime;
        private String FetalMovementDate;
        private Object StartTime;
        private String FetalMovementTime;
        private int FetalMovementNumberDay;
        private int TwelveHour;
        private int Morning;
        private int Afternoon;
        private int Night;
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

        public Object getStartTime() {
            return StartTime;
        }

        public void setStartTime(Object StartTime) {
            this.StartTime = StartTime;
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
    }
}
