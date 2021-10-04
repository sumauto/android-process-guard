package com.sumauto.guard.deamon

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.sumauto.guard.utils.ServiceHelper
import com.sumauto.guard.utils.XLog

class DaemonService:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        XLog.logComponentAlive(javaClass)
        ServiceHelper.bind(this, ResidentService::class.java)
        ServiceHelper.bind(this, Assist1::class.java)
        ServiceHelper.bind(this, Assist2::class.java)
    }
}