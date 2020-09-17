package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.TempleListModel;

import java.util.List;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface TempleListView extends BaseView {
    void renderAllData(List<TempleListModel> model, boolean update);
}
