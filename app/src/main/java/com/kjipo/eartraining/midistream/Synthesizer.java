package com.kjipo.eartraining.midistream;


import android.media.midi.MidiReceiver;

import java.io.IOException;

public class Synthesizer extends MidiReceiver {


    @Override
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {

    }
}
