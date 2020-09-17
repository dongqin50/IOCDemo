package com.conagra.hardware.model;

import android.support.annotation.DrawableRes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2016/12/4.
 */

public class JkgjListBackModel implements Serializable {


    private List<DataBean> Data;

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable {
        public DataBean() {
        }

        public DataBean(int iconResId, String title, String content) {
            this.iconResId = iconResId;
            this.title = title;
            this.content = content;
        }
        /**
         * HardwareCode : FETALHEART
         * CustomerNo : d53e60a9-da8e-4318-a58e-66d07499119c
         * LastRecordDate : 12  2 2016 11:57AM
         * LastValue : 0次/分钟
         * OrderIndex : 2
         */
        private
        @DrawableRes
        int iconResId;
        private String title;
        private String content;
        private String HardwareCode;
        private String CustomerNo;
        private String LastRecordDate;
        private String LastValue;
        private int OrderIndex;

        public int getIconResId() {
            return iconResId;
        }

        public void setIconResId(int iconResId) {
            this.iconResId = iconResId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHardwareCode() {
            return HardwareCode;
        }

        public void setHardwareCode(String HardwareCode) {
            this.HardwareCode = HardwareCode;
        }

        public String getCustomerNo() {
            return CustomerNo;
        }

        public void setCustomerNo(String CustomerNo) {
            this.CustomerNo = CustomerNo;
        }

        public String getLastRecordDate() {
            return LastRecordDate;
        }

        public void setLastRecordDate(String LastRecordDate) {
            this.LastRecordDate = LastRecordDate;
        }

        public String getLastValue() {
            return LastValue;
        }

        public void setLastValue(String LastValue) {
            this.LastValue = LastValue;
        }

        public int getOrderIndex() {
            return OrderIndex;
        }

        public void setOrderIndex(int OrderIndex) {
            this.OrderIndex = OrderIndex;
        }
    }
}
