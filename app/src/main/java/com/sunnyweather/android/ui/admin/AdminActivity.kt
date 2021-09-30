package com.sunnyweather.android.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.Repository

class AdminActivity : AppCompatActivity() {

    private lateinit var clearPlaceBtn: Button
    private lateinit var clearDiaryBtn: Button
    private lateinit var clearUserBtn: Button
    private lateinit var clearPostBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        clearPlaceBtn = findViewById(R.id.clearPlaceBtn)
        clearDiaryBtn = findViewById(R.id.clearDiaryBtn)
        clearUserBtn = findViewById(R.id.clearUserBtn)
        clearPostBtn = findViewById(R.id.clearPostBtn)

        clearPlaceBtn.setOnClickListener {
            Repository.clearAllPlace()
        }
        clearDiaryBtn.setOnClickListener {
            Repository.clearAllDiary()
        }
        clearUserBtn.setOnClickListener {
            Repository.clearAllUser()
        }
        clearPostBtn.setOnClickListener {
            Repository.clearAllPost()
        }

    }
}