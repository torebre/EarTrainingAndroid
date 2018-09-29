package com.kjipo.eartraining.eartrainer


import com.kjipo.eartraining.midi.sonivox.SonivoxMidiPlayer
import com.kjipo.scoregenerator.SequenceGenerator
import javax.inject.Singleton


@Singleton
class EarTrainerImpl : EarTrainer {
    var sequenceGeneratorInternal = SequenceGenerator()
    var midiPlayer = SonivoxMidiPlayer()

    override fun getSequenceGenerator() = sequenceGeneratorInternal

    override fun getMidiInterface() = midiPlayer

}
