package com.gojeck.network.api

import com.gojeck.feature.model.TrendingRepositoriesModel
import retrofit2.http.GET

interface RepositoryApiService {
    @GET("repositories")
    suspend fun getTrendingRepositories(): TrendingRepositoriesModel
}
