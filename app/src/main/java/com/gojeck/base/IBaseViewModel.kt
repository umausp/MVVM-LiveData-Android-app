package com.gojeck.base

import androidx.annotation.MainThread
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface IBaseViewModel {

    /**
     * hide if it's not success
     * normally used for page init.
     * but if need to hide page. you can use this
     */
    val initState: GoLiveState

    @MainThread
    fun <T> GoLiveResource<T>.load(
        work: suspend CoroutineScope.() -> T,
        onResult: (Resource<T>) -> Resource<T>
    ): Job

    @MainThread
    fun <T> MediatorLiveData<T>.loadDataAndState(state2: GoLiveState?, work: suspend CoroutineScope.() -> T): Job
}
