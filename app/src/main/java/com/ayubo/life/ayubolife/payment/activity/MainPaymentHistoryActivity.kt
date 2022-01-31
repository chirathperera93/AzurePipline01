package com.ayubo.life.ayubolife.payment.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.android.synthetic.main.activity_main_payment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPaymentHistoryActivity : AppCompatActivity() {
    lateinit var appToken: String;
    lateinit var pref: PrefManager;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_payment_history)

        pref = PrefManager(baseContext);
        appToken = pref.userToken;

        imageViewForMainPaymentHistoryBackBtn.setOnClickListener {
            finish()
        }

        getPaymentHistoryData()
    }

    private fun getPaymentHistoryData() {

        payment_history_loading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1().create(ApiInterface::class.java);
        val call: Call<ProfileDashboardResponseData> = apiService.getUserPurchaseHistoryData(
            AppConfig.APP_BRANDING_ID,
            appToken,
            "user_dashboard",
            System.currentTimeMillis()
        );
        call.enqueue(object : Callback<ProfileDashboardResponseData> {
            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                payment_history_loading.visibility = View.GONE
                if (response.isSuccessful) {
                    val userPurchaseHistoryData: JsonArray =
                        Gson().toJsonTree(response.body()!!.data).asJsonArray;

                    println(userPurchaseHistoryData)

                    payment_history_recycler_view.layoutManager =
                        LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
                    val purchaseHistoryItemArrayList = ArrayList<PurchaseHistoryItem>()
                    for (i in 0 until userPurchaseHistoryData.size()) {
                        val purchaseHistoryItem = userPurchaseHistoryData.get(i)

                        purchaseHistoryItemArrayList.add(
                            PurchaseHistoryItem(
                                purchaseHistoryItem.asJsonObject.get("id").asString,
                                purchaseHistoryItem.asJsonObject.get("app_id").asString,
                                purchaseHistoryItem.asJsonObject.get("user_id").asString,
                                purchaseHistoryItem.asJsonObject.get("title").asString,
                                purchaseHistoryItem.asJsonObject.get("description").asString,
                                purchaseHistoryItem.asJsonObject.get("icon").asString,
                                purchaseHistoryItem.asJsonObject.get("status").asString,
                                purchaseHistoryItem.asJsonObject.get("currency").asString,
                                purchaseHistoryItem.asJsonObject.get("cost").asInt,
                                purchaseHistoryItem.asJsonObject.get("created_datetime").asLong,
                                purchaseHistoryItem.asJsonObject.get("updated_datetime").asLong

                            )

                        )


                    }

                    val purchaseHistoryItemAdapter =
                        PurchaseHistoryItemAdapter(baseContext, purchaseHistoryItemArrayList)
                    payment_history_recycler_view.adapter = purchaseHistoryItemAdapter

                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                payment_history_loading.visibility = View.GONE
            }

        })

    }
}