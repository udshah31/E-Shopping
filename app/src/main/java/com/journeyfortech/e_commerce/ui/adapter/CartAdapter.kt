package com.journeyfortech.e_commerce.ui.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.databinding.ItemCartBinding
import com.journeyfortech.e_commerce.ui.listeners.QuantityListener

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var cartList: MutableList<ProductResponseItem> = mutableListOf()
    lateinit var listener: QuantityListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemCartBinding.bind(itemView)
        fun bind(product: ProductResponseItem) {
            binding.tvCartTitle.text = product.title
            binding.tvCartDescription.text = product.description
            Glide.with(itemView).load(product.image).into(binding.ivCart)
            binding.tvCartDroppedPrice.paintFlags =
                binding.tvCartDroppedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvQuantity.text = product.quantity.toString()

            incrementCartQuantity(product, binding)
            decrementCartQuantity(product, binding)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_cart, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cartList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(currentItem) }
        }
    }

    private fun incrementCartQuantity(
        cart: ProductResponseItem,
        binding: ItemCartBinding
    ) {
        binding.addCartQuantity.setOnClickListener {
            listener.onQuantityAdded(cart.id, binding.tvQuantity)
        }
    }

    private fun decrementCartQuantity(
        cart: ProductResponseItem,
        binding: ItemCartBinding
    ) {
        binding.removeCartQuantity.setOnClickListener {
            listener.onQuantityAdded(cart.id, binding.tvQuantity)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private var onItemClickListener: ((ProductResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProductResponseItem) -> Unit) {
        onItemClickListener = listener
    }


    fun setData(product: List<ProductResponseItem>) {
        this.cartList = product.toMutableList()
        notifyDataSetChanged()
    }

    fun getAdapterPosition(position: Int): ProductResponseItem {
        return cartList[position]
    }

    fun getProductId(position: Int): Int {
        val currentProduct: ProductResponseItem = cartList[position]
        return currentProduct.quantity
    }

    fun getProductQty(position: Int): Int {
        val currentProduct: ProductResponseItem = cartList[position]
        return currentProduct.quantity
    }

    fun getItems(): MutableList<ProductResponseItem> = cartList

}