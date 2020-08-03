package com.gojeck

import androidx.multidex.MultiDexApplication
import com.gojeck.koin.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.EmptyLogger

open class MyApplication : MultiDexApplication() {

    companion object {
        @JvmStatic
        lateinit var application: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            } else {
                EmptyLogger()
            }

            androidContext(this@MyApplication)
            modules(provideDependency())
        }
    }

    private fun terminateKoin() {
        stopKoin()
    }

    override fun onTerminate() {
        super.onTerminate()
        terminateKoin()
    }

    open fun provideDependency() = appComponent
}