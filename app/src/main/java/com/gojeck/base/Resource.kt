package com.gojeck.base

import com.gojeck.utils.log
import kotlinx.coroutines.Job
import java.io.IOException

sealed class Resource<out T> {
    data class Loading(val job: Job? = null) : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure(val message: String?, val retry: () -> Unit = {}) :
        Resource<Nothing>()

    data class NetworkError(val retry: () -> Unit = {}) : Resource<Nothing>()

    data class Error(val throwable: Throwable = RuntimeException("empty error"), val retry: () -> Unit = {}) : Resource<Nothing>() {
        init {
            log(throwable)
        }
    }

    fun get(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun onLoading(onResult: (job: Job?) -> Unit): Resource<T> {
        if (this is Loading) {
            onResult(job)
        }
        return this
    }

    inline fun onSuccess(onResult: (T) -> Unit) {
        if (this is Success) {
            onResult(data)
        }
    }

    inline fun onFailure(onResult: (Failure) -> Unit) {
        if (this is Failure) {
            onResult(this)
        }
    }

    inline fun onError(onResult: (Error) -> Unit) {
        if (this is Error) {
            onResult(this)
        }
    }

    inline fun onNetworkError(onResult: (retry: () -> Unit) -> Unit) {
        if (this is NetworkError) {
            onResult(retry)
        }
    }

    fun isSuccess() = this is Success
    fun isFailure() = this is Failure
    fun isLoading() = this is Loading
    fun isResult() = !isLoading()
    fun isError() = this is Error
    fun isNetworkError() = this is NetworkError
}

class ResourceException(val resource: Resource<Nothing>) : IOException()

typealias State = Resource<Any?>
