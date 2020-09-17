package com.conagra.hardware.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.conagra.R;
import com.conagra.databinding.ActivityFetalmoveLisiBinding;
import com.conagra.di.component.DaggerFealMoveComponent;
import com.conagra.di.component.FealMoveComponent;
import com.conagra.di.module.FealMoveModule;
import com.conagra.hardware.adapter.LisiRecordAdapter;
import com.conagra.mvp.model.FetalMoveListModel;
import com.conagra.mvp.presenter.FealMoveListPresenter;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.FealMoveListView;

/**
 * Created by yedongqin on 2016/10/23.
 */

public class FetalMoveListActivity extends DragBaseAppCompatActivity<
        ActivityFetalmoveLisiBinding, FealMoveListPresenter, FealMoveComponent> implements FealMoveListView {
    private LisiRecordAdapter mAdapter;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_fetalmove_lisi);
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        doInit();
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerFealMoveComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .fealMoveModule(new FealMoveModule()).build();
        mComponent.inject(this);
    }

    public void doInit() {
        setTitle("历史记录");
        mBinding.lisiRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LisiRecordAdapter(mBinding.lisiRv, null);
        mAdapter.setOnClickListener((index,data)->{
            Intent intent = new Intent(FetalMoveListActivity.this, FetalMoveActivity.class);
            intent.putExtra("data",data.getCreateTime());
            intent.putExtra("userId",userId);
            startActivity(intent);
        });
        mBinding.lisiRv.setAdapter(mAdapter);
        mPresenter.GetALLList();
    }

    @Override
    public void renderAllData(FetalMoveListModel model, boolean update) {
        if (mAdapter.getCount() <= 0) {
            if (model.getRows() == null || model.getRows().size() <= 0) {
                showEmpty();
            } else {
                mAdapter.setDataList(model.getRows());
            }
        } else {
            mAdapter.addAll(model.getRows());
        }
        if(mAdapter.getCount() <= 0){
            showEmpty();
        }
    }

//    private void doRequestData() {

//        if(mAdapter.getCount() == 0){
//            mRecycleView.doLoadingSetting();
//        }
//        mRecycleView.doHideView();
//        HardwareNetWorkRequest.getFetalMoveMentListByRang(
//                this,
//                mHosptialBean.getValue1(0),
//                CacheUtils.getmUserModel().getCustomerNo(),
//                TimeUtils.getYMDData(-30),
//                TimeUtils.getYMDData(0),
//                10,page,
//                new MySubscriber<LiSiBackBean>(LiSiBackBean.class){
//                    @Override
//                    public void onBackError(String message) {
//                        super.onBackError(message);
//                        mRecycleView.doHideView();
//                        mRecycleView.doShowErrorView();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        mRecycleView.doHideView();
//                        mRecycleView.doShowErrorView();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        ActivityUtils.hideRequestDialog(FetalMoveListActivity.this);
//                    }
//
//                    @Override
//                    public void onBackNext(LiSiBackBean backBean) {
//                        super.onBackNext(backBean);
//                        mRecycleView.doHideView();
//                        if(backBean != null) {
//                            List<LiSiBackBean.PageDataBean> list = backBean.getPageData();
//                            if (list != null && list.size() > 0) {
//                                mAdapter.setDataList(list);
//                            }
//                        }
//                        if(mAdapter.getCount() == 0){
//                            mRecycleView.doShowEmptyView();
//                        }
//                    }
//                });
//    }

}
