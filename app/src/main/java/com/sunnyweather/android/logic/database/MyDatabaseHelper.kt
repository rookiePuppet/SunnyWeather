package com.sunnyweather.android.logic.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.util.Log

class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version){

    private val createDiary = "create table Diary(" +
            "id integer primary key autoincrement," +
            "date text," +
            "title text," +
            "content text)"

    private val createPlace = "create table Place(" +
            "id integer primary key autoincrement," +
            "name text," +
            "lng text," +
            "lat text)"

    private val createUser = "create table User(" +
            "id integer primary key autoincrement," +
            "username text," +
            "phoneNumber text," +
            "password text," +
            "image blob," +
            "publishedList text," +
            "likedList text," +
            "staredList text)"

    private val createPost = "create table Post(" +
            "id integer primary key autoincrement," +
            "key integer," +
            "posterPN text," +
            "title text," +
            "content text," +
            "time text)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(createDiary)
        p0?.execSQL(createPlace)
        p0?.execSQL(createUser)
        p0?.execSQL(createPost)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}