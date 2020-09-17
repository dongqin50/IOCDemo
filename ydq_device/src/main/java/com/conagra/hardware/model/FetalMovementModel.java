package com.conagra.hardware.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 16/10/8.
 */
public class FetalMovementModel implements Serializable {

    private String HospitalID; //       医院编码

    private String CustomerNo; // 客户编号

    private int ClickNumber; // 点击次数(一个批次的总点击次数)
    private List<String> FetalMovementTimes; // 记录时间点（胎动时间点）,数组
    private String StartTime;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String hospitalID) {
        HospitalID = hospitalID;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String customerNo) {
        CustomerNo = customerNo;
    }

    public int getClickNumber() {
        return ClickNumber;
    }

    public void setClickNumber(int clickNumber) {
        ClickNumber = clickNumber;
    }

    public String getFetalMovementTimes() {
        StringBuilder sb = new StringBuilder();
        if(FetalMovementTimes != null){
            for(int i = 0 ; i < FetalMovementTimes.size(); i++){
                sb.append(FetalMovementTimes.get(i) + ",");
            }

        }
        return sb.length() > 0?sb.substring(0,sb.length()-1):"";
    }

    public void setFetalMovementTimes(List<String> fetalMovementTimes) {
        FetalMovementTimes = fetalMovementTimes;
    }
}
