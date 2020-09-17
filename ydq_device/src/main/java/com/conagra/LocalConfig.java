package com.conagra;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yedongqin on 2017/10/31.
 */

public class LocalConfig {

    private static SharedPreferences mSf = null;
    private static final String SESSION = "Session";
    private static final String DEVICE_FETALHEART = "fetalheart";   //胎心
    private static final String DEVICE_BLOOD_SUGAR = "bloodSugar";  //血糖
    private static final String DEVICE_BLOOD_PRESSURE = "bloodPressure";    //血压
    private static final String DEVICE_TEMPLE = "temple";   //温度


    public LocalConfig(Context c) {
        mSf = c.getSharedPreferences(SESSION, Context.MODE_PRIVATE);
    }

    public void setFetalHeartDevices(String devicesAddress) {
        SharedPreferences.Editor editor = mSf.edit();
        editor.putString(DEVICE_FETALHEART, devicesAddress);
        editor.commit();
    }
    public void setBloodSugarDevices(String devicesAddress) {
        SharedPreferences.Editor editor = mSf.edit();
        editor.putString(DEVICE_BLOOD_SUGAR, devicesAddress);
        editor.commit();
    }

    public void setBloodPressureDevices(String devicesAddress) {
        SharedPreferences.Editor editor = mSf.edit();
        editor.putString(DEVICE_BLOOD_PRESSURE, devicesAddress);
        editor.commit();
    }
    public void setTempleDevices(String devicesAddress) {
        SharedPreferences.Editor editor = mSf.edit();
        editor.putString(DEVICE_TEMPLE, devicesAddress);
        editor.commit();
    }

    public String getFetalHeartDevices() {
        return mSf.getString(DEVICE_FETALHEART, "");
    }

    public String getBloodSugarDevices() {
        return mSf.getString(DEVICE_BLOOD_SUGAR, "");
    }

    public String getBloodPressureDevices() {
        return mSf.getString(DEVICE_BLOOD_PRESSURE, "");
    }

    public String getTempleDevices() {
        return mSf.getString(DEVICE_TEMPLE, "");
    }

}
