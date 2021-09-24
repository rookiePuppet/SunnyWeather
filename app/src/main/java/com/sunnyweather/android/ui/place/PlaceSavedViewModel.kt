package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Places

class PlaceSavedViewModel : ViewModel() {

    val placeList = MutableLiveData(getSavedPlace())

    var deletedPlaceList = ArrayList<Places>()

    val deletedPositionList = ArrayList<Int>()

    fun getSavedPlace() = Repository.getSavePlace()

    fun isPlaceSaved(place: Places) = Repository.isPlaceSaved(place)

    fun deletePlace(placeName: String) = Repository.deletePlace(placeName)

    fun savePlace(place: Places) = Repository.savePlace(place)

}