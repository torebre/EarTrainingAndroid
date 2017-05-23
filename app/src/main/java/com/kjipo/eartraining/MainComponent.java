package com.kjipo.eartraining;


import com.kjipo.eartraining.score.ScoreActivity;
import com.kjipo.eartraining.svg.SvgModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ServiceModule.class, SvgModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);

    void inject(ScoreActivity scoreActivity);


}
