package com.gojeck.feature.utils

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gojeck.base.BaseRecyclerViewAdapter

fun <VM : Any> RecyclerView.bindData(
    itemList: List<VM>?,
    layoutId: Int,
    lifecycleOwner: LifecycleOwner?
) {
    if (itemList == null) return

    if (layoutManager == null) {
        layoutManager = LinearLayoutManager(context)
    }

    @Suppress("UNCHECKED_CAST")
    val adapter = adapter as? BaseRecyclerViewAdapter<VM>
        ?: (object : BaseRecyclerViewAdapter<VM>(lifecycleOwner) {
            override fun getItemLayoutId(position: Int) = layoutId
        }.also { adapter = it })

    adapter.submitList(null)
    adapter.submitList(itemList)
}
