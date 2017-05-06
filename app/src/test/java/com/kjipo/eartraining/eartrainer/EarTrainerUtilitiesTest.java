package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.ClefType;
import com.kjipo.eartrainingandroid.data.ElementType;
import com.kjipo.eartrainingandroid.data.Note;
import com.kjipo.eartrainingandroid.data.Sequence;
import com.kjipo.eartrainingandroid.data.SequenceBuilder;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EarTrainerUtilitiesTest {

    @Test
    public void transformToJsonTest() {
        Sequence sequence = SequenceBuilder.createSequence()
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setClefType(ClefType.TREBLE)
                .setDurationOfBar(4)
                .addNote(new Note(1, 60, 1, ElementType.QUARTERNOTE, 4))
                .build();
        assertThat(EarTrainerUtilities.transformToJson(sequence))
                .isEqualTo("{\"clef\":\"TREBLE\",\"timeSignatureNominator\":4," +
                        "\"timeSignatureDenominator\":4,\"notes\":[{\"id\":1,\"pitch\":60," +
                        "\"startWithinBar\":1,\"elementType\":\"QUARTERNOTE\"," +
                        "\"durationWithinBar\":4,\"velocity\":127}]," +
                        "\"durationOfBar\":4,\"tempoInMillisecondsPerQuarterNote\":0}");
    }

}
