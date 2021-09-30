package com.sunnyweather.android.logic.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Diary

object DiaryDao {

    private val db = SunnyWeatherApplication.dbHelper.writableDatabase

    fun saveDiary(diary: Diary) {
        val values = ContentValues().apply {
            put("date", diary.date)
            put("title", diary.title)
            put("content", diary.content)
        }
        db.insert("Diary", null, values)
    }

    fun updateDiary(diary: Diary) {
        val values = ContentValues().apply {
            put("title", diary.title)
            put("content", diary.content)
        }
        db.update("Diary", values, "date = ?", arrayOf(diary.date))
    }

    fun deleteDiary(date: String) {
        db.delete("Diary", "date = ?", arrayOf(date))
    }

    @SuppressLint("Range")
    fun searchDiary(titleText: String): ArrayList<Diary> {
        val diaryList = ArrayList<Diary>()
        val cursor = db.query("Diary", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndex("title"))
                if (!title.contains(titleText)) {
                    continue
                }
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val diary = Diary(date, title, content)
                diaryList.add(diary)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return diaryList
    }

    @SuppressLint("Range")
    fun getSavedDiary(): ArrayList<Diary> {
        val diaryList = ArrayList<Diary>()
        val cursor = db.query("Diary", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val diary = Diary(date, title, content)
                diaryList.add(diary)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return diaryList
    }

    fun clearAllDiary() {
        db.execSQL("delete from Diary")
    }

}