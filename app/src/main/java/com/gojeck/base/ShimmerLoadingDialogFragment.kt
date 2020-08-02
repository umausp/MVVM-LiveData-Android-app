package com.gojeck.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gojeck.R


class ShimmerLoadingDialogFragment : DialogFragment() {
    companion object {
        private const val SHIMMER_FRAGMENT_TAG = "ShimmerLoadingDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shimmering_loading_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setGravity(Gravity.TOP)
        val params = dialog.window?.attributes
        params?.y = 0

        dialog.window?.attributes = params
        return dialog
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, SHIMMER_FRAGMENT_TAG)
    }
}
