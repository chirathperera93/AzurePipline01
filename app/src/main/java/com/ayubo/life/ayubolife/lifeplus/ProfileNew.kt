package com.ayubo.life.ayubolife.lifeplus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ayubo.life.ayubolife.BuildConfig
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.AppUpdateChecker
import com.ayubo.life.ayubolife.activity.ContactInfoActivity
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices
import com.ayubo.life.ayubolife.insurances.Activities.InsurancePolicesActivity
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity
import com.ayubo.life.ayubolife.login.EnterPromoCodeActivity
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.login.UserProfileActivity
import com.ayubo.life.ayubolife.login.vm.UserProfileVM
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.new_payment.PUSH_ACTION
import com.ayubo.life.ayubolife.new_payment.PUSH_META
import com.ayubo.life.ayubolife.notification.activity.NotificationsListActivity
import com.ayubo.life.ayubolife.notification.model.NotiCountMainData
import com.ayubo.life.ayubolife.notification.model.NotiCountMainResponse
import com.ayubo.life.ayubolife.payment.activity.MainPaymentHistoryActivity
import com.ayubo.life.ayubolife.payment.activity.PurchaseHistory
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.push.Config
import com.ayubo.life.ayubolife.qrcode.DecoderActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.RevampV1DashboardFragment
import com.ayubo.life.ayubolife.satalite_menu.CommiunityActivity
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.signalr.SignalRSingleton
import com.ayubo.life.ayubolife.utility.Utility
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.flavor.activity.AboutActivity
import com.flavors.changes.Constants
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import kotlinx.android.synthetic.main.activity_new_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.ask_main_cell.view.*
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

class ProfileNew : BaseActivity(),
    RevampV1DashboardFragment.ChangeFragmentInterface {
    var width: Int = 0
    lateinit var pref: PrefManager;
    lateinit var userid_ExistingUser: String;
    lateinit var userid_ExistingUser_static: String;
    lateinit var hasToken: String;
    lateinit var toolbar: Toolbar;
    lateinit var drawer: DrawerLayout;
    lateinit var navigationView: NavigationView;
    lateinit var navHeader: View;
    var navItemIndex: Int = 0;
    var TAG_HOME: String = "profile_new";
    var CURRENT_TAG: String = TAG_HOME;
    lateinit var encodedHashToken: String;
    lateinit var url: String;
    lateinit var txtName: TextView;
    lateinit var txt_noti_count: TextView;
    lateinit var navi_header_mobile_no: TextView;

    //    lateinit var btn_notifications: ImageView;
    lateinit var ic_call_image: ImageView;
    lateinit var imgProfile: CircleImageView;
    lateinit var prefs: SharedPreferences;
    lateinit var activityTitles: Array<String>;
    var TAG: String = ProfileNew::class.java.simpleName;
    lateinit var fragment: Fragment

    var notificationCount: String = "0";

    var deviceIdPush: String? = null;

    //    lateinit var signalRForegroundService: SignalRForegroundService;
    lateinit var mServiceIntent: Intent;
    lateinit var view_container: LinearLayout;
    lateinit var layout_for_link_to_profile: LinearLayout;

    lateinit var signalRSingleton: SignalRSingleton


    @Inject
    lateinit var lifePlusProgramVM: LifePlusProgramVM

    @Inject
    lateinit var userProfileVM: UserProfileVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getInstance().appComponent.inject(this)


        createSignalRConnection();

        if (Constants.type == Constants.Type.AYUBO) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            setContentView(R.layout.activity_new_home_with_side_menu);
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            val color: String = "#000000";
            val whiteInt: Int = Color.parseColor(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = window;
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.statusBarColor = whiteInt;

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor: View = window.decorView;

                //    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                decor.systemUiVisibility = 0;
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
        pref.newServiceLastRunningTime = "0";

        prefs = this.getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        pref.setMapReload(false);


        if (!pref.isSentProfileData) {
            getProfileData().execute();
        }



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
//        val txt_expert_heading: TextView = toolbar.findViewById(R.id.txt_expert_heading);
//        txt_expert_heading.text = "Me"
//        lay_search_icon.visibility = View.VISIBLE
//        txt_noti_count = toolbar.findViewById(R.id.txt_noti_count);
//        txt_noti_count.setVisibility(View.VISIBLE);
//        btn_notifications.setVisibility(View.VISIBLE);


//        lay_notifications.setOnClickListener {
//            val intent: Intent = Intent(baseContext, NotificationsListActivity::class.java);
//            startActivity(intent);
//        };

//        lay_search.setOnClickListener {
//            DiscoverSearchActivity.startActivity(baseContext)
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


    internal inner class getProfileData : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg p0: Void?): Void? {

            subscription.add(userProfileVM.getNewProfileById(pref.loginUser["uid"].toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { }
                .doOnTerminate { }
                .doOnError { }
                .subscribe {
                    if (it.isSuccess) {
                        pref.setProfileDataSend(true);
                        val data = userProfileVM.newUserProfileData
                        val name = data.first_name + " " + data.last_name;


                        var profileImageURL = "";

                        if (data.image_url == null || data.image_url == "") {
                            profileImageURL = pref.loginUser["image_path"].toString();
                        } else {
                            profileImageURL = data.image_url;
                        }

                        pref.createUserProfile(
                            name,
                            data.email,
                            data.date_of_birth,
                            data.gender.toString(),
                            data.nic,
                            data.phone_mobile,
                            data.countrycode_c,
                            profileImageURL,
                            data.id,
                            data.user_name
                        )

                        pref.createLoginUser(
                            data.id,
                            name,
                            data.email,
                            data.phone_mobile,
                            pref.loginUser["hashkey"].toString(),
                            profileImageURL,
                            data.countrycode_c
                        )
                    }
                }
            );

            return null;
        }
    }

    private fun loadNavHeader() {
        try {
            if (Constants.type == Constants.Type.LIFEPLUS) {
                view_container.setBackgroundResource(R.drawable.lifeplus_nav_header_main_bg)
            }


            // txtName.setText(fullName);
            val sName: String? = pref.userProfile["name"];
            // userEmail=pref.getLoginUser().get("email");
            txtName.text = sName;

            var imageURL = pref.loginUser["image_path"].toString();

            if (imageURL.contains(".jpg") || imageURL.contains(".jpeg") || imageURL.contains(".png")) {
                Glide.with(baseContext).load(imageURL).into(imgProfile)
            } else {
                if (imageURL != "" && !imageURL.contains(MAIN_URL_LIVE_HAPPY)) {
                    imageURL = MAIN_URL_LIVE_HAPPY + imageURL;
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
                startActivity(Intent(this@ProfileNew, UserProfileActivity::class.java));
                drawer.closeDrawers();
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun createSignalRConnection() {


        signalRSingleton = SignalRSingleton.getInstance(baseContext);



        if (isNetworkAvailable(baseContext)) {
            try {
                createSignalRConnectionHub().execute();

            } catch (e: Exception) {
                e.printStackTrace();
            }

        } else {
            try {
                Toast.makeText(baseContext, "No internet connectivity", Toast.LENGTH_SHORT)
                    .show();
            } catch (e: Exception) {
                e.printStackTrace();
            }

        }


//        // create signalr foreground
//        signalRForegroundService = SignalRForegroundService();
//        mServiceIntent = Intent(this, signalRForegroundService::class.java);
//        if (!isMyServiceRunning(signalRForegroundService)) {
//
//            createSignalRConnectionHub().execute();
//        }
    }

//    private fun isNetworkAvailable(): Boolean {
//        val connectivityManager: ConnectivityManager =
//            getSystemService(Context.CONNECTIVITY_SERVICE) as (ConnectivityManager);
//
//        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo;
//
//        val connected: Boolean =
//            activeNetworkInfo != null && activeNetworkInfo.isAvailable && activeNetworkInfo.isConnected;
//
//        print(connected)
//        return connected;
//    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }


    internal inner class createSignalRConnectionHub : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
//                startService(mServiceIntent);
                signalRSingleton.startConnection();
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }

//    private fun isMyServiceRunning(serviceClass: SignalRForegroundService): Boolean {
//        val manager: ActivityManager =
//            getSystemService(Context.ACTIVITY_SERVICE) as (ActivityManager);
//
//        for (service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.javaClass.name.equals(service.service.className)) {
//                Log.i("Service status", "Running");
//                return true;
//            }
//        }
//        Log.i("Service status", "Not running");
//        return false;
//    }

    override fun onDestroy() {
//        val broadcastIntent: Intent = Intent();
//        broadcastIntent.action = "restartservice";
//        broadcastIntent.setClass(this, ReStartForegroundService::class.java);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy()
    }

    fun goToDiscover(view: View) {
//        val intent: Intent = Intent(baseContext, LifePlusProgramActivity::class.java);
//        val intent: Intent = Intent(baseContext, NewDiscoverActivity::class.java);
        val intent: Intent = SetDiscoverPage().getDiscoverIntent(this);
        intent.putExtra("isFromSearchResults", false);
        startActivity(intent);
    }

    fun goToMessageList(view: View) {
        val intent: Intent = Intent(
            baseContext,
            NotificationsListActivity::class.java
        )
        startActivity(intent)
    }

//    fun goToFeed(view: View) {
//        val intent = Intent(baseContext, NewHomeWithSideMenuActivity::class.java);
//        startActivity(intent);
//    }

    override fun onResume() {
        super.onResume()


//        val bundle = intent.extras
//        if (bundle != null && (bundle.containsKey(PUSH_META) || bundle.containsKey(PUSH_ACTION))) {
//            val action = bundle.getSerializable(PUSH_ACTION) as String
//            val meta = bundle.getSerializable(PUSH_META) as String
//            processAction(action, meta)
//        }


        // load nav menu header data
        loadNavHeader();
        if (pref.userToken.length > 50) {
            getNotificationsData();
        }

        val googleSupportServices: GoogleSupportServices =
            GoogleSupportServices(this@ProfileNew);

        if (googleSupportServices.isGooglePlayServicesAvailable) {
            val appUpdateChecker: AppUpdateChecker = AppUpdateChecker(this@ProfileNew);
            appUpdateChecker.checkForUpdate(false);
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val bundle = intent!!.extras
        if (bundle != null && (bundle.containsKey(PUSH_META) || bundle.containsKey(PUSH_ACTION))) {
            val action = bundle.getSerializable(PUSH_ACTION) as String
            val meta = bundle.getSerializable(PUSH_META) as String
            processAction(action, meta)
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
                                "Check out the all in one ayubo.life wellness app." + MAIN_URL_APPS + "download";
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
                    startActivity(Intent(this@ProfileNew, InsurancePolicesActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, PurchaseHistory::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, MainPaymentHistoryActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_coporate -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Join_community_clicked", null);
                    }

                    startActivity(Intent(this@ProfileNew, CommiunityActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_payments -> {
//                    val intentPay: Intent =
//                            Intent(this@ProfileNew, PaymentSettingsActivity::class.java);
//                    startActivity(intentPay);
//
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@ProfileNew, EnterPromoCodeActivity::class.java);
                    intentPromo.putExtra("fromClass", "no");
                    startActivity(intentPromo);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent = Intent(this@ProfileNew, LifePointActivity::class.java);
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
                    val intent: Intent = Intent(this@ProfileNew, DecoderActivity::class.java);
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
                    startActivity(Intent(this@ProfileNew, UserProfileActivity::class.java));
                    //   startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                    drawer.closeDrawers();
                    true;
                }

                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {
                    //  FirebaseAnalytics Added Inside
                    startActivity(Intent(this@ProfileNew, HelpFeedbackActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_badges -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, Badges_Activity::class.java));
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
                    profile_new_loading.visibility = View.VISIBLE
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

                            profile_new_loading.visibility = View.GONE
                            if (response.isSuccessful) {


                                val wnwValidateMainData: JsonObject =
                                    Gson().toJsonTree(response.body()!!.data).asJsonObject;

//                                val intent2: Intent =
//                                    Intent(this@ProfileNew, GroupViewActivity::class.java);
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
                            profile_new_loading.visibility = View.GONE
                            Toast.makeText(baseContext, "Server error", Toast.LENGTH_SHORT)
                                .show()
                        }

                    })


                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intentPromo: Intent =
                        Intent(this@ProfileNew, EnterPromoCodeActivity::class.java);
                    intentPromo.putExtra("fromClass", "no");
                    startActivity(intentPromo);

                    drawer.closeDrawers();
                    true
                }

                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, PurchaseHistory::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, MainPaymentHistoryActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_insurance -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Clicked_insurances", null);
                    }
                    startActivity(Intent(this@ProfileNew, InsurancePolicesActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_lifepoints -> {
                    //  FirebaseAnalytics Added Inside
                    val inl: Intent = Intent(this@ProfileNew, LifePointActivity::class.java);
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
                    val intent: Intent = Intent(this@ProfileNew, DecoderActivity::class.java);
                    intent.putExtra("error", "old");
                    startActivity(intent);

                    drawer.closeDrawers();
                }

                R.id.nav_profile -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Profile_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, UserProfileActivity::class.java));
                    drawer.closeDrawers();
                }

                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, AboutActivity::class.java));
                    drawer.closeDrawers();
                }

                R.id.nav_help -> {
                    //  FirebaseAnalytics Added Inside
                    startActivity(Intent(this@ProfileNew, HelpFeedbackActivity::class.java));
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
//                    startActivity(Intent(this@ProfileNew, CommiunityActivity::class.java));
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_promo -> {
                    //  FirebaseAnalytics Added Inside
                    val intent: Intent =
                        Intent(this@ProfileNew, EnterPromoCodeActivity::class.java);
                    intent.putExtra("fromClass", "no");
                    startActivity(intent);
                    drawer.closeDrawers();
                    true
                }

//                R.id.nav_lifepoints -> {
//                    //  FirebaseAnalytics Added Inside
//                    val inl: Intent = Intent(this@ProfileNew, LifePointActivity::class.java);
//                    startActivity(inl);
//                    drawer.closeDrawers();
//                    true
//                }

                R.id.nav_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("History_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, PurchaseHistory::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_payment_history -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Payment_History_Clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, MainPaymentHistoryActivity::class.java));
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
                    val intent: Intent = Intent(this@ProfileNew, DecoderActivity::class.java);
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
                    startActivity(Intent(this@ProfileNew, UserProfileActivity::class.java));
                    // startActivity(new Intent(NewHomeWithSideMenuActivity.this, ProfileActivity.class));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_about -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("About_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, AboutActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_help -> {

                    //  FirebaseAnalytics Added Inside
                    startActivity(Intent(this@ProfileNew, HelpFeedbackActivity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_badges -> {
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Badges_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, Badges_Activity::class.java));
                    drawer.closeDrawers();
                    true
                }

                R.id.nav_contact -> {
                    //  FirebaseAnalytics Added Inside
                    //  FirebaseAnalytics Adding
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("ContactInfo_clicked", null);
                    }
                    startActivity(Intent(this@ProfileNew, ContactInfoActivity::class.java));
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

        try {
            fragment = getProfileNewFragment();
            val fragmentTransaction: FragmentTransaction =
                supportFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            );
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
            //Closing drawer on item click
            drawer.closeDrawers();
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun getProfileNewFragment(): Fragment {


        val profileNewFragment: ProfileNewFragment = ProfileNewFragment();

        return profileNewFragment;


    }

    private fun selectNavMenu() {

//        val tabName: String = Ram.getTopMenuTabName();
//        if(tabName.equals("history")){
//            navItemIndex=3;
//        }

        try {
            navigationView.menu.getItem(navItemIndex).isChecked = true;
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
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

                            try {
                                val myAnim: Animation =
                                    AnimationUtils.loadAnimation(this@ProfileNew, R.anim.bounce);
                                val interpolator: ProfileNew.MyBounceInterpolator =
                                    ProfileNew.MyBounceInterpolator(0.5, 20.0);
                                myAnim.interpolator = interpolator;
//                                btn_notifications.startAnimation(myAnim);
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


//                            if (!isFirsttime) {
//                                isFirsttime = true;
//                                MediaPlayer mp = MediaPlayer.create(NewHomeWithSideMenuActivity.this, Settings.System.DEFAULT_NOTIFICATION_URI);
//                                if (mp != null) {
//                                    mp.start();
//                                }
//
//                            }


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

    private fun Service_getSetDefaultDevice_ServiceCall() {

        try {
            if (Utility.isInternetAvailable(ProfileNewActivity@ this)) {
                val task: ProfileNew.Service_SetDefaultDevice = Service_SetDefaultDevice();
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

    override fun onGotoToDoClick(action: String) {
        if (action == "todo") {
            val tabLayout: TabLayout = findViewById<TabLayout>(R.id.tabLayoutMain)
            val tab: TabLayout.Tab? = tabLayout.getTabAt(1)
            tab!!.select()
        }
    }
}