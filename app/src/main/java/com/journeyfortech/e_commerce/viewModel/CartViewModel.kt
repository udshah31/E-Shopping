package com.journeyfortech.e_commerce.viewModel

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.journeyfortech.e_commerce.data.db.CartEntity
import com.journeyfortech.e_commerce.data.db.Favourite
import com.journeyfortech.e_commerce.data.model.product.ProductResponseItem
import com.journeyfortech.e_commerce.repository.ECommerceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ECommerceRepository
) : ViewModel() {

    val getAllProducts: LiveData<List<Favourite>>

    val getAllFavoriteProducts: LiveData<List<Favourite>>

    val getAllCart: LiveData<List<CartEntity>>

    val allPriceLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        getAllProducts = repository.getAllProducts
        getAllFavoriteProducts = repository.getAllFavProducts
        getAllCart = repository.getAllCart
    }

    fun findItemsWithIds(ids: List<Int>) = repository.findItemsWithIds(ids)

    fun updateAllFavouriteTOFalse() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.updateAllFavouriteTOFalse()
        }
    }

    fun insertProduct(favourite: Favourite) {
        viewModelScope.launch (Dispatchers.Default){
            repository.insertProduct(favourite)
        }
    }

    fun deleteProduct(favourite: Favourite) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteProduct(favourite)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAll()
        }
    }

    fun updateProduct(productResponseItem: ProductResponseItem) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.updateProduct(productResponseItem)
        }
    }

    private suspend fun findCartItemId(id: Int): CartEntity? {
        return repository.findCartItemId(id)
    }

    fun deleteCartItem(cartEntity: CartEntity) {
        viewModelScope.launch (Dispatchers.Default){
            repository.deleteCart(cartEntity)
        }
    }

    fun deleteAllCart() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAll()
        }
    }

    fun addOrUpdateCartProduct(id: Int) {
        viewModelScope.launch (Dispatchers.Default){
            val cart = repository.findCartItemId(id)
            val product = repository.findCartItemId(id)

            if (cart?.cartQty ?: 0 < product?.cartQty?.toInt()!!) {
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

    fun handlePriceBasedOnQuantity(
        id: Int,
        price: String,
        priceTextView: TextView,
        cartQuantityTextView: TextView
    ) {

        viewModelScope.launch {
            val quantity = findCartItemId(id)?.cartQty ?: 0
            val textFormat = "Price : ${price.toInt().times(quantity)}"
            priceTextView.text = textFormat
            cartQuantityTextView.text = quantity.toString()
        }

    }

    fun handleTotalPriceBasedListSize(cartList: MutableList<ProductResponseItem>) {
        viewModelScope.launch {
            var price = 0
            for (i in 0..cartList.size - 1) {
                val currentElement = cartList[i]
                val findCartItemId = findCartItemId(currentElement.id)
                if (findCartItemId != null) {
                    val quantity = findCartItemId.cartQty
                    price += currentElement.price.toInt() * quantity
                }
            }
            allPriceLiveData.postValue(price.toString().plus(" Ron"))
        }
    }




    fun handleIncrementButtonBasedOnQuantity(id: Int, quantityTextView: TextView) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            val product = repository.findCartItemId(id)
            if (cart?.cartQty ?: 0 < product?.cartQty!!) {
                cart!!.cartQty.plus(1)
                withContext(Dispatchers.Main) {
                    quantityTextView.text = cart.cartQty.toString()
                }
                repository.updateCart(cart)
            }
        }
    }

    fun handleDecrementButton(id: Int, quantityTextView: TextView) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            if (cart!!.cartQty > 1) {
                cart.cartQty.minus(1)
                withContext(Dispatchers.Main) {
                    quantityTextView.text = cart.cartQty.toString()
                }
                repository.updateCart(cart)
            }
        }
    }

    fun buyButtonAction(cartList: MutableList<ProductResponseItem>, actionAfterDone: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 0..cartList.size - 1) {
                val currentElementCart = cartList[i]
                val cartItem = findCartItemId(currentElementCart.id)
                val product = repository.findItemWithId(currentElementCart.id)
                val quantity = cartItem?.cartQty
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