package com.kjipo.eartraining.score

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject


class ScoreViewModel(private val actionProcessorHolder: ScoreActionProcessorHolder) : ViewModel() {


    fun processIntent(intent: Observable<ScoreIntent>) {
        intent.subscribe(intentsSubject)
    }

    private val intentFilter: ObservableTransformer<ScoreIntent, ScoreIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<ScoreIntent>(
                        shared.ofType(ScoreIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(ScoreIntent.InitialIntent::class.java)
                )
            }
        }


    fun states(): Observable<ScoreViewState> = states


    private val reducer = BiFunction { previousState: ScoreViewState,
                                       result: ScoreActionResult ->
        when (result) {
            is ScoreActionResult.GenerateScoreResult.Success -> {
                previousState.copy(isPlaying = false,
                        sequenceGenerator = result.sequenceGenerator,
                        scoreCounter = if (previousState.scoreCounter == null) {
                            0
                        } else {
                            previousState.scoreCounter + 1
                        }, submitted = false,
                        addTargetScore = false)
            }
            is ScoreActionResult.GenerateScoreResult.Failure -> {
                previousState
            }
            is ScoreActionResult.SubmitAction.Success -> {
                previousState.copy(isPlaying = false,
                        sequenceGenerator = result.sequenceGenerator,
                        scoreCounter = if (previousState.scoreCounter == null) {
                            0
                        } else {
                            previousState.scoreCounter + 1
                        },
                        submitted = true,
                        addTargetScore = true)
            }
            is ScoreActionResult.SubmitAction.Failure -> {
                previousState
            }
            is ScoreActionResult.PlayAction -> when (result) {
                is ScoreActionResult.PlayAction.Success -> previousState.copy(isPlaying = false)
                is ScoreActionResult.PlayAction.Failure -> previousState.copy(isPlaying = false)
                is ScoreActionResult.PlayAction.InFlight -> previousState.copy(isPlaying = true)
            }
            is ScoreActionResult.TargetPlayAction -> when (result) {
                is ScoreActionResult.TargetPlayAction.Success -> previousState.copy(isPlaying = false)
                is ScoreActionResult.TargetPlayAction.Failure -> previousState.copy(isPlaying = false)
                is ScoreActionResult.TargetPlayAction.InFlight -> previousState.copy(isPlaying = true)
            }
            is ScoreActionResult.ChangeActiveElementAction -> when (result) {
                is ScoreActionResult.ChangeActiveElementAction.Success -> previousState.copy(chooseTargetMenu = false)
                is ScoreActionResult.ChangeActiveElementAction.Failure -> previousState.copy(chooseTargetMenu = false)
                is ScoreActionResult.ChangeActiveElementAction.InFlight -> previousState.copy(chooseTargetMenu = true)
            }
        }

    }

    private fun compose(): Observable<ScoreViewState> {
        return intentsSubject.compose<ScoreIntent>(intentFilter)
                .map(this::actionFromIntent)
                .filter { action -> action !is ScoreAction.Skip }
                .compose(actionProcessorHolder.actionProcessor)
                .scan(ScoreViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }


    private fun actionFromIntent(intent: ScoreIntent): ScoreAction {
        return when (intent) {
            is ScoreIntent.InitialIntent -> {
                ScoreAction.Skip
            }
            is ScoreIntent.PlayAction -> {
                ScoreAction.PlayScore
            }
            is ScoreIntent.GenerateIntent -> {
                ScoreAction.GenerateNewScore
            }
            is ScoreIntent.SubmitIntent -> {
                ScoreAction.Submit
            }
            is ScoreIntent.TargetAction -> {
                ScoreAction.TargetPlay
            }
            is ScoreIntent.ChangeActiveElementType -> {
                ScoreAction.ChangeActiveElementType
            }
        }
    }


    fun <T : Any, U : Any> Observable<T>.notOfType(clazz: Class<U>): Observable<T> {
        checkNotNull(clazz) { "clazz is null" }
        return filter { !clazz.isInstance(it) }
    }

    private val intentsSubject: PublishSubject<ScoreIntent> = PublishSubject.create()
    private val states: Observable<ScoreViewState> = compose()


}