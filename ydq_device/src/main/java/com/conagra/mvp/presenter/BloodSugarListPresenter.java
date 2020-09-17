package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodSugarListView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by yedongqin on 2017/10/31.
 */

public class BloodSugarListPresenter extends BasePresenter<BloodSugarListView> {

    @Inject
    @Named("bloodSugarGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private int page = 1;
    private int rows = 100;

    @Inject
    public BloodSugarListPresenter() {
    }

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
        mUCGetAllList.execute(new MySubscriber<BloodSugarListModel> (){
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
            public void onBackNext(BloodSugarListModel model) {
                getView().hideLoading();
                getView().renderAllData(model,page > 1);
            }

        }, map);
    }


}
