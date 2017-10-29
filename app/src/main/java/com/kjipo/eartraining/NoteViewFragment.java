package com.kjipo.eartraining;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.kjipo.eartraining.eartrainer.EarTrainerUtilities;

import javax.annotation.Nullable;
import javax.inject.Inject;


public class NoteViewFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private CustomWebViewClient noteViewClient;

    private NoteViewModel noteViewModel;

//    AutoClearedValue<<NoteviewFragem> binding;


    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parentViewGroup,
                             @Nullable Bundle savedInstanceState) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_noteview, parentViewGroup, false, dataBindingComponent);

        View rootView = inflater.inflate(R.layout.fragment_noteview,
                parentViewGroup, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.noteView);
        noteViewClient = new CustomWebViewClient();
        noteViewClient.attachWebView(myWebView);

        Log.i("test", "onCreateView: " +dataBinding);

        Button playButton = rootView.findViewById(R.id.btnPlay);
        Button generateButton = rootView.findViewById(R.id.btnGenerate);

        playButton.setOnClickListener(l -> playSequence());
        generateButton.setOnClickListener(l -> generateSequence());

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        noteViewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel.class);



    }




    public void loadNoteSequence(String sequenceAsJson) {
        noteViewClient.loadNoteSequence(sequenceAsJson);


    }

    public void playSequence() {
//        try {
//
//
//            System.out.println("Sequence to play: " + earTrainer.getCurrentSequence());
//
//            System.out.println("Sequence as JSON: " + MidiUtilities.transformSequenceToJson(earTrainer.getCurrentSequence()));
//
//            midiPlayer.playSequence(MidiUtilities.transformSequenceToMidiFormat(earTrainer.getCurrentSequence()));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void generateSequence() {
        loadNoteSequence(EarTrainerUtilities.transformToJson(noteViewModel.generateNextSequence()));
    }



}
