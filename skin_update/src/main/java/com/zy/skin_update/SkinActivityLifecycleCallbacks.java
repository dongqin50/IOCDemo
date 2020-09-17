package com.zy.skin_update;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import com.zy.skin_update.utils.SkinManager;

import java.lang.reflect.Field;

public class SkinActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        try {
            Field mFactorySet = LayoutInflater.class.getDeclaredField("mFactorySet");
            mFactorySet.setAccessible(true);
            mFactorySet.setBoolean(layoutInflater,false);
            layoutInflater.setFactory2(new SkinFactory(activity.getPackageName()+activity.getLocalClassName()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.v("aaaadddd"," -------------- onActivityStarted  ---------------------");

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.v("aaaadddd"," -------------- onActivityResumed  ---------------------");

    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.v("aaaadddd"," -------------- onActivityPaused  ---------------------");

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.v("aaaadddd"," -------------- onActivityStopped  ---------------------");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.v("aaaadddd"," -------------- onActivitySaveInstanceState  ---------------------");

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.v("aaaadddd"," -------------- onActivityDestroyed  ---------------------");
        String name = activity.getPackageName()+activity.getLocalClassName();
        SkinManager.getInstance().remove(name);

    }
}
