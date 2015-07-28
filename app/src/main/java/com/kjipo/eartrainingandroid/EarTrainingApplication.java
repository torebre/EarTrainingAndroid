package com.kjipo.eartrainingandroid;


import android.app.Application;

import dagger.ObjectGraph;


public class EarTrainingApplication extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MainModule());
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

}
