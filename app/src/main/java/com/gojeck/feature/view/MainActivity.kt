package com.gojeck.feature.view

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.gojeck.R
import com.gojeck.base.BaseActivity
import com.gojeck.databinding.ActivityMainBinding
import com.gojeck.feature.model.TrendingRepositoryMainViewModel
import com.gojeck.feature.viewModel.RepositoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    private val repoViewModel: RepositoryViewModel by viewModel()

    override val layoutId: Int = R.layout.activity_main

    init {
        setMenu(R.menu.menu_sort_home) { item: MenuItem ->
            if (item.itemId == R.id.menu_sort_by_starts) {
                sortByStars()
            } else if (item.itemId == R.id.menu_sort_by_name) {
                sortByName()
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (binding as ActivityMainBinding).apply {
            model = repoViewModel
        }
    }

    override fun onViewModelSetup() {

        observeTrendingRepositories(binding as ActivityMainBinding)

        (binding as ActivityMainBinding).swipeContainer.setOnRefreshListener(this)

        repoViewModel.repositoryItemClickLiveData.observeEvent(this, Observer {
            repoViewModel.trendingRepositoryBindLiveData.value?.forEach { item ->
                if (item.name != it.name && it.itemClickedLiveData.get().not()) {
                    item.itemClickedLiveData.set(false)
                }
            }

            it.itemClickedLiveData.set(!it.itemClickedLiveData.get())
        })
    }

    private fun observeTrendingRepositories(activityMainBinding: ActivityMainBinding) {
        repoViewModel.trendingRepositoryLiveData.observe(Observer {
            if (it.isSuccess()) {
                it?.get()?.let { data ->
                    bindData(data)
                    activityMainBinding.swipeContainer.isRefreshing = false
                }
            }
        })
    }

    private fun bindData(data: List<TrendingRepositoryMainViewModel>) {
        repoViewModel.trendingRepositoryBindLiveData.value = data
    }

    override fun onRefresh() {
        repoViewModel.getTrendingRepositories(true)
    }

    private fun sortByName() {
        repoViewModel.trendingRepositoryBindLiveData.value?.sortedWith((compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }))
            .also {
                it?.let { it1 -> bindData(it1) }
            }
    }

    private fun sortByStars() {
        repoViewModel.trendingRepositoryBindLiveData.value?.sortedByDescending {
            it.stars
        }.also {
            it?.let { it1 -> bindData(it1) }
        }
    }
}
