package com.conagra.mvp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * 倒计时
 */
public class CountDownService extends Service {

    private boolean isStarting;

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    //
    public IBinder onBind(Intent intent) {

        return new CountDownBinder();
    }

    public Observable getCode(final Context context, final int num) {
        isStarting = true;
        return Observable.interval(1000, TimeUnit.MILLISECONDS)
                .takeWhile(new Predicate<Long>() {

                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < num && isStarting;
                    }
                })
                .map(new Function<Long,Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return aLong;
                    }
                }).subscribeOn(Schedulers.io());
    }

    /**
     * 倒计时
     */
    public class CountDownBinder extends Binder {
        public CountDownService getCountDownService(){
            return CountDownService.this;
        }
    }

}
