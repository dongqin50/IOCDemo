package com.conagra.di.repository;

import com.conagra.hardware.model.TempleModel;
import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.TempleListModel;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface TempleRepository {

    //当天是否已经测量过
    @POST("/Patient/Temperature/TempleIsExistMinuteData")
    Observable<BaseBackBean<Object>> IsExistMinuteData(@Body RequestBody params);

    @POST("/Patient/Temperature/Create")
    Observable<BaseBackBean<TempleModel>> Create(@Body RequestBody params);

    @POST("/Patient/Temperature/Edit")
    Observable<BaseBackBean<Object>> Edit(@Body RequestBody params);

    @POST("/Patient/Temperature/Delete")
    Observable<BaseBackBean<Object>> Delete(@Body RequestBody params);

    @POST("/DrSick/Temperature/GetALLList")
    Observable<BaseBackBean<List<TempleListModel>>> GetALLList(@Body RequestBody params);
}
