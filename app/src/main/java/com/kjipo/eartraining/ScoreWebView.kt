package com.kjipo.eartraining

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView
import com.kjipo.eartraining.score.ScoreActivity


class ScoreWebView(context: Context?, attrs: AttributeSet?) : WebView(context, attrs) {
    private var xStart: Int = 0
    private var yStart: Int = 0


//    private var velocityTracker: VelocityTracker? = null


    companion object {
        const val X_DIFF = 30
        const val Y_DIFF = 30
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
//                velocityTracker?.clear()
//                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
//                velocityTracker?.addMovement(event)

                xStart = event.x.toInt()
                yStart = event.y.toInt()

                Log.i("ScoreWebView", "x: $xStart, y: $yStart")
            }

            MotionEvent.ACTION_MOVE -> {
//                velocityTracker?.apply {
//                    val pointerId: Int = event.getPointerId(event.actionIndex)
//                    addMovement(event)
//                    computeCurrentVelocity(1000)
//
//                    Log.i("Mouse", "X velocity: ${getXVelocity(pointerId)}")
//                    Log.i("Mouse", "Y velocity: ${getYVelocity(pointerId)}")
//                }

                val xDelta = event.x.toInt() - xStart
                val yDelta = event.y.toInt() - yStart

                Log.i("ScoreWebView", "x delta: $xDelta, y delta: $yDelta")

                if (xDelta > X_DIFF) {
                    (context as ScoreActivity).moveSelection(false)
                    xStart = event.x.toInt()
                } else if (xDelta < -X_DIFF) {
                    (context as ScoreActivity).moveSelection(true)
                    xStart = event.x.toInt()
                }

                if (yDelta > Y_DIFF) {
                    (context as ScoreActivity).moveNote(false)
                    yStart = event.y.toInt()
                } else if (yDelta < -Y_DIFF) {
                    (context as ScoreActivity).moveNote(true)
                    yStart = event.y.toInt()
                }
            }

            MotionEvent.ACTION_UP -> {

//                velocityTracker?.recycle()
//                velocityTracker = null
            }

            MotionEvent.ACTION_CANCEL -> {

//                velocityTracker?.recycle()
//                velocityTracker = null
            }

        }

        return true
    }

}