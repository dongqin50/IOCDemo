package com.conagra.di.component;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.FealHeartModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.activity.FetalHeartActivity;
import com.conagra.hardware.activity.HeartListActivity;
import com.conagra.hardware.activity.VideoPlayActivity;

import dagger.Component;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class,FealHeartModule.class, HardwareManagerModule.class})
public interface FealHeartComponent extends ActivityComponent {
    void inject(FetalHeartActivity activity);
    void inject(HeartListActivity activity);
    void inject(VideoPlayActivity activity);
}
