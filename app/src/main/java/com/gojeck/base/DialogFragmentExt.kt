package com.gojeck.base

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment?.isShowing(): Boolean =
    this != null && dialog != null && dialog!!.isShowing && !isRemoving

fun DialogFragment?.showIfNotShowing(manager: FragmentManager, packageName: String) {
    if (!isShowing()) {
        this!!.show(manager, packageName)
    }
}
