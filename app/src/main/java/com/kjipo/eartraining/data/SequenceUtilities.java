package com.kjipo.eartraining.data;


public final class SequenceUtilities {


    private SequenceUtilities() {

    }


    public int getStartTimeForNote(Note note, Sequence sequence) {
        return getTimeForNote(note, sequence, true);
    }


    public int getOffTimeForNote(Note note, Sequence sequence) {
        return getTimeForNote(note, sequence, false);
    }


    private static int getTimeForNote(Note note, Sequence sequence, boolean startTime) {
        int counter = 0;
        for (Note currentNote : sequence.getNotes()) {
            if (note == currentNote) {
                return startTime ? counter :
                        (counter + getDurationForNote(currentNote.getElementType()));
            }
            counter += getDurationForNote(note.getElementType());
        }
        throw new IllegalArgumentException("Note not found");
    }


    private static int getDurationForNote(ElementType elementType) {
        // TODO Need to decide on the shortest note


        switch (elementType) {

            case WHOLE_NOTE:
                return 4;

            case HALFNOTE:
                return 2;

            case QUARTERNOTE:
                return 1;

            default:
                return 0;


        }


    }



}
