package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by Dongqin.Ye on 2018/3/22.
 */

public class FetalMoveListsRowBean {

    /**
     * tb_FetalMovement_ID : c21aa646-9427-4f6b-aa27-bbced30932d2
     * CustomerID : 0154a9ea-06a2-4460-85fa-e394099faf62
     * SUMClickNumber : 0
     * RecordTimePointModel : [{"StartTime":"2018-03-11","RecordTimePoint":["2018-03-11 18:05:29"]}]
     * DateModel : [{"CreateTime":"2018-03-11","SUMClickNumber":0}]
     */

    private String tb_FetalMovement_ID;
    private String CustomerID;
    private int SUMClickNumber;
    private List<RecordTimePointModelBean> RecordTimePointModel;
    private List<DateModelBean> DateModel;

    public String getTb_FetalMovement_ID() {
        return tb_FetalMovement_ID;
    }

    public void setTb_FetalMovement_ID(String tb_FetalMovement_ID) {
        this.tb_FetalMovement_ID = tb_FetalMovement_ID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public int getSUMClickNumber() {
        return SUMClickNumber;
    }

    public void setSUMClickNumber(int SUMClickNumber) {
        this.SUMClickNumber = SUMClickNumber;
    }

    public List<RecordTimePointModelBean> getRecordTimePointModel() {
        return RecordTimePointModel;
    }

    public void setRecordTimePointModel(List<RecordTimePointModelBean> RecordTimePointModel) {
        this.RecordTimePointModel = RecordTimePointModel;
    }

    public List<DateModelBean> getDateModel() {
        return DateModel;
    }

    public void setDateModel(List<DateModelBean> DateModel) {
        this.DateModel = DateModel;
    }

}
