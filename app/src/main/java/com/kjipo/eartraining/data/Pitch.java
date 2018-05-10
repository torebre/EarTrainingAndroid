package com.kjipo.eartraining.data;

public class Pitch {
    private final int id;
    private final int timeOn;
    private final int timeOff;
    private final int pitch;


    public Pitch(int id, int timeOn, int timeOff, int pitch) {
        this.id = id;
        this.timeOn = timeOn;
        this.timeOff = timeOff;
        this.pitch = pitch;
    }

    public int getTimeOn() {
        return timeOn;
    }

    public int getTimeOff() {
        return timeOff;
    }

    public int getPitch() {
        return pitch;
    }

    public int getId() {
        return id;
    }
}
