package com.journeyfortech.e_commerce.viewModel

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    private val _cart = MutableStateFlow<Resource<List<Cart>>>(Resource.Loading())
    val cart = _cart.asStateFlow()

    private val _totalPrice = MutableStateFlow<String>("")
    val totalPrice = _totalPrice.asStateFlow()

    init {
        getAllCart()
    }

    private fun getAllCart() = viewModelScope.launch {
        repository.getAllCart
            .onStart {
                _cart.value = Resource.Loading()
            }.catch { e ->
                _cart.value = Resource.Error(e.toString())
            }.collect { response ->
                _cart.value = Resource.Success(response)
            }
    }


    fun deleteCartById(id: Int) = viewModelScope.launch {
        repository.deleteCartById(id)
    }

    private fun updateCart(cart: Cart) = viewModelScope.launch {
        repository.updateCartQuantity(cart)
    }


    fun totalPrice(cart: MutableList<Cart>) {
        viewModelScope.launch {
            var price = 0
            for (i in 0 until cart.size - 1) {
                val currentElement = cart[i]
                val findCartItemId = repository.getCartItem(currentElement.id!!)
                findCartItemId.collect {
                    price += currentElement.price?.toInt()?.times(it.quantity!!)!!
                }
            }
            _totalPrice.value = price.toString()
        }

    }

    fun quantityIncrement(id: Int, quantity: TextView) {
        viewModelScope.launch {
            repository.getCartItem(id).collect {
                it.quantity!!.plus(1)
                quantity.text = it.quantity.toString()
                updateCart(it)
            }
        }
    }

    fun quantityDecrease(id: Int, quantity: TextView) {
        viewModelScope.launch {
            repository.getCartItem(id).collect {
                if (it.quantity!! > 1) {
                    it.quantity!!.minus(1)
                    quantity.text = it.quantity.toString()
                }
                updateCart(it)
            }
        }
    }

}

