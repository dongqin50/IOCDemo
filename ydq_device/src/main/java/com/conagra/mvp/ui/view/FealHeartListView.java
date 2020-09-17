package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.HeartListModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealHeartListView extends BaseView {
    void renderAllData(HeartListModel model, boolean update);
}
