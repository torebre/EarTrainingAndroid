package com.kjipo.eartraining.eartrainer;


import com.kjipo.scoregenerator.SequenceGenerator;

import javax.inject.Singleton;


@Singleton
public class EarTrainerImpl implements EarTrainer {
    private final SequenceGenerator sequenceGenerator;


    public EarTrainerImpl() {
        this.sequenceGenerator = new SequenceGenerator();
    }


    @Override
    public SequenceGenerator getSequenceGenerator() {
        return sequenceGenerator;
    }


}
