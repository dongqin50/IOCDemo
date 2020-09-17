package com.conagra.mvp.ui.view;

import com.conagra.hardware.model.BloodPressureModel;
import com.conagra.mvp.model.BloodPressureListModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface BloodPressureView extends BaseView {
    void isExist(BloodPressureModel model, boolean isEdit);

    void renderAllData(BloodPressureListModel model);

    void createOrUpdateSuccess();

    void isExistDevice(boolean isExist, String deviceAddress);
    void addDeviceStatus(boolean success);
}
