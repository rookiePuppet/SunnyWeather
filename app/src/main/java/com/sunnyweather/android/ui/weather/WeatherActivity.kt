package com.sunnyweather.android.ui.weather

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.ui.place.ForecastAdapter
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var placeName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var currentSky: TextView
    private lateinit var currentAQI: TextView
    private lateinit var nowLayout: ConstraintLayout
    private lateinit var forecastRecyclerView: RecyclerView
    private lateinit var tempRangeText: TextView
    private lateinit var coldRiskText: TextView
    private lateinit var dressingText: TextView
    private lateinit var ultravioletText: TextView
    private lateinit var carWashingText: TextView
    private lateinit var visibilityText: TextView
    private lateinit var apparentTempText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windText: TextView
    private lateinit var weatherLayout: ScrollView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var navBtn: Button
    lateinit var drawerLayout: DrawerLayout

    private lateinit var aqiText: TextView
    private lateinit var aqiDescriptionText: TextView
    private lateinit var pm25Text: TextView
    private lateinit var pm10Text: TextView
    private lateinit var o3Text: TextView
    private lateinit var so2Text: TextView
    private lateinit var no2Text: TextView
    private lateinit var coText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.fitsSystemWindows = true
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_weather)

        placeName = findViewById(R.id.placeName)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentAQI)
        nowLayout = findViewById(R.id.nowLayout)
        forecastRecyclerView = findViewById(R.id.forecastRecyclerView)
        weatherLayout = findViewById(R.id.weatherLayout)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        navBtn = findViewById(R.id.navBtn)
        drawerLayout = findViewById(R.id.drawerLayout)
        tempRangeText = findViewById(R.id.tempRangeText)
        //生活指数
        coldRiskText = findViewById(R.id.coldRiskText)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        visibilityText = findViewById(R.id.visibilityText)
        apparentTempText = findViewById(R.id.apparentTempText)
        humidityText = findViewById(R.id.humidityText)
        windText = findViewById(R.id.windText)

        aqiText = findViewById(R.id.aqiText)
        aqiDescriptionText = findViewById(R.id.aqiDescription)
        pm25Text = findViewById(R.id.pm25Text)
        pm10Text = findViewById(R.id.pm10Text)
        o3Text = findViewById(R.id.o3Text)
        so2Text = findViewById(R.id.so2Text)
        no2Text = findViewById(R.id.no2Text)
        coText = findViewById(R.id.coText)


        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        }
        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        swipeRefresh.isRefreshing = false
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //填充now.xml布局中的数据
        val currentTempText = "${realtime.temperature.toInt()}"
        currentTemp.text = currentTempText
        val maxTemp = "${daily.temperature[0].max.toInt()}"
        val minTemp = "${daily.temperature[0].min.toInt()}"
        tempRangeText.text = "$maxTemp℃ / $minTemp℃"
        currentSky.text = getSky(realtime.skycon).info
        currentAQI.text = "空气${realtime.airQuality.description.chn}"
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        //填充air_quality.xml布局中的数据
        val aqi = realtime.airQuality.aqi.chn.toInt()
        val aqiDescription = realtime.airQuality.description.chn
        val pm25 = realtime.airQuality.pm25
        val pm10 = realtime.airQuality.pm10
        val o3 = realtime.airQuality.o3
        val so2 = realtime.airQuality.so2
        val no2 = realtime.airQuality.no2
        val co = realtime.airQuality.co
        aqiText.text = aqi.toString()
        aqiDescriptionText.text = aqiDescription
        pm25Text.text = pm25.toString()
        pm10Text.text = pm10.toString()
        o3Text.text = o3.toString()
        so2Text.text = so2.toString()
        no2Text.text = no2.toString()
        coText.text = co.toString()
        //填充forecast.xml布局中的数据
        val adapter = ForecastAdapter(daily)
        forecastRecyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        forecastRecyclerView.layoutManager = layoutManager
        /*forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout, false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
            val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }*/
        //填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc

        visibilityText.text = realtime.visibility.toInt().toString()
        apparentTempText.text = "${realtime.visibility.toInt()}℃"
        humidityText.text = "${(realtime.humidity*100).toInt()}%"
        windText.text = "null"
        weatherLayout.visibility = View.VISIBLE
    }

}