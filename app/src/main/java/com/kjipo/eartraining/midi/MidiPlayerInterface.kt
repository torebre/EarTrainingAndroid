package com.kjipo.eartraining.midi


interface MidiPlayerInterface {

    fun noteOn(pitch: Int)

    fun noteOff(pitch: Int)

    fun start()

    fun stop()

}
