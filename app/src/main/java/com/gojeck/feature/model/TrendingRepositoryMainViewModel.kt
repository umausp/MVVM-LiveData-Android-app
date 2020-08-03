package com.gojeck.feature.model

import androidx.databinding.ObservableBoolean
import com.gojeck.base.GoLiveData
import com.gojeck.base.call

class TrendingRepositoryMainViewModel(
    trendingRepositoriesModelItem: TrendingRepositoriesModelItem,
    private val repositoryItemClickLiveData: GoLiveData<TrendingRepositoryMainViewModel>
) {

    val author = trendingRepositoriesModelItem.author
    val avatar = trendingRepositoriesModelItem.avatar
    val description = trendingRepositoriesModelItem.description
    val forks = trendingRepositoriesModelItem.forks
    val language = trendingRepositoriesModelItem.language
    val languageColor = trendingRepositoriesModelItem.languageColor
    val name = trendingRepositoriesModelItem.name
    val stars = trendingRepositoriesModelItem.stars

    val itemClickedLiveData = ObservableBoolean(false)

    fun itemOnClick() {
        repositoryItemClickLiveData.call(this)
    }
}
