package com.kjipo.eartraining.midi;


import android.util.Log;

import com.google.gson.Gson;
import com.kjipo.scoregenerator.Sequence;

import java.util.ArrayList;
import java.util.List;


public final class MidiUtilities {


    public static String transformSequenceToJson(Sequence sequence) {
        Gson gson = new Gson();
        return gson.toJson(sequence);
    }

    public static List<PlayerEvent> transformToPlayerEvents(byte midiData[]) {
        // TODO

        List<PlayerEvent> playerEvents = new ArrayList<>();


        // 14 bytes

        // Header 4 bytes

        // 4 bytes with length information

        // 15 bytes

        int deltaTime = 0;
        PlayerEvent currentPlayerEvent = null;
        long cumulativeTime = 0;


        // TODO For now just jump down to the MIDI messages
        for (int i = 37; i < midiData.length - 4; ++i) {

            deltaTime = midiData[i];
            if (deltaTime != 0) {
                cumulativeTime += deltaTime;
                currentPlayerEvent = new PlayerEvent(cumulativeTime);
                playerEvents.add(currentPlayerEvent);
            }

            if (midiData[i] == MidiMessages.NOTE_ON.getMessageAsByte()) {

                Log.i("Midi", "Note on: " + midiData[i]);

                byte pitch = midiData[i++];
                // TODO Use velocity
                ++i;
                currentPlayerEvent.addOnPitch(pitch);
            } else if (midiData[i] == MidiMessages.NOTE_OFF.getMessageAsByte()) {
                byte pitch = midiData[i++];
                // TODO Use velocity
                ++i;
                currentPlayerEvent.addOffPitch(pitch);
            }
        }

        return playerEvents;
    }


    public static double pitchToFrequency(double pitch) {
        double semitones = pitch - MidiConstants.CONCERT_A_PITCH;
        return MidiConstants.CONCERT_A_FREQUENCY * Math.pow(2.0, semitones / 12.0);
    }


}
