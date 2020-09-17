package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodpressure.BloodPressureGetALLList;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodPressureListView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class BloodPressureListPresenter extends BasePresenter<BloodPressureListView> {


    @Inject
    @Named("bloodPressureGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private int page = 1;
    private int rows = 100;

    @Inject
    public BloodPressureListPresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void GetALLList() {

        Map map = new HashMap();
        map.put(BloodPressureGetALLList.CUSTOMER_ID, userId);
        map.put(BloodPressureGetALLList.PAGE, page);
        map.put(BloodPressureGetALLList.ROWS, rows);
        map.put(BloodPressureGetALLList.BEGIN_DATE, null);
        map.put(BloodPressureGetALLList.END_DATA, null);
        getView().showLoading();
        mUCGetAllList.execute(new MySubscriber<BloodPressureListModel>(){
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showServerError("网络异常");
            }


            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().hideLoading();
                getView().showServerError(message);
            }

            @Override
            public void onBackNext(BloodPressureListModel model) {
                getView().hideLoading();
                getView().renderAllData(model,page > 1);
            }

        }, map);
    }


}
