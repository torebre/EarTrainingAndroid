package com.kjipo.eartraining.score;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;

import com.kjipo.eartraining.CustomWebViewClient;
import com.kjipo.eartraining.EarTrainingApplication;
import com.kjipo.eartraining.MainActivity;
import com.kjipo.eartraining.R;
import com.kjipo.eartraining.adapter.TransitionListenerAdapter;
import com.kjipo.eartraining.eartrainer.EarTrainer;
import com.kjipo.eartraining.helper.TransitionHelper;
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
        ((EarTrainingApplication) getApplication()).getMainComponent().inject(this);

        setContentView(R.layout.score_act);

        final WebView myWebView = (WebView) findViewById(R.id.score);


        CustomWebViewClient noteViewClient = new CustomWebViewClient();
        noteViewClient.attachWebView(myWebView);

        final Button btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Playing note", "Test");
                midiPlayer.noteOn(60);

                String script = "var s2 = Snap(1000, 1000);\n" +
                        "var bigCircle = s2.circle(10, 10, 10);\n" +
                        "bigCircle.attr({\n" +
                        "    fill: \"#bada55\",\n" +
                        "    stroke: \"#000\",\n" +
                        "    strokeWidth: 5\n" +
                        "});" +
                        "console.log(\"Test20\");";

                Log.i("Test", "Evaluating Javascript");
                myWebView.evaluateJavascript(script, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i("Test2", "Received value: " +value);
                    }
                });


            }
        });

        midiPlayer.setup(getApplicationContext());

    }

    @Override
    protected void onStart() {
        super.onStart();
        midiPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        midiPlayer.stop();
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
