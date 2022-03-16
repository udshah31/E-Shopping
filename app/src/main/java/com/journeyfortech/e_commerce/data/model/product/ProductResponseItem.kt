package com.journeyfortech.e_commerce.data.model.product

import com.journeyfortech.e_commerce.data.db.Favourite
import java.io.Serializable

data class ProductResponseItem(
    var description: String,
    var id: Int,
    var image: String,
    var price: Double,
    var droppedPrice: Double,
    var quantity: Int,
    var rating: Rating,
    var title: String,
    var isFav: Boolean,
    var isCart: Boolean
) : Serializable {
    fun toEntity(): Favourite {
        return Favourite(
            id,
            title,
            description,
            price,
            droppedPrice,
            quantity,
            rating.rate,
            rating.count,
            image,
            isFav,
            isCart
        )
    }
}