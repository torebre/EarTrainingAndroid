package com.kjipo.eartraining.score


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.Button
import com.kjipo.eartraining.CustomWebViewClient
import com.kjipo.eartraining.R
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.midi.MidiPlayerInterface
import com.kjipo.eartraining.recorder.Recorder
import com.kjipo.eartraining.svg.SequenceToSvg
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
    lateinit var sequenceToSvg: SequenceToSvg
    @Inject
    lateinit var recorder: Recorder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.score_act)

        // TODO Is this necessary?
        WebView.setWebContentsDebuggingEnabled(true);


        val myWebView = findViewById<View>(R.id.score) as WebView

        val noteViewClient = CustomWebViewClient()
        noteViewClient.attachWebView(myWebView)

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        btnPlay.setOnClickListener {
            Log.i("Playing note", "Test")
            midiPlayer.noteOn(60)

            val script = "var s2 = Snap(1000, 1000);\n" +
                    "var bigCircle = s2.circle(10, 10, 10);\n" +
                    "bigCircle.attr({\n" +
                    "    fill: \"#bada55\",\n" +
                    "    stroke: \"#000\",\n" +
                    "    strokeWidth: 5\n" +
                    "});" +
                    "console.log(\"Test20\");"

            Log.i("Test", "Evaluating Javascript")
            myWebView.evaluateJavascript(script) { value -> Log.i("Test2", "Received value: " + value) }
        }


        val recordButton = findViewById<Button>(R.id.btnRecord)
        recordButton.setOnClickListener {
            record()
        }

        val generateButton = findViewById<Button>(R.id.btnGenerate)
        generateButton.setOnClickListener {
            val sequence = earTrainer.generateNextSequence()
            noteViewClient.loadNoteSequence(sequenceToSvg.transformToSvg(sequence))
        }

        midiPlayer.setup(applicationContext)

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


    override fun onTouchEvent(event: MotionEvent): Boolean {

        // TODO Just here to check when this event is triggered

        val action = event.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("debug", "Action was DOWN")
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("debug", "Action was MOVE")
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d("debug", "Action was UP")
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("debug", "Action was CANCEL")
                return true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d("debug", "Movement occurred outside bounds " + "of current screen element")
                return true
            }
            else -> return super.onTouchEvent(event)
        }
    }


}
