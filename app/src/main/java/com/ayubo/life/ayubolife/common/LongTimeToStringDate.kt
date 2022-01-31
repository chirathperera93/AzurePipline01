package com.ayubo.life.ayubolife.common

import java.text.SimpleDateFormat
import java.util.*

class LongTimeToStringDate {

    fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val simpleDateFormatForDate = SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormatForDate.format(date)
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val simpleDateFormatForDate = SimpleDateFormat("dd MM yyyy hh:mm aaa");
        return simpleDateFormatForDate.format(date)
    }
}