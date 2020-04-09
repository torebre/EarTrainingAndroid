package com.kjipo.eartraining.score

import android.util.Log
import com.kjipo.eartraining.BaseSchedulerProvider
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.storage.EarTrainingDatabase
import com.kjipo.eartraining.storage.StoredSequence
import com.kjipo.eartraining.storage.SubmittedSequence
import com.kjipo.handler.ScoreHandlerElement
import com.kjipo.score.Duration
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

class ScoreActionProcessorHolder(private val earTrainer: EarTrainer,
                                 private val database: EarTrainingDatabase,
                                 private val schedulerProvider: BaseSchedulerProvider) {
    private var currentScoreId = 0L
    private var currentActiveDuration: Duration? = Duration.QUARTER
    private var isNote = true


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
                            .dropBreadcrumb()
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
                            .dropBreadcrumb()
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
                            .map { ScoreActionResult.TargetPlayAction.Success }
                            .cast(ScoreActionResult.TargetPlayAction::class.java)
                            .onErrorReturn(ScoreActionResult.TargetPlayAction::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .dropBreadcrumb()
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
                    .dropBreadcrumb()
                    .observeOn(schedulerProvider.ui())
        }
    }


    private val changeActiveElementTypeProcessor = ObservableTransformer<ScoreAction.ChangeActiveElementType, ScoreActionResult.ChangeActiveElementAction> { actions ->
        actions.flatMap {
            when (it) {
                is ScoreAction.ChangeActiveElementType.ShowMenu -> {
                    Observable.just(ScoreActionResult.ChangeActiveElementAction.ShowMenu)
                }
                is ScoreAction.ChangeActiveElementType.UpdateValue -> {
                    currentActiveDuration = it.selectedElement
                    isNote = it.isNote
                    Observable.just(ScoreActionResult.ChangeActiveElementAction.UpdateValueAndHide(it.selectedElement, it.isNote))
                }
                is ScoreAction.ChangeActiveElementType.HideMenu -> {
                    Observable.just(ScoreActionResult.ChangeActiveElementAction.HideMenu)
                }
            }
        }
                .cast(ScoreActionResult.ChangeActiveElementAction::class.java)
                .onErrorReturn(ScoreActionResult.ChangeActiveElementAction::Failure)
                .subscribeOn(schedulerProvider.io())
                .dropBreadcrumb()
                .observeOn(schedulerProvider.ui())
    }

    private val insertElementProcessor = ObservableTransformer<ScoreAction.InsertElement, ScoreActionResult.ScoreUpdated> { actions ->
        actions.flatMap {
            // TODO Hardcoded pitch only for testing
            earTrainer.getSequenceGenerator().insertNote(it.activeElement, currentActiveDuration?.let { it }
                    ?: Duration.QUARTER, 60)
            Observable.just(ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), null))
        }
                .subscribeOn(schedulerProvider.io())
                .dropBreadcrumb()
                .observeOn(schedulerProvider.ui())
    }

    private val activeElementMoveProcessor = ObservableTransformer<ScoreAction.ActiveElementSelect, ScoreActionResult.ScoreUpdated> { actions ->
        actions.flatMap { activeElementSelectAction ->
            val scoreHandlerElements = earTrainer.getSequenceGenerator().scoreHandler.getScoreHandlerElements()
            Observable.just(scoreHandlerElements.find { it.id == activeElementSelectAction.activeElement }.let {

                if (it == null) {
                    ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), earTrainer.getSequenceGenerator().getIdOfFirstSelectableElement())
                } else {
                    scoreHandlerElements.indexOf(it).let { index ->
                        getScoreUpdatedResult(index, activeElementSelectAction, scoreHandlerElements)
                    }
                }
            })
        }
                .subscribeOn(schedulerProvider.io())
                .dropBreadcrumb()
                .observeOn(schedulerProvider.ui())
    }

    private fun getScoreUpdatedResult(index: Int, activeElementSelectAction: ScoreAction.ActiveElementSelect, scoreHandlerElements: List<ScoreHandlerElement>): ScoreActionResult.ScoreUpdated {
        return if (index == -1) {
            ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), earTrainer.getSequenceGenerator().getIdOfFirstSelectableElement())
        } else if (activeElementSelectAction.left) {
            if (index == 0) {
                ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), earTrainer.getSequenceGenerator().getIdOfFirstSelectableElement())
            } else {
                ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), scoreHandlerElements[index - 1].id)
            }
        } else {
            if (index == scoreHandlerElements.lastIndex) {
                ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), earTrainer.getSequenceGenerator().getIdOfFirstSelectableElement())
            } else {
                ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), scoreHandlerElements[index + 1].id)
            }
        }
    }


    private val moveNoteProcessor = ObservableTransformer<ScoreAction.MoveNote, ScoreActionResult.ScoreUpdated> { actions ->
        actions.flatMap { moveNoteAction ->
            val scoreHandlerElements = earTrainer.getSequenceGenerator().scoreHandler.getScoreHandlerElements()
            scoreHandlerElements.find { it.id == moveNoteAction.selectedElement }?.let {
                if (it.isNote) {
                    earTrainer.getSequenceGenerator().moveNoteOneStep(moveNoteAction.selectedElement, moveNoteAction.up)
                }
            }
            Observable.just(ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), moveNoteAction.selectedElement))
        }
                .subscribeOn(schedulerProvider.io())
                .dropBreadcrumb()
                .observeOn(schedulerProvider.ui())
    }


    private val deleteNoteProcessor = ObservableTransformer<ScoreAction.DeleteNote, ScoreActionResult.ScoreUpdated> { actions ->
        actions.flatMap { deleteNoteAction ->
            earTrainer.getSequenceGenerator().deleteElement(deleteNoteAction.selectedElement)
            Observable.just(ScoreActionResult.ScoreUpdated(earTrainer.getSequenceGenerator(), deleteNoteAction.selectedElement))
        }
                .subscribeOn(schedulerProvider.io())
                .dropBreadcrumb()
                .observeOn(schedulerProvider.ui())
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
                                    .compose(changeActiveElementTypeProcessor),
                            shared.ofType(ScoreAction.InsertElement::class.java).compose(insertElementProcessor),
                            shared.ofType(ScoreAction.ActiveElementSelect::class.java).compose(activeElementMoveProcessor),
                            shared.ofType(ScoreAction.MoveNote::class.java).compose(moveNoteProcessor),
                            shared.ofType(ScoreAction.DeleteNote::class.java).compose(deleteNoteProcessor)))
                            .mergeWith(shared.filter { v ->
                                v !is ScoreAction.PlayScore
                                        && v !is ScoreAction.GenerateNewScore
                                        && v !is ScoreAction.Submit
                                        && v !is ScoreAction.TargetPlay
                                        && v !is ScoreAction.ChangeActiveElementType
                                        && v !is ScoreAction.InsertElement
                                        && v !is ScoreAction.ActiveElementSelect
                                        && v !is ScoreAction.MoveNote
                                        && v !is ScoreAction.DeleteNote
                            }.flatMap { action ->
                                Observable.error<ScoreActionResult>(
                                        IllegalArgumentException("Unknown action: $action")
                                )
                            })
                }
            }

}