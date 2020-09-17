package com.conagra.di.repository;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.FetalHeartModel;
import com.conagra.mvp.model.HeartListModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealHeartRepository {


    @Multipart
    @POST("/Patient/FetalHeart/Create")
    Observable<BaseBackBean<FetalHeartModel>> Create(
            @Part("CustomerID") RequestBody CustomerID,
            @Part("StartTime") RequestBody StartTime,
            @Part("Times") RequestBody Times,
            @Part("AvgHeartRate") RequestBody AvgHeartRate,
            @Part("PiontValue") RequestBody PiontValue,
            @Part("PiontUterine") RequestBody PiontUterine,
            @Part MultipartBody.Part file);

    @POST("/FetalHeart/Delete")
    Observable<BaseBackBean<Object>> Delete(@Body RequestBody params);

    @POST("/DrSick/FetalHeart/GetALLList")
    Observable<BaseBackBean<HeartListModel>> GetALLList(@Body RequestBody params);

    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Url String url);
//    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);
}
