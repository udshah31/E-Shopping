package com.journeyfortech.e_commerce.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourite ORDER BY id ASC")
    fun getAllFavourite(): Flow<List<Favourite>>

    @Query("DELETE FROM favourite WHERE id = :id")
    suspend fun deleteFavouriteById(id: Int)

    @Delete
    suspend fun deleteAllFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourite WHERE id =:id")
    fun getFavouriteItem(id: Int) : Flow<List<Favourite>>

}