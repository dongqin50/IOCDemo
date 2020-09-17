package com.conagra.di.interactor.hardwaremanager;

import com.conagra.di.UseCase;
import com.conagra.di.repository.HardwareManagerRepository;
import com.conagra.mvp.model.HardwareManagerCreateModel;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2018/6/28.
 */


public class HardwareManagerCreate extends UseCase {

    private HardwareManagerRepository repository;

    public final static String DEVICE_TYPE = "Type";
    public final static String DEVICE_MAC_ADDRESS = "MacAddress";
    public final static String DEVICE_USER_ID = "CreatorID";
    public final static String DEVICE_NAME = "Name";


    @Inject
    public HardwareManagerCreate( HardwareManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<HardwareManagerCreateModel> buildUseCaseObservable(Object params) {
        Map map = (Map) params;
        return repository.create(getBody(buildJsonBody(map))).map((v)->v.getData());
    }
}

