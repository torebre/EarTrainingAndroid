package com.kjipo.eartrainingandroid.midi;


import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.Gson;
import com.kjipo.eartrainingandroid.data.Note;
import com.kjipo.eartrainingandroid.data.Sequence;

import java.util.ArrayList;
import java.util.List;


public final class MidiUtilities {


    public static byte[] transformSequenceToMidiFormat(Sequence sequence) {
        MidiTrackBuilder builder = MidiTrackBuilder.createNewInstance();
        builder.setTempo(sequence.getTempoInMillisecondsPerQuarterNote());
        int currentPoint = 0;
        Multimap<Integer, Note> pendingOffEvents = MultimapBuilder.treeKeys().arrayListValues().build();


        for (Note note : sequence.getNotes()) {

            switch (note.getElementType()) {
                case BAR_LINE:
                    currentPoint = 0;

                default:
                    if (note.getStartWithinBar() < currentPoint) {
                        // We make an assumtion that the notes are ordered by
                        // increasing start points
                        throw new RuntimeException("At point: " + currentPoint + ". Going backwards in: " + sequence);
                    }
                    currentPoint = note.getStartWithinBar();
            }


// TODO Handle off events
//            Map.Entry<Integer, Collection<Note>> offEvents =


            // TODO Is 0 a valid pitch?
            if (note.getPitch() >= 0) {
                builder.addNoteOn(note.getPitch(), note.getVelocity());
                pendingOffEvents.put(note.getStartWithinBar() + note.getDurationWithinBar(), note);
            }


        }


        // TODO Handle remaining off events

        return builder.build();
    }


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
        for (int i = 36; i < midiData.length - 4; ++i) {
//            byte currentByte = midiData[i];

            deltaTime = midiData[i];
            if (deltaTime != 0) {
                cumulativeTime += deltaTime;
                currentPlayerEvent = new PlayerEvent(cumulativeTime);
                playerEvents.add(currentPlayerEvent);
            }

            if (midiData[i] == MidiMessages.NOTE_ON.getMessageAsByte()) {
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


//        ByteArrayOutputStream output = new ByteArrayOutputStream(midiMessages.size()); // TODO Should have a little more initial capacity
//
//        try {
//            output.write(new byte[]{
//                    0x4d, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06,
//                    0x00, 0x00,
//                    0x00, 0x01,
//                    0x00, (byte) ticksPerQuarterNote,
//            });
//
//            // Write header
//            output.write(new byte[]{0x4D, 0x54, 0x72, 0x6B});
//
//            byte tempo[] = splitLengthIntoBytes(microsecondsPerMidiQuarterNote);
//            byte tempoAndTimeSignature[] = new byte[]{
//                    // Tempo
//                    0x00, (byte) 0xFF, 0x51, 0x03, tempo[1], tempo[2], tempo[3],
//                    // Time signature
//                    0x00, (byte) 0xFF, 0x58, 0x04, (byte) timeSignatureNominator, (byte) timeSignatureDenominator, 24, 8, // TODO Figure out the meaning of the last two numbers
//            };
//
//            // Now write the length of the MIDI messages
//            output.write(splitLengthIntoBytes(midiMessages.size() + 4 + tempoAndTimeSignature.length));
//            output.write(tempoAndTimeSignature);
//
//            // Write the midi messages
//            for (int message : midiMessages) {
//                output.write((byte) message);
//            }
//
//            // Write end of track
//            output.write(new byte[]{0x01, (byte) 0xFF, 0x2F, 0x00});
//        } catch (IOException e) {
//            throw new IllegalStateException(e);
//        }

    }


}
