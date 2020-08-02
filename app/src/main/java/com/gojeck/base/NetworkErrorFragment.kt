package com.gojeck.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gojeck.R

class NetworkErrorFragment : DialogFragment() {
    lateinit var classPackageName: String

    var retryListener: (() -> Unit)? = null

    companion object {
        private const val NETWORK_FRAGMENT_TAG = "NetworkErrorFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_network_error, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, theme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                dismiss()
                activity?.onBackPressed()
            }
        }
    }


    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, NETWORK_FRAGMENT_TAG)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (retryListener != null) {
//            showNetworkError(requireView(), logTag, classPackageName) {
//                dismiss()
//                retryListener?.invoke()
//            }
        }
    }

}
