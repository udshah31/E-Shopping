package com.journeyfortech.e_commerce.data.model.product

import com.journeyfortech.e_commerce.data.db.Products
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
    fun toEntity(): Products {
        return Products(
            id,
            title,
            description,
            price,
            droppedPrice,
            quantity,
            rating,
            image,
            isCart,
            isFav
        )
    }
}
