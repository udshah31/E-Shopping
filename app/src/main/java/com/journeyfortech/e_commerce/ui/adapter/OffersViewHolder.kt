package com.journeyfortech.e_commerce.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Offer
import com.journeyfortech.e_commerce.databinding.ItemOffersBinding
import javax.inject.Inject

class OffersViewHolder @Inject constructor(
    private val itemOffersBinding: ItemOffersBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemOffersBinding.root) {
    fun bind(offers: Offer) {
        itemOffersBinding.apply {
            //val imageUrl = IMAGE_URL_FRONT+ nowPlaying.poster_path
            glide.load(offers.image).into(ivOffers)
        }
    }
}