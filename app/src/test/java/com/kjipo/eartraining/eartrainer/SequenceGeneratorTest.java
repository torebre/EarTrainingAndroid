package com.kjipo.eartraining.eartrainer;


import com.kjipo.eartraining.data.Sequence;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SequenceGeneratorTest {


    @Test
    public void generateSimpleSequence() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Sequence sequence = sequenceGenerator.createNewSequence();
        assertThat(sequence).isNotNull();
        assertThat(sequence.getTimeSignatureDenominator()).isNotEqualTo(0);
        assertThat(sequence.getTimeSignatureNominator()).isNotEqualTo(0);
        assertThat(sequence.getPitchSequence()).isNotEmpty();
        assertThat(sequence.getRenderingSequence()).isNotNull();


        System.out.println("Rendering sequence: " +sequence.getRenderingSequence());
    }

    @Test
    public void sequenceTest() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();
        Sequence sequence = sequenceGenerator.createNewSequence();

        System.out.println(EarTrainerUtilities.transformToJson(sequence));
    }

}
