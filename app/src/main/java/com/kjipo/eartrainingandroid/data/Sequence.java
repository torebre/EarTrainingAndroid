package com.kjipo.eartrainingandroid.data;


import java.util.ArrayList;
import java.util.List;


/**
 * The point of a Sequence is to collect the information about a sequence
 * of notes that is needed to render them in the NoteViewer.
 *
 * The representation should offload the renderer. The renderer should
 * be able to easily go through a sequence and lay out elements, and not
 * do computations that could be done earlier.
 *
 * Sequence-classes will be serialized to JSON.
 *
 */
public final class Sequence {
    private ClefType clef;
    private int timeSignatureNominator;
    private int timeSignatureDenominator;
    private List<Note> notes;
    private int durationOfBar;
    private int tempoInMillisecondsPerQuarterNote;



    Sequence() {
        notes = new ArrayList<>();
    }

    public Sequence(Sequence sequence) {
        this(sequence.getClef(), sequence.getTimeSignatureNominator(),
                sequence.getTimeSignatureDenominator(), new ArrayList<>(sequence.getNotes()),
                sequence.getDurationOfBar(), sequence.getTempoInMillisecondsPerQuarterNote());
    }

    public Sequence(ClefType clef, int timeSignatureNominator, int timeSignatureDenominator,
                    List<Note> notes,
                    int durationOfBar, int tempoInMillisecondsPerQuarterNote) {
        this.clef = clef;
        this.timeSignatureNominator = timeSignatureNominator;
        this.timeSignatureDenominator = timeSignatureDenominator;
        this.notes = notes;
        this.durationOfBar = durationOfBar;
        this.tempoInMillisecondsPerQuarterNote = tempoInMillisecondsPerQuarterNote;
    }

    public ClefType getClef() {
        return clef;
    }

    public void setClef(ClefType clef) {
        this.clef = clef;
    }

    public int getTimeSignatureNominator() {
        return timeSignatureNominator;
    }

    public void setTimeSignatureNominator(int timeSignatureNominator) {
        this.timeSignatureNominator = timeSignatureNominator;
    }

    public int getTimeSignatureDenominator() {
        return timeSignatureDenominator;
    }

    public void setTimeSignatureDenominator(int timeSignatureDenominator) {
        this.timeSignatureDenominator = timeSignatureDenominator;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setDurationOfBar(int durationOfBar) {
        this.durationOfBar = durationOfBar;
    }

    public int getDurationOfBar() {
        return durationOfBar;
    }

    public void setTempoInMillisecondsPerQuarterNote(int tempoInMillisecondsPerQuarterNote) {
        this.tempoInMillisecondsPerQuarterNote = tempoInMillisecondsPerQuarterNote;
    }

    public int getTempoInMillisecondsPerQuarterNote() {
        return tempoInMillisecondsPerQuarterNote;
    }

}
