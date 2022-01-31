package com.ayubo.life.ayubolife.health

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.new_payment.PAYHERE_PAYMENT_ID
import com.ayubo.life.ayubolife.new_payment.SELECTED_ORDER_ID
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_om_page4_payment_detail.view.*
import kotlinx.android.synthetic.main.activity_ompage5_track_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OMPage5TrackOrder : BaseActivity(),
        OrderMedicineLocationAdapter.OnOrderMedicineLocationItemClickListener {

    private var orderMedicineTrackOrderAdapter: OrderMedicineTrackOrderAdapter? = null
    lateinit var prefManager: PrefManager;
    var selectedOrderId: String? = "";
    lateinit var oMCreatedOrderObj: OMCreatedOrderObj
    private var orderMedicineLocationAdapter: OrderMedicineLocationAdapter? = null

    var oMMainOrderItem: OMTrackOrder? = null

    private fun retrieveFromSingleton() {


//        val oMCreatedOrder = prefManager.orderMedicineCreatedOrder


//        oMCreatedOrderObj = Gson().fromJson(oMCreatedOrder, OMCreatedOrderObj::class.java);

        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton();
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ompage5_track_order)
        prefManager = PrefManager(baseContext);
        retrieveFromSingleton()
        selectedOrderId = oMCreatedOrderObj.id;
        readExtras()



//        val currentIntent: Intent = intent;
//        val orderId: String? = currentIntent.getStringExtra("orderId");
//
//        if (orderId !== null) {
//            selectedOrderId = orderId.toString()
//        }


        initializeUI()

        getTrackOrderDetail()

        setWaitingPriceMedicinesLocation()

    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(SELECTED_ORDER_ID)) {
            val SELECTED_ORDER_ID = bundle.getSerializable(SELECTED_ORDER_ID) as String
            selectedOrderId = SELECTED_ORDER_ID;
        }
    }

    private fun initializeUI() {
        track_order_back_btn_image.setOnClickListener {
            finish()
        }

        linearLayoutForOMCallUs.setOnClickListener {
            processAction("call", "0117988788")
        }

        linearLayoutForOMChat.setOnClickListener {
            processAction("open_conversation", oMMainOrderItem!!.session_id!!)
        }
    }

    @SuppressLint("WrongConstant")
    private fun getTrackOrderDetail() {
        trackOrderProgressBar.visibility = View.VISIBLE
        delivery_process_recycler_view.layoutManager =
                LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)

        val apiService: ApiInterface =
                ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);

        val getOrderTrackingCall: Call<ProfileDashboardResponseData> = apiService.getOrderTracking(
                AppConfig.APP_BRANDING_ID,
                prefManager.userToken,
                selectedOrderId
        );

        getOrderTrackingCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
            ) {
                trackOrderProgressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val orderTrackingMainData: JsonObject =
                            Gson().toJsonTree(response.body()!!.data).asJsonObject;

                    track_order_delivery_loaction_main.visibility = View.VISIBLE

                    print(orderTrackingMainData)

                    val orderMedicineTrackOrderItemsArrayList = ArrayList<OMTrackOrderItem>()

                    val mainOrder = orderTrackingMainData.get("order").asJsonObject
                    oMMainOrderItem =
                            Gson().fromJson(mainOrder, OMTrackOrder::class.java);


                    val date = Date(oMMainOrderItem!!.lastupdated_datetime!!.toLong())
                    val formatter: SimpleDateFormat = SimpleDateFormat("EEEE, dd MMMM");
                    val strDate: String = formatter.format(date);
                    time_text.text = strDate

                    order_id_text.text = "Order ID : " + oMMainOrderItem!!.order_id

                    if (oMMainOrderItem!!.payment != null && oMMainOrderItem!!.payment!!.total_amount != null) {
                        amount_value_text.text =
                                oMMainOrderItem!!.payment!!.total_amount + " " + oMMainOrderItem!!.payment!!.currency
                        amount_label_text.visibility = View.VISIBLE
                    }


                    val orderTrackerArray = orderTrackingMainData.get("tracking").asJsonArray

                    if (orderTrackerArray.size() > 0) {
                        for (i in 0 until orderTrackerArray.size()) {
                            val orderTrack = orderTrackerArray[i]

                            val oMTrackOrderItem: OMTrackOrderItem =
                                    Gson().fromJson(orderTrack, OMTrackOrderItem::class.java);

                            orderMedicineTrackOrderItemsArrayList.add(oMTrackOrderItem)

                            orderMedicineTrackOrderAdapter =
                                    OrderMedicineTrackOrderAdapter(
                                            baseContext,
                                            orderMedicineTrackOrderItemsArrayList,
                                            false
                                    )


                            delivery_process_recycler_view.adapter = orderMedicineTrackOrderAdapter
                        }
                    }


                }


            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                throwable.printStackTrace()
                trackOrderProgressBar.visibility = View.GONE

            }

        });


    }

    @SuppressLint("WrongConstant")
    private fun setWaitingPriceMedicinesLocation() {
        track_order_location_recycler_view.layoutManager =
                LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)

        val orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()

        orderMedicineLocationItemsArrayList.add(oMCreatedOrderObj.address!!)
        orderMedicineLocationAdapter =
                OrderMedicineLocationAdapter(
                        baseContext,
                        orderMedicineLocationItemsArrayList,
                        orderMedicineLocationItemsArrayList,
                        false,
                        false,
                        false
                )

        try {
            orderMedicineLocationAdapter!!.onOrderMedicineLocationItemClickListener =
                    this@OMPage5TrackOrder
        } catch (e: Exception) {
            e.printStackTrace()
        }


        track_order_location_recycler_view.adapter = orderMedicineLocationAdapter
    }

    override fun orderMedicineLocationItemClick(OrderMedicineLocationItem: OMAddress) {

    }

    override fun orderMedicineFavLocationItemClick(OrderMedicineLocationItem: OMAddress) {

    }

    override fun orderMedicineLocationEditItemClick(OrderMedicineLocationItem: OMAddress) {

    }

    override fun orderMedicineLocationLongPress(OrderMedicineLocationItem: OMAddress) {

    }
}