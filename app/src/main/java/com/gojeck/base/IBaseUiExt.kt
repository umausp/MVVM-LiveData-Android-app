package com.gojeck.base

import androidx.lifecycle.Observer


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
            networkErrorLayoutLiveData.value = false
            return@Observer
        }

        if (showProgressBar) {
            shimmeringLayout.value = it.isLoading()
            networkErrorLayoutLiveData.value = false
        }

        if (showFullPageError) {
            it.onNetworkError {
                networkErrorLayoutLiveData.value = true
            }
        }
    }
