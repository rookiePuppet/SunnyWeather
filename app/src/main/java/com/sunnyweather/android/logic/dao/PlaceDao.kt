package com.sunnyweather.android.logic.dao

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

object PlaceDao {

    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place_" + place.name, Gson().toJson(place))
        }
    }

    fun getSavedPlace(): List<Place> {
        val placeList = ArrayList<Place>()
        val placeJson = sharedPreferences().all
        placeJson.entries.forEach {
            val place = Gson().fromJson(it.value.toString(), Place::class.java)
            placeList.add(place)
        }
        return placeList
    }

    fun isPlaceSaved() = sharedPreferences().all.isNotEmpty()

    fun deletePlace(placeName: String) : Boolean{
        if(sharedPreferences().contains("place_${placeName}")) {
            sharedPreferences().edit().remove(placeName).apply()
            return true
        }
        return false
    }

    private fun sharedPreferences() = SunnyWeatherApplication.context.
            getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

}