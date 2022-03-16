package com.journeyfortech.e_commerce.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Slider
import com.journeyfortech.e_commerce.databinding.ItemContainerBinding
import javax.inject.Inject

class HomePageSliderViewHolder @Inject constructor(
    private val itemContainerBinding: ItemContainerBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemContainerBinding.root) {
    fun bind(poster: Slider) {
        itemContainerBinding.apply {
            //val imageUrl = Constants.IMAGE_URL_BACK + poster.file_path

            glide.load(poster.image).into(image)
            tvTitle.text = poster.title
            tvDicount.text = poster.discount

        }
    }
}