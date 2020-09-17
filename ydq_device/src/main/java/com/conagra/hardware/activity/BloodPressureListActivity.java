package com.conagra.hardware.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodpressureListBinding;
import com.conagra.di.component.BloodPressureComponent;
import com.conagra.di.component.DaggerBloodPressureComponent;
import com.conagra.di.module.BloodPressureModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.adapter.BloodPressureListAdapter;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.presenter.BloodPressureListPresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.BloodPressureListView;
import com.conagra.mvp.utils.TimeUtils;


/**
 * Created by yedongqin on 2016/10/31.
 */

public class BloodPressureListActivity extends DragBaseFragmentActivity<
        ActivityBloodpressureListBinding, BloodPressureListPresenter, BloodPressureComponent> implements BloodPressureListView {

    private BloodPressureListAdapter mAdapter;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodpressure_list);
        doInit();

    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerBloodPressureComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .bloodPressureModule(new BloodPressureModule()).build();
        mComponent.inject(this);
    }

    public void doInit() {
        setTitle("历史记录");
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);

        mAdapter = new BloodPressureListAdapter(mBinding.bloodpressureListRv, null);
        mBinding.bloodpressureListRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodpressureListRv.setAdapter(mAdapter);

        mAdapter.setOnClickListener((index,model)->{
            Intent intent = new Intent(BloodPressureListActivity.this,BloodPressureDetailActivity.class);
            intent.putExtra("data", TimeUtils.getTime( model.getCreateTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
            intent.putExtra("userId",userId);
            startActivity(intent);
        });

        mPresenter.GetALLList();
    }

    @Override
    public void renderAllData(BloodPressureListModel model, boolean update) {
        if(mAdapter.getCount() <= 0){
            if(model.getRows() == null || model.getRows().size() <= 0){
                showEmpty();
            }else {
                mAdapter.setDataList(model.getRows());
            }
        }else {
            mAdapter.addAll(model.getRows());
        }
        if(mAdapter.getCount() <= 0){
            showEmpty();
        }
    }

}
