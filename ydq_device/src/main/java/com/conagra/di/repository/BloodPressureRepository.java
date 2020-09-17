package com.conagra.di.repository;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.model.BloodSugarIsExistModel;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface BloodPressureRepository {

    //当天是否已经测量过血压
    @POST("/Patient/BloodPressure/IsExistBloodPressure")
    Observable<BaseBackBean<BloodSugarIsExistModel>> IsExistBloodPressure(@Body RequestBody params);

    @POST("/Patient/BloodPressure/Create")
    Observable<BaseBackBean<Object>> Create(@Body RequestBody params);

    @POST("/Patient/BloodPressure/Edit")
    Observable<BaseBackBean<Object>> Edit(@Body RequestBody params);

    @POST("/Patient/BloodPressure/Delete")
    Observable<BaseBackBean<Object>> Delete(@Body RequestBody params);

    @POST("/DrSick/BloodPressure/GetALLList")
    Observable<BaseBackBean<BloodPressureListModel>> GetALLList(@Body RequestBody params);


}
