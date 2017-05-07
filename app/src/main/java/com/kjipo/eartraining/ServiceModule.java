package com.kjipo.eartraining;


import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.eartrainer.EarTrainerImpl;
import com.kjipo.eartraining.midi.MidiPlayer;
import com.kjipo.eartraining.midi.MidiPlayerInterface;

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
