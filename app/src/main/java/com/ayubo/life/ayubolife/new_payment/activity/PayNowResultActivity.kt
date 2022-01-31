package com.ayubo.life.ayubolife.new_payment.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.health.CalculatedPriceAdapter
import com.ayubo.life.ayubolife.health.PaymentItems
import com.ayubo.life.ayubolife.new_payment.CHARGE_PAYMENT_RESPONSE
import com.ayubo.life.ayubolife.new_payment.model.ChargePaymentDetail
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pay_now_result.*

class PayNowResultActivity : BaseActivity() {

    var phoneNumber = "0"

    var chargePaymentDetail: ChargePaymentDetail? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_now_result)

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(CHARGE_PAYMENT_RESPONSE)) {
            val mainChargePaymentResponseString =
                bundle.getSerializable(CHARGE_PAYMENT_RESPONSE) as String


            chargePaymentDetail = Gson().fromJson(
                mainChargePaymentResponseString,
                ChargePaymentDetail::class.java
            );

            print(chargePaymentDetail)

        }

        setData()
    }

    private fun setData() {

        openContactNumber.setOnClickListener {
            processAction("call", phoneNumber)
        }


        if (chargePaymentDetail!!.icon_url != null) {
            Glide.with(baseContext).load(chargePaymentDetail!!.icon_url)
                .into(imageViewForPaymentResult)
        }


        if (chargePaymentDetail!!.title != null) {
            textViewForPaymentResultTopic.text = chargePaymentDetail!!.title
            textViewForPaymentResultTopic.setTextColor(Color.parseColor(chargePaymentDetail!!.title_color))

        }

        if (chargePaymentDetail!!.description != null) {
            textViewForPaymentResultDescription.text = chargePaymentDetail!!.description
        }

        if (chargePaymentDetail!!.payment_details != null) {
            setPaymentDetail(chargePaymentDetail!!.payment_details!!)
        }


        paymentResultTotalAmountLabel.visibility = View.VISIBLE
        paymentResultTotalAmountValue.text =
            chargePaymentDetail!!.payment_total!!.toString() + " " + chargePaymentDetail!!.payment_currency!!


        phoneNumber = chargePaymentDetail!!.texts!!.get("text_three").asString;


        textView1.text = chargePaymentDetail!!.texts!!.get("text_one").asString
        textView2.text = chargePaymentDetail!!.texts!!.get("text_two").asString
        textView3.text = chargePaymentDetail!!.texts!!.get("text_three").asString


        button1.text = chargePaymentDetail!!.button_one!!.get("text").asString
        if (chargePaymentDetail!!.status) {
            button1.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        } else {
            button1.setBackgroundResource(R.drawable.color_ff4343_gradient_rounded_button)
        }

        button1.setOnClickListener {
            processAction(
                chargePaymentDetail!!.button_one!!.get("action").asString,
                chargePaymentDetail!!.button_one!!.get("meta").asString
            )
            finish()
        }

        button2.text = chargePaymentDetail!!.button_two!!.get("text").asString

        button2.setOnClickListener {
            processAction(
                chargePaymentDetail!!.button_two!!.get("action").asString,
                chargePaymentDetail!!.button_two!!.get("meta").asString
            )
            finish()
        }

    }

    private fun setPaymentDetail(paymentItemsArrayList: ArrayList<PaymentItems>) {
        paymentResultCalculatedPrice.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        val calculatedPriceAdapter: CalculatedPriceAdapter =
            CalculatedPriceAdapter(baseContext, paymentItemsArrayList, false)


        paymentResultCalculatedPrice.adapter = calculatedPriceAdapter
    }
}