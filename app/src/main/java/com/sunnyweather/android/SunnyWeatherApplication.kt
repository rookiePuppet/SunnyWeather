package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.sunnyweather.android.logic.database.MyDatabaseHelper

class SunnyWeatherApplication : Application() {

    companion object {

        const val TOKEN = "8OUwXDF3YiNGfzby"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val DATABASE_VERSION = 1

        @SuppressLint("StaticFieldLeak")
        lateinit var dbHelper: MyDatabaseHelper

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        dbHelper = MyDatabaseHelper(this, "AppDatabase.db", DATABASE_VERSION)
    }

}