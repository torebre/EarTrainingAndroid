package com.kjipo.eartraining.score


import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.kjipo.eartraining.svg.SequenceToSvg
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class ScoreActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var earTrainer: EarTrainer
    @Inject
    lateinit var midiPlayer: MidiPlayerInterface
    @Inject
    lateinit var sequenceToSvg: SequenceToSvg


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.score_act)

        val myWebView = findViewById<View>(R.id.score) as WebView


        val noteViewClient = CustomWebViewClient()
        noteViewClient.attachWebView(myWebView)

        val btnPlay = findViewById<Button>(R.id.btnGenerate)
        btnPlay.setOnClickListener {
            Log.i("Playing note", "Test")
            midiPlayer!!.noteOn(60)

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

        midiPlayer!!.setup(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        midiPlayer!!.start()
    }

    override fun onStop() {
        super.onStop()
        midiPlayer!!.stop()
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

}
