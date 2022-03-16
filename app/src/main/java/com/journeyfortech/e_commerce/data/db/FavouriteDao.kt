package com.journeyfortech.e_commerce.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(favourite: Favourite)

    @Update
    suspend fun updateProduct(favourite: Favourite)

    @Query("UPDATE favourite SET isFav = 0")
    suspend fun updateAllFavToFalse()

    @Delete
    suspend fun deleteFav(favourite: Favourite)

    @Query("DELETE FROM favourite")
    suspend fun deleteAll()

    @Query("SELECT * FROM favourite")
    fun getAllProducts(): LiveData<List<Favourite>>

    @Query("SELECT * FROM favourite WHERE isFav = 1")
    fun getAllFavProducts(): LiveData<List<Favourite>>

    @Query("SELECT * FROM favourite WHERE isCart = 1")
    fun getAllCartProducts():LiveData<List<Favourite>>

    @Query("SELECT * FROM favourite WHERE id =:id")
    fun findItemId(id: String): Favourite?

    @Query("SELECT * FROM favourite WHERE id IN (:ids)")
    fun findItemsWithIds(ids: List<Int>): LiveData<List<Favourite>>
}