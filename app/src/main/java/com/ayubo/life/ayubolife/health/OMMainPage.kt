package com.ayubo.life.ayubolife.health


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.activity.PurchaseHistory
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.flavors.changes.Constants
import com.google.android.gms.common.api.GoogleApiClient
import com.kofigyan.stateprogressbar.StateProgressBar
import kotlinx.android.synthetic.main.activity_ommain_page.*

class OMMainPage : BaseActivity(),
    OMPage1Submit.ChangeFragmentInterface,
    OMPage1Submit.SubmitButtonInterface,
    OMPage1Submit.GoToHistoryInterface,
    OMPage2SetLocation.OMPage2SetLocationNextButtonInterface,
    OMPage3SelectPartner.OMPage3SelectPartnerNextButtonInterface,
    OMPage4PaymentDetail.OMPage4PaymentDetailNextButtonInterface {
    var CURRENT_TAG: String = "OMMainPage";

    var currentPosition: Int = -1

    lateinit var oMCreatedOrderObj: OMCreatedOrderObj
    var isFromHistory: Boolean = false

    var googleApiClient: GoogleApiClient? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ommain_page)
        isFromHistory = false
        submit_proceed_btn.isEnabled = false;
        submit_proceed_btn.setBackgroundResource(R.drawable.radius_background_disable)

//        loadFragments(1)
        initializeUI()

        getSingletonOMData()


        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

    }

    private fun getSingletonOMData() {


        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton()

        if (oMCreatedOrderObj.status == null) {
            loadFragments(1, true)
        } else {
            if (oMCreatedOrderObj.status.equals("Delivered") || oMCreatedOrderObj.status.equals("Cancelled")) {
                loadFragments(1)
            } else {
                loadFragments(4)
            }


        }


    }

    override fun onResume() {
        super.onResume()
        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton()
        if (oMCreatedOrderObj.status == null && oMCreatedOrderObj.files != null) {
            submit_proceed_btn.isEnabled = true;

            if (Constants.type == Constants.Type.LIFEPLUS) {
                submit_proceed_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

            } else {
                submit_proceed_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
            }


        } else {
            submit_proceed_btn.isEnabled = false;
            submit_proceed_btn.setBackgroundResource(R.drawable.radius_background_disable)
        }


//        enableDeviceLocation();

    }

