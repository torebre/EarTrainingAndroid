package com.kjipo.eartraining.eartrainer


import com.kjipo.eartraining.midi.sonivox.SonivoxMidiPlayer
import com.kjipo.score.Duration
import com.kjipo.scoregenerator.NoteSequenceElement
import com.kjipo.scoregenerator.SequenceGenerator
import com.kjipo.scoregenerator.SimpleNoteSequence
import com.kjipo.scoregenerator.SimpleSequenceGenerator
import javax.inject.Singleton


@Singleton
class EarTrainerImpl : EarTrainer {
    var sequenceGeneratorInternal = SequenceGenerator()
    var midiPlayer = SonivoxMidiPlayer()

    override var currentTargetSequence = SimpleNoteSequence(emptyList())

    override fun getSequenceGenerator() = sequenceGeneratorInternal

    override fun getMidiInterface() = midiPlayer

    override fun createNewTrainingSequence() {
        currentTargetSequence = SimpleSequenceGenerator.createSequence()
        resetUserInputSequence()
        sequenceGeneratorInternal.loadSimpleNoteSequence(currentTargetSequence)
    }

    private fun resetUserInputSequence() {
        currentTargetSequence = SimpleNoteSequence(listOf(NoteSequenceElement.RestElement(Duration.QUARTER),
                NoteSequenceElement.RestElement(Duration.QUARTER),
                NoteSequenceElement.RestElement(Duration.QUARTER),
                NoteSequenceElement.RestElement(Duration.QUARTER)))
    }

}
