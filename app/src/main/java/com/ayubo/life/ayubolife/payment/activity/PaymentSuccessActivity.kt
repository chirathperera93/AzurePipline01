package com.ayubo.life.ayubolife.payment.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity
import com.ayubo.life.ayubolife.payment.EXTRA_TRANSACTION_AMOUNT
import com.ayubo.life.ayubolife.payment.EXTRA_TRANSACTION_DATE
import com.ayubo.life.ayubolife.payment.EXTRA_TRANSACTION_ID
import com.ayubo.life.ayubolife.payment.EXTRA_TRANSACTION_STRING
import com.ayubo.life.ayubolife.payment.model.ApiPAayList
import com.ayubo.life.ayubolife.utility.Ram
import kotlinx.android.synthetic.main.activity_payment_success.*


class PaymentSuccessActivity : AppCompatActivity() {

    internal var successPaymentData: ArrayList<ApiPAayList>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        successPaymentData = Ram.getSuccessPaymentDataList() as ArrayList<ApiPAayList>?


        initView()


    }


    private fun initView() {


        for (c in 0 until successPaymentData!!.size) {

            val successObj = successPaymentData!![c]

            var inflater: LayoutInflater? = null
            inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            assert(inflater != null)
            val cell = inflater!!.inflate(R.layout.payment_success_cell, null)

            val tv_payment_name = cell.findViewById(R.id.tv_payment_name) as TextView
            val tv_payment_value = cell.findViewById(R.id.tv_payment_value) as TextView

            tv_payment_name.text = successObj.key
            tv_payment_value.text = successObj.value


            layout_details.addView(cell)

        }


        btn_done.setOnClickListener {
            val i = Intent(applicationContext, NewHomeWithSideMenuActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        }

    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_TRANSACTION_ID)) {
            val transaction_id = bundle.getSerializable(EXTRA_TRANSACTION_ID) as String
            val transaction_string = bundle.getSerializable(EXTRA_TRANSACTION_STRING) as String
            val transaction_date = bundle.getSerializable(EXTRA_TRANSACTION_DATE) as String
            val transaction_amount = bundle.getSerializable(EXTRA_TRANSACTION_AMOUNT) as String
        }
    }

    companion object {
        fun startActivity(context: Activity) {


            val intent = Intent(context, PaymentSuccessActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val i = Intent(applicationContext, NewHomeWithSideMenuActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)

    }
//    companion object {
//        fun startActivity(context: Activity,transaction_id: String,transaction_string: String,transaction_date: String,transaction_amount: String) {
//            val intent = Intent(context, PaymentSuccessActivity::class.java)
//            intent.putExtra(EXTRA_TRANSACTION_ID, transaction_id)
//            intent.putExtra(EXTRA_TRANSACTION_STRING, transaction_string)
//            intent.putExtra(EXTRA_TRANSACTION_DATE, transaction_date)
//            intent.putExtra(EXTRA_TRANSACTION_AMOUNT, transaction_amount)
//            context.startActivity(intent)
//        }
//    }
}


