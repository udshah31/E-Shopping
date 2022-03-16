package com.journeyfortech.e_commerce.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.journeyfortech.e_commerce.data.model.Offer
import com.journeyfortech.e_commerce.databinding.ItemOffersBinding
import javax.inject.Inject

class HomeOffersAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var offerList = emptyList<Offer>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return OffersViewHolder(
            ItemOffersBinding.inflate(
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
        val offer = offerList[position]

        (holder as OffersViewHolder).apply {
            bind(offer)
            itemView.setOnClickListener {
                onItemClickListener?.let { it(offer) }
            }
        }

    }

    private var onItemClickListener: ((Offer) -> Unit)? = null


    fun setOnItemClickListener(listener: (Offer) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    fun setData(offers: List<Offer>) {
        this.offerList = offers
        notifyDataSetChanged()
    }
}