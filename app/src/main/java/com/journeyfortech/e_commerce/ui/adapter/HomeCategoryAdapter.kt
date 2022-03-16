package com.journeyfortech.e_commerce.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Category
import com.journeyfortech.e_commerce.databinding.ItemCategoryBinding
import javax.inject.Inject

class HomeCategoryAdapter @Inject constructor(
    private val glide: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categoryList = emptyList<Category>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
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
        val category = categoryList[position]

        (holder as CategoryViewHolder).apply {
            bind(category)
            itemView.setOnClickListener {
                onItemClickListener?.let { it(category) }
            }
        }

    }

    private var onItemClickListener: ((Category) -> Unit)? = null


    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(category: List<Category>) {
        this.categoryList = category
        notifyDataSetChanged()
    }
}