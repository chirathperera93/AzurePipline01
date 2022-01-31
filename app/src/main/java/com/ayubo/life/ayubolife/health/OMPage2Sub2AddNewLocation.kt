package com.ayubo.life.ayubolife.health

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_medicine_add_new_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OMPage2Sub2AddNewLocation : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    lateinit var prefManager: PrefManager;
    lateinit var oMCreatedOrderObj: OMCreatedOrderObj
    var editOMCreateAddress: OMAddress? = null
    var isGoToMain: Boolean = false
    var isEditAddress: Boolean = false
    var namePlaceCharSequence: String = "";
    var cityCharSequence: String = "";


    //    =================================== google map =================================
    lateinit var googleMap: GoogleMap
    private var locationPermissionGranted = false
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null

    private var addressLatitude: Double = 0.0;
    private var addressLongitude: Double = 0.0;

    private val defaultLocation = LatLng(6.8700881, 79.8766771)
    //   ====================================================================================

    companion object {
        private val TAG = OMPage2Sub2AddNewLocation::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // START maps_current_place_on_create_save_instance_state
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // END maps_current_place_on_create_save_instance_state


        setContentView(R.layout.activity_medicine_add_new_location)
        getLocationPermission()
        prefManager = PrefManager(baseContext);


        val currentIntent: Intent = intent;
        val goToMain: String? = currentIntent.getStringExtra("goToMain");
        val editAddress: String? = currentIntent.getStringExtra("editAddress");

        if (goToMain !== null) {
            isGoToMain = goToMain.toBoolean();
        }


        if (!editAddress.equals("")) {
            submit_add_new_location_btn.text = "Update Address"
            main_topic_text_view.text = "Update the Location"
            if (Constants.type == Constants.Type.LIFEPLUS) {
                submit_add_new_location_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

            } else {
                submit_add_new_location_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
            }
            isEditAddress = true
            editOMCreateAddress = Gson().fromJson(editAddress, OMAddress::class.java);

            if (editOMCreateAddress!!.latitude != null) {
                addressLatitude = editOMCreateAddress!!.latitude!!;
            }

            if (editOMCreateAddress!!.longitude != null) {
                addressLongitude = editOMCreateAddress!!.longitude!!
            }


            name_place.setText(editOMCreateAddress!!.display_name)
            house_number_text.setText(editOMCreateAddress!!.house_number)
            street_text.setText(editOMCreateAddress!!.street)
            address_line_1.setText(editOMCreateAddress!!.address_line1)
            address_line_2.setText(editOMCreateAddress!!.address_line2)
            landmark_text.setText(editOMCreateAddress!!.landmark)
            city_text.setText(editOMCreateAddress!!.city)
            postal_code_text.setText(editOMCreateAddress!!.postalcode)


        } else {
            main_topic_text_view.text = "Add a new location"
            if (isGoToMain) {
                main_topic_text_view.text = "Add favourite location"
            }

            submit_add_new_location_btn.text = "Add Address"
            submit_add_new_location_btn.isEnabled = false
            submit_add_new_location_btn.setTextColor(resources.getColor(R.color.white))
            submit_add_new_location_btn.setBackgroundResource(R.drawable.radius_background_disable)
            isEditAddress = false
            editOMCreateAddress = null

            name_place.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (charSequence != null) {

                        namePlaceCharSequence = charSequence.replace("\\s".toRegex(), "")
                        if (namePlaceCharSequence != "" && cityCharSequence != "") {
                            submit_add_new_location_btn.isEnabled = true
                            if (Constants.type == Constants.Type.LIFEPLUS) {
                                submit_add_new_location_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

                            } else {
                                submit_add_new_location_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
                            }
                        } else {
                            submit_add_new_location_btn.isEnabled = false
                            submit_add_new_location_btn.setTextColor(resources.getColor(R.color.white))
                            submit_add_new_location_btn.setBackgroundResource(R.drawable.radius_background_disable)
                        }
                    } else {
                        submit_add_new_location_btn.isEnabled = false
                        submit_add_new_location_btn.setTextColor(resources.getColor(R.color.white))
                        submit_add_new_location_btn.setBackgroundResource(R.drawable.radius_background_disable)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

            city_text.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (charSequence != null) {

                        cityCharSequence = charSequence.replace("\\s".toRegex(), "")
                        if (cityCharSequence != "" && namePlaceCharSequence != "") {
                            submit_add_new_location_btn.isEnabled = true
                            if (Constants.type == Constants.Type.LIFEPLUS) {
                                submit_add_new_location_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)

                            } else {
                                submit_add_new_location_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
                            }
                        } else {
                            submit_add_new_location_btn.isEnabled = false
                            submit_add_new_location_btn.setTextColor(resources.getColor(R.color.white))
                            submit_add_new_location_btn.setBackgroundResource(R.drawable.radius_background_disable)
                        }
                    } else {
                        submit_add_new_location_btn.isEnabled = false
                        submit_add_new_location_btn.setTextColor(resources.getColor(R.color.white))
                        submit_add_new_location_btn.setBackgroundResource(R.drawable.radius_background_disable)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        }

        initializeUI(savedInstanceState)
    }

    private fun initializeUI(savedInstanceState: Bundle?) {

//        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromDraftSingleton()
        oMCreatedOrderObj = OMCommon(baseContext).retrieveFromCommonSingleton()

        back_topic_layout.setOnClickListener {
            finish();
        }

        submit_add_new_location_btn.setOnClickListener {
            if (!isEditAddress) {
                val name_place = name_place.text
                val house_number_text = house_number_text.text
                val street_text = street_text.text
                val city_text = city_text.text
                val address_line_1 = address_line_1.text
                val address_line_2 = address_line_2.text
                val landmark_text = landmark_text.text
                val postal_code_text = postal_code_text.text



                addLocationProgressBar.visibility = View.VISIBLE
                val apiService: ApiInterface =
                    ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java);


                val createAddress: OMAddress =
                    OMAddress(
                        null,
                        null,
                        null,
                        name_place.toString(),
                        house_number_text.toString(),
                        street_text.toString(),
                        address_line_1.toString(),
                        address_line_2.toString(),
                        landmark_text.toString(),
                        city_text.toString(),
                        postal_code_text.toString(),
                        addressLatitude,
                        addressLongitude,
                        isGoToMain,
                        false

                    )


                val createAddressCall: Call<ProfileDashboardResponseData> =
                    apiService.createAddress(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.userToken,
                        createAddress
                    );


                createAddressCall.enqueue(object : Callback<ProfileDashboardResponseData> {
                    override fun onResponse(
                        p0: Call<ProfileDashboardResponseData>,
                        respose: Response<ProfileDashboardResponseData>
                    ) {

                        addLocationProgressBar.visibility = View.GONE
                        if (respose.isSuccessful) {


                            if (isGoToMain) {
                                finish();
                                Toast.makeText(
                                    baseContext,
                                    "Added the location to Favourites successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    baseContext,
                                    "Location is added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(baseContext, OMPage2Sub1ChangeLocation::class.java)
                                startActivity(intent)
                                finish();
                            }
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                        addLocationProgressBar.visibility = View.GONE
                    }

                })
            } else {
                editOMCreateAddress!!.display_name = name_place.text.toString();
                editOMCreateAddress!!.house_number = house_number_text.text.toString();
                editOMCreateAddress!!.street = street_text.text.toString();
                editOMCreateAddress!!.address_line1 = address_line_1.text.toString();
                editOMCreateAddress!!.address_line2 = address_line_2.text.toString();
                editOMCreateAddress!!.landmark = landmark_text.text.toString();
                editOMCreateAddress!!.city = city_text.text.toString();
                editOMCreateAddress!!.postalcode = postal_code_text.text.toString();
                editOMCreateAddress!!.latitude = addressLatitude
                editOMCreateAddress!!.longitude = addressLongitude

                updateAddress(editOMCreateAddress!!)

            }


        }


        if (Constants.type == Constants.Type.LIFEPLUS) {
            pin_for_location.setBackgroundResource(R.drawable.life_plus_map_pin)

        } else {
            pin_for_location.setBackgroundResource(R.drawable.ayubo_map_pin)
        }

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        map_view_for_g_map.getMapAsync(this)
        map_view_for_g_map.onCreate(savedInstanceState)
        if (map_view_for_g_map != null) {
            map_view_for_g_map.getMapAsync(this)
        }

    }

    private fun updateAddress(orderMedicineLocationItem: OMAddress) {
        addLocationProgressBar.visibility = View.VISIBLE
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
                Toast.makeText(
                    baseContext,
                    "Modified the location successfully",
                    Toast.LENGTH_SHORT
                ).show()
                addLocationProgressBar.visibility = View.GONE
                finish()
            }

            override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                addLocationProgressBar.visibility = View.GONE
                Toast.makeText(
                    baseContext,
                    "Server Error",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })

    }


    //  START maps_current_place_on_save_instance_state
    override fun onSaveInstanceState(outState: Bundle) {
        this.googleMap.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    // END maps_current_place_on_save_instance_state

    override fun onResume() {
        super.onResume()
        map_view_for_g_map.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {


        this.googleMap = googleMap

// Prompt the user for permission.
        getLocationPermission()


        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        var selectedLatitude: Double = 0.0;
        var selectedLongitude: Double = 0.0;

        this.googleMap.setOnCameraChangeListener { cameraPosition ->
            selectedLatitude = cameraPosition.target.latitude
            selectedLongitude = cameraPosition.target.longitude

        }

        this.googleMap.setOnCameraMoveStartedListener {
            runOnUiThread {
                moving_image.visibility = View.VISIBLE
            }
        }

        this.googleMap.setOnCameraIdleListener {
            runOnUiThread {
                moving_image.visibility = View.GONE
                getAddress(selectedLatitude, selectedLongitude);
            }
        }
    }

    //  maps_current_place_location_permission
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    //  maps_current_place_get_device_location
    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.

                        if (isEditAddress) {
                            val deviceLocation =
                                LatLng(addressLatitude, addressLongitude);
                            this.googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    deviceLocation, DEFAULT_ZOOM.toFloat()
                                )
                            )

                        } else {
                            lastKnownLocation = task.result
                            if (lastKnownLocation != null) {
                                val deviceLocation =
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    );
                                this.googleMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        deviceLocation, DEFAULT_ZOOM.toFloat()
                                    )
                                )
                            }
                        }


                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        this.googleMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        this.googleMap.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }


    //  START maps_current_place_update_location_ui
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (this.googleMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                this.googleMap.isMyLocationEnabled = true
                this.googleMap.uiSettings.isMyLocationButtonEnabled = true
                this.googleMap.uiSettings.isZoomControlsEnabled = true;
            } else {
                this.googleMap.isMyLocationEnabled = false
                this.googleMap.uiSettings.isMyLocationButtonEnabled = false
                this.googleMap.uiSettings.isZoomControlsEnabled = false;
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getAddress(latitude: Double, longitude: Double) {
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault());

        val addresses: List<Address> = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        var addressVal: String = "";
        var cityVal: String = "";
        var stateVal: String = "";
        var countryVal: String = "";
        var postalCodeVal: String = "";
        var knownNameVal: String = "";
        var subAdminArea: String = "";
        var premises: String = "";
        var subLocality: String = "";
        var subThoroughfare: String = "";
        var thoroughfare: String = "";

        if (addresses.isNotEmpty()) {
            addressVal =
                addresses[0].getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


            cityVal = if (addresses[0].locality != null) addresses[0].locality else "";
            stateVal = if (addresses[0].adminArea != null) addresses[0].adminArea else "";
            countryVal = if (addresses[0].countryName != null) addresses[0].countryName else "";
            postalCodeVal = if (addresses[0].postalCode != null) addresses[0].postalCode else "";
            knownNameVal =
                if (addresses[0].featureName != null) addresses[0].featureName else ""; // Only if available else return NULL


            premises = if (addresses[0].premises != null) addresses[0].premises else "";
            subAdminArea = if (addresses[0].subAdminArea != null) addresses[0].subAdminArea else "";
            subLocality = if (addresses[0].subLocality != null) addresses[0].subLocality else "";
            subThoroughfare =
                if (addresses[0].subThoroughfare != null) addresses[0].subThoroughfare else "";
            thoroughfare = if (addresses[0].thoroughfare != null) addresses[0].thoroughfare else "";


            println("address - $addressVal")

            println("city - $cityVal")

            println("state - $stateVal")

            println("country - $countryVal")

            println("postalCode - $postalCodeVal")

            println("knownName - $knownNameVal")

            println("latitude - $latitude")

            println("longitude - $longitude")

            println("subAdminArea - $subAdminArea")

            println("premises - $premises")

            println("subLocality - $subLocality")

            println("subThoroughfare - $subThoroughfare")

            println("thoroughfare - $thoroughfare")


            house_number_text.setText(subThoroughfare);
            street_text.setText(thoroughfare);
            city_text.setText(cityVal);
            postal_code_text.setText(postalCodeVal);
            addressLatitude = latitude
            addressLongitude = longitude


        }


    }

    private fun getLatitudeLongitude(strAddress: String) {
        val coder: Geocoder = Geocoder(this);
        val address: List<Address>;

        try {
            address = coder.getFromLocationName(strAddress, 5);

            val location: Address = address[0];
            val lat = location.latitude;
            val lon = location.longitude;
            println("latitude ===============================================================  $lat")
            println("longitude ===============================================================  $lon")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker!!.tag != null) {
            val key = marker.tag!!.toString()

            print(key)

        }
        return false
    }


}