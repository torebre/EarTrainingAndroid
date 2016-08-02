package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.Sequence;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class SequenceGeneratorTest {


    @Test
    public void generateSimpleSequence() {
        SequenceGenerator sequenceGenerator = new SequenceGenerator();

        Sequence sequence = sequenceGenerator.createNewSequence();

        assertThat(sequence).isNotNull();

    }




}
