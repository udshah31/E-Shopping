package com.journeyfortech.e_commerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.Fragment
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.databinding.FragmentUserBinding
import com.journeyfortech.e_commerce.ui.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment  @Inject constructor() : BaseFragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as HomeActivity).apply {
            showBottomNavigation()
            supportActionBar?.title = HtmlCompat.fromHtml(
                "<font color='#060A08'>${getString(R.string.title_user)}</font>",
                FROM_HTML_MODE_LEGACY
            )
        }

    }
}


