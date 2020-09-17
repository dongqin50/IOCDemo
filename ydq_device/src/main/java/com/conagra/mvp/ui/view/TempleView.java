package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.TempleListModel;

import java.util.List;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface TempleView extends BaseView {

    void isExist(boolean isEdit);

    void renderAllData(List<TempleListModel> model);

    void createOrUpdateSuccess();

    void isExistDevice(boolean isExist, String deviceAddress);
    void addDeviceStatus(boolean success);
}
