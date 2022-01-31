package com.ayubo.life.ayubolife.kotlin_utility

import java.text.SimpleDateFormat

public class CommonUtility {


   public fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return df.parse(date).time
    }
}