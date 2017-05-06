package com.kjipo.eartrainingandroid.eartrainer;


import java.util.List;


public class AudioSequence {
    private final List<NoteAudio> notes;

    public AudioSequence(List<NoteAudio> notes) {
        this.notes = notes;
    }

    public List<NoteAudio> getNotes() {
        return notes;
    }
}
