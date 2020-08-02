package com.gojeck.koin

import com.gojeck.feature.viewModel.RepositoryViewModel
import com.gojeck.network.api.TrendingRepoRepositoryImpl
import com.gojeck.room.TrendingRepoDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val KoinModuleRepository = module {
    viewModel { RepositoryViewModel(get()) }

    single { TrendingRepoDatabase.getTrendingDBInstance() }

    single { get<TrendingRepoDatabase>().getTrendingRepoDao() }

    factory { TrendingRepoRepositoryImpl(get(), get()) }
}
