package com.ayubo.life.ayubolife.revamp.v1.activity

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.CommonApiInterface
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCorporateEmailBody
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCorporateEmailResponse
import com.flavors.changes.Constants
import kotlinx.android.synthetic.main.activity_add_corporate_email_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCorporateEmailDialog : Activity() {
    lateinit var appToken: String
    lateinit var pref: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_corporate_email_dialog)

        val displayMetrics: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext)

        val width = deviceScreenDimension.displayWidth
        val height = deviceScreenDimension.displayHeight

        window.setLayout(((width * .8).toInt()), ((height * .7).toInt()))


        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0.5f
        window.attributes = params
        window.setBackgroundDrawableResource(R.drawable.bg_round)

        pref = PrefManager(baseContext)
        appToken = pref.userToken

        editTextForCorporateEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editTextForCorporateEmail.hasFocus())
                    setUpdateButtonEnableUI()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        add_corporate_email_close_btn.setOnClickListener {
            finish()
        }
    }

    private fun setUpdateButtonEnableUI() {
        update_corporate_email_btn.isEnabled = true
        if (Constants.type == Constants.Type.LIFEPLUS) {
            update_corporate_email_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)
        } else {
            update_corporate_email_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

    }


    fun updateCorporateEmail(view: View) {
        corporateEmailLoading.visibility = View.VISIBLE

        val updateCorporateEmailBody =
            UpdateCorporateEmailBody(editTextForCorporateEmail.text.toString())

        val commonApiInterface: CommonApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(CommonApiInterface::class.java)


        val updateCorporateRequestCall: Call<UpdateCorporateEmailResponse> =
            commonApiInterface.updateCorporate(
                appToken,
                AppConfig.APP_BRANDING_ID,
                updateCorporateEmailBody
            )


        updateCorporateRequestCall.enqueue(object : Callback<UpdateCorporateEmailResponse> {
            override fun onResponse(
                call: Call<UpdateCorporateEmailResponse>,
                response: Response<UpdateCorporateEmailResponse>
            ) {


                corporateEmailLoading.visibility = View.GONE

                if (response.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Successfully updated the email",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Ops Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }

            override fun onFailure(call: Call<UpdateCorporateEmailResponse>, t: Throwable) {
                Toast.makeText(
                    baseContext,
                    "Ops Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
                corporateEmailLoading.visibility = View.GONE
            }


        })
    }
}