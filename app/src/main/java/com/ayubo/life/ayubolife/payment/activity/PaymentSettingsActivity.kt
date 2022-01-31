package com.ayubo.life.ayubolife.payment.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.adapter.PaymentSettingsAdapter
import com.ayubo.life.ayubolife.payment.vm.PaymentSettingsVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.utility.Consts
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_payment_options.*
import javax.inject.Inject

class PaymentSettingsActivity : BaseActivity(), PaymentSettingsAdapter.OnItemClickListener {

    override fun onProcessAction(action: String, meta: String) {

        if(action=="addtobill"){
           Ram.setIsFromSetting(true)
        }
        processAction(action,meta)
    }


    @Inject
    lateinit var paymentOptionsActivityVM: PaymentSettingsVM

    private var paymentOptionsAdapter: PaymentSettingsAdapter? = null


    companion object {
        fun startActivity(context: Activity) {
            val intent = Intent(context, PaymentSettingsActivity::class.java)
            context.startActivity(intent)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_options)
        App.getInstance().appComponent.inject(this)

        initView()

        subscription.add(paymentOptionsActivityVM.sendPaymentOptions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        val mlayoutManager = LinearLayoutManager(this@PaymentSettingsActivity)
                        paymentOptionsAdapter = PaymentSettingsAdapter(this@PaymentSettingsActivity,paymentOptionsActivityVM.mainResponse)
                        paymentOptionsAdapter!!.onitemClickListener = this@PaymentSettingsActivity
                        listPaymentOptions.apply {
                            layoutManager = mlayoutManager
                            adapter = paymentOptionsAdapter
                        }

                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })
        )

    }



    private fun initView() {
        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener {finish()}
        backButton.setOnClickListener {finish()}
        // BACK BUTTON EVENT END
    }

}
