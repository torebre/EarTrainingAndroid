package com.kjipo.eartraining;


import android.app.Activity;
import android.app.Application;

import com.kjipo.eartraining.svg.SvgModule;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class EarTrainingApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;


    private static final String GLYPH_URL = "file:///android_asset/js/glyphs.json";


    @Override
    public void onCreate() {
        super.onCreate();

        AppInjector.init(this);

//        mainComponent = DaggerMainComponent.builder()
//                .serviceModule(new ServiceModule())
//                .svgModule(new SvgModule())
//                .build();


    }



    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
