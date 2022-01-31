package com.ayubo.life.ayubolife.prochat.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat

/**
 * Created by Admin on 3/23/2018.
 */
const val CALL_PERMISSION = 1000
class CallPhone(val context:Context) {
    private lateinit var number: String

    fun reQuestCall(number: String) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.number = number
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.CALL_PHONE), CALL_PERMISSION)
            return
        }

        call(number)
    }

    @SuppressLint("MissingPermission")
    fun call(number:String){
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + number)
        context.startActivity(callIntent)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CALL_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (number != null) {
                        reQuestCall(number!!)
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }
}