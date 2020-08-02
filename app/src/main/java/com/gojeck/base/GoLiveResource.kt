package com.gojeck.base

import androidx.annotation.MainThread
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.*

typealias GoLiveResource<T> = MediatorLiveData<Resource<T>>
typealias GoLiveState = GoLiveResource<Any?>

fun <T> GoLiveResource<T>.setLoading(job: Job) {
    value = Resource.Loading(job)
}

@MainThread
fun <T> CoroutineScope.loadDataAndState(
    liveData: MediatorLiveData<T>,
    state: GoLiveState?,
    work: suspend CoroutineScope.() -> T
): Job {
    //if error occurs in the async() before call await(), then crash occurs. this prevent the crash. but exception occurs, so, exception will be caught in the getResource()
    return launch(CoroutineExceptionHandler { _, _ -> }, CoroutineStart.LAZY) {
        getResource(work) {
            this@loadDataAndState.loadDataAndState(liveData, state, work)
        }?.also {
            it.onSuccess {
                liveData.postValue(it)
            }
            state?.postValue(it)
        }

    }.also {
        state?.setLoading(it)
        it.start()
    }
}

/**
 * @return if it's cancelled, return null for ignoring
 */
private suspend fun <T> CoroutineScope.getResource(
    action: suspend CoroutineScope.() -> T,
    retry: () -> Unit
): Resource<T>? = try {
    Resource.Success(action(this))
} catch (e: CancellationException) {
    //if cancel. then ignore it
    null
} catch (e: ResourceException) {
    when (val resource = e.resource) {
        is Resource.NetworkError -> {
            Resource.NetworkError(retry)
        }
        else -> resource
    }
}

@MainThread
inline fun <X, Y> MediatorLiveData<X>.map(crossinline transform: (X) -> Y): MediatorLiveData<Y> {
    val result = MediatorLiveData<Y>()
    result.addSource(this) { x -> result.value = transform(x) }
    return result
}

