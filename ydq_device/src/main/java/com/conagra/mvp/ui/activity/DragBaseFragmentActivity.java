package com.conagra.mvp.ui.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.conagra.LocalConfig;
import com.conagra.di.component.ActivityComponent;
import com.conagra.mvp.presenter.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public abstract class DragBaseFragmentActivity
        <T extends ViewDataBinding,
         P extends BasePresenter,
         C extends ActivityComponent> extends BaseFragmentActivity<T> {


    protected C mComponent;
    protected P mPresenter;

    @Inject
    public LocalConfig mLocalConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();

    }

    protected abstract void initializeInjector();

    public C getComponent() {
        return mComponent;
    }

    public void setComponent(C mComponent) {
        this.mComponent = mComponent;
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Inject
    public void setPresenter(P mPresenter) {
        this.mPresenter = mPresenter;
    }


}
