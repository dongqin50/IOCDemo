package com.conagra.di.interactor.temple;

import com.conagra.di.UseCase;
import com.conagra.di.repository.TempleRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class TempleDelete extends UseCase {

    /**
     * 血糖ID
     */
    public final static String TEMPERATURE_ID = "tb_Temperature_ID";

    private TempleRepository repository;

    @Inject
    public TempleDelete(TempleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Delete(getBody(buildJsonBody((Map) params)));
    }
}
