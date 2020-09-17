package com.conagra.di.repository;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.model.FetalMoveListModel;
import com.conagra.mvp.model.FetalMoveListsModel;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public interface FealMoveRepository {


    @POST("/Patient/MoveMent/Create")
    Observable<BaseBackBean<Object>> Create(@Body RequestBody params);



    @POST("/DrSick/MoveMent/GetALLList")
    Observable<BaseBackBean<FetalMoveListModel>> GetALLList(@Body RequestBody params);

    @POST("/DrSick/MoveMent/GetMoveMentList")
    Observable<BaseBackBean<FetalMoveListsModel>> GetList(@Body RequestBody params);
}
