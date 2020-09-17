package com.conagra.di.interactor.temple;

import com.conagra.di.UseCase;
import com.conagra.di.repository.TempleRepository;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 *
 * 查询血糖列表
 */

public class TempleGetALLList extends UseCase {

    /**
     * 页数
     */
    public final static String PAGE = "page";
    /**
     * 每页条数
     */
    public final static String ROWS = "rows";
    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    /**
     * 医院
     */
    public final static String HOSPITAL_ID = "HospitalID";
    /**
     * 开始日期
     */
    public final static String BEGIN_DATE = "BeginDate";
    /**
     * 结束日期
     */
    public final static String END_DATA = "EndData";

    private TempleRepository repository;

    @Inject
    public TempleGetALLList(TempleRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        return repository.GetALLList(getBody(buildJsonBody((Map) params)));
    }
}
