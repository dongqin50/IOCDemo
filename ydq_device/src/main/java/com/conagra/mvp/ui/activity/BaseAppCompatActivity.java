package com.conagra.mvp.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conagra.MainApplication;
import com.conagra.R;
import com.conagra.databinding.ActivityAppcompatBaseBinding;
import com.conagra.di.component.ApplicationComponent;
import com.conagra.di.module.ActivityModule;
import com.conagra.mvp.ui.view.BaseView;
import com.conagra.mvp.utils.ActivityUtils;
import com.xiaoye.myutils.utils.DialogManagerUtils;

/**
 * Created by yedongqin on 2017/10/4.
 */

public class BaseAppCompatActivity<T extends ViewDataBinding> extends AppCompatActivity implements BaseView {
    protected ActivityAppcompatBaseBinding mActivityBaseBinding;
    public T mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.setWindowStatusBarColor(this, R.color.colorMain);
        mActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_appcompat_base);
    }

    protected void setLayoutView(@LayoutRes int layoutId){
        View view = LayoutInflater.from(this).inflate(layoutId,null,false);
        setLayoutView(view);
    }

    protected void setLayoutView(View view){
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutView(view,layoutParams);
    }
    protected void setLayoutView(View view,ViewGroup.LayoutParams layoutParams){
        mActivityBaseBinding.container.addView(view,layoutParams);
        mBinding = DataBindingUtil.bind(view);
        initView();
    }


    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        mActivityBaseBinding.title.setText(title);
    }

    private void initView() {
        setUpToolbar();
        setUpTransition();
    }

    private void setUpTransition() {
//        setTransitionMode(TRANSITION_SLIDE);
    }

    protected  void setUpToolbar(){
        setSupportActionBar(mActivityBaseBinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.vd_back_ico);
        getSupportActionBar().setHomeButtonEnabled(true);
        mActivityBaseBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     *  点击右边按钮触发事件
     */
    public void setRightComponent(@DrawableRes int rightBt,View.OnClickListener listener) {

        mActivityBaseBinding.rightBt.setVisibility(View.VISIBLE);
        mActivityBaseBinding.rightBt.setOnClickListener(listener);
        mActivityBaseBinding.rightBt.setImageResource(rightBt);

    }

    /**
     * Get the Main Application component for dependency injection.
     *
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    public void showLoading() {
        DialogManagerUtils.getInstance().showRequestDialog(this);
    }

    @Override
    public void hideLoading() {
        DialogManagerUtils.getInstance().closeDialg();
    }

    @Override
    public void showMessage(int msgId) {

    }

    @Override
    public void showHint(int msgId) {

    }

    @Override
    public void showHint(String msg) {

    }

    @Override
    public void showNetworkError() {
        mActivityBaseBinding.container.setVisibility(View.GONE);
        mActivityBaseBinding.error.setVisibility(View.VISIBLE);
        mActivityBaseBinding.empty.setVisibility(View.GONE);
    }

    @Override
    public void showServerError() {
        mActivityBaseBinding.container.setVisibility(View.GONE);
        mActivityBaseBinding.error.setVisibility(View.VISIBLE);
        mActivityBaseBinding.empty.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(String msg) {

    }

    @Override
    public void showAuthorizedError() {

    }

    @Override
    public void showEmpty() {
        mActivityBaseBinding.container.setVisibility(View.GONE);
        mActivityBaseBinding.error.setVisibility(View.GONE);
        mActivityBaseBinding.empty.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DialogManagerUtils.getInstance().onDestory();
    }
}
