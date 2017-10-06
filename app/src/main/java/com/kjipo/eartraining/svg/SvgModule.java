package com.kjipo.eartraining.svg;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SvgModule {


    public SvgModule() {

    }


    @Provides
    @Singleton
    public SequenceToSvg getSequenceToSvg() {
        return new SequenceToSvg();
    }

}
