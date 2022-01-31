package com.ayubo.life.ayubolife.prochat.common

import android.content.SharedPreferences
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.prochat.data.model.User
import com.google.gson.Gson
import java.net.MalformedURLException
import java.net.URL
import java.util.*


enum class SharedPreferenceKeys {
    AUTH_TOKEN,
    CURRENT_USER,
    CURRENT_SCREEN,
    PUSH_TOKEN
}


fun SharedPreferences.save(key: SharedPreferenceKeys, value: Float) {
    edit().putFloat(key.name, value).apply()
}

fun SharedPreferences.save(key: SharedPreferenceKeys, value: String) {
    edit().putString(key.name, value).apply()
}

fun SharedPreferences.save(key: SharedPreferenceKeys, value: Int) {
    edit().putInt(key.name, value).apply()
}

fun SharedPreferences.getCurrentUser(): User {
    val user = User()
    user.userId = getString("uid", null)
    user.firstName = getString("name", null)
    user.email = getString("email", null)
    user.phone = getString("mobile", null)
    return user
}

fun SharedPreferences.saveCurrentUser(from: User) {
    val gson = Gson()
    val jsonAdapter = gson.getAdapter(User::class.java)
    val data = jsonAdapter.toJson(from)
    save(SharedPreferenceKeys.CURRENT_USER, data)
}

fun SharedPreferences.getAuthToken(): String = getString("key_user_token", "").toString()
fun SharedPreferences.getAuthTokenWithBearer(): String =
    "Bearer ".plus(getString("key_user_token", ""))

fun SharedPreferences.clear() {
    this.edit().clear().commit()
}

//view group
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun Long.convertReadableDate(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000L
    return DateFormat.format("yyyy-MM-dd hh:mm a", cal).toString().toUpperCase()
}

fun Long.convertReadableTime(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000L

    return DateFormat.format("HH:mm a", cal).toString().toUpperCase()
}


fun String.getFileNameFromURL(): String {
    val url = this
    if (url == null) {
        return ""
    }
    try {
        val resource = URL(url)
        val host = resource.getHost()
        if (host.length > 0 && url.endsWith(host)) {
            // handle ...example.com
            return ""
        }
    } catch (e: MalformedURLException) {
        return ""
    }

    val startIndex = url.lastIndexOf('/') + 1
    val length = url.length

    // find end index for ?
    var lastQMPos = url.lastIndexOf('?')
    if (lastQMPos == -1) {
        lastQMPos = length
    }

    // find end index for #
    var lastHashPos = url.lastIndexOf('#')
    if (lastHashPos == -1) {
        lastHashPos = length
    }

    // calculate the end index
    val endIndex = Math.min(lastQMPos, lastHashPos)
    return url.substring(startIndex, endIndex)
}