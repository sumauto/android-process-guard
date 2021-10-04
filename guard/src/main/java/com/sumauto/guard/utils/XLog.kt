package com.sumauto.guard.utils

import android.util.Log


/*
 * Copyright:	
 * Author: 		Lincoln
 * Description:	
 * History:		2021/08/15 
 */
internal object XLog {
    private const val TAG = "SumautoGuard"

    fun logComponentAlive(cm:Class<*>){
        d("Alive==>"+cm.simpleName)
    }

    @JvmStatic
    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    @JvmStatic
    fun error(t: Throwable) {
        Log.e(TAG, "error", t)
    }
}