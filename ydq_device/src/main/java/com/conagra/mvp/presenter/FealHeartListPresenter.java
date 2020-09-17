package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.mvp.model.HeartListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.FealHeartListView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class FealHeartListPresenter extends BasePresenter<FealHeartListView> {

    @Inject
    public FealHeartListPresenter() {
    }

    @Inject
    @Named("fealHeartGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private int page = 1;
    private int rows = 100;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void GetALLList() {

        Map map = new HashMap();
        map.put(BloodSugarGetALLList.CUSTOMER_ID, userId);
        map.put(BloodSugarGetALLList.PAGE, page);
        map.put(BloodSugarGetALLList.ROWS, rows);
        map.put(BloodSugarGetALLList.BEGIN_DATE, null);
        map.put(BloodSugarGetALLList.END_DATA, null);
        getView().showLoading();
        mUCGetAllList.execute(new MySubscriber<HeartListModel>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().hideLoading();
                getView().showServerError(message);
            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showServerError("网络异常");
            }

            @Override
            public void onBackNext(HeartListModel model) {
                getView().hideLoading();
                getView().renderAllData(model,page > 1);
            }

        }, map);
    }

}
