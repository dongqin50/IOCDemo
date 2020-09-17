package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.BloodPressureListModel;

public interface BloodPressureDetailView extends BaseView{

    void renderAllData(BloodPressureListModel model);
}
