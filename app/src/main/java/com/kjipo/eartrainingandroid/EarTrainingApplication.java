package com.kjipo.eartrainingandroid;


import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;


public class EarTrainingApplication extends Application { // implements HasDispatchingActivityInjector {
//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private MainComponent mainComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        // TODO


    }


//    @Override
//    public DispatchingAndroidInjector<Activity> activityInjector() {
//        return null;
//    }
}
