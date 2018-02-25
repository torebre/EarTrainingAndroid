package com.kjipo.eartraining;


import android.webkit.JavascriptInterface;


/**
 * This will be exposed to the NoteViewer.
 *
 */
public class NoteVisualizerInterface {


    @JavascriptInterface
    public void playSequence() {

        // TODO
        System.out.println("Playing sequence");


    }

    @JavascriptInterface
    public void generateNewSequence() {

        // TODO
        System.out.println("Generating new sequence");

    }


}