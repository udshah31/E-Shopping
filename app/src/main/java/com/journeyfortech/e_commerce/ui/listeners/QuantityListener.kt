package com.journeyfortech.e_commerce.ui.listeners

import android.widget.TextView

interface QuantityListener {
    fun onQuantityAdded(id: Int, quantityTextView: TextView)
    fun onQuantityRemoved(id: Int, quantityTextView: TextView)
}