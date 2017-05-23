package com.kjipo.eartraining.score;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.kjipo.eartraining.CustomWebViewClient;
import com.kjipo.eartraining.R;
import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.midi.MidiPlayerInterface;
import com.kjipo.eartraining.svg.SequenceToSvg;

import javax.inject.Inject;


public class ScoreActivity extends AppCompatActivity {

    @Inject
    EarTrainer earTrainer;
    @Inject
    MidiPlayerInterface midiPlayer;
    @Inject
    SequenceToSvg sequenceToSvg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_act);

        WebView myWebView = (WebView) findViewById(R.id.score);
        CustomWebViewClient noteViewClient = new CustomWebViewClient();
        noteViewClient.attachWebView(myWebView);

    }

    public static void start(Activity activity, ActivityOptionsCompat options) {
        Intent starter = getStartIntent(activity);
        ActivityCompat.startActivity(activity, starter, options.toBundle());
    }

    public static void start(Context context) {
        Intent starter = getStartIntent(context);
        context.startActivity(starter);
    }

    @NonNull
    static Intent getStartIntent(Context context) {
        Intent starter = new Intent(context, ScoreActivity.class);
        return starter;
    }

}
