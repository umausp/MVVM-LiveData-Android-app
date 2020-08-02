package com.gojeck.base

import androidx.lifecycle.Observer
import com.gojeck.R


/**
 * hide layout on loading.
 * on error, show full error fragment. so, doesn't show layout as well
 */
fun IBaseUi.stateObserverFullPage(
    showProgressBar: Boolean = true,
    showFullPageError: Boolean = true,
    onResult: (State) -> Boolean = { false }
): Observer<State> =
    Observer {

        if (onResult(it)) {
            return@Observer
        }

        if (showProgressBar) {
            shimmeringLayout.value = it.isLoading()
//            showLoadingFullPage(it.isLoading())
        }

        if (showFullPageError) {
            it.onNetworkError { retry ->
                showNetworkErrorFullPage(retry)
            }
        }
    }

private fun IBaseUi.showNetworkErrorFullPage(retry: () -> Unit) {
    var fragment = binding.root.getTag(R.id.view_tag_network_error) as? NetworkErrorFragment?
    if (fragment == null) {
        fragment = NetworkErrorFragment()
        binding.root.setTag(R.id.view_tag_network_error, fragment)
    }

    fragment.classPackageName = classPackageName
    fragment.retryListener = retry
    fragment.showIfNotShowing(findFragmentManager(), classPackageName)
}

private fun IBaseUi.showLoadingFullPage(showLoading: Boolean) {
    var fragment =
        binding.root.getTag(R.id.view_tag_shimmer_loading) as? ShimmerLoadingDialogFragment?
    if (fragment == null) {
        fragment = ShimmerLoadingDialogFragment()
        binding.root.setTag(R.id.view_tag_shimmer_loading, fragment)
    }

    if (showLoading) {
        fragment.showIfNotShowing(findFragmentManager(), classPackageName)
    } else {
//        fragment.dismissAllowingStateLoss()
    }
}

