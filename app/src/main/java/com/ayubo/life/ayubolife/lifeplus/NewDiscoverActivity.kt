package com.ayubo.life.ayubolife.lifeplus

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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
import com.ayubo.life.ayubolife.payment.activity.PurchaseHistory
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.push.Config
import com.ayubo.life.ayubolife.qrcode.DecoderActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.satalite_menu.CommiunityActivity
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.utility.Utility
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
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_life_plus_program.*
import kotlinx.android.synthetic.main.activity_new_discover_program.*
import kotlinx.android.synthetic.main.activity_new_profile.*
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

class NewDiscoverActivity : BaseActivity(), NewDiscoverAdapter.OnCardClickListener {
    var width: Int = 0;
    lateinit var pref: PrefManager;
    lateinit var prefs: SharedPreferences;
    lateinit var userid_ExistingUser: String;
    lateinit var userid_ExistingUser_static: String;
    lateinit var hasToken: String;
    lateinit var toolbar: Toolbar;

    //    lateinit var btn_notifications: ImageView;
//    lateinit var txt_noti_count: TextView;
    lateinit var drawer: DrawerLayout;
    lateinit var navigationView: NavigationView;
    lateinit var navHeader: View;
    lateinit var txtName: TextView;
    lateinit var imgProfile: CircleImageView;
    lateinit var activityTitles: Array<String>;
    var navItemIndex: Int = 0;
    var TAG_HOME: String = "home";
    var CURRENT_TAG: String = TAG_HOME;
    lateinit var encodedHashToken: String;
    lateinit var url: String;
    lateinit var fragment: Fragment
    var deviceIdPush: String? = null;
    var TAG: String = NewDiscoverActivity::class.java.simpleName;
    var isFromSearchResults: Boolean = false
    var mainResponse: LifePlusProgramMainResponse? = null
    var notificationCount: String = "0";
    lateinit var appToken: String;

    var listItems: ArrayList<SliderData> = ArrayList<SliderData>();

    private var sliderAdapter: SliderAdapter? = null

    var timer: Timer? = null;

    lateinit var ic_call_image: ImageView;
    lateinit var navi_header_mobile_no: TextView;
    lateinit var view_container: LinearLayout;
    lateinit var layout_for_link_to_profile: LinearLayout;

    @Inject
    lateinit var lifePlusProgramVM: LifePlusProgramVM

