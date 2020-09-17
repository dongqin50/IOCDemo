package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.BloodPressureListModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface BloodPressureListView extends BaseView {

    void renderAllData(BloodPressureListModel model, boolean update);
}
