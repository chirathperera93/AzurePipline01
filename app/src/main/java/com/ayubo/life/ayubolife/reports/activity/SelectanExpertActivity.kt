package com.ayubo.life.ayubolife.reports.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.activity.PaymentSettingsActivity
import com.ayubo.life.ayubolife.payment.vm.EnterDialogPinNumberActivityVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.vm.SelectanExpertActivityVM
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import javax.inject.Inject

class SelectanExpertActivity : BaseActivity() {


    @Inject
    lateinit var selectanExpertActivityVM: SelectanExpertActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectan_expert)

        App.getInstance().appComponent.inject(this)

        getExperts()
    }

    private fun getExperts(){

        val jsonStr = "{" +
                "\"userid\": \"" + "16070bb5-0285-38f0-a90e-59b7be8ac4c2" + "\"" +
                "}"

        subscription.add(selectanExpertActivityVM.getExpertData(jsonStr).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        val intent = Intent(this, PaymentSettingsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )
    }

}
