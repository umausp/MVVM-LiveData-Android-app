package com.gojeck

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.gojeck.feature.viewModel.RepositoryViewModel
import com.gojeck.utils.LogcatLogger
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val repoViewModel: RepositoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repoViewModel.repositoryLiveData.observe(this, Observer {
            LogcatLogger.d("test", it.toString())
        })
    }
}