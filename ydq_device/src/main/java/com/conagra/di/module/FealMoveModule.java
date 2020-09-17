package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.fealMove.FealMoveCreate;
import com.conagra.di.interactor.fealMove.FealMoveGetALLList;
import com.conagra.di.interactor.fealMove.FealMoveGetList;
import com.conagra.di.repository.FealMoveRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@Module
public class FealMoveModule {


    @Provides
    @PerActivity
    @Named("fealMoveCreate")

    public UseCase provideFealMoveCreate(FealMoveRepository repository) {
        return new FealMoveCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("fealMoveGetAllList")
    public UseCase provideFealMoveGetAllList(FealMoveRepository repository) {
        return new FealMoveGetALLList(repository);
    }

    @Provides
    @PerActivity
    @Named("fealMoveGetList")
    public UseCase provideFealMoveGetList(FealMoveRepository repository) {
        return new FealMoveGetList(repository);
    }


}
