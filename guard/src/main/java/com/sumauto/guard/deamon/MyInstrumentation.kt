package com.sumauto.guard.deamon

import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import com.sumauto.guard.utils.XLog

class MyInstrumentation:Instrumentation() {
    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        XLog.d("MyInstrumentation.onCreate $arguments")
        targetContext.startService(Intent(targetContext, Assist1::class.java))
    }
}