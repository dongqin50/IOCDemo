package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.fealheart.FealHeartCreate;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerCreate;
import com.conagra.hardware.model.FetalHeartRecordModel;
import com.conagra.mvp.model.FetalHeartModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.FealHeartView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class FealHeartPresenter extends BasePresenter<FealHeartView> {


    @Inject
    @Named("fealHeartCreate")
    UseCase mUCCreate;


    private String userId;

    @Inject
    public FealHeartPresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void create(FetalHeartRecordModel model) {

        if(model == null)
            return;
        Map map = new HashMap();
        map.put(FealHeartCreate.CUSTOMER_ID, userId);
        map.put(FealHeartCreate.AUDIO_URL,model.getAudiofs() );
        map.put(FealHeartCreate.AVG_HEART_RATE,model.getAvgHeartRate() + "");
        map.put(FealHeartCreate.PIONT_U_TERINE,model.getPionts2());
        map.put(FealHeartCreate.POINT_VALUE, model.getPionts1());
        map.put(FealHeartCreate.START_TIME,model.getStartTime() );
        map.put(FealHeartCreate.TIMES,model.getTimes() + "");
        getView().showLoading();
        mUCCreate.execute(new MySubscriber<FetalHeartModel>() {
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
            public void onBackNext(FetalHeartModel model) {
                getView().hideLoading();
                getView().createOrUpdateSuccess(model);
            }

        }, map);
    }
    @Inject
    @Named("hardwareManagerGetDeviceByMac")
    UseCase mDeviceIsExist;

    @Inject
    @Named("hardwareManagerCreate")
    UseCase mDeviceCreate;

    public void addDevice(String mac) {
        Map map = new HashMap();
        map.put(HardwareManagerCreate.DEVICE_NAME, "Fealheart_"+System.currentTimeMillis());
        map.put(HardwareManagerCreate.DEVICE_MAC_ADDRESS, mac);
        map.put(HardwareManagerCreate.DEVICE_USER_ID, userId);
        map.put(HardwareManagerCreate.DEVICE_TYPE, "2");
        mDeviceCreate.execute(new MySubscriber<Object> (){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showServerError();
                getView().addDeviceStatus(false);
            }

            @Override
            public void onBackNext(Object model) {
                getView().addDeviceStatus(true);
            }

        }, map);
    }

    public void isExistDevice(String mac) {
        mDeviceIsExist.execute(new Subscriber<Boolean>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showServerError();
            }

            @Override
            public void onNext(Boolean model) {
                getView().isExistDevice(model,mac);
            }

        }, mac);
    }
}
