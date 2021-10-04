package com.sumauto.guard.utils

import android.util.Log


/*
 * Copyright:	
 * Author: 		Lincoln
 * Description:	
 * History:		2021/08/15 
 */
internal object XLog {
    const val TAG = "ProcessKeeper"

    @JvmStatic
    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    @JvmStatic
    fun error(t: Throwable) {
        Log.e(TAG, "error", t)
    }
}