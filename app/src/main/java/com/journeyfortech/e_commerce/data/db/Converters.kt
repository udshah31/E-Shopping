package com.journeyfortech.e_commerce.data.db

import androidx.room.TypeConverter
import com.journeyfortech.e_commerce.data.model.product.Rating
import org.json.JSONObject


class Converters {

    @TypeConverter
    fun toRating(rating: Rating): String {
        return JSONObject().apply {
            put("count", rating.count)
            put("rate", rating.rate)
        }.toString()
    }

    @TypeConverter
    fun fromRating(rating: String): Rating {
        val json = JSONObject(rating)
        return Rating(json.getInt("count"), json.getDouble("rate"))

    }
}