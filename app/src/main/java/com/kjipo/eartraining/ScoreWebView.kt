package com.kjipo.eartraining

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView
import com.kjipo.eartraining.score.ScoreActivity
import com.kjipo.eartraining.score.ScoreIntent


class ScoreWebView(context: Context?, attrs: AttributeSet?) : WebView(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            (context as ScoreActivity).changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.OpenMenu)
        }
        Log.i("Mouse","Event: $event")

        return true
    }

}