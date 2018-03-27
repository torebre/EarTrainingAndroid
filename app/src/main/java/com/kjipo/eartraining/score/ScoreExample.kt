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
                    duration = Duration.HALF
                    beamGroup = 1
                }

                note {
                    note = NoteType.H
                    duration = Duration.QUARTER
                    beamGroup = 1
                }

                note {
                    note = NoteType.C
                    octave = 8
                    duration = Duration.QUARTER
                }

                note {
                    note = NoteType.C
                    octave = 4
                    duration = Duration.HALF
                }

            }

            // TODO Support multiple bars
            bar {
                note {
                    note = NoteType.C
                    duration = Duration.QUARTER
                }

                note {
                    note = NoteType.F
                    duration = Duration.QUARTER
                }

            }

        }

        println(testScore.renderingElements)

        var idCounter = 0
        testScore.renderingElements.forEach { it.id = idCounter++ }

        return testScore

    }



}
