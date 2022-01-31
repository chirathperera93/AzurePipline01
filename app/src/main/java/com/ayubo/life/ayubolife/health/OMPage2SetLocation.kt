package com.ayubo.life.ayubolife.health

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Paint
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_om_page1_submit.view.*
import kotlinx.android.synthetic.main.activity_om_page2_set_location.*
import kotlinx.android.synthetic.main.activity_om_page2_set_location.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OMPage2SetLocation : Fragment(),
    MedMediaPreviewCardAdapter.OnMedMediaPreviewItemClickListener,
    OrderMedicineLocationAdapter.OnOrderMedicineLocationItemClickListener {

    private var medMediaPreviewCardAdapter: MedMediaPreviewCardAdapter? = null
    var mediaPreviewCardItemsArrayList = ArrayList<OMMediaFiles>()

    var dotsImageViews: ArrayList<ImageView> = ArrayList<ImageView>();
    private var dotsCount: Int = 0;

    private var CAMERA_REQUEST: Int = 1888;
    private var MY_CAMERA_PERMISSION_CODE: Int = 100;

    var imageArr: ArrayList<RequestFileClaimImage> = ArrayList<RequestFileClaimImage>();
    var upLoadedImageList: ArrayList<String> = ArrayList<String>();
    var mLayoutManager: RecyclerView.LayoutManager? = null;

    //    var mUploadDocumentAdapter: UploadDocumentAdapter? = null;
    var mUploadDocumentList: ArrayList<UploadDocumentItem> =
        ArrayList<UploadDocumentItem>();
    lateinit var prefManager: PrefManager;

    lateinit var mainView: View;

    var isNotifyItem: Boolean = true

    private var orderMedicineLocationAdapter: OrderMedicineLocationAdapter? = null

    lateinit var changeFragmentInterface: OMPage2SetLocationNextButtonInterface;

    var orderMedicineLocationItemsArrayList: ArrayList<OMAddress> = ArrayList<OMAddress>()


    lateinit var oMCreatedOrderObj: OMCreatedOrderObj

    lateinit var authorization: String;
    var selectedFavLocation: OMAddress? = null;


    var googleApiClient: GoogleApiClient? = null;


    interface OMPage2SetLocationNextButtonInterface {
        fun onOMPage2SetLocationNextButtonClick(position: Int);

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        mainView = inflater.inflate(R.layout.activity_om_page2_set_location, container, false);

        initializeUI();



        return mainView;
    }

    override fun onResume() {
        super.onResume()
        authorization = prefManager.userToken;
        orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()
        oMCreatedOrderObj = OMCommon(requireContext()).retrieveFromCommonSingleton()
        setMediaPreviewRecycler()
        setMedicinesLocation()


        enableDeviceLocation();
    }


    private fun enableDeviceLocation() {

        val manager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as (LocationManager);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            if (googleApiClient == null) {

                googleApiClient = GoogleApiClient.Builder(requireContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                        override fun onConnected(p0: Bundle?) {
                        }

                        override fun onConnectionSuspended(p0: Int) {
                            googleApiClient!!.connect();
                        }

                    })
                    .addOnConnectionFailedListener { connectionResult ->
                        Log.d(
                            "Location error",
                            "Location error " + connectionResult.errorCode
                        );
                    }.build();
                googleApiClient!!.connect();


                val locationRequest: LocationRequest = LocationRequest.create();
                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
                locationRequest.interval = 30 * 1000;
                locationRequest.fastestInterval = 5 * 1000;
                val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

                builder.setAlwaysShow(true);

                val result: PendingResult<LocationSettingsResult> =
                    LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        builder.build()
                    );

                result.setResultCallback { locationSettingsResult ->
                    val status: Status = locationSettingsResult.status;

                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            status.startResolutionForResult(activity, 199);
                        } catch (e: IntentSender.SendIntentException) {
                        }
                    }
                }
            }

        }
    }


    private fun initializeUI() {

        prefManager = PrefManager(requireContext());

//        mainView.text_area_add_special_note.setOnTouchListener(object : View.OnTouchListener {
//            @SuppressLint("ClickableViewAccessibility")
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                isNotifyItem = true
//                return  true;
//            }
//
//        })

        mainView.text_area_add_special_note.setOnClickListener {
            isNotifyItem = true
        }

        mainView.text_area_add_special_note.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mediaPreviewCardItemsArrayList.forEachIndexed { index, medMediaPreviewCardItem ->

                    if (medMediaPreviewCardItem.isClicked == true) {
                        medMediaPreviewCardItem.note = p0.toString()


                        oMCreatedOrderObj.files = mediaPreviewCardItemsArrayList

                        val jsonObject: JsonObject =
                            Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;




                        if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
                            OMCommon(context!!).saveToDraftSingletonAndRetrieve(jsonObject)
                        }


                        oMCreatedOrderObj =
                            OMCommon(context!!).saveToCommonSingletonAndRetrieve(jsonObject)


                        if (isNotifyItem) {
                            medMediaPreviewCardAdapter!!.notifyItemChanged(index)
                        }


                    }
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                print(p0)
            }

        })

        mainView.change_location.setOnClickListener {
            if (orderMedicineLocationItemsArrayList.size == 0) {
                val intent = Intent(requireContext(), OMPage2Sub2AddNewLocation::class.java)
                intent.putExtra("goToMain", "true");
                intent.putExtra("editAddress", "");
                startActivity(intent)
            } else {
                val intent = Intent(requireContext(), OMPage2Sub1ChangeLocation::class.java)
                startActivity(intent)
            }


        }

        mainView.set_location_proceed_btn.setOnClickListener {

            oMCreatedOrderObj.address = selectedFavLocation

            val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;


            if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
                OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject)
            }

            oMCreatedOrderObj =
                OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject)





            updateOrder();


        }

    }

    private fun updateOrder() {

        val oMCreatedOrder: OMCreatedOrderObj = OMCreatedOrderObj(
            null,
            AppConfig.APP_BRANDING_ID,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            oMCreatedOrderObj.files,
            oMCreatedOrderObj.address,
            oMCreatedOrderObj.partner,
            oMCreatedOrderObj.payment
        )

        val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrder).asJsonObject;


        if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject);
        }

        OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);

        changeFragmentInterface = context as (OMPage2SetLocationNextButtonInterface);
        changeFragmentInterface.onOMPage2SetLocationNextButtonClick(3)


    }

    private fun setMediaPreviewRecycler() {
        try {
            mainView.media_preview_recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            mediaPreviewCardItemsArrayList = ArrayList<OMMediaFiles>()

            for (i in 0 until oMCreatedOrderObj.files!!.size) {
                val mediaFile = oMCreatedOrderObj.files!![i]


                mediaPreviewCardItemsArrayList.add(
                    OMMediaFiles(
                        mediaFile.URL,
                        mediaFile.MediaName,
                        mediaFile.MediaFolder,
                        mediaFile.MediaType,
                        mediaFile.note,
                        false
                    )

                )
            }

            medMediaPreviewCardAdapter =
                MedMediaPreviewCardAdapter(requireContext(), mediaPreviewCardItemsArrayList, false)
            medMediaPreviewCardAdapter!!.onMedMediaPreviewItemClickListener =
                this@OMPage2SetLocation
            mainView.media_preview_recycler_view.adapter = medMediaPreviewCardAdapter

            dotsCount = medMediaPreviewCardAdapter!!.itemCount
            mainView.media_preview_indicators_layout.removeAllViewsInLayout()
            dotsImageViews = ArrayList<ImageView>()


            for (i in 0 until dotsCount) {
                dotsImageViews.add(ImageView(requireContext()))
                dotsImageViews[i]
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.nonactive_dot
                        )
                    );
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 0, 8, 0);
                val imageView: ImageView = dotsImageViews[i];
                mainView.media_preview_indicators_layout.addView(imageView, params);
            }

            dotsImageViews[0].setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.subscribe_slide_active_dot
                )
            );

            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    println(recyclerView)
                    println(newState)


//                    for (i in 0 until dotsCount) {
//                        dotsImageViews[i].setImageDrawable(
//                            ContextCompat.getDrawable(
//                                context!!,
//                                R.drawable.nonactive_dot
//                            )
//                        );
//                    }
//
//                    dotsImageViews[newState].setImageDrawable(
//                        ContextCompat.getDrawable(
//                            context!!,
//                            R.drawable.subscribe_slide_active_dot
//                        )
//                    );
                }
            }
            mainView.media_preview_recycler_view.addOnScrollListener(scrollListener)
            mainView.text_area_add_special_note.setText(mediaPreviewCardItemsArrayList[0].note)


//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setMedicinesLocation() {
        mainView.uploadMediaProgressBar.visibility = View.VISIBLE
        mainView.set_location_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        orderMedicineLocationItemsArrayList = ArrayList<OMAddress>()

        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java);

        val getAddressesCall: Call<ProfileDashboardResponseData> = apiService.getAddresses(
            AppConfig.APP_BRANDING_ID,
            authorization
        );

        getAddressesCall.enqueue(object : Callback<ProfileDashboardResponseData> {
            override fun onResponse(
                p0: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {
                mainView.uploadMediaProgressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    print(response)

                    val dashboardMainData: JsonArray =
                        Gson().toJsonTree(response.body()!!.data).asJsonArray;
                    selectedFavLocation = null;

                    print(oMCreatedOrderObj.address)

                    if (oMCreatedOrderObj.address != null) {
                        orderMedicineLocationItemsArrayList.add(oMCreatedOrderObj.address!!);
                    }

                    if (dashboardMainData.size() > 0) {

                        for (i in 0 until dashboardMainData.size()) {
                            val location = dashboardMainData[i]

                            val oMAddressObj: OMAddress =
                                Gson().fromJson(location, OMAddress::class.java);



                            if (oMCreatedOrderObj.address != null) {
                                if (oMCreatedOrderObj.address!!.id != oMAddressObj.id) {
                                    if (location.asJsonObject.get("fav_address").asBoolean) {
                                        orderMedicineLocationItemsArrayList.add(oMAddressObj);
                                    }
                                }

                            } else {
                                if (location.asJsonObject.get("fav_address").asBoolean) {
                                    orderMedicineLocationItemsArrayList.add(oMAddressObj);
                                }
                            }


                        }

                    }



                    if (orderMedicineLocationItemsArrayList.size > 0) {
                        mainView.set_location_topic.visibility = View.VISIBLE
                        mainView.change_location.text = "Change Address"
                        mainView.change_location.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
                    } else {
                        mainView.set_location_topic.visibility = View.GONE
                        mainView.change_location.text = "Add a new address"
                        mainView.change_location.paintFlags = Paint.UNDERLINE_TEXT_FLAG;
                    }

                    orderMedicineLocationAdapter =
                        OrderMedicineLocationAdapter(
                            context!!,
                            orderMedicineLocationItemsArrayList,
                            orderMedicineLocationItemsArrayList,
                            false,
                            false,
                            true
                        )

                    try {
                        orderMedicineLocationAdapter!!.onOrderMedicineLocationItemClickListener =
                            this@OMPage2SetLocation
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    mainView.set_location_recycler_view.adapter = orderMedicineLocationAdapter


                } else {
                    Toast.makeText(
                        context,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                    ).show()
                    mainView.uploadMediaProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                Toast.makeText(
                    context!!,
                    "There is an issue when loading data, Please contact admin.",
                    Toast.LENGTH_SHORT
                ).show()
                mainView.uploadMediaProgressBar.visibility = View.GONE
            }

        })


    }


    override fun onMedMediaPreviewItemClick(
        medMediaPreviewCardItem: OMMediaFiles,
        isNotify: Boolean
    ) {
        isNotifyItem = isNotify
        mainView.text_area_add_special_note.setText(medMediaPreviewCardItem.note)
    }

    override fun orderMedicineLocationItemClick(orderMedicineLocationItem: OMAddress) {


        selectedFavLocation = orderMedicineLocationItem


        mainView.set_location_proceed_btn.isEnabled = true

        if (Constants.type == Constants.Type.LIFEPLUS) {
            mainView.set_location_proceed_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

        } else {
            mainView.set_location_proceed_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

    }

    override fun orderMedicineFavLocationItemClick(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun orderMedicineLocationEditItemClick(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }

    override fun orderMedicineLocationLongPress(OrderMedicineLocationItem: OMAddress) {
        print(OrderMedicineLocationItem)
    }


}

