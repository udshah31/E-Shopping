package com.journeyfortech.e_commerce.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Slider
import com.journeyfortech.e_commerce.databinding.ItemDetailPageSliderBinding
import javax.inject.Inject

class DetailPageSlider @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var imageList = emptyList<Slider>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return DetailPageSliderViewHolder(
            ItemDetailPageSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), glide
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentImage = imageList[position]
        (holder as DetailPageSliderViewHolder).apply {
            bind(currentImage)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setData(posters: List<Slider>) {
        this.imageList = posters
        notifyDataSetChanged()
    }


}