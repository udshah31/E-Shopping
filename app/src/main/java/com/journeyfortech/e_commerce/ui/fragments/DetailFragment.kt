package com.journeyfortech.e_commerce.ui.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.data.model.slider
import com.journeyfortech.e_commerce.databinding.FragmentDetailBinding
import com.journeyfortech.e_commerce.ui.HomeActivity
import com.journeyfortech.e_commerce.ui.adapter.DetailPageSlider
import com.journeyfortech.e_commerce.ui.adapter.SimilarProductAdapter
import com.journeyfortech.e_commerce.utils.Resource
import com.journeyfortech.e_commerce.viewModel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment @Inject constructor() : BaseFragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailViewModel>()
    private var job: Job? = null

    @Inject
    lateinit var viewPagerAdapter: DetailPageSlider

    @Inject
    lateinit var similarItemsAdapter: SimilarProductAdapter

    private var isFav: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as HomeActivity).apply {
            hideBottomNavigation()
            hideToolBar()
        }
        setUpRecyclerView()

        binding.apply {
            detailToolbar.setNavigationIcon(R.drawable.back_icon)
            detailToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            vpDetail.adapter = viewPagerAdapter
            vpDetail.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicators(position)
                }
            })
        }

        viewPagerAdapter.setData(posters = slider)
        setIndicator(slider.size)
        job = lifecycleScope.launchWhenCreated {
            viewModel.singleProduct.collect {
                binding.apply {
                    when (it) {
                        is Resource.Success -> {
                            uiCommunicationListener.displayProgressBar(false)
                            detailTitle.text = it.data!!.title
                            rbRating.rating = it.data.rating.rate.toFloat()
                            tvReview.text = "${it.data.rating.count} + Reviews"
                            detailDiscount.text = "35%"


                            detailActualPrice.text = "Rs. ${it.data.price}"
                            detailFakePrice.text = "Rs.  ${it.data.price}"
                            detailFakePrice.paintFlags =
                                detailFakePrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            detailTvOverview.text = it.data.description


                            fabFavouriteBtnAction(it.data)
                            fabCartBtnAction()
                        }
                        is Resource.Error -> {

                        }

                        is Resource.Loading -> {
                            uiCommunicationListener.displayProgressBar(true)
                        }
                    }

                }
            }
        }
        job = lifecycleScope.launchWhenCreated {
            viewModel.products.collect {
                binding.apply {
                    when (it) {

                        is Resource.Success -> {
                            uiCommunicationListener.displayProgressBar(false)
                            similarItemsAdapter.setData(it.data!!)
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            uiCommunicationListener.displayProgressBar(true)
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

    private fun fabFavouriteBtnAction(product: ProductResponseItem) {
        binding.apply {
            detailFavourite.setOnClickListener {
                if (!product.isFav) {
                    product.isFav = true
                    viewModel.updateProduct(product)
                    detailFavourite.setImageResource(R.drawable.ic_round_favorite)
                    Toast.makeText(activity, " Add to Favourite", Toast.LENGTH_SHORT).show()
                } else if (product.isFav) {
                    product.isFav = false
                    viewModel.updateProduct(product)
                    detailFavourite.setImageResource(R.drawable.ic_round_favorite_border)
                    Toast.makeText(activity, "Removed from favourite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun fabCartBtnAction() {
        binding.apply {
            detailCart.setOnClickListener {

            }
        }
    }


    private fun setUpRecyclerView() {
        binding.apply {
            rvSimilarItem.adapter = similarItemsAdapter
            rvSimilarItem.layoutManager =
                LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job?.cancel()
        (requireActivity() as HomeActivity).apply {
            showBottomNavigation()
            showToolBar()
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }
}


