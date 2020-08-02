package com.gojeck.feature.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.gojeck.base.BaseViewModel
import com.gojeck.base.map
import com.gojeck.feature.model.TrendingRepositoriesModel
import com.gojeck.feature.model.TrendingRepositoryMainViewModel
import com.gojeck.network.api.RepositoryApiService

class RepositoryViewModel(api: RepositoryApiService) : BaseViewModel() {
    private val repositoryLiveData = MediatorLiveData<TrendingRepositoriesModel>()
    val shimmerLoading = MutableLiveData(false)

    val trendingRepositoryLiveData = repositoryLiveData.map {
        convertToTrendingRepositoryMainViewModel(it)
    }

    init {
        repositoryLiveData.loadDataAndState(initState) {
            api.getTrendingRepositories()
        }
    }

    private fun convertToTrendingRepositoryMainViewModel(trendingRepositoriesModel: TrendingRepositoriesModel): List<TrendingRepositoryMainViewModel> {
        return trendingRepositoriesModel.map {
            TrendingRepositoryMainViewModel(it)
        }
    }
}
