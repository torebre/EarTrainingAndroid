package com.kjipo.eartrainingandroid;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import javax.inject.Inject;


public class MainActivity extends Activity {
    private CustomWebViewClient noteViewClient;
    @Inject
    SequenceGenerator sequenceGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((EarTrainingApplication)getApplication()).inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        WebView myWebView = (WebView) findViewById(R.id.noteView);
        noteViewClient = new CustomWebViewClient();
        noteViewClient.attachWebView(myWebView);
    }

    public void generateSequence(View view) {
        noteViewClient.loadNoteSequence(sequenceGenerator.getSequence());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // TODO Just here to check when this event is triggered

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d("debug", "Action was DOWN");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d("debug", "Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d("debug", "Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d("debug", "Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d("debug", "Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }


    }
