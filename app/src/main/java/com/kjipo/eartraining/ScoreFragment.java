package com.kjipo.eartraining;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ScoreFragment extends Fragment {


    public ScoreFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_score, parentViewGroup, false);
        return rootView;
    }


}
