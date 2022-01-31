package com.ayubo.life.ayubolife.janashakthionboarding.title_clicked

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R

class getAgreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_agree_)
    }

    fun clickToFinish(view: View){
        finish()
    }
}
