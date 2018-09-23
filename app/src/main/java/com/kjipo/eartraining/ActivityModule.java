package com.kjipo.eartraining;


import com.kjipo.eartraining.score.ScoreActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract ScoreActivity contributeScoreActivity();

}
