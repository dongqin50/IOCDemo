package com.conagra.di.interactor.bloodsugar;

import com.conagra.di.UseCase;
import com.conagra.di.repository.BloodSugarRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class BloodSugarEdit extends UseCase {

    /**
     * 血糖ID
     */
    public final static String TB_BLOODSUGAR_ID = "tb_BloodSugar_ID";
    /**
     * 血糖值
     */
    public final static String BLOOD_SUGAR_VALUE = "BloodSugarValue";

    private BloodSugarRepository repository;

    @Inject
    public BloodSugarEdit(BloodSugarRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.BloodSugarEdit(getBody(buildJsonBody((Map) params)));
    }
}
