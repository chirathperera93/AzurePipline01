package com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Settings
import com.ayubo.life.ayubolife.janashakthionboarding.experts.ExpertsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_final_answer.*

class FinalAnswerActivity : BaseActivity() {
    var settingsObj: Settings? = null

    var pref: PrefManager? = null


    lateinit var action: String
    lateinit var meta: String

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("action")) {
            meta = bundle.getSerializable("action") as String
            action = bundle.getSerializable("action") as String
        }
    }

    companion object {
        fun startActivity(context: Activity, action: String, meta: String) {
            val intent = Intent(context, FinalAnswerActivity::class.java)
            intent.putExtra("action", action)
            intent.putExtra("meta", meta)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_answer)
        App.getInstance().appComponent.inject(this)
        pref = PrefManager(this)

        settingsObj = Ram.getQuestionSetting();

        if (AppConfig.APP_BRANDING_ID == "life_plus") {
            btn_pay.setTextColor(ContextCompat.getColor(this, R.color.black));
            btn_pay.setBackgroundResource(R.drawable.reports_button_green_selected);
        }

        img_ending_heading.text = settingsObj!!.ending_heading
        img_ending_text.text = settingsObj!!.ending_text

        if (settingsObj!!.ending_image.length == 0) {
            img_ending_image.visibility = View.GONE
        } else {
            Glide.with(this).load(settingsObj!!.ending_image).into(img_ending_image)
        }

        readExtras()
    }

    fun clickBack_FinalQuestionActivity(view: View) {


        if (action.isNotEmpty()) {
            processAction(action, meta)
        } else {

            if (pref!!.isFromJanashakthiDyanamic) {
                finish()
            } else {
                if (pref!!.isFromJanashakthiWelcomee) {
                    ExpertsActivity.startActivity(this)
                    finish()
                } else {
                    MedicalUpdateActivity.startActivity(this)
                    finish()

                }
            }
        }


    }

}
