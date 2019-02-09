package com.kjipo.eartraining.recorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dagger.android.AndroidInjection
import javax.inject.Inject

class RecordActivity: AppCompatActivity() {

    private val AUDIO_ECHO_REQUEST = 0


    @Inject
    lateinit var recorder: Recorder


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)


    }


//    private fun recordIntent(): Observable<RecordIntent.Record> {
//        return RxView.clicks(btnRecord).map {
//            RecordIntent.Record()
//        }
//    }


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