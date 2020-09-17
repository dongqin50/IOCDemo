package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.BloodSugarListModel;

public interface BloodSugarDetailView extends BaseView {
    void renderAllData(BloodSugarListModel model);
}
