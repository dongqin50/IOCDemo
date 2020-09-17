package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.di.interactor.temple.TempleGetALLList;
import com.conagra.mvp.model.MySubscriber;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.ui.view.TempleListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

public class TempleListPresenter extends BasePresenter<TempleListView> {

    @Inject
    public TempleListPresenter() {
    }

    @Inject
    @Named("templeGetAllList")
    UseCase mUCGetAllList;

    private String userId;
    private int page = 1;
    private int rows = 100;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void GetALLList() {

        Map map = new HashMap();
        map.put(TempleGetALLList.CUSTOMER_ID, userId);
        map.put(TempleGetALLList.PAGE, page);
        map.put(TempleGetALLList.ROWS, rows);
        map.put(TempleGetALLList.BEGIN_DATE, null);
        map.put(TempleGetALLList.END_DATA, null);
        getView().showLoading();
        mUCGetAllList.execute(new MySubscriber<List<TempleListModel>>(){
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
            public void onBackNext(List<TempleListModel> model) {
                getView().hideLoading();
                getView().renderAllData(model,page > 1);
            }

        }, map);
    }

}
