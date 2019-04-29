package com.kjipo.eartraining.midi.sonivox

import com.kjipo.eartraining.midi.MidiMessages
import com.kjipo.eartraining.midi.MidiPlayerInterface
import org.billthefarmer.mididriver.MidiDriver

class SonivoxMidiPlayer : MidiPlayerInterface {

    private val midiDriver = MidiDriver()


    override fun start() {
        midiDriver.start()
    }

    override fun stop() {
        midiDriver.stop()
    }

    override fun noteOn(pitch: Int) {
        val command = ByteArray(3)
        command[0] = MidiMessages.NOTE_ON.message.toByte()
        command[1] = pitch.toByte()
        command[2] = 127

        midiDriver.queueEvent(command)
    }

    override fun noteOff(pitch: Int) {
        val command = ByteArray(3)
        command[0] = MidiMessages.NOTE_OFF.message.toByte()
        command[1] = pitch.toByte()
        command[2] = 127

        midiDriver.queueEvent(command)
    }

}