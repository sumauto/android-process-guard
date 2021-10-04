package com.sumauto.guard.deamon

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64

class StartInfo() : Parcelable {

    var processName: String? = ""
    var publicSourceDir: String? = ""
    var nativeLibraryDir: String? = ""
    var serviceIntent: Intent = Intent()
    var insIntent: Intent = Intent()

    constructor(parcel: Parcel) : this() {
        processName = parcel.readString()
        publicSourceDir = parcel.readString()
        nativeLibraryDir = parcel.readString()
        serviceIntent = Intent.CREATOR.createFromParcel(parcel)
        insIntent = Intent.CREATOR.createFromParcel(parcel)
    }


    companion object {


        @JvmField
        val CREATOR = object : Parcelable.Creator<StartInfo> {
            override fun createFromParcel(parcel: Parcel): StartInfo {
                return StartInfo(parcel)
            }

            override fun newArray(size: Int): Array<StartInfo?> {
                return arrayOfNulls(size)
            }
        }

        fun create(str: String): StartInfo {
            val result = Base64.decode(str, 2)
            val obtain = Parcel.obtain()
            obtain.unmarshall(result, 0, result.size)
            obtain.setDataPosition(0)
            return CREATOR.createFromParcel(obtain)
        }
    }

    fun dump(): String {
        return "processName=$processName"
    }

    override fun toString(): String {
        val obtain = Parcel.obtain()
        writeToParcel(obtain, 0)
        val string = Base64.encodeToString(obtain.marshall(), 2)
        obtain.recycle()
        return string
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(processName)
        parcel.writeString(publicSourceDir)
        parcel.writeString(nativeLibraryDir)
        serviceIntent.writeToParcel(parcel, flags)
        insIntent.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }


}