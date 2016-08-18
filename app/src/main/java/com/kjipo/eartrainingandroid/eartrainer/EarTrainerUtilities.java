package com.kjipo.eartrainingandroid.eartrainer;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kjipo.eartrainingandroid.data.ClefType;
import com.kjipo.eartrainingandroid.data.ElementType;
import com.kjipo.eartrainingandroid.data.Note;
import com.kjipo.eartrainingandroid.data.Sequence;
import com.kjipo.eartrainingandroid.data.SequenceBuilder;

public final class EarTrainerUtilities {


    private EarTrainerUtilities() {

    }


    public static String transformToJson(Sequence sequence) {
        Gson gson = new Gson();
        return gson.toJson(sequence);
    }


    public static byte[] transformToMidiData(Sequence sequence) {

        // TODO
        return null;
    }



    public static void main(String args[]) {
        Sequence sequence = SequenceBuilder.createSequence()
                .setTimeSignatureDenominator(4)
                .setTimeSignatureNominator(4)
                .setClefType(ClefType.TREBLE)
                .setDurationOfBar(4)
                .addNote(new Note(1, 60, 1, ElementType.HALFNOTE, 4))
                .build();
        System.out.println(EarTrainerUtilities.transformToJson(sequence));

    }



}
