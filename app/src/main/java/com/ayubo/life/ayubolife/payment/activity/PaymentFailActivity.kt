package com.ayubo.life.ayubolife.payment.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.ayubo.life.ayubolife.R

class PaymentFailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_fail)

        initView()


    }

    companion object {
        fun startActivity(context: Activity) {
            val intent = Intent(context, PaymentFailActivity::class.java)
            context.startActivity(intent)
        }
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
