package com.kjipo.eartraining.score

import android.util.Log
import com.kjipo.eartraining.BaseSchedulerProvider
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.storage.EarTrainingDatabase
import com.kjipo.eartraining.storage.StoredSequence
import com.kjipo.eartraining.storage.SubmittedSequence
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

class ScoreActionProcessorHolder(private val earTrainer: EarTrainer,
                                 private val database: EarTrainingDatabase,
                                 private val schedulerProvider: BaseSchedulerProvider) {
    private var currentScoreId = 0L


    private val generateScoreTaskProcessor =
            ObservableTransformer<ScoreAction.GenerateNewScore, ScoreActionResult.GenerateScoreResult> { actions ->
                actions.flatMap {
                    Single.fromCallable {
                        earTrainer.createNewTrainingSequence()
                        currentScoreId = database.generatedSequenceDao().insertGeneratedSequence(StoredSequence())

                        ScoreActionResult.GenerateScoreResult.Success(earTrainer.getSequenceGenerator(), earTrainer.currentTargetSequence)
                    }.toObservable()
//                            .map { it -> ScoreActionResult.GenerateScoreResult.Success(it) }
                            .cast(ScoreActionResult.GenerateScoreResult::class.java)
                            .onErrorReturn(ScoreActionResult.GenerateScoreResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
//                            .startWith(ScoreActionResult.GenerateScoreResult.InFlight)
                }
            }


    private val playScoreProcessor =
            ObservableTransformer<ScoreAction, ScoreActionResult.PlayAction> { actions ->
                actions.flatMap {
                    Single.fromCallable {
                        val midiScript = MidiScript(earTrainer.getSequenceGenerator().pitchSequence, earTrainer.getMidiInterface())
                        midiScript.play()
                        true
                    }.toObservable()
                            .map { ScoreActionResult.PlayAction.Success }
                            .cast(ScoreActionResult.PlayAction::class.java)
                            .onErrorReturn(ScoreActionResult.PlayAction::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(ScoreActionResult.PlayAction.InFlight)
                }
            }

    private val targetPlayProcessor =
            ObservableTransformer<ScoreAction, ScoreActionResult.TargetPlayAction> { actions ->
                actions.flatMap {
                    Single.fromCallable {
                        val midiScript = MidiScript(earTrainer.currentTargetSequence.transformToPitchSequence(), earTrainer.getMidiInterface())
                        midiScript.play()
                        true
                    }.toObservable()
                            .map { it -> ScoreActionResult.TargetPlayAction.Success }
                            .cast(ScoreActionResult.TargetPlayAction::class.java)
                            .onErrorReturn(ScoreActionResult.TargetPlayAction::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(ScoreActionResult.TargetPlayAction.InFlight)
                }

            }


    private val submitProcessor = ObservableTransformer<ScoreAction, ScoreActionResult.SubmitAction> { actions ->
        actions.flatMap {
            Single.fromCallable {
                Log.i("Database", "Stored sequences:")
                database.generatedSequenceDao().getAllStoredSequences().forEach {
                    Log.i("Database", "Stored sequence: $it")
                }

                Log.i("Database", "Submitted sequences:")
                database.generatedSequenceDao().getAllSubmittedSequences().forEach {
                    Log.i("Database", "Submitted sequence: $it")
                }

                database.generatedSequenceDao().insertSubmittedSequence(SubmittedSequence(null, currentScoreId))

            }.toObservable().map { it -> ScoreActionResult.SubmitAction.Success(earTrainer.getSequenceGenerator()) }
                    .cast(ScoreActionResult.SubmitAction::class.java)
                    .onErrorReturn(ScoreActionResult.SubmitAction::Failure)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
        }
    }


    private val changeActiveElementTypeProcessor = ObservableTransformer<ScoreAction, ScoreActionResult.ChangeActiveElementAction> { actions ->
        actions.flatMap {
            when (it) {
                is ScoreAction.ChangeActiveElementType -> {
                    Observable.just(ScoreActionResult.ChangeActiveElementAction.Success)
                }
                else -> {
                    Observable.just(ScoreActionResult.ChangeActiveElementAction.InFlight)
                }

            }
        }
                .cast(ScoreActionResult.ChangeActiveElementAction::class.java)
                .onErrorReturn(ScoreActionResult.ChangeActiveElementAction::Failure)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .startWith(ScoreActionResult.ChangeActiveElementAction.Success)
    }

    internal var actionProcessor =
            ObservableTransformer<ScoreAction, ScoreActionResult> { actions ->
                actions.publish { shared ->
                    Observable.merge<ScoreActionResult>(listOf(
                            shared.ofType(ScoreAction.PlayScore::class.java)
                                    .compose(playScoreProcessor),
                            shared.ofType(ScoreAction.GenerateNewScore::class.java)
                                    .compose(generateScoreTaskProcessor),
                            shared.ofType(ScoreAction.Submit::class.java)
                                    .compose(submitProcessor),
                            shared.ofType(ScoreAction.TargetPlay::class.java)
                                    .compose(targetPlayProcessor),
                            shared.ofType(ScoreAction.ChangeActiveElementType::class.java)
                                    .compose(changeActiveElementTypeProcessor)))
                            .mergeWith(shared.filter { v ->
                                v !is ScoreAction.PlayScore
                                        && v !is ScoreAction.GenerateNewScore
                                        && v !is ScoreAction.Submit
                                        && v !is ScoreAction.TargetPlay
                                        && v !is ScoreAction.ChangeActiveElementType
                            }.flatMap { action ->
                                Observable.error<ScoreActionResult>(
                                        IllegalArgumentException("Unknown action: $action")
                                )
                            })
                }
            }

}