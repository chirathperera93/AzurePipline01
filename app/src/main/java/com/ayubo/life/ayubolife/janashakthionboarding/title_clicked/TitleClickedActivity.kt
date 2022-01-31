package com.ayubo.life.ayubolife.janashakthionboarding.title_clicked

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.db.DBRequest
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.login.LoginActivity_First
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_medical_update.*
import kotlinx.android.synthetic.main.activity_title_clicked_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class TitleClickedActivity : AppCompatActivity() {
     var pref: PrefManager? =null
    var policy_user_master_id:String?=null
    var meta:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_clicked_)

        pref = PrefManager(this)

        policy_user_master_id = intent.getStringExtra("meta")



    }

    fun clickUpload(v: View) {
        MedicalUpdateActivity.startActivity(this)
        finish()
    }

     fun clickPreceedQes(v: View) {
         getProceedWithQuestionre()
    }
    fun clickToFinishScreen(view: View){
        view.context
        finish()
    }

    fun getProceedWithQuestionre() {

        progressBar_ProceedWith.visibility = View.VISIBLE
        Log.d("....ddd.....",pref!!.relateID)
        val apiService = ApiClient.getNewApiClient().create(ApiInterface::class.java)
        val call = apiService.proceedWithQuestions(AppConfig.APP_BRANDING_ID,pref!!.userToken,pref!!.loginUser.get("uid"),pref!!.relateID)
        call.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                progressBar_ProceedWith.visibility = View.GONE
                if (response.isSuccessful) {
                    println("========t======")

                  finish()

                }
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                progressBar_ProceedWith.visibility = View.GONE

                println("========t======$t")
            }
        })
    }
}
