package com.kjipo.eartraining.midi;


public interface MidiPlayerInterface {

    void noteOn(int pitch);

    void noteOff(int pitch);

    void start();

    void stop();

}
