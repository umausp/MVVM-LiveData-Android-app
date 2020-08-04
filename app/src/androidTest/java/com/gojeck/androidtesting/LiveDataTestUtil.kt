package com.gojeck.androidtesting

import com.gojeck.base.GoLiveData
import com.gojeck.base.GoLiveResource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> GoLiveData<T>.await(count: Int = 1, seconds: Long = 2): T {
    val latch = CountDownLatch(count)
    observeForever {
        latch.countDown()
    }
    latch.await(seconds, TimeUnit.SECONDS)

    @Suppress("UNCHECKED_CAST")
    return value as T
}

fun <T> GoLiveResource<T>.awaitData(seconds: Long = 2): T {
    //if already value exists, countdown 2 times

    val oldVersion = version
    val latch = CountDownLatch(1)
    observeForever {
        if (!it.isResult() || version == oldVersion) {
            return@observeForever
        }
        latch.countDown()
    }
    latch.await(seconds, TimeUnit.SECONDS)
    @Suppress("UNCHECKED_CAST")
    return value!!.get() as T
}
