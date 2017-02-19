package com.kjipo.eartrainingandroid;

import com.kjipo.eartrainingandroid.eartrainer.EarTrainer;
import com.kjipo.eartrainingandroid.eartrainer.EarTrainerImpl;
import com.kjipo.eartrainingandroid.eartrainer.SequenceGenerator;
import com.kjipo.eartrainingandroid.midi.MidiPlayer;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = {MainActivity.class, EarTrainer.class}
)
public class MainModule {

    @Provides
    @SuppressWarnings("unused")
    public EarTrainer provideEarTrainer() {
        return new EarTrainerImpl();
    }

    @Provides
    public MidiPlayer provideMidiPlayer() {
        return new MidiPlayer();
    }

}
