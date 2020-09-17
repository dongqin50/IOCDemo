package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodpressure.BloodPressureCreate;
import com.conagra.di.interactor.bloodpressure.BloodPressureIsExist;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerCreate;
import com.conagra.hardware.model.BloodPressureModel;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.model.BloodSugarIsExistModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodPressureView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;


public class BloodPressurePresenter extends BasePresenter<BloodPressureView> {

    @Inject
    @Named("bloodPressureIsExit")
    UseCase mUCBloodPressureIsExit;

    @Inject
    @Named("bloodPressureCreate")
    UseCase mUCCreate;

    @Inject
    @Named("bloodPressureGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private String time;

    @Inject
    public BloodPressurePresenter() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void isExist(BloodPressureModel model) {
        Map map = new HashMap();
        map.put(BloodPressureIsExist.CUSTOMER_ID, userId);

        mUCBloodPressureIsExit.execute(new MySubscriber<BloodSugarIsExistModel>() {

            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().showServerError(message);
            }

            @Override
            public void onBackNext(BloodSugarIsExistModel o) {
                getView().isExist(model,o.isIsExist());
            }
        }, map);
    }

    public void create(BloodPressureModel model) {

        Map map = new HashMap();
        map.put(BloodPressureCreate.CUSTOMER_ID, model.getCustomerNo());
        map.put(BloodPressureCreate.SYSTOLIC_PRESSURE, model.getSystolicPressure());
        map.put(BloodPressureCreate.DIASTOLIC_PRESSURE, model.getDiastolicPressure());
        map.put(BloodPressureCreate.HEART_RATE, model.getHeartRate());
        map.put(BloodPressureCreate.STATE, model.getState());

        getView().showLoading();
        mUCCreate.execute(new MySubscriber<String>() {
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
            public void onBackNext(String model) {
                getView().hideLoading();
                getView().createOrUpdateSuccess();
            }

        }, map);

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

    @Inject
    @Named("hardwareManagerGetDeviceByMac")
    UseCase mDeviceIsExist;

    @Inject
    @Named("hardwareManagerCreate")
    UseCase mDeviceCreate;

    public void addDevice(String mac) {
        Map map = new HashMap();
        map.put(HardwareManagerCreate.DEVICE_NAME, "BloodPressure_"+System.currentTimeMillis());
        map.put(HardwareManagerCreate.DEVICE_MAC_ADDRESS, mac);
        map.put(HardwareManagerCreate.DEVICE_USER_ID, userId);
        map.put(HardwareManagerCreate.DEVICE_TYPE, "3");
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
