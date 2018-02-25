package com.kjipo.eartraining.midi;



import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class MidiTrackerBuilderTest {

    @Test
    public void testSplitLength() {
        assertThat(MidiTrackBuilder.splitLengthIntoBytes(127)).isEqualTo(new byte[]{0x00, 0x00, 0x00, 127});
        assertThat(MidiTrackBuilder.splitLengthIntoBytes(27)).isEqualTo(new byte[]{0x00, 0x00, 0x00, 27});;
    }

    @Test
    public void testCreateSimpleSequence() throws IOException {
        MidiTrackBuilder midiTrackBuilder = MidiTrackBuilder.createNewInstance();
        byte midiSequence[] = midiTrackBuilder.addDelta(0)
                .setTicksPerQuarterNote(16)
                .setTempo(100000000)
                .setTimeSignature(4, 4)
                .addNoteOn(60, 127)
                .addDelta(60)
                .addNoteOff(60, 127)
                .build();
        Assert.assertTrue(midiSequence.length > 0);
    }

}
