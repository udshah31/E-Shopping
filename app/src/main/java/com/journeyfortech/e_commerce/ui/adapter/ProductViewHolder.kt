package com.journeyfortech.e_commerce.ui.adapter

import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.databinding.ItemProductBinding
import javax.inject.Inject

class ProductViewHolder @Inject constructor(
    private val itemProductBinding: ItemProductBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemProductBinding.root) {

    fun bind(product: ProductResponseItem) {
        itemProductBinding.apply {
            //val imageUrl = IMAGE_URL_FRONT+ nowPlaying.poster_path
            glide.load(product.image).into(rivImage)
            tvDiscount.text = "35%"
            tvTitle.text = product.title
            tvReview.text = product.rating.count.toString() + " Reviews"
            tvPrice.text = "Rs. " + product.price.toString()
            tvReducedPrice.text = "Rs.  ${product.price}"
            tvReducedPrice.paintFlags = tvReducedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            rbRating.rating = product.rating.rate.toFloat()
        }
    }

}