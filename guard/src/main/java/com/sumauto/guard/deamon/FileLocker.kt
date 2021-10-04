package com.sumauto.guard.deamon

import com.sumauto.guard.utils.XLog
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel

object FileLocker {
    private var fileChannel: FileChannel? = null

    fun startLock(file: File, info: StartInfo) {

        val randomAccessFile = RandomAccessFile(file, "rw")
        val ch = randomAccessFile.channel
        XLog.d("start lock ${file.path}")
        ch.lock()
        XLog.d("start success")
        randomAccessFile.setLength(0)
        randomAccessFile.writeBytes(info.toString())
        fileChannel = ch
    }

    fun startLockOnGuard(file: File): StartInfo {
        val randomAccessFile = RandomAccessFile(file, "rw")
        val ch = randomAccessFile.channel
        XLog.d("GuardInit lock start $file")
        ch.lock()
        val string = randomAccessFile.readLine()
        val si = StartInfo.create(string)
        XLog.d(si.dump())
        XLog.d("GuardInit locked,means ${si.processName} has died")
        return si
    }
}