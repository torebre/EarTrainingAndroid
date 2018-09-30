package com.kjipo.eartraining.score


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.jakewharton.rxbinding2.view.RxView
import com.kjipo.eartraining.CustomWebViewClient
import com.kjipo.eartraining.R
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.recorder.Recorder
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.score_act.*
import javax.inject.Inject


class ScoreActivity : AppCompatActivity() {

    private val AUDIO_ECHO_REQUEST = 0

    @Inject
    lateinit var earTrainer: EarTrainer
    @Inject
    lateinit var recorder: Recorder

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: ScoreViewModel

    private var midiScript: MidiScript? = null
    private var noteViewClient: CustomWebViewClient? = null

    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelFactory(earTrainer)
        viewModel = viewModelFactory.create(ScoreViewModel::class.java)

        earTrainer.getSequenceGenerator().createNewSequence()
        setContentView(R.layout.score_act)

        // This enables the possibility of debugging the webview from Chrome
        WebView.setWebContentsDebuggingEnabled(true)

        val myWebView = findViewById<View>(R.id.score) as WebView

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

        btnRecord.setOnClickListener { record() }
    }

    override fun onStart() {
        super.onStart()
        earTrainer.getMidiInterface().start()

        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntent(intents())


    }


    fun intents(): Observable<ScoreIntent> {
        return Observable.merge(initialIntent(), playIntent(), generateIntent())
    }

    private fun playIntent(): Observable<ScoreIntent.PlayAction> {
        return RxView.clicks(btnPlay).map {
            ScoreIntent.PlayAction("play")
        }
    }

    private fun generateIntent(): Observable<ScoreIntent.GenerateIntent> {
        return RxView.clicks(btnGenerate).map {
            ScoreIntent.GenerateIntent()
        }
    }


    private fun initialIntent(): Observable<ScoreIntent.InitialIntent> {
        return Observable.just(ScoreIntent.InitialIntent("initialTask"))
    }

    override fun onStop() {
        super.onStop()
        earTrainer.getMidiInterface().stop()
        disposable.clear()
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

    fun record() {
        Log.i("Record", "Record button pressed")

        val status = ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.RECORD_AUDIO)
        if (status != PackageManager.PERMISSION_GRANTED) {

            Log.i("Record", "Requesting permissions")

            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    AUDIO_ECHO_REQUEST)
            return
        }

        Log.i("Record", "Calling recordAudio")
        recorder.recordAudio()
    }


    fun render(state: ScoreViewState) {
        btnPlay.isEnabled = !state.isPlaying

        state.sequenceGenerator?.let { sequenceGenerator ->
            noteViewClient?.let {
                it.scoreHandler?.scoreHandler = sequenceGenerator
                it.updateWebscore()
            }
        }

    }

}
