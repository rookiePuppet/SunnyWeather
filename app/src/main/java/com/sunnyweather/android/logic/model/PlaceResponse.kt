package com.sunnyweather.android.logic.model

import android.location.Location
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: ArrayList<Place>)

data class Place(val name: String, val location: com.sunnyweather.android.logic.model.Location,
            @SerializedName("formatted_address") val address: String)

data class Location(val lng: String, val lat: String)
