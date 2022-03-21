package com.journeyfortech.e_commerce.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey
    var cartId: Int? = null,
    var cartQuantity: Int
)