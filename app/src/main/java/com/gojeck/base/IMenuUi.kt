package com.gojeck.base

import android.view.MenuItem
import androidx.annotation.MenuRes

interface IMenuUi {
    @get:MenuRes
    var menuId: Int
    fun setMenu(@MenuRes menuId: Int, onMenuItemClickListener: (MenuItem) -> Boolean)
    var onMenuItemClickListener: (MenuItem) -> Boolean
}
