package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.BloodSugarListModel;

/**
 * Created by yedongqin on 2017/10/29.
 */

public interface BloodSugarView extends BaseView{

    void isExist(String bloodSugarId, String value, int timeSlot, boolean isEdit);

    void renderAllData(BloodSugarListModel model);

    void createOrUpdateSuccess();

    void isExistDevice(boolean isExist, String deviceAddress);
    void addDeviceStatus(boolean success);
}
