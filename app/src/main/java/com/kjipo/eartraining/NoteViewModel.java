package com.kjipo.eartraining;

import android.arch.lifecycle.ViewModel;
import android.webkit.WebViewClient;

import com.kjipo.eartraining.data.Sequence;
import com.kjipo.eartraining.eartrainer.EarTrainer;

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
