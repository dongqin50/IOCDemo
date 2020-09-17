package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by yedongqin on 2018/3/10.
 */

public class BloodPressureListModel {

    /**
     * rows : [{"tb_BloodPressure_ID":"eb898b2d-7680-48cf-ac4a-d73685c7194b","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","SystolicPressure":69,"DiastolicPressure":57,"HeartRate":0,"State":12,"CreateTime":"2018-03-10 16:46:53","HospitalID":null}]
     * total : 1
     */

    private int total;
    private List<BloodPressureRowBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BloodPressureRowBean> getRows() {
        return rows;
    }

    public void setRows(List<BloodPressureRowBean> rows) {
        this.rows = rows;
    }

}
