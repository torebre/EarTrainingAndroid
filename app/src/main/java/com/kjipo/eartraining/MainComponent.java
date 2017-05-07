package com.kjipo.eartraining;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ServiceModule.class)
public interface MainComponent {

    void inject(MainActivity mainActivity);


}
