package com.kjipo.eartraining

import android.R
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.webkit.WebView
import android.widget.LinearLayout
import android.widget.PopupWindow


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

        event?.let {
//            overlay.add(drawable)

//            val popupView = findViewById<View>(com.kjipo.eartraining.R.id.noteChooser)

            val inflater =  context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(com.kjipo.eartraining.R.layout.note_chooser, null)

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it

            val popupWindow = PopupWindow(popupView, width, height, focusable)

            popupWindow.showAtLocation(this, Gravity.CENTER, 0, 0)

            // dismiss the popup window when touched
            // dismiss the popup window when touched
            popupView.setOnTouchListener(OnTouchListener { v, event ->
                popupWindow.dismiss()
                true
            })



        }
        Log.i("Mouse","Event: $event")




        return true
    }

    private val drawable: ShapeDrawable = run {
        val x = 10
        val y = 10
        val width = 300
        val height = 50
//        contentDescription = context.resources.getString(R.string.my_view_desc)

        ShapeDrawable(OvalShape()).apply {
            // If the color isn't set, the shape uses black as the default.
            paint.color = 0xff74AC23.toInt()
            // If the bounds aren't set, the shape can't be drawn.
            setBounds(x, y, x + width, y + height)
        }
    }




}