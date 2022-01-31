package com.ayubo.life.ayubolife.map_challange


import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.challenges.Cards
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices
import com.ayubo.life.ayubolife.map_challange.activity.JournalActivity
import com.ayubo.life.ayubolife.map_challange.model.LocationData
import com.ayubo.life.ayubolife.map_challange.model.apChallangeStepsMainResponse
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity
import com.ayubo.life.ayubolife.map_challenges.QuitChallangeActivity
import com.ayubo.life.ayubolife.map_challenges.ShareMapPosition_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_CITY_LIST
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.payment.EXTRA_CURRENT_STEPS
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.utility.Utility
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.huawei.hms.maps.HuaweiMap
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_map_challangenew.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MapChallangeKActivity : BaseActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    DagableViewAdapter.OnItemClickListener,
    com.huawei.hms.maps.OnMapReadyCallback,
    HuaweiMap.OnMarkerClickListener {


    override fun onProcessAction(action: String, meta: String) {
        processAction(action, meta)
    }

    override fun sharePosition(card: Cards) {
        val intent = Intent(this@MapChallangeKActivity, ShareMapPosition_Activity::class.java)
        intent.putExtra("share_cityName", card.city)
        intent.putExtra("share_completed_steps", card.steps)
        intent.putExtra("share_noofDays", card.days)
        intent.putExtra("status", card.status)
        intent.putExtra("share_image", card.share_image)
        startActivity(intent)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker!!.tag != null) {
            val key = marker.tag!!.toString()

            if ((key == "start_position") || (key == "end_position") || (key == "currentUserImagePosition") || (key == "currentUserStepsPosition")) {
                //Do nothing
            } else {
                if (key.startsWith("treatures")) {
                    marker.remove()
                    try {
                        val parts: Array<String> = key.split("_".toRegex()).toTypedArray()
                        val tresurePosition = parts[1]
                        val obj = treatures[tresurePosition.toInt()]
                        onProcessAction(obj.action, obj.meta)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (key.startsWith("journal")) {
                    try {
                        val parts: Array<String> = key.split("_".toRegex()).toTypedArray()
                        val cityPosition = parts[1]
                        val obj = journalCities!![cityPosition.toInt()]
                        if (currentSteps >= obj.steps) {
                            onProcessAction(obj.action!!, obj.link!!)
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

        }
        return false
    }

    override fun onMarkerClick(marker: com.huawei.hms.maps.model.Marker?): Boolean {
        if (marker!!.tag != null) {
            val key = marker.tag!!.toString()

            if ((key == "start_position") || (key == "end_position") || (key == "currentUserImagePosition") || (key == "currentUserStepsPosition")) {
                //Do nothing
            } else {
                if (key.startsWith("treatures")) {
                    marker.remove()
                    try {
                        val parts: Array<String> = key.split("_".toRegex()).toTypedArray()
                        val tresurePosition = parts[1]
                        val obj = treatures[tresurePosition.toInt()]
                        onProcessAction(obj.action, obj.meta)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (key.startsWith("journal")) {
                    try {
                        val parts: Array<String> = key.split("_".toRegex()).toTypedArray()
                        val cityPosition = parts[1]
                        val obj = journalCities!![cityPosition.toInt()]
                        if (currentSteps >= obj.steps) {
                            onProcessAction(obj.action!!, obj.link!!)
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

        }
        return false
    }

    private val TAG = "MAP Activity"
    lateinit var treatures: List<com.ayubo.life.ayubolife.challenges.Treatures>
    // private var SlidingUpLayout: SlidingUpPanelLayout? = null

    var roadsArry: JSONArray? = null
    var isAlreadyCashed = false
    var mainResponse: apChallangeStepsMainResponse? = null
    var roadPathDataList: List<LocationData>? = null
    var roadTrees: ArrayList<LocationData>? = null
    var roadPendingToWalk: ArrayList<LocationData>? = null
    var roadAlreadyWalked: ArrayList<LocationData>? = null
    var journalCities: ArrayList<LocationData>? = null

    var nestCity: String? = null
    var nestCitySteps: Int = 0

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var markerOptions: MarkerOptions
    private lateinit var marker: Marker
    private lateinit var cameraPosition: CameraPosition
    var defaultLongitude = -122.088426
    var defaultLatitude = 37.388064
    lateinit var googleMap: GoogleMap
    lateinit var huaweiMap: HuaweiMap
    var currentItem: LocationData? = null
    var firstItem: LocationData? = null
    var lastItem: LocationData? = null

    var challangeID: String = ""
    var isCityFileNull: Boolean = false;
    lateinit var userid_ExistingUser: String
    var currentSteps: Int = 0

    var currentLat = 0.0
    var currentLongi: kotlin.Double = 0.0
    var current_distance: kotlin.Double = 0.0
    var zoomLevel: Double = 12.0
    private var pref: PrefManager? = null
    lateinit var cityJsonString: String
    val MAPVIEW_BUNDLE_KEY: String = "MapViewBundleKey";
    var googleSupportServices: GoogleSupportServices =
        GoogleSupportServices(baseContext);

    @Inject
    lateinit var mapChallangeVM: MapChallangeVM

    private fun readExtras() {

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_CHALLANGE_ID)) {
            challangeID = bundle.getSerializable(EXTRA_CHALLANGE_ID) as String
            if (challangeID.isNotEmpty()) {

//                val cityFile = readJSONFromAsset(challangeID)

                //    challangeID="d28f0063-caf9-11ea-8f53-000d3aa00fd6"
                userid_ExistingUser = pref!!.loginUser["uid"]!!

//                if (cityFile == null) {
//                    isCityFileNull = true;
//                    getRoadJSONAtFirstTime(userid_ExistingUser, challangeID)
                // goes to new method
//                    getAdventureChallengeRoute();


//                } else {
//                    isCityFileNull = false;
                getChallengeStepsData(challangeID)
//                }


            }
        }
    }


    companion object {
        fun startActivity(context: Context?, challangeID: String) {
            val intent = Intent(context, MapChallangeKActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_ID, challangeID)
            context!!.startActivity(intent)
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        //  this.googleMap.isMyLocationEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_challangenew)
        App.getInstance().appComponent.inject(this)
        googleMapViewnew.visibility = View.GONE
        huaweiMapViewnew.visibility = View.GONE
        googleSupportServices =
            GoogleSupportServices(baseContext);
        isCityFileNull = false;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        initView()

        setUpMap()


        if (!googleSupportServices.isGooglePlayServicesAvailable) {
            googleMapViewnew.visibility = View.GONE
            huaweiMapViewnew.visibility = View.VISIBLE
            var mapViewBundle: Bundle? = null;
            if (savedInstanceState != null) {
                mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
            }
            huaweiMapViewnew.getMapAsync(this)
            huaweiMapViewnew.onCreate(mapViewBundle)
            if (huaweiMapViewnew != null) {
                huaweiMapViewnew.getMapAsync(this)
            }
        } else {
            googleMapViewnew.visibility = View.VISIBLE
            huaweiMapViewnew.visibility = View.GONE
            googleMapViewnew.getMapAsync(this)
            googleMapViewnew.onCreate(savedInstanceState)
            if (googleMapViewnew != null) {
                googleMapViewnew.getMapAsync(this)
            }
        }



        if (isOnline(this@MapChallangeKActivity)) {
            readExtras()
        } else {
            Toast.makeText(
                this@MapChallangeKActivity,
                "No active internet connection",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    override fun onResume() {
        super.onResume()


        if (googleSupportServices.isGooglePlayServicesAvailable) {
            googleMapViewnew.onResume()
        } else {
            huaweiMapViewnew.onResume()
        }

    }

    fun initView() {
        pref = PrefManager(this)

        btn_leave_challenge.setOnClickListener {
            userid_ExistingUser = pref!!.loginUser["uid"]!!
            val intent = Intent(this, QuitChallangeActivity::class.java)
            intent.putExtra("user", userid_ExistingUser)
            intent.putExtra("challenge_id", challangeID)
            startActivity(intent)
        }
        btn_appointmentright.setOnClickListener {
            val intent = Intent(this, JournalActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_CITY_LIST, journalCities)
            intent.putExtra(EXTRA_CURRENT_STEPS, currentSteps)
            startActivity(intent)
        }
    }

    private fun getPathObjectsData(currentSteps: Int) {

        proDialog.visibility = View.VISIBLE
        var dataList: String? = null
        try {
            val cityFile = readJSONFromAsset(challangeID)

//           dataList = JSONArray(cityFile).toString()


            val jsonElement: JsonElement? = JsonParser().parse(cityFile);
            if (jsonElement!!.isJsonArray()) {
                dataList = JSONArray(cityFile).toString()
            } else {
                val obj: JSONObject = JSONObject(cityFile);
                dataList = obj.get("route").toString();
            }

            proDialog.visibility = View.GONE

//            val obj: JSONObject = JSONObject(cityFile);
//
//            if (obj.has("route")) {
//                dataList = obj.get("route").toString();
//            } else {
//                dataList = JSONArray(cityFile).toString()
//            }


            Log.d("=======ROAD===========", dataList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        roadTrees = ArrayList<LocationData>()
        roadPendingToWalk = ArrayList<LocationData>()
        roadAlreadyWalked = ArrayList<LocationData>()
        journalCities = ArrayList<LocationData>()

        var roadList = ArrayList<LocationData>()
        try {

            val gson = Gson()
            val type = object : TypeToken<java.util.ArrayList<LocationData?>?>() {}.type
            val locationLists = gson.fromJson<java.util.ArrayList<LocationData>>(dataList, type)

            roadList.addAll(locationLists)
            var isCurrentItem: Boolean = true
            var isFirstItem: Boolean = true
            for (i in 0 until roadList.size) {

                val item = roadList[i]

                //Setting First Item
                if (isFirstItem) {
                    firstItem = item
                    isFirstItem = false
                }

                if (item.link!!.length > 1) {
                    roadTrees!!.add(item)
                }

                if (item.city!!.length > 1) {
                    journalCities!!.add(item)
                }
                if (currentSteps < item.steps) {
                    roadPendingToWalk!!.add(item)

                    if (isCurrentItem) {
                        Log.d("==================", item.lat.toString())
                        currentItem = item
                        isCurrentItem = false
                        nestCity = item.nextcity
                        nestCitySteps = item.stepstonext
                        currentLat = item.lat
                        currentLongi = item.long
                        zoomLevel = item.zooml!!.toDouble()
                        roadAlreadyWalked!!.add(item)
                    }

                    //Setting Last Item
                    lastItem = item
                } else {

                    roadAlreadyWalked!!.add(item)
                    lastItem = item
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        if (currentItem == null) {
            currentItem = lastItem
        }

        if (googleSupportServices.isGooglePlayServicesAvailable) {
            drawWalkedRoad(roadAlreadyWalked!!)

            drawPendingRoad(roadPendingToWalk!!)

            val currentPlace = LatLng(currentItem!!.lat, currentItem!!.long)

            val currentPlaceTop = LatLng((currentItem!!.lat + 0.00010), currentItem!!.long)
            showUserSteps("", currentPlaceTop, "currentUserStepsPosition")

            showUserImage("", currentPlace, "currentUserImagePosition")

            showJournalsCities(journalCities!!)

            this.googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentPlace,
                    getZoomLevel(zoomLevel)
                )
            )
        } else {

            drawWalkedRoadForHuawei(roadAlreadyWalked!!)

            drawPendingRoadForHuawei(roadPendingToWalk!!)

            val currentPlace =
                com.huawei.hms.maps.model.LatLng(currentItem!!.lat, currentItem!!.long)

            val currentPlaceTop =
                com.huawei.hms.maps.model.LatLng((currentItem!!.lat + 0.00010), currentItem!!.long)
            showUserStepsForHuawei("", currentPlaceTop, "currentUserStepsPosition")

            showUserImageForHuawei("", currentPlace, "currentUserImagePosition")

            showJournalsCities(journalCities!!)

            this.huaweiMap.moveCamera(
                com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom(
                    currentPlace,
                    getZoomLevel(zoomLevel)
                )
            )
        }


    }

    private fun drawWalkedRoad(roadPendingToWalk: ArrayList<LocationData>) {
        var pointsPending: MutableList<LatLng>? = null
        pointsPending = java.util.ArrayList()

        // LOAD ROAD DOTS......................
        for (i in 0 until roadPendingToWalk.size) {
            val roadPath = LatLng(roadPendingToWalk[i].lat, roadPendingToWalk[i].long)
            pointsPending.add(roadPath)
        }

        val polylineOptionsWalked = PolylineOptions()
        polylineOptionsWalked.addAll(pointsPending)
        polylineOptionsWalked.geodesic(true)
        polylineOptionsWalked.width(10f)
        polylineOptionsWalked.color(Color.parseColor("#ed8414"))
        googleMap.addPolyline(polylineOptionsWalked)
    }

    private fun drawWalkedRoadForHuawei(roadPendingToWalk: ArrayList<LocationData>) {
        var pointsPending: MutableList<com.huawei.hms.maps.model.LatLng>? = null
        pointsPending = java.util.ArrayList()

        // LOAD ROAD DOTS......................
        for (i in 0 until roadPendingToWalk.size) {
            val roadPath = com.huawei.hms.maps.model.LatLng(
                roadPendingToWalk[i].lat,
                roadPendingToWalk[i].long
            )
            pointsPending.add(roadPath)
        }

        val polylineOptionsWalkedHuawei = com.huawei.hms.maps.model.PolylineOptions()
        polylineOptionsWalkedHuawei.addAll(pointsPending)
        polylineOptionsWalkedHuawei.geodesic(true)
        polylineOptionsWalkedHuawei.width(10f)
        polylineOptionsWalkedHuawei.color(Color.parseColor("#ed8414"))
        huaweiMap.addPolyline(polylineOptionsWalkedHuawei)
    }

    private fun drawPendingRoad(roadPendingToWalk: ArrayList<LocationData>) {
        val PATTERN_DASH_LENGTH_PX = 20
        val PATTERN_GAP_LENGTH_PX = 20
        val DOT: PatternItem = Dot()
        val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
        val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
        val PATTERN_POLYGON_ALPHA = Arrays.asList<PatternItem>(GAP, DOT)


        // val patternPoligonAlpha = listOf<PatternItem>(GAP, DOT)
        var points2: MutableList<LatLng>? = null
        points2 = java.util.ArrayList()

        // LOAD ROAD DOTS......................
        for (i in 0 until roadPendingToWalk.size) {
            val roadPath = LatLng(roadPendingToWalk[i].lat, roadPendingToWalk[i].long)
            points2.add(roadPath)
        }

        val polylineOptions2 = PolylineOptions()
        polylineOptions2.addAll(points2)
        polylineOptions2.geodesic(true)
        polylineOptions2.width(10f)
        polylineOptions2.color(Color.parseColor("#ffffff"))
        polylineOptions2.pattern(PATTERN_POLYGON_ALPHA)
        googleMap.addPolyline(polylineOptions2)
    }

    private fun drawPendingRoadForHuawei(roadPendingToWalk: ArrayList<LocationData>) {
        val PATTERN_DASH_LENGTH_PX = 20
        val PATTERN_GAP_LENGTH_PX = 20
        val DOT: com.huawei.hms.maps.model.PatternItem = com.huawei.hms.maps.model.Dot()
        val DASH: com.huawei.hms.maps.model.PatternItem =
            com.huawei.hms.maps.model.Dash(PATTERN_DASH_LENGTH_PX.toFloat())
        val GAP: com.huawei.hms.maps.model.PatternItem =
            com.huawei.hms.maps.model.Gap(PATTERN_GAP_LENGTH_PX.toFloat())
        val PATTERN_POLYGON_ALPHA = Arrays.asList<com.huawei.hms.maps.model.PatternItem>(GAP, DOT)


        // val patternPoligonAlpha = listOf<PatternItem>(GAP, DOT)
        var points2: MutableList<com.huawei.hms.maps.model.LatLng>? = null
        points2 = java.util.ArrayList()

        // LOAD ROAD DOTS......................
        for (i in 0 until roadPendingToWalk.size) {
            val roadPath = com.huawei.hms.maps.model.LatLng(
                roadPendingToWalk[i].lat,
                roadPendingToWalk[i].long
            )
            points2.add(roadPath)
        }

        val polylineOptions2Huawei = com.huawei.hms.maps.model.PolylineOptions()
        polylineOptions2Huawei.addAll(points2)
        polylineOptions2Huawei.geodesic(true)
        polylineOptions2Huawei.width(10f)
        polylineOptions2Huawei.color(Color.parseColor("#E4E2E2"))
        polylineOptions2Huawei.pattern(PATTERN_POLYGON_ALPHA)
        huaweiMap.addPolyline(polylineOptions2Huawei)
    }


//    internal inner class getRoadJSONAtFirstTime : AsyncTask<Void, Void, Void>() {
//        override fun doInBackground(vararg p0: Void?): Void? {
//            getChallengeStepsData(challangeID)
//
//            return null;
//        }
//    }


    private fun getRoadJSONAtFirstTime(userID: String, challangeID: String) {
        getChallengeStepsData(challangeID)


//        val jsonStr = "{" +
//                "\"userID\": \"" + userID + "\"," +
//                "\"challenge_id\": \"" + challangeID + "\"" +
//                "}"
//
//        subscription.add(mapChallangeVM.getRoadJSON("join_adventure_challenge", jsonStr).subscribeOn(
//                Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe { proDialog.visibility = View.VISIBLE }
//                .doOnTerminate { proDialog.visibility = View.GONE }
//                .doOnError { proDialog.visibility = View.GONE }
//                .subscribe({
//                    if (it.isSuccess) {
//
//
//                        roadsArry = mapChallangeVM.roadsArry
//
//
//                        writeJSONtoFile("$challangeID.json", roadsArry.toString())
//                        proDialog.visibility = View.VISIBLE
//                        getChallengeStepsData(challangeID)
//
//
//                    } else {
//                        showMessage("Failed loading data")
//                    }
//                }, {
//                    it.printStackTrace()
//                    showMessage("Failed loading data")
//                })
//        )


    }

    private fun writeJSONtoFile(fileName: String, strData: String) {
        try {
            this@MapChallangeKActivity.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(strData.toByteArray())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getChallengeStepsData(challangeID: String) {

        proDialog.visibility = View.VISIBLE

        val jsonStr = "{" +
                "\"userID\": \"" + userid_ExistingUser + "\"," +
                "\"challenge_id\": \"" + challangeID + "\"" +
                "}"

        subscription.add(mapChallangeVM.getChallangeData("getChallengeSteps", jsonStr).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog.visibility = View.VISIBLE }
            .doOnTerminate { proDialog.visibility = View.GONE }
            .doOnError { proDialog.visibility = View.GONE }
            .subscribe(

                {
                    if (it.isSuccess) {
                        proDialog.visibility = View.GONE

                        if (mapChallangeVM.sreverResponse == 0) {


                            val cards =
                                mapChallangeVM.mainResponseObj!!.cards as ArrayList<com.ayubo.life.ayubolife.challenges.Cards>

                            currentSteps = mapChallangeVM.mainResponseObj!!.counter

                            if (cards.isNotEmpty()) {
                                dragView.visibility = View.VISIBLE

                                val dagableViewAdapter =
                                    DagableViewAdapter(this@MapChallangeKActivity, cards)
                                dagableViewAdapter.onitemClickListener = this@MapChallangeKActivity


                                val mlayoutManager = LinearLayoutManager(this@MapChallangeKActivity)

                                dropdown_items_recycler.apply {
                                    layoutManager = mlayoutManager
                                    adapter = dagableViewAdapter
                                }


                                img_btn_down_arrow.visibility = View.GONE

                                startGif5()

                                img_btn_down_arrow.setOnClickListener {
                                    slidingup_layout.panelState =
                                        SlidingUpPanelLayout.PanelState.EXPANDED
                                }

                                slidingup_layout!!.addPanelSlideListener(object :
                                    SlidingUpPanelLayout.PanelSlideListener {
                                    override fun onPanelSlide(panel: View, slideOffset: Float) {
                                        // Log.i(TAG, "onPanelSlide, offset $slideOffset")

                                        val margin = slideOffset * 30
                                        val params = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                        var intRate = margin.toInt()
                                        intRate = 30 - intRate
                                        //   Log.i(TAG, "onPanelSlide, ...... $intRate")
                                        params.setMargins(intRate, 0, intRate, 0)
                                        top_bgnew.layoutParams = params

                                        val mlp = dropdown_items_recycler
                                            .getLayoutParams() as ViewGroup.MarginLayoutParams
                                        mlp.setMargins(intRate, 0, intRate, 0)

                                        if (0.89 < slideOffset) {
                                            //  Log.i(TAG, "onPanelSlide slideOffset, ...... $slideOffset")
                                            slidingup_layout!!.setDragView(top_bgnew)
                                            img_btn_down_arrow.visibility = View.VISIBLE
                                            top_bgnew.visibility = View.VISIBLE

                                            dropdown_items_recycler.setOnTouchListener(View.OnTouchListener { v, event -> false })
                                        } else {
                                            //       Log.i(TAG, "onPanelSlide slideOffset 0, ...... $slideOffset")
                                            //   CustomMapDetaisl_lv.setSelection(0)
                                            slidingup_layout!!.setDragView(dragView)
                                            img_btn_down_arrow.visibility = View.GONE
                                            dropdown_items_recycler.setOnTouchListener(View.OnTouchListener { v, event ->
                                                true
                                                // mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED)
                                            })
//                                       CustomMapDetaisl_lv.setOnTouchListener(View.OnTouchListener { v, event ->
//                                           SlidingUpLayout!!.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED)
//                                           println("..................0......" + event.action)
//                                           true
//                                       })
                                        }

                                    }

                                    override fun onPanelStateChanged(
                                        panel: View,
                                        previousState: SlidingUpPanelLayout.PanelState,
                                        newState: SlidingUpPanelLayout.PanelState
                                    ) {
                                        Log.i(TAG, "onPanelStateChanged $newState")
                                        // slidingup_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED;
//                                   if (newState == "EXPANDED") {
//
//                                   } else {
//                                       //  mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//
//                                   }

                                    }
                                })


                            } else {
                                dragView.visibility = View.GONE
                            }


                            treatures = mapChallangeVM.mainResponseObj!!.treatures


                            val cityFile = readJSONFromAsset(challangeID)

                            if (cityFile == null) {
                                isCityFileNull = true;
                                getAdventureChallengeRoute();

                            } else {
                                isCityFileNull = false;
                                getPathObjectsData(currentSteps)
                                showTreasures(treatures)
                            }


                        } else if (mapChallangeVM.sreverResponse == 23) {
                            val intent: Intent = Intent(
                                this@MapChallangeKActivity,
                                MapJoinChallenge_Activity::class.java
                            );
                            intent.putExtra("challenge_id", challangeID)
                            startActivity(intent);
                            finish()
                        }


                    } else {
                        showAlertServiceError("Service temporarily unavailable, Please try again later")
                    }
                }, {
                    it.printStackTrace()
                    showAlertServiceError(it.message.toString())
                })
        )
    }


//    internal inner class getAdventureChallengeRoute : AsyncTask<Void, Void, Void>() {
//
//
//        override fun doInBackground(vararg p0: Void?): Void? {
//
//            val httpClient: HttpClient = DefaultHttpClient();
//            val httpPost: HttpPost = HttpPost(ApiClient.BASE_URL_live_v6);
//            httpPost.setHeader(BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
//
//
//            //Post Data
//            val nameValuePair: ArrayList<NameValuePair> = ArrayList<NameValuePair>();
//
//
//            //  email = lusername;
//            //  password = lpassword;
//
//
//            val jsonStr: String =
//                    "{" +
//                            "\"userID\": \"" + userid_ExistingUser + "\"," +
//                            "\"challenge_id\": \"" + challangeID + "\"" +
//                            "}";
//
//            //   challenge_id
//
//            nameValuePair.add(BasicNameValuePair("method", "get_adventure_challenge_route"));
//            nameValuePair.add(BasicNameValuePair("input_type", "JSON"));
//            nameValuePair.add(BasicNameValuePair("response_type", "JSON"));
//            nameValuePair.add(BasicNameValuePair("rest_data", jsonStr));
//
//
//            System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
//            //Encoding POST data
//            try {
//                httpPost.setEntity(UrlEncodedFormEntity(nameValuePair));
//
//            } catch (e: UnsupportedEncodingException) {
//                // log exception
//                e.printStackTrace();
//            }
//
//            var response: HttpResponse? = null;
//            try {
//                response = httpClient.execute(httpPost);
//                System.out.println("........getActivityDetails..response..........." + response);
//                System.out.println("========Service Calling============0");
//                var responseString: String? = null;
//                responseString = EntityUtils.toString(response.getEntity());
//                val jsonObj: JSONObject = JSONObject(responseString);
//                val res: String = jsonObj.optString("result").toString();
//
//                val result: Int = Integer.parseInt(res);
//
//                if (result == 0) {
//                    cityJsonString = jsonObj.optString("json").toString();
//
//                    try {
//
//                        val jsonElement: JsonObject = JsonParser().parse(cityJsonString).getAsJsonObject();
//
//                        val jsArray: JSONArray = JSONArray();
//
//                        for (i in 0 until jsonElement.get("route").asJsonArray.size()) {
//                            jsArray.put(jsonElement.get("route").asJsonArray.get(i));
//                        }
//
//
//                        roadsArry = jsArray;
//                        writeJSONtoFile("$challangeID.json", roadsArry.toString())
//                        getChallengeStepsData(challangeID);
//
//
//                    } catch (e: Exception) {
//                        e.printStackTrace();
//                    }
//
//
//                } else {
//                }
//
//            } catch (e: IOException) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }

    fun getAdventureChallengeRoute() {

        proDialog.visibility = View.VISIBLE

        val jsonStr = "{" +
                "\"userID\": \"" + userid_ExistingUser + "\"," +
                "\"challenge_id\": \"" + challangeID + "\"" +
                "}"





        subscription.add(mapChallangeVM.getAdventureChallengeRoutes(
            "get_adventure_challenge_route",
            jsonStr
        ).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog.visibility = View.VISIBLE }
            .doOnTerminate { proDialog.visibility = View.GONE }
            .doOnError { proDialog.visibility = View.GONE }
            .subscribe(

                {
                    if (it.isSuccess) {
                        System.out.println(it)
                        System.out.println(mapChallangeVM.mainResponseAdventureChallenge)


                        try {


                            roadsArry = mapChallangeVM.roadsArry;


                            writeJSONtoFile("$challangeID.json", roadsArry.toString())




                            getPathObjectsData(currentSteps)
                            showTreasures(treatures)


                        } catch (e: Exception) {
                            e.printStackTrace();
                        }


                    } else {
                        showAlertServiceError("Service temporarily unavailable, Please try again later")

                    }

                }, {
                    it.printStackTrace()
                    showAlertServiceError(it.message.toString())
                })
        )
    }


    fun showAlertServiceError(msg: String) {

//        val builder = android.app.AlertDialog.Builder(this@MapChallangeKActivity)
        val builder = android.app.AlertDialog.Builder(this@MapChallangeKActivity)
        val inflater =
            this@MapChallangeKActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_service_error, null, false)

        builder.setView(layoutView)
        val dialog = builder.create()
        dialog.setCancelable(false)

        val lbl_alert_message = layoutView.findViewById<View>(R.id.lbl_alert_message) as TextView
        lbl_alert_message.text = msg

        val btn_cancel = layoutView.findViewById<View>(R.id.btn_cancel) as TextView
        btn_cancel.setOnClickListener {
            dialog.cancel()
            finish()
            // readExtras()
//            proDialog.visibility = View.GONE

        }
        dialog.show()
    }

    fun startGif5() {
        val timerForAnimation = Timer()
        timerForAnimation.schedule(timerTask_timerForAnimation, 2000)

        println("========timerForAnimation=========")
    }

    private val timerTask_timerForAnimation = object : TimerTask() {
        override fun run() {

            runOnUiThread(Runnable {

                val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
                valueAnimator.addUpdateListener {
                    val value = it.animatedValue as Float
                    dragView.translationY = value
                }
                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.duration = 3000
                valueAnimator.start()
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        if (googleSupportServices.isGooglePlayServicesAvailable) {
            this.googleMap = googleMap
            val bitmapdraw2 = resources.getDrawable(R.drawable.maploading) as BitmapDrawable
            val b2 = bitmapdraw2.bitmap
            val kandy = LatLng(7.409591, 80.736906)
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kandy, 8f))
            this.googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            this.googleMap.setOnMarkerClickListener(this)

            setUpMap()
        }
    }

    override fun onMapReady(huaweiMap: HuaweiMap) {
        if (!googleSupportServices.isGooglePlayServicesAvailable) {
            this.huaweiMap = huaweiMap;
            val bitmapdraw2 = resources.getDrawable(R.drawable.maploading) as BitmapDrawable
            val b2 = bitmapdraw2.bitmap
            val kandy = com.huawei.hms.maps.model.LatLng(7.409591, 80.736906)
            this.huaweiMap.moveCamera(
                com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom(
                    kandy,
                    8f
                )
            )
            this.huaweiMap.mapType = HuaweiMap.MAP_TYPE_NORMAL
            this.huaweiMap.setOnMarkerClickListener(this)

            setUpMap()
        }
    }

    private fun showUserSteps(imageUrl: String, place: LatLng, tagName: String) {

        var markerView = (baseContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.steps_in_map_layout, null)
        val txtStepsInMap = markerView.findViewById<View>(R.id.txt_steps_in_map) as TextView
        txtStepsInMap.text = currentSteps.toString()

        googleMap.addMarker(
            MarkerOptions()
                .position(place)
                .title("")
                .snippet("")
                .icon(
                    BitmapDescriptorFactory
                        .fromBitmap(
                            MapChallengeActivity.createDrawableFromView(
                                this,
                                markerView
                            )
                        )
                )
        )

    }

    private fun showUserStepsForHuawei(
        imageUrl: String,
        place: com.huawei.hms.maps.model.LatLng,
        tagName: String
    ) {

        var markerView = (baseContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.steps_in_map_layout, null)
        val txtStepsInMap = markerView.findViewById<View>(R.id.txt_steps_in_map) as TextView
        txtStepsInMap.text = currentSteps.toString()

        huaweiMap.addMarker(
            com.huawei.hms.maps.model.MarkerOptions()
                .position(place)
                .title("")
                .snippet("")
                .icon(
                    com.huawei.hms.maps.model.BitmapDescriptorFactory
                        .fromBitmap(
                            MapChallengeActivity.createDrawableFromView(
                                this,
                                markerView
                            )
                        )
                )
        )

    }

    private fun showUserImage(imageUrl: String, place: LatLng, tagName: String) {

        val imagepathDb: String = pref!!.loginUser["image_path"]!!
        val rand = Random()
        val n = rand.nextInt(50) + 1
        val rannum = Integer.toString(n)

        var burlImg = "";
        if (imagepathDb.contains(".jpg") || imagepathDb.contains(".jpeg") || imagepathDb.contains(".png")) {
            burlImg = imagepathDb;
        } else {


            if (imagepathDb != "" && !imagepathDb.contains(ApiClient.MAIN_URL_LIVE_HAPPY)) {
                burlImg = ApiClient.MAIN_URL_LIVE_HAPPY + "$imagepathDb&cache=$rannum"

            } else {
                burlImg = "$imagepathDb&cache=$rannum"
            }


        }


        val requestOptions1 = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        val imageSize = Utility.getImageSizeFor_DeviceDensitySize(130)
        Glide.with(this)
            .asBitmap()
            .override(imageSize)
            .apply(requestOptions1)
            .load(burlImg)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val mBitmap: Bitmap? =
                        MapChallengeActivity.getCircularBitmapWithWhiteBorder(resource, 2)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(place)
                            .icon(BitmapDescriptorFactory.fromBitmap(mBitmap))
                    ).tag = tagName
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun showUserImageForHuawei(
        imageUrl: String,
        place: com.huawei.hms.maps.model.LatLng,
        tagName: String
    ) {

        val imagepathDb: String = pref!!.loginUser["image_path"]!!
        val rand = Random()
        val n = rand.nextInt(50) + 1
        val rannum = Integer.toString(n)
        val burlImg = ApiClient.MAIN_URL_LIVE_HAPPY + "$imagepathDb&cache=$rannum"
        val requestOptions1 = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        val imageSize = Utility.getImageSizeFor_DeviceDensitySize(100)
        Glide.with(this)
            .asBitmap()
            .override(imageSize)
            .apply(requestOptions1)
            .load(burlImg)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                    val mBitmap: Bitmap? =
                        MapChallengeActivity.getCircularBitmapWithWhiteBorder(resource, 2)
                    huaweiMap.addMarker(
                        com.huawei.hms.maps.model.MarkerOptions()
                            .position(place)
                            .icon(
                                com.huawei.hms.maps.model.BitmapDescriptorFactory.fromBitmap(
                                    mBitmap
                                )
                            )
                    ).tag = tagName
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun showJournalsCities(journalsList: List<LocationData>) {


        for (i in journalsList.indices) {

            val mytag = "journal_$i"

            val journalObj = journalsList[i]

            Log.d("=image=====j==========", journalObj.cityimg!!)
            // val image = journalObj.cityimg.replace("zoom_level", getDensityName())

            //    Log.d("=image====j===========",image)
            if (googleSupportServices.isGooglePlayServicesAvailable) {
                val place = LatLng(journalObj.lat, journalObj.long)

                var bitmapdraw2: BitmapDrawable? = null

                if (journalObj.steps <= currentSteps) {
                    bitmapdraw2 = resources.getDrawable(R.drawable.landscape_blue) as BitmapDrawable
                } else {
                    bitmapdraw2 = resources.getDrawable(R.drawable.landscape_grey) as BitmapDrawable
                }


                val b2 = bitmapdraw2.bitmap

                val mapMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(place)
                        .icon(BitmapDescriptorFactory.fromBitmap(b2))
                )
                mapMarker.tag = mytag
                mapMarker.showInfoWindow()
            } else {
                val place = com.huawei.hms.maps.model.LatLng(journalObj.lat, journalObj.long)
                var bitmapdraw2: BitmapDrawable? = null
                if (journalObj.steps <= currentSteps) {
                    bitmapdraw2 = resources.getDrawable(R.drawable.landscape_blue) as BitmapDrawable
                } else {
                    bitmapdraw2 = resources.getDrawable(R.drawable.landscape_grey) as BitmapDrawable
                }
                val b2 = bitmapdraw2.bitmap
                val mapMarker = huaweiMap.addMarker(
                    com.huawei.hms.maps.model.MarkerOptions()
                        .position(place)
                        .icon(com.huawei.hms.maps.model.BitmapDescriptorFactory.fromBitmap(b2))
                )
                mapMarker.tag = mytag
                mapMarker.showInfoWindow()
            }
        }
    }

    private fun showTreasures(treatures: List<com.ayubo.life.ayubolife.challenges.Treatures>) {


        for (i in treatures.indices) {

            val mytag = "treatures_$i"

            val treasureObj = treatures[i]

            val image = treasureObj.objimg.replace("zoom_level", getDensityName())
            // val image =   "http://hemaswellness.cloudapp.net/admin_panel/public/uploads/Alzheimer%E2%80%99s/native_post_anuradhapura_1597245024_twin__ponds_anuradhapura.png"
            Log.d("=image====t===========", image)

            val requestOptions = RequestOptions().fitCenter()
            val denc = Utility.getDensity()


            if (googleSupportServices.isGooglePlayServicesAvailable) {
                val place = LatLng(treasureObj.latp, treasureObj.longp)

                Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .apply(requestOptions)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            var mBitmap = Utility.getResizedBitmapImport(denc.toFloat(), resource)
                            // mBitmap = MapChallengeActivity.getCircularBitmapWithWhiteBorder(mBitmap, 2)
                            val icon = BitmapDescriptorFactory.fromBitmap(mBitmap)

                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(place)
                                    .icon(icon)
                            ).tag = mytag
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            } else {
                val place = com.huawei.hms.maps.model.LatLng(treasureObj.latp, treasureObj.longp)

                Glide.with(this)
                    .asBitmap()
                    .load(image)
                    .apply(requestOptions)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {

                            var mBitmap = Utility.getResizedBitmapImport(denc.toFloat(), resource)
                            // mBitmap = MapChallengeActivity.getCircularBitmapWithWhiteBorder(mBitmap, 2)
                            val icon =
                                com.huawei.hms.maps.model.BitmapDescriptorFactory.fromBitmap(mBitmap)

                            huaweiMap.addMarker(
                                com.huawei.hms.maps.model.MarkerOptions()
                                    .position(place)
                                    .icon(icon)
                            ).tag = mytag
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }


        }

    }

}
