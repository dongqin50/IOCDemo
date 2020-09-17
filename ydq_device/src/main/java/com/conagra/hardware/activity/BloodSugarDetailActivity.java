package com.conagra.hardware.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodsugarDetailBinding;
import com.conagra.di.component.BloodSugarComponent;
import com.conagra.di.component.DaggerBloodSugarComponent;
import com.conagra.di.module.BloodSugarModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.layout.BloodSugarLayout2;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.presenter.BloodSugarDetailPresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.BloodSugarDetailView;

public class BloodSugarDetailActivity extends
        DragBaseFragmentActivity<ActivityBloodsugarDetailBinding, BloodSugarDetailPresenter, BloodSugarComponent> implements BloodSugarDetailView {

    private BloodSugarLayout2 view2;
    private String currentTime;
    private static final String TAG = BloodSugarActivity.class.getSimpleName();
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodsugar_detail);

        doInit();
    }

    public void doInit() {
        userId = getIntent().getStringExtra("userId");
        currentTime = getIntent().getStringExtra("data");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        setTitle("历史详情");
        view2 = new BloodSugarLayout2(this, currentTime);
        view2.doHideBar();
        mBinding.bloodSugar.addView(view2);
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerBloodSugarComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .bloodSugarModule(new BloodSugarModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void renderAllData(BloodSugarListModel model) {
        view2.renderAllData(model);
    }

    public void requestAllData(String time) {
        mPresenter.setTime(time);
        mPresenter.GetALLList();
    }

}
