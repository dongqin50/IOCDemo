package com.conagra.di.component;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.BloodPressureModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.activity.BloodPressureActivity;
import com.conagra.hardware.activity.BloodPressureDetailActivity;
import com.conagra.hardware.activity.BloodPressureListActivity;

import dagger.Component;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class,BloodPressureModule.class, HardwareManagerModule.class})
public interface BloodPressureComponent extends ActivityComponent {
    void inject(BloodPressureActivity activity);
    void inject(BloodPressureListActivity activity);
    void inject(BloodPressureDetailActivity activity);
}
