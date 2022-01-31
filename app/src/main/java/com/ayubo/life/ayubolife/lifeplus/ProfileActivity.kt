package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.fragments.CircleTransform
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentSettingsActivity
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY
import com.ayubo.life.ayubolife.utility.Ram
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile2.*
import java.util.*

class ProfileActivity : BaseActivity() {


    lateinit var pref: PrefManager


    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        pref = PrefManager(this)

        val rand = Random()
        val n = rand.nextInt(50) + 1
        val rannum = Integer.toString(n)

        val imagepath_db = pref.getLoginUser().get("image_path")


        val burlImg = MAIN_URL_LIVE_HAPPY + "$imagepath_db&cache=$rannum"

        val name = pref.loginUser["name"]
        val mobile = pref.getLoginUser().get("mobile")

        txt_name.text = name
        txt_mobilenumber.text = mobile

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transforms(CircleTransform(this@ProfileActivity))
        Glide.with(this@ProfileActivity).load(burlImg)
            .thumbnail(0.5f)
            .apply(requestOptions)
            .into(img_profile)


        lay_lifepoints.setOnClickListener {
            val inl = Intent(this, LifePointActivity::class.java)
            startActivity(inl)
        }

        lay_reports.setOnClickListener {
            val intent = Intent(this, ReportDetailsActivity::class.java)
            intent.putExtra("data", "all")
            Ram.setReportsType("fromHome")
            startActivity(intent)
        }

        lay_payment.setOnClickListener {
            val intentPay = Intent(this, PaymentSettingsActivity::class.java)
            startActivity(intentPay)
        }

        lay_lifepoints.setOnClickListener {
            val inl = Intent(this, LifePointActivity::class.java)
            startActivity(inl)
        }
        lay_subscription.setOnClickListener {
            val intent = Intent(this, NativePostActivity::class.java)
            intent.putExtra("meta", "384")
            startActivity(intent)
        }

//        val crashButton = Button(this)
//        crashButton.text = "Crash!"
//        crashButton.setOnClickListener {
//            Crashlytics.getInstance().crash() // Force a crash
//        }
//
//        addContentView(crashButton, ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT))

    }

}