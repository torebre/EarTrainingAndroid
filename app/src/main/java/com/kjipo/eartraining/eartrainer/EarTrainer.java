package com.kjipo.eartraining.eartrainer;


import com.kjipo.scoregenerator.Sequence;


public interface EarTrainer {

    Sequence getCurrentSequence();

    Sequence generateNextSequence();

}
