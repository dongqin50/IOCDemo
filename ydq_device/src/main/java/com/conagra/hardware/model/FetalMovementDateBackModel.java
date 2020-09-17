package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 2016/10/14.
 */
public class FetalMovementDateBackModel implements Serializable {


    /**
     * LSH : 6C8063DD-4A34-459E-B0FE-F7A99D1EAE79
     * CustomerNo : 00045ee4-fb55-4f7f-a3f1-a81776f36e14
     * FetalMovementDateTime : 2016-10-14T11:19:59.39
     * FetalMovementDate : 2016-10-14T00:00:00
     * FetalMovementTime :
     * FetalMovementNumberDay : 10
     * TwelveHour : 40
     * Morning : 10
     * Afternoon : 0
     * Night : 0
     * FetalMovementState : 良好
     */

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
}
