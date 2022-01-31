package com.ayubo.life.ayubolife.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.ayubo.life.ayubolife.R
import com.facebook.appevents.AppEventsLogger
import com.flavors.changes.Constants
import kotlinx.android.synthetic.main.new_get_started.*

class NewGetStartedActivity : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window;

            var background: Drawable = resources.getDrawable(R.drawable.ayubo_get_started_bg);

            background = if (Constants.type == Constants.Type.LIFEPLUS) {
                resources.getDrawable(R.drawable.life_plus_get_started_bg);
            } else {
                resources.getDrawable(R.drawable.ayubo_get_started_bg);
            }


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = resources.getColor(android.R.color.transparent);
            window.navigationBarColor = resources.getColor(android.R.color.transparent);
            window.setBackgroundDrawable(background);
        }


        setContentView(R.layout.new_get_started)


        if (Constants.type == Constants.Type.LIFEPLUS) {
            logo_image.setImageResource(R.drawable.life_plus_logo)
            get_started_description.setText(R.string.get_started_description_life_plus)
            get_started_button.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            logo_image.setImageResource(R.drawable.correct_ayubo_logo)
            get_started_description.setText(R.string.get_started_description_ayubo_life)
            get_started_button.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }


        get_started_button.setOnClickListener {
            val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(baseContext);
            loggerFB.logEvent("User_GetStarted ")
            val intent: Intent = Intent(this, EnterMobileNumberActivity::class.java);
            startActivity(intent);
            finish();
        }


    }
}