package com.kjipo.eartraining.eartrainer;


public class NoteAudio {
    private final int pitch;
    private final int absoluteStart;
    private final int duration;


    public NoteAudio(int pitch, int absoluteStart, int duration) {
        this.pitch = pitch;
        this.absoluteStart = absoluteStart;
        this.duration = duration;
    }

    public int getPitch() {
        return pitch;
    }

    public int getAbsoluteStart() {
        return absoluteStart;
    }

    public int getDuration() {
        return duration;
    }
}
