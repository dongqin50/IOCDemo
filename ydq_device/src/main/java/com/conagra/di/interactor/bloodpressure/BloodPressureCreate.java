package com.conagra.di.interactor.bloodpressure;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodPressureRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/10/31.
 */

public class BloodPressureCreate extends UseCase {

    /**
     * 患者ID    private String CustomerID;
     private int SystolicPressure;
     private int DiastolicPressure;
     private int HeartRate;
     private String State;
     private String RecorderID;
     private String HospitalID;
     private String isDelete;
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 数值
     */
    public final static String STATE = "State";
    /**
     * 收缩压
     */
    public final static String SYSTOLIC_PRESSURE = "SystolicPressure";
    /**
     * 舒张压
     */
    public final static String DIASTOLIC_PRESSURE = "DiastolicPressure";
    /**
     * 心率
     */
    public final static String HEART_RATE = "HeartRate";

    private BloodPressureRepository repository;

    @Inject
    public BloodPressureCreate(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Create(getBody(buildJsonBody((Map) params)));
    }
}