//    private fun enableDeviceLocation() {
//
//        val manager: LocationManager =
//            getSystemService(Context.LOCATION_SERVICE) as (LocationManager);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//
//
//            if (googleApiClient == null) {
//
//                googleApiClient = GoogleApiClient.Builder(this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
//                        override fun onConnected(p0: Bundle?) {
//                        }
//
//                        override fun onConnectionSuspended(p0: Int) {
//                            googleApiClient!!.connect();
//                        }
//
//                    })
//                    .addOnConnectionFailedListener { connectionResult ->
//                        Log.d(
//                            "Location error",
//                            "Location error " + connectionResult.errorCode
//                        );
//                    }.build();
//                googleApiClient!!.connect();
//
//
//                val locationRequest: LocationRequest = LocationRequest.create();
//                locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
//                locationRequest.interval = 30 * 1000;
//                locationRequest.fastestInterval = 5 * 1000;
//                val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//                builder.setAlwaysShow(true);
//
//                val result: PendingResult<LocationSettingsResult> =
//                    LocationServices.SettingsApi.checkLocationSettings(
//                        googleApiClient,
//                        builder.build()
//                    );
//
//                result.setResultCallback { locationSettingsResult ->
//                    val status: Status = locationSettingsResult.status;
//
//                    if (status.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                        try {
//                            status.startResolutionForResult(this, 199);
//                        } catch (e: IntentSender.SendIntentException) {
//                        }
//                    }
//                }
//            }
//
//        }
//    }


    private fun loadFragments(position: Int, isOpenPopUp: Boolean = false) {
        // update the main content by replacing fragments
        var fragment: Fragment? = null;

        currentPosition = position

        om_main_scroll_view.fullScroll(View.FOCUS_UP);

        when (position) {
            1 -> {
                submit_proceed_btn_main.visibility = View.VISIBLE
                submit_proceed_btn.isEnabled = false;
                submit_proceed_btn.setBackgroundResource(R.drawable.radius_background_disable)
                fragment = OMPage1Submit(isOpenPopUp)
                step_navigation.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
            }
            2 -> {

                submit_proceed_btn_main.visibility = View.GONE
                fragment = OMPage2SetLocation();
                step_navigation.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            }
            3 -> {

                submit_proceed_btn_main.visibility = View.GONE
                fragment = OMPage3SelectPartner();
                step_navigation.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
            }
            4 -> {

                submit_proceed_btn_main.visibility = View.GONE
                fragment = OMPage4PaymentDetail();
                step_navigation.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            }
            else -> {

            }
        }

        if (fragment != null) {
            val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction();
//            fragmentTransaction.setCustomAnimations(
//                android.R.anim.fade_in,
//                android.R.anim.fade_out
//            );

            fragmentTransaction.replace(R.id.om_fragments, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();


        } else {
            // error in creating fragment
            Toast.makeText(baseContext, "Error in creating fragment", Toast.LENGTH_SHORT).show()

        }
    }

    var descriptionData = arrayOf(
        "Upload\nPrescription",
        "Pick\nDelivery Location",
        "Select\nPharmacy",
        "Pay\nthe Amount"
    );

    private fun initializeUI() {
        page_back_btn_submit_om_main.setOnClickListener {
            onBackPressed()
        }


        step_navigation.setStateDescriptionData(descriptionData)

//        step_navigation.setStateDescriptionTypeface("fonts/montserrat_semi_bold.ttf");
        step_navigation.setStateDescriptionTypeface("font/montserrat_semi_bold.ttf");
        step_navigation.setStateNumberTypeface("font/montserrat_bold.ttf");


        submit_proceed_btn.setOnClickListener {
            createNewOrder()
        }


    }

    override fun onOMPage1SubmitNextButtonClick(
        position: Int
    ) {
        loadFragments(position)
    }

    override fun onMedicineHistoryOrderButtonClick(oMCreatedOrderObj: OMCreatedOrderObj) {
        print(oMCreatedOrderObj)

        processAction("payment", "prescriptionorder:${oMCreatedOrderObj.id}")
    }

    override fun onOMPage2SetLocationNextButtonClick(position: Int) {
        loadFragments(position)
    }

    override fun onOMPage3SelectPartnerNextButtonClick(position: Int) {
        loadFragments(position)
    }


    override fun onBackPressed() {
//        super.onBackPressed()
        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton()

        print(oMCreatedOrderObj.status)


        if (oMCreatedOrderObj.status != null) {

            finish()

        } else {
            currentPosition -= 1;
            moveTaskToBack(false)
            if (currentPosition != 0) {
                loadFragments(currentPosition)
                return
            } else {
                super.onBackPressed()
            }
        }


    }

    override fun onOMPage4PaymentDetailNextButtonClick(position: Int) {
        loadFragments(position)
    }

    override fun onGoToHistoryInterfaceClick() {
//        val intent = Intent(baseContext, OMViewHistory::class.java)
//        startActivity(intent)
//        finish()

        val intent: Intent = Intent(baseContext, PurchaseHistory::class.java);
        intent.putExtra("previous_activity", "order_medicine")
        startActivity(intent);
        finish()
    }

    private fun createNewOrder() {

        submit_proceed_btn_main.visibility = View.GONE

        loadFragments(2);

    }

    override fun onSubmitButtonClick(isButtonEnabled: Boolean) {
        if (isButtonEnabled) {
            submit_proceed_btn.isEnabled = true;

            if (Constants.type == Constants.Type.LIFEPLUS) {
                submit_proceed_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

            } else {
                submit_proceed_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
            }
        } else {
            submit_proceed_btn.isEnabled = false;
            submit_proceed_btn.setBackgroundResource(R.drawable.radius_background_disable)
        }
    }

}