package com.kjipo.eartrainingandroid;


import com.kjipo.eartrainingandroid.eartrainer.EarTrainer;
import com.kjipo.eartrainingandroid.eartrainer.EarTrainerImpl;
import com.kjipo.eartrainingandroid.midi.MidiPlayer;
import com.kjipo.eartrainingandroid.midi.MidiPlayerInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {


    @Provides
    @Singleton
    public EarTrainer provideEarTrainer() {
        return new EarTrainerImpl();
    }


    @Provides
    @Singleton
    public MidiPlayerInterface provideMidiPlayer() {
        return new MidiPlayer();
    }


}
