package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.FetalMoveListModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealMoveListView extends BaseView {
    void renderAllData(FetalMoveListModel model, boolean update);
}
