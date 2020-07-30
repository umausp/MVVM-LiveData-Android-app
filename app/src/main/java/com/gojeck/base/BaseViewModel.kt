package com.gojeck.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel(), IBaseViewModel, LifecycleObserver {

    override val initState by lazy { GoLiveState() }

    override fun <T> GoLiveResource<T>.load(
        work: suspend CoroutineScope.() -> T,
        onResult: (Resource<T>) -> Resource<T>
    ): Job = viewModelScope.loadResource(this@load, work, onResult)

    override fun <T> MediatorLiveData<T>.loadDataAndState(
        state2: GoLiveState?,
        work: suspend CoroutineScope.() -> T
    ): Job = viewModelScope.loadDataAndState(this@loadDataAndState, state2, work)

}
