package com.conagra.di.repository;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.HardwareManagerCreateModel;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yedongqin on 2018/6/28.
 */

public interface HardwareManagerRepository {

    @POST("/DrSick/Device/Create")
    Observable<BaseBackBean<HardwareManagerCreateModel>> create(@Body RequestBody params);

    @POST("/DrSick/Device/GetDeviceByMac")
    Observable<BaseBackBean<Boolean>> getDeviceByMac(@Body RequestBody params);


    @POST("/DrSick/Device/Delete")
    Observable<BaseBackBean<Object>> delete(@Body RequestBody params);

}
