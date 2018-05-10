package com.kjipo.eartraining.data;


import com.kjipo.svg.RenderingSequence;

public final class SequenceBuilder {
    private Sequence sequence;


    private SequenceBuilder() {
        sequence = new Sequence();
    }

    public static SequenceBuilder createSequence() {
        return new SequenceBuilder();
    }

    public SequenceBuilder setClefType(ClefType clefType) {
        sequence.setClef(clefType);
        return this;
    }

    public SequenceBuilder setTimeSignatureNominator(int timeSignatureNominator) {
        sequence.setTimeSignatureNominator(timeSignatureNominator);
        return this;
    }

    public SequenceBuilder setTimeSignatureDenominator(int timeSignatureDenominator) {
        sequence.setTimeSignatureDenominator(timeSignatureDenominator);
        return this;
    }

    public SequenceBuilder setDurationOfBar(int durationOfBar) {
        sequence.setDurationOfBar(durationOfBar);
        return this;
    }

    public SequenceBuilder setTempoInMillisecondsPerQuarterNote(
            int tempoInMillisecondsPerQuarterNote) {
        sequence.setTempoInMillisecondsPerQuarterNote(tempoInMillisecondsPerQuarterNote);
        return this;
    }

    public SequenceBuilder addPitch(Pitch pitch) {
        sequence.addPitch(pitch);
        return this;
    }

    public SequenceBuilder addRenderingSequence(RenderingSequence renderingSequence) {
        sequence.setRenderingSequence(renderingSequence);
        return this;
    }

    public Sequence build() {
        return new Sequence(sequence);
    }

}
