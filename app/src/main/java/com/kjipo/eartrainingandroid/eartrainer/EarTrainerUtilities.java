package com.kjipo.eartrainingandroid.eartrainer;


import com.kjipo.eartrainingandroid.data.Sequence;

public final class EarTrainerUtilities {


    private EarTrainerUtilities() {

    }


    public static String transformToJson(Sequence sequence) {

        // TODO Current just a hardcoded sequence used for testing
        return "{\"clef\":\"TREBLE\",\"timeSignatureNominator\":4,\"timeSignatureDenominator\":4,\"notes\":[{\"id\":0,\"pitch\":60,\"cumulativeDuration\":0,\"elementType\":\"HALFNOTE\"},{\"id\":1,\"pitch\":64,\"cumulativeDuration\":2,\"elementType\":\"HALFNOTE\"},{\"id\":2,\"pitch\":-1,\"cumulativeDuration\":0,\"elementType\":\"BAR_LINE\"},{\"id\":3,\"pitch\":62,\"cumulativeDuration\":0,\"elementType\":\"QUARTERNOTE\"},{\"id\":4,\"pitch\":61,\"cumulativeDuration\":1,\"elementType\":\"QUARTERNOTE\"},{\"id\":5,\"pitch\":60,\"cumulativeDuration\":2,\"elementType\":\"HALFNOTE\"},{\"id\":6,\"pitch\":-1,\"cumulativeDuration\":0,\"elementType\":\"BAR_LINE\"},{\"id\":7,\"pitch\":70,\"cumulativeDuration\":0,\"elementType\":\"HALFNOTE\"},{\"id\":8,\"pitch\":69,\"cumulativeDuration\":2,\"elementType\":\"HALFNOTE\"},{\"id\":9,\"pitch\":-1,\"cumulativeDuration\":0,\"elementType\":\"BAR_LINE\"},{\"id\":10,\"pitch\":66,\"cumulativeDuration\":0,\"elementType\":\"HALFNOTE\"},{\"id\":11,\"pitch\":62,\"cumulativeDuration\":2,\"elementType\":\"HALFNOTE\"},{\"id\":12,\"pitch\":-1,\"cumulativeDuration\":0,\"elementType\":\"BAR_LINE\"},{\"id\":13,\"pitch\":64,\"cumulativeDuration\":0,\"elementType\":\"QUARTERNOTE\"}],\"durationOfBar\":4}";
    }


    public static byte[] transformToMidiData(Sequence sequence) {

        // TODO
        return null;
    }


}
