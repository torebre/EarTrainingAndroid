package com.kjipo.eartraining.score


import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.widget.PopupMenu
import com.jakewharton.rxbinding2.view.RxView
import com.kjipo.eartraining.CustomWebViewClient
import com.kjipo.eartraining.R
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.storage.EarTrainingDatabase
import com.kjipo.scoregenerator.SequenceGenerator
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.score_act.*
import org.koin.android.ext.android.inject


class ScoreActivity : AppCompatActivity() {
    private val earTrainer: EarTrainer by inject()
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ScoreViewModel

    private var midiScript: MidiScript? = null
    private var noteViewClient: CustomWebViewClient? = null

    private val disposable = CompositeDisposable()


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

        val myWebView = findViewById<View>(R.id.score) as WebView

        earTrainer.createNewTrainingSequence()
        val scoreHandlerWrapper = ScoreHandlerWrapper(earTrainer.getSequenceGenerator())
        scoreHandlerWrapper.listeners.add(object : ScoreHandlerListener {
            override fun pitchSequenceChanged() {
                midiScript = MidiScript(earTrainer.getSequenceGenerator().pitchSequence, earTrainer.getMidiInterface())
            }
        })

        noteViewClient = CustomWebViewClient()
        noteViewClient?.let {
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

    private fun intents(): Observable<ScoreIntent> {
        return Observable.merge(listOf(initialIntent(), playIntent(), generateIntent(), submitIntent(), targetIntent(), changeActiveElementType()))
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
        return RxView.clicks(btnChangeActive).map { ScoreIntent.ChangeActiveElementType }
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
            noteViewClient?.let {
                it.scoreHandler?.scoreHandler = sequenceGenerator
                it.updateWebscore()
            }
        }

        if (state.chooseTargetMenu) {
            val popupMenu = PopupMenu(this, btnChangeActive)
            popupMenu.menuInflater.inflate(R.menu.choose_input_type, popupMenu.menu)
            popupMenu.show()
        }

        if (state.addTargetScore) {
            val sequenceGenerator = SequenceGenerator()
            sequenceGenerator.loadSimpleNoteSequence(earTrainer.currentTargetSequence)
            val targetSequenceWrapper = ScoreHandlerWrapper(sequenceGenerator)
            noteViewClient?.let {
                it.loadSecondScore(targetSequenceWrapper, "targetSequence")
            }
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
