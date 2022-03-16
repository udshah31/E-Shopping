package com.journeyfortech.e_commerce.ui

import androidx.appcompat.app.AppCompatActivity
import com.journeyfortech.e_commerce.utils.UiCommunicationListener

abstract class BaseActivity:AppCompatActivity(), UiCommunicationListener {

    abstract override fun displayProgressBar(isLoading: Boolean)
}