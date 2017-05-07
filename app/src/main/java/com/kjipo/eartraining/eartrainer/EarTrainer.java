package com.kjipo.eartraining.eartrainer;


import com.kjipo.eartraining.data.Sequence;


public interface EarTrainer {


    Sequence getCurrentSequence();

    Sequence generateNextSequence();


}
