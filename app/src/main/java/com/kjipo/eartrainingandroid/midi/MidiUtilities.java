package com.kjipo.eartrainingandroid.midi;


import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SortedSetMultimap;
import com.kjipo.midi.*;
import com.kjipo.representation.Note;
import com.kjipo.representation.Sequence;

import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;


public final class MidiUtilities {

    public static byte[] transformSequenceToMidiFormat(Sequence sequence) {
        com.kjipo.midi.MidiTrackBuilder builder = com.kjipo.midi.MidiTrackBuilder.createNewInstance();
        builder.setTempo(sequence.getTempoInMillisecondsPerQuarterNote());
        int currentPoint = 0;
        Multimap<Integer, Note> pendingOffEvents = MultimapBuilder.treeKeys().arrayListValues().build();


        for (Note note : sequence.getNotes()) {

            switch (note.getElementType()) {
                case BAR_LINE:
                    currentPoint = 0;

                default:
                    if (note.getStartWithinBar() < currentPoint) {
                        // We make an assumtion that the notes are ordered by
                        // increasing start points
                        throw new RuntimeException("At point: " + currentPoint + ". Going backwards in: " + sequence);
                    }
                    currentPoint = note.getStartWithinBar();
            }


// TODO Handle off events
//            Map.Entry<Integer, Collection<Note>> offEvents =


            // TODO Is 0 a valid pitch?
            if (note.getPitch() >= 0) {
                builder.addNoteOn(note.getPitch(), note.getVelocity());
                pendingOffEvents.put(note.getStartWithinBar() + note.getDurationWithinBar(), note);
            }


        }


        // TODO Handle remaining off events

        return builder.build();
    }


}
