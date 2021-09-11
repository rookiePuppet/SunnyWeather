package com.sunnyweather.android.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatusViewModelFactory(private val placeSavedAdapter: PlaceSavedAdapter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = StatusViewModel(placeSavedAdapter) as T

}