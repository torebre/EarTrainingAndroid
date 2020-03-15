package com.kjipo.eartraining

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView

class ScoreWebView(context: Context?, attrs: AttributeSet?) : WebView(context, attrs) {

//    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, com.android.internal.R.attr.webViewStyle)
//
//    fun WebView(context: Context?, attrs: AttributeSet?) {
//        this(context, attrs, com.android.internal.R.attr.webViewStyle)
//    }
//
//    fun WebView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
//        this(context, attrs, defStyleAttr, 0)
//    }
//
//    fun WebView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
//        this(context, attrs, defStyleAttr, defStyleRes, null, false)
//    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("Mouse","Event: $event")

        return true
    }

}