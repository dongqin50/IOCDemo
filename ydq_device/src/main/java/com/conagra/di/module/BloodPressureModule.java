package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.bloodpressure.BloodPressureCreate;
import com.conagra.di.interactor.bloodpressure.BloodPressureDelete;
import com.conagra.di.interactor.bloodpressure.BloodPressureEdit;
import com.conagra.di.interactor.bloodpressure.BloodPressureGetALLList;
import com.conagra.di.interactor.bloodpressure.BloodPressureIsExist;
import com.conagra.di.repository.BloodPressureRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@Module
public class BloodPressureModule {

    /**
     * 当天是否已经测量过血压
     *
     * @param repository
     * @return
     */
    @Provides
    @PerActivity
    @Named("bloodPressureIsExit")
    public UseCase provideBloodPressureIsExit(BloodPressureRepository repository) {
        return new BloodPressureIsExist(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodPressureCreate")

    public UseCase provideBloodPressureCreate(BloodPressureRepository repository) {
        return new BloodPressureCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodPressureEdit")
    public UseCase provideBloodPressureEdit(BloodPressureRepository repository) {
        return new BloodPressureEdit(repository);

    }

    @Provides
    @PerActivity
    @Named("bloodPressureDelete")
    public UseCase provideBloodPressureDelete(BloodPressureRepository repository) {
        return new BloodPressureDelete(repository);
    }

    @Provides
    @PerActivity
    @Named("bloodPressureGetAllList")
    public UseCase provideBloodPressureGetAllList(BloodPressureRepository repository) {
        return new BloodPressureGetALLList(repository);
    }
}
