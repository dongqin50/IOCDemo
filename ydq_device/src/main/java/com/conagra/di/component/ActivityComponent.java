package com.conagra.di.component;

import android.app.Activity;

import com.conagra.di.PerActivity;
import com.conagra.di.module.ActivityModule;

import dagger.Component;

/**
 * Created by yedongqin on 2017/10/29.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = ActivityModule.class)
public interface ActivityComponent extends android.databinding.DataBindingComponent {
    Activity activity();
}
