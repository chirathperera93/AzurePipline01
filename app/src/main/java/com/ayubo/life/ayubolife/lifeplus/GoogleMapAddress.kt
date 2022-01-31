package com.ayubo.life.ayubolife.lifeplus

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.ayubo.life.ayubolife.R
import com.flavors.changes.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_google_map_address.*
import java.util.*

class GoogleMapAddress : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    lateinit var googleMap: GoogleMap
    private var locationPermissionGranted = false
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lastKnownLocation: Location? = null


    private val defaultLocation = LatLng(6.8700881, 79.8766771)


    companion object {
        private val TAG = GoogleMapAddress::class.java.simpleName
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


        // [START maps_current_place_on_create_save_instance_state]
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // [END maps_current_place_on_create_save_instance_state]

        setContentView(R.layout.activity_google_map_address)

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

    // [START maps_current_place_on_save_instance_state]
    override fun onSaveInstanceState(outState: Bundle) {
        this.googleMap.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }
    // [END maps_current_place_on_save_instance_state]


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
            println("centerLat ================== " + cameraPosition.target.latitude.toString());

            println("centerLong =============== " + cameraPosition.target.longitude.toString());
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


    // [START maps_current_place_location_permission]
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
    // [END maps_current_place_location_permission]

    // [START maps_current_place_get_device_location]
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
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {


                            val deviceLocation =
                                LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude);
                            this.googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    deviceLocation, DEFAULT_ZOOM.toFloat()
                                )
                            )
//                            this.googleMap.addMarker(
//                                MarkerOptions()
//                                    .title("")
//                                    .snippet("")
//                                    .position(deviceLocation)
//                            );
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        this.googleMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
//                        this.googleMap.addMarker(
//                            MarkerOptions()
//                                .title("")
//                                .snippet("")
//                                .position(defaultLocation)
//                        );
                        this.googleMap.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    // [END maps_current_place_get_device_location]


    // [START maps_current_place_update_location_ui]
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
    // [END maps_current_place_update_location_ui]

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker!!.tag != null) {
            val key = marker.tag!!.toString()

            print(key)

        }
        return false
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

        if (addresses.isNotEmpty()) {
            addressVal =
                addresses[0].getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()


            cityVal = if (addresses[0].locality != null) addresses[0].locality else "";
            stateVal = if (addresses[0].adminArea != null) addresses[0].adminArea else "";
            countryVal = if (addresses[0].countryName != null) addresses[0].countryName else "";
            postalCodeVal = if (addresses[0].postalCode != null) addresses[0].postalCode else "";
            knownNameVal =
                if (addresses[0].featureName != null) addresses[0].featureName else ""; // Only if available else return NULL

            println("address - $addressVal")
            address.text = "address - $addressVal"

            println("city - $cityVal")
            city.text = "city - $cityVal"

            println("state - $stateVal")
            state.text = "state - $stateVal"

            println("country - $countryVal")
            country.text = "country - $countryVal"

            println("postalCode - $postalCodeVal")
            postalCode.text = "postalCode - $postalCodeVal"

            println("knownName - $knownNameVal")
            knownName.text = "knownName - $knownNameVal"

            println("latitude - $latitude")
            latitudeName.text = "latitude - $latitude"

            println("longitude - $longitude")
            longitudeName.text = "longitude - $longitude"


            getLatitudeLongitude(addressVal)
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
}