package com.kjipo.eartraining.score

import com.kjipo.scoregenerator.Pitch


internal fun transformPitchSequenceToFormatForDatabase(pitchSequence: List<Pitch>) =
        pitchSequence.map {
            "(${it.timeOn},${it.timeOff},${it.pitch},${it.duration.ticks})"
        }.joinToString(",")
