package com.kjipo.eartrainingandroid.midi;


import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.kjipo.eartrainingandroid.data.Sequence;
import com.kjipo.eartrainingandroid.eartrainer.SequenceGenerator;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class MidiPlayerTest {


    @Test
    public void testMidiTransformation() throws InterruptedException {
//        SequenceGenerator sequenceGenerator = new SequenceGenerator();
//        Sequence sequence = sequenceGenerator.createNewSequence();
//        byte sequenceTransformedToMidi[] = MidiUtilities.transformSequenceToMidiFormat(sequence);

        MidiTrackBuilder midiTrackBuilder = MidiTrackBuilder.createNewInstance();
        byte sequenceTransformedToMidi[] = midiTrackBuilder.addDelta(0)
                .setTicksPerQuarterNote(16)
                .setTempo(100000000)
                .setTimeSignature(4, 4)
                .addNoteOn(60, 127)
                .addNoteOn(70, 127)
                .addDelta(60)
                .addNoteOff(60, 127)
                .build();

        MidiPlayer midiPlayer = new MidiPlayer();

        midiPlayer.playSequence(sequenceTransformedToMidi);

        System.out.println("Test20");


        Thread.sleep(20000);






    }


}
