package com.ayubo.life.ayubolife.janashakthionboarding.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.experts.ExpertsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.intro.JanashakthiIntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.webview.JanashakthiWebViewActivity
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS
import kotlinx.android.synthetic.main.activity_janashakthi_web_view.*
import kotlinx.android.synthetic.main.activity_janashakthi_welcome.*

class JanashakthiWelcomeActivity : AppCompatActivity() {
    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, JanashakthiWelcomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_janashakthi_welcome)

        btnBeginJourney.setOnClickListener {
            JanashakthiIntroActivity.startActivity(this)
            // IntroActivity.startActivity(this)
            finish()
        }

//        chkAcceptTerms.setOnCheckedChangeListener { compoundButton, b ->
//            btnBeginJourney.isEnabled = b
//        }
//

    }

    fun onTermsAndConditionClicked(view: View) {
        //  JanashakthiWebViewActivity.startActivity(this, view.tag as String)
        val intent = Intent(this, CommonWebViewActivity::class.java)
        intent.putExtra("URL", MAIN_URL_APPS + "static/janashakthi/terms")
        this.startActivity(intent)

    }
}
