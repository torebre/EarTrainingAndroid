package com.kjipo.eartrainingandroid;


import android.app.Application;


public class EarTrainingApplication extends Application {
    private MainComponent mainComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        mainComponent = DaggerMainComponent.create();
    }


    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
