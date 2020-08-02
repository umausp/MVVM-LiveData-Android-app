package com.gojeck.network.api

import androidx.lifecycle.LiveData
import com.gojeck.base.GoLiveResource
import com.gojeck.feature.model.TrendingRepositoriesModelItem
import com.gojeck.network.NetworkBoundResource
import com.gojeck.room.TrendingRepoDao
import java.util.concurrent.TimeUnit

interface TrendingRepoRepository {
    fun getTrendingLiveData(forRefresh: Boolean): GoLiveResource<List<TrendingRepositoriesModelItem>>
}

class TrendingRepoRepositoryImpl(
    private val trendingRepoDao: TrendingRepoDao,
    private val trendingRepoApi: RepositoryApiService
) : TrendingRepoRepository {
    private val mRateLimiter = RateLimiter(2, TimeUnit.HOURS)
    override fun getTrendingLiveData(forRefresh: Boolean): GoLiveResource<List<TrendingRepositoriesModelItem>> =
        object :
            NetworkBoundResource<List<TrendingRepositoriesModelItem>, List<TrendingRepositoriesModelItem>>() {
            override fun loadFromDb(): LiveData<List<TrendingRepositoriesModelItem>> {
                return trendingRepoDao.getTrendingRepositories()
            }

            override fun shouldFetch(data: List<TrendingRepositoriesModelItem>?): Boolean {
                return if (forRefresh || data.isNullOrEmpty()) {
                    mRateLimiter.reset()
                    true
                } else {
                    mRateLimiter.shouldFetch()
                }
            }

            override suspend fun createCall(): List<TrendingRepositoriesModelItem> {
                return trendingRepoApi.getTrendingRepositories()
            }

            override suspend fun saveCallResult(item: List<TrendingRepositoriesModelItem>) {
                trendingRepoDao.delete()
                trendingRepoDao.insert(item)
                mRateLimiter.reset()
            }

        }
}
