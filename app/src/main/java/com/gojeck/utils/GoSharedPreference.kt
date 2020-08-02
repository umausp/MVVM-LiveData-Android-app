package com.gojeck.utils

import android.content.Context
import android.content.SharedPreferences
import com.gojeck.BuildConfig


class GoSharedPreference {

    companion object {
        private const val RATE_LIMITER_TIME_KEY = "rate_limiter_time_key"

        fun saveRateLimiterTime(timeStamp: Long) {
            val prefs: SharedPreferences = ctx.getSharedPreferences(
                BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE
            )
            prefs.edit().putLong(RATE_LIMITER_TIME_KEY, timeStamp).apply()
        }

        fun getRateLimiterTime(): Long {
            val prefs: SharedPreferences = ctx.getSharedPreferences(
                BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE
            )
            return prefs.getLong(RATE_LIMITER_TIME_KEY, 0)
        }
    }
}