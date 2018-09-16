package com.kjipo.eartraining.score


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Button
import com.kjipo.eartraining.CustomWebViewClient
import com.kjipo.eartraining.R
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiPlayerInterface
import com.kjipo.eartraining.midi.MidiScript
import com.kjipo.eartraining.recorder.Recorder
import com.kjipo.handler.ScoreHandler
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class ScoreActivity : AppCompatActivity(), HasSupportFragmentInjector {

    private val AUDIO_ECHO_REQUEST = 0

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var earTrainer: EarTrainer
    @Inject
    lateinit var midiPlayer: MidiPlayerInterface
    @Inject
    lateinit var recorder: Recorder

    private var midiScript: MidiScript? = null
    private var noteViewClient: CustomWebViewClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        earTrainer.sequenceGenerator.createNewSequence()
        setContentView(R.layout.score_act)

        // This enables the possibility of debugging the webview from Chrome
        WebView.setWebContentsDebuggingEnabled(true)

        val myWebView = findViewById<View>(R.id.score) as WebView

        val scoreHandlerWrapper = ScoreHandlerWrapper(earTrainer.sequenceGenerator)
        scoreHandlerWrapper.listeners.add(object : ScoreHandlerListener {
            override fun pitchSequenceChanged() {
                midiScript = MidiScript(earTrainer.sequenceGenerator.pitchSequence, midiPlayer)
            }
        })

        noteViewClient = CustomWebViewClient()
        noteViewClient?.let {
            it.attachWebView(myWebView, this.assets, scoreHandlerWrapper)
            it.loadNoteSequence()
        }

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        btnPlay.setOnClickListener {
            midiScript?.play()
        }


        val recordButton = findViewById<Button>(R.id.btnRecord)
        recordButton.setOnClickListener {
            record()
        }

        val generateButton = findViewById<Button>(R.id.btnGenerate)
        generateButton.setOnClickListener {
            setupSequence()
        }

        midiPlayer.setup(applicationContext)
    }

    private fun setupSequence() {
        earTrainer.sequenceGenerator.createNewSequence()

        midiScript = MidiScript(earTrainer.sequenceGenerator.pitchSequence, midiPlayer)
        noteViewClient?.let {
            earTrainer.sequenceGenerator.scoreHandler.updateScore()
            it.scoreHandler?.scoreHandler = earTrainer.sequenceGenerator
            it.updateWebscore()
        }
    }


    override fun onStart() {
        super.onStart()
        midiPlayer.start()
    }

    override fun onStop() {
        super.onStop()
        midiPlayer.stop()
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


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // TODO Why does returning the object without a cast not work?
        return dispatchingAndroidInjector as AndroidInjector<Fragment>
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

}
