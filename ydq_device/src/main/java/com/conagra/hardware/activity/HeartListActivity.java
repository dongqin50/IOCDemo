package com.conagra.hardware.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.conagra.R;
import com.conagra.databinding.ActivityHeartrecordBinding;
import com.conagra.di.component.DaggerFealHeartComponent;
import com.conagra.di.component.FealHeartComponent;
import com.conagra.di.module.FealHeartModule;
import com.conagra.hardware.adapter.HeartRecordAdapter;
import com.conagra.mvp.model.HeartListModel;
import com.conagra.mvp.presenter.FealHeartListPresenter;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.FealHeartListView;

/**
 * Created by yedongqin on 16/9/28.
 */
public class HeartListActivity extends DragBaseAppCompatActivity<
        ActivityHeartrecordBinding,FealHeartListPresenter,FealHeartComponent> implements FealHeartListView {

//    @BindView(R.id.heartrecord_header)
//    TempleHeaderView mHeader;
//    @BindView(R.id.heartrecord_rv)
    private HeartRecordAdapter mAdapter;
    private int page;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_heartrecord);
        doInit();
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerFealHeartComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .fealHeartModule(new FealHeartModule()).build();
        mComponent.inject(this);
    }

    @Override
    public void renderAllData(HeartListModel model, boolean update) {
        if(mAdapter.getCount() <= 0){
            if(model != null){
                mAdapter.setDataList(model.getRows());
            }else {
                showEmpty();
            }
        }else {
            mAdapter.addAll(model.getRows());
        }
        if(mAdapter.getCount() <= 0){
            showEmpty();
        }
    }

    public void doInit() {
        page = 1;
        setTitle(" 历史记录");
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        mAdapter = new HeartRecordAdapter(mBinding.heartrecordRv,null);
        mBinding.heartrecordRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.heartrecordRv.setAdapter(mAdapter);
        mAdapter.setOnClickListener((position,data)->{
            if(data != null){
                Intent intent = new Intent(HeartListActivity.this, VideoPlayActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });
        mPresenter.GetALLList();
    }

    private void doRequestData(){
//        if(mAdapter.getCount() == 0){
//            mBinding.heartrecordRv.doLoadingSetting();
//        }
//
//        mBinding.heartrecordRv.doHideView();
//        HardwareNetWorkRequest.doGetFetalheartListByRang(
//                this,
//                mHosptialBean.getValue1(0),
//                CacheUtils.getmUserModel().getCustomerNo(),10,page,new MySubscriber<HeartListBackModel>(HeartListBackModel.class){
//                    @Override
//                    public void onBackError(String message) {
//                        super.onBackError(message);
//                        mBinding.heartrecordRv.doHideView();
//                        mBinding.heartrecordRv.doShowErrorView();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        mBinding.heartrecordRv.doHideView();
//                        mBinding.heartrecordRv.doShowErrorView();
//                    }
//
//                    @Override
//                    public void onBackNext(HeartListBackModel backModel) {
//                        super.onBackNext(backModel);
//                        mBinding.heartrecordRv.doHideView();
//                        if(backModel != null){
//                            mAdapter.addAll(backModel.getPageData());
//                        }
//                        if(mAdapter.getCount() == 0)
//                            mBinding.heartrecordRv.doShowEmptyView();
//                    }
//                }
//        );
    }

}
