package com.kjipo.eartraining.eartrainer;


import com.kjipo.eartraining.data.ClefType;
import com.kjipo.eartraining.data.ElementType;
import com.kjipo.eartraining.data.Note;
import com.kjipo.eartraining.data.Sequence;
import com.kjipo.eartraining.data.SequenceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SequenceGenerator {
    private Sequence currentSequence;
    private AudioSequence currentAudioSequence;
    private final Random random = new Random();

    private static final int DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE = 1000;


    public SequenceGenerator() {

    }

    public Sequence createNewSequence() {
        SequenceBuilder defaultSequenceSetup = createDefaultSetup();
        int previousNote = 60;
        int id = 0;
        int timeCounter = 0;

        List<NoteAudio> noteAudioList = new ArrayList<>();
        for(int i = 0; i < 5; ++i) {
            int duration = nextDuration();
            NoteAudio noteAudio = new NoteAudio(previousNote, timeCounter, duration);
            noteAudioList.add(noteAudio);
                    previousNote = nextPitch(previousNote);
            timeCounter += duration;
        }

        currentAudioSequence = new AudioSequence(noteAudioList);
        timeCounter = 0;
        int currentBarEnd = 4 * DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE;
        int beatCounter = 0;

        // TODO This only works because there are only two types of notes used so far

        for(NoteAudio noteAudio : noteAudioList) {
            ElementType elementType = getElementType(noteAudio);
            int beatUpdate;
            switch(elementType) {
                case HALFNOTE:
                    beatUpdate = 2;
                    break;

                case QUARTERNOTE:
                    beatUpdate = 1;
                    break;

                default:
                    throw new IllegalArgumentException("Unexpected element type: " +elementType);
            }

            int duration = noteAudio.getDuration();
            if(timeCounter < currentBarEnd && timeCounter + duration > currentBarEnd) {
                Note note = new Note(id++, noteAudio.getPitch(), 4, ElementType.QUARTERNOTE, 1);
                note.setJoinNext(true);
                defaultSequenceSetup.addNote(note);
                note = new Note(id++, noteAudio.getPitch(), 1, ElementType.QUARTERNOTE, 1);
                defaultSequenceSetup.addNote(note);
                // New bar
                beatCounter = 1;
            }
            else {
                Note note = new Note(id++, noteAudio.getPitch(), beatCounter, elementType, beatUpdate);
                defaultSequenceSetup.addNote(note);
            }
            beatCounter += beatUpdate;
            beatCounter %= 4;
        }

        currentSequence = defaultSequenceSetup.build();
        return currentSequence;
    }

    private static ElementType getElementType(NoteAudio noteAudio) {
        switch (noteAudio.getDuration()) {
            case DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE:
                return ElementType.QUARTERNOTE;

            case 2 * DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE:
                return ElementType.HALFNOTE;

            default:
                throw new IllegalArgumentException("Unexpected duration: " +noteAudio.getDuration());
        }
    }


    private int nextPitch(int previousPitch) {
        int pitch;
        do {
            pitch = previousPitch + random.nextInt(10) - 10;
        }
        while(pitch < 55 && pitch > 80);
        return pitch;
    }

    private int nextDuration() {
        if(Math.random() < 0.7) {
            return DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE;
        }
        return 2 * DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE;
    }

    private static SequenceBuilder createDefaultSetup() {
        return SequenceBuilder.createSequence()
                .setClefType(ClefType.TREBLE)
                .setTempoInMillisecondsPerQuarterNote(DEFAULT_TEMPO_MILLISECONDS_PER_QUARTER_NOTE)
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setDurationOfBar(4);
    }

    public Sequence getCurrentSequence() {
        if(currentSequence == null) {
            throw new IllegalStateException("No sequence generated");
        }
        return currentSequence;
    }


}
