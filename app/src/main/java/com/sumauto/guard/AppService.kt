package com.sumauto.guard

import android.app.Service
import android.content.Intent
import android.os.*

/**
 * this service will be create when process died
 */
class AppService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

}