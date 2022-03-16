package com.journeyfortech.e_commerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.data.model.category
import com.journeyfortech.e_commerce.data.model.offers
import com.journeyfortech.e_commerce.data.model.slider
import com.journeyfortech.e_commerce.databinding.FragmentHomeBinding
import com.journeyfortech.e_commerce.ui.HomeActivity
import com.journeyfortech.e_commerce.ui.adapter.HomeCategoryAdapter
import com.journeyfortech.e_commerce.ui.adapter.HomeMostSellingProductAdapter
import com.journeyfortech.e_commerce.ui.adapter.HomeOffersAdapter
import com.journeyfortech.e_commerce.ui.adapter.HomePageSlider
import com.journeyfortech.e_commerce.utils.Resource
import com.journeyfortech.e_commerce.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor() : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private var job: Job? = null

    @Inject
    lateinit var viewPagerAdapter: HomePageSlider

    @Inject
    lateinit var homeCategoryAdapter: HomeCategoryAdapter

    @Inject
    lateinit var homeOffersAdapter: HomeOffersAdapter

    @Inject
    lateinit var homeMostSellingProductAdapter: HomeMostSellingProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as HomeActivity).apply {
            showBottomNavigation()
            showToolBar()
            supportActionBar?.title = HtmlCompat.fromHtml(
                "<font color='#060A08'>Sasto Bazar</font>",
                FROM_HTML_MODE_LEGACY
            )

        }
        setUpRecyclerView()
        binding.apply {
            sliderViewPager.adapter = viewPagerAdapter
            sliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicators(position)
                }
            })
        }

        viewPagerAdapter.setData(slider)
        setIndicator(slider.size)

        homeCategoryAdapter.setOnItemClickListener {

        }
        homeMostSellingProductAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionNavHomeToDetailFragment(it)
            findNavController().navigate(action)
        }
        homeCategoryAdapter.setData(category)

        homeOffersAdapter.setData(offers)

        job = lifecycleScope.launchWhenCreated {
            viewModel.products.collect {
                binding.apply {
                    when (it) {

                        is Resource.Success -> {
                            uiCommunicationListener.displayProgressBar(false)
                            viewPagerAdapter.setData(slider)
                            setIndicator(slider.size)

                            homeCategoryAdapter.setData(com.journeyfortech.e_commerce.data.model.category)

                            homeOffersAdapter.setData(com.journeyfortech.e_commerce.data.model.offers)

                            homeMostSellingProductAdapter.setData(it.data!!)
                        }

                        is Resource.Error -> {

                        }

                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun setIndicator(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            binding.layoutSliderIndicator.addView(indicators[i])
        }
        binding.layoutSliderIndicator.visibility = View.VISIBLE
        setCurrentIndicators(0)
    }


    private fun setCurrentIndicators(position: Int) {
        val childCount: Int = binding.layoutSliderIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutSliderIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }


    private fun setUpRecyclerView() {
        binding.apply {
            rvCategory.adapter = homeCategoryAdapter
            rvCategory.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            rvOffers.adapter = homeOffersAdapter
            rvOffers.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            rvMostSelling.adapter = homeMostSellingProductAdapter
            rvMostSelling.layoutManager =
                LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            rvJustForYou.adapter = homeMostSellingProductAdapter
            rvJustForYou.layoutManager =
                GridLayoutManager(activity, 2, RecyclerView.VERTICAL, false)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        job?.cancel()
    }
}


