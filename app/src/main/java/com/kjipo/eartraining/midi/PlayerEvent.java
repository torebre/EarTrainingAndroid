package com.kjipo.eartraining.midi;


import java.util.ArrayList;
import java.util.List;


public class PlayerEvent {
    private final long offsetTimeFromStart;
    private final List<Integer> offPitches = new ArrayList<>();
    private final List<Integer> onPitches = new ArrayList<>();


    public PlayerEvent(long offsetTimeFromStart) {
        this.offsetTimeFromStart = offsetTimeFromStart;
    }

    public void addOnPitch(int onPitch) {
        onPitches.add(onPitch);
    }

    public void addOffPitch(int offPitch) {
        offPitches.add(offPitch);
    }

    public List<Integer> getOffPitches() {
        return offPitches;
    }

    public List<Integer> getOnPitches() {
        return onPitches;
    }

    public long getOffsetTimeFromStart() {
        return offsetTimeFromStart;
    }

    @Override
    public String toString() {
        return "PlayerEvent{" +
                "offsetTimeFromStart=" + offsetTimeFromStart +
                ", offPitches=" + offPitches +
                ", onPitches=" + onPitches +
                '}';
    }

}
