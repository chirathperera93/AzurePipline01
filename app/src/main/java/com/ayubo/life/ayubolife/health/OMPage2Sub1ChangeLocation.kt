package com.ayubo.life.ayubolife.health

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_medicine_add_new_location.*
import kotlinx.android.synthetic.main.activity_medicine_change_location.*
import kotlinx.android.synthetic.main.activity_medicine_change_location.back_topic_layout
import kotlinx.android.synthetic.main.activity_om_page2_set_location.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OMPage2Sub1ChangeLocation : BaseActivity(),
    OrderMedicineLocationAdapter.OnOrderMedicineLocationItemClickListener {

    private var orderMedicineLocationAdapter: OrderMedicineLocationAdapter? = null
    lateinit var prefManager: PrefManager;
    lateinit var oMCreatedOrderObj: OMCreatedOrderObj
    var selectedFavLocation: OMAddress? = null;
    var orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()
    var orderMedicineFavLocationItemsArrayList = ArrayList<OMAddress>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_change_location)

        prefManager = PrefManager(baseContext);

        initializeUI()


    }

    override fun onResume() {
        super.onResume()
        setMyLocationRecyclerView()
    }

    private fun initializeUI() {

        set_location_btn.isEnabled = true

        if (Constants.type == Constants.Type.LIFEPLUS) {
            set_location_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            set_location_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }


        back_topic_layout.setOnClickListener {
            finish();
        }

        add_new_location.setOnClickListener {
            val intent = Intent(this, OMPage2Sub2AddNewLocation::class.java)
            intent.putExtra("goToMain", "false");
            intent.putExtra("editAddress", "");
            startActivity(intent)
            finish()
        }

        set_location_btn.setOnClickListener {


            if (selectedFavLocation !== null) {
                oMCreatedOrderObj.address = selectedFavLocation;


                val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;

                OMCommon(baseContext).saveToDraftSingletonAndRetrieve(jsonObject);
                OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
            }


            finish();
        }


        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton();
    }


    @SuppressLint("WrongConstant")
    private fun setMyLocationRecyclerView() {
        my_location_recycler_view.layoutManager =
            LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)

        orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()
        orderMedicineFavLocationItemsArrayList = ArrayList<OMAddress>()

        setLocationProgressBar.visibility = View.VISIBLE

        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java);

        val getAddressesCall: Call<ProfileDashboardResponseData> = apiService.getAddresses(
            AppConfig.APP_BRANDING_ID,
            prefManager.userToken
        );




        getAddressesCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                p0: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                setLocationProgressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    print(response)

                    val dashboardMainData: JsonArray =
                        Gson().toJsonTree(response.body()!!.data).asJsonArray;

                    if (dashboardMainData.size() > 0) {

                        for (i in 0 until dashboardMainData.size()) {
                            val location = dashboardMainData[i]

                            val oMAddress: OMAddress =
                                Gson().fromJson(location.toString(), OMAddress::class.java);

                            orderMedicineLocationItemsArrayList.add(oMAddress)

                            if (oMAddress.fav_address == true) {
                                orderMedicineFavLocationItemsArrayList.add(oMAddress)
                            }


                        }

                    }

                    orderMedicineLocationAdapter =
                        OrderMedicineLocationAdapter(
                            baseContext,
                            orderMedicineLocationItemsArrayList,
                            orderMedicineFavLocationItemsArrayList,
                            false,
                            true
                        )

                    try {
                        orderMedicineLocationAdapter!!.onOrderMedicineLocationItemClickListener =
                            this@OMPage2Sub1ChangeLocation
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    my_location_recycler_view.adapter = orderMedicineLocationAdapter


                } else {
                    Toast.makeText(
                        baseContext,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                    ).show()
                    setLocationProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                Toast.makeText(
                    baseContext,
                    "There is an issue when loading data, Please contact admin.",
                    Toast.LENGTH_SHORT
                ).show()
                setLocationProgressBar.visibility = View.GONE
            }

        })
    }

    override fun orderMedicineLocationItemClick(orderMedicineLocationItem: OMAddress) {
        print(orderMedicineLocationItem)

        selectedFavLocation = orderMedicineLocationItem;

    }

    override fun orderMedicineFavLocationItemClick(orderMedicineLocationItem: OMAddress) {

        updateAddress(orderMedicineLocationItem);

    }

    private fun updateAddress(orderMedicineLocationItem: OMAddress) {
        setLocationProgressBar.visibility = View.VISIBLE


        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java);

        val updateAddressCall: Call<ProfileDashboardResponseData> = apiService.updateAddress(
            AppConfig.APP_BRANDING_ID,
            prefManager.userToken,
            orderMedicineLocationItem
        );

        updateAddressCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                p0: Call<ProfileDashboardResponseData>,
                p1: Response<ProfileDashboardResponseData>
            ) {
                setLocationProgressBar.visibility = View.GONE

                var favMessage = "";
                if (orderMedicineLocationItem.fav_address == true) {
                    favMessage = "Added address to favourites"
                } else {
                    favMessage = "Removed address from favourites"
                }

                Toast.makeText(baseContext, favMessage, Toast.LENGTH_SHORT).show();
                setMyLocationRecyclerView();

            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                setLocationProgressBar.visibility = View.GONE
                Toast.makeText(baseContext, "Server error", Toast.LENGTH_SHORT).show();
            }
        })

    }

    override fun orderMedicineLocationEditItemClick(orderMedicineLocationItem: OMAddress) {
        print(orderMedicineLocationItem)
        val jsonObject: JsonObject = Gson().toJsonTree(orderMedicineLocationItem).asJsonObject;
        val intent = Intent(baseContext, OMPage2Sub2AddNewLocation::class.java)
        intent.putExtra("editAddress", jsonObject.toString());
        startActivity(intent)


    }

    override fun orderMedicineLocationLongPress(OrderMedicineLocationItem: OMAddress) {
        openBottomSlider(OrderMedicineLocationItem)
    }

    fun openBottomSlider(OrderMedicineLocationItem: OMAddress) {

        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(baseContext);
        bottomSheetDialog.setContentView(R.layout.om_delete_address_bottom_sheet);
        bottomSheetDialog.show();


        val address: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.address)
        val delete_btn: Button? = bottomSheetDialog.findViewById<Button>(R.id.delete_btn)
        val not_now: TextView? = bottomSheetDialog.findViewById<TextView>(R.id.not_now)

        address?.text = OrderMedicineLocationItem.display_name;


        delete_btn?.setOnClickListener {
            if (orderMedicineFavLocationItemsArrayList.size <= 1) {
                if (OrderMedicineLocationItem.fav_address!!) {
                    Toast.makeText(
                        baseContext,
                        "You can't remove all location from favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    bottomSheetDialog.dismiss()
                    deleteAddress(OrderMedicineLocationItem)
                }
            } else {
                bottomSheetDialog.dismiss()
                deleteAddress(OrderMedicineLocationItem)
            }
        }

        not_now?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

//        builder.setTitle("Title")
//            .setView(R.layout.om_delete_address_bottom_sheet)
//            .setFullWidth(false)
//            .show();
//        val address: TextView =
//            builder.view.findViewById<TextView>(R.id.address)
//        val delete_btn: Button =
//            builder.view.findViewById<Button>(R.id.delete_btn)
//        val not_now: TextView =
//            builder.view.findViewById<TextView>(R.id.not_now)

//        address.text = OrderMedicineLocationItem.display_name;
//
//
//        delete_btn.setOnClickListener {
//            if (orderMedicineFavLocationItemsArrayList.size <= 1) {
//                if (OrderMedicineLocationItem.fav_address!!) {
//                    Toast.makeText(
//                        baseContext,
//                        "You can't remove all location from favourites",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    builder.dismiss()
//                    deleteAddress(OrderMedicineLocationItem)
//                }
//            } else {
//                builder.dismiss()
//                deleteAddress(OrderMedicineLocationItem)
//            }
//        }
//
//        not_now.setOnClickListener {
//            builder.dismiss()
//        }


    }

    private fun deleteAddress(OrderMedicineLocationItem: OMAddress) {
        setLocationProgressBar.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java);

        val deleteAddressCall: Call<ProfileDashboardResponseData> = apiService.deleteAddress(
            AppConfig.APP_BRANDING_ID,
            prefManager.userToken,
            OrderMedicineLocationItem.id
        );

        deleteAddressCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                p0: Call<ProfileDashboardResponseData>,
                p1: Response<ProfileDashboardResponseData>
            ) {
                setLocationProgressBar.visibility = View.GONE
                if (p1.isSuccessful) {
                    Toast.makeText(baseContext, "Address deleted successfully", Toast.LENGTH_SHORT)
                        .show();
                    setMyLocationRecyclerView();
                }
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                setLocationProgressBar.visibility = View.GONE
                Toast.makeText(
                    baseContext,
                    "Server error, Can't delete the address",
                    Toast.LENGTH_SHORT
                ).show();
            }

        })


    }
}