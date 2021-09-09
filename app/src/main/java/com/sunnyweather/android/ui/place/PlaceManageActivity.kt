package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.dao.PlaceDao

class PlaceManageActivity : AppCompatActivity() {

    private lateinit var placeSavedRecyclerView: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_manage)
        /*设置Toolbar*/
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        /*配置RecyclerView*/
        placeSavedRecyclerView = findViewById(R.id.placeSavedRecyclerView)
        val placeList = PlaceDao.getSavedPlace()
        val placeSavedAdapter = PlaceSavedAdapter(placeList)
        placeSavedRecyclerView.adapter = placeSavedAdapter
        val layoutManager = LinearLayoutManager(this)
        placeSavedRecyclerView.layoutManager = layoutManager
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

}