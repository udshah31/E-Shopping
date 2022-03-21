package com.journeyfortech.e_commerce.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CartDao {
    @Query( "SELECT * FROM cart")
    fun getAllCart() : Flow<List<Cart>>

    @Insert
    suspend fun insertCart(cart: Cart)

    @Update
    suspend fun updateCart(cart: Cart)

    @Query("SELECT * FROM cart WHERE cartId = :id")
    suspend fun findCartItemId(id: Int): Cart?

    @Delete
    suspend fun deleteCartItem(cart: Cart)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCart()
}