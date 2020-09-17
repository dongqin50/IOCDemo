package com.conagra.mvp.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.conagra.hardware.model.VerificationCodeBackBean;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.utils.LogMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yedongqin on 16/9/25.
 */
public  class VerificationCodeService extends Service {

    private boolean flag;
    private VerificationCodeBackBean vcBean;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public VerificationCodeBackBean getVcBean() {
        return vcBean;
    }

    //
    public IBinder onBind(Intent intent) {

        return new VerifictionCodeBinder();
    }

    public Observable getCode(final Context context, final String mobile, final int modle, final BaseListener.Observable1<String, VerificationCodeBackBean> observable){
        flag = true;
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .takeWhile(new Predicate<Long>() {

                    @Override
                    public boolean test(Long aLong) throws Exception {
                        LogMessage.doLogErrorMessage("VerCode",aLong + " " + "flag " + flag);
                        return aLong < 90 && flag;
                    }
                })
                .map(new Function<Long,Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        if(aLong == 0){
                            //发送验证码
//                            Looper.prepare();
//                            UserNetWorkRequest.doRegister(mobile,m);

//                            UserNetWorkRequest.getVerificationCode(VerificationCodeService.this,mobile,modle,new MySubscriber<VerificationCodeBackBean>( VerificationCodeBackBean.class){
//
//                                @Override
//                                public void onBackError(String message) {
//                                    super.onBackError(message);
//                                    flag = false;
//                                    vcBean = null;
//                                    if(observable != null){
//                                        observable.onAction(message);
//                                    }
//                                }
//
//                                @Override
//                                public void onBackNext( VerificationCodeBackBean dataBean) {
//                                    super.onBackNext(dataBean);
//                                    boolean flag = false;
//                                    if(vcBean != null && dataBean != null){
//                                        vcBean = dataBean;
//                                        flag = true;
//                                    }
//
//                                    if(flag){
//                                        if(observable != null){
//                                            observable.onAction1(dataBean);
//                                        }
//                                    }else {
//                                        if(observable != null){
//                                            observable.onAction("没有数据");
//                                        }
//                                    }
//                                }
//                            });
//                        }
//                        if (aLong == 89) {
//                            flag = false;
//                            UserNetWorkRequest.doCallVerificationOver(VerificationCodeService.this,vcBean.getLSH(),new MySubscriber<String>(String.class){
//                                @Override
//                                public void onCompleted() {
//                                    super.onCompleted();
//                                    vcBean = null;
//                                }
//
//                                @Override
//                                public void onBackError(String message) {
//                                    super.onBackError(message);
//                                    vcBean = null;
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    vcBean = null;
//                                }
//                            });
                        }
                        return aLong;
                    }
                }).subscribeOn(Schedulers.io());
    }


    public void onDestory(){
        vcBean = null;
        flag = false;
    }

    public class VerifictionCodeBinder extends Binder {
        public VerificationCodeService getVerificationCodeService(){
            return VerificationCodeService.this;
        }
    }

}


