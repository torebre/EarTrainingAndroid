package com.kjipo.eartraining.midistream;

import android.content.Context;

import com.kjipo.eartraining.midi.MidiMessages;
import com.kjipo.eartraining.midi.MidiPlayerInterface;

import java.io.IOException;


public class MidiStream implements MidiPlayerInterface {
    private Synthesizer synthesizer;


    @Override
    public void noteOn(int pitch) {
        byte command[] = new byte[3];
        command[0] = (byte)MidiMessages.NOTE_ON.message;
        command[1] = (byte)pitch;
        command[2] = 127;
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void noteOff(int pitch) {
        byte command[] = new byte[3];
        command[0] = (byte)MidiMessages.NOTE_OFF.message;
        command[1] = (byte)pitch;
        command[2] = 127;
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup(Context applicationContext) {
        synthesizer = new Synthesizer();
    }

    @Override
    public void start() {
        synthesizer.start();
    }

    @Override
    public void stop() {
        synthesizer.stop();
    }
}
