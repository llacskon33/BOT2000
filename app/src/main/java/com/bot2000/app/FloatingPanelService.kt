package com.bot2000.app

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class FloatingPanelService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WindowManager::class.java)
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_panel, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 32
            y = 160
        }

        windowManager.addView(floatingView, params)

        floatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    val layoutParams = floatingView.layoutParams as WindowManager.LayoutParams
                    layoutParams.x = (event.rawX - event.downTime).toInt()
                    layoutParams.y = (event.rawY - event.downTime).toInt()
                    windowManager.updateViewLayout(floatingView, layoutParams)
                }
            }
            false
        }

        floatingView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            stopSelf()
        }
        floatingView.findViewById<Button>(R.id.toggleButton).setOnClickListener {
            val statusText = floatingView.findViewById<TextView>(R.id.statusText)
            val current = statusText.text.toString()
            statusText.text = if (current.contains("hidden")) "BOT2000 activo" else "BOT2000 oculto"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::floatingView.isInitialized) {
            windowManager.removeViewImmediate(floatingView)
        }
    }
}
