package com.gojeck

import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gojeck.base.BaseActivity
import com.gojeck.databinding.ActivityMainBinding
import com.gojeck.feature.utils.bindData
import com.gojeck.feature.viewModel.RepositoryViewModel
import com.gojeck.utils.LogcatLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val repoViewModel: RepositoryViewModel by viewModel()

    override val layoutId: Int = R.layout.activity_main

    override val classPackageName: String = BuildConfig.APPLICATION_ID

    override fun onViewModelSetup() {
        repoViewModel.initState.observe(initStateObserver)

        val activityMainBinding = binding as ActivityMainBinding

        observeTrendingRepositories(activityMainBinding)

        observeShimmerLoading(activityMainBinding)

        observeNetworkError(activityMainBinding)

        activityMainBinding.swipeContainer.setOnRefreshListener(this)
    }

    private fun observeTrendingRepositories(activityMainBinding: ActivityMainBinding) {
        repoViewModel.trendingRepositoryLiveData.observe(Observer {
            if (it.isSuccess()) {
                it?.get()?.let { data ->
                    activityMainBinding.rvActivityMainTrendingRepository.bindData(
                        data,
                        R.layout.trending_repositories_layout,
                        this
                    )
                    activityMainBinding.swipeContainer.isRefreshing = false
                }
            }
        })
    }

    private fun observeShimmerLoading(activityMainBinding: ActivityMainBinding) {
        repoViewModel.shimmerLoadingLiveData.observe(this, Observer {
            if (it) {
                activityMainBinding.rvActivityMainTrendingRepository.visibility =
                    View.GONE

                activityMainBinding.shimmerLayout.shimmerViewContainer.visibility =
                    View.VISIBLE

                activityMainBinding.shimmerLayout.shimmerViewContainer.startShimmer()
            } else {
                activityMainBinding.shimmerLayout.shimmerViewContainer.visibility =
                    View.GONE

                activityMainBinding.rvActivityMainTrendingRepository.visibility =
                    View.VISIBLE
            }
        })
    }

    private fun observeNetworkError(activityMainBinding: ActivityMainBinding) {
        repoViewModel.networkErrorLiveData.observe(this, Observer {
            if (it) {
                activityMainBinding.networkErrorLayout.lytNetworkErrorMainContainer.visibility =
                    View.VISIBLE
                activityMainBinding.networkErrorLayout.btnNetworkErrorRetry.setOnClickListener {
                    repoViewModel.getTrendingRepositories(false)
                }
            } else {
                activityMainBinding.networkErrorLayout.lytNetworkErrorMainContainer.visibility =
                    View.GONE
            }
        })
    }

    override fun onRefresh() {
        repoViewModel.getTrendingRepositories(true)
    }
}
