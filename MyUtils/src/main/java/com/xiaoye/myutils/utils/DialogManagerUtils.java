package com.xiaoye.myutils.utils;

import android.content.Context;

import com.xiaoye.myutils.dialog.InternetRequestDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yedongqin on 2018/4/14.
 */

public class DialogManagerUtils {

    private InternetRequestDialog mRequestDialog;
    private static DialogManagerUtils mDialogManagerUtils;
//    private long duration = 60 * 1000;
//    private static Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };

    public  static DialogManagerUtils getInstance(){
        if(mDialogManagerUtils == null){
            synchronized (DialogManagerUtils.class){
                if(mDialogManagerUtils == null) {
                    mDialogManagerUtils = new DialogManagerUtils();
                }
            }
        }
        return mDialogManagerUtils;
    }

    /**
     * 网络请求显示Dialog框
     * @param context
     */
    public void showRequestDialog(Context context){
        if(mRequestDialog != null){
            closeDialg();
        }
        mRequestDialog = new InternetRequestDialog(context, true, new InternetRequestDialog.OnDialogListener() {
            @Override
            public void closeDialog() {
                closeDialg();
            }
        });

        mRequestDialog.show();

       Observable.interval(60, TimeUnit.SECONDS)
                .take(1)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        closeDialg();
                    }
                });
//        closeDialgInDuration(duration);
    }

//    /**
//     * 在指定时间中关闭Dialog
//     * @param duration
//     */
//    private void closeDialgInDuration(long duration){
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                closeDialg();
//                mHandler.removeCallbacks(this);
//            }
//        },duration);
//    }

    /**
     * 网络异常显示Dialog框
     * @param context
     */
    public void showNetworkErrorDialog(Context context){
        if(mRequestDialog != null){
            closeDialg();
        }

        mRequestDialog = new InternetRequestDialog(context, false, new InternetRequestDialog.OnDialogListener() {
            @Override
            public void closeDialog() {
                closeDialg();
            }
        });
        mRequestDialog.show();
//        closeDialgInDuration(duration);
    }

    public boolean isShowing(){
        if(mRequestDialog == null){
            return false;
        }
        return mRequestDialog.isShowing();
    }

    public void closeDialg(){
        if(mRequestDialog != null){
            if(mRequestDialog.isShowing()){
                mRequestDialog.dismiss();
                mRequestDialog.removeDialog();
            }

            mRequestDialog = null;
        }
    }

    public void onDestory(){
//        InternetRequestDialogmHandler = null;
        mDialogManagerUtils = null;
    }
}
