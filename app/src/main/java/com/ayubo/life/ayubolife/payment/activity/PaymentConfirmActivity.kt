package com.ayubo.life.ayubolife.payment.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity
import com.ayubo.life.ayubolife.payment.*
import com.ayubo.life.ayubolife.payment.model.PaymentConfirmMainData
import com.ayubo.life.ayubolife.payment.model.PaymentConfirmMainDataNew
import com.ayubo.life.ayubolife.payment.vm.PaymentConfirmVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_payment_confirm.*
import javax.inject.Inject
import com.ayubo.life.ayubolife.utility.Ram


class PaymentConfirmActivity : BaseActivity() {

    var isWentToPaid: Boolean = false


    lateinit var dataObj: PaymentConfirmMainData

//    lateinit var dataObjNew: PaymentConfirmMainDataNew

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_confirm)
        App.getInstance().appComponent.inject(this)
        paymentConfirmationConstraintLayout.visibility = View.GONE;

        initView()

        readExtras()

    }


    override fun onResume() {
        super.onResume()

        if (isWentToPaid) {
            val i = Intent(applicationContext, NewHomeWithSideMenuActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }
    }

    private fun getConfirmationData(
            item_price_master_id: Int,
            text: String,
            amount: String,
            relate_type_id: String,
            related_id: String,
            payment_source_id: String,
            payment_frequency: String,
            payment_frequency_id: String,
            custom_param: String,
            user_payment_method_id: String
    ) {

        subscription.add(paymentConfirmVM.getPaymentConfirmation(
                item_price_master_id,
                text,
                amount,
                relate_type_id,
                related_id,
                payment_source_id,
                payment_frequency,
                payment_frequency_id,
                custom_param,
                user_payment_method_id
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({

                    if (it.isSuccess) {

                        dataObjNew = paymentConfirmVM.dataObj;

                        println("8888888888888888888888888888888888888888888888888 " + dataObjNew);
//                        dataObj = paymentConfirmVM.dataObj

//                        loadView(dataObj)

                    } else {
                        showMessage(R.string.service_loading_fail)
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage(R.string.service_loading_fail)
                })

        )

    }

    private fun makePayment() {
        val map: HashMap<String, String> = HashMap<String, String>()
        val dataList = dataObj.payment_params
        for (i in 0 until dataList.size) {
            val obj = dataList[i]
            map[obj.key] = obj.value
        }



        subscription.add(paymentConfirmVM.makePaymentRequest(dataObj.payment_meta, map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        if (paymentConfirmVM.apiPayObj.payment_status == "complete") {
                            Ram.setSuccessPaymentDataList(paymentConfirmVM.apiPayObj.list)
                            PaymentSuccessActivity.startActivity(this@PaymentConfirmActivity)
                        } else {
                            PaymentFailActivity.startActivity(this@PaymentConfirmActivity)
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

    private fun loadView(dataObj: PaymentConfirmMainData) {

        tv_heading.text = dataObj.heading

        val dataList = dataObj.table



        if (dataList.isNotEmpty()) {

            for (i in 0 until dataList.size) {
                val priceObj = dataList[i]
                val inflater = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?)!!
                val cell = inflater.inflate(R.layout.raw_payment_confirm_details, null)
                val tvPaymentType = cell.findViewById(R.id.tv_payment_type) as TextView
                val tvPriceValue = cell.findViewById(R.id.tv_price_value) as TextView
                tvPaymentType.text = priceObj.label
                tvPriceValue.text = priceObj.value
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(0, 0, 0, 30)
                cell.layoutParams = lp
                cell.tag = priceObj
                lay_values_container.addView(cell, lp)
            }
        }
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_PAYMENT_CONFRIME_ITEM_PRICE_MASTER_ID)) {
            val itemPriceMasterId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_ITEM_PRICE_MASTER_ID) as Int
            val text = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_TEXT) as String
            val amount = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_AMOUNT) as String
            val relateTypeId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_RELATETYPE_ID) as String
            val relatedId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_RELATED_ID) as String
            val paymentSourceId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_SOURCE_ID) as String
            val paymentFrequency = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_FREQUENCY) as String
            val paymentFrequencyId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_FREQUENCY_ID) as String
            val customParam = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_CUSTOM_PARAMS) as String
            val userPaymentMethodId = bundle.getSerializable(EXTRA_PAYMENT_CONFRIME_USER_PAYMENT_METHOD_ID) as String


            if (itemPriceMasterId > -1) {
                getConfirmationData(
                        itemPriceMasterId,
                        text,
                        amount,
                        relateTypeId,
                        relatedId,
                        paymentSourceId,
                        paymentFrequency,
                        paymentFrequencyId,
                        customParam,
                        userPaymentMethodId
                )
            }
        }
    }

    companion object {
        fun startActivity(context: Activity,
                          item_price_master_id: Int,
                          text: String,
                          amount: String,
                          relate_type_id: String,
                          related_id: String,
                          payment_source_id: String,
                          payment_frequency: String,
                          service_payment_frequency_source_id: String,
                          custom_param: String,
                          user_payment_method_id: Int
        ) {
            val intent = Intent(context, PaymentConfirmActivity::class.java)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_ITEM_PRICE_MASTER_ID, item_price_master_id)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_TEXT, text)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_AMOUNT, amount)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_RELATETYPE_ID, relate_type_id)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_RELATED_ID, related_id)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_SOURCE_ID, payment_source_id)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_FREQUENCY, payment_frequency)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_FREQUENCY_ID, service_payment_frequency_source_id)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_CUSTOM_PARAMS, custom_param)
            intent.putExtra(EXTRA_PAYMENT_CONFRIME_USER_PAYMENT_METHOD_ID, user_payment_method_id)
            context.startActivity(intent)
        }
    }


    private fun initView() {

        Ram.setSuccessPaymentDataList(null)

        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        // BACK BUTTON EVENT END

        btn_continue.setOnClickListener {

            if (dataObj.payment_action == "apipay") {
                makePayment()
            } else {
                isWentToPaid = true
                paymentActionProcess(dataObj.payment_action, dataObj.payment_meta)
            }

        }
    }


}

