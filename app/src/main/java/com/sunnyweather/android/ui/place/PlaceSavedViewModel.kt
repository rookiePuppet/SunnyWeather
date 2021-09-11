package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceSavedViewModel : ViewModel() {

    val placeList = MutableLiveData(getSavedPlace())

    fun deletePlace(placeName: String) = Repository.deletePlace(placeName)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun getSavedPlace(index: Int) = Repository.getSavedPlace(index)

    fun isPlaceSaved() = Repository.isPlaceSaved()

    fun clearPlace() = Repository.clearPlace()

    fun savePlace(place: Place) = Repository.savePlace(place)

}