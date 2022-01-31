package com.ayubo.life.ayubolife.payment.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_ID
import com.ayubo.life.ayubolife.payment.adapter.OtherPaymentOptionsAdapter
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.payment.model.SubmitPinResponse
import com.ayubo.life.ayubolife.payment.vm.OtherPaymentActivityVM
import com.ayubo.life.ayubolife.payment.vm.PaymentConfirmVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_payment_options.*
import kotlinx.android.synthetic.main.activity_payment_pin_submit.*
import javax.inject.Inject
import kotlin.properties.Delegates

class PaymentPinSubmitActivity : BaseActivity() {
    var icCharactersComplete: Boolean = false;

    @Inject
    override lateinit var paymentConfirmVM: PaymentConfirmVM;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_pin_submit)
        App.getInstance().appComponent.inject(this);
        initView();

    }

    private fun initView() {

       if(AppConfig.APP_BRANDING_ID.equals("life_plus")){
           activity_payment_pay_pin_ConstraintLayout.setBackgroundResource(R.color.lifeplus_login_bg);
           activity_payment_pay_pin_enter_pin_textView.setTextColor(Color.parseColor("#00ff79"));
           activity_payment_pay_pin_enter_textView.setTextColor(Color.parseColor("#00ff79"));
       }

        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.activity_payment_pay_pin_lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }


        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        activity_payment_pay_pin_txt_pin_entry.requestFocus();

        activity_payment_pay_pin_txt_pin_entry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                icCharactersComplete = s.toString().length == 4


            }
        })

        activity_payment_pay_pin_img_go_next.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val code = activity_payment_pay_pin_txt_pin_entry.text.toString();
                val pref = PrefManager(this@PaymentPinSubmitActivity);
                subscription.add(paymentConfirmVM.submitPin(pref.paymentId.toInt(), code.toInt()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { activity_payment_pay_pin_progressBar.visibility = View.VISIBLE }
                        .doOnTerminate { activity_payment_pay_pin_progressBar.visibility = View.GONE }
                        .doOnError { activity_payment_pay_pin_progressBar.visibility = View.GONE }
                        .subscribe({
                            if (it.isSuccess) {
                                processAction(paymentConfirmVM.submitPinObj.action, paymentConfirmVM.submitPinObj.meta);

                            } else {
                                showMessage(R.string.service_loading_fail)
                            }
                        }, {
                            activity_payment_pay_pin_progressBar.visibility = View.GONE
                            showMessage(R.string.service_loading_fail)
                        }));


            }
        })
    }


}