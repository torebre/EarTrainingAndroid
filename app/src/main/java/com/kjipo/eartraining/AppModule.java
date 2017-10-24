package com.kjipo.eartraining;

import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.eartrainer.EarTrainerImpl;
import com.kjipo.eartraining.midi.MidiPlayerInterface;
import com.kjipo.eartraining.midistream.MidiStream;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {


    @Provides
    EarTrainer provideEarTrainer() {
        return new EarTrainerImpl();
    }

    @Provides
    MidiPlayerInterface provideMidiPlayer() {
        return new MidiStream();
    }



}
