package com.conagra.di.interactor.fealMove;

import com.conagra.di.UseCase;
import com.conagra.di.repository.FealMoveRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/10/31.
 */

public class FealMoveCreate extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 数值
     */
    public final static String CLICK_NUMBER = "ClickNumber";
    public final static String RECORD_TIME_POINT = "RecordTimePoint";
    public final static String START_TIME = "StartTime";

    private FealMoveRepository repository;

    @Inject
    public FealMoveCreate(FealMoveRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.Create(getBody(buildJsonBody((Map) params)));
    }
}
