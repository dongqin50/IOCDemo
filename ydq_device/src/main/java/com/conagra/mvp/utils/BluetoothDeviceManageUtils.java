package com.conagra.mvp.utils;

import android.text.TextUtils;

/**
 * Created by Dongqin.Ye on 2018/6/16.
 */

public class BluetoothDeviceManageUtils {



    private static StringBuilder BLOOD_PRESURE_MAC_ADDRESS = new StringBuilder("");
    private static StringBuilder BLOOD_SUGER_MAC_ADDRESS = new StringBuilder("");
    private static StringBuilder TEMPLE_MAC_ADDRESS = new StringBuilder("");
    private static StringBuilder FETAL_HEART_MAC_ADDRESS = new StringBuilder("");



    public boolean isExistAboutBloodPresure(String mac) {
        if (!TextUtils.isEmpty(mac) && BLOOD_PRESURE_MAC_ADDRESS.toString().contains(mac)) {
            return true;
        }
        return false;
    }

    public boolean addBloodPresure(String mac) {
        if (!isExistAboutBloodPresure(mac)) {
            BLOOD_PRESURE_MAC_ADDRESS.append(mac + ",");
            return true;
        }
        return false;
    }


}
