package com.kjipo.eartrainingandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;


public class NoteViewFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noteview, parentViewGroup, false);
        WebView myWebView = (WebView) rootView.findViewById(R.id.noteView);
        CustomWebViewClient noteViewClient = new CustomWebViewClient();
        noteViewClient.attachWebView(myWebView);
        return rootView;
    }


}
