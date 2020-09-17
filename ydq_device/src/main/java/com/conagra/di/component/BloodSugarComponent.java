package com.conagra.di.component;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.BloodSugarModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.activity.BloodSugarActivity;
import com.conagra.hardware.activity.BloodSugarDetailActivity;
import com.conagra.hardware.activity.BloodSugarListActivity;

import dagger.Component;

/**
 * Created by yedongqin on 2017/10/29.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class,BloodSugarModule.class, HardwareManagerModule.class})
public interface BloodSugarComponent extends ActivityComponent {
    void inject(BloodSugarActivity activity);
    void inject(BloodSugarListActivity activity);
    void inject(BloodSugarDetailActivity activity);
}
