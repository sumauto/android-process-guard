package com.sumauto.guard.deamon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Parcel
import android.os.Process
import android.system.Os
import com.sumauto.guard.utils.ReflectHelper
import com.sumauto.guard.utils.XLog
import java.io.File

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
class GuardInit(var path: String) {
    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            XLog.d("GuardInit process start:${args[0]}")

            try {
                XLog.d(">>>> invoke exemptAll()")
                ReflectHelper.exemptAll()
                XLog.d(">>>> invoke setArgV0(): niceName=")
                Process::class.java.getMethod(
                    "setArgV0", *arrayOf<Class<*>>(
                        String::class.java
                    )
                ).invoke(
                    null,
                    *arrayOf("sum-n")
                )
            } catch (th: Throwable) {
                XLog.error(th)
            }
            Os.setsid()
            GuardInit(args[0]).start()
            XLog.d("GuardInit process end")
        }
    }

    private var iActivityManager: IBinder? = null
    private var serviceData: Parcel? = null

    init {
        try {
            iActivityManager = Class.forName("android.app.ActivityManagerNative")
                .getMethod("getDefault").apply { isAccessible = true }.invoke(null) as IBinder
        } catch (e: Exception) {
        }

        if (iActivityManager == null) {
            try {
                iActivityManager = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String::class.java)
                    .invoke(null, Context.ACTIVITY_SERVICE) as IBinder
            } catch (e: Exception) {
                XLog.error(e)
            }
        }
        XLog.d("iActivityManager= $iActivityManager")

    }

    fun start() {
        try {
            val si = FileLocker.startLockOnGuard(File(path))
            assembleServiceParcel(si.serviceIntent)
            val insData = assembleInstrumentationParcel(si.insIntent)
            startService()
            startInstrumentation(insData)
        } catch (e: Exception) {
            XLog.error(e)
            XLog.d("GuardInit lock error")
        }
    }

    private fun assembleInstrumentationParcel(intent: Intent): Parcel {
        XLog.d("@_@")
        val instrumentationData = Parcel.obtain()
        instrumentationData.writeInterfaceToken("android.app.IActivityManager")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instrumentationData.writeInt(1)
        }
        intent.component!!.writeToParcel(instrumentationData, 0)
        instrumentationData.writeString(null)
        instrumentationData.writeInt(0)
        instrumentationData.writeInt(0)
        instrumentationData.writeStrongBinder(null)
        instrumentationData.writeStrongBinder(null)
        instrumentationData.writeInt(0)
        instrumentationData.writeString(null)
        return instrumentationData
    }

    /**
     * public ComponentName startService(IApplicationThread caller, Intent service,
     * String resolvedType, String callingPackage, int userId) throws RemoteException {
     * Parcel data = Parcel.obtain();
     * Parcel reply = Parcel.obtain();
     * data.writeInterfaceToken(IActivityManager.descriptor);
     * data.writeStrongBinder(caller != null ? caller.asBinder() : null);
     * service.writeToParcel(data, 0);
     * data.writeString(resolvedType);
     * data.writeString(callingPackage);
     * data.writeInt(userId);
     * mRemote.transact(START_SERVICE_TRANSACTION, data, reply, 0);
     * reply.readException();
     * ComponentName res = ComponentName.readFromParcel(reply);
     * data.recycle();
     * reply.recycle();
     * return res;
     * }
     */
    private fun assembleServiceParcel(intent: Intent?) {
        XLog.d("assembleServiceParcel $intent")
        if (intent == null) return
        val serviceData = Parcel.obtain()
        serviceData.writeInterfaceToken("android.app.IActivityManager")
        serviceData.writeStrongBinder(null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            serviceData.writeInt(1)
        }
        intent.writeToParcel(serviceData, 0)
        serviceData.writeString(null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            serviceData.writeInt(0) // 0 : WTF!!!
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            serviceData.writeString(intent.component!!.packageName)
        }
        serviceData.writeInt(0)
        this.serviceData = serviceData
    }

    private fun startInstrumentation(data: Parcel) {
        try {
            XLog.d("startInstrumentation $data")
            val reply = Parcel.obtain()
            iActivityManager!!.transact(IBinderManager.startInstrumentation(), data, reply, 1)
            val exception = reply.readException()
            val result = reply.readInt()
            XLog.d("result:$result $exception")

        } catch (th: Throwable) {
            XLog.error(th)
        }
    }


    private fun startService() {
        XLog.d("startService $serviceData")
        if (serviceData != null) {
            try {
                XLog.d("startService transact")
                iActivityManager!!.transact(IBinderManager.startService(), serviceData!!, null, 1)
            } catch (th: Throwable) {
                XLog.error(th)
            }
        }
    }

}