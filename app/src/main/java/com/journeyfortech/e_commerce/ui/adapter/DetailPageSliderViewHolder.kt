package com.journeyfortech.e_commerce.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Slider
import com.journeyfortech.e_commerce.databinding.ItemDetailPageSliderBinding
import javax.inject.Inject

class DetailPageSliderViewHolder @Inject constructor(
    private val itemDetailPageSliderBinding: ItemDetailPageSliderBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemDetailPageSliderBinding.root) {

    fun bind(poster: Slider) {
        itemDetailPageSliderBinding.apply {
            //val imageUrl = Constants.IMAGE_URL_BACK + poster.file_path
            glide.load(poster.image).into(image)
        }
    }
}