package com.sumauto.guard

import android.app.Application
import android.content.Intent
import com.sumauto.guard.deamon.*
import com.sumauto.guard.utils.ServiceHelper
import com.sumauto.guard.utils.SystemUtils
import com.sumauto.guard.utils.XLog
import java.io.File

object SumautoGuard {

    @JvmStatic
    fun attach(application: Application) {
        val processName = SystemUtils.getProcessName()
        XLog.d("call attach on $processName")

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