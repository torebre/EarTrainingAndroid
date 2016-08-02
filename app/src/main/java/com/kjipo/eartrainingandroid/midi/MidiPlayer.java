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
            NotePlayer notePlayer = new NotePlayer(offPitch);
            currentlyPlaying.remove(offPitch);
        }

    }

}
