package com.sunnyweather.android.ui.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(private val daily: DailyResponse.Daily) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateInfo: TextView = view.findViewById(R.id.dateInfo)
        val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
        val skyInfo: TextView = view.findViewById(R.id.skyInfo)
        val tempInfo: TextView = view.findViewById(R.id.tempInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skycon = daily.skycon[position]
        val simpleDateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        holder.dateInfo.text = transformDate(simpleDateFormat.format(skycon.date))
        holder.skyInfo.text = getSky(skycon.value).info
        holder.skyIcon.setImageResource(getSky(skycon.value).icon)
        val temperature = daily.temperature[position]
        holder.tempInfo.text = "${temperature.min.toInt()}~${temperature.max.toInt()}℃"
    }

    override fun getItemCount(): Int = daily.skycon.size

    private fun transformDate(target: String) : String{
        val day = 24*60*60*1000L
        val simpleDateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val nowTime = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val today = simpleDateFormat.parse(nowTime)
        val target = simpleDateFormat.parse(target)
        Log.d("WeatherActivity", "${today.time - target.time}")
        val result = when(today.time - target.time) {
            0L -> "今天"
            -day -> "明天"
            -2*day -> "后天"
            else -> {
                calendar.time = Date(target.time)
                getWeek(calendar.get(Calendar.DAY_OF_WEEK))
            }
        }
        return result
    }

    private fun getWeek(week: Int) : String =
        when(week) {
            Calendar.SUNDAY -> "周日"
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            Calendar.SATURDAY -> "周六"
            else -> ""
        }

}