package com.zy.skin_update;

import android.app.Application;

import com.zy.skin_update.utils.SkinManager;

public class SkinApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.newInstance(this,getResources());
        registerActivityLifecycleCallbacks(new SkinActivityLifecycleCallbacks());
    }
}
