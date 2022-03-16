package com.journeyfortech.e_commerce.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.databinding.ItemProductBinding
import javax.inject.Inject

class HomeMostSellingProductAdapter @Inject constructor(
    private val glide: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var productList = emptyList<ProductResponseItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), glide
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val product = productList[position]

        (holder as ProductViewHolder).apply {
            bind(product)
            itemView.setOnClickListener {
                onItemClickListener?.let { it(product) }
            }
        }

    }

    private var onItemClickListener: ((ProductResponseItem) -> Unit)? = null


    fun setOnItemClickListener(listener: (ProductResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setData(product: List<ProductResponseItem>) {
        this.productList = product
        notifyDataSetChanged()
    }
}