package com.zy.base_core;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;

public interface BasePluginLifeCycle {

    void attach(Activity proxyActivity);

    void onCreate(Bundle savedInstanceState);

//    void setContentView(int layoutResID);
//    void setContentView(View view);

    void onRestart();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onPostCreate(Bundle savedInstanceState) ;

    void setTheme(@StyleRes int resid);
//    void onSaveInstanceState(Bundle outState);
//
//    void onRestoreInstanceState(Bundle savedInstanceState);

    //当指定了android:configChanges="orientation"后,方向改变时onConfigurationChanged被调用
    void onConfigurationChanged(Configuration newConfig);

    boolean onTouchEvent(MotionEvent event);
}
