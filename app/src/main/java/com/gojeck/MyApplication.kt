package com.gojeck

import androidx.multidex.MultiDexApplication
import com.gojeck.koin.KoinModuleRepository
import com.gojeck.koin.koinModuleNetwork
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.EmptyLogger

class MyApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        terminateKoin()
    }


    fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            } else {
                EmptyLogger()
            }

            androidContext(this@MyApplication)
            modules(koinModuleNetwork + KoinModuleRepository)
        }
    }

    fun terminateKoin() {
        stopKoin()
    }
}