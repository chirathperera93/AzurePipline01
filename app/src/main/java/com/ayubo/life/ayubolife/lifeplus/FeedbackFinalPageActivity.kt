package com.ayubo.life.ayubolife.lifeplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ayubo.life.ayubolife.R
import kotlinx.android.synthetic.main.feedback_final_page.*

class FeedbackFinalPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_final_page)

        back_to_home.setOnClickListener {
            finish();
        }
    }
}