package com.kjipo.eartraining.midistream;


import android.media.midi.MidiReceiver;

import com.kjipo.eartraining.synth.SynthEngine;

import java.io.IOException;

public class Synthesizer extends MidiReceiver {
private final SynthEngine synthEngine;

    public Synthesizer() {
        synthEngine = new SynthEngine();

    }


    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        synthEngine.onSend(msg, offset, count, timestamp);
    }


    public void start() {
        synthEngine.start();
    }

    public void stop() {
        synthEngine.stop();
    }
}
