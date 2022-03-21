package com.journeyfortech.e_commerce.repository

import com.journeyfortech.e_commerce.data.api.ApiService
import com.journeyfortech.e_commerce.data.db.AppDatabase
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.data.db.Products
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

    val getAllProducts: Flow<List<Products>> = appDatabase.productDao().getAllProducts()

    val getAllFavouriteProducts: Flow<List<Products>> =
        appDatabase.productDao().getAllFavoriteProducts()

    fun findItemsWithIds(ids: List<Int>): Flow<List<Products>> =
        appDatabase.productDao().findItemsWithIds(ids)

    fun findItemWithId(id: Int): Products? = appDatabase.productDao().findItemId(id.toString())

    suspend fun updateAllFavouriteToFalse() = appDatabase.productDao().updateAllFavouriteToFalse()

    suspend fun insetProduct(products: Products) = appDatabase.productDao().insertProduct(products)

    suspend fun deleteProduct(products: ProductResponseItem) {
        val entityFromProduct = products.toEntity()
        appDatabase.productDao().deleteProduct(entityFromProduct)
    }

    suspend fun deleteAll() = appDatabase.productDao().deleteAll()

    suspend fun updateProduct(productResponseItem: ProductResponseItem) {
        val entityFromProduct = productResponseItem.toEntity()
        appDatabase.productDao().updateProduct(entityFromProduct)
    }

    suspend fun updateProductEntity(products: Products) =
        appDatabase.productDao().updateProduct(products)


    //cart
    val getAllCart: Flow<List<Cart>> = appDatabase.cartDao().getAllCart()

    suspend fun insertCart(cart: Cart) = appDatabase.cartDao().insertCart(cart)

    suspend fun updateCart(cart: Cart) = appDatabase.cartDao().updateCart(cart)

    suspend fun deleteCartItem(cart: Cart) = appDatabase.cartDao().deleteCartItem(cart)

    suspend fun deleteAllCart() = appDatabase.cartDao().deleteAllCart()

    suspend fun findCartItemId(id: Int): Cart? = appDatabase.cartDao().findCartItemId(id)

}