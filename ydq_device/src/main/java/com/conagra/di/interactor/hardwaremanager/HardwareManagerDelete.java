package com.conagra.di.interactor.hardwaremanager;

import com.conagra.di.UseCase;
import com.conagra.di.repository.HardwareManagerRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2018/6/28.
 */


public class HardwareManagerDelete extends UseCase {

    private HardwareManagerRepository repository;

    @Inject
    public HardwareManagerDelete(HardwareManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<Object> buildUseCaseObservable(Object params) {
        Map map = (Map) params;
        return repository.delete(getBody(buildJsonBody(map))).map((v)->v.getData());
    }
}

