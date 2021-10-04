package com.sumauto.guard.utils

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

object ServiceHelper {
    fun bind(context: Context, serviceClass: Class<out Service>) {
        context.bindService(Intent(context, serviceClass), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                XLog.d("onServiceConnected: $name")

            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }, Context.BIND_AUTO_CREATE or Context.BIND_NOT_FOREGROUND)
    }
}