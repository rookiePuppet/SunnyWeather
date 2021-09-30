package com.sunnyweather.android.logic.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Blob

@Parcelize
data class User(
    val username: String,
    val password: String,
    val phoneNumber: String,
    val image: Bitmap,
    val publishedList: String,
    val likedList: String,
    val staredList: String
) : Parcelable