package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.fealMove.FealMoveGetALLList;
import com.conagra.mvp.model.FetalMoveListModel;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.ui.view.FealMoveListView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class FealMoveListPresenter extends BasePresenter<FealMoveListView> {


    @Inject
    @Named("fealMoveGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private int page = 1;
    private int rows = 100;

    @Inject
    public FealMoveListPresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void GetALLList() {

        Map map = new HashMap();
        map.put(FealMoveGetALLList.CUSTOMER_ID, userId);
        map.put(FealMoveGetALLList.PAGE, page);
        map.put(FealMoveGetALLList.ROWS, rows);
        map.put(FealMoveGetALLList.BEGIN_DATE, null);
        map.put(FealMoveGetALLList.END_DATA, null);
        getView().showLoading();
        mUCGetAllList.execute(new MySubscriber<FetalMoveListModel>(){
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showServerError("网络异常");
            }
            @Override
            public void onBackError(String message) {
                super.onBackError(message);
                getView().hideLoading();
                getView().showServerError(message);
            }
            @Override
            public void onBackNext(FetalMoveListModel model) {
                getView().hideLoading();
                getView().renderAllData(model,page > 1);
            }

        }, map);
    }

}
