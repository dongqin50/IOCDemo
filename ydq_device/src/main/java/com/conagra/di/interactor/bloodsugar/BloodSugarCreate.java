package com.conagra.di.interactor.bloodsugar;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodSugarRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/10/31.
 */

public class BloodSugarCreate extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 数值
     */
    public final static String BLOOD_SUGAR_VALUE = "BloodSugarValue";
    /**
     * 类别
     */
    public final static String TIME_SLOT = "TimeSlot";

    private BloodSugarRepository repository;

    @Inject
    public BloodSugarCreate(BloodSugarRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.BloodSugarCreate(getBody(buildJsonBody((Map) params)));
    }
}
