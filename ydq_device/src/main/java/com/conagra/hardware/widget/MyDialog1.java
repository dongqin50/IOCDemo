package com.conagra.hardware.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.conagra.R;
import com.conagra.databinding.DialogCustom1Binding;
import com.conagra.mvp.utils.LogMessage;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;


/**
 * Created by yedongqin on 16/9/17.
 */
public class MyDialog1 extends Dialog {

    private DialogCustom1Binding mBinding;
    private Observable mCancleObservable;

    private Observable mSureObservable;

    private Observable mShowObservable;

    public MyDialog1(Context context) {
        this(context, R.style.Dialog);
    }

    public MyDialog1(Context context, int themeResId) {
        super(context,themeResId);
        init(context);
    }

    private void init(Context context){

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_custom1,null,false);
        setContentView(mBinding.getRoot());
        LogMessage.doLogMessage(MyDialog1.class.getSimpleName(),"");
        mCancleObservable = RxView.clicks(mBinding.dialog1Cancle);
//                .throttleFirst(1000, TimeUnit.MILLISECONDS);

        mSureObservable = RxView.clicks(mBinding.dialog1Sure);
//                .throttleFirst(1000, TimeUnit.MILLISECONDS);

        mShowObservable = RxView.clicks(mBinding.dialog1Show);
//                .throttleFirst(1000, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
    }

    /**
     * 取消
     * @return
     */
    public Observable doCancleEvent(){
        return mCancleObservable;
    }

    /**
     * 取消
     * @return
     */
    public Observable doCancleEvent(String title) {
        mBinding.negativeButton.setText(title);
        return mCancleObservable;
    }

    public void setmTvCancleTitle(String title) {
        mBinding.negativeButton.setText(title);
    }

    public void setmTvSureTitle(String title) {
        mBinding.positiveButton.setText(title);
    }

    /**
         * 确定
         * @return
         */
    public Observable doSureEvent(){
        return mSureObservable;
    }
     /**
         * 确定
         * @return
         */
    public Observable doSureEvent(String title){
        mBinding.positiveButton.setText(title);
        return mSureObservable;
    }

    public void setDialogTitle(String content){
        mBinding.dialog1Message.setText(content);
    }

    /**
     * 设置消息信息
     * @param content
     */
    public void setContent(String content){
        mBinding.dialog1Choose.setVisibility(View.VISIBLE);
        mBinding.dialog1Show.setVisibility(View.GONE);
        mBinding.dialog1Message.setText(content);
    }

     /**
     * 设置消息信息
     * @param content
     */
    public Observable doShowContent(String content){
        mBinding.dialog1Choose.setVisibility(View.GONE);
        mBinding.dialog1Show.setVisibility(View.VISIBLE);
        mBinding.dialog1ShowContent.setText(content);

        return mShowObservable;
    }



}
