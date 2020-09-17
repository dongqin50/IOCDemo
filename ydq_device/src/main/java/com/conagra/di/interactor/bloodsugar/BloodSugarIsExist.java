package com.conagra.di.interactor.bloodsugar;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodSugarRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodSugarIsExist extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 时间类型
     */
    public final static String TIME_SLOT = "TimeSlot";

    private BloodSugarRepository repository;

    @Inject
    public BloodSugarIsExist(BloodSugarRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {

        return repository.isExistBloodSugar(getBody(buildJsonBody((Map) params)));
    }
}
