package com.conagra.mvp.model;

/**
 * Created by yedongqin on 2018/3/10.
 */

public class TempleListModel {

        /**
         * tb_Temperature_ID : 357fa58e-5a11-4ff4-9832-59faf7d630ec
         * TemperatureValue : 35.0
         * State : 0
         * CreateTime : 2017-11-12
         */

        private String tb_Temperature_ID;
        private double TemperatureValue;
        private int State;
        private String CreateTime;

        public String getTb_Temperature_ID() {
            return tb_Temperature_ID;
        }

        public void setTb_Temperature_ID(String tb_Temperature_ID) {
            this.tb_Temperature_ID = tb_Temperature_ID;
        }

        public double getTemperatureValue() {
            return TemperatureValue;
        }

        public void setTemperatureValue(double TemperatureValue) {
            this.TemperatureValue = TemperatureValue;
        }

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }
}
