package com.kjipo.eartraining.eartrainer;


import com.google.gson.Gson;
import com.kjipo.eartraining.data.Sequence;

public final class EarTrainerUtilities {


    private EarTrainerUtilities() {

    }


    public static String transformToJson(Sequence sequence) {
        Gson gson = new Gson();
        return gson.toJson(sequence);
    }

}
