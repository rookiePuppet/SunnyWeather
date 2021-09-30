package com.sunnyweather.android.logic.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val key: Int, //帖子唯一键值，用于获取评论
    val posterPN: String, //用户手机号
    val title: String,
    val content: String,
    val time: String,
) : Parcelable