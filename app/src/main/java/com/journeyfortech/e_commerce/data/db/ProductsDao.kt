package com.journeyfortech.e_commerce.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(products: Products)

    @Update
    suspend fun updateProduct(products: Products)

    @Query("UPDATE product SET isFav = 0")
    suspend fun updateAllFavouriteToFalse()

    @Delete
    suspend fun deleteProduct(products: Products)

    @Query("DELETE FROM product ")
    suspend fun deleteAll()

    @Query("SELECT * FROM product ORDER BY id ASC")
    fun getAllProducts(): Flow<List<Products>>

    @Query(value = "SELECT * FROM product WHERE isFav = 1")
    fun getAllFavoriteProducts() : Flow<List<Products>>

    @Query("SELECT * FROM product WHERE isFav = 1")
    fun getAllCartProducts(): Flow<List<Products>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun findItemId(id: String?):Products?

    @Query("SELECT * FROM product WHERE id IN (:ids)")
    fun findItemsWithIds(ids :List<Int>):Flow<List<Products>>

}