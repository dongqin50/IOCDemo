package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarCreate;
import com.conagra.di.interactor.bloodsugar.BloodSugarEdit;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.di.interactor.bloodsugar.BloodSugarIsExist;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerCreate;
import com.conagra.mvp.model.BloodSugarIsExistModel;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.BloodSugarView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodSugarPresenter extends BasePresenter<BloodSugarView> {

    @Inject
    @Named("bloodSugarIsExit")
    UseCase mUCBloodSugarIsExit;

    @Inject
    @Named("bloodSugarCreate")
    UseCase mUCCreate;

    @Inject
    @Named("bloodSugarEdit")
    UseCase mUCBloodSugarEdit;

    @Inject
    @Named("bloodSugarDelete")
    UseCase mUCDelete;

    @Inject
    @Named("bloodSugarGetAllList")
    UseCase mUCGetAllList;


    private String userId;
    private String time;

    @Inject
    public BloodSugarPresenter() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void isExist(int  timeSlot,final String value) {
        Map map = new HashMap();
        map.put(BloodSugarIsExist.CUSTOMER_ID, userId);
        map.put(BloodSugarIsExist.TIME_SLOT, timeSlot);

        mUCBloodSugarIsExit.execute(new MySubscriber<BloodSugarIsExistModel>() {

            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().showServerError(message);
            }

            @Override
            public void onBackNext(BloodSugarIsExistModel model) {
                getView().isExist(model.getTb_BloodSugar_ID(),value,timeSlot,model.isIsExist());
            }
        }, map);
    }

    public void create(String sugarValue, int timeSlot) {

        Map map = new HashMap();
        map.put(BloodSugarCreate.CUSTOMER_ID, userId);
        map.put(BloodSugarCreate.BLOOD_SUGAR_VALUE, sugarValue);
        map.put(BloodSugarCreate.TIME_SLOT, timeSlot);
        getView().showLoading();
        mUCCreate.execute(new MySubscriber<String>() {
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
            public void onBackNext(String model) {
                getView().hideLoading();

                getView().createOrUpdateSuccess();
            }

        }, map);

    }

    public void edit(String bloodSugarId, String sugarValue) {
        Map map = new HashMap();
        map.put(BloodSugarEdit.BLOOD_SUGAR_VALUE, sugarValue);
        map.put(BloodSugarEdit.TB_BLOODSUGAR_ID, bloodSugarId);

        mUCBloodSugarEdit.execute(new MySubscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("ddd");
            }

            @Override
            public void onBackNext(String model) {
                getView().createOrUpdateSuccess();
            }

        }, map);
    }

    public void GetALLList() {
        Map map = new HashMap();
        map.put(BloodSugarGetALLList.CUSTOMER_ID, userId);
        map.put(BloodSugarGetALLList.PAGE, 1);
        map.put(BloodSugarGetALLList.ROWS,100);
        map.put(BloodSugarGetALLList.BEGIN_DATE, time);
        map.put(BloodSugarGetALLList.END_DATA, time);

        mUCGetAllList.execute(new MySubscriber<BloodSugarListModel> (){
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

    @Inject
    @Named("hardwareManagerGetDeviceByMac")
    UseCase mDeviceIsExist;

    @Inject
    @Named("hardwareManagerCreate")
    UseCase mDeviceCreate;

    public void addDevice(String mac) {
        Map map = new HashMap();
        map.put(HardwareManagerCreate.DEVICE_NAME, "BloodSugar_"+System.currentTimeMillis());
        map.put(HardwareManagerCreate.DEVICE_MAC_ADDRESS, mac);
        map.put(HardwareManagerCreate.DEVICE_USER_ID, userId);
        map.put(HardwareManagerCreate.DEVICE_TYPE, "4");
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
