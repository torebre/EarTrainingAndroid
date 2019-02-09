package com.kjipo.eartraining.eartrainer


import com.kjipo.eartraining.midi.MidiPlayerInterface
import com.kjipo.scoregenerator.SequenceGenerator
import com.kjipo.scoregenerator.SimpleNoteSequence

interface EarTrainer {

    var currentTargetSequence: SimpleNoteSequence

    fun getSequenceGenerator(): SequenceGenerator

    fun getMidiInterface(): MidiPlayerInterface

    fun createNewTrainingSequence()

}
