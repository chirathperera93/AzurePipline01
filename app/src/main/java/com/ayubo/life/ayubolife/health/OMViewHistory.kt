package com.ayubo.life.ayubolife.health

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_om_page1_submit.view.*
import kotlinx.android.synthetic.main.activity_omview_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OMViewHistory : BaseActivity(),
    OrderMedicineHistoryAdapter.OrderMedicineHistoryItemClickListener {
    lateinit var prefManager: PrefManager;
    private var orderMedicineHistoryAdapter: OrderMedicineHistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_omview_history)
        prefManager = PrefManager(baseContext);

        if (Constants.type == Constants.Type.LIFEPLUS) {
            order_history_order_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            order_history_order_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }


        search_medicine_order.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                orderMedicineHistoryAdapter!!.filter.filter(charSequence)
            }

            override fun afterTextChanged(charSequence: Editable?) {

            }
        });


        getOrderMedicineHistoryData()

        medicine_history_back_btn_image.setOnClickListener {
            val jsonObject: JsonObject =
                Gson().toJsonTree(OMCommon(this).retrieveFromDraftSingleton()).asJsonObject;
            OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
            val intent = Intent(this, OMMainPage::class.java)
            startActivity(intent)
            finish()
        }



        order_history_order_now_btn.setOnClickListener {

            val jsonObject: JsonObject =
                Gson().toJsonTree(OMCommon(this).retrieveFromDraftSingleton()).asJsonObject;
            OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);

            val intent = Intent(this, OMMainPage::class.java)
            startActivity(intent)
            finish()

        }


        pullToRefresh.setOnRefreshListener {
            getOrderMedicineHistoryData();
            pullToRefresh.isRefreshing = false
        };


    }

    private fun getOrderMedicineHistoryData() {
        medicineHistoryProgressBar.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);

        val call: Call<ProfileDashboardResponseData> = apiService.getMyOrders(
            AppConfig.APP_BRANDING_ID,
            prefManager.userToken,
            "All",
            "All"
        );

        call.enqueue(object : Callback<ProfileDashboardResponseData> {

            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {

                if (response.isSuccessful) {
                    println(response)

                    val oMCreatedOrderObjList: ArrayList<OMCreatedOrderObj> =
                        ArrayList<OMCreatedOrderObj>()

                    val getOrdersMainData: JsonArray =
                        Gson().toJsonTree(response.body()!!.data).asJsonArray;

                    try {


                        if (getOrdersMainData.size() == 0) {
                            empty_image.visibility = View.VISIBLE
                            search_medicine_order.visibility = View.GONE

                        } else {
                            empty_image.visibility = View.GONE
                            search_medicine_order.visibility = View.VISIBLE
                            for (i in 0 until getOrdersMainData.size()) {
                                val order: JsonElement = getOrdersMainData.get(i)
                                val orderElement =
                                    Gson().fromJson(order, OMCreatedOrderObj::class.java);
                                oMCreatedOrderObjList.add(orderElement)
                            }

                            setPreviousMedicines(oMCreatedOrderObjList)
                        }

                        medicineHistoryProgressBar.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    Toast.makeText(
                        baseContext,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                    ).show()
                    medicineHistoryProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                medicineHistoryProgressBar.visibility = View.GONE
                throwable.printStackTrace()
            }

        })
    }


    @SuppressLint("WrongConstant")
    private fun setPreviousMedicines(oMCreatedOrderObjArrayList: ArrayList<OMCreatedOrderObj>) {
        medicine_history_recycler_view.layoutManager =
            LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)

        val previousMedicineItemsArrayList = ArrayList<OMCreatedOrderObj>()

        for (i in 0 until oMCreatedOrderObjArrayList.size) {
            val oMCreatedOrder = oMCreatedOrderObjArrayList[i]
            previousMedicineItemsArrayList.add(oMCreatedOrder)


        }


        orderMedicineHistoryAdapter =
            OrderMedicineHistoryAdapter(baseContext, previousMedicineItemsArrayList, false, false)

        try {
            orderMedicineHistoryAdapter!!.onOrderMedicineHistoryItemClickListener =
                this@OMViewHistory
        } catch (e: Exception) {
            e.printStackTrace()
        }


        medicine_history_recycler_view.adapter = orderMedicineHistoryAdapter


    }

    override fun orderMedicineHistoryItemClick(commonHistoryItem: OMCreatedOrderObj) {


        val jsonObject: JsonObject =
            Gson().toJsonTree(commonHistoryItem).asJsonObject;
        OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);


        if (commonHistoryItem.status.equals("Order Confirmed")
            ||
            commonHistoryItem.status.equals("Payment Completed")
            ||
            commonHistoryItem.status.equals("Dispatched")
            ||
            commonHistoryItem.status.equals("On the way")
            ||
            commonHistoryItem.status.equals("Processing")
        ) {
            val intent = Intent(this, OMPage5TrackOrder::class.java)
            startActivity(intent)
        } else if (commonHistoryItem.status.equals("Payment Updated")) {
            processAction("payment", "prescriptionorder:${commonHistoryItem.id}")

        } else {

            val changedObject = OMCreatedOrderObj(
                null,
                null,
                null,
                null,
                commonHistoryItem.status,
                null,
                null,
                null,
                null,
                commonHistoryItem.files,
                commonHistoryItem.address,
                commonHistoryItem.partner,
                commonHistoryItem.payment
            );
            val changedJsonObject: JsonObject = Gson().toJsonTree(changedObject).asJsonObject;

            if (commonHistoryItem.status.equals("Delivered") || commonHistoryItem.status.equals("Cancelled")) {
                OMCommon(baseContext).saveToCommonSingletonAndRetrieve(changedJsonObject)
            }


            val intent = Intent(this, OMMainPage::class.java)
            startActivity(intent)
        }


    }

    override fun orderMedicineHistoryMainCardClick(commonHistoryItem: OMCreatedOrderObj) {

        if (commonHistoryItem.status.equals("Payment Updated")) {
            processAction("payment", "prescriptionorder:${commonHistoryItem.id}")
        } else {
            val jsonObject: JsonObject = Gson().toJsonTree(commonHistoryItem).asJsonObject;
            OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
            val intent = Intent(this, OMMainPage::class.java)
            startActivity(intent)
        }

    }

}