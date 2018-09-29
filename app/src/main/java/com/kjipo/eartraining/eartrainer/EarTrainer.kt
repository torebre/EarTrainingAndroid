package com.kjipo.eartraining.eartrainer


import com.kjipo.eartraining.midi.MidiPlayerInterface
import com.kjipo.scoregenerator.SequenceGenerator

interface EarTrainer {

    fun getSequenceGenerator(): SequenceGenerator

    fun getMidiInterface(): MidiPlayerInterface

}
