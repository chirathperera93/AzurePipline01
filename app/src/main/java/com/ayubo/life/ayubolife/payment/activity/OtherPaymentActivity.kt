package com.ayubo.life.ayubolife.payment.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_META
import com.ayubo.life.ayubolife.payment.adapter.OtherPaymentOptionsAdapter
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.payment.vm.OtherPaymentActivityVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_payment_options.*
import javax.inject.Inject

class OtherPaymentActivity : BaseActivity() , OtherPaymentOptionsAdapter.OnItemClickListener{

    override fun onProcessAction(action: String, meta: String) {
        processAction(action,meta)
    }

    override fun onPaymentProcess(obj: PriceList) {
        onPaymentProcessed(obj)
    }


    @Inject
    lateinit var otherPaymentOptionsActivityVM: OtherPaymentActivityVM

    private var otherPaymentOptionsAdapter: OtherPaymentOptionsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_payment)
        App.getInstance().appComponent.inject(this)

        initView()


        subscription.add(otherPaymentOptionsActivityVM.getOtherPaymentOptions(readExtras()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        val mlayoutManager = LinearLayoutManager(this@OtherPaymentActivity)
                        otherPaymentOptionsAdapter = OtherPaymentOptionsAdapter(this@OtherPaymentActivity, otherPaymentOptionsActivityVM.dataList)
                        otherPaymentOptionsAdapter!!.onitemClickListener = this@OtherPaymentActivity
                        listPaymentOptions.apply {
                            layoutManager = mlayoutManager
                            adapter = otherPaymentOptionsAdapter
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

    private fun readExtras():String {
        lateinit var meta:String
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PAYMENT_META)) {
            meta = bundle.getSerializable(EXTRA_PAYMENT_META) as String
        }
        return meta
    }

    companion object {
        fun startActivity(context: Activity,meta:String) {
            val intent = Intent(context, OtherPaymentActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_META, meta)
            context.startActivity(intent)
        }
    }

    private fun initView() {
        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        // BACK BUTTON EVENT END
    }

}