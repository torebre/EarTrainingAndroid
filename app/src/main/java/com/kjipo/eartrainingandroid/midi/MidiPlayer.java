package com.kjipo.eartrainingandroid.midi;


import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MidiPlayer {
    public static final int SAMPLES_PER_FRAME = 2;
    public static final int BYTES_PER_SAMPLE = 4; // float
    public static final int BYTES_PER_FRAME = SAMPLES_PER_FRAME * BYTES_PER_SAMPLE;
    public static final int FRAME_RATE = 48000;
    private static final int FRAMES_PER_BUFFER = 240;

    private final float[] buffer = new float[FRAMES_PER_BUFFER * SAMPLES_PER_FRAME];

//    private float mPhase = 0.0f;
//    private float mPhaseIncrement = 0.01f;
//    private float mFrequency = (float) pitchToFrequency(60);
//    private float mFrequencyScaler = 1.0f;
//    private float mAmplitude = 1.0f;

    private final Executor playerExecutor = Executors.newSingleThreadExecutor();
    private final AudioTrack audioTrack;

    private Map<Integer, NotePlayer> currentlyPlaying = new ConcurrentHashMap<>();


    public MidiPlayer() {
        audioTrack = setupAudioTrack();
        startPlayer();
    }


    private static AudioTrack setupAudioTrack() {
        int minBufferSizeBytes = AudioTrack.getMinBufferSize(FRAME_RATE,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_FLOAT);
        Log.i("Test", "AudioTrack.minBufferSize = " + minBufferSizeBytes
                + " bytes = " + (minBufferSizeBytes / BYTES_PER_FRAME)
                + " frames");
        int bufferSize = 8 * minBufferSizeBytes / 8;

        return new AudioTrack(AudioManager.STREAM_MUSIC,
                FRAME_RATE,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_FLOAT,
                bufferSize,
                AudioTrack.MODE_STREAM);

    }


//    public void mix(float[] outputBuffer, int samplesPerFrame, float level) {
//        int numFrames = outputBuffer.length / samplesPerFrame;
//        for (int i = 0; i < numFrames; i++) {
//            float output = render();
//            int offset = i * samplesPerFrame;
//            for (int jf = 0; jf < samplesPerFrame; jf++) {
//                outputBuffer[offset + jf] += output * level;
//            }
//        }
//    }


    public void startPlayer() {
        audioTrack.play();

//        float current = 0;
//        for (int i = 0; i < buffer.length; ++i) {
//            buffer[i] = (float) Math.sin(current);
//            current += 1;
//        }

        playerExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

//                    mix(buffer, SAMPLES_PER_FRAME, 1.0f);


                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = 0.0f;
                    }


                    for (NotePlayer notePlayer : currentlyPlaying.values()) {
                        int numFrames = buffer.length / SAMPLES_PER_FRAME;
                        for (int i = 0; i < numFrames; i++) {
                            float level = notePlayer.incrementAndGetPhase();
                            level = transformToSineWave(level);
                            int offset = i * SAMPLES_PER_FRAME;
                            for (int jf = 0; jf < SAMPLES_PER_FRAME; jf++) {
                                buffer[offset + jf] += level; // output * level;
                            }
                        }
                    }
                    audioTrack.write(buffer, 0, buffer.length, AudioTrack.WRITE_BLOCKING);
                }
            }
        });

    }


    public void stopPlayer() {
        audioTrack.stop();
    }

    public void playSequence(byte midiData[]) throws InterruptedException {
        List<PlayerEvent> playerEvents = MidiUtilities.transformToPlayerEvents(midiData);
        long start = System.currentTimeMillis();

        for (PlayerEvent playerEvent : playerEvents) {
            while (true) {
                long offsetFromStart = playerEvent.getOffsetTimeFromStart();
                if (System.currentTimeMillis() - start >= offsetFromStart) {

                    Log.i("Midi", "Processing event");

                    processPlayerEvent(playerEvent);
                    break;
                }
                Thread.sleep(System.currentTimeMillis() - start);
            }
        }
    }


    private void processPlayerEvent(PlayerEvent playerEvent) {
        for (Integer onPitch : playerEvent.getOnPitches()) {
            NotePlayer notePlayer = new NotePlayer(onPitch);
            currentlyPlaying.put(onPitch, notePlayer);
        }
        for (Integer offPitch : playerEvent.getOffPitches()) {
//            NotePlayer notePlayer = new NotePlayer(offPitch);
            currentlyPlaying.remove(offPitch);
        }

    }


    // Factorial constants.
    private static final float IF3 = 1.0f / (2 * 3);
    private static final float IF5 = IF3 / (4 * 5);
    private static final float IF7 = IF5 / (6 * 7);
    private static final float IF9 = IF7 / (8 * 9);
    private static final float IF11 = IF9 / (10 * 11);


    private static float transformToSineWave(float phase) {
        // Convert raw sawtooth to sine.
//            float phase = incrementWrapPhase();
        return fastSin(phase); // * getAmplitude();
    }


    /**
     * Calculate sine using Taylor expansion. Do not use values outside the range.
     *
     * @param currentPhase in the range of -1.0 to +1.0 for one cycle
     */
    public static float fastSin(float currentPhase) {

        /* Wrap phase back into region where results are more accurate. */
        float yp = (currentPhase > 0.5f) ? 1.0f - currentPhase
                : ((currentPhase < (-0.5f)) ? (-1.0f) - currentPhase : currentPhase);

        float x = (float) (yp * Math.PI);
        float x2 = (x * x);
        /* Taylor expansion out to x**11/11! factored into multiply-adds */
        return x * (x2 * (x2 * (x2 * (x2 * ((x2 * (-IF11)) + IF9) - IF7) + IF5) - IF3) + 1);
    }


}
