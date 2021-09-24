package com.sunnyweather.android.logic

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.lifecycle.liveData
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.dao.DiaryDao
import com.sunnyweather.android.logic.dao.PlaceDao
import com.sunnyweather.android.logic.model.Diary
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Places
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import com.sunnyweather.android.ui.diary.DiaryEditActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    /*Diary*/
    fun saveDiary(diary: Diary) = DiaryDao.saveDiary(diary)

    fun searchDiary(titleText: String) = DiaryDao.searchDiary(titleText)

    fun updateDiary(diary: Diary) = DiaryDao.updateDiary(diary)

    fun deleteDiary(date: String) = DiaryDao.deleteDiary(date)

    fun getSavedDiary(): ArrayList<Diary> = DiaryDao.getSavedDiary()

    /*Place*/
    fun savePlace(place: Places) = PlaceDao.savePlace(place)

    fun isPlaceSaved(place: Places) = PlaceDao.isPlaceSaved(place)

    fun getSavePlace() = PlaceDao.getSavedPlace()

    fun deletePlace(placeName: String) = PlaceDao.deletePlace(placeName)

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if(placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    /*Weather*/
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}