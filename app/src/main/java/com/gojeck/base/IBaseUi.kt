package com.gojeck.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.savedstate.SavedStateRegistryOwner

interface IBaseUi : SavedStateRegistryOwner {

    /**
     * viewModel name should be "model" for auto binding
     * if you'd like to change it, override setVariable
     */
    var binding: ViewDataBinding

    val toolbarId: Int

    val layoutId: Int

    /**
     * when observe LiveData, override this
     */
    fun onViewModelSetup()

    /**
     * hide layout by showing full error fragment if not success,
     */
    val initStateObserver: Observer<State>

    val shimmeringLayout: MutableLiveData<Boolean>

    val networkErrorLayoutLiveData: MutableLiveData<Boolean>

    fun <T> GoLiveData<T>.observe(observer: Observer<in T>)

    fun findFragmentManager(): FragmentManager

    val classPackageName: String
}