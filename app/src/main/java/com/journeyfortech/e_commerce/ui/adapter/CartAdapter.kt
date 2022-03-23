package com.journeyfortech.e_commerce.ui.adapter


import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.journeyfortech.e_commerce.R
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.databinding.ItemCartBinding
import com.journeyfortech.e_commerce.ui.listeners.QuantityListener

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var cartList: MutableList<Cart> = mutableListOf()
    lateinit var listener: QuantityListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemCartBinding.bind(itemView)
        fun bind(cartEntity: Cart) {
            binding.tvCartTitle.text = cartEntity.title
            binding.tvCartDescription.text = cartEntity.description
            Glide.with(itemView).load(cartEntity.image).into(binding.ivCart)
            binding.tvCartPrice.text = "Rs." + cartEntity.price.toString()
            binding.tvCartDroppedPrice.paintFlags =
                binding.tvCartDroppedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvQuantity.text = cartEntity.quantity.toString()


            incrementCartQuantity(cartEntity, binding)
            decrementCartQuantity(cartEntity, binding)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
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
        cart: Cart,
        binding: ItemCartBinding
    ) {
        binding.addCartQuantity.setOnClickListener {
            listener.onQuantityAdded(cart.id!!, binding.tvQuantity)
        }
    }

    private fun decrementCartQuantity(
        cart: Cart,
        binding: ItemCartBinding
    ) {
        binding.removeCartQuantity.setOnClickListener {
            listener.onQuantityAdded(cart.id!!, binding.tvQuantity)
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    private var onItemClickListener: ((Cart) -> Unit)? = null

    fun setOnItemClickListener(listener: (Cart) -> Unit) {
        onItemClickListener = listener
    }


    fun setData(product: List<Cart>) {
        this.cartList = product.toMutableList()
        notifyDataSetChanged()
    }

    fun getAdapterPosition(position: Int): Cart {
        return cartList[position]
    }

    /*fun getCartId(position: Int): Int {
        val currentProduct: Cart = cartList[position]
        return currentProduct.quantity!!
    }

    fun getCartQty(position: Int): Int {
        val currentProduct: Cart = cartList[position]
        return currentProduct.quantity!!
    }*/

     fun getItems(): MutableList<Cart> = cartList

}