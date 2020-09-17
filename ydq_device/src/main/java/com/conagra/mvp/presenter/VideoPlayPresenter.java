package com.conagra.mvp.presenter;

import com.conagra.di.UseCase;
import com.conagra.listener.HttpProgressOnNextListener;
import com.conagra.mvp.model.DownInfo;
import com.conagra.mvp.model.ProgressDownSubscriber;
import com.conagra.mvp.ui.view.VideoPlayView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by yedongqin on 2018/3/17.
 */

public class VideoPlayPresenter extends BasePresenter<VideoPlayView> {

    @Inject
    @Named("fealFileDownload")
    UseCase fealFileDownload;

    @Inject
    public VideoPlayPresenter() {
    }

    public void downLoad(DownInfo info){
        getView().showLoading();
        info.setListener(new HttpProgressOnNextListener() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onStart(){
            }

            @Override
            public void onComplete() {
                getView().hideLoading();
                getView().downloadSuccess(info.getSavePath());
            }

            @Override
            public void updateProgress(long readLength, long countLength) {

            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getView().hideLoading();
                getView().downloadError();
            }
        });
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);

        fealFileDownload.execute(info)
                .subscribe(subscriber);
    }

}
