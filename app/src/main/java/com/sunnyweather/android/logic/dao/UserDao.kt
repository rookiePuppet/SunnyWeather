package com.sunnyweather.android.logic.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.JsonWriter
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.User
import com.sunnyweather.android.toList
import java.io.ByteArrayOutputStream

object UserDao {

    private val db = SunnyWeatherApplication.dbHelper.writableDatabase

    fun saveUser(user: User) {
        val values = ContentValues().apply {
            put("username", user.username)
            put("password", user.password)
            put("phoneNumber", user.phoneNumber)
            put("image", bitmapToByte(user.image))
            put("publishedList", user.publishedList)
            put("likedList", user.likedList)
            put("staredList", user.staredList)
        }
        db.insert("User", null, values)
    }

    fun addPublished(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val publishedList = ArrayList<Int>()
        val userList = user.publishedList.toList()
        if(userList.contains(postKey)) return false
        if (userList.isNotEmpty()) publishedList.addAll(userList)
        publishedList.add(postKey)
        val values = ContentValues().apply {
            put("publishedList", publishedList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    fun removePublished(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val publishedList = ArrayList<Int>()
        val userList = user.publishedList.toList()
        if (userList.isEmpty()) {
            return false
        }
        publishedList.addAll(userList)
        publishedList.remove(postKey)
        val values = ContentValues().apply {
            put("publishedList", publishedList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    fun addStared(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val staredList = ArrayList<Int>()
        val userList = user.staredList.toList()
        if (userList.contains(postKey)) return false
        if (userList.isNotEmpty()) staredList.addAll(userList)
        staredList.add(postKey)
        val values = ContentValues().apply {
            put("staredList", staredList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    fun removeStared(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val staredList = ArrayList<Int>()
        val userList = user.staredList.toList()
        if (userList.isEmpty()) {
            return false
        }
        staredList.addAll(userList)
        staredList.remove(postKey)
        val values = ContentValues().apply {
            put("staredList", staredList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    fun addLiked(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val likedList = ArrayList<Int>()
        val userList = user.likedList.toList()
        if (userList.contains(postKey)) return false
        if (userList.isNotEmpty()) likedList.addAll(userList)
        likedList.add(postKey)
        val values = ContentValues().apply {
            put("likedList", likedList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    fun removeLiked(userPN: String, postKey: Int): Boolean {
        val user = searchUserByPhoneNumber(userPN) ?: return false
        val likedList = ArrayList<Int>()
        val userList = user.likedList.toList()
        if (userList.isEmpty()) {
            return false
        }
        likedList.addAll(userList)
        likedList.remove(postKey)
        val values = ContentValues().apply {
            put("likedList", likedList.toString())
        }
        db.update("User", values, "phoneNumber = ?", arrayOf(userPN))
        return true
    }

    @SuppressLint("Range")
    fun deleteUserByPhoneNumber(pn: String) {
        db.delete("User", "phoneNumber = ?", arrayOf(pn))
    }

    @SuppressLint("Range")
    fun searchUserByPhoneNumber(pn: String): User? {
        var user: User? = null
        val cursor = db.query("User", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"))
                if (phoneNumber != pn) {
                    continue
                }
                val userName = cursor.getString(cursor.getColumnIndex("username"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val imageBlob: ByteArray = cursor.getBlob(cursor.getColumnIndex("image"))
                val image = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.size)
                val publishedList = cursor.getString(cursor.getColumnIndex("publishedList"))
                val likedList = cursor.getString(cursor.getColumnIndex("likedList"))
                val staredList = cursor.getString(cursor.getColumnIndex("staredList"))
                user = User(
                    userName,
                    password,
                    phoneNumber,
                    image,
                    publishedList,
                    likedList,
                    staredList
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return user
    }

    fun clearAllUser() {
        db.execSQL("delete from User")
    }

    private fun bitmapToByte(bitmap: Bitmap): ByteArray {
        val bAOS = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bAOS)
        return bAOS.toByteArray()
    }

}