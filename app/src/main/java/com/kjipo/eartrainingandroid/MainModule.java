package com.kjipo.eartrainingandroid;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {MainActivity.class, SequenceGenerator.class}
)
public class MainModule {

    @Provides
    @SuppressWarnings("unused")
    public SequenceGenerator provideSequenceGenerator() {
        return new SequenceGenerator();
    }

}
