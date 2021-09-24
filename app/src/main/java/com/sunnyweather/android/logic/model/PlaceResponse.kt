package com.sunnyweather.android.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: ArrayList<Place>)

data class Place(val name: String, val location: Location,
            @SerializedName("formatted_address") val address: String)

data class Location(val lng: String, val lat: String)
