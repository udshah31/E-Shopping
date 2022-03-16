package com.journeyfortech.e_commerce.repository

import androidx.lifecycle.LiveData
import com.journeyfortech.e_commerce.data.api.ApiService
import com.journeyfortech.e_commerce.data.db.AppDatabase
import com.journeyfortech.e_commerce.data.db.CartEntity
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ECommerceRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) {

    suspend fun getProducts(): Flow<List<ProductResponseItem>> = flow {
        emit(apiService.getProducts())
    }.flowOn(Dispatchers.IO).conflate()


    suspend fun getSingleProduct(productId: Int): Flow<ProductResponseItem> = flow {
        emit(apiService.getSingleProduct(productId))
    }.flowOn(Dispatchers.IO).conflate()


    //database

    val getAllProducts: LiveData<List<Favourite>> = appDatabase.favDao().getAllProducts()

    val getAllFavProducts: LiveData<List<Favourite>> = appDatabase.favDao().getAllFavProducts()

    fun findItemsWithIds(ids: List<Int>): LiveData<List<Favourite>> {
        return appDatabase.favDao().findItemsWithIds(ids)
    }

    fun findItemWithId(id: Int): Favourite? {
        return appDatabase.favDao().findItemId(id.toString())
    }

    suspend fun updateAllFavouriteTOFalse() {
        appDatabase.favDao().updateAllFavToFalse()
    }

    suspend fun insertProduct(favourite: Favourite) = appDatabase.favDao().insertFav(favourite)

    suspend fun deleteProduct(favourite: Favourite) = appDatabase.favDao().deleteFav(favourite)

    suspend fun deleteAll() {
        appDatabase.favDao().deleteAll()
    }

    suspend fun updateProduct(productResponseItem: ProductResponseItem) {
        val favEntity = productResponseItem.toEntity()
        appDatabase.favDao().updateProduct(favEntity)
    }

    suspend fun updateProductEntity(favourite: Favourite) {
        appDatabase.favDao().updateProduct(favourite)
    }


    //cart
    val getAllCart: LiveData<List<CartEntity>> = appDatabase.cartDao().getAllCart()

    suspend fun insertCart(cartEntity: CartEntity) = appDatabase.cartDao().insertCart(cartEntity)

    suspend fun updateCart(cartEntity: CartEntity) = appDatabase.cartDao().updateCart(cartEntity)

    suspend fun deleteCart(cartEntity: CartEntity) = appDatabase.cartDao().deleteCart(cartEntity)

    suspend fun deleteAllCart() {
        appDatabase.cartDao().deleteAllCart()
    }

    suspend fun findCartItemId(id: Int): CartEntity? {
        return appDatabase.cartDao().findCartItemId(id)
    }


}