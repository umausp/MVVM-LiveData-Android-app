package com.gojeck.koin

import com.gojeck.BuildConfig
import com.gojeck.network.DataAdapterFactory
import com.gojeck.network.api.RepositoryApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val koinModuleNetwork = module {
    factory {
        provideRetrofit(BuildConfig.BASE_URI, createOkHttpClient()).create(
            RepositoryApiService::class.java
        )
    }
}

fun provideRetrofit(url: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(DataAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}
