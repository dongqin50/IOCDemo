package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodPressureDetailView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class BloodPressureDetailPresenter  extends BasePresenter<BloodPressureDetailView> {

    @Inject
    @Named("bloodPressureGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private String time;

    @Inject
    public BloodPressureDetailPresenter() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void GetALLList(String startTime,String endTime) {
        Map map = new HashMap();
        map.put(BloodSugarGetALLList.CUSTOMER_ID, userId);
        map.put(BloodSugarGetALLList.PAGE, 1);
        map.put(BloodSugarGetALLList.ROWS,100);
        map.put(BloodSugarGetALLList.BEGIN_DATE, startTime);
        map.put(BloodSugarGetALLList.END_DATA, endTime);

        mUCGetAllList.execute(new MySubscriber<BloodPressureListModel> (){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showServerError("网络错误");
            }

            @Override
            public void onBackNext(BloodPressureListModel model) {
                getView().renderAllData(model);
            }

        }, map);

    }


}

