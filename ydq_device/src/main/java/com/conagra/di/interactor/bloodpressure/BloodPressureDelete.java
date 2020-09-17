package com.conagra.di.interactor.bloodpressure;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodPressureRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodPressureDelete extends UseCase {

    /**
     * 血糖ID
     */
    public final static String BloodSugar_ID = "ID";

    private BloodPressureRepository repository;

    @Inject
    public BloodPressureDelete(BloodPressureRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Delete(getBody(buildJsonBody((Map) params)));
    }
}
