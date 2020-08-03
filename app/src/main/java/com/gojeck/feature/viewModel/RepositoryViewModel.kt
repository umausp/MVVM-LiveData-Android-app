package com.gojeck.feature.viewModel

import com.gojeck.base.BaseViewModel
import com.gojeck.base.GoLiveData
import com.gojeck.base.GoLiveResource
import com.gojeck.base.successDataMap
import com.gojeck.feature.model.TrendingRepositoriesModelItem
import com.gojeck.feature.model.TrendingRepositoryMainViewModel
import com.gojeck.network.api.TrendingRepoRepositoryImpl
import com.gojeck.utils.NetworkUtils
import com.gojeck.utils.ctx

class RepositoryViewModel(private val trendingRepoRepositoryImpl: TrendingRepoRepositoryImpl) :
    BaseViewModel() {

    val shimmerLoadingLiveData = GoLiveData(false)

    val networkErrorLiveData = GoLiveData(false)

    val trendingRepositoryLiveData = GoLiveResource<List<TrendingRepositoryMainViewModel>?>()

    val trendingRepositoryBindLiveData = GoLiveData<List<TrendingRepositoryMainViewModel>>()

    val repositoryItemClickLiveData = GoLiveData<TrendingRepositoryMainViewModel>()

    init {
        getTrendingRepositories(false)
    }

    fun getTrendingRepositories(forRefresh: Boolean) {
        if (NetworkUtils.isNetworkAvailable(ctx)) {
            showNetworkErrorView(false)

            showLoading(true)

            trendingRepositoryLiveData.replaceSource(
                trendingRepoRepositoryImpl.getTrendingLiveData(forRefresh).successDataMap {
                    showLoading(false)
                    convertToTrendingRepositoryMainViewModel(it)
                })
        } else {
            showNetworkErrorView(true)
        }
    }

    private fun convertToTrendingRepositoryMainViewModel(trendingRepositoriesModel: List<TrendingRepositoriesModelItem>): List<TrendingRepositoryMainViewModel> {
        return trendingRepositoriesModel.map {
            TrendingRepositoryMainViewModel(it, repositoryItemClickLiveData)
        }
    }

    private fun showLoading(showLoading: Boolean) {
        shimmerLoadingLiveData.value = showLoading
    }

    private fun showNetworkErrorView(showNetworkError: Boolean) {
        networkErrorLiveData.value = showNetworkError
    }
}
