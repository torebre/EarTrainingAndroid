package com.kjipo.eartraining.recorder;

import android.app.Fragment;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import javax.inject.Inject;

public class Recorder extends Fragment {

    static {
//        System.loadLibrary("native-audio");
        System.loadLibrary("essentia-interface");
    }

    public static native void createEngine();

    public static native boolean createAudioRecorder();

    public static native void startRecording();

    public static native void shutdown();

    public static native int getFD();

    public static native void initializeEssentia();


    private ParcelFileDescriptor.AutoCloseInputStream underlyingStream;


    @Inject
    public Recorder() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createEngine();

        int sampleRate = 0;
        int bufSize = 0;

        /*
         * retrieve fast audio path sample rate and buf size; if we have it, we pass to native
         * side to create a player with fast audio enabled [ fast audio == low latency audio ];
         * IF we do not have a fast audio path, we pass 0 for sampleRate, which will force native
         * side to pick up the 8Khz sample rate.
         */
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            AudioManager myAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            String nativeParam = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
//            sampleRate = Integer.parseInt(nativeParam);
//            nativeParam = myAudioMgr.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
//            bufSize = Integer.parseInt(nativeParam);
//        }


    }


    public void recordAudio() {
//        recordAudioInternal();

        initializeEssentia();


    }


    // Single out recording for run-permission needs
    static boolean created = false;

    private void recordAudioInternal() {
        if (!created) {
            Log.i("Record", "Creating engine");
            createEngine();
            Log.i("Record", "Created engine");

            created = createAudioRecorder();
        }
        if (created) {
            setupStream();

            Log.i("Record", "Starting recording");
            startRecording();

            Log.i("Record", "Waiting for data");
            try {
                while (underlyingStream.read() != -1) {
                    int read = underlyingStream.read();
                    Log.i("Record", "Read: " + read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void setupStream() {
        ParcelFileDescriptor pfd = ParcelFileDescriptor.adoptFd(getFD());
        underlyingStream = new ParcelFileDescriptor.AutoCloseInputStream(pfd);


    }


}
