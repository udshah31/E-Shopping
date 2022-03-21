package com.journeyfortech.e_commerce.viewModel

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.Cart
import com.journeyfortech.e_commerce.data.db.Products
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import com.journeyfortech.e_commerce.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    private val _cart = MutableStateFlow<Resource<List<Cart>>>(Resource.Loading())
    val cart = _cart.asStateFlow()

    private val _allPrice = MutableStateFlow<String>("")
    val allPrice = _allPrice.asStateFlow()

    init {
        getAllCart()
    }

    fun findItemWithIds(ids: List<Int>) = repository.findItemsWithIds(ids)

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

    fun updateAllFavouriteToFalse() = viewModelScope.launch {
        repository.updateAllFavouriteToFalse()
    }

    fun insertProduct(products: Products) = viewModelScope.launch {
        repository.insetProduct(products)
    }

    fun deleteProduct(products: ProductResponseItem) = viewModelScope.launch {
        repository.deleteProduct(products)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }



    private suspend fun findCartItemId(id: Int): Cart? {
        return repository.findCartItemId(id)
    }

    fun deleteCartItem(cart: Cart) = viewModelScope.launch {
        repository.deleteCartItem(cart)
    }

    fun deleteAllCart() = viewModelScope.launch { repository.deleteAllCart() }


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

    fun handlePriceBasedOnQuantity(
        id: Int,
        price: String,
        priceTextView: TextView,
        cartQuantityTextView: TextView
    ) {

        viewModelScope.launch {
            val quantity = findCartItemId(id)?.cartQuantity ?: 0
            val textFormat = "Price : ${price.toInt().times(quantity)}"
            priceTextView.text = textFormat
            cartQuantityTextView.text = quantity.toString()
        }

    }

    fun handleTotalPriceBasedListSize(cartList: MutableList<ProductResponseItem>) {
        viewModelScope.launch {
            var price = 0
            for (i in 0 until cartList.size) {
                val currentElement = cartList[i]
                val findCartItemId = findCartItemId(currentElement.id)
                if (findCartItemId != null) {
                    val quantity = findCartItemId.cartQuantity
                    price += currentElement.price.toInt() * quantity
                }
            }
            _allPrice.value = price.toString().plus(" Ron")
        }
    }


     fun handleIncrementButtonBasedOnQuantity(id: Int, quantityTextView: TextView) {
         viewModelScope.launch {
             val cart = repository.findCartItemId(id)
             val product = repository.findCartItemId(id)
             if (cart?.cartQuantity ?: 0 < product?.cartQuantity!!) {
                 cart!!.cartQuantity.plus(1)
                 withContext(Dispatchers.Main) {
                     quantityTextView.text = cart.cartQuantity.toString()
                 }
                 repository.updateCart(cart)
             }
         }
     }
    fun handleDecrementButton(id: Int, quantityTextView: TextView) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            if (cart!!.cartQuantity > 1) {
                cart.cartQuantity.minus(1)
                withContext(Dispatchers.Main) {
                    quantityTextView.text = cart.cartQuantity.toString()
                }
                repository.updateCart(cart)
            }
        }
    }

     fun buyButtonAction(cartList: MutableList<ProductResponseItem>, actionAfterDone: () -> Unit) {
         viewModelScope.launch(Dispatchers.Default) {
             for (i in 0 until cartList.size) {
                 val currentElementCart = cartList[i]
                 val cartItem = findCartItemId(currentElementCart.id)
                 val product = repository.findItemWithId(currentElementCart.id)
                 val quantity = cartItem?.cartQuantity
                 product?.quantity = currentElementCart.quantity.minus(quantity!!)
                 repository.updateProductEntity(product!!)
             }
             repository.deleteAllCart()
             withContext(Dispatchers.Main) {
                 actionAfterDone()
             }
         }
     }
}