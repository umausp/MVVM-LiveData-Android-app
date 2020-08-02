package com.gojeck.feature.model

import androidx.lifecycle.MutableLiveData

class TrendingRepositoryMainViewModel(trendingRepositoriesModelItem: TrendingRepositoriesModelItem) {

    val author = trendingRepositoriesModelItem.author
    val avatar = trendingRepositoriesModelItem.avatar
    val currentPeriodStars = trendingRepositoriesModelItem.currentPeriodStars
    val description = trendingRepositoriesModelItem.description
    val forks = trendingRepositoriesModelItem.forks
    val language = trendingRepositoriesModelItem.language
    val languageColor = trendingRepositoriesModelItem.languageColor
    val name = trendingRepositoriesModelItem.name
    val stars = trendingRepositoriesModelItem.stars
    val url = trendingRepositoriesModelItem.url

    val itemClickedLiveData = MutableLiveData(false)

    fun itemOnClick() {
        itemClickedLiveData.value = itemClickedLiveData.value?.not()
    }
}
