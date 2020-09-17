package com.conagra.di.component;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.di.module.TempleModule;
import com.conagra.hardware.activity.TempleActivity;
import com.conagra.hardware.activity.TempleListActivity;

import dagger.Component;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class, TempleModule.class, HardwareManagerModule.class})
public interface TempleComponent extends ActivityComponent {
    void inject(TempleActivity activity);
    void inject(TempleListActivity activity);
}
