package com.journeyfortech.e_commerce.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
import com.journeyfortech.e_commerce.databinding.FragmentCartBinding
import com.journeyfortech.e_commerce.ui.HomeActivity
import com.journeyfortech.e_commerce.ui.adapter.CartAdapter
import com.journeyfortech.e_commerce.ui.listeners.QuantityListener
import com.journeyfortech.e_commerce.utils.Resource
import com.journeyfortech.e_commerce.viewModel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CartFragment : BaseFragment(), QuantityListener {
    private lateinit var binding: FragmentCartBinding
    private val viewModel by viewModels<CartViewModel>()
    private val cartAdapter = CartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as HomeActivity).apply {
            showBottomNavigation()
            supportActionBar?.title = HtmlCompat.fromHtml(
                "<font color='#060A08'>${getString(R.string.title_cart)}</font>",
                FROM_HTML_MODE_LEGACY
            )
        }

        setUpRecyclerView()
        totalPrice()

        cartAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
            }
            findNavController().navigate("")
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
                val item = cartAdapter.getAdapterPosition(position)
                viewModel.deleteCartById(item.id!!)
                Snackbar.make(view, "Successfully Removed", Snackbar.LENGTH_LONG).show()
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvCart)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.cart.collect {
                when (it) {

                    is Resource.Success -> {
                        if (it.data!!.isEmpty()) {
                            binding.cartEmpty.root.visibility = View.VISIBLE
                            binding.rvCart.visibility = View.INVISIBLE
                            binding.totalLayout.visibility = View.INVISIBLE
                        } else {
                            binding.rvCart.visibility = View.VISIBLE
                            binding.totalLayout.visibility = View.VISIBLE
                            cartAdapter.setData(it.data)
                            viewModel.totalPrice(cartAdapter.getItems())

                        }
                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }

    }


    private fun totalPrice() {
        lifecycleScope.launchWhenCreated {
            viewModel.totalPrice
                .collect {
                    binding.totalPrice.text = it
                }
        }
    }


    private fun setUpRecyclerView() {
        cartAdapter.listener = this
        binding.rvCart.adapter = cartAdapter
        binding.rvCart.setHasFixedSize(true)
        binding.rvCart.layoutManager = LinearLayoutManager(activity)

    }


    override fun onQuantityAdded(id: Int, quantityTextView: TextView) {
        Toast.makeText(activity,"add",Toast.LENGTH_SHORT).show()
        return viewModel.quantityIncrement(id, quantityTextView)
    }

    override fun onQuantityRemoved(id: Int, quantityTextView: TextView) {
        Toast.makeText(activity,"removed",Toast.LENGTH_SHORT).show()
        return viewModel.quantityDecrease(id, quantityTextView)
    }


}


