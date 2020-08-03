package com.gojeck.feature.utils

import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gojeck.R


/**
 * set list of item viewModel and item layout, then no need to implement RecyclerViewAdapter.
 * on the layout, viewModel name should be 'model'
 *
 * - if you want other layout manager, use app:layoutManager="LinearLayoutManager" over than the attributes
 *
 * - this doesn't use lifecycle. so, don't use LiveData, just use variable.
 *   if you have to use LiveData, use bindData(itemList, layoutId, lifecycleOwner) on Fragment or Activity side
 *
 * @param itemList  Item Model
 * @param layoutId  Item's layout id
 * */
@BindingAdapter("itemList", "itemLayoutId")
fun <VM : Any> RecyclerView.bindData(
    itemList: List<VM>?,
    layoutId: Int
) {
    bindData(itemList, layoutId, null)
}

@BindingAdapter("app:imgUrl")
fun ImageView.setImageUrl(imgUrl: String?) {
    if (imgUrl.isNullOrEmpty().not()) {
        val options: RequestOptions = RequestOptions()
            .circleCrop()
            .placeholder(R.drawable.circle_drawable)
            .error(R.drawable.circle_drawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.NORMAL)

        Glide.with(context)
            .load(imgUrl)
            .apply(options)
            .into(this)

    } else {
        setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.circle_drawable
            )
        )
    }
}

@BindingAdapter("app:imgTint")
fun ImageView.setTint(tintColor: String?) {
    if (tintColor.isNullOrEmpty().not()) {
        try {
            setColorFilter(Color.parseColor(tintColor), android.graphics.PorterDuff.Mode.MULTIPLY)
        } catch (ex: IllegalArgumentException) {
            val defaultColor = "#ffcc0000"
            setColorFilter(
                Color.parseColor(defaultColor),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )
        }
    }
}