    private var newDiscoverAdapter: NewDiscoverAdapter? = null

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
                val window: Window = window;
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.statusBarColor = whiteInt;

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor: View = window.decorView;
                decor.systemUiVisibility = 0;
            }

            setContentView(R.layout.activity_new_home_with_side_menu_lipfeplus);
        } else {
            setContentView(R.layout.activity_new_home_with_side_menu_hemas);
        }

        pref = PrefManager(this);
        appToken = pref.userToken;
        width = getScreenWidth()
        pref.newServiceLastRunningTime = "0";

        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        pref.setMapReload(false);

        try {

            userid_ExistingUser = pref.loginUser["uid"].toString();
            userid_ExistingUser_static = pref.loginUser["uid"].toString();
            hasToken = pref.loginUser["hashkey"].toString();
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
//                Intent(this@NewDiscoverActivity, NotificationsListActivity::class.java);
//            startActivity(intent);
//        };

//        lay_search.setOnClickListener {
//            DiscoverSearchActivity.startActivity(this@NewDiscoverActivity)
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
        view_container = navHeader.findViewById(R.id.view_container);
        navi_header_mobile_no = navHeader.findViewById(R.id.navi_header_mobile_no);
        ic_call_image = navHeader.findViewById(R.id.ic_call_image);
        layout_for_link_to_profile = navHeader.findViewById(R.id.layout_for_link_to_profile);

        // load toolbar titles from string resources
        activityTitles = resources.getStringArray(R.array.nav_item_activity_titles);


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
            if (Constants.type == Constants.Type.LIFEPLUS) {
                view_container.setBackgroundResource(R.drawable.lifeplus_nav_header_main_bg)
            }


            val sName: String? = pref.userProfile["name"];
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
                        this@NewDiscoverActivity,
                        UserProfileActivity::class.java
                    )
                );
                drawer.closeDrawers();
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    private fun Service_getSetDefaultDevice_ServiceCall() {

        try {
            if (Utility.isInternetAvailable(NewDiscoverActivity@ this)) {
                val task: NewDiscoverActivity.Service_SetDefaultDevice = Service_SetDefaultDevice();
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

    private fun loadDiscoverFragment() {

        selectNavMenu();
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        fragment = getNewDiscoverFragment();
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

    private fun getNewDiscoverFragment(): Fragment {
        val newDiscoverFragment: NewDiscoverActivityFragment = NewDiscoverActivityFragment();
        return newDiscoverFragment;


    }

    private fun selectNavMenu() {

        try {
//            val tabName: String = Ram.getTopMenuTabName();
            navigationView.menu.getItem(navItemIndex).isChecked = true;
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show();
        }


    }


    private fun setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {
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
                                "Check out the all in one ayubo.life wellness app. " + ApiClient.MAIN_URL_APPS + "download";
                            sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Share ayubo.life"
                            );
                            title = "Share ayubo.life";
                        } else if (Constants.type == Constants.Type.LIFEPLUS) {
                            shareBody =
                                "Check out the all in one lifeplus wellness app." + ApiClient.MAIN_URL_APPS + "downloadlifeplus";
                            sendIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Share lifeplus"
                            );
                            title = "Share lifeplus";
                        } else {
                            shareBody =
                                "Check out the all in one ayubo.life wellness app." + ApiClient.MAIN_URL_APPS + " download";
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
                            this@NewDiscoverActivity,
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

                    startActivity(Intent(this@NewDiscoverActivity, CommiunityActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_payments -> {
//                    val intentPay: Intent =
//                            Intent(this@NewDiscoverActivity, PaymentSettingsActivity::class.java);
//                    startActivity(intentPay);
//
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@NewDiscoverActivity, EnterPromoCodeActivity::class.java);
                    intentPromo.putExtra("fromClass", "no");
                    startActivity(intentPromo);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent =
                        Intent(this@NewDiscoverActivity, LifePointActivity::class.java);
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
                        Intent(this@NewDiscoverActivity, DecoderActivity::class.java);
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
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, PurchaseHistory::class.java));
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
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {
                    //  FirebaseAnalytics Added Inside
                    startActivity(
                        Intent(
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, Badges_Activity::class.java));
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


                    drawer.closeDrawers();
                    loading_bar.visibility = View.VISIBLE
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

                            loading_bar.visibility = View.GONE
                            if (response.isSuccessful) {


                                val wnwValidateMainData: JsonObject =
                                    Gson().toJsonTree(response.body()!!.data).asJsonObject;

//                                val intent2: Intent =
//                                    Intent(this@NewDiscoverActivity, GroupViewActivity::class.java);
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
                            loading_bar.visibility = View.GONE
                            Toast.makeText(baseContext, "Server error", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })


//
//                    val intent2: Intent =
//                        Intent(this@NewDiscoverActivity, GroupViewActivity::class.java);
//                    intent2.putExtra(EXTRA_PAYMENT_META, "100");
//                    startActivity(intent2);
//                    drawer.closeDrawers();


                }
                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@NewDiscoverActivity, EnterPromoCodeActivity::class.java);
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
                            this@NewDiscoverActivity,
                            InsurancePolicesActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent =
                        Intent(this@NewDiscoverActivity, LifePointActivity::class.java);
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
                    startActivity(Intent(this@NewDiscoverActivity, PurchaseHistory::class.java));
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
                            this@NewDiscoverActivity,
                            MainPaymentHistoryActivity::class.java
                        )
                    );
                    drawer.closeDrawers();
                    true
                }
                R.id.nav_qrcode -> {
                    //  FirebaseAnalytics Added inside
                    val intent: Intent =
                        Intent(this@NewDiscoverActivity, DecoderActivity::class.java);
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
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                }
                R.id.nav_help -> {
                    //  FirebaseAnalytics Added
                    startActivity(
                        Intent(
                            this@NewDiscoverActivity,
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
//                    startActivity(Intent(this@NewDiscoverActivity, CommiunityActivity::class.java));
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intent: Intent =
                        Intent(this@NewDiscoverActivity, EnterPromoCodeActivity::class.java);
                    intent.putExtra("fromClass", "no");
                    startActivity(intent);
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_lifepoints -> {
//                    //  FirebaseAnalytics Added Inside
//                    val inl: Intent = Intent(this@NewDiscoverActivity, LifePointActivity::class.java);
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
                        Intent(this@NewDiscoverActivity, DecoderActivity::class.java);
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
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, PurchaseHistory::class.java));
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
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {

                    //  FirebaseAnalytics Added Inside
                    startActivity(
                        Intent(
                            this@NewDiscoverActivity,
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
                    startActivity(Intent(this@NewDiscoverActivity, Badges_Activity::class.java));
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
                            this@NewDiscoverActivity,
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

    companion object {
        fun startActivity(
            context: Context,
            isFromSearchResults: Boolean,
            id: String,
            name: String,
            type: String
        ) {
            val intent = Intent(context, NewDiscoverActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_RESULTS_FROM, isFromSearchResults)
            intent.putExtra(EXTRA_SEARCH_RESULTS_ID, id)
            intent.putExtra(EXTRA_SEARCH_RESULTS_NAME, name)
            intent.putExtra(EXTRA_SEARCH_RESULTS_TYPE, type)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        readExtra()
        // load nav menu header data
        loadNavHeader();
        if (pref.getUserToken().length > 50) {
            getNotificationsData();
        }
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
                                this@NewDiscoverActivity,
                                R.anim.bounce
                            );
                            val interpolator: NewDiscoverActivity.MyBounceInterpolator =
                                NewDiscoverActivity.MyBounceInterpolator(0.5, 20.0);
                            myAnim.setInterpolator(interpolator);


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

    private fun readExtra() {

        isFromSearchResults = intent.getBooleanExtra(EXTRA_SEARCH_RESULTS_FROM, false)

        if (isFromSearchResults) {
            val id = intent.getStringExtra(EXTRA_SEARCH_RESULTS_ID)
            val name = intent.getStringExtra(EXTRA_SEARCH_RESULTS_NAME)
            val type = intent.getStringExtra(EXTRA_SEARCH_RESULTS_TYPE)
            getDiscoverSearchedResults(id!!, name!!, type!!)
        } else {
            getNewDiscoverPrograms()
        }

    }

    private fun getNewDiscoverPrograms() {


        if (pref.savedAyuboDiscoverData != null && pref.savedAyuboDiscoverData != "") {
            val jsonParser: JsonParser = JsonParser();

            val jsonObject: JsonObject =
                jsonParser.parse(pref.savedAyuboDiscoverData) as (JsonObject);


            if (jsonObject.size() > 0 && jsonObject.has("data")) {

                if (jsonObject.get("data").asJsonObject.has("banner_list")) {
                    pref.saveAyuboDiscoverData(JsonObject())
                }

            }
        }





        if (pref.savedAyuboDiscoverData == null || pref.savedAyuboDiscoverData == "") {
            loading_bar.visibility = View.VISIBLE
            val apiService: ApiInterface =
                ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);


            val call: Call<ProfileDashboardResponseData> = apiService.getDiscoverData(
                AppConfig.APP_BRANDING_ID,
                appToken
            );

            call.enqueue(object : Callback<ProfileDashboardResponseData> {
                override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
                ) {
                    loading_bar.visibility = View.GONE
                    if (response.isSuccessful) {
                        System.out.println(response)


                        val discovery: JsonObject =
                            Gson().toJsonTree(response.body()!!.data).asJsonObject;

                        pref.saveAyuboDiscoverData(discovery)
                        setLifePlusDiscoverUIWithData(discovery);


                    } else {
                        loading_bar.visibility = View.GONE
                        Toast.makeText(
                            baseContext,
                            "There is an issue when loading data, Please contact admin.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProfileDashboardResponseData>, p1: Throwable) {
                    loading_bar.visibility = View.GONE
                }

            })
        } else {
            try {

                loading_bar.visibility = View.GONE
                val jsonParser: JsonParser = JsonParser();
                val savedAyuboDiscoverDataGsonObject: JsonObject =
                    jsonParser.parse(pref.savedAyuboDiscoverData) as (JsonObject);


                setLifePlusDiscoverUIWithData(savedAyuboDiscoverDataGsonObject);

                val task = updateAyuboDiscoverDataInBackground();
                task.execute();

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }


    internal inner class updateAyuboDiscoverDataInBackground() : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {


            val apiService: ApiInterface =
                ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);


            val call: Call<ProfileDashboardResponseData> = apiService.getDiscoverData(
                AppConfig.APP_BRANDING_ID,
                appToken
            );

            call.enqueue(object : Callback<ProfileDashboardResponseData> {
                override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
                ) {
                    if (response.isSuccessful) {
                        System.out.println(response)


                        val discovery: JsonObject =
                            Gson().toJsonTree(response.body()!!.data).asJsonObject;

                        pref.saveAyuboDiscoverData(discovery)
                        setLifePlusDiscoverUIWithData(discovery);


                    }
                }

                override fun onFailure(call: Call<ProfileDashboardResponseData>, p1: Throwable) {

                }

            })



            return null
        }

    }


    @SuppressLint("WrongConstant")
    fun setLifePlusDiscoverUIWithData(discovery: JsonObject) {

        val imageListArray: JsonArray = discovery.get("banner_items").asJsonArray
        listItems = ArrayList<SliderData>();

        for (i in 0 until imageListArray.size()) {
            val imageListItem: JsonObject = imageListArray.get(i).asJsonObject
            listItems.add(
                SliderData(
                    imageListItem.get("image_url").asString,
                    imageListItem.get("action").asString,
                    imageListItem.get("meta").asString
                )
            );
        }


        val discoveryMainData: JsonArray = discovery.get("cards").asJsonArray

        if (discoveryMainData.size() > 0) {
            try {
                discover_data_recyclerview.layoutManager =
                    LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)
//                            discover_data_recyclerview.isNestedScrollingEnabled = false;
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
                        listItems
                    )
                )


                for (i in 0 until discoveryMainData.size()) {
                    val discover = discoveryMainData.get(i)
                    discoverDataItemsArrayList.add(
                        NewDiscoverCardItem(
                            discover.asJsonObject.get("title").asString,
                            discover.asJsonObject.get("summary").asString,
                            discover.asJsonObject.get("icon_url").asString,
                            discover.asJsonObject.get("label").asString,
                            discover.asJsonObject.get("card_image_url").asString,
                            discover.asJsonObject.get("action").asString,
                            discover.asJsonObject.get("meta").asString,
                            null
                        )

                    )


                }



                newDiscoverAdapter =
                    NewDiscoverAdapter(
                        baseContext,
                        discoverDataItemsArrayList,
                        false,
                        this@NewDiscoverActivity
                    )
                newDiscoverAdapter!!.onClickListener = this@NewDiscoverActivity
                discover_data_recyclerview.adapter = newDiscoverAdapter
                discover_data_recyclerview.isNestedScrollingEnabled = false
                main_new_discovery_scroll_view.isSmoothScrollingEnabled = true


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onStop() {
        super.onStop()

//        if (timer != null)
//            timer!!.cancel();
    }


//    private val timerTask_timerForAnimation = object : TimerTask() {
//        override fun run() {
//
//            runOnUiThread(Runnable {
//                if (my_pager.getCurrentItem() < listItems.size - 1) {
//                    my_pager.setCurrentItem(my_pager.getCurrentItem() + 1);
//                } else
//                    my_pager.setCurrentItem(0);
//
//
//            })
//        }
//    }

    private fun getDiscoverSearchedResults(id: String, name: String, type: String) {


        subscription.add(lifePlusProgramVM.getDiscoverSearchedResults(id, name, type).subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading_bar.visibility = View.VISIBLE }
            .doOnTerminate { loading_bar.visibility = View.GONE }
            .doOnError { loading_bar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {

                    mainResponse = lifePlusProgramVM.mainResponse

                    System.out.println(mainResponse)


                } else {
                    showMessage("Failed loading data")
                }
            }, {
                it.printStackTrace()
                showMessage("Failed loading data")
            })
        )
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

    class MyBounceInterpolator(amplitude: Double, frequency: Double) :
        android.view.animation.Interpolator {
        var mAmplitude: Double = amplitude;
        var mFrequency: Double = frequency;


        override fun getInterpolation(time: Float): Float {
            return (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1).toFloat();
        }
    }

    fun processDefaultAction(action: String, meta: String) {
        processAction(action, meta)
    }

    override fun onCardClickListener(action: String, meta: String) {


        val loggerFB = AppEventsLogger.newLogger(this)
        val params = Bundle()
        params.putString(
            AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME,
            AppConfig.FACEBOOK_EVENT_ID_DISCOVER
        )
        params.putString(
            AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME,
            action
        )
        params.putString(AppConfig.FACEBOOK_EVENT_ID_META, meta)
        loggerFB.logEvent(
            AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
            params
        )




        if (meta == "178" && SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Disc_DoctorService", null);
        }

        if (meta == "179" && SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Disc_NutriService", null);
        }

        if (meta == "180" && SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Disc_FitnessService", null);
        }

        if (meta == "1069" && SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Disc_MentalService", null);
        }


        processAction(action, meta)
    }

    override fun onBannerCardClickListener(action: String, meta: String) {
        processAction(action, meta)
    }


}