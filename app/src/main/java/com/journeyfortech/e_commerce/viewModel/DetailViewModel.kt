package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.data.db.Favourite
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

    /* private val _isFavourite = MutableStateFlow(false)
     val isFavourite = _isFavourite.asStateFlow()

     private val _isFavoriteLoading = MutableStateFlow(false)
     val isFavoriteLoading = _isFavoriteLoading.asStateFlow()

     private val _isCart = MutableStateFlow<Boolean>(false)
     val isCart = _isCart.asStateFlow()

     private val _isCartLoading = MutableStateFlow<Boolean>(false)
     val isCartLoading = _isCartLoading.asStateFlow()*/


    init {
        getSingleProduct(productId!!.id)
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

    fun deleteCartById(id: Int) = viewModelScope.launch {
        repository.deleteCartById(id)
    }


    fun deleteFavouriteById(id: Int) = viewModelScope.launch {
        repository.deleteFavouriteById(id)
    }


    /*private fun isFavourite(id: Int) = viewModelScope.launch {
        _isFavoriteLoading.value = true
        repository.getFavouriteItem(id)
            .catch {
                _isFavoriteLoading.value = false
            }.collect {
                _isFavourite.value = it?.isFav!!
                _isFavoriteLoading.value = false
            }
    }


    fun onFavouriteClicked() = viewModelScope.launch {
        _isFavoriteLoading.value = true
        if (_isFavourite.value) {
            val delete = deleteFavouriteById(productId!!.id)
            _isFavourite.value = delete.equals(1)
        } else {
            val savedId = singleProduct.value.data!!.let {
                val item = Favourite(
                    id = it.id,
                    image = it.image,
                    description = it.description,
                    price = it.price,
                    droppedPrice = it.droppedPrice,
                    title = it.title,
                    quantity = it.quantity,
                    rating = it.rating,
                    isFav = true,
                )
                insertFavourite(item)
            }
            _isFavourite.value = savedId.equals(productId?.id)
        }
        _isFavoriteLoading.value = false
    }*/

    //database

    fun getAllFavourite() = repository.getAllFavourite

    fun insertFavourite(favourite: Favourite) = viewModelScope.launch {
        repository.insetFavourite(favourite)
    }


    //cart
    fun insertCart(cart: Cart) = viewModelScope.launch {
        repository.insertCart(cart)
    }

    fun getAllCart() = repository.getAllCart


}