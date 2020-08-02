package com.gojeck.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gojeck.R

abstract class BaseActivity : AppCompatActivity(), IBaseUi {

    override lateinit var binding: ViewDataBinding

    override val toolbarId: Int
        get() = R.id.toolbar

    override val initStateObserver: Observer<State> by lazy { stateObserverFullPage() }

    override fun findFragmentManager(): FragmentManager = supportFragmentManager

    override val shimmeringLayout: MutableLiveData<Boolean> = MutableLiveData(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupActionbar()
        setToolbarTitle("")
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onViewModelSetup()
    }

    private fun setupView() {
        if (layoutId == 0) {
            return
        }

        binding = DataBindingUtil.setContentView(this, layoutId)

        binding.lifecycleOwner = this
    }

    private fun setupActionbar() {
        val toolbar = findViewById<View>(toolbarId)
        if (toolbar == null || toolbar !is Toolbar) {
            return
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setToolbarTitle(title: String?) {
        supportActionBar?.title = title
    }

    override fun onViewModelSetup() {}

    override fun <T> MediatorLiveData<T>.observe(observer: Observer<in T>) {
        observe(this@BaseActivity, observer)
    }

}