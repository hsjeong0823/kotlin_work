package com.example.starbuckskotlin.util

import android.util.Log
import com.example.starbuckskotlin.BuildConfig

object LogUtil {
    private val DEBUG_MODE: Boolean = BuildConfig.DEBUG

    @JvmStatic
    fun d(tag: String?, log: String) {
        if (DEBUG_MODE) {
            Log.d(tag, log)
        }
    }

    @JvmStatic
    fun i(tag: String?, log: String) {
        if (DEBUG_MODE) {
            Log.i(tag, log)
        }
    }

    @JvmStatic
    fun e(tag: String?, error: Exception?) {
        if (DEBUG_MODE) {
            Log.e(tag, "error", error)
        }
    }

    @JvmStatic
    fun e(tag: String?, error: String) {
        if (DEBUG_MODE) {
            Log.e(tag, error)
        }
    }
}