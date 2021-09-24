package com.sunnyweather.android.logic.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Places
import kotlin.collections.ArrayList as ArrayList

object PlaceDao {

    private val db = SunnyWeatherApplication.dbHelper.writableDatabase

    fun savePlace(place: Places) {
        val values = ContentValues().apply {
            put("name", place.name)
            put("lng", place.lng)
            put("lat", place.lat)
        }
        db.insert("Place", null, values)
    }

    @SuppressLint("Range")
    fun isPlaceSaved(place: Places): Boolean {
        var isPlaceSaved = false
        val cursor = db.query("Place", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                if (name == place.name) {
                    isPlaceSaved = true
                    break
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return isPlaceSaved
    }

    @SuppressLint("Range")
    fun getSavedPlace(): ArrayList<Places> {
        val placeList = ArrayList<Places>()
        val cursor = db.query("Place", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val lng = cursor.getString(cursor.getColumnIndex("lng"))
                val lat = cursor.getString(cursor.getColumnIndex("lat"))
                val place = Places(name, lng, lat)
                placeList.add(place)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return placeList
    }

    fun deletePlace(placeName: String) {
        db.delete("Place", "name = ?", arrayOf(placeName))
    }


}