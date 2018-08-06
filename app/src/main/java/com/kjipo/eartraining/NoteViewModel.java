package com.kjipo.eartraining;

import android.arch.lifecycle.ViewModel;

import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.scoregenerator.Sequence;

import javax.inject.Inject;

public class NoteViewModel extends ViewModel {
    private final EarTrainer earTrainer;


    @Inject
    NoteViewModel(EarTrainer earTrainer) {
        this.earTrainer = earTrainer;
    }


    public Sequence generateNextSequence() {
        return earTrainer.generateNextSequence();
    }
}
