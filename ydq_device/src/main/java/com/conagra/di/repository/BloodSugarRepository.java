package com.conagra.di.repository;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.BloodSugarIsExistModel;
import com.conagra.mvp.model.BloodSugarListModel;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yedongqin on 2017/10/29.
 */

public interface BloodSugarRepository {

    /**
     * 当天是否已经测量过血糖
     * @param params
     * @return
     */
    @POST("/Patient/BloodSugar/isExistBloodSugar")
    Observable<BaseBackBean<BloodSugarIsExistModel>> isExistBloodSugar(@Body RequestBody params);


    @POST("/Patient/BloodSugar/Create")
    Observable<BaseBackBean<String>> BloodSugarCreate(@Body RequestBody params);


    @POST("/Patient/BloodSugar/Edit")
    Observable<BaseBackBean<String>> BloodSugarEdit(@Body RequestBody params);


    @POST("/Patient/BloodSugar/Delete")
    Observable<Object> BloodSugarDelete(@Body RequestBody params);


    @POST("/DrSick/BloodSugar/GetALLList")
    Observable<BaseBackBean<BloodSugarListModel>> BloodSugarGetALLList(@Body RequestBody params);

//
//    @POST("/DrSick/BloodSugar/GetWeekList")
//    Observable<Object> BloodSugarGetWeekList(@Body RequestBody params);
//
//
//    @POST("/DrSick/BloodSugar/GetList")
//    Observable<Object> BloodSugarGetList(@Body RequestBody params);
//
//
//    @POST("/DrSick/BloodSugar/GetUpDownList")
//    Observable<Object> BloodSugarGetUpDownList(@Body RequestBody params);
}
