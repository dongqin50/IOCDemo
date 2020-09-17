package com.conagra.di.component;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.FealMoveModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.activity.FetalMoveActivity;
import com.conagra.hardware.activity.FetalMoveListActivity;

import dagger.Component;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class,FealMoveModule.class, HardwareManagerModule.class})
public interface FealMoveComponent extends ActivityComponent {
    void inject(FetalMoveActivity activity);
    void inject(FetalMoveListActivity activity);
}
