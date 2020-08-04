package com.gojeck.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.savedstate.SavedStateRegistryOwner

interface IBaseUi : SavedStateRegistryOwner, IMenuUi {

    /**
     * viewModel name should be "model" for auto binding
     * if you'd like to change it, override setVariable
     */
    var binding: ViewDataBinding

    /**
     * toolbar id is "toolbar"
     * If you want to change, override this property
     */
    val toolbarId: Int

    val layoutId: Int

    /**
     * when observe LiveData, override this
     */
    fun onViewModelSetup()

    fun <T> GoLiveData<T>.observe(observer: Observer<in T>)

}