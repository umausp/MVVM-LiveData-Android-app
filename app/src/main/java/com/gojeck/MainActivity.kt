package com.gojeck

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.gojeck.base.BaseActivity
import com.gojeck.databinding.ActivityMainBinding
import com.gojeck.feature.utils.bindData
import com.gojeck.feature.viewModel.RepositoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {
    val repoViewModel: RepositoryViewModel by viewModel()

    override val layoutId: Int = R.layout.activity_main
    override val classPackageName: String = BuildConfig.APPLICATION_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (binding as ActivityMainBinding).shimmerLayout.shimmerViewContainer.startShimmer()
    }

    override fun onViewModelSetup() {
        repoViewModel.initState.observe(initStateObserver)
        repoViewModel.trendingRepositoryLiveData.observe(Observer {
            (binding as ActivityMainBinding).rvActivityMainTrendingRepository.bindData(
                it,
                R.layout.trending_repositories_layout,
                this
            )

        })

        shimmeringLayout.observe(this, Observer {
            if (it) {
                (binding as ActivityMainBinding).shimmerLayout.shimmerViewContainer.visibility =
                    View.VISIBLE
            } else {
                (binding as ActivityMainBinding).shimmerLayout.shimmerViewContainer.visibility =
                    View.GONE
            }
        })
    }
}
