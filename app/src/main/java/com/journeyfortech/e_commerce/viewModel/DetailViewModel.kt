package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.data.db.Products
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Constants.PRODUCT_ID
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ECommerceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private val productId = state.get<ProductResponseItem>(PRODUCT_ID)

    private val _singleProduct = MutableStateFlow<Resource<ProductResponseItem>>(Resource.Loading())
    val singleProduct = _singleProduct.asStateFlow()


    private val _products =
        MutableStateFlow<Resource<List<ProductResponseItem>>>(Resource.Loading())
    val products = _products.asStateFlow()



    init {
        getSingleProduct(productId!!.id)
        //isFavourite(productId.id)
        getProducts()
    }

    private fun getSingleProduct(productId: Int) = viewModelScope.launch {
        repository.getSingleProduct(productId)
            .onStart {
                _singleProduct.value = Resource.Loading()
            }.catch { e ->
                _singleProduct.value = Resource.Error(e.toString())
            }.collect { response ->
                _singleProduct.value = Resource.Success(response)
            }
    }

    private fun getProducts() = viewModelScope.launch {
        repository.getProducts()
            .onStart {
                _products.value = Resource.Loading()
            }.catch { e ->
                _products.value = Resource.Error(e.toString())
            }.collect { response ->
                _products.value = Resource.Success(response)
            }
    }





    //database

    fun updateProduct(products: ProductResponseItem) = viewModelScope.launch {
        repository.updateProduct(products)
    }

    //cart

    fun addOrUpdateCartProduct(id: Int) {
        viewModelScope.launch {
            val cart = repository.findCartItemId(id)
            val product = repository.findCartItemId(id)

            if (cart?.cartQuantity ?: 0 < product?.cartQuantity!!) {
                if (cart != null) {
                    cart.cartQuantity.plus(1)
                    repository.updateCart(cart)
                } else {
                    val cartEntity = Cart(id, 1)
                    repository.insertCart(cartEntity)
                }
            }
        }
    }

    fun getAllCart() = repository.getAllCart


}