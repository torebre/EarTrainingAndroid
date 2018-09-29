package com.kjipo.eartraining.midistream

import com.kjipo.eartraining.midi.MidiMessages
import com.kjipo.eartraining.midi.MidiPlayerInterface
import java.io.IOException


class MidiStream : MidiPlayerInterface {
    private var synthesizer = Synthesizer()


    override fun noteOn(pitch: Int) {
        val command = ByteArray(3)
        command[0] = MidiMessages.NOTE_ON.message.toByte()
        command[1] = pitch.toByte()
        command[2] = 127
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3)
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    override fun noteOff(pitch: Int) {
        val command = ByteArray(3)
        command[0] = MidiMessages.NOTE_OFF.message.toByte()
        command[1] = pitch.toByte()
        command[2] = 127
        try {
            // TODO Not necessary to wait here? Put on a different thread
            synthesizer.send(command, 0, 3)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    override fun start() {
        synthesizer.start()
    }

    override fun stop() {
        synthesizer.stop()
    }
}
