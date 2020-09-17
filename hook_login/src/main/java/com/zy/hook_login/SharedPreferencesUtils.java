package com.zy.hook_login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static SharedPreferencesUtils mInstance;
    private static Application mApplication;
    private static SharedPreferences mSharedPreferences;
    private final static String mSharePreferencesName = "zy_data";
    private final static String SHARE_PREFERENCE_IS_LOGIN = "isLogin";
    public static void init(Application application){
        if(null == mInstance){
            synchronized (SharedPreferencesUtils.class){
                if(null == mInstance){
                    mApplication = application;
                    mSharedPreferences = application.getSharedPreferences(mSharePreferencesName, Context.MODE_PRIVATE);
                    mInstance = new SharedPreferencesUtils();
                }
            }
        }

    }

    public static SharedPreferencesUtils getInstance() {
        return mInstance;
    }

    /**
     * 是否登录
     * @return
     */
    public boolean isLogin(){
       return mSharedPreferences.getBoolean(SHARE_PREFERENCE_IS_LOGIN,false);
    }

    public void setLogin(boolean login){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SHARE_PREFERENCE_IS_LOGIN,login);
        editor.apply();
    }
}
