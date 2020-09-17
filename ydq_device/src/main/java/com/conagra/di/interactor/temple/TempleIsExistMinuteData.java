package com.conagra.di.interactor.temple;

import com.conagra.di.UseCase;
import com.conagra.di.repository.TempleRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public class TempleIsExistMinuteData extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 时间类型
     */
    public final static String TIME_SLOT = "TimeSlot";

    private TempleRepository repository;

    @Inject
    public TempleIsExistMinuteData(TempleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {

        return repository.IsExistMinuteData(getBody(buildJsonBody((Map) params)));
    }
}
