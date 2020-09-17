package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 2016/11/3.
 */

public class BloodSugarModel implements Serializable {



    /**
     * 3.9.2添加血糖
     * @param CustomerNo  客户编号
     * @param Category         类别
     * @param BloodSugarValue  值
     * @param State 状态    0：正常   1：高血糖   2：低血糖
     * @param RecorderID 操作人代码
     * @param HospitalID   医院编码
     * @param isDelete   是否清空       0：不清空     1：清空
     * @param appId
     * @param sign
     *
     * /// <summary>
     *
     * 血糖
     * 正常血糖  餐前2.8~6.2		餐后7.8~9.0		睡前3.9~6.2
     * 高血糖    餐前>=6.3		    餐后 >=9.1		睡前>=6.3
     * 低血糖    餐前<=2.7		    餐后 <=7.7		睡前<=2.7
     * Category值
     *					0：早餐前	1：早餐后
     *					2：午餐前	3：午餐后
     *					4：晚餐前	5：晚餐后
     *					6：睡觉前
     * </summary>
     *
     * @return
     */
    
    private String CustomerNo;
    private String Category;
    private String BloodSugarValue;
    private String State;
    private String RecorderID;
    private String HospitalID;
    private String isDelete;

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getBloodSugarValue() {
        return BloodSugarValue;
    }

    public void setBloodSugarValue(String bloodSugarValue) {
        BloodSugarValue = bloodSugarValue;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getRecorderID() {
        return RecorderID;
    }

    public void setRecorderID(String recorderID) {
        RecorderID = recorderID;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

}
