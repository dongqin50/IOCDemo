package com.conagra.mvp.presenter;

import com.conagra.mvp.ui.view.BaseView;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BasePresenter<T extends BaseView> {

    protected T view;

    public BasePresenter() {
    }

    public void registerView(T view){
        this.view = view;
    }

    public T getView(){
        return view;
    }
}
