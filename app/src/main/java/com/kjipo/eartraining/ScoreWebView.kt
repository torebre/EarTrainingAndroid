package com.kjipo.eartraining

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.webkit.WebView
import com.kjipo.eartraining.score.ScoreActivity
import com.kjipo.eartraining.score.ScoreIntent


class ScoreWebView(context: Context?, attrs: AttributeSet?) : WebView(context, attrs) {

    private var velocityTracker: VelocityTracker? = null


//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        event?.let {
//            (context as ScoreActivity).changeElementTypeSubject.onNext(ScoreIntent.ChangeActiveElementType.OpenMenu)
//        }
//        Log.i("Mouse","Event: $event")
//
//        return true
//    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker?.clear()
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                velocityTracker?.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(1000)

                    Log.i("Mouse", "X velocity: ${getXVelocity(pointerId)}")
                    Log.i("Mouse", "Y velocity: ${getYVelocity(pointerId)}")
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                velocityTracker?.recycle()
                velocityTracker = null
            }

        }

        return true
    }

}