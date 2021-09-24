package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.place.StatusViewModel.Companion.STATUS_MANAGE

class PlaceManageActivity : AppCompatActivity() {

    val statusViewModel by lazy { ViewModelProvider(this, StatusViewModelFactory(placeSavedAdapter)).get(StatusViewModel::class.java) }

    val placeSavedViewModel: PlaceSavedViewModel by viewModels()

    lateinit var placeSavedRecyclerView: RecyclerView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private var check: MenuItem? = null
    private lateinit var placeSavedAdapter: PlaceSavedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_manage)
        /*设置Toolbar*/
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /*配置RecyclerView*/
        placeSavedRecyclerView = findViewById(R.id.placeSavedRecyclerView)
        placeSavedAdapter = PlaceSavedAdapter(placeSavedViewModel.placeList.value!!, this)
        placeSavedRecyclerView.adapter = placeSavedAdapter
        val layoutManager = LinearLayoutManager(this)
        placeSavedRecyclerView.layoutManager = layoutManager
        /*配置DrawerLayout*/
        drawerLayout = findViewById(R.id.drawerLayout)
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(INPUT_METHOD_SERVICE) as
                        InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                placeSavedAdapter.refreshPlaceList()
            }

            override fun onDrawerStateChanged(newState: Int) {}

        })
        /*配置FloatingActionButton*/
        floatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        /*切换界面*/
        statusViewModel.statusLiveData.observe(this) {
            if (statusViewModel.statusLiveData.value == STATUS_MANAGE) {
                toolbar.title = "管理城市"
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
                check?.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
            } else {
                toolbar.title = "编辑城市"
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
                check?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        check = menu.findItem(R.id.check)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (statusViewModel.statusLiveData.value == STATUS_MANAGE) {
                    finish()
                } else {
                    placeSavedAdapter.apply {
                        placeList.clear()
                        placeList.addAll(placeSavedViewModel.getSavedPlace())
                        notifyDataSetChanged()
                    }
                    placeSavedViewModel.deletedPlaceList.clear()
                    placeSavedViewModel.deletedPositionList.clear()
                    statusViewModel.transformStatus()
                    //取消更改
                }
            }
            R.id.check -> {
                if (placeSavedViewModel.deletedPlaceList.isNotEmpty()) {
                    val deletedPlace = StringBuilder().apply {
                        for (place in placeSavedViewModel.deletedPlaceList) {
                            append(place.name)
                            append("，")
                        }
                        deleteCharAt(this.lastIndex)
                    }
                    Toast.makeText(this, "你删除了$deletedPlace", Toast.LENGTH_SHORT).show()
                    Log.d("SunnyWeather", placeSavedViewModel.deletedPositionList.toString())
                    val intent = Intent().putIntegerArrayListExtra("deleteInfo", placeSavedViewModel.deletedPositionList)
                    setResult(RESULT_OK, intent)

                }
                statusViewModel.transformStatus()
                //保存更改
            }
        }
        return true
    }

}
