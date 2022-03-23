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
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.data.db.Favourite
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
                            setFloatStatusFav(it.data.id)
                            fabCartBtnAction(it.data)
                            setFloatStatusCart(it.data.id)
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
                    val item = Favourite(
                        product.id,
                        product.title,
                        product.description,
                        product.price,
                        product.droppedPrice,
                        product.quantity,
                        product.rating,
                        product.image,
                        product.isCart,
                        true
                    )
                    detailFavourite.setImageResource(R.drawable.ic_round_favorite)
                    viewModel.insertFavourite(item)
                    Toast.makeText(activity, "Added To favourite", Toast.LENGTH_SHORT).show()
                } else {
                    product.isFav = false
                    detailFavourite.setImageResource(R.drawable.ic_round_favorite_border)
                    viewModel.deleteFavouriteById(product.id)
                    Toast.makeText(activity, "Removed From favourite", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setFloatStatusFav(id: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.getAllFavourite()
                .collect { fav ->
                    if (fav.isNullOrEmpty()) {
                        binding.detailFavourite.setImageResource(R.drawable.ic_round_favorite_border)
                    } else {
                        fav.map { item ->
                            if (item.id == id) {
                                if (item.isFav == true) {
                                    binding.detailFavourite.setImageResource(R.drawable.ic_round_favorite)
                                } else {
                                    binding.detailFavourite.setImageResource(R.drawable.ic_round_favorite_border)
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun fabCartBtnAction(product: ProductResponseItem) {
        binding.apply {
            detailCart.setOnClickListener {
                if (!product.isCart) {
                    product.isCart = true
                    val item = Cart(
                        product.id,
                        product.title,
                        product.description,
                        product.price,
                        product.droppedPrice,
                        product.quantity,
                        product.rating,
                        product.image,
                        true,
                    )
                    detailCart.setImageResource(R.drawable.ic_round_add_shopping_cart)
                    viewModel.insertCart(item)
                    Toast.makeText(activity, "Added To Cart", Toast.LENGTH_SHORT).show()
                } else {
                    product.isCart = false
                    detailCart.setImageResource(R.drawable.ic_round_remove_shopping_cart)
                    viewModel.deleteCartById(product.id)
                    Toast.makeText(activity, "Removed From Cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setFloatStatusCart(id: Int) {
        lifecycleScope.launchWhenCreated {
            viewModel.getAllCart()
                .collect { cart ->
                    if (cart.isNullOrEmpty()) {
                        binding.detailCart.setImageResource(R.drawable.ic_round_add_shopping_cart)
                    } else {
                        cart.map { item ->
                            if (item.id == id) {
                                if (item.isCart == true) {
                                    binding.detailCart.setImageResource(R.drawable.ic_round_remove_shopping_cart)
                                } else {
                                    binding.detailCart.setImageResource(R.drawable.ic_round_add_shopping_cart)
                                }
                            }
                        }
                    }
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


