package com.conagra.di.module;

import android.app.Activity;
import android.content.Context;

import com.conagra.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yedongqin on 2017/10/29.
 */

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity(){
        return this.mActivity;
    }

    @Provides
    @PerActivity
    public Context provideContext(){
        return this.mActivity;
    }

}
