package com.gojeck.network

import com.gojeck.base.Resource
import com.gojeck.base.ResourceException
import com.gojeck.feature.model.TrendingRepositoriesModel
import com.gojeck.utils.log
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class DataAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            if (TrendingRepositoriesModel::class.java.isAssignableFrom(getRawType(callType))) {
                val trendingRepositoriesModel = object : ParameterizedType {
                    override fun getRawType(): Type {
                        return TrendingRepositoriesModel::class.java
                    }

                    override fun getOwnerType(): Type? {
                        return null
                    }

                    override fun getActualTypeArguments(): Array<Type> {
                        return arrayOf(callType)
                    }
                }
                ResourceAdapter<TrendingRepositoriesModel>(trendingRepositoriesModel)
            } else null
        }
        else -> null
    }

    class ResourceAdapter<U : TrendingRepositoriesModel>(
        private val type: Type
    ) : CallAdapter<U, Call<U>> {

        override fun responseType() = type
        override fun adapt(call: Call<U>): Call<U> = ResourceCall(call)

        class ResourceCall<U : TrendingRepositoriesModel>(proxy: Call<U>) :
            CallDelegate<U, U>(proxy) {
            override fun enqueueImpl(callback: Callback<U>) {

                proxy.enqueue(object : Callback<U> {
                    override fun onResponse(call: Call<U>, response: Response<U>) {
                        if (response.body() == null) {
                            onFail(null, callback)
                            return
                        }

                        if (!response.isSuccessful) {
                            onFail(null, callback)
                            return
                        }

                        val body: U? = response.body()
                        if (body == null) {
                            onFail(null, callback)
                            return
                        }

                        if (body.isEmpty()) {
                            onFail(body, callback)
                            return
                        }

                        body.let {
                            if (it.isNotEmpty()) {
                                onSuccess(body, callback)
                                return
                            }
                        }

                        onFail(body, callback)
                    }

                    override fun onFailure(call: Call<U>, t: Throwable) {
                        onError(t, callback)
                    }
                })
            }

            override fun cloneImpl(): Call<U> {
                return ResourceCall(proxy.clone())
            }

            private fun Throwable?.toResult(): Resource<Nothing> = when (this) {
                is IOException -> {
                    log(this)
                    Resource.NetworkError()
                }
                else -> Resource.Error(this ?: RuntimeException("unknown throwable"))
            }


            fun onSuccess(response: U, callback: Callback<U>) {
                callback.onResponse(
                    this@ResourceCall,
                    Response.success(response)
                )
            }

            fun onFail(response: U?, callback: Callback<U>) {
                callback.onFailure(
                    this@ResourceCall,
                    ResourceException(
                        Resource.Failure(
                            "Please try again."
                        )
                    )
                )
            }

            fun onError(throwable: Throwable?, callback: Callback<U>) {
                if (throwable is ResourceException) {
                    callback.onFailure(this@ResourceCall, throwable)
                } else {
                    callback.onFailure(this@ResourceCall, ResourceException(throwable.toResult()))
                }
            }

            override fun timeout(): Timeout {
                return Timeout()
            }
        }

        abstract class CallDelegate<TIn, TOut>(
            protected val proxy: Call<TIn>
        ) : Call<TOut> {
            final override fun execute(): Response<TOut> = throw NotImplementedError()

            final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
            final override fun clone(): Call<TOut> = cloneImpl()

            override fun cancel() = proxy.cancel()
            override fun request(): Request = proxy.request()
            override fun isExecuted() = proxy.isExecuted
            override fun isCanceled() = proxy.isCanceled

            abstract fun enqueueImpl(callback: Callback<TOut>)
            abstract fun cloneImpl(): Call<TOut>
        }
    }
}
