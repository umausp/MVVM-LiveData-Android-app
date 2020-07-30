package com.gojeck.utils

import com.gojeck.BuildConfig
import com.google.gson.Gson

@Suppress("NOTHING_TO_INLINE")
inline fun Any.log(e: Throwable) {
    if (BuildConfig.DEBUG) {
        LogcatLogger.e(javaClass.simpleName, e)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Any.log(vararg obj: Any?) {
    if (BuildConfig.DEBUG) {
        LogcatLogger.d(javaClass.simpleName, Gson().toJson(obj))
    }
}
