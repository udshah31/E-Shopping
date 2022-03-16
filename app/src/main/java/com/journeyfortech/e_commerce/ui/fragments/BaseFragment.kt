package com.journeyfortech.e_commerce.ui.fragments

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.journeyfortech.e_commerce.utils.UiCommunicationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    val TAG: String = "AppDebug"
    lateinit var uiCommunicationListener: UiCommunicationListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UiCommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement UICommunicationListener")
        }

    }
}