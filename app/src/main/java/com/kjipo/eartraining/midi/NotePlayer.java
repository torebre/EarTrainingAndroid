package com.kjipo.eartrainingandroid.midi;


public class NotePlayer {
    private float mPhase = 0.0f;
    private float mPhaseIncrement = 0.01f;
    private float mFrequency;
    private float mFrequencyScaler = 1.0f;
    private float mAmplitude = 1.0f;



    public NotePlayer(int note) {
        mFrequency = (float)MidiUtilities.pitchToFrequency(60);
    }

    float incrementWrapPhase() {
        mPhase += mPhaseIncrement;
        while (mPhase > 1.0) {
            mPhase -= 2.0;
        }
        while (mPhase < -1.0) {
            mPhase += 2.0;
        }
        return mPhase;
    }

    public float incrementAndGetPhase() {
        return incrementWrapPhase() * mAmplitude;
    }

    private void updatePhaseIncrement() {
        mPhaseIncrement = 2.0f * mFrequency * mFrequencyScaler / 48000.0f;
    }

}
