package com.sumauto.guard.deamon

import com.sumauto.guard.utils.XLog


object IBinderManager {
    private var startService = 0
    private var broadcastIntent = 0
    private var startInstrumentation = 0
    private operator fun invoke(str: String, str2: String): Int {
        var result = -1
        try {
            val cls = Class.forName("android.app.IActivityManager\$Stub")
            val declaredField = cls.getDeclaredField(str)
            declaredField.isAccessible = true
            result = declaredField.getInt(cls)
            declaredField.isAccessible = false
        } catch (th: Throwable) {
            try {
                val cls2 = Class.forName("android.app.IActivityManager")
                val declaredField2 = cls2.getDeclaredField(str2)
                declaredField2.isAccessible = true
                result = declaredField2.getInt(cls2)
                declaredField2.isAccessible = false
            } catch (th1: Throwable) {
            }
        }
        XLog.d("!! get transaction[$str] : $result")
        return result
    }

    fun startService(): Int {
        return startService
    }

    fun broadcastIntent(): Int {
        return broadcastIntent
    }

    fun startInstrumentation(): Int {
        return startInstrumentation
    }

    fun thrown(th: Throwable) {
        th.printStackTrace()
    }

    init {
        startService = invoke(
            "TRANSACTION_startService",
            "START_SERVICE_TRANSACTION"
        )
        broadcastIntent = invoke(
            "TRANSACTION_broadcastIntent",
            "BROADCAST_INTENT_TRANSACTION"
        )
        startInstrumentation = invoke(
            "TRANSACTION_startInstrumentation",
            "START_INSTRUMENTATION_TRANSACTION"
        )
    }
}