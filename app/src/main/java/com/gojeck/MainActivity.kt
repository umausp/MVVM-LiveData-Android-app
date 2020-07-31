package com.gojeck

import androidx.lifecycle.Observer
import com.gojeck.base.BaseActivity
import com.gojeck.feature.viewModel.RepositoryViewModel
import com.gojeck.utils.LogcatLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    val repoViewModel: RepositoryViewModel by viewModel()

    override val layoutId: Int = R.layout.activity_main
    override val classPackageName: String = BuildConfig.APPLICATION_ID

    override fun onViewModelSetup() {
        repoViewModel.initState.observe(initStateObserver)
        repoViewModel.repositoryLiveData.observe(Observer {
            LogcatLogger.d("test", it.toString())
        })
    }
}
