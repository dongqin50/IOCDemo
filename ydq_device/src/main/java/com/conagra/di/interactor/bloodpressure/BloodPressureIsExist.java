package com.conagra.di.interactor.bloodpressure;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodPressureRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodPressureIsExist extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";

    private BloodPressureRepository repository;

    @Inject
    public BloodPressureIsExist(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {

        return repository.IsExistBloodPressure(getBody(buildJsonBody((Map) params)));
    }
}
