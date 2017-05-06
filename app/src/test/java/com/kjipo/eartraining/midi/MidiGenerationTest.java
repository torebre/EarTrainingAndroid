package com.kjipo.eartrainingandroid.midi;


import com.kjipo.eartrainingandroid.data.Sequence;
import com.kjipo.eartrainingandroid.eartrainer.SequenceGenerator;

import org.junit.Test;


public class MidiGenerationTest {




    @Test
    public void createMidiTrack() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Sequence sequence = sequenceGenerator.createNewSequence();
        String sequenceAsJson = MidiUtilities.transformSequenceToJson(sequence);

        // TODO


    }




}
