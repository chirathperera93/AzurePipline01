package com.ayubo.life.ayubolife.twilio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import kotlinx.android.synthetic.main.activity_twillio_call_init.*

class TwillioCallInit : BaseActivity() {

    @Inject
    lateinit var twillioCallInitVM: TwillioCallInitVM

    lateinit var pref: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twillio_call_init)

        App.getInstance().appComponent.inject(this)

        pref = PrefManager(this)

        getConfirmationData()
    }

    companion object {
        fun startActivity(context: Context?) {
            val intent = Intent(context, TwilioHomeActivity::class.java)
            context!!.startActivity(intent)
        }
    }

    private fun getConfirmationData() {
        val userid = pref.loginUser.get("uid")
        // val userid="2c3b1975-ccef-5a0e-1411-5b1e099e612e"
        val appointment_id = "125006bf-b691-603a-9fb1-5ba3316985b4"
        val reciving_user = "16070bb5-0285-38f0-a90e-59b7be8ac4c2"

        val jsonStr = "{" +
                "\"user_id\": \"" + userid + "\"," +
                "\"appointment_id\": \"" + appointment_id + "\"," +
                "\"reciving_user\": \"" + reciving_user + "\"" +
                "}"

        subscription.add(twillioCallInitVM.getTwillioData(jsonStr).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBarTwillio.visibility = View.VISIBLE }
                .doOnTerminate { progressBarTwillio.visibility = View.GONE }
                .doOnError { progressBarTwillio.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                    val mainDataRes = twillioCallInitVM.twillioDataMainResponse

                        if (mainDataRes!!.result == 0) {

//                            TwilioHomeActivity.startActivity(this@TwillioCallInit,mainDataRes.data.callerAccesstoken,appointment_id,"outgoing")

                            finish()
                        }
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    progressBarTwillio.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })

        )
    }
}
