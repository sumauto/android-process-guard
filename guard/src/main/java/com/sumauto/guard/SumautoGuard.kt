package com.sumauto.guard

import android.app.Application
import android.content.Intent
import com.sumauto.guard.deamon.*
import com.sumauto.guard.utils.NativeBridge
import com.sumauto.guard.utils.ServiceHelper
import com.sumauto.guard.utils.SystemUtils
import com.sumauto.guard.utils.XLog
import java.io.File

object SumautoGuard {

    private val mDaemonGuard:DaemonGuard by lazy {
        DaemonGuard()
    }

    @JvmStatic
    fun attachDaemon(application: Application) {
        mDaemonGuard.attach(application)
    }

}