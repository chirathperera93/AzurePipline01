package com.ayubo.life.ayubolife.payment.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.health.*
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_omview_history.*
import kotlinx.android.synthetic.main.fragment_medicine_history.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicineHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicineHistory : Fragment(), OrderMedicineHistoryAdapter.OrderMedicineHistoryItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var prefManager: PrefManager;
    private var orderMedicineHistoryAdapter: OrderMedicineHistoryAdapter? = null

    lateinit var mainView: View

    lateinit var changeFragmentInterface: ChangeFragmentInterface;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        prefManager = PrefManager(context);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        mainView = inflater.inflate(R.layout.fragment_medicine_history, container, false)


        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.order_history_order_now_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.order_history_order_now_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }


        mainView.search_medicine_order.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                orderMedicineHistoryAdapter!!.filter.filter(charSequence)
            }

            override fun afterTextChanged(charSequence: Editable?) {

            }
        });

        mainView.order_history_order_now_btn.setOnClickListener {

            val jsonObject: JsonObject =
                    Gson().toJsonTree(context?.let { it1 -> OMCommon(it1).retrieveFromDraftSingleton() }).asJsonObject;
            context?.let { it1 -> OMCommon(it1).saveToCommonSingletonAndRetrieve(jsonObject) };

            val intent = Intent(context, OMMainPage::class.java)
            startActivity(intent)


        }


        mainView.pullToRefresh.setOnRefreshListener {
            getOrderMedicineHistoryData();
            pullToRefresh.isRefreshing = false
        };


        // Inflate the layout for this fragment
        return mainView
    }

    override fun onResume() {
        super.onResume()
        getOrderMedicineHistoryData()
    }

    private fun getOrderMedicineHistoryData() {
        mainView.medicineHistoryProgressBar.visibility = View.VISIBLE
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
                            mainView.empty_image.visibility = View.VISIBLE
                            mainView.search_medicine_order.visibility = View.GONE

                        } else {
                            mainView.empty_image.visibility = View.GONE
                            mainView.search_medicine_order.visibility = View.VISIBLE
                            for (i in 0 until getOrdersMainData.size()) {
                                val order: JsonElement = getOrdersMainData.get(i)
                                val orderElement =
                                        Gson().fromJson(order, OMCreatedOrderObj::class.java);
                                oMCreatedOrderObjList.add(orderElement)
                            }

                            setPreviousMedicines(oMCreatedOrderObjList)
                        }

                        mainView.medicineHistoryProgressBar.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    Toast.makeText(
                            context,
                            "There is an issue when loading data, Please contact admin.",
                            Toast.LENGTH_SHORT
                    ).show()
                    mainView.medicineHistoryProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                mainView.medicineHistoryProgressBar.visibility = View.GONE
                throwable.printStackTrace()
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun setPreviousMedicines(oMCreatedOrderObjArrayList: ArrayList<OMCreatedOrderObj>) {
        mainView.medicine_history_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        val previousMedicineItemsArrayList = ArrayList<OMCreatedOrderObj>()

        for (i in 0 until oMCreatedOrderObjArrayList.size) {
            val oMCreatedOrder = oMCreatedOrderObjArrayList[i]
            previousMedicineItemsArrayList.add(oMCreatedOrder)


        }


        orderMedicineHistoryAdapter =
                context?.let { OrderMedicineHistoryAdapter(it, previousMedicineItemsArrayList, false, false) }

        try {
            orderMedicineHistoryAdapter!!.onOrderMedicineHistoryItemClickListener = this@MedicineHistory
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mainView.medicine_history_recycler_view.adapter = orderMedicineHistoryAdapter


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicineHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MedicineHistory().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    interface ChangeFragmentInterface {
        fun onMedicineHistoryOrderButtonClick(oMCreatedOrderObj: OMCreatedOrderObj);
    }

    override fun orderMedicineHistoryItemClick(commonHistoryItem: OMCreatedOrderObj) {
        val jsonObject: JsonObject =
                Gson().toJsonTree(commonHistoryItem).asJsonObject;
        context?.let { OMCommon(it).saveToCommonSingletonAndRetrieve(jsonObject) };


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
            val intent = Intent(context, OMPage5TrackOrder::class.java)
            startActivity(intent)
        } else if (commonHistoryItem.status.equals("Payment Updated")) {

            changeFragmentInterface = context as (MedicineHistory.ChangeFragmentInterface);
            changeFragmentInterface.onMedicineHistoryOrderButtonClick(commonHistoryItem)
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
                context?.let { OMCommon(it).saveToCommonSingletonAndRetrieve(changedJsonObject) }
            }


            val intent = Intent(context, OMMainPage::class.java)
            startActivity(intent)
        }
    }

    override fun orderMedicineHistoryMainCardClick(commonHistoryItem: OMCreatedOrderObj) {
        if (commonHistoryItem.status.equals("Payment Updated")) {
            changeFragmentInterface = context as (MedicineHistory.ChangeFragmentInterface);
            changeFragmentInterface.onMedicineHistoryOrderButtonClick(commonHistoryItem)
        } else {
            val jsonObject: JsonObject = Gson().toJsonTree(commonHistoryItem).asJsonObject;
            context?.let { OMCommon(it).saveToCommonSingletonAndRetrieve(jsonObject) };
            val intent = Intent(context, OMMainPage::class.java)
            startActivity(intent)
        }
    }
}