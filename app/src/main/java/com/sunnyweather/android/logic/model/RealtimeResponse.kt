package com.sunnyweather.android.logic.model

import android.view.Window
import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime(
        val skycon: String,
        val temperature: Float,
        val humidity: Float,
        val visibility: Float,
        val wind: Wind,
        @SerializedName("apparent_temperature") val apparentTemperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality
    )

    data class AirQuality(
        val aqi: AQI, val pm25: Float, val pm10: Float,
        val o3: Float, val so2: Float, val no2: Float, val co: Float, val description: Description
    )

    data class Wind(val speed: Float, val direction: Float)

    data class AQI(val chn: Float)

    data class Description(val chn: String)

}