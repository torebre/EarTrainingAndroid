package com.kjipo.eartraining;


import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.kjipo.eartraining.recorder.Recorder;

import javax.annotation.Nullable;
import javax.inject.Inject;


public class NoteViewFragment extends Fragment implements Injectable {

    private static final int AUDIO_ECHO_REQUEST = 0;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    Recorder recorder;

    private CustomWebViewClient noteViewClient;

    private NoteViewModel noteViewModel;


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

        // TODO
//        noteViewClient.attachWebView(myWebView, getContext().getAssets());

        Log.i("test", "onCreateView: " +dataBinding);

        Button playButton = rootView.findViewById(R.id.btnPlay);
        Button generateButton = rootView.findViewById(R.id.btnGenerate);
        Button recordButton = rootView.findViewById(R.id.btnRecord);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        noteViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NoteViewModel.class);
    }



    public void record() {
        int status = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO);
        if (status != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_ECHO_REQUEST);
            return;
        }
        recorder.recordAudio();
    }



}
