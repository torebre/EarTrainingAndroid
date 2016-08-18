package com.kjipo.eartrainingandroid.data;


public class Note {
    private final int id;
    private final int pitch;
    private final int startWithinBar;
    private final ElementType elementType;
    private final int durationWithinBar;
    private boolean joinNext;

    private int velocity = 127;


    public Note(int id, int pitch, int startWithinBar, ElementType elementType,
                int durationWithinBar) {
        this.id = id;
        this.pitch = pitch;
        this.startWithinBar = startWithinBar;
        this.elementType = elementType;
        this.durationWithinBar = durationWithinBar;
    }


    /**
     * If the pitch is less than 0 it means that no sound
     * should be played.
     *
     */
    public int getPitch() {
        return pitch;
    }


    public ElementType getElementType() {
        return elementType;
    }

    public int getStartWithinBar() {
        return startWithinBar;
    }

    public int getId() {
        return id;
    }

    public int getDurationWithinBar() {
        return durationWithinBar;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isJoinNext() {
        return joinNext;
    }

    public void setJoinNext(boolean joinNext) {
        this.joinNext = joinNext;
    }
}
