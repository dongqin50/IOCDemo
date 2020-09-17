package com.xiaoye.myutils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.xiaoye.myutils.R;
import com.xiaoye.myutils.databinding.DialogInternetRequestBinding;

/**
 * Created by yedongqin on 2018/4/14.
 */

public class InternetRequestDialog extends Dialog {
    private boolean isRequest;
    private Animation mRotateAnimation;
    private boolean isCloseBack;
    private Context mContext;
    protected DialogInternetRequestBinding mBinding;
//    protected UiDialogLoadingBinding dialogLoadingBinding;
//    private
    private static Handler mHandler = new Handler(){
    };
    private OnDialogListener mOnShowListener;
    public interface OnDialogListener{
        void closeDialog();
    }

    public InternetRequestDialog(Context context, boolean isRequest,OnDialogListener onShowListener) {
        this(context, R.style.Dialog,isRequest,onShowListener);

    }
//    public DialogInternetR mBinding;

    public InternetRequestDialog(Context context, int themeResId, boolean isRequest,OnDialogListener onShowListener) {
        super(context, themeResId);
        this.mContext = context;
        this.mOnShowListener = onShowListener;
        try{
//            setContentView(R.layout.dialog_internet_request);
            this.isRequest = isRequest;
//            ButterKnife.bind(this); ActivityTemplelistBinding
            mBinding = DataBindingUtil.bind(LayoutInflater.from(context).inflate(R.layout.dialog_internet_request,null));
            setContentView(mBinding.getRoot());
//            DataBindingUtil.bind(LayoutInflater.from(context).inflate(R.layout.dialog_internet_request,null));
            setCanceledOnTouchOutside(false);
            if(isRequest){
                mBinding.dialogRequestIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dialog_request));
                mBinding.dialogRequestTv.setText("加载中...");
                mRotateAnimation = AnimationUtils.loadAnimation(mContext,R.anim.dialog_rotate);
                mRotateAnimation.setInterpolator(new LinearInterpolator());
                mBinding.dialogRequestIv.startAnimation(mRotateAnimation);
            }else{
                mBinding.dialogRequestIv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.dialog_request_error));
                mBinding.dialogRequestTv.setText("网路请求超时");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setCloseBack(boolean closeBack) {
        isCloseBack = closeBack;
    }

    public void show(final long time){

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mOnShowListener != null && mContext != null){
                    mOnShowListener.closeDialog();
                }
                mHandler.removeCallbacks(this);
            }
        },time);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(this != null && isShowing() && keyCode == KeyEvent.KEYCODE_BACK && isCloseBack){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void removeDialog(){
        mRotateAnimation = null;
        mContext = null;
        mHandler = null;
    }
}
