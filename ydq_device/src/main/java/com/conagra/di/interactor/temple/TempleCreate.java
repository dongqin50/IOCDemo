package com.conagra.di.interactor.temple;

import com.conagra.di.UseCase;
import com.conagra.di.repository.TempleRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/10/31.
 */

public class TempleCreate extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    public final static String HOSPITAL_ID = "HospitalID";
    /**
     * 数值
     */
    public final static String TEMPERTURE_VALUE = "TemperatureValue";
    public final static String STATE = "State";

    private TempleRepository repository;

    @Inject
    public TempleCreate(TempleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Create(getBody(buildJsonBody((Map) params)));
    }
}
