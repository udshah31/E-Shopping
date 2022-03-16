package com.journeyfortech.e_commerce.data.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favourite")
@Parcelize
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var price: Double? = null,
    var droppedPrice: Double? = null,
    var quantity: Int? = null,
    var rating: Double? = null,
    var ratingReview: Int? = null,
    var image: String? = null,
    var isFav: Boolean? = null,
    var isCart: Boolean
) : Parcelable