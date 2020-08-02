package com.gojeck.network.api

import android.os.SystemClock
import com.gojeck.utils.GoSharedPreference
import java.util.concurrent.TimeUnit

/**
 * Utility class that decides whether we should fetch some data or not.
 */
class RateLimiter(timeout: Int, timeUnit: TimeUnit) {
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFetch(): Boolean {
        val lastFetched = GoSharedPreference.getRateLimiterTime()
        val now = now()
        if (lastFetched <= 0) {
            GoSharedPreference.saveRateLimiterTime(now)
            return true
        }
        if (now - lastFetched > timeout) {
            GoSharedPreference.saveRateLimiterTime(now)
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()

    @Synchronized
    fun reset() {
        GoSharedPreference.saveRateLimiterTime(now())
    }
}
