package com.kjipo.eartraining.score

import com.kjipo.svg.*


object ScoreExample {

    fun getTestScore(): RenderingSequence {
        val testScore = createScore().score {
            bar {
                clef = Clef.G
                timeSignature = TimeSignature(4, 4)

                note {
                    note = NoteType.C
                    duration = 24
                    beamGroup = 1
                }

                note {
                    note = NoteType.H
                    duration = 24
                    beamGroup = 1
                }

                note {
                    note = NoteType.C
                    octave = 8
                    duration = 48
                }

                note {
                    note = NoteType.C
                    octave = 4
                    duration = 24
                }

            }

            // TODO Support multiple bars
            bar {
                note {
                    note = NoteType.C
                    duration = 24
                }

                note {
                    note = NoteType.F
                    duration = 48
                }

            }

        }

        println(testScore.renderingElements)

        var idCounter = 0
        testScore.renderingElements.forEach { it.id = idCounter++ }

        return testScore

    }



}
