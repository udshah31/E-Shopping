package com.journeyfortech.e_commerce.data.api

import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    //products
    @GET("products")
    suspend fun getProducts(): List<ProductResponseItem>


    @GET("products/{id}")
    suspend fun getSingleProduct(
        @Path("id") productId: Int
    ): ProductResponseItem
}