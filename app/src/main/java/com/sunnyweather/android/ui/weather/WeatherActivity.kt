package com.sunnyweather.android.ui.weather

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.ui.place.PlaceManageActivity
import com.sunnyweather.android.ui.place.PlaceSavedViewModel

class WeatherActivity : AppCompatActivity() {

    private val placeSavedViewModel: PlaceSavedViewModel by viewModels()

    private val startActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.d("SunnyWeather", "ok")
                if (it.data != null) {
                    val currentPosition = it.data!!.getIntExtra("position", 0)
                    viewPager.currentItem = currentPosition

                    val deletedPosition = it.data!!.getIntArrayExtra("deleteInfo")
                    if (deletedPosition != null) {
                        for (position in deletedPosition) {
                            Log.d("SunnyWeather", position.toString())
                            viewPagerAdapter.removePage(position)
                        }
                    }
                }
            }
        }

    private lateinit var navBtn: Button
    private lateinit var viewPager: ViewPager2

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.fitsSystemWindows = true
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        if(placeSavedViewModel.getSavedPlace().isEmpty()) {
            val intent = Intent(this, PlaceManageActivity::class.java)
            startActivityLauncher.launch(intent)
        }
        setContentView(R.layout.activity_weather)
        /*配置ViewPager*/
        viewPagerAdapter = ViewPagerAdapter(getPageList(), this)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = viewPagerAdapter
        /*配置topLayout*/
        navBtn = findViewById(R.id.navBtn)
        navBtn.setOnClickListener {
            val intent = Intent(this, PlaceManageActivity::class.java)
            startActivityLauncher.launch(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        viewPagerAdapter.refreshPage(placeSavedViewModel.getSavedPlace())
    }

    private fun getPageList(): ArrayList<WeatherFragment> {
        val pageList = ArrayList<WeatherFragment>()
        for (place in placeSavedViewModel.getSavedPlace()) {
            pageList.add(WeatherFragment(place))
        }
        return pageList
    }

}