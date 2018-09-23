package com.kjipo.eartraining;


import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.eartrainer.EarTrainerImpl;
import com.kjipo.eartraining.midi.MidiPlayer;
import com.kjipo.eartraining.midi.MidiPlayerInterface;
import com.kjipo.eartraining.midi.sonivox.SonivoxMidiPlayer;
import com.kjipo.eartraining.midistream.MidiStream;
import com.kjipo.eartraining.recorder.Recorder;

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
//        return new MidiStream();
        return new SonivoxMidiPlayer();
    }


    @Provides
    @Singleton
    public Recorder provideRecorder() {
        return new Recorder();
    }


}
