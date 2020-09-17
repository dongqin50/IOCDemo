package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerCreate;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerDelete;
import com.conagra.di.interactor.hardwaremanager.HardwareManagerGetDeviceByMac;
import com.conagra.di.repository.HardwareManagerRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yedongqin on 2018/6/28.
 */
@Module
public class HardwareManagerModule {


    @Provides
    @PerActivity
    @Named("hardwareManagerCreate")
    public UseCase provideHardwareManagerCreate(HardwareManagerRepository repository) {
        return new HardwareManagerCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("hardwareManagerDelete")
    public UseCase provideHardwareManagerDelete(HardwareManagerRepository repository) {
        return new HardwareManagerDelete(repository);
    }


    @Provides
    @PerActivity
    @Named("hardwareManagerGetDeviceByMac")
    public UseCase provideHardwareManagerGetDeviceByMac(HardwareManagerRepository repository) {
        return new HardwareManagerGetDeviceByMac(repository);
    }


}
