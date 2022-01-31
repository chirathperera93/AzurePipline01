package com.ayubo.life.ayubolife.timeline

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.ask.AskActivity

class TodayTilesActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context?){
            context!!.startActivity(Intent(context, TodayTilesActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_tiles)




    }

}
