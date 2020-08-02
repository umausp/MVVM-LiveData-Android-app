package com.gojeck.base

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import kotlinx.coroutines.*

typealias GoLiveResource<T> = GoLiveData<Resource<T>>
typealias GoLiveState = GoLiveResource<Any?>

fun <T> GoLiveResource<T>.setLoading(job: Job) {
    value = Resource.Loading(job)
}

fun <T> GoLiveResource<T>.setSuccess(data: T) {
    value = Resource.Success(data)
}

fun <T> GoLiveResource<T>.postSuccess(data: T) {
    postValue(Resource.Success(data))
}

fun <T> GoLiveResource<T>.getData(): T? = value?.get()


@MainThread
fun <T> CoroutineScope.loadDataAndState(
    liveData: GoLiveData<T>,
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
fun <T> CoroutineScope.loadResource(
    liveData: GoLiveResource<T>? = null,
    work: suspend CoroutineScope.() -> T
): Job {
//if error occurs in the async() before call await(), then crash occurs. this prevent the crash. but exeption occurs, so, exception will be catched in the getResource()
    return launch(CoroutineExceptionHandler { _, _ -> }, CoroutineStart.LAZY) {
        getResource(work) {
            this@loadResource.loadResource(liveData, work)
        }?.also {
            liveData?.postValue(it)
        }

    }.also {
        liveData?.setLoading(it)
        it.start()
    }
}

fun <X, Y> GoLiveResource<X>.successDataMap(@NonNull func: (X) -> Y): GoLiveResource<Y> = map {
    @Suppress("UNCHECKED_CAST")
    when (it) {
        is Resource.Success -> try {
            Resource.Success(func(it.data))
        } catch (e: Exception) {
            Resource.NetworkError()
        }
        else -> it as Resource<Y>
    }
}


@MainThread
fun <T> CoroutineScope.loadResource(
    liveData: GoLiveResource<T>? = null,
    state: GoLiveState? = null,
    work: suspend CoroutineScope.() -> T
): Job {
    //if error occurs in the async() before call await(), then crash occurs. this prevent the crash. but exeption occurs, so, exception will be catched in the getResource()
    return launch(CoroutineExceptionHandler { _, _ -> }, CoroutineStart.LAZY) {
        getResource(work) {
            this@loadResource.loadResource(liveData, state, work)
        }?.also {
            liveData?.postValue(it)
            state?.postValue(it)
        }

    }.also {
        liveData?.setLoading(it)
        state?.setLoading(it)
        it.start()
    }
}

