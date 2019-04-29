package com.kjipo.eartraining.eartrainer


import com.kjipo.eartraining.midi.sonivox.SonivoxMidiPlayer
import com.kjipo.score.Duration
import com.kjipo.scoregenerator.NoteSequenceElement
import com.kjipo.scoregenerator.SequenceGenerator
import com.kjipo.scoregenerator.SimpleNoteSequence
import com.kjipo.scoregenerator.SimpleSequenceGenerator


class EarTrainerImpl : EarTrainer {
    private var sequenceGeneratorInternal = SequenceGenerator()
    private var midiPlayer = SonivoxMidiPlayer()

    override var currentTargetSequence = SimpleNoteSequence(emptyList())

    override fun getSequenceGenerator() = sequenceGeneratorInternal

    override fun getMidiInterface() = midiPlayer

    override fun createNewTrainingSequence() {
        currentTargetSequence = SimpleSequenceGenerator.createSequence()

        // TODO Add a clear method
//        sequenceGeneratorInternal.loadSimpleNoteSequence(createEmptySequence())

        sequenceGeneratorInternal = SequenceGenerator()
        sequenceGeneratorInternal.loadSimpleNoteSequence(createEmptySequence())
    }

    private fun createEmptySequence() =
            SimpleNoteSequence(listOf(NoteSequenceElement.RestElement(Duration.QUARTER),
                    NoteSequenceElement.RestElement(Duration.QUARTER),
                    NoteSequenceElement.RestElement(Duration.QUARTER),
                    NoteSequenceElement.RestElement(Duration.QUARTER)))


}
