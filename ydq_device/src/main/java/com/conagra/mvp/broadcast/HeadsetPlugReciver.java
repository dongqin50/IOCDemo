package com.conagra.mvp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yedongqin on 16/9/19.
 */
public class HeadsetPlugReciver extends BroadcastReceiver {

    //耳机插入通知
    public static Observable observableHeadsetInsert;
    //耳机拔出通知
    public static Observable observableHeadsetExtract;

    public static boolean isHeadSet;

//    public static boolean isHeadSet() {
//
////        if(!isHeadSet){
////            AudioManager audioManager  = (AudioManager) ActivityManageUtils.getCurrentContext().getSystemService(Context.AUDIO_SERVICE);
////            isHeadSet = audioManager.isWiredHeadsetOn();
////        }
//        return isHeadSet;
//    }

    /**
     * 耳机插拔监听
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case Intent.ACTION_HEADSET_PLUG:
                int state = intent.getIntExtra("state",0);
                //耳机拔出
                if(state == 0){
                    isHeadSet = false;
                    observableHeadsetExtract = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            subscriber.onNext("耳机已拔出");
                        }
                    });
                }else{
                    isHeadSet = true;
                    observableHeadsetInsert = Observable.create(new Observable.OnSubscribe<String>() {
                        @Override
                        public void call(Subscriber<? super String> subscriber) {
                            subscriber.onNext("耳机已插入");
                        }
                    });
                }
                break;

        }
    }
}
