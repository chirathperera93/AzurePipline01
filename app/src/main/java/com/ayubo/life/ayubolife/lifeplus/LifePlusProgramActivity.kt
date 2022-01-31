package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.BuildConfig
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.ContactInfoActivity
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.discover_search.DiscoverSearchActivity
import com.ayubo.life.ayubolife.insurances.Activities.InsurancePolicesActivity
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity
import com.ayubo.life.ayubolife.login.EnterPromoCodeActivity
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.login.UserProfileActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.notification.activity.NotificationsListActivity
import com.ayubo.life.ayubolife.notification.model.NotiCountMainData
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse
import com.ayubo.life.ayubolife.payment.activity.MainPaymentHistoryActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentSettingsActivity
import com.ayubo.life.ayubolife.payment.activity.PurchaseHistory
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.push.Config
import com.ayubo.life.ayubolife.qrcode.DecoderActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.satalite_menu.CommiunityActivity
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.utility.Utility
import com.ayubo.life.ayubolife.walk_and_win.StepObj
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.flavor.activity.AboutActivity
import com.flavors.changes.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.huawei.hms.hihealth.SettingController
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_life_plus_program.*
import kotlinx.android.synthetic.main.activity_new_discover_program.*
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicNameValuePair
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class LifePlusProgramActivity : BaseActivity(), NewDiscoverAdapter.OnCardClickListener {
    val listFragments = ArrayList<ProgramBannersFragment>()
    var isFromSearchResults: Boolean = false
    var mainResponse: LifePlusProgramMainResponse? = null

    @Inject
    lateinit var lifePlusProgramVM: LifePlusProgramVM


    lateinit var pref: PrefManager;
    lateinit var prefs: SharedPreferences;
    lateinit var userid_ExistingUser: String;
    lateinit var userid_ExistingUser_static: String;
    lateinit var hasToken: String;
    lateinit var toolbar: Toolbar;
    lateinit var drawer: DrawerLayout;
    lateinit var navigationView: NavigationView;
    lateinit var navHeader: View;
    var navItemIndex: Int = 0;
    var TAG_HOME: String = "home";
    var CURRENT_TAG: String = TAG_HOME;
    lateinit var encodedHashToken: String;
    lateinit var url: String;
    lateinit var txtName: TextView;
//    lateinit var txt_noti_count: TextView;
//    lateinit var btn_notifications: ImageView;
    lateinit var imgProfile: CircleImageView;
    lateinit var activityTitles: Array<String>;
    var TAG: String = LifePlusProgramActivity::class.java.simpleName;
    var deviceIdPush: String? = null;
    private lateinit var mSettingController: SettingController;
    private var REQUEST_LOGIN_CODE: Int = 1102;
    private var REQUEST_SIGN_IN_LOGIN: Int = 1002;
    private var REQUEST_HEALTH_AUTH: Int = 1003;
    private var HEALTH_SCHEME: String = "huaweischeme://healthapp/achievement?module=kit";
    private lateinit var loggerFB: AppEventsLogger
    lateinit var fragment: Fragment
    lateinit var jsonStepsForDailyArrayForNewSave: ArrayList<StepObj>;
    lateinit var jsonStepsForWeekArrayForNewSave: ArrayList<StepObj>;
    var notificationCount: String = "0";
    lateinit var todayStepsToSendToServer: String;
    lateinit var progressAyuboNew: ProgressBar;
    private var newDiscoverAdapter: NewDiscoverAdapter? = null

    var handler: Handler = Handler();
    var timer: Timer? = null;
    var DELAY_MS: Long = 3000;//delay in milliseconds before task is to be executed
    var PERIOD_MS: Long = 3000; // time in milliseconds between successive task executions.
    private var isLinkedHuawei: Boolean = false;


    lateinit var ic_call_image: ImageView;
    lateinit var navi_header_mobile_no: TextView;
    lateinit var view_container: LinearLayout;
    lateinit var layout_for_link_to_profile: LinearLayout;

    @Inject
    lateinit var lifePlusProgramActivityVM: LifePlusProgramActivityVM


    companion object {
        fun startActivity(
            context: Context,
            isFromSearchResults: Boolean,
            id: String,
            name: String,
            type: String
        ) {
            val intent = Intent(context, LifePlusProgramActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_RESULTS_FROM, isFromSearchResults)
            intent.putExtra(EXTRA_SEARCH_RESULTS_ID, id)
            intent.putExtra(EXTRA_SEARCH_RESULTS_NAME, name)
            intent.putExtra(EXTRA_SEARCH_RESULTS_TYPE, type)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    var width: Int = 0


    override fun onResume() {
        super.onResume()
        // load nav menu header data
        loadNavHeader();


        readExtra()
        if (pref.userToken.length > 50) {
            getNotificationsData();
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)

        if (Constants.type == Constants.Type.AYUBO) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            val noticolor: String = "#000000";
            val whiteInt: Int = Color.parseColor(noticolor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(whiteInt);

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor: View = getWindow().getDecorView();

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.setSystemUiVisibility(0);
                //   decor.setSystemUiVisibility(0);
                // }
            }

            setContentView(R.layout.activity_new_home_with_side_menu_lipfeplus);
        } else {
            Log.i("App Name : ", "Hemas App ");
            setContentView(R.layout.activity_new_home_with_side_menu_hemas);
        }

        width = getScreenWidth()
        pref = PrefManager(this);
        pref.setNewServiceLastRunningTime("0");

        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        pref.setMapReload(false);

        try {

            userid_ExistingUser = pref.getLoginUser().get("uid").toString();
            userid_ExistingUser_static = pref.getLoginUser().get("uid").toString();
            hasToken = pref.getLoginUser().get("hashkey").toString();
            toolbar = findViewById(R.id.toolbar_for_newsidemenu);

        } catch (e: Exception) {
            e.printStackTrace();
        }

//        val lay_notifications: RelativeLayout = toolbar.findViewById(R.id.lay_notifications);
//        btn_notifications = toolbar.findViewById(R.id.btn_notifications);
//        val lay_search: RelativeLayout = toolbar.findViewById(R.id.lay_search);
//        val lay_search_icon: ImageView = toolbar.findViewById(R.id.lay_search_icon);
        val txt_expert_heading: TextView = toolbar.findViewById(R.id.txt_expert_heading);
        txt_expert_heading.text = "Discover"
        txt_expert_heading.setTextColor(Color.parseColor("#3B3B3B"))
//        lay_search_icon.visibility = View.VISIBLE
//        txt_noti_count = toolbar.findViewById(R.id.txt_noti_count);
//        txt_noti_count.setVisibility(View.VISIBLE);
//        btn_notifications.setVisibility(View.VISIBLE);


//        lay_notifications.setOnClickListener {
//            val intent: Intent =
//                Intent(this@LifePlusProgramActivity, NotificationsListActivity::class.java);
//            startActivity(intent);
//        };

//        lay_search.setOnClickListener {
//            DiscoverSearchActivity.startActivity(this@LifePlusProgramActivity)
//        };

        drawer = findViewById(R.id.drawer_layout);


        if (Constants.type == Constants.Type.AYUBO) {
            navigationView = findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            navigationView = findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            navigationView = findViewById(R.id.nav_view_newSidemenu_with_activity);
        } else {
            Log.i("App Name : ", "Hemas App ");
            navigationView = findViewById(R.id.nav_view_newSidemenu_with_activity_hemas);
        }

        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.navi_header_name);
        imgProfile = navHeader.findViewById(R.id.img_profile_new);
        navi_header_mobile_no = navHeader.findViewById(R.id.navi_header_mobile_no);
        ic_call_image = navHeader.findViewById(R.id.ic_call_image);
        view_container = navHeader.findViewById(R.id.view_container);
        layout_for_link_to_profile = navHeader.findViewById(R.id.layout_for_link_to_profile);


        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // initializing navigation menu
        if (Constants.type == Constants.Type.AYUBO) {
            setUpNavigationView();
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            setUpNavigationView_LifePlus();
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            setUpNavigationView();
        } else {
            Log.i("App Name : ", "Hemas App ");
            setUpNavigationView_Hemas();
        }

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            // discover page ui
            loadDiscoverFragment();
        }

        try {
            displayFirebaseRegId();
        } catch (e: Exception) {
            e.printStackTrace();
        }

        try {
            Service_getSetDefaultDevice_ServiceCall();
        } catch (e: Exception) {
            e.printStackTrace();
        }


    }

    private fun loadNavHeader() {
        try {
            // txtName.setText(fullName);
            if (Constants.type == Constants.Type.LIFEPLUS) {
                view_container.setBackgroundResource(R.drawable.lifeplus_nav_header_main_bg)
            }
            val sName: String? = pref.userProfile["name"];
            // userEmail=pref.getLoginUser().get("email");
            txtName.text = sName;
            var imageURL = pref.loginUser["image_path"].toString();

            if (imageURL.contains(".jpg") || imageURL.contains(".jpeg") || imageURL.contains(".png")) {
                Glide.with(baseContext).load(imageURL).into(imgProfile)
            } else {
                if (imageURL != "" && !imageURL.contains(ApiClient.MAIN_URL_LIVE_HAPPY)) {
                    imageURL = ApiClient.MAIN_URL_LIVE_HAPPY + imageURL;

                }
                Glide.with(baseContext).load(imageURL).into(imgProfile)
            }

            if (pref.loginUser["mobile"].toString() != "") {
                ic_call_image.visibility = View.VISIBLE
                navi_header_mobile_no.text = pref.loginUser["mobile"].toString()
            }

            layout_for_link_to_profile.setOnClickListener {
                //  FirebaseAnalytics Adding
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                }
                startActivity(
                    Intent(
                        this@LifePlusProgramActivity,
                        UserProfileActivity::class.java
                    )
                );
                drawer.closeDrawers();
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun goToMe(view: View) {
        val intent = Intent(baseContext, ProfileNew::class.java);
        startActivity(intent);
    }

    fun goToHome(view: View) {
        val intent = Intent(baseContext, NewHomeWithSideMenuActivity::class.java);
        startActivity(intent);
    }

    fun goToMessageList(view: View) {
        val intent: Intent = Intent(
            baseContext,
            NotificationsListActivity::class.java
        )
        startActivity(intent)
    }

    private fun Service_getSetDefaultDevice_ServiceCall() {

        try {
            if (Utility.isInternetAvailable(LifePlusProgramActivity@ this)) {
                val task: Service_SetDefaultDevice = Service_SetDefaultDevice();
                task.execute();
            } else {
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    internal inner class Service_SetDefaultDevice : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            makeSetDefaultDevice();
            return null
        }

    }

    private fun makeSetDefaultDevice() {

        val httpClient: HttpClient = DefaultHttpClient();
        val httpPost: HttpPost = HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        val nameValuePair: ArrayList<NameValuePair> = ArrayList<NameValuePair>();

        val versionName: String = BuildConfig.VERSION_NAME;


        val jsonStr: String =
            "{" +
                    "\"userID\": \"" + userid_ExistingUser + "\"," +
                    "\"deviceID\": \"" + deviceIdPush + "\"," +
                    "\"voipId\": \"" + "" + "\"," +
                    "\"version\": \"" + versionName + "\"," +
                    "\"os\": \"" + "android" + "\"" +
                    "}";


        nameValuePair.add(BasicNameValuePair("method", "setMyNewDeviceID"));
        nameValuePair.add(BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(BasicNameValuePair("rest_data", jsonStr));


        System.out.println(".......send Device id with....setMyNewDeviceID..............." + nameValuePair.toString());

        //Encoding POST data
        System.out.println("............App steps Upldated..................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(UrlEncodedFormEntity(nameValuePair));
        } catch (e: UnsupportedEncodingException) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            val response: HttpResponse = httpClient.execute(httpPost);
            System.out.println("==========deviceIdPush===Sent=======" + deviceIdPush);
            val r: Int = 0;

        } catch (e: ClientProtocolException) {
            // Log exception
            e.printStackTrace();
        } catch (e: IOException) {
            // Log exception
            e.printStackTrace();
        }

    }

    private fun displayFirebaseRegId() {
        val pref: SharedPreferences =
            applicationContext.getSharedPreferences(Config.SHARED_PREF, 0);
        val regId: String? = pref.getString("regId", null);

        if (!TextUtils.isEmpty(regId)) {
            try {
                deviceIdPush = regId;
                val editor: SharedPreferences.Editor = prefs.edit();
                editor.putString("push_id", deviceIdPush);
                editor.commit();
                Log.e(TAG, "====Notification=push_id=======" + deviceIdPush);
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else {
            try {
                deviceIdPush = FirebaseInstanceId.getInstance().getToken();
                val editor: SharedPreferences.Editor = prefs.edit();
                editor.putString("push_id", deviceIdPush);
                editor.commit();

                Log.e(TAG, "========Notification registration faild!=========" + deviceIdPush);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }


        // checking if user is on other navigation menu
        // rather than home
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadDiscoverFragment();
            return;

        }
        super.onBackPressed()
    }

    fun getNotificationsData() {

        val appToken: String = pref.getUserToken();

        val apiService = ApiClient.getNewApiClient().create(ApiInterface::class.java);
        val call = apiService.getNotificationCount(appToken)
        call.enqueue(object : Callback<NotiCountMainResponse> {
            override fun onResponse(
                call: Call<NotiCountMainResponse>,
                response: Response<NotiCountMainResponse>
            ) {
                if (response.isSuccessful()) {
                    val res: NotiCountMainResponse? = response.body();
                    val data: NotiCountMainData? = res?.getData();

                    if (data != null) {


                        val count: Int = data.getUnreadCount();

                        if (count > 0) {
                            notificationCount = Integer.toString(count);
//                            txt_noti_count.setText(notificationCount);

                            val myAnim: Animation = AnimationUtils.loadAnimation(
                                this@LifePlusProgramActivity,
                                R.anim.bounce
                            );
                            val interpolator: MyBounceInterpolator =
                                MyBounceInterpolator(0.5, 20.0);
                            myAnim.setInterpolator(interpolator);


//                            if (!isFirsttime) {
//                                isFirsttime = true;
//                                MediaPlayer mp = MediaPlayer.create(NewHomeWithSideMenuActivity.this, Settings.System.DEFAULT_NOTIFICATION_URI);
//                                if (mp != null) {
//                                    mp.start();
//                                }
//
//                            }


//                            btn_notifications.startAnimation(myAnim);
//                            txt_noti_count.setVisibility(View.VISIBLE);

                        } else {
//                            txt_noti_count.setVisibility(View.INVISIBLE);
                        }
                    }


                }
            }

            override fun onFailure(call: Call<NotiCountMainResponse>, t: Throwable) {

            }

        }


        )
    }

    class MyBounceInterpolator(amplitude: Double, frequency: Double) :
        android.view.animation.Interpolator {
        var mAmplitude: Double = amplitude;
        var mFrequency: Double = frequency;


        override fun getInterpolation(time: Float): Float {
            return (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1).toFloat();
        }
    }


    private fun readExtra() {

        isFromSearchResults = intent.getBooleanExtra(EXTRA_SEARCH_RESULTS_FROM, false)

        if (isFromSearchResults) {
            var id = intent.getStringExtra(EXTRA_SEARCH_RESULTS_ID)
            var name = intent.getStringExtra(EXTRA_SEARCH_RESULTS_NAME)
            var type = intent.getStringExtra(EXTRA_SEARCH_RESULTS_TYPE)

            if (id == null) {
                id = "";
            }

            if (name == null) {
                name = "";
            }

            if (type == null) {
                type = "";
            }

            getDiscoverSearchedResults(id, name, type)
        } else {
            getLifePlusPrograms()
        }

        getDiscoverBannerData()

    }

    private fun getDiscoverBannerData() {


        if (pref.savedAyuboDiscoverBanner == null || pref.savedAyuboDiscoverBanner == "") {

            subscription.add(
                lifePlusProgramActivityVM.getAyuboBannerData().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { proDialog_lifeplusprogram2.visibility = View.VISIBLE }
                    .doOnTerminate { proDialog_lifeplusprogram2.visibility = View.GONE }
                    .doOnError { proDialog_lifeplusprogram2.visibility = View.GONE }
                    .subscribe({
                        if (it.isSuccess) {


                            print(lifePlusProgramActivityVM.discoverDataDetailJsonObject)

                            pref.saveAyuboDiscoverBanner(lifePlusProgramActivityVM.discoverDataDetailJsonObject)
                            setDiscoverBannerUiWithData(lifePlusProgramActivityVM.discoverDataDetailJsonObject);

                        } else {
                            showMessage(MSG_FAILED_REQUEST)
                        }
                    }, {
                        showMessage(MSG_FAILED_REQUEST)
                    })
            )

        } else {
            try {
                val jsonParser: JsonParser = JsonParser();
                val savedAyuboDiscoverBannerGsonObject: JsonObject =
                    jsonParser.parse(pref.savedAyuboDiscoverBanner) as (JsonObject);
                setDiscoverBannerUiWithData(savedAyuboDiscoverBannerGsonObject)
                val task = getDiscoverBannerDataAsync();
                task.execute();


            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    internal inner class getDiscoverBannerDataAsync : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            subscription.add(
                lifePlusProgramActivityVM.getAyuboBannerData().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isSuccess) {


                            pref.saveAyuboDiscoverBanner(lifePlusProgramActivityVM.discoverDataDetailJsonObject)
                            setDiscoverBannerUiWithData(lifePlusProgramActivityVM.discoverDataDetailJsonObject);


                        } else {
//                            showMessage(MSG_FAILED_REQUEST)
                        }
                    }, {
                        it.printStackTrace()
//                        showMessage(MSG_FAILED_REQUEST)
                    })
            )

            return null;
        }

    }

    fun setDiscoverBannerUiWithData(discoverDataDetailJsonObject: JsonObject) {
        val bannerItemsArray: JsonArray =
            discoverDataDetailJsonObject.get("banner_items").asJsonArray


        val bannerListItems = ArrayList<SliderData>();

        for (i in 0 until bannerItemsArray.size()) {
            val imageListItem: JsonObject = bannerItemsArray.get(i).asJsonObject
            bannerListItems.add(
                SliderData(
                    imageListItem.get("image_url").asString,
                    imageListItem.get("action").asString,
                    imageListItem.get("meta").asString
                )
            );
        }

        try {
            ayubo_banner_recyclerview.layoutManager =
                LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)


            val discoverDataItemsArrayList =
                ArrayList<NewDiscoverCardItem>()

            discoverDataItemsArrayList.add(
                NewDiscoverCardItem(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    bannerListItems
                )
            )

            newDiscoverAdapter =
                NewDiscoverAdapter(
                    baseContext,
                    discoverDataItemsArrayList,
                    false,
                    this@LifePlusProgramActivity
                )
            newDiscoverAdapter!!.onClickListener = this@LifePlusProgramActivity
            ayubo_banner_recyclerview.adapter = newDiscoverAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
//                R.id.nav_home -> {
//                    //  Ram.setTopMenuTabName("home");
//                    drawer.closeDrawers();
//                    navItemIndex = 0;
//                    CURRENT_TAG = TAG_HOME;
//                    true
//                }

                R.id.nav_share -> {
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Share_ayubo_life_clicked", null);
                    }


                    try {
                        val sendIntent: Intent = Intent(android.content.Intent.ACTION_SEND);
                        //  Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);

                        var shareBody: String = "";
                        var title: String = "";
                        if (Constants.type == Constants.Type.AYUBO) {
                            shareBody =
                                "Check out the all in one ayubo.life wellness app. " + MAIN_URL_APPS + "download";
                            sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Share ayubo.life"
                            );
                            title = "Share ayubo.life";
                        } else if (Constants.type == Constants.Type.LIFEPLUS) {
                            shareBody =
                                "Check out the all in one lifeplus wellness app." + MAIN_URL_APPS + "downloadlifeplus";
                            sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Share lifeplus"
                            );
                            title = "Share lifeplus";
                        } else {
                            shareBody =
                                "Check out the all in one ayubo.life wellness app." + MAIN_URL_APPS + " download";
                            sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Share ayubo.life"
                            );
                            title = "Share ayubo.life";
                        }

                        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        sendIntent.setType("text/plain");
                        // startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                        startActivity(Intent.createChooser(sendIntent, title));
                        drawer.closeDrawers();
                    } catch (e: Exception) {
                        e.toString();
                    }
                    true
                }

                R.id.nav_insurance -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Clicked_insurances", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            InsurancePolicesActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_coporate -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Join_community_clicked", null);
                    }

                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            CommiunityActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_payments -> {
//                    val intentPay: Intent =
//                            Intent(this@LifePlusProgramActivity, PaymentSettingsActivity::class.java);
//                    startActivity(intentPay);
//
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@LifePlusProgramActivity, EnterPromoCodeActivity::class.java);
                    intentPromo.putExtra("fromClass", "no");
                    startActivity(intentPromo);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent =
                        Intent(this@LifePlusProgramActivity, LifePointActivity::class.java);
                    startActivity(inl);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_connect -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                    }
                    try {
                        try {
                            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace();
                        }
                        url =
                            ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                        val newUrl: String =
                            Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                        val i: Intent = Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(newUrl));
                        startActivity(i);
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_qrcode -> {
                    //  FirebaseAnalytics Added inside
                    val intent: Intent =
                        Intent(this@LifePlusProgramActivity, DecoderActivity::class.java);
                    intent.putExtra("error", "old");
                    startActivity(intent);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_profile -> {

                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            UserProfileActivity::class.java
                        )
                    );
                    //   startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                    drawer.closeDrawers();
                    true;
                }

                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            PurchaseHistory::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            MainPaymentHistoryActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@LifePlusProgramActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {
                    //  FirebaseAnalytics Added Inside
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            HelpFeedbackActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_badges -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            Badges_Activity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                else -> {
                    navItemIndex = 0;
                    false

                };
            }

            //Checking if the item is in checked state or not, if not make it in checked state
            if (it.isChecked()) {
                it.setChecked(false);
            } else {
                it.setChecked(true);
            }
            it.setChecked(true);

            //  loadHomeFragment();

            return@setNavigationItemSelectedListener true;
        }

        val actionBarDrawerToggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer)

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private fun setUpNavigationView_LifePlus() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
//                R.id.nav_home -> {
//                    //  Ram.setTopMenuTabName("home");
//                    drawer.closeDrawers();
//                    navItemIndex = 0;
//                    CURRENT_TAG = TAG_HOME;
//                    true
//                }
                R.id.nav_share -> {
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Share_ayubo_life_clicked", null);
                    }


                    try {
                        val sendIntent: Intent = Intent(android.content.Intent.ACTION_SEND);
                        //  Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        // String shareBody = "Check out the all in one ayubo.life wellness app. http://apps.ayubo.life/download";
                        // sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life");
                        val shareBody: String =
                            "Check out the all in one lifeplus wellness app. http://bit.ly/lifeplusbd";
                        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share lifeplus");
                        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        sendIntent.setType("text/plain");
                        // startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                        startActivity(Intent.createChooser(sendIntent, "Share lifeplus"));
                        drawer.closeDrawers();
                    } catch (e: Exception) {
                        e.toString();
                    }
                }
                R.id.nav_subscriptions -> {
//                    proDialog_lifeplusprogram2
                    drawer.closeDrawers();
                    proDialog_lifeplusprogram2.visibility = View.VISIBLE
                    val apiService: ApiInterface =
                        ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);

                    val getWnWValidateCall: Call<ProfileDashboardResponseData> =
                        apiService.getWnWValidate(
                            AppConfig.APP_BRANDING_ID,
                            pref.userToken
                        );


                    getWnWValidateCall.enqueue(object : Callback<ProfileDashboardResponseData> {
                        override fun onResponse(
                            call: Call<ProfileDashboardResponseData>,
                            response: Response<ProfileDashboardResponseData>
                        ) {

                            proDialog_lifeplusprogram2.visibility = View.GONE
                            if (response.isSuccessful) {


                                val wnwValidateMainData: JsonObject =
                                    Gson().toJsonTree(response.body()!!.data).asJsonObject;

//                                val intent2: Intent = Intent(
//                                    this@LifePlusProgramActivity,
//                                    GroupViewActivity::class.java
//                                );
//                                intent2.putExtra(
//                                    EXTRA_PAYMENT_META,
////                                    100
//                                    wnwValidateMainData.get("meta").asString
//                                );
//                                startActivity(intent2);
                                processAction(
                                    wnwValidateMainData.get("action").asString,
                                    wnwValidateMainData.get("meta").asString
                                )
                            } else {
                                Toast.makeText(baseContext, "No data found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(
                            errorCall: Call<ProfileDashboardResponseData>,
                            error: Throwable
                        ) {
                            error.printStackTrace()
                            proDialog_lifeplusprogram2.visibility = View.GONE
                            Toast.makeText(baseContext, "Server error", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })

//                    val intent2: Intent = Intent(this@LifePlusProgramActivity, GroupViewActivity::class.java);
//                    intent2.putExtra(EXTRA_PAYMENT_META, "100");
//                    startActivity(intent2);
//                    drawer.closeDrawers();
                }
                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@LifePlusProgramActivity, EnterPromoCodeActivity::class.java);
                    intentPromo.putExtra("fromClass", "no");
                    startActivity(intentPromo);

                    drawer.closeDrawers();
                    true
                }
                R.id.nav_insurance -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Clicked_insurances", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            InsurancePolicesActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent =
                        Intent(this@LifePlusProgramActivity, LifePointActivity::class.java);
                    startActivity(inl);
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_connect -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                    }
                    try {
                        try {
                            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace();
                        }
                        url =
                            ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                        val newUrl: String =
                            Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                        val i: Intent = Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(newUrl));
                        startActivity(i);
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            PurchaseHistory::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            MainPaymentHistoryActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_qrcode -> {
                    //  FirebaseAnalytics Added inside
                    val intent: Intent =
                        Intent(this@LifePlusProgramActivity, DecoderActivity::class.java);
                    intent.putExtra("error", "old");
                    startActivity(intent);

                    drawer.closeDrawers();
                }
                R.id.nav_profile -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            UserProfileActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                }
                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@LifePlusProgramActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                }
                R.id.nav_help -> {
                    //  FirebaseAnalytics Added Inside
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            HelpFeedbackActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                }
                else -> {
                    navItemIndex = 0;
                    false

                };

            }

            //Checking if the item is in checked state or not, if not make it in checked state
            if (it.isChecked()) {
                it.setChecked(false);
            } else {
                it.setChecked(true);
            }
            it.setChecked(true);
            return@setNavigationItemSelectedListener true;
        }
        val actionBarDrawerToggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer)

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private fun setUpNavigationView_Hemas() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_home -> {
                    //  Ram.setTopMenuTabName("home");
                    drawer.closeDrawers();
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    true
                }

//                R.id.nav_coporate -> {
//                    //  FirebaseAnalytics Adding
//                    if (SplashScreen.firebaseAnalytics != null) {
//                        SplashScreen.firebaseAnalytics.logEvent("Join_community_clicked", null);
//                    }
//                    startActivity(Intent(this@LifePlusProgramActivity, CommiunityActivity::class.java));
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intent: Intent =
                        Intent(this@LifePlusProgramActivity, EnterPromoCodeActivity::class.java);
                    intent.putExtra("fromClass", "no");
                    startActivity(intent);
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_lifepoints -> {
//                    //  FirebaseAnalytics Added Inside
//                    val inl: Intent = Intent(this@LifePlusProgramActivity, LifePointActivity::class.java);
//                    startActivity(inl);
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_connect -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("ConnectDevice_clicked", null);
                    }
                    try {
                        try {
                            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace();
                        }
                        url =
                            ApiClient.BASE_URL + "index.php?module=MA_Health_Data&action=connectDevices";
                        val newUrl: String =
                            Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), url);

                        val i: Intent = Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(newUrl));
                        startActivity(i);
                    } catch (e: Exception) {
                        e.printStackTrace();
                    }
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_qrcode -> {
                    //  FirebaseAnalytics Added inside
                    val intent: Intent =
                        Intent(this@LifePlusProgramActivity, DecoderActivity::class.java);
                    intent.putExtra("error", "old");
                    startActivity(intent);
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_profile -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            UserProfileActivity::class.java
                        )
                    );
                    // startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            PurchaseHistory::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            MainPaymentHistoryActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@LifePlusProgramActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {

                    //  FirebaseAnalytics Added Inside
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            HelpFeedbackActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_badges -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            Badges_Activity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_contact -> {
                    //  FirebaseAnalytics Added Inside
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("ContactInfo_clicked", null);
                    }
                    startActivity(
                        Intent(
                            this@LifePlusProgramActivity,
                            ContactInfoActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }

                else -> {
                    navItemIndex = 0;
                    false

                };

            }


            //Checking if the item is in checked state or not, if not make it in checked state
            if (it.isChecked()) {
                it.setChecked(false);
            } else {
                it.setChecked(true);
            }
            it.setChecked(true);
            return@setNavigationItemSelectedListener true;


        }


        val actionBarDrawerToggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer)

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private fun loadDiscoverFragment() {

        selectNavMenu();

        // set toolbar title
        //   setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            //   toggleFab();
            return;
        }

        fragment = getDiscoverFragment();
        val fragmentTransaction: FragmentTransaction =
            getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        );
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
        //Closing drawer on item click
        drawer.closeDrawers();

    }

    private fun selectNavMenu() {

        try {
//            val tabName: String = Ram.getTopMenuTabName();
            navigationView.menu.getItem(navItemIndex).isChecked = true;
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show();
        }

//        if(tabName.equals("history")){
//            navItemIndex=3;
//        }


    }

    private fun getDiscoverFragment(): Fragment {


        val discoverFragment: LifePlusProgramActivityFragment = LifePlusProgramActivityFragment();

        return discoverFragment;


    }

    inner class PagerAdapter(fm: FragmentManager?, var list: List<ProgramBannersFragment>) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(position: Int) = list[position]

        override fun getCount() = list.size

    }

    private fun getDiscoverSearchedResults(id: String = "", name: String = "", type: String = "") {


        subscription.add(lifePlusProgramVM.getDiscoverSearchedResults(id, name, type).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { proDialog_lifeplusprogram2.visibility = View.VISIBLE }
            .doOnTerminate { proDialog_lifeplusprogram2.visibility = View.GONE }
            .doOnError { proDialog_lifeplusprogram2.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    mainResponse = lifePlusProgramVM.mainResponse

                    //=========================================
                    // Program Banners View Display Strat
                    val bannerList = mainResponse!!.data.banner_list

                    listFragments.clear()


//                    val tabLayout = findViewById<TabLayout>(R.id.tabdots_programs)

                    if ((bannerList != null) && (bannerList.isNotEmpty())) {

                        bannerList.forEach {
                            val fragment = ProgramBannersFragment.newInstance(it)
                            listFragments.add(fragment)
                        }
//                        pager_programdiscover.adapter =
//                            PagerAdapter(supportFragmentManager, listFragments)
//                        pager_programdiscover.layoutParams =
//                            RelativeLayout.LayoutParams(width, (width / 2) + 30)
//                        tabLayout.setupWithViewPager(pager_programdiscover, true)

                    } else {
//                        banner_container.visibility = View.GONE
//                        tabdots_programs.visibility = View.GONE
                    }


                    // Scroll Program View Display Strat
                    //========================================

                    val scroll_programs = mainResponse!!.data.scroll_programs

                    layout_recomonded_programs2.removeAllViews()

                    if (scroll_programs.isNotEmpty()) {


                        for (c in 0 until scroll_programs.size) {

                            val innerScrolledList: ScrollPrograms = scroll_programs[c]

                            val conetentViewMain = LinearLayout(this)
                            conetentViewMain.orientation = LinearLayout.VERTICAL

                            val horizontalScrollView = HorizontalScrollView(this)

                            val conetentViewSub = LinearLayout(this)
                            conetentViewSub.orientation = LinearLayout.HORIZONTAL

                            val sectionHeaderText = TextView(this)
                            sectionHeaderText.text = innerScrolledList.title
                            sectionHeaderText.setPadding(0, 20, 5, 5)
                            sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
                            val typeFace: Typeface? = ResourcesCompat.getFont(
                                this.applicationContext,
                                R.font.roboto_bold
                            )
                            sectionHeaderText.typeface = typeFace
                            sectionHeaderText.setTextColor(Color.parseColor("#000000"))
                            horizontalScrollView.addView(conetentViewSub)

                            conetentViewMain.addView(sectionHeaderText)
                            conetentViewMain.addView(horizontalScrollView)

                            for (i in 0 until innerScrolledList.list.size) {


                                val linearLayout = LinearLayout(this)
                                val prams = ViewGroup.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                linearLayout.layoutParams = prams
                                linearLayout.orientation = LinearLayout.HORIZONTAL

                                val secondObj: ListSP = innerScrolledList.list[i]
                                var inflater: LayoutInflater? = null
                                inflater =
                                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                                assert(inflater != null)
                                val cell = inflater!!.inflate(R.layout.discover_cell, null)
                                val imgImage = cell.findViewById(R.id.img_image_rpc) as ImageView
                                val txt_program_button =
                                    cell.findViewById(R.id.txt_program_button) as TextView
                                val txtProgramHeading =
                                    cell.findViewById(R.id.txt_program_heading) as TextView
                                val txtProgramDesc =
                                    cell.findViewById(R.id.txt_program_desc) as TextView
                                val txt_offer = cell.findViewById(R.id.txt_offer) as TextView
                                val offer_container =
                                    cell.findViewById(R.id.offer_container) as LinearLayout


                                txtProgramHeading.text = secondObj.title


                                if (secondObj.offer_text.isNotEmpty()) {
                                    txt_offer.text = secondObj.offer_text
                                } else {
                                    offer_container.visibility = View.GONE
                                }

                                txt_program_button.text = secondObj.item_sub_category

                                txtProgramDesc.text = secondObj.item_sub_text

                                var urlImage = secondObj.bg_img

                                urlImage = urlImage.replace("zoom_level", "xxxhdpi")

                                Log.d("=======Img=====", urlImage)
                                Glide.with(this@LifePlusProgramActivity).load(urlImage)
                                    .into(imgImage)

                                val w = (width * 39) / 100
                                val h = (width * 43) / 100
                                Log.d("wwwwww", width.toString())
                                Log.d("Sub Tile", w.toString())
                                val lp = LinearLayout.LayoutParams(w, h)
                                lp.setMargins(0, 0, 4, 0)
                                cell.layoutParams = lp

                                cell.tag = secondObj
                                cell.setOnClickListener {

                                    val obj = cell.tag as ListSP


                                    loggerFB = AppEventsLogger.newLogger(this)
                                    val params = Bundle()
                                    params.putString(
                                        AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME,
                                        AppConfig.FACEBOOK_EVENT_ID_DISCOVER
                                    )
                                    params.putString(
                                        AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME,
                                        obj.action
                                    )
                                    params.putString(AppConfig.FACEBOOK_EVENT_ID_META, obj.meta)
                                    loggerFB.logEvent(
                                        AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
                                        params
                                    )


                                    processAction(obj.action, obj.meta)

                                }

                                conetentViewSub.addView(cell)

                            }


                            layout_recomonded_programs2.addView(conetentViewMain)
                        }

                    } else {
                        txt_recomonded_programs_heading.visibility = View.GONE
                        layout_recomonded_programs.visibility = View.GONE
                    }


                } else {
                    showMessage("Failed loading data")
                }
            }, {
                it.printStackTrace()
                showMessage("Failed loading data")
            })
        )
    }

    fun stopTimer() {
        //handler.removeCallbacks(null);

        if (timer != null)
            timer!!.cancel();
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun getLifePlusPrograms() {


        if (pref.savedAyuboDiscoverData == null || pref.savedAyuboDiscoverData == "") {
            subscription.add(lifePlusProgramVM.getLifePlusPrograms().subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { proDialog_lifeplusprogram2.visibility = View.VISIBLE }
                .doOnTerminate { proDialog_lifeplusprogram2.visibility = View.GONE }
                .doOnError { proDialog_lifeplusprogram2.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {

                        mainResponse = lifePlusProgramVM.mainResponse

                        val discoverMainData: JsonObject =
                            Gson().toJsonTree(mainResponse).asJsonObject;

                        pref.saveAyuboDiscoverData(discoverMainData)

                        setDiscoverUIWithData(mainResponse);


                    } else {
                        showMessage("Failed loading data")
                    }
                }, {
                    it.printStackTrace()
                    showMessage("Failed loading data")
                })
            )
        } else {

            try {
                proDialog_lifeplusprogram2.visibility = View.GONE
                val jsonParser: JsonParser = JsonParser();
                val gsonObject: JsonObject =
                    jsonParser.parse(pref.savedAyuboDiscoverData) as (JsonObject);

                val lifePlusProgramMainResponse: LifePlusProgramMainResponse =
                    Gson().fromJson(gsonObject, LifePlusProgramMainResponse::class.java);

                setDiscoverUIWithData(lifePlusProgramMainResponse)

                val task = updateAyuboDiscoverDataInBackground();
                task.execute();


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    internal inner class updateAyuboDiscoverDataInBackground() : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {


            subscription.add(lifePlusProgramVM.getLifePlusPrograms().subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
                .doOnTerminate { }
                .doOnError { }
                .subscribe({
                    if (it.isSuccess) {

                        mainResponse = lifePlusProgramVM.mainResponse

                        val discoverMainData: JsonObject =
                            Gson().toJsonTree(mainResponse).asJsonObject;
                        pref.saveAyuboDiscoverData(discoverMainData)

                        setDiscoverUIWithData(mainResponse);


                    } else {
//                        showMessage("Failed loading data")
                    }
                }, {
                    it.printStackTrace()
                })
            )



            return null
        }

    }

    private fun setDiscoverUIWithData(mainResponse: LifePlusProgramMainResponse?) {
        //=========================================
        // Program Banners View Display Strat
        val bannerList = mainResponse!!.data.banner_list

        listFragments.clear()


//                    val tabLayout = findViewById<TabLayout>(R.id.tabdots_programs)

        if ((bannerList != null) && (bannerList.isNotEmpty())) {

            bannerList.forEach {
                val fragment = ProgramBannersFragment.newInstance(it)
                listFragments.add(fragment)
            }
//                        pager_programdiscover.adapter =
//                            PagerAdapter(supportFragmentManager, listFragments)
//                        pager_programdiscover.layoutParams =
//                            RelativeLayout.LayoutParams(width, (width / 2) + 30)
//                        tabLayout.setupWithViewPager(pager_programdiscover, true)

            var currentPage: Int = 0;
            /*After setting the adapter use the timer */
            handler = Handler();
            val Update: Runnable = Runnable() {

                if (currentPage == bannerList.size) {
                    currentPage = 0;
                }
//                            pager_programdiscover.setCurrentItem(currentPage++, true);

            };

            timer = Timer(); // This will create a new Thread
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update);
                } // task to be scheduled

            }, DELAY_MS, PERIOD_MS);

        } else {
//                        banner_container.visibility = View.GONE
//                        tabdots_programs.visibility = View.GONE
        }


        // Scroll Program View Display Strat
        //========================================

        this@LifePlusProgramActivity.runOnUiThread {
            val scroll_programs = mainResponse.data.scroll_programs

            try {
                layout_recomonded_programs2.removeAllViews()

                if (scroll_programs.isNotEmpty()) {


                    for (c in 0 until scroll_programs.size) {

                        val innerScrolledList: ScrollPrograms = scroll_programs[c]

                        val conetentViewMain = LinearLayout(this)
                        conetentViewMain.orientation = LinearLayout.VERTICAL

                        val horizontalScrollView = HorizontalScrollView(this)

                        val conetentViewSub = LinearLayout(this)
                        conetentViewSub.orientation = LinearLayout.HORIZONTAL

                        val sectionHeaderText = TextView(this)
                        sectionHeaderText.text = innerScrolledList.title
                        sectionHeaderText.setPadding(0, 20, 5, 5)
                        sectionHeaderText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
                        val typeFace: Typeface? =
                            ResourcesCompat.getFont(this, R.font.roboto_bold)
                        sectionHeaderText.typeface = typeFace
                        sectionHeaderText.setTextColor(Color.parseColor("#000000"))
                        horizontalScrollView.addView(conetentViewSub)

                        conetentViewMain.addView(sectionHeaderText)
                        conetentViewMain.addView(horizontalScrollView)

                        for (i in 0 until innerScrolledList.list.size) {


                            val linearLayout = LinearLayout(this)
                            val prams = ViewGroup.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            linearLayout.layoutParams = prams
                            linearLayout.orientation = LinearLayout.HORIZONTAL

                            val secondObj: ListSP = innerScrolledList.list[i]
                            var inflater: LayoutInflater? = null
                            inflater =
                                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
                            assert(inflater != null)
                            val cell = inflater!!.inflate(R.layout.discover_cell, null)
                            val imgImage = cell.findViewById(R.id.img_image_rpc) as ImageView
                            val txt_program_button =
                                cell.findViewById(R.id.txt_program_button) as TextView
                            val txtProgramHeading =
                                cell.findViewById(R.id.txt_program_heading) as TextView
                            val txtProgramDesc =
                                cell.findViewById(R.id.txt_program_desc) as TextView
                            val txt_offer = cell.findViewById(R.id.txt_offer) as TextView
                            val offer_container =
                                cell.findViewById(R.id.offer_container) as LinearLayout


                            txtProgramHeading.text = secondObj.title


                            if (secondObj.offer_text.isNotEmpty()) {
                                txt_offer.text = secondObj.offer_text
                            } else {
                                offer_container.visibility = View.GONE
                            }

                            txt_program_button.text = secondObj.item_sub_category

                            txtProgramDesc.text = secondObj.item_sub_text

                            var urlImage = secondObj.bg_img

                            urlImage = urlImage.replace("zoom_level", "xxxhdpi")

                            Log.d("=======Img=====", urlImage)

                            Glide.with(this@LifePlusProgramActivity).load(urlImage)
                                .into(imgImage)


                            val w = (width * 39) / 100
                            val h = (width * 43) / 100
                            Log.d("wwwwww", width.toString())
                            Log.d("Sub Tile", w.toString())
                            val lp = LinearLayout.LayoutParams(w, h)
                            lp.setMargins(0, 0, 4, 0)
                            cell.layoutParams = lp

                            cell.tag = secondObj
                            cell.setOnClickListener {

                                val obj = cell.tag as ListSP

                                loggerFB = AppEventsLogger.newLogger(this)
                                val params = Bundle()
                                params.putString(
                                    AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME,
                                    AppConfig.FACEBOOK_EVENT_ID_DISCOVER
                                )
                                params.putString(
                                    AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME,
                                    obj.action
                                )
                                params.putString(AppConfig.FACEBOOK_EVENT_ID_META, obj.meta)
                                loggerFB.logEvent(
                                    AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
                                    params
                                )


                                //  FirebaseAnalytics Adding


                                var firebaseAction = ""
                                var firebaseMeta = ""


                                if (obj.action != null) {
                                    firebaseAction = obj.action
                                }


                                if (obj.meta != null) {
                                    firebaseMeta = obj.meta
                                }


                                val bundleForFirebase = Bundle();
                                bundleForFirebase.putString("ACTION", firebaseAction);
                                bundleForFirebase.putString("META", firebaseMeta);


                                if (bundleForFirebase != null) {
                                    try {
                                        SplashScreen.firebaseAnalytics.logEvent(
                                            "Discover_Service",
                                            bundleForFirebase
                                        );
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }





                                if (obj.title == "Doctor Services") {
                                    SplashScreen.firebaseAnalytics.logEvent(
                                        "Disc_DoctorService",
                                        null
                                    );
                                }


                                if (obj.title == "Nutrition Services") {
                                    SplashScreen.firebaseAnalytics.logEvent(
                                        "Disc_NutriService",
                                        null
                                    );
                                }

                                if (obj.title == "Fitness Services") {
                                    SplashScreen.firebaseAnalytics.logEvent(
                                        "Disc_FitnessService",
                                        null
                                    );
                                }


                                // Disc_MentalService


                                pref.discoverTileName = secondObj.item_sub_category


                                processAction(obj.action, obj.meta)
//                                        processAction("patient_report_review", "98bb0dbf-3c6f-8151-6f75-5a5dc4d58d24:free")
//                                        processAction("doctor_booking", "98bb0dbf-3c6f-8151-6f75-5a5dc4d58d24:paid")

                            }

                            conetentViewSub.addView(cell)

                        }


                        layout_recomonded_programs2.addView(conetentViewMain)
                    }

                } else {
                    txt_recomonded_programs_heading.visibility = View.GONE
                    layout_recomonded_programs.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    public fun processDefaultAction(action: String, meta: String) {
        processAction(action, meta)
    }

    override fun onCardClickListener(action: String, meta: String) {
    }

    override fun onBannerCardClickListener(action: String, meta: String) {
        processAction(action, meta)
    }


}
