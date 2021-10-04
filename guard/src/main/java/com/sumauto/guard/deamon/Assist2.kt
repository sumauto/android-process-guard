package com.sumauto.guard.deamon

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.sumauto.guard.utils.XLog

class Assist2:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        XLog.d("Assist2 onBind")
        return Binder()
    }

    override fun onCreate() {
        super.onCreate()
        XLog.logComponentAlive(javaClass)
    }
}