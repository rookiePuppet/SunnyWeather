package com.sunnyweather.android

import java.lang.StringBuilder

fun String.toList(): List<Int> {
    val list = ArrayList<Int>()
    if (this == "") return list
    val content = StringBuilder(this).apply {
        deleteCharAt(0)
        deleteCharAt(lastIndex)
    }.toString()
    if (content != "") {
        val strList = content.split(", ")
        for (str in strList) {
            list.add(str.toInt())
        }
    }
    return list
}
