package com.gojeck.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import com.gojeck.base.GoLiveResource
import com.gojeck.base.Resource
import com.gojeck.base.asBase
import kotlinx.coroutines.*

/**
 * Consideration
 * 1. use LiveData or suspend function -> LiveData. because db data can be changed.
 * 2. show data while loading and error if data exists on database? -> no. it depends on design. but I didn't see the design like this until now.
 * 3. after api error, if new db data comes? -> ignore db data. we can't be sure that db notify data only when related data is saved.
 */
abstract class NetworkBoundResource<ResultType, RequestType> :
    GoLiveResource<ResultType>(Resource.Loading()) {

    init {
        @Suppress("LeakingThis")
        val dbSource = loadFromDb().asBase()
        //this will be active on observe
        addSource(dbSource) { dbData ->
            removeSource(dbSource)//load from data on init. it's no need more. so remove
            if (shouldFetch(dbData)) {
                fetchFromNetwork()
            } else {
                addSource(dbSource) { newData ->
                    value = Resource.Success(newData)
                }
            }
        }
    }

    private fun fetchFromNetwork() {
//        GlobalScope.loadResource(this) {
        val readScope = CoroutineScope(Dispatchers.Default)
        readScope.launch {
            withContext(Dispatchers.IO) {
                saveCallResult(createCall())
            }
            // we specially request a new live data,
            // otherwise we will get immediately last cached value,
            // which may not be updated with latest results received from network.
            withContext(Dispatchers.Main) {
                addSource(loadFromDb()) { newData ->
                    value = Resource.Success(newData)
                }
            }
            readScope.cancel()
            //if success. save data on db. but not reflect on result
            throw CancellationException()
        }

    }

    /**
     * this return value is LiveData because DB data can be changed.
     */
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): RequestType

    protected abstract suspend fun saveCallResult(item: RequestType)
}
