package com.journeyfortech.e_commerce.repository

import com.journeyfortech.e_commerce.data.api.ApiService
import com.journeyfortech.e_commerce.data.db.AppDatabase
import com.journeyfortech.e_commerce.data.db.Cart
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

    val getAllFavourite: Flow<List<Favourite>> = appDatabase.favouriteDao().getAllFavourite()

    suspend fun insetFavourite(favourite: Favourite) =
        appDatabase.favouriteDao().insertFavourite(favourite)

    suspend fun deleteFavouriteById(id: Int) = appDatabase.favouriteDao().deleteFavouriteById(id)

    suspend fun deleteAllFavourite(favourite: Favourite) =
        appDatabase.favouriteDao().deleteAllFavourite(favourite)

    fun getFavouriteItem(id: Int) = appDatabase.favouriteDao().getFavouriteItem(id)


    //cart
    val getAllCart: Flow<List<Cart>> = appDatabase.cartDao().getAllCart()

    suspend fun insertCart(cart: Cart) = appDatabase.cartDao().insertCart(cart)

    suspend fun updateCartQuantity(cart: Cart) = appDatabase.cartDao().updateCart(cart)

    suspend fun deleteCart(cart: Cart) = appDatabase.cartDao().deleteCart(cart)

    suspend fun deleteCartById(id: Int) = appDatabase.cartDao().deleteCartById(id)

    fun getCartItem(id: Int) = appDatabase.cartDao().getCartItem(id)

}