package com.kjipo.eartraining;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ActivityModule.class, ServiceModule.class})
interface EarTrainingApplicationComponent extends AndroidInjector<EarTrainingApplication> {

}

