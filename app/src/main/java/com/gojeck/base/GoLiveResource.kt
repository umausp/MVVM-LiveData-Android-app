package com.gojeck.base

import androidx.annotation.MainThread
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.*

typealias GoLiveResource<T> = MediatorLiveData<Resource<T>>
typealias GoLiveState = GoLiveResource<Any?>

fun <T> GoLiveResource<T>.setSuccess(data: T) {
    value = Resource.Success(data)
}

fun <T> GoLiveResource<T>.postSuccess(data: T) {
    postValue(Resource.Success(data))
}

fun <T> GoLiveResource<T>.setLoading(job: Job) {
    value = Resource.Loading(job)
}

fun <T> GoLiveResource<T>.postError(data: Throwable) {
    postValue(Resource.Error(data))
}

fun <T> GoLiveResource<T>.getData(): T? = value?.get()

@MainThread
fun <T> CoroutineScope.loadResource(
    liveData: GoLiveResource<T>? = null,
    work: suspend CoroutineScope.() -> T,
    onResult: (Resource<T>) -> Resource<T>
): Job {
    //if error occurs in the async() before call await(), then crash occurs. this prevent the crash. but exeption occurs, so, exception will be catched in the getResource()
    return launch(CoroutineExceptionHandler { _, _ -> }, CoroutineStart.LAZY) {
        getResource(work) {
            this@loadResource.loadResource(liveData, work, onResult)
        }?.also {
            liveData?.postValue(onResult(it))
        }
    }.also {
        liveData?.setLoading(it)
        it.start()
    }
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
        is Resource.Error -> {
            Resource.Error(resource.throwable, retry)
        }
        is Resource.Failure -> {
            Resource.Failure(resource.message, retry)
        }
        is Resource.NetworkError -> {
            Resource.NetworkError(retry)
        }
        else -> resource
    }
} catch (e: Exception) {
    Resource.Error(e, retry)
}


