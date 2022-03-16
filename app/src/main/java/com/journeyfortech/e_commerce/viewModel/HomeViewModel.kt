package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    private val _products =
        MutableStateFlow<Resource<List<ProductResponseItem>>>(Resource.Loading())
    val products = _products.asStateFlow()


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

    init {
        getProducts()
    }

}