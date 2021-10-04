package com.sumauto.guard.deamon

import com.sumauto.guard.utils.XLog
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executors

object AppProcessRunner {


    private val executor= Executors.newSingleThreadExecutor()

    fun startAppProcess(si: StartInfo, niceName: String, lockFile:String) {
        XLog.d("startGuard $niceName")

        val commands = mutableListOf<String>()
        commands += "export CLASSPATH=\$CLASSPATH:${si.publicSourceDir}"
        var appProcess = "app_process"
        val libDir: String
        if (si.nativeLibraryDir!!.contains("arm64")) {
            libDir = "lib64"
            if (File("/system/bin/app_process64").exists()) {
                appProcess = "app_process64"
            }
        } else {
            libDir = "lib"
            if (File("/system/bin/app_process32").exists()) {
                appProcess = "app_process32"
            }
        }
        commands += "export _LD_LIBRARY_PATH=/system/$libDir/:/vendor/$libDir/:${si.nativeLibraryDir}"
        commands += "export LD_LIBRARY_PATH=/system/$libDir/:/vendor/$libDir/:${si.nativeLibraryDir}"
        commands += "%s / %s %s --application --nice-name=%s &".format(
            appProcess,
            GuardInit::class.java.canonicalName,
            lockFile,
            niceName
        )

        commands.forEach {
            XLog.d(it)
        }
        executor.execute {
            execute(File(File.separator), commands.toTypedArray())
        }
    }


    private fun execute(dir: File, commands: Array<String>) {
        try {
            val processBuilder = ProcessBuilder()
            System.getenv("PATH")?.split(":")?.find {
                val f = File(it,"sh")
                if (f.exists()) {
                    processBuilder.command(f.path).redirectErrorStream(true)
                    return@find true
                }
                return@find false
            }

            processBuilder.directory(dir)

            val envMap = processBuilder.environment()
            envMap.putAll(System.getenv())
//            envMap.keys.forEach { key ->
//                XLog.d("env-->$key=${envMap[key]}")
//            }
//            commands.forEach {
//                XLog.d("cmd-->$it")
//            }
            val process = processBuilder.start()
            process.outputStream.use { os ->
                commands.forEach {
                    if (it.endsWith("\n")) {
                        os.write(it.toByteArray())
                    } else {
                        os.write("$it\n".toByteArray())
                    }
                }
                os.write("exit 156\n".toByteArray())
                os.flush()
            }

            process.waitFor()

            val result = process.inputStream.bufferedReader(Charsets.UTF_8).use {
                it.readText()
            }
            XLog.d(result)
        } catch (e: Exception) {
            XLog.error(e)
        }
    }
}