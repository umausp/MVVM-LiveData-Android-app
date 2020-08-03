package com.gojeck.koin

import com.gojeck.feature.viewModel.RepositoryViewModel
import com.gojeck.network.api.TrendingRepoRepositoryImpl
import com.gojeck.room.TrendingRepoDatabase
import com.gojeck.room.createDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val KoinModuleRepository = module {
    viewModel { RepositoryViewModel(get()) }

    single<TrendingRepoDatabase> { createDatabase() }

    single { get<TrendingRepoDatabase>().getTrendingRepoDao() }

    factory { TrendingRepoRepositoryImpl(get(), get()) }
}
