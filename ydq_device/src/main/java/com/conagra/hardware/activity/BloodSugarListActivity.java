package com.conagra.hardware.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodpressureListBinding;
import com.conagra.di.component.BloodSugarComponent;
import com.conagra.di.component.DaggerBloodSugarComponent;
import com.conagra.di.module.BloodSugarModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.adapter.BloodSugarListAdapter;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.presenter.BloodSugarListPresenter;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.BloodSugarListView;
import com.conagra.mvp.utils.TimeUtils;

/**
 * Created by yedongqin on 2016/11/3.
 */

public class BloodSugarListActivity extends DragBaseAppCompatActivity<
        ActivityBloodpressureListBinding,BloodSugarListPresenter,BloodSugarComponent> implements BloodSugarListView{

    private BloodSugarListAdapter mAdapter;
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodpressure_list);
        initializeInjector();
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        doInit();
    }



    public void doInit() {
        setTitle("历史记录");
        mAdapter = new BloodSugarListAdapter(mBinding.bloodpressureListRv,null);
        mBinding.bloodpressureListRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodpressureListRv.setAdapter(mAdapter);
        mAdapter.setOnClickListener((index,model)->{
            Intent intent = new Intent(BloodSugarListActivity.this,BloodSugarDetailActivity.class);
            intent.putExtra("data", TimeUtils.getTime( model.getCreateTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
            intent.putExtra("userId",userId);
            startActivity(intent);
        });

        mPresenter.GetALLList();
    }

    @Override
    public void renderAllData(BloodSugarListModel model,boolean update) {
        if(update){
            mAdapter.addAll(model.getRows());
        }else {
            mAdapter.setDataList(model.getRows());
        }
        if(mAdapter.getCount() <= 0){
            showEmpty();
        }
    }

    protected void initializeInjector() {
        mComponent = DaggerBloodSugarComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .bloodSugarModule(new BloodSugarModule())
                .build();
        mComponent.inject(this);
    }
}
