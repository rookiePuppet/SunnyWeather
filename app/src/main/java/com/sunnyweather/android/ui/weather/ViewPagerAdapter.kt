package com.sunnyweather.android.ui.weather

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Places
import com.sunnyweather.android.ui.place.PlaceSavedViewModel

class ViewPagerAdapter(private val pageList: ArrayList<WeatherFragment>, fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int) = pageList[position]

    fun refreshPage(placeList: ArrayList<Places>) {
        pageList.clear()
        for (place in placeList) {
            pageList.add(WeatherFragment(place))
        }
        notifyDataSetChanged()
    }

    fun removePage(position: Int) {
        pageList.removeAt(position)
        notifyItemRemoved(position)
    }

}