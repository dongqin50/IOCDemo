package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.fealMove.FealMoveCreate;
import com.conagra.di.interactor.fealMove.FealMoveGetALLList;
import com.conagra.hardware.model.FetalMovementModel;
import com.conagra.mvp.model.FetalMoveListsModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.FealMoveView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class FealMovePresenter extends BasePresenter<FealMoveView> {

    @Inject
    @Named("fealMoveCreate")
    UseCase mUCCreate;

    @Inject
    @Named("fealMoveGetList")
    UseCase fealMoveGetList;

    private String userId;
    private String time;

    @Inject
    public FealMovePresenter() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void create(FetalMovementModel model) {
        if(mUCCreate != null && mUCCreate.isUnsubscribe()) {
            mUCCreate.unsubscribe();
        }
        Map map = new HashMap();
        map.put(FealMoveCreate.CUSTOMER_ID, userId);
        map.put(FealMoveCreate.CLICK_NUMBER, model.getClickNumber());
        map.put(FealMoveCreate.RECORD_TIME_POINT, model.getFetalMovementTimes());
        map.put(FealMoveCreate.START_TIME, model.getStartTime());
        getView().showLoading();
        mUCCreate.execute(new MySubscriber<String>() {
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
            public void onBackNext(String model) {
                getView().hideLoading();
                getView().createOrUpdateSuccess();
            }

        }, map);
    }

    public void GetALLList(String startTime,String endTime) {

        if(fealMoveGetList != null && fealMoveGetList.isUnsubscribe()) {
            fealMoveGetList.unsubscribe();
        }
        Map map = new HashMap();
        map.put(FealMoveGetALLList.CUSTOMER_ID, userId);
        map.put(FealMoveGetALLList.PAGE, 1);
        map.put(FealMoveGetALLList.ROWS,100);
        map.put(FealMoveGetALLList.BEGIN_DATE, startTime);
        map.put(FealMoveGetALLList.END_DATA, endTime);

        fealMoveGetList.execute(new MySubscriber<FetalMoveListsModel> (){
            @Override
            public void onCompleted() {

            }
            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().showServerError(message);
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showServerError("网络异常");
            }

            @Override
            public void onBackNext(FetalMoveListsModel model) {
                getView().renderAllData(model);
            }

        }, map);
    }
}
