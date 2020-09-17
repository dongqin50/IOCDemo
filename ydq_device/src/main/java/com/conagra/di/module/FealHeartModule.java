package com.conagra.di.module;

import com.conagra.di.PerActivity;
import com.conagra.di.UseCase;
import com.conagra.di.interactor.fealheart.FealHeartCreate;
import com.conagra.di.interactor.fealheart.FealHeartDelete;
import com.conagra.di.interactor.fealheart.FealHeartGetALLList;
import com.conagra.di.interactor.fealheart.FileDownload;
import com.conagra.di.repository.FealHeartRepository;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dongqin.Ye on 2017/11/2.
 */

@Module
public class FealHeartModule {


    @Provides
    @PerActivity
    @Named("fealHeartCreate")
    public UseCase provideFealHeartCreate(FealHeartRepository repository) {
        return new FealHeartCreate(repository);
    }

    @Provides
    @PerActivity
    @Named("fealHeartDelete")
    public UseCase provideFealHeartDelete(FealHeartRepository repository) {
        return new FealHeartDelete(repository);
    }

    @Provides
    @PerActivity
    @Named("fealFileDownload")
    public UseCase provideFileDownload(FealHeartRepository repository) {
        return new FileDownload(repository);
    }

    @Provides
    @PerActivity
    @Named("fealHeartGetAllList")
    public UseCase provideFealHeartGetAllList(FealHeartRepository repository) {
        return new FealHeartGetALLList(repository);
    }

}
