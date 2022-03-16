package com.journeyfortech.e_commerce.data.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getAllCart(): LiveData<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cartEntity: CartEntity)

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Query("SELECT * FROM cart WHERE cartId =:id")
    suspend fun findCartItemId(id: Int): CartEntity?

    @Delete
    suspend fun deleteCart(cartEntity: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCart()

}