package com.conagra.di.component;


import com.conagra.MessageActivity;
import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;
import com.conagra.di.module.MessageModule;

import dagger.Component;

/**
 * Created by yedongqin on 2018/5/5.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = {ActivityModule.class,MessageModule.class})
public interface MessageComponent extends ActivityComponent {
    void inject(MessageActivity activity);
}