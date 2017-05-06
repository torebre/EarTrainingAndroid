package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.Note;
import com.kjipo.eartrainingandroid.data.Sequence;

import java.util.Iterator;


public class SequenceComparator {
    private final AudioSequence target;



    public SequenceComparator(AudioSequence target) {
        this.target = target;
    }



    public SequenceComparison compareToTarget(AudioSequence audioSequence) {
        int timeCounter = 0;
        int timeCounterInput = 0;
        Iterator<NoteAudio> inputNotes = audioSequence.getNotes().iterator();

        for(NoteAudio noteAudio : target.getNotes()) {
            timeCounter += noteAudio.getAbsoluteStart();
            if(inputNotes.hasNext()) {
                NoteAudio inputNote = inputNotes.next();
                timeCounterInput += inputNote.getDuration();
            }

            // TODO Figure out some way to split the sequences into blocks and then compute some distance between blocks that cover the same time period

        }

        // TODO
        return null;

    }


}
