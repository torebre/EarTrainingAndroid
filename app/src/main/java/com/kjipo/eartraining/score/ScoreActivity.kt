package com.kjipo.eartraining.score


import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
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

    private var popupWindow: PopupWindow? = null

    val changeElementTypeSubject = BehaviorSubject.create<ScoreIntent.ChangeActiveElementType>()

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

    private fun intents(): Observable<ScoreIntent> = Observable.merge(listOf(initialIntent(),
            playIntent(),
            generateIntent(),
            submitIntent(),
            targetIntent(),
            changeElementTypeSubject,
            chooseElementIntent(),
            insertElement(),
            selectLeft(),
            selectRight(),
            moveNoteUp(),
            moveNoteDown()))


    private fun playIntent(): Observable<ScoreIntent.PlayAction> = RxView.clicks(btnPlay).map {
        ScoreIntent.PlayAction
    }


    private fun targetIntent(): Observable<ScoreIntent.TargetAction> = RxView.clicks(btnTarget).map {
        ScoreIntent.TargetAction
    }

    private fun generateIntent(): Observable<ScoreIntent.GenerateIntent> = RxView.clicks(btnGenerate).map {
        ScoreIntent.GenerateIntent
    }


    private fun submitIntent(): Observable<ScoreIntent.SubmitIntent> = RxView.clicks(btnSubmit).map {
        ScoreIntent.SubmitIntent
    }

    private fun chooseElementIntent() = RxView.clicks(btnChooseElement).map {
        ScoreIntent.ChangeActiveElementType.OpenMenu
    }

    private fun initialIntent(): Observable<ScoreIntent.InitialIntent> = Observable.just(ScoreIntent.InitialIntent)

    private fun insertElement(): Observable<ScoreIntent.InsertElementIntent> = RxView.clicks(btnInsert).flatMap {
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

    private fun moveNoteUp(): Observable<ScoreIntent.MoveNote> = RxView.clicks(btnUp).flatMap {
        Observable.create(ObservableOnSubscribe<ScoreIntent.MoveNote> { subscriber ->
            noteViewClient.getIdOfActiveElement {
                if (!subscriber.isDisposed && it != null) {
                    subscriber.onNext(ScoreIntent.MoveNote(it, true))
                    subscriber.onComplete()
                }
            }
        })
    }


    private fun moveNoteDown(): Observable<ScoreIntent.MoveNote> = RxView.clicks(btnDown).flatMap {
        Observable.create(ObservableOnSubscribe<ScoreIntent.MoveNote> { subscriber ->
            noteViewClient.getIdOfActiveElement {
                if (!subscriber.isDisposed && it != null) {
                    subscriber.onNext(ScoreIntent.MoveNote(it, false))
                    subscriber.onComplete()
                }
            }
        })
    }


    private fun selectRight(): Observable<ScoreIntent.ChangeActiveElement> = RxView.clicks(btnRight).flatMap {
        Observable.create(ObservableOnSubscribe<ScoreIntent.ChangeActiveElement> { subscriber ->
            noteViewClient.getIdOfActiveElement { activeElementId ->
                if (!subscriber.isDisposed && activeElementId != null) {
                    subscriber.onNext(ScoreIntent.ChangeActiveElement(activeElementId, false))
                    subscriber.onComplete()
                }
            }
        })
    }

    private fun selectLeft(): Observable<ScoreIntent.ChangeActiveElement> = RxView.clicks(btnLeft).flatMap {
        Observable.create(ObservableOnSubscribe<ScoreIntent.ChangeActiveElement> { subscriber ->
            noteViewClient.getIdOfActiveElement { activeElementId ->
                if (!subscriber.isDisposed && activeElementId != null) {
                    subscriber.onNext(ScoreIntent.ChangeActiveElement(activeElementId, true))
                    subscriber.onComplete()
                }
            }
        })
    }


    override fun onStop() {
        super.onStop()
        earTrainer.getMidiInterface().stop()
        disposable.clear()
    }

    private fun render(state: ScoreViewState) {
        btnPlay.isEnabled = !state.isPlaying
        btnSubmit.isEnabled = !state.submitted

        state.sequenceGenerator?.let { sequenceGenerator ->
            noteViewClient.let {
                it.scoreHandler?.scoreHandler = sequenceGenerator
                it.updateWebscore()
            }
        }

        if (state.chooseTargetMenu) {
            val webView = findViewById<View>(R.id.score)
            val inflater = webView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.note_chooser, null)

            popupView.findViewById<Button>(R.id.btnQuarter)?.run {
                setOnClickListener {
                    changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(Duration.QUARTER, true))
                }
            }

            popupView.findViewById<ImageButton>(R.id.btnHalf)?.run {
                setOnClickListener {
                    changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(Duration.HALF, true))
                }
            }

            popupView.findViewById<Button>(R.id.btnWhole)?.run {
                setOnClickListener {
                    changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.UpdateValue(Duration.WHOLE, true))
                }
            }

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            popupWindow = PopupWindow(popupView, width, height, focusable).apply {
                showAtLocation(webView, Gravity.CENTER, 0, 0)

                popupView.setOnTouchListener { _, _ ->
                    dismiss()
                    changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.CloseMenu)
                    true
                }
            }
        }
        else {
            if(popupWindow != null) {
                popupWindow?.dismiss()
                changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.CloseMenu)
                popupWindow = null
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
