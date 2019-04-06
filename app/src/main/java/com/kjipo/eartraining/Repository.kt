package com.kjipo.eartraining

import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.eartrainer.EarTrainerImpl
import com.kjipo.eartraining.midi.MidiPlayerInterface
import com.kjipo.eartraining.midi.sonivox.SonivoxMidiPlayer
import org.koin.dsl.module


val appModule = module {

    single<MidiPlayerInterface> { SonivoxMidiPlayer() }

    factory { EarTrainerImpl() as EarTrainer }

}



