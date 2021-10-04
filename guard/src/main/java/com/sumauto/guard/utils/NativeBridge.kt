package com.sumauto.guard.utils

object NativeBridge {
    init {
        System.loadLibrary("guard")
    }
    external fun hello()
}