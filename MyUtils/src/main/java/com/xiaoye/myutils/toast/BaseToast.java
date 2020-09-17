package com.xiaoye.myutils.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by yedongqin on 2017/4/12.
 */

public class BaseToast extends Toast {

    public interface ToastEventListerner{

        void handlerEventBeforeShow();
        /**
         * 当Toast关闭时触发该事件
         */
        void handlerEventAfterCancle();
    }

    private boolean isShow;
    private ToastEventListerner mToastEventListener;
    public BaseToast(Context context,ToastEventListerner mToastEventListener) {
        super(context);
        this.mToastEventListener = mToastEventListener;
    }

    public boolean isShowing() {
        return isShow;
    }

    @Override
    public void show() {
        if(mToastEventListener != null){
            mToastEventListener.handlerEventBeforeShow();
        }
        super.show();
        isShow = true;
    }

    @Override
    public void cancel() {
        super.cancel();
        isShow = false;
        if(mToastEventListener != null){
            mToastEventListener.handlerEventAfterCancle();
        }
    }

}
