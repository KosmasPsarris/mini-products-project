package com.kosmas.tecprin.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun String.reformatDate(): String? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let {
            outputFormat.format(it)
        } ?: run {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
