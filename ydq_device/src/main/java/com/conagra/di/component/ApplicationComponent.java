package com.conagra.di.component;

import android.databinding.DataBindingComponent;

import com.conagra.LocalConfig;
import com.conagra.MainApplication;
import com.conagra.di.module.ApplicationModule;
import com.conagra.di.module.NetModule;
import com.conagra.di.repository.BloodPressureRepository;
import com.conagra.di.repository.BloodSugarRepository;
import com.conagra.di.repository.FealHeartRepository;
import com.conagra.di.repository.FealMoveRepository;
import com.conagra.di.repository.HardwareManagerRepository;
import com.conagra.di.repository.TempleRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yedongqin on 2017/9/30.
 */
@Singleton
@Component(modules = {ApplicationModule.class, NetModule.class})
public interface ApplicationComponent extends DataBindingComponent {


    MainApplication getApplication();
    BloodSugarRepository getBloodSugarRepository();
    BloodPressureRepository getBloodPressureRepository();
    FealHeartRepository getFealHeartRepository();
    FealMoveRepository getFealMoveRepository();
    TempleRepository getTempleRepository();
    HardwareManagerRepository getHardwareManagerRepository();
    LocalConfig getLocalConfig();

}
