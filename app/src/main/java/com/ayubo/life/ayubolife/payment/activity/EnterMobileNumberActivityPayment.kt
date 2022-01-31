package com.ayubo.life.ayubolife.payment.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.EXTRA_MOBILE_NUMBER_TYPE
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.webrtc.App
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.img_go_next
import kotlinx.android.synthetic.main.activity_enter_mobile_number2.*


class EnterMobileNumberActivityPayment : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val whiteInt = Color.parseColor("#c49f12")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = whiteInt
        }

        setContentView(R.layout.activity_enter_mobile_number2)
        App.getInstance().appComponent.inject(this)

        initView()


    }

    override fun onResume() {
        super.onResume()

        readExtras()
    }

    private fun readExtras(): String {
        var mobileType: String? = null
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_MOBILE_NUMBER_TYPE)) {
            mobileType = bundle.getSerializable(EXTRA_MOBILE_NUMBER_TYPE) as String
        }
        return mobileType!!
    }

    private fun initView() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        txt_moblie_number_new.requestFocus()


        img_go_next.setOnClickListener {
            if (txt_moblie_number_new.text.length > 8) {

                var strNumber = txt_moblie_number_new.text.toString()
                if (strNumber.length == 9) {
                    strNumber = "94$strNumber"
                }
                if (strNumber.length == 10) {
                    strNumber = strNumber.substring(1)
                    strNumber = "94$strNumber"
                }
                EnterDialogPinNumberActivity.startActivity(
                    this@EnterMobileNumberActivityPayment,
                    strNumber,
                    readExtras()
                )
                finish()
            } else {
                Toast.makeText(this, R.string.enter_mobile_number, Toast.LENGTH_LONG).show()
            }
        }

        // BACK EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        // BACK BUTTON EVENT END
    }


    companion object {
        fun startActivity(context: Context, meta: String) {

            val intent = Intent(context, EnterMobileNumberActivityPayment::class.java)
            intent.putExtra(EXTRA_MOBILE_NUMBER_TYPE, meta)
            context.startActivity(intent)

        }
    }

}



