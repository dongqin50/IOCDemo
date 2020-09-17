package com.conagra.di.interactor.fealheart;

import com.conagra.di.UseCase;
import com.conagra.di.repository.FealHeartRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class FealHeartDelete extends UseCase {

    /**
     * 血糖ID
     */
    public final static String BloodSugar_ID = "ID";

    private FealHeartRepository repository;

    @Inject
    public FealHeartDelete(FealHeartRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Delete(getBody(buildJsonBody((Map) params)));
    }
}
