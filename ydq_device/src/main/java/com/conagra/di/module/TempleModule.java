package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.temple.TempleCreate;
import com.conagra.di.interactor.temple.TempleDelete;
import com.conagra.di.interactor.temple.TempleEdit;
import com.conagra.di.interactor.temple.TempleGetALLList;
import com.conagra.di.interactor.temple.TempleIsExistMinuteData;
import com.conagra.di.repository.TempleRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@Module
public class TempleModule {

    /**
     * 当天是否已经测量过血糖
     *
     * @param repository
     * @return
     */
    @Provides
    @PerActivity
    @Named("templeIsExitMinuteData")
    public UseCase provideTempleIsExistMinuteData(TempleRepository repository) {
        return new TempleIsExistMinuteData(repository);
    }

    @Provides
    @PerActivity
    @Named("templeCreate")

    public UseCase provideTempleCreate(TempleRepository repository) {
        return new TempleCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("templeEdit")
    public UseCase provideTempleEdit(TempleRepository repository) {
        return new TempleEdit(repository);

    }

    @Provides
    @PerActivity
    @Named("templeDelete")
    public UseCase provideTempleDelete(TempleRepository repository) {
        return new TempleDelete(repository);
    }

    @Provides
    @PerActivity
    @Named("templeGetAllList")
    public UseCase provideTempleGetAllList(TempleRepository repository) {
        return new TempleGetALLList(repository);
    }
}
