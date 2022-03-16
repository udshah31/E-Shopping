package com.journeyfortech.e_commerce.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Category
import com.journeyfortech.e_commerce.databinding.ItemCategoryBinding
import javax.inject.Inject

class CategoryViewHolder @Inject constructor(
    private val itemCategoryBinding: ItemCategoryBinding,
    private val glide: RequestManager
) : RecyclerView.ViewHolder(itemCategoryBinding.root) {

    fun bind(category:Category){
        itemCategoryBinding.apply {
            glide.load(category.image)
                .into(ivCategory)
            tvCategory.text = category.title

        }
    }
}