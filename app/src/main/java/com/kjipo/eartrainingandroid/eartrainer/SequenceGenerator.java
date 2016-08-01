package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.ClefType;
import com.kjipo.eartrainingandroid.data.ElementType;
import com.kjipo.eartrainingandroid.data.Note;
import com.kjipo.eartrainingandroid.data.Sequence;
import com.kjipo.eartrainingandroid.data.SequenceBuilder;
import com.kjipo.eartrainingandroid.midi.MidiTrackBuilder;



public class SequenceGenerator {
    private Sequence currentSequence;



    public SequenceGenerator() {

    }

    public Sequence createNewSequence() {
        Sequence sequence = SequenceBuilder.createSequence()
                .setClefType(ClefType.TREBLE)
                .setTempoInMillisecondsPerQuarterNote(1000)
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setDurationOfBar(4)
                .addNote(new Note(1, 60, 1, ElementType.QUARTERNOTE, 4))
                .build();

        currentSequence = sequence;

        return sequence;
    }

    public Sequence getCurrentSequence() {
        if(currentSequence == null) {
            throw new IllegalStateException("No sequence generated");
        }
        return currentSequence;
    }


}
