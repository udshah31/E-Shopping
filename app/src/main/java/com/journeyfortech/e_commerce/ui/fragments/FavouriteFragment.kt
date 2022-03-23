package com.journeyfortech.e_commerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.databinding.FragmentFavouriteBinding
import com.journeyfortech.e_commerce.ui.HomeActivity
import com.journeyfortech.e_commerce.ui.adapter.FavouriteAdapter
import com.journeyfortech.e_commerce.utils.Resource
import com.journeyfortech.e_commerce.viewModel.FavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteFragment @Inject constructor() : BaseFragment() {
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavouriteViewModel>()

    @Inject
    lateinit var favouriteAdapter: FavouriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setUpRecyclerView()
        (requireActivity() as HomeActivity).apply {
            showBottomNavigation()
            supportActionBar?.title = HtmlCompat.fromHtml(
                "<font color='#060A08'>${getString(R.string.title_favourite)}</font>",
                FROM_HTML_MODE_LEGACY
            )
        }
        binding.apply {
            rvFav.setHasFixedSize(true)
            rvFav.adapter = favouriteAdapter
            rvFav.layoutManager = LinearLayoutManager(activity)
            favouriteAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                }
                findNavController().navigate("")
            }
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val item = favouriteAdapter.getAdapterPosition(position)
                viewModel.deleteFavouriteById(item.id!!)
                Snackbar.make(view, "Successfully Removed", Snackbar.LENGTH_LONG).show()
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFav)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.favourite.collect {
                when(it){
                    is Resource.Loading ->{
                        uiCommunicationListener.displayProgressBar(true)
                    }
                    is Resource.Success -> {
                        uiCommunicationListener.displayProgressBar(false)
                        if (it.data!!.isEmpty()) {
                            binding.favouriteEmpty.root.visibility = View.VISIBLE
                            binding.rvFav.visibility = View.INVISIBLE
                        } else {
                            binding.rvFav.visibility = View.VISIBLE
                            favouriteAdapter.setData(it.data)
                        }
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        binding.apply {
            rvFav.adapter = favouriteAdapter
            rvFav.layoutManager = LinearLayoutManager(activity)
        }
    }


}


