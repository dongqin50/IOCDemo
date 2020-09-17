package com.conagra.mvp.model;


import java.util.List;

/**
 * Created by yedongqin on 2018/3/14.
 */

public class FetalMoveListsModel {


    /**
     * rows : [{"tb_FetalMovement_ID":"c21aa646-9427-4f6b-aa27-bbced30932d2","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","SUMClickNumber":0,"RecordTimePointModel":[{"StartTime":"2018-03-11","RecordTimePoint":["2018-03-11 18:05:29"]}],"DateModel":[{"CreateTime":"2018-03-11","SUMClickNumber":0}]}]
     * total : 1
     */
    private int total;
    private List<FetalMoveListsRowBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FetalMoveListsRowBean> getRows() {
        return rows;
    }

    public void setRows(List<FetalMoveListsRowBean> rows) {
        this.rows = rows;
    }

}
