package com.gojeck.feature.viewModel

import androidx.lifecycle.MediatorLiveData
import com.gojeck.base.BaseViewModel
import com.gojeck.feature.model.TrendingRepositoriesModel
import com.gojeck.network.api.RepositoryApiService

class RepositoryViewModel(api : RepositoryApiService) : BaseViewModel() {
    val repositoryLiveData = MediatorLiveData<TrendingRepositoriesModel>()

    init {
        repositoryLiveData.loadDataAndState(initState) {
            api.getTrendingRepositories()
        }
    }
}
