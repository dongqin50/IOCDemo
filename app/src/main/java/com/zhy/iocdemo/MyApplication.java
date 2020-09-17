package com.zhy.iocdemo;

import android.app.Application;

import com.zy.skin_update.utils.SkinManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.newInstance(this,getResources());
//        registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallbacks());
    }
}
