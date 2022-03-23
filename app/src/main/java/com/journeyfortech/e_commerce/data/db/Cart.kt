package com.journeyfortech.e_commerce.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.journeyfortech.e_commerce.data.model.product.Rating

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var droppedPrice: Double? = null,
    var quantity: Int? = null,
    var rating: Rating? = null,
    var image: String? = null,
    var isCart: Boolean? = null
)