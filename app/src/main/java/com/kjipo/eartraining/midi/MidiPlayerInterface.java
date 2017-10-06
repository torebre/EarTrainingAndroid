package com.kjipo.eartraining.midi;


import android.content.Context;

public interface MidiPlayerInterface {

    void noteOn(int pitch);

    void noteOff(int pitch);

    void setup(Context applicationContext);

    void start();

    void stop();

}
