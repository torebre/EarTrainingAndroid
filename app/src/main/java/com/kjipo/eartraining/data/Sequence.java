package com.kjipo.eartraining.data;


import com.kjipo.score.RenderingSequence;

import java.util.ArrayList;
import java.util.List;


public final class Sequence {
    private ClefType clef;
    private int timeSignatureNominator;
    private int timeSignatureDenominator;
    //    private List<Note> notes = new ArrayList<>();
    private int durationOfBar;
    private int tempoInMillisecondsPerQuarterNote;

    private List<Pitch> pitchSequence = new ArrayList<>();
    private RenderingSequence renderingSequence;


    public Sequence() {

    }

    public Sequence(Sequence sequence) {
        this(sequence.getClef(), sequence.getTimeSignatureNominator(),
                sequence.getTimeSignatureDenominator(),
//                new ArrayList<>(sequence.getNotes()),
                sequence.renderingSequence,
                sequence.getDurationOfBar(),
                sequence.getTempoInMillisecondsPerQuarterNote(),
                new ArrayList<>(sequence.getPitchSequence()));
    }

    public Sequence(ClefType clef, int timeSignatureNominator, int timeSignatureDenominator,
//                    List<Note> notes,
                    RenderingSequence renderingSequence,
                    int durationOfBar,
                    int tempoInMillisecondsPerQuarterNote,
                    List<Pitch> pitchSequence) {
        this.clef = clef;
        this.timeSignatureNominator = timeSignatureNominator;
        this.timeSignatureDenominator = timeSignatureDenominator;
//        this.notes = notes;
        this.renderingSequence = renderingSequence;
        this.durationOfBar = durationOfBar;
        this.tempoInMillisecondsPerQuarterNote = tempoInMillisecondsPerQuarterNote;
        this.pitchSequence = pitchSequence;
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

//    public void addNote(Note note) {
//        notes.add(note);
//    }

//    public List<Note> getNotes() {
//        return notes;
//    }

//    public void setNotes(List<Note> notes) {
//        this.notes = notes;
//    }

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

    public List<Pitch> getPitchSequence() {
        return pitchSequence;
    }

    public void addPitch(Pitch pitch) {
        pitchSequence.add(pitch);
    }

    public RenderingSequence getRenderingSequence() {
        return renderingSequence;
    }

    public void setRenderingSequence(RenderingSequence renderingSequence) {
        this.renderingSequence = renderingSequence;
    }
}
