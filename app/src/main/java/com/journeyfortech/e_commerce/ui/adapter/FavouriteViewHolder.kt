package com.journeyfortech.e_commerce.ui.adapter

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.db.Products
import com.journeyfortech.e_commerce.databinding.ItemFavouriteBinding
import javax.inject.Inject

class FavouriteViewHolder @Inject constructor(
    private val itemFavouriteBinding: ItemFavouriteBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemFavouriteBinding.root) {

    fun bind(products: Products){
        itemFavouriteBinding.apply {
            glide.load(products.image)
                .into(ivFav)
            tvFavTitle.text = products.title
            tvFavDescription.text = products.description
            tvFavPrice.text = "Rs. ${products.price}"
            tvFavDroppedPrice.text = "Rs.  ${products.price}"
            tvFavDroppedPrice.paintFlags = tvFavDroppedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}