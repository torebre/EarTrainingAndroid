package com.kjipo.eartrainingandroid.midi;


public interface MidiPlayerInterface {

    void playSequence(byte midiData[]) throws InterruptedException;

}
