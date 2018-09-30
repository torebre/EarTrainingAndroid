package com.kjipo.eartraining.score

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import io.reactivex.functions.BiFunction


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
            is ScoreActionResult.GenerateScoreResult -> when (result) {
                is ScoreActionResult.GenerateScoreResult.Success -> {
                    previousState.copy(false, result.sequenceGenerator,
                            if (previousState.scoreCounter == null) {
                                0
                            } else {
                                previousState.scoreCounter + 1
                            })
                }
                is ScoreActionResult.GenerateScoreResult.Failure -> {
                    previousState
                }
            }
            is ScoreActionResult.PlayAction -> when (result) {
                is ScoreActionResult.PlayAction.Success -> previousState.copy(false)
                is ScoreActionResult.PlayAction.Failure -> previousState.copy(false)
                is ScoreActionResult.PlayAction.InFlight -> previousState.copy(true)
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
                ScoreAction.PlayScore(intent.taskId)
            }
            is ScoreIntent.GenerateIntent -> {
                ScoreAction.GenerateNewScore
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