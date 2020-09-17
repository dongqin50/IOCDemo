package com.conagra.mvp.ui.view;

import com.conagra.mvp.model.FetalMoveListsModel;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealMoveView extends BaseView {


    void renderAllData(FetalMoveListsModel model);

    /**
     * 渲染某天数据
     */
    void renderOtherData();

    void createOrUpdateSuccess();
}
