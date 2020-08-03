package com.gojeck.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gojeck.R

abstract class BaseActivity : AppCompatActivity(), IBaseUi {

    override lateinit var binding: ViewDataBinding

    override val toolbarId: Int
        get() = R.id.toolbar


    override var menuId = 0
    override var onMenuItemClickListener: (MenuItem) -> Boolean = { _ -> false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupActionbar()
        setToolbarTitle()
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

    override fun setMenu(@MenuRes menuId: Int, onMenuItemClickListener: (MenuItem) -> Boolean) {
        this.menuId = menuId
        this.onMenuItemClickListener = onMenuItemClickListener
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menuId == 0) {
            return super.onCreateOptionsMenu(menu)
        }

        menuInflater.inflate(menuId, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (onMenuItemClickListener(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setToolbarTitle() {
        supportActionBar?.title = ""
    }

    override fun onViewModelSetup() {}

    override fun <T> GoLiveData<T>.observe(observer: Observer<in T>) {
        observe(this@BaseActivity, observer)
    }

}