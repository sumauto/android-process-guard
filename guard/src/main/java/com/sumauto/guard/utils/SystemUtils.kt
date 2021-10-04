package com.sumauto.guard.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build


/*
 * Copyright:	
 * Author: 		Lincoln
 * Description:	
 * History:		2021/08/15 
 */
internal object SystemUtils {

    fun isMainProcess(context: Context): Boolean {
        return context.packageName.equals(getProcessName())
    }

    @SuppressLint("ObsoleteSdkInt", "PrivateApi")
    fun getProcessName(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }

        try {
            val methodName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                "currentProcessName"
            } else {
                "currentPackageName"
            }
            return Class.forName("android.app.ActivityThread")
                .getDeclaredMethod(methodName)
                .invoke(null) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}