package com.journeyfortech.e_commerce.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {


    fun insertFav(favourite: Favourite) = viewModelScope.launch {
        repository.insertProduct(favourite)
    }

    fun getAllFav() = repository.getAllFavProducts

    fun deleteFavById(favourite: Favourite) = viewModelScope.launch {
        repository.deleteProduct(favourite)
    }
}