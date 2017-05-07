package com.kjipo.eartraining.midi;


import com.kjipo.eartraining.data.Sequence;
import com.kjipo.eartraining.eartrainer.SequenceGenerator;

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
