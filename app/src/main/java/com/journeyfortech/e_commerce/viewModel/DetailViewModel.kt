package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.CartEntity
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    private val _singleProduct =
        MutableStateFlow<Resource<ProductResponseItem>>(Resource.Loading())
    val singleProduct = _singleProduct.asStateFlow()


    private val _products =
        MutableStateFlow<Resource<List<ProductResponseItem>>>(Resource.Loading())
    val products = _products.asStateFlow()

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }


    init {
        getProducts()
    }


    fun getSingleProduct(productId: Int) = viewModelScope.launch {
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
    fun insertFav(favourite: Favourite) = viewModelScope.launch {
        repository.insertProduct(favourite)
    }

    fun updateProduct(productResponseItem: ProductResponseItem) {
        viewModelScope.launch {
            repository.updateProduct(productResponseItem)
        }
    }

    fun getAllFav() = repository.getAllFavProducts


    fun deleteFav(favourite: Favourite) = viewModelScope.launch {
        repository.deleteProduct(favourite)
    }

    //cart

    fun addOrUpdateCartProduct(id: Int) {
        viewModelScope.launch(Dispatchers.Default + coroutineExceptionHandler) {
            val cart = repository.findCartItemId(id)
            val product = repository.findCartItemId(id)

            if (cart?.cartQty ?: 0 < product?.cartQty!!) {
                if (cart != null) {
                    cart.cartQty.plus(1)
                    repository.updateCart(cart)
                } else {
                    val cartEntity = CartEntity(id, 1)
                    repository.insertCart(cartEntity)
                }
            }
        }
    }

    fun getAllCart() = repository.getAllCart


}