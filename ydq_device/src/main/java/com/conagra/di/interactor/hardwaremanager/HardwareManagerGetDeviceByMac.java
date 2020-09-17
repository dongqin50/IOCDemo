package com.conagra.di.interactor.hardwaremanager;

import com.conagra.di.UseCase;
import com.conagra.di.repository.HardwareManagerRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2018/6/28.
 */

public class HardwareManagerGetDeviceByMac extends UseCase<Boolean>{

    private HardwareManagerRepository repository;

    @Inject
    public HardwareManagerGetDeviceByMac( HardwareManagerRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Object params) {
        Map<String,String> map = new HashMap();
        map.put("Mac",(String)params);
        return repository.getDeviceByMac(getBody(buildJsonBody(map))).map((v)->v.getData());
    }
}
