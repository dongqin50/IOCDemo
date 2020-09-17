package com.xiaoye.myutils.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by yedongqin on 2017/4/13.
 */

public abstract class BaseDialog extends Dialog  {

    public BaseDialog(Context context) {
        super(context);
        setContentView();
        initView();
        initData(context);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView();
        initView();
        initData(context);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView();
        initView();
        initData(context);
    }

    public abstract void initView();

    public abstract void initData(Context context);

    public abstract void setContentView();
}
