package com.gojeck.utils

import android.content.Context
import com.gojeck.BuildConfig
import com.gojeck.MyApplication

val isTesting = noThrow { Class.forName("androidx.test.espresso.Espresso") } != null
        || noThrow { Class.forName("org.robolectric.RobolectricTestRunner") } != null

val ctx: Context
    inline get() = app

val app: MyApplication
    inline get() = MyApplication.application

inline fun <T> noThrow(action: () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        null
    }
}
