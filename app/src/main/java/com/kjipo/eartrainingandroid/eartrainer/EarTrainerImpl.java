package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.Sequence;

import javax.inject.Inject;


public class EarTrainerImpl implements EarTrainer {
    @Inject
    SequenceGenerator sequenceGenerator;



    @Override
    public Sequence generateNextSequence() {
        sequenceGenerator.createNewSequence();
//        return sequenceGenerator.getCurrentSequenceAsJson();


        // TODO
        return null;

    }

}
