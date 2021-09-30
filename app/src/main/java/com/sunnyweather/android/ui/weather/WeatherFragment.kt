package com.sunnyweather.android.ui.weather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.*
import com.sunnyweather.android.ui.diary.DiaryActivity
import java.text.SimpleDateFormat
import java.util.*

class WeatherFragment(private val place: Places) : Fragment() {

    private val weatherViewModel by lazy { ViewModelProvider(this,
        WeatherViewModelFactory(place)).get(WeatherViewModel::class.java) }

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var weatherLayout: ScrollView
    private lateinit var writeDownBtn: ImageButton
    /*now.xml*/
    private lateinit var placeName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var tempRangeText: TextView
    private lateinit var currentSky: TextView
    private lateinit var currentAQI: TextView
    private lateinit var nowLayout: ConstraintLayout
    /*forecast.xml*/
    private lateinit var forecastRecyclerView: RecyclerView
    /*life_index.xml*/
    private lateinit var coldRiskText: TextView
    private lateinit var dressingText: TextView
    private lateinit var ultravioletText: TextView
    private lateinit var carWashingText: TextView
    private lateinit var visibilityText: TextView
    private lateinit var apparentTempText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windText: TextView
    /*air_quality.xml*/
    private lateinit var aqiText: TextView
    private lateinit var aqiDescriptionText: TextView
    private lateinit var pm25Text: TextView
    private lateinit var pm10Text: TextView
    private lateinit var o3Text: TextView
    private lateinit var so2Text: TextView
    private lateinit var no2Text: TextView
    private lateinit var coText: TextView

    private lateinit var lastUpdateText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherLayout = view.findViewById(R.id.weatherLayout)
        swipeRefresh = view.findViewById(R.id.swipeRefresh)
        writeDownBtn = view.findViewById(R.id.writedownBtn)
        /*now.xml*/
        placeName = view.findViewById(R.id.placeName)
        currentTemp = view.findViewById(R.id.currentTemp)
        tempRangeText = view.findViewById(R.id.tempRangeText)
        currentSky = view.findViewById(R.id.currentSky)
        currentAQI = view.findViewById(R.id.currentAQI)
        nowLayout = view.findViewById(R.id.nowLayout)
        /*forecast.xml*/
        forecastRecyclerView = view.findViewById(R.id.forecastRecyclerView)
        /*life_index.xml*/
        coldRiskText = view.findViewById(R.id.coldRiskText)
        dressingText = view.findViewById(R.id.dressingText)
        ultravioletText = view.findViewById(R.id.ultravioletText)
        carWashingText = view.findViewById(R.id.carWashingText)
        visibilityText = view.findViewById(R.id.visibilityText)
        apparentTempText = view.findViewById(R.id.apparentTempText)
        humidityText = view.findViewById(R.id.humidityText)
        windText = view.findViewById(R.id.windText)
        /*air_quality.xml*/
        aqiText = view.findViewById(R.id.aqiText)
        aqiDescriptionText = view.findViewById(R.id.aqiDescription)
        pm25Text = view.findViewById(R.id.pm25Text)
        pm10Text = view.findViewById(R.id.pm10Text)
        o3Text = view.findViewById(R.id.o3Text)
        so2Text = view.findViewById(R.id.so2Text)
        no2Text = view.findViewById(R.id.no2Text)
        coText = view.findViewById(R.id.coText)

        lastUpdateText = view.findViewById(R.id.lastUpdateText)
        /**/
        weatherViewModel.weatherLiveData.observe(viewLifecycleOwner) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather.realtime, weather.daily)
            } else {
                Toast.makeText(activity, "未能获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        writeDownBtn.setOnClickListener {
            startActivity(Intent(activity?.applicationContext, DiaryActivity::class.java))
        }
    }

    private fun refreshWeather() {
        weatherViewModel.refreshWeather()
        swipeRefresh.isRefreshing = false
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = simpleDateFormat.format(Date(System.currentTimeMillis()))
        lastUpdateText.text = "上次更新时间：${currentTime}"
    }

    private fun showWeatherInfo(realtime: RealtimeResponse.Realtime, daily: DailyResponse.Daily) {
        weatherLayout.visibility = View.VISIBLE
        /*更新now布局*/
        placeName.text = weatherViewModel.place.name
        currentTemp.text = "${realtime.temperature.toInt()}"
        tempRangeText.text = "${daily.temperature[0].max.toInt()} / ${daily.temperature[0].min.toInt()} ℃"
        currentSky.text = getSky(realtime.skycon).info
        currentAQI.text = "空气${realtime.airQuality.description.chn}"
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        /*更新airQuality布局*/
        val aq = realtime.airQuality
        aqiText.text = "${aq.aqi.chn.toInt()}"
        aqiDescriptionText.text = aq.description.chn
        pm25Text.text = "${aq.pm25.toInt()}"
        pm10Text.text = "${aq.pm10.toInt()}"
        o3Text.text = "${aq.o3.toInt()}"
        so2Text.text = "${aq.so2.toInt()}"
        no2Text.text = "${aq.no2.toInt()}"
        coText.text = "${aq.co.toInt()}"
        /*更新forecast布局*/
        val forecastAdapter = ForecastAdapter(daily)
        forecastRecyclerView.adapter = forecastAdapter
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        forecastRecyclerView.layoutManager = layoutManager
        /*更新lifeIndex布局*/
        val dli = daily.lifeIndex
        coldRiskText.text = dli.coldRisk[0].desc
        dressingText.text = dli.dressing[0].desc
        ultravioletText.text = dli.ultraviolet[0].desc
        carWashingText.text = dli.carWashing[0].desc
        visibilityText.text = "${realtime.visibility.toInt()}"
        apparentTempText.text = "${realtime.apparentTemperature.toInt()}℃"
        humidityText.text = "${(realtime.humidity*100).toInt()}%"
        windText.text = "${realtime.wind.speed.toInt()}"
    }

}