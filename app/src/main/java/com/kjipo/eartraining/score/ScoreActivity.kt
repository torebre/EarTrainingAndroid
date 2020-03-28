package com.kjipo.eartraining.score


import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.webkit.WebView
import android.widget.PopupMenu
import com.jakewharton.rxbinding2.view.RxView
import com.kjipo.eartraining.CustomWebViewClient
import com.kjipo.eartraining.R
import com.kjipo.eartraining.ScoreWebView
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.storage.EarTrainingDatabase
import com.kjipo.score.Duration
import com.kjipo.scoregenerator.SequenceGenerator
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.score_act.*
import org.koin.android.ext.android.inject


class ScoreActivity : AppCompatActivity() {
    private val earTrainer: EarTrainer by inject()
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ScoreViewModel

    private var midiScript: MidiScript? = null
    private lateinit var noteViewClient: CustomWebViewClient

    private val disposable = CompositeDisposable()

    private val changeElementTypeSubject = BehaviorSubject.create<ScoreIntent.ChangeActiveElementType>()

//    private var velocityTracker: VelocityTracker? = null

//    private var submittedLatch = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(applicationContext,
                EarTrainingDatabase::class.java, "ear_training_database")
                .fallbackToDestructiveMigration() // TODO Only for development
                .build()

        viewModelFactory = ViewModelFactory(earTrainer, database)
        viewModel = viewModelFactory.create(ScoreViewModel::class.java)

        setContentView(R.layout.score_act)

        // This enables the possibility of debugging the webview from Chrome
        WebView.setWebContentsDebuggingEnabled(true)

        val myWebView = findViewById<View>(R.id.score) as ScoreWebView

        earTrainer.createNewTrainingSequence()
        val scoreHandlerWrapper = ScoreHandlerWrapper(earTrainer.getSequenceGenerator())
        scoreHandlerWrapper.listeners.add(object : ScoreHandlerListener {
            override fun pitchSequenceChanged() {
                midiScript = MidiScript(earTrainer.getSequenceGenerator().pitchSequence, earTrainer.getMidiInterface())
            }
        })

        noteViewClient = CustomWebViewClient().also {
            it.attachWebView(myWebView, this.assets, scoreHandlerWrapper)
            it.loadNoteSequence()
        }




    }

    override fun onStart() {
        super.onStart()
        earTrainer.getMidiInterface().start()

        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntent(intents())
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when(event.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                velocityTracker?.clear()
//                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
//                velocityTracker?.addMovement(event)
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                velocityTracker?.apply {
//                    val pointerId: Int = event.getPointerId(event.actionIndex)
//                    addMovement(event)
//                    computeCurrentVelocity(1000)
//
//                    Log.i("Mouse", "X velocity: ${getXVelocity(pointerId)}")
//                    Log.i("Mouse", "Y velocity: ${getYVelocity(pointerId)}")
//                }
//            }
//
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                velocityTracker?.recycle()
//                velocityTracker = null
//            }
//
//        }
//
//        return true
//    }

    private fun intents(): Observable<ScoreIntent> {
        return Observable.merge(listOf(initialIntent(), playIntent(), generateIntent(), submitIntent(), targetIntent(), changeActiveElementType(), changeElementTypeSubject, insertElement(), selectLeft(), selectRight(), moveNoteUp(), moveNoteDown()))
    }

    private fun playIntent(): Observable<ScoreIntent.PlayAction> {
        return RxView.clicks(btnPlay).map {
            ScoreIntent.PlayAction
        }
    }

    private fun targetIntent(): Observable<ScoreIntent.TargetAction> {
        return RxView.clicks(btnTarget).map {
            ScoreIntent.TargetAction
        }
    }

    private fun generateIntent(): Observable<ScoreIntent.GenerateIntent> {
        return RxView.clicks(btnGenerate).map {
            ScoreIntent.GenerateIntent
        }
    }

    private fun submitIntent(): Observable<ScoreIntent.SubmitIntent> {
        return RxView.clicks(btnSubmit).map {
            ScoreIntent.SubmitIntent
        }
    }

    private fun initialIntent(): Observable<ScoreIntent.InitialIntent> {
        return Observable.just(ScoreIntent.InitialIntent)
    }

    private fun changeActiveElementType(): Observable<ScoreIntent.ChangeActiveElementType> {
        return RxView.clicks(btnChangeActive).map { ScoreIntent.ChangeActiveElementType.OpenMenu }
    }

    private fun insertElement(): Observable<ScoreIntent.InsertElementIntent> {
        return RxView.clicks(btnInsert).flatMap {
            Observable.create(
                    ObservableOnSubscribe<ScoreIntent.InsertElementIntent> { subscriber ->
                        noteViewClient.getIdOfActiveElement {
                            if (!subscriber.isDisposed) {
                                subscriber.onNext(ScoreIntent.InsertElementIntent(it))
                                subscriber.onComplete()
                            }
                        }
                    })
        }
    }

    private fun moveNoteUp(): Observable<ScoreIntent.MoveNote> {
        return RxView.clicks(btnUp).flatMap {
            Observable.create(ObservableOnSubscribe<ScoreIntent.MoveNote> { subscriber ->
                noteViewClient.getIdOfActiveElement {
                    if (!subscriber.isDisposed && it != null) {
                        subscriber.onNext(ScoreIntent.MoveNote(it, true))
                        subscriber.onComplete()
                    }
                }
            })
        }
    }

    private fun moveNoteDown(): Observable<ScoreIntent.MoveNote> {
        return RxView.clicks(btnDown).flatMap {
            Observable.create(ObservableOnSubscribe<ScoreIntent.MoveNote> { subscriber ->
                noteViewClient.getIdOfActiveElement {
                    if (!subscriber.isDisposed && it != null) {
                        subscriber.onNext(ScoreIntent.MoveNote(it, false))
                        subscriber.onComplete()
                    }
                }
            })
        }
    }

    private fun selectRight(): Observable<ScoreIntent.ChangeActiveElement> {
        return RxView.clicks(btnRight).flatMap {
            Observable.create(ObservableOnSubscribe<ScoreIntent.ChangeActiveElement> { subscriber ->
                noteViewClient.getIdOfActiveElement { activeElementId ->
                    if (!subscriber.isDisposed && activeElementId != null) {
                        subscriber.onNext(ScoreIntent.ChangeActiveElement(activeElementId, false))
                        subscriber.onComplete()
                    }
                }
            })
        }
    }

    private fun selectLeft(): Observable<ScoreIntent.ChangeActiveElement> {
        return RxView.clicks(btnLeft).flatMap {
            Observable.create(ObservableOnSubscribe<ScoreIntent.ChangeActiveElement> { subscriber ->
                noteViewClient.getIdOfActiveElement { activeElementId ->
                    if (!subscriber.isDisposed && activeElementId != null) {
                        subscriber.onNext(ScoreIntent.ChangeActiveElement(activeElementId, true))
                        subscriber.onComplete()
                    }
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        earTrainer.getMidiInterface().stop()
        disposable.clear()
    }

    private fun render(state: ScoreViewState) {
        btnPlay.isEnabled = !state.isPlaying
        btnSubmit.isEnabled = !state.submitted

//        if(state.submitted && !submittedLatch) {
//            submittedLatch = true
//        }

//        if(!state.submitted && submittedLatch) {
        state.sequenceGenerator?.let { sequenceGenerator ->
            noteViewClient.let {
                it.scoreHandler?.scoreHandler = sequenceGenerator
                it.updateWebscore()
            }
        }
//            submittedLatch = false
//        }


        if (state.chooseTargetMenu) {
            val popupMenu = PopupMenu(this, btnChangeActive)
            popupMenu.menuInflater.inflate(R.menu.choose_input_type, popupMenu.menu)
            popupMenu.show()

            popupMenu.setOnDismissListener({ changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(null, true)) })

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.test_item -> {
                        changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(Duration.HALF, true))
                    }
                    R.id.test_item2 -> {
                        changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(Duration.QUARTER, true))
                    }
                }
                popupMenu.dismiss()
                true
            }
        }

        if (state.addTargetScore) {
            val sequenceGenerator = SequenceGenerator()
            sequenceGenerator.loadSimpleNoteSequence(earTrainer.currentTargetSequence)
            val targetSequenceWrapper = ScoreHandlerWrapper(sequenceGenerator)
            noteViewClient.loadSecondScore(targetSequenceWrapper, "targetSequence")
        }

        if (state.activeElement != null) {
            noteViewClient.setActiveElement(state.activeElement)
        }

    }

    companion object {

        fun start(activity: Activity, options: ActivityOptionsCompat) {
            val starter = getStartIntent(activity)
            ActivityCompat.startActivity(activity, starter, options.toBundle())
        }

        fun start(context: Context) {
            val starter = getStartIntent(context)
            context.startActivity(starter)

        }

        internal fun getStartIntent(context: Context): Intent {
            return Intent(context, ScoreActivity::class.java)
        }
    }


}
