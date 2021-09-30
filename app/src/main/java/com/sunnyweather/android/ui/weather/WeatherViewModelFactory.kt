package com.sunnyweather.android.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Places

class WeatherViewModelFactory(private val place: Places) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = WeatherViewModel(place) as T

}