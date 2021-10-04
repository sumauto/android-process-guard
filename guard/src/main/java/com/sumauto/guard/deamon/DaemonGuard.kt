package com.sumauto.guard.deamon

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Process
import com.sumauto.guard.utils.NativeBridge
import com.sumauto.guard.utils.ServiceHelper
import com.sumauto.guard.utils.SystemUtils
import com.sumauto.guard.utils.XLog
import java.io.File

internal class DaemonGuard {
    fun attach(application: Application, serviceClass: Class<out Service>? = null) {

        val processName = SystemUtils.getProcessName()
        if (SystemUtils.isMainProcess(application)) {
            ServiceHelper.bind(application, ResidentService::class.java)
            return
        }

        if (processName?.contains("sumauto_daemon") == true) {
            val lockFile =
                File(application.filesDir, processName.replace("${application.packageName}:", ""))

            val appInfo = application.applicationInfo
            val si = StartInfo()
            si.nativeLibraryDir = appInfo.nativeLibraryDir
            si.publicSourceDir = appInfo.publicSourceDir
            si.processName = SystemUtils.getProcessName()
            si.serviceIntent = Intent(application, DaemonService::class.java)
            si.insIntent = Intent(application, MyInstrumentation::class.java)
            //application.startInstrumentation(si.insIntent.component!!,null,null)
            FileLocker.startLock(lockFile, si)


            AppProcessRunner.startAppProcess(si, "daemon_s", lockFile.absolutePath)
        }

    }
}