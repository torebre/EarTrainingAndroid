package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.Sequence;

import dagger.Module;



public class EarTrainerImpl implements EarTrainer {
    private final SequenceGenerator sequenceGenerator;


    public EarTrainerImpl() {
        this.sequenceGenerator = new SequenceGenerator();
    }



    @Override
    public Sequence generateNextSequence() {
        sequenceGenerator.createNewSequence();
        return sequenceGenerator.getCurrentSequence();
    }


    @Override
    public Sequence getCurrentSequence() {
        return sequenceGenerator.getCurrentSequence();
    }

}
