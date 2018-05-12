package com.kjipo.eartraining.eartrainer;


import com.kjipo.eartraining.data.Sequence;

import javax.inject.Singleton;


@Singleton
public class EarTrainerImpl implements EarTrainer {
    private final SequenceGenerator sequenceGenerator;


    public EarTrainerImpl() {
        this.sequenceGenerator = new SequenceGenerator();
    }



    @Override
    public Sequence generateNextSequence() {
        sequenceGenerator.createNewSequence(false);
        return sequenceGenerator.getCurrentSequence();
    }


    @Override
    public Sequence getCurrentSequence() {
        return sequenceGenerator.getCurrentSequence();
    }

}
