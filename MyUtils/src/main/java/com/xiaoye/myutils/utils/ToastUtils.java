package com.xiaoye.myutils.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.xiaoye.myutils.R;
import com.xiaoye.myutils.toast.BaseToast;
import com.xiaoye.myutils.toast.CustomToast;

/**
 * Created by yedongqin on 2017/4/12.
 */

public class ToastUtils implements BaseToast.ToastEventListerner{

    public final static int TOAST_TYPE_PROPS = 1;   //原生的Toast
    public final static int TOAST_TYPE_CUSTOME_1 = 2;   //自定义Toast，第一种方案
    private static ToastUtils mToastUtils;
    private Toast mToast;
    /**
     * 获取当前的toastType类型
     */
    public int mToastType;

    /**
     * 创建ToastUtils工具的单例子
     * @return
     */
    public static ToastUtils getInstance(){
        if(mToastUtils == null){
            synchronized (ToastUtils.class){
                if(mToastUtils == null){
                    mToastUtils = new ToastUtils();
                }
            }
        }
        mToastUtils.init();
       return mToastUtils;
    }

    private  void init(){
        mToastType = Integer.parseInt(PropertiesUtils.getInstance("setting").getProperties().getProperty("toastType"));
        Log.v("aa","m");
    }

    /**
     * 弹出原生toast
     * @param context
     * @param text
     * @param duration
     * @param toastType  对应toast的类型
     */
    public void makeText(Context context,CharSequence text,int duration,int toastType){
        switch (toastType){
            case TOAST_TYPE_CUSTOME_1:
                mToast = CustomToast.makeText(context,text,duration, R.layout.ul_item_content,R.id.item_content,this);
                break;
            case TOAST_TYPE_PROPS:
                mToast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
                break;
        }

        if(mToast != null && !isShowing()){
            mToast.show();
        }
    }

    /**
     * 弹出原生toast
     * @param context
     * @param text
     */
    public void makeText(Context context,CharSequence text){
        makeText(context,text,Toast.LENGTH_SHORT,mToastType);
    }

    /**
     * 弹出原生toast
     * @param context
     * @param text
     */
    public void makeText(Context context,String text,int duration){
        makeText(context,text,Toast.LENGTH_SHORT,mToastType);
    }

    /**
     * 弹出原生toast
     * @param context
     * @param text
     * @param toastType 对应toast的类型
     */
    public void makeText(Context context,CharSequence text,int toastType){
        makeText(context,text,Toast.LENGTH_SHORT,toastType);
    }

    @Override
    public void handlerEventBeforeShow() {
        if(mToast != null && mToast instanceof BaseToast){
            if(isShowing()){
                mToast.cancel();
            }
            mToast = null;
        }
    }

    @Override
    public void handlerEventAfterCancle() {
        handlerEventBeforeShow();
    }

    private boolean isShowing(){
        if(mToast != null && mToast instanceof BaseToast){
            return ((BaseToast) mToast).isShowing();
        }
        return false;
    }
}
