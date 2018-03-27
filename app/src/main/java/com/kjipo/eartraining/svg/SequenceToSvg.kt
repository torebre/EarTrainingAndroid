package com.kjipo.eartraining.svg


import android.util.Log
import com.kjipo.eartraining.data.Sequence
import com.kjipo.svg.*


class SequenceToSvg {


    fun transformToSvg(sequence: Sequence?): RenderingSequence {
        if (sequence == null) {
            return RenderingSequence(emptyList(), ViewBox(0, 0, 0, 0))
        }

        Log.i("SVG", "Notes: " + sequence.notes)

        // TODO Only here for testing

        return createScore().score {
            bar {
                clef = Clef.G
                timeSignature = TimeSignature(4, 4)

                note {
                    note = NoteType.A
                    duration = Duration.QUARTER
                }

                note {
                    note = NoteType.H
                    duration = Duration.HALF
                    beamGroup = 1
                }

                note {
                    note = NoteType.C
                    octave = 7
                    duration = Duration.HALF
                }


            }

        }


    }
}
