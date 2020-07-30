package com.gojeck.koin

import com.gojeck.feature.viewModel.RepositoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val KoinModuleRepository = module {
    viewModel { RepositoryViewModel(get()) }
}
