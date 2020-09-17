package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/14.
 */
public class FetalMovementIntervalBackModel implements Serializable {

    /**
     * FetalMovementTime : 09:37
     */

    private List<DataMorningBean> data_Morning;
    /**
     * FetalMovementTime : 12:33
     */

    private List<DataAfternoonBean> data_Afternoon;
    private List<DataNightBean> data_Night;
    /**
     * data_CurrentDate : {"data_CurrentDate":"2016-11-28"}
     */

    private DataCurrentDateBean data_CurrentDate;

    public List<DataMorningBean> getData_Morning() {
        return data_Morning;
    }


    public void setData_Morning(List<DataMorningBean> data_Morning) {
        this.data_Morning = data_Morning;
    }

    public List<DataAfternoonBean> getData_Afternoon() {
        return data_Afternoon;
    }

    public void setData_Afternoon(List<DataAfternoonBean> data_Afternoon) {
        this.data_Afternoon = data_Afternoon;
    }

    public List<DataNightBean> getData_Night() {
        return data_Night;
    }

    public void setData_Night(List<DataNightBean> data_Night) {
        this.data_Night = data_Night;
    }

    public DataCurrentDateBean getData_CurrentDate() {
        return data_CurrentDate;
    }

    public void setData_CurrentDate(DataCurrentDateBean data_CurrentDate) {
        this.data_CurrentDate = data_CurrentDate;
    }

    public static class DataMorningBean {
        private String StartTime;
        private String FetalMovementTime;

        public String getFetalMovementTime() {
            return FetalMovementTime;
        }

        public void setFetalMovementTime(String FetalMovementTime) {
            this.FetalMovementTime = FetalMovementTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }
    }

    public static class DataAfternoonBean {
        private String FetalMovementTime;
        private String StartTime;
        public String getFetalMovementTime() {
            return FetalMovementTime;
        }

        public void setFetalMovementTime(String FetalMovementTime) {
            this.FetalMovementTime = FetalMovementTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
        }
    }

    public static class DataNightBean {
        private String FetalMovementTime;
        private String StartTime;
        public String getFetalMovementTime() {
            return FetalMovementTime;
        }

        public void setFetalMovementTime(String FetalMovementTime) {
            this.FetalMovementTime = FetalMovementTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String startTime) {
            StartTime = startTime;
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
}
