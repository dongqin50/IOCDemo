package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerCreate;
import com.conagra.di.interactor.temple.TempleCreate;
import com.conagra.hardware.model.TempleModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.ui.view.TempleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class TemplePresenter extends BasePresenter<TempleView> {

    @Inject
    @Named("templeGetAllList")
    UseCase mUCGetAllList;

    @Inject
    @Named("templeCreate")
    UseCase mUCCreate;



    private String userId;
    private String hospitalId;
    private String time;
    private long startTime;

    @Inject
    public TemplePresenter() {
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void create(String tempertureValue) {

        long time = (System.currentTimeMillis()-startTime)/1000;
        if(time > 60){
            Map map = new HashMap();
            map.put(TempleCreate.CUSTOMER_ID, userId);
            map.put(TempleCreate.STATE, "0");
            map.put(TempleCreate.TEMPERTURE_VALUE, tempertureValue);
            map.put(TempleCreate.HOSPITAL_ID, hospitalId);
            getView().showLoading();
            mUCCreate.execute(new MySubscriber<TempleModel>() {
                @Override
                public void onBackError(String message) {
                    super.onBackError(message);
                    getView().hideLoading();
                    getView().showServerError(message);
                }

                @Override
                public void onError(Throwable e) {
                    getView().hideLoading();
                    getView().showServerError();
                }

                @Override
                public void onBackNext(TempleModel model) {
                    getView().hideLoading();
                    startTime = System.currentTimeMillis();
                    getView().createOrUpdateSuccess();
                }

            }, map);
        }else {
            getView().isExist(true);
        }
    }



    public void GetALLList(String startTime,String endTime) {
        Map map = new HashMap();
        map.put(BloodSugarGetALLList.CUSTOMER_ID, userId);
        map.put(BloodSugarGetALLList.PAGE, 1);
        map.put(BloodSugarGetALLList.ROWS,100);
        map.put(BloodSugarGetALLList.BEGIN_DATE, startTime);
        map.put(BloodSugarGetALLList.END_DATA, endTime);
        mUCGetAllList.execute(new MySubscriber<List<TempleListModel>> (){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showServerError();
            }

            @Override
            public void onBackNext(List<TempleListModel> model) {
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
        map.put(HardwareManagerCreate.DEVICE_NAME, "Temple_"+  System.currentTimeMillis());
        map.put(HardwareManagerCreate.DEVICE_MAC_ADDRESS, mac);
        map.put(HardwareManagerCreate.DEVICE_USER_ID, userId);
        map.put(HardwareManagerCreate.DEVICE_TYPE, "1");
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
