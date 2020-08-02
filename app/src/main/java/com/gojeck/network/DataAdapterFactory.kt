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
                ResourceAdapter(trendingRepositoriesModel)
            } else null
        }
        else -> null
    }

    class ResourceAdapter(
        private val type: Type
    ) : CallAdapter<TrendingRepositoriesModel, Call<TrendingRepositoriesModel>> {

        override fun responseType() = type
        override fun adapt(call: Call<TrendingRepositoriesModel>): Call<TrendingRepositoriesModel> = ResourceCall(call)

        class ResourceCall(proxy: Call<TrendingRepositoriesModel>) :
            CallDelegate<TrendingRepositoriesModel, TrendingRepositoriesModel>(proxy) {
            override fun enqueueImpl(callback: Callback<TrendingRepositoriesModel>) {

                proxy.enqueue(object : Callback<TrendingRepositoriesModel> {
                    override fun onResponse(call: Call<TrendingRepositoriesModel>, response: Response<TrendingRepositoriesModel>) {
                        if (response.body() == null) {
                            onFail(callback)
                            return
                        }

                        if (!response.isSuccessful) {
                            onFail(callback)
                            return
                        }

                        val body: TrendingRepositoriesModel? = response.body()
                        if (body == null) {
                            onFail(callback)
                            return
                        }

                        if (body.isEmpty()) {
                            onFail(callback)
                            return
                        }

                        body.let {
                            if (it.isNotEmpty()) {
                                onSuccess(body, callback)
                                return
                            }
                        }

                        onFail(callback)
                    }

                    override fun onFailure(call: Call<TrendingRepositoriesModel>, t: Throwable) {
                        onFail(callback)
                    }
                })
            }

            override fun cloneImpl(): Call<TrendingRepositoriesModel> {
                return ResourceCall(proxy.clone())
            }

            private fun Throwable?.toResult(): Resource<Nothing> = when (this) {
                is IOException -> {
                    log(this)
                    Resource.NetworkError()
                }
                else -> Resource.NetworkError()
            }


            fun onSuccess(response: TrendingRepositoriesModel, callback: Callback<TrendingRepositoriesModel>) {
                callback.onResponse(
                    this@ResourceCall,
                    Response.success(response)
                )
            }

            fun onFail(callback: Callback<TrendingRepositoriesModel>) {
                callback.onFailure(
                    this@ResourceCall,
                    IOException()
                )
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
