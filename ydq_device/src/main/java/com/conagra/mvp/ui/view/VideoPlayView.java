package com.conagra.mvp.ui.view;

/**
 * Created by yedongqin on 2018/3/17.
 */

public interface VideoPlayView extends BaseView {


    void downloadSuccess(String path);

    void downloadError();
}
