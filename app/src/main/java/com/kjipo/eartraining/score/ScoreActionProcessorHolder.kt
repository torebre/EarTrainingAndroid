package com.kjipo.eartraining.score

import com.kjipo.eartraining.BaseSchedulerProvider
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

class ScoreActionProcessorHolder(private val earTrainer: EarTrainer,
                                 private val schedulerProvider: BaseSchedulerProvider) {


    private val generateScoreTaskProcessor =
            ObservableTransformer<ScoreAction.GenerateNewScore, ScoreActionResult.GenerateScoreResult> {
                // TODO

                Observable.empty<ScoreActionResult.GenerateScoreResult>()

            }


    private val playScoreProcessor =
            ObservableTransformer<ScoreAction, ScoreActionResult.PlayAction> { actions ->
                actions.flatMap { playAction ->
                    Single.fromCallable {
                        val midiScript = MidiScript(earTrainer.getSequenceGenerator().pitchSequence, earTrainer.getMidiInterface())
                        midiScript.play()
                        true
                    }.toObservable()
                            .map { it -> ScoreActionResult.PlayAction.Success }
                            .cast(ScoreActionResult.PlayAction::class.java)
                            .onErrorReturn(ScoreActionResult.PlayAction::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(ScoreActionResult.PlayAction.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<ScoreAction, ScoreActionResult> { actions ->
                actions.publish { shared ->
                    Observable.merge<ScoreActionResult>(
                            shared.ofType(ScoreAction.PlayScore::class.java)
                                    .compose(playScoreProcessor),
                            shared.ofType(ScoreAction.GenerateNewScore::class.java)
                                    .compose(generateScoreTaskProcessor))
                            .mergeWith(shared.filter { v ->
                                v !is ScoreAction.PlayScore
                                        && v !is ScoreAction.GenerateNewScore
                            }.flatMap { w ->
                                Observable.error<ScoreActionResult>(
                                        IllegalArgumentException("Unknown action: $w")
                                )
                            })
                }
            }

}