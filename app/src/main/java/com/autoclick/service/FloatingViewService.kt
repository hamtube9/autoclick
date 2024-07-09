package com.autoclick.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.LinearLayout
import com.autoclick.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FloatingViewService : Service() {

    private var windowManager: WindowManager? = null
    private var windowManagerParams: WindowManager.LayoutParams? = null
    private lateinit var floatingWidget: View
    private lateinit var btnStart: Button
    private lateinit var llDrag: LinearLayout
    private lateinit var btnStop: Button
    private var isStart: Boolean = false

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflaterView(inflater)
        return START_STICKY

    }

    private fun inflaterView(layoutInflater: LayoutInflater) {
        floatingWidget = layoutInflater.inflate(R.layout.floating_view_widget, null)
        windowManagerParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        windowManagerParams!!.gravity = Gravity.TOP or Gravity.START
        windowManagerParams!!.x = 0
        windowManagerParams!!.y = 100
        windowManager!!.addView(floatingWidget, windowManagerParams)
        btnStart = floatingWidget.findViewById(R.id.btnStartService)
        llDrag = floatingWidget.findViewById(R.id.llDrag)
        btnStart.text = "START"
        val accessibilityService = AppAccessibilityService()

        llDrag.setOnTouchListener { _, motionEvent ->
            windowManagerParams!!.x = motionEvent!!.rawX as Int - floatingWidget.measuredWidth / 2

            windowManagerParams!!.y = motionEvent.rawY as Int - floatingWidget.measuredHeight - 25

            windowManager!!.updateViewLayout(floatingWidget, windowManagerParams)
            false
        }

        btnStart.setOnClickListener {
            if(isStart){
                isStart = false
                btnStart.text = "START"
                GlobalScope.launch(Dispatchers.IO) {
                    accessibilityService.stopSpam()
                    val intent = Intent()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.action = "com.auto.click.mainscreen"
                    startActivity(intent)
                    windowManager?.removeView(floatingWidget)
                }
            }else{
                isStart = true
                btnStart.text = "STOP"
                GlobalScope.launch(Dispatchers.IO) {
                    accessibilityService.autoClick()
                }
            }

        }


    }
}