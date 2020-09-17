package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.BloodSugarListModel;

/**
 * Created by yedongqin on 2017/10/31.
 */

public interface BloodSugarListView extends BaseView {

    void renderAllData(BloodSugarListModel model, boolean update);
}
