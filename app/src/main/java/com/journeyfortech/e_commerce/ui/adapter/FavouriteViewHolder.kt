package com.journeyfortech.e_commerce.ui.adapter

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.databinding.ItemFavouriteBinding
import javax.inject.Inject

class FavouriteViewHolder @Inject constructor(
    private val itemFavouriteBinding: ItemFavouriteBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemFavouriteBinding.root) {

    fun bind(favourite: Favourite){
        itemFavouriteBinding.apply {
            glide.load(favourite.image)
                .into(ivFav)
            tvFavTitle.text = favourite.title
            tvFavDescription.text = favourite.description
            tvFavPrice.text = "Rs. ${favourite.price}"
            tvFavDroppedPrice.text = "Rs.  ${favourite.price}"
            tvFavDroppedPrice.paintFlags = tvFavDroppedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }
}