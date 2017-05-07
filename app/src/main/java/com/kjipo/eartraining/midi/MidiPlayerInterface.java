package com.kjipo.eartraining.midi;


public interface MidiPlayerInterface {

    void playSequence(byte midiData[]) throws InterruptedException;

}
