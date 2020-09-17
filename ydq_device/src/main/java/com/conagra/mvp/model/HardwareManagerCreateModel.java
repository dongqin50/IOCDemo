package com.conagra.mvp.model;

/**
 * Created by yedongqin on 2018/7/5.
 */

public class HardwareManagerCreateModel {


    /**
     * tb_DeviceID : 6ee333f2-4b57-47d0-95a1-d548cff236c0
     * Type : 2
     * MacAddress : 88:1B:99:0C:DE:D3
     * Name : Fealheart_1530720578020
     * CreatorID : 19bee3c0-1275-4006-b736-e6f48ab26e03
     * CreatorName : null
     * HospitalID : null
     * CreateTime : 2018-07-05
     */

    private String tb_DeviceID;
    private int Type;
    private String MacAddress;
    private String Name;
    private String CreatorID;
    private Object CreatorName;
    private Object HospitalID;
    private String CreateTime;

    public String getTb_DeviceID() {
        return tb_DeviceID;
    }

    public void setTb_DeviceID(String tb_DeviceID) {
        this.tb_DeviceID = tb_DeviceID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String MacAddress) {
        this.MacAddress = MacAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCreatorID() {
        return CreatorID;
    }

    public void setCreatorID(String CreatorID) {
        this.CreatorID = CreatorID;
    }

    public Object getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(Object CreatorName) {
        this.CreatorName = CreatorName;
    }

    public Object getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(Object HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }
}
