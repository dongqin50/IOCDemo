package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodsugar.BloodSugarCreate;
import com.conagra.di.interactor.bloodsugar.BloodSugarDelete;
import com.conagra.di.interactor.bloodsugar.BloodSugarEdit;
import com.conagra.di.interactor.bloodsugar.BloodSugarGetALLList;
import com.conagra.di.interactor.bloodsugar.BloodSugarIsExist;
import com.conagra.di.repository.BloodSugarRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yedongqin on 2017/10/29.
 */
@Module
public class BloodSugarModule {

    /**
     * 当天是否已经测量过血糖
     *
     * @param repository
     * @return
     */
    @Provides
    @PerActivity
    @Named("bloodSugarIsExit")
    public UseCase provideBloodSugarIsExit(BloodSugarRepository repository) {
        return new BloodSugarIsExist(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodSugarCreate")

    public UseCase provideBloodSugarCreate(BloodSugarRepository repository) {
        return new BloodSugarCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodSugarEdit")
    public UseCase provideBloodSugarEdit(BloodSugarRepository repository) {
        return new BloodSugarEdit(repository);

    }

    @Provides
    @PerActivity
    @Named("bloodSugarDelete")
    public UseCase provideBloodSugarDelete(BloodSugarRepository repository) {
        return new BloodSugarDelete(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodSugarGetAllList")
    public UseCase provideBloodSugarGetAllList(BloodSugarRepository repository) {
        return new BloodSugarGetALLList(repository);
    }
}
