package com.conagra.di.module;

import com.conagra.LocalConfig;
import com.conagra.MainApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yedongqin on 2017/9/30.
 */
@Module
public class ApplicationModule {

    private MainApplication application;

    public ApplicationModule(MainApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    MainApplication provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    LocalConfig provideLocalConfig(){
        return new LocalConfig(application);
    }

}
