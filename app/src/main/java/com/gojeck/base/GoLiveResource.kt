package com.gojeck.base

import androidx.annotation.NonNull
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

typealias GoLiveResource<T> = GoLiveData<Resource<T>>
typealias GoLiveState = GoLiveResource<Any?>

fun <T> GoLiveResource<T>.setLoading(job: Job) {
    value = Resource.Loading(job)
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

