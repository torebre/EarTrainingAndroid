package com.kjipo.eartraining.score

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import io.reactivex.functions.BiFunction


class ScoreViewModel(private val actionProcessorHolder: ScoreActionProcessorHolder) : ViewModel() {


    fun processIntent(intent: Observable<ScoreIntent>) {
        intent
                .doOnSubscribe { Log.i("Rx", "Test24") }
                .subscribe(intentsSubject)
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
                    // TODO
                    previousState

                }
                is ScoreActionResult.GenerateScoreResult.Failure -> {
                    // TODO

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
                .doOnEach {

                    Log.i("Rx", "Test23")

                }
                .map(this::actionFromIntent)
                .filter { action -> action !is ScoreAction.Skip }
                .compose(actionProcessorHolder.actionProcessor)
                .scan(ScoreViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)


//                .compose<ScoreViewState>()
//                .map<AddEditTaskAction>(this::actionFromIntent)
//                // Special case where we do not want to pass this event down the stream
//                .filter { action -> action !is AddEditTaskAction.SkipMe }
//                .compose(actionProcessorHolder.actionProcessor)
//                // Cache each state and pass it to the reducer to create a new state from
//                // the previous cached one and the latest Result emitted from the action processor.
//                // The Scan operator is used here for the caching.
//                .scan(AddEditTaskViewState.idle(), reducer)
//                // When a reducer just emits previousState, there's no reason to call render. In fact,
//                // redrawing the UI in cases like this can cause jank (e.g. messing up snackbar animations
//                // by showing the same snackbar twice in rapid succession).
//                .distinctUntilChanged()
//                // Emit the last one event of the stream on subscription
//                // Useful when a View rebinds to the ViewModel after rotation.
//                .replay(1)
//                // Create the stream on creation without waiting for anyone to subscribe
//                // This allows the stream to stay alive even when the UI disconnects and
//                // match the stream's lifecycle to the ViewModel's one.
//                .autoConnect(0)
    }


    private fun actionFromIntent(intent: ScoreIntent): ScoreAction {
        return when (intent) {
            is ScoreIntent.InitialIntent -> {
                ScoreAction.GenerateNewScore(intent.taskId)
            }
            is ScoreIntent.PlayAction -> {
                ScoreAction.PlayScore(intent.taskId)
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