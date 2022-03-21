package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Products
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    private val _favourite = MutableStateFlow<Resource<List<Products>>>(Resource.Loading())
    val favourite = _favourite.asStateFlow()


    init {
        getAllCart()
    }

    private fun getAllCart() = viewModelScope.launch {
        repository.getAllProducts
            .onStart {
                _favourite.value = Resource.Loading()
            }.catch { e->
                _favourite.value = Resource.Error(e.toString())
            }.collect { response ->
                _favourite.value = Resource.Success(response)
            }
    }



}