package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.FetalHeartModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealHeartView extends BaseView {

    void createOrUpdateSuccess(FetalHeartModel serverUrl);

    void isExistDevice(boolean isExist, String deviceAddress);
    void addDeviceStatus(boolean success);
}
