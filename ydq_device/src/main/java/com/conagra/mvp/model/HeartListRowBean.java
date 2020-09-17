package com.conagra.mvp.model;

import java.io.Serializable;

/**
 * Created by Dongqin.Ye on 2018/3/22.
 */

public class HeartListRowBean  implements Serializable {
    /**
     * tb_FetalHeart_ID : 4878e544-1336-474e-b5f8-f44d0ebf6606
     * CustomerID : 0154a9ea-06a2-4460-85fa-e394099faf62
     * HospitalID : null
     * StartTime : 2018-03-18 21:02:11
     * Times : 51
     * AvgHeartRate : 0
     * PiontValue : 154,178,117,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,126,130,129,124,118,113,100,100,97,61,64,71,69,68,67,75,77
     * PiontUterine : 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
     * AudioFileName : 1521378131890.wav
     * AudiofileSize : 187644
     * AudioURL : upload/Audio/FetalHeart/2018/3/18/20180318090309.wav
     * CreateTime : 2018-03-18 21:03:09
     */

    private String tb_FetalHeart_ID;
    private String CustomerID;
    private Object HospitalID;
    private String StartTime;
    private int Times;
    private int AvgHeartRate;
    private String PiontValue;
    private String PiontUterine;
    private String AudioFileName;
    private int AudiofileSize;
    private String AudioURL;
    private String CreateTime;

    public String getTb_FetalHeart_ID() {
        return tb_FetalHeart_ID;
    }

    public void setTb_FetalHeart_ID(String tb_FetalHeart_ID) {
        this.tb_FetalHeart_ID = tb_FetalHeart_ID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public Object getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(Object HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public int getTimes() {
        return Times;
    }

    public void setTimes(int Times) {
        this.Times = Times;
    }

    public int getAvgHeartRate() {
        return AvgHeartRate;
    }

    public void setAvgHeartRate(int AvgHeartRate) {
        this.AvgHeartRate = AvgHeartRate;
    }

    public String getPiontValue() {
        return PiontValue;
    }

    public void setPiontValue(String PiontValue) {
        this.PiontValue = PiontValue;
    }

    public String getPiontUterine() {
        return PiontUterine;
    }

    public void setPiontUterine(String PiontUterine) {
        this.PiontUterine = PiontUterine;
    }

    public String getAudioFileName() {
        return AudioFileName;
    }

    public void setAudioFileName(String AudioFileName) {
        this.AudioFileName = AudioFileName;
    }

    public int getAudiofileSize() {
        return AudiofileSize;
    }

    public void setAudiofileSize(int AudiofileSize) {
        this.AudiofileSize = AudiofileSize;
    }

    public String getAudioURL() {
        return AudioURL;
    }

    public void setAudioURL(String AudioURL) {
        this.AudioURL = AudioURL;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
