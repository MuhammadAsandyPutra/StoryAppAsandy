package com.example.storyappasandy.data.utils

import java.text.SimpleDateFormat

class Utils {
    companion object {
        private val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        private val desiredDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

        fun formatApiDateToDesiredFormat(apiDate: String): String {
            return try {
                val date = apiDateFormat.parse(apiDate)
                desiredDateFormat.format(date)
            } catch (e: Exception) {

                apiDate
            }
        }
    }

}