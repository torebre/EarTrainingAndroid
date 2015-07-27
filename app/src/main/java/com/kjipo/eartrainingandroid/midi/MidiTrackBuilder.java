package com.kjipo.eartrainingandroid.midi;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class MidiTrackBuilder {
    private int timeSignatureNominator;
    private int timeSignatureDenominator;
    private int ticksPerQuarterNote;
    private int deltaWaiting = -1;
    private int microsecondsPerMidiQuarterNote;
    private List<Integer> midiMessages = new ArrayList<Integer>();


    private MidiTrackBuilder() {

    }


    public static MidiTrackBuilder createNewInstance() {
        return new MidiTrackBuilder();
    }


    public MidiTrackBuilder setTimeSignature(int nominator, int denominator) {
        timeSignatureNominator = nominator;
        // TODO Should this be a normal number in instead of giving the power of 2 directly?
        timeSignatureDenominator = denominator;
        return this;
    }


    public MidiTrackBuilder setTicksPerQuarterNote(int ticks) {
        if (ticks > 0xFF) {
            throw new IllegalArgumentException("Max number of ticks is currently 255");
        }
        ticksPerQuarterNote = ticks;
        return this;
    }


    public MidiTrackBuilder setTempo(int microsecondsPerMidiQuarterNote) {
        this.microsecondsPerMidiQuarterNote = microsecondsPerMidiQuarterNote;
        return this;
    }


    private void writeDelta() {
        if (deltaWaiting != -1) {
            midiMessages.add(deltaWaiting);
            deltaWaiting = -1;
        } else {
            midiMessages.add(0);
        }
    }


    public MidiTrackBuilder addNoteOn(int pitch, int velocity) {
        writeDelta();
        midiMessages.add(com.kjipo.midi.MidiMessages.NOTE_ON.message);
        midiMessages.add(pitch);
        midiMessages.add(velocity);
        return this;
    }


    public MidiTrackBuilder addNoteOff(int pitch, int velocity) {
        writeDelta();
        midiMessages.add(com.kjipo.midi.MidiMessages.NOTE_OFF.message);
        midiMessages.add(pitch);
        midiMessages.add(velocity);
        return this;
    }


    public MidiTrackBuilder addDelta(int deltaPeriod) {
        if (deltaWaiting != -1) {
            throw new IllegalArgumentException("Cannot add two delta periods in a row");
        }
        deltaWaiting = deltaPeriod;
        return this;
    }

    protected static byte[] splitLengthIntoBytes(int length) {
        // TODO If the length is greater than 127 we need to do some extra calculations
//        if (length > 127) {
//            throw new IllegalArgumentException("Only 127 events are supported so far");
//        }

        return new byte[]{
                (byte) ((length & 0xFF000000) >> 24),
                (byte) ((length & 0xFF0000) >> 16),
                (byte) ((length & 0xFF00) >> 8),
                (byte) (length & 0xFF)
        };
    }

    public byte[] build() {
        ByteArrayOutputStream output = new ByteArrayOutputStream(midiMessages.size()); // TODO Should have a little more initial capacity

        try {
            output.write(new byte[]{
                    0x4d, 0x54, 0x68, 0x64, 0x00, 0x00, 0x00, 0x06,
                    0x00, 0x00,
                    0x00, 0x01,
                    0x00, (byte) ticksPerQuarterNote,
            });

            // Write header
            output.write(new byte[]{0x4D, 0x54, 0x72, 0x6B});

            byte tempo[] = splitLengthIntoBytes(microsecondsPerMidiQuarterNote);
            byte tempoAndTimeSignature[] = new byte[]{
                    // Tempo
                    0x00, (byte) 0xFF, 0x51, 0x03, tempo[1], tempo[2], tempo[3],
                    // Time signature
                    0x00, (byte) 0xFF, 0x58, 0x04, (byte) timeSignatureNominator, (byte) timeSignatureDenominator, 24, 8, // TODO Figure out the meaning of the last two numbers
            };

            // Now write the length of the MIDI messages
            output.write(splitLengthIntoBytes(midiMessages.size() + 4 + tempoAndTimeSignature.length));
            output.write(tempoAndTimeSignature);

            // Write the midi messages
            for (int message : midiMessages) {
                output.write((byte) message);
            }

            // Write end of track
            output.write(new byte[]{0x01, (byte) 0xFF, 0x2F, 0x00});
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return output.toByteArray();
    }


}
