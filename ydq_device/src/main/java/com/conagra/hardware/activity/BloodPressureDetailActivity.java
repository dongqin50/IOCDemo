package com.conagra.hardware.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodpressureDetailBinding;
import com.conagra.di.component.BloodPressureComponent;
import com.conagra.di.component.DaggerBloodPressureComponent;
import com.conagra.di.module.BloodPressureModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.layout.BloodPressureLayout2;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.presenter.BloodPressureDetailPresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.BloodPressureDetailView;

public class BloodPressureDetailActivity extends DragBaseFragmentActivity<
        ActivityBloodpressureDetailBinding, BloodPressureDetailPresenter, BloodPressureComponent>
        implements BloodPressureDetailView {

    private BloodPressureLayout2 view2;
    private String userId;
    private String currentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodpressure_detail);
        setTitle("历史详情");
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);

        if (getIntent().hasExtra("data")) {
            currentTime = getIntent().getStringExtra("data");
        }
        view2 = new BloodPressureLayout2(this,currentTime);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(layoutParams);
//        mBinding.bloodPressure.
//        view2 = mBinding.;
//        view2 = mBinding.bloodPressure;
        mBinding.bloodPressure.addView(view2);
    }
    public void requestGetAll(String startTime, String endTime) {
        mPresenter.GetALLList(startTime, endTime);
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



    @Override
    public void renderAllData(BloodPressureListModel model) {
        view2.renderAllData(model);
    }

}
