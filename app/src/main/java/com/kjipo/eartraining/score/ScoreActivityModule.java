package com.kjipo.eartraining.score;

import com.kjipo.eartraining.AppModule;
import com.kjipo.eartraining.FragmentBuildersModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ScoreActivityModule {

    @ContributesAndroidInjector(modules = {AppModule.class, FragmentBuildersModule.class})
    abstract ScoreActivity contributeMainActivity();
}
