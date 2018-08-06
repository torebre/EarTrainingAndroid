package com.kjipo.eartraining.midi

import android.util.Log
import com.google.common.collect.Lists
import java.util.*
import com.kjipo.scoregenerator.Sequence

class MidiScript(val sequence: Sequence, val midiPlayer: MidiPlayerInterface) {
    private val pitchEvents = Lists.newArrayList<PitchEvent>()

    init {
        for (note in sequence.pitchSequence) {
            pitchEvents.add(PitchEvent(note.id, note.timeOff, false, note.pitch))
            pitchEvents.add(PitchEvent(note.id, note.timeOn, true, note.pitch))
        }
        Collections.sort(pitchEvents)
    }

    fun play() {
        val playThread = Thread {
            var timeCounter = 0

            Log.i("Midi", "Pitch events: ${pitchEvents}")

            pitchEvents.forEach {
                val time = it.time
                val pitchEvent = it

                Log.i("Midi", "Pitch event: $it")

                Log.i("Midi", "Sleeping for " +time.minus(timeCounter).toLong() +" milliseconds")
                Thread.sleep(time.minus(timeCounter).toLong())
                if (pitchEvent.on) {
                    Log.i("Midi", "Pitch on: ${pitchEvent.pitch}")
                    midiPlayer.noteOn(pitchEvent.pitch)
                    Log.i("Midi", "On-message sent")
                } else {
                    Log.i("Midi", "Pitch off: ${pitchEvent.pitch}")
                    midiPlayer.noteOff(pitchEvent.pitch)
                    Log.i("Midi", "Off-message sent")
                }
                timeCounter = time
            }
        }
        playThread.start()
    }
}


data class PitchEvent(val id: Int, val time: Int, val on: Boolean, val pitch: Int) : Comparable<PitchEvent> {
    override fun compareTo(other: PitchEvent) = time.compareTo(other.time)
}