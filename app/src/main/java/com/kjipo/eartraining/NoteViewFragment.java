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

import javax.annotation.Nullable;
import javax.inject.Inject;


public class NoteViewFragment extends LifecycleFragment implements Injectable {

//    @Inject
//    ViewModelProvider.Factory viewModelFactory;


//    private CustomWebViewClient noteViewClient;

    private NoteViewModel noteViewModel;

//    private FragmentValue<>

    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parentViewGroup,
                             @Nullable Bundle savedInstanceState) {

        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_noteview, parentViewGroup, false, dataBindingComponent);

        View rootView = inflater.inflate(R.layout.fragment_noteview,
                parentViewGroup, false);
//        WebView myWebView = (WebView) rootView.findViewById(R.id.noteView);
//        noteViewClient = new CustomWebViewClient();
//        noteViewClient.attachWebView(myWebView);

        Log.i("test", "onCreateView: " +dataBinding);





        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

//        noteViewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel.class);



    }




    public void loadNoteSequence(String sequenceAsJson) {
//        noteViewClient.loadNoteSequence(sequenceAsJson);


    }



}
