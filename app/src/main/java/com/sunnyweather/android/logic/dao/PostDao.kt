package com.sunnyweather.android.logic.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Post

object PostDao {

    private val db = SunnyWeatherApplication.dbHelper.writableDatabase

    fun savePost(post: Post) {
        val values = ContentValues().apply {
            put("key", post.key)
            put("posterPN", post.posterPN)
            put("title", post.title)
            put("content", post.content)
            put("time", post.time)
        }
        db.insert("Post", null, values)
    }

    @SuppressLint("Range")
    fun isKeyExisted(key: Int): Boolean {
        val cursor = db.query("Post", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val aKey = cursor.getInt(cursor.getColumnIndex("key"))
                if (aKey == key) {
                    return true
                }
            } while (cursor.moveToNext())
        }
        return false
    }

    @SuppressLint("Range")
    fun searchPostByKey(key: Int): Post? {
        var post: Post? = null
        val cursor = db.query("Post", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val aKey = cursor.getInt(cursor.getColumnIndex("key"))
                if (aKey != key) {
                    continue
                }
                val posterPN = cursor.getString(cursor.getColumnIndex("posterPN"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                post = Post(aKey, posterPN, title, content, time)
            } while (cursor.moveToNext())
        }
        return post
    }

    @SuppressLint("Range")
    fun searchPostByTitle(title: String): ArrayList<Post> {
        val result = ArrayList<Post>()
        val cursor = db.query("Post", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val aTitle = cursor.getString(cursor.getColumnIndex("title"))
                if (!aTitle.contains(title)) {
                    continue
                }
                val posterPN = cursor.getString(cursor.getColumnIndex("posterPN"))
                val key = cursor.getInt(cursor.getColumnIndex("key"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val post = Post(key, posterPN, aTitle, content, time)
                result.add(post)
            } while (cursor.moveToNext())
        }
        return result
    }

    @SuppressLint("Range")
    fun getSavedPost(): ArrayList<Post> {
        val result = ArrayList<Post>()
        val cursor = db.query("Post", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val posterPN = cursor.getString(cursor.getColumnIndex("posterPN"))
                val key = cursor.getInt(cursor.getColumnIndex("key"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val time = cursor.getString(cursor.getColumnIndex("time"))
                val post = Post(key, posterPN, title, content, time)
                result.add(post)
            } while (cursor.moveToNext())
        }
        return result
    }

    fun deletePostByKey(key: Int) {
        db.delete("Post", "key = ?", arrayOf(key.toString()))
    }

    fun clearAllPost() {
        db.execSQL("delete from Post")
    }


}