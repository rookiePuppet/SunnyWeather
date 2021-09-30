package com.sunnyweather.android.logic.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Diary(val date: String, val title: String, val content: String) : Parcelable