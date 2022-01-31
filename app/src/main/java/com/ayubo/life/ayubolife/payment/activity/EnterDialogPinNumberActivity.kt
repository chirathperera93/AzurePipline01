package com.ayubo.life.ayubolife.payment.activity


import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import android.text.Editable
import android.text.TextWatcher
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.payment.EXTRA_MOBILE_NUMBER_OBJECT
import com.ayubo.life.ayubolife.payment.EXTRA_MOBILE_NUMBER_TYPE
import com.ayubo.life.ayubolife.payment.EXTRA_PAYMENT_META
import com.ayubo.life.ayubolife.payment.vm.EnterDialogPinNumberActivityVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject



class EnterDialogPinNumberActivity : BaseActivity() {

    @Inject
    lateinit var enterDialogPinNumberActivityVM: EnterDialogPinNumberActivityVM

    lateinit var pref: PrefManager
    var icCharactersComplete:Boolean =false

    var serverRef:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val whiteInt = Color.parseColor("#c49f12")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = whiteInt
        }


        setContentView(R.layout.activity_enter_dialog_pin_number)
        App.getInstance().appComponent.inject(this)


        initLocalData()

        initView()

        readExtras()

    }


    private fun isOTPCorrect(otp:String) {

        if(otp.equals(serverRef)){


        }
    }

    private fun initView() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        txt_pin_entry.requestFocus()

        img_go_next.setOnClickListener {
            if (icCharactersComplete) {
                sendOTPtoDialog(txt_pin_entry.text.toString())
            } else {
                Toast.makeText(this, R.string.wrong_pin_number, Toast.LENGTH_LONG).show()
            }
        }

        txt_pin_entry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                icCharactersComplete = s.toString().length == 6


            }
        })

       // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener {finish()}
        backButton.setOnClickListener {finish()}
        // BACK BUTTON EVENT END
    }

    private fun initLocalData() {
        pref = PrefManager(this)
    }

    private fun sendOTPtoDialog(otpNumber:String){

        subscription.add(enterDialogPinNumberActivityVM.verifyDialogOTP(enterDialogPinNumberActivityVM.serverRef!!,otpNumber).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                     //   Add condition to coming screen , cheange the destination

                       val fromAcSettings= Ram.isIsFromSetting()
                        if(fromAcSettings){
                            Ram.setIsFromSetting(false)
                            val intent = Intent(this, PaymentSettingsActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        }else {
                            finish()
                           // PaymentActivity.startActivity(this@UploadReportActivity,meta)

//                            val intent = Intent(this, PaymentActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            intent.putExtra(EXTRA_PAYMENT_META, meta)
//                            startActivity(intent)
//                            finish()
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

    private fun sendDialogNumber(mobileNumber:String,type:String) {

        subscription.add(enterDialogPinNumberActivityVM.sendDialogNumberToGetOTP(mobileNumber,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                     serverRef= enterDialogPinNumberActivityVM.serverRef!!
                      //  finish()

                    } else {
                        //  showMessage("Failed loading data")
                    }
                }, {
                    progressBar.visibility = View.GONE
                    showMessage("Failed loading data")
                })
        )
    }

    private fun readExtras() {

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_MOBILE_NUMBER_OBJECT)) {
            val mobileNumber = bundle.getSerializable(EXTRA_MOBILE_NUMBER_OBJECT) as String
            val mobileType = bundle.getSerializable(EXTRA_MOBILE_NUMBER_TYPE) as String
            if(mobileNumber.length>8){
                sendDialogNumber(mobileNumber,mobileType)
            }
        }
        }

    companion object {
        fun startActivity(context: Activity, mobileNumber: String, mobileType: String) {

            val intent = Intent(context, EnterDialogPinNumberActivity::class.java)
            intent.putExtra(EXTRA_MOBILE_NUMBER_OBJECT, mobileNumber)
            intent.putExtra(EXTRA_MOBILE_NUMBER_TYPE, mobileType)
            context.startActivity(intent)

        }
    }
}




