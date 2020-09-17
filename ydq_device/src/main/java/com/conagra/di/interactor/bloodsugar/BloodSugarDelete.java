package com.conagra.di.interactor.bloodsugar;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodSugarRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodSugarDelete extends UseCase {

    /**
     * 血糖ID
     */
    public final static String BloodSugar_ID = "ID";

    private BloodSugarRepository repository;

    @Inject
    public BloodSugarDelete(BloodSugarRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.BloodSugarDelete(getBody(buildJsonBody((Map) params)));
    }
}
