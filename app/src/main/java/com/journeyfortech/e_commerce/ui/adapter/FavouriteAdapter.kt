package com.journeyfortech.e_commerce.ui.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.databinding.ItemFavouriteBinding
import javax.inject.Inject

class FavouriteAdapter @Inject constructor(
    private var glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favouriteList = emptyList<Favourite>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FavouriteViewHolder(
            ItemFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),glide
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = favouriteList[position]
        (holder as FavouriteViewHolder).apply {
            bind(currentItem)
            itemView.setOnClickListener {
                onItemClickListener?.let { it(currentItem) }
            }
        }
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }

    private var onItemClickListener: ((Favourite) -> Unit)? = null
    fun setOnItemClickListener(listener: (Favourite) -> Unit) {
        onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(products: List<Favourite>) {
        this.favouriteList = products
        notifyDataSetChanged()
    }

    fun getAdapterPosition(position: Int): Favourite {
        return favouriteList[position]
    }


}