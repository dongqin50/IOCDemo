package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodSugarDetailView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class BloodSugarDetailPresenter extends BasePresenter<BloodSugarDetailView>{

    @Inject
    @Named("bloodSugarGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private String time;


    @Inject
    public BloodSugarDetailPresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void GetALLList() {
        Map map = new HashMap();
        map.put(BloodSugarGetALLList.CUSTOMER_ID, userId);
        map.put(BloodSugarGetALLList.PAGE, 1);
        map.put(BloodSugarGetALLList.ROWS,100);
        map.put(BloodSugarGetALLList.BEGIN_DATE, time);
        map.put(BloodSugarGetALLList.END_DATA, time);

        mUCGetAllList.execute(new MySubscriber<BloodSugarListModel>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showServerError("服务器正在升级...");
            }

            @Override
            public void onBackNext(BloodSugarListModel model) {
                getView().renderAllData(model);
            }

        }, map);

    }
}
