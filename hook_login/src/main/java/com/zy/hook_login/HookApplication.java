package com.zy.hook_login;

import android.app.Application;

public class HookApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HookManager.init();
        SharedPreferencesUtils.init(this);
        HookManager.getInstance().hookManager(this);
    }
}
