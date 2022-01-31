package com.ayubo.life.ayubolife.lifeplus

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.BuildConfig
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.common.ShowCommonAlertPopUp
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity
import com.ayubo.life.ayubolife.huawei_hms.GoogleSupportServices
import com.ayubo.life.ayubolife.huawei_hms.HuaweiResult
import com.ayubo.life.ayubolife.huawei_hms.HuaweiScopes
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity
import com.ayubo.life.ayubolife.login.LoginActivity_First
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.payment.activity.PaymentActivity
import com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse
import com.ayubo.life.ayubolife.pojo.timeline.Post
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.timeline.models.PopupMainData
import com.ayubo.life.ayubolife.timeline.models.PopupMainResponse
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.StepObj
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.appevents.AppEventsLogger
import com.flavors.changes.Constants
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResult
import com.google.android.gms.location.ActivityRecognition
import com.google.android.material.tabs.TabLayout
import com.huawei.hmf.tasks.Task
import com.huawei.hms.hihealth.DataController
import com.huawei.hms.hihealth.HiHealthOptions
import com.huawei.hms.hihealth.HuaweiHiHealth
import com.huawei.hms.hihealth.SettingController
import com.huawei.hms.hihealth.data.SampleSet
import com.huawei.hms.support.hwid.HuaweiIdAuthAPIManager
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ProfileNewFragment : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    lateinit var mainView: View
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var pref: PrefManager


    private lateinit var loggerFB: AppEventsLogger
    var PACKAGE_NAME: String = "com.google.android.apps.fitness"
    lateinit var mApiClient: GoogleApiClient
    lateinit var jsonStepsForDailyArrayForNewSave: ArrayList<StepObj>
    lateinit var jsonStepsForWeekArrayForNewSave: ArrayList<StepObj>
    var TAG: String = "MainActivity"
    lateinit var todayStepsToSendToServer: String
    lateinit var userid_ExistingUser: String
    lateinit var appToken: String
    private var isLinkedHuawei: Boolean = false
    private lateinit var mSettingController: SettingController
    private var REQUEST_LOGIN_CODE: Int = 1102
    private var REQUEST_SIGN_IN_LOGIN: Int = 1002
    private var REQUEST_OAUTH: Int = 1
    private var authInProgress: Boolean = false
    private var REQUEST_HEALTH_AUTH: Int = 1003
    private var HEALTH_SCHEME: String = "huaweischeme://healthapp/achievement?module=kit"
    var googleFitAlertDialog: AlertDialog? = null
    var isOpenedDialog: Boolean = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.activity_new_profile, container, false)


        pref = PrefManager(context)
        userid_ExistingUser = pref.loginUser["uid"].toString()
        val bottom_menu_view_lifeplus =
            mainView.findViewById<LinearLayout>(R.id.bottom_menu_view_lifeplus)


        val args: Bundle? = arguments
        val isShowBottomBar: Boolean = args?.getBoolean("isShowBottomBar", true) ?: true
        val dashboard_meta: String = args?.getString("dashboard_meta", "") ?: ""

        pref.newDashBoardData = dashboard_meta




        if (isShowBottomBar) {
            bottom_menu_view_lifeplus.visibility = View.VISIBLE
            val img_btn_notifications = mainView.findViewById<ImageView>(R.id.img_btn_notifications)

            if (Constants.type == Constants.Type.LIFEPLUS) {
                img_btn_notifications.setImageResource(R.drawable.active_home_life_plus_image)
//                context?.resources?.getColor(R.color.black)?.let { bottom_menu_view_lifeplus.setBackgroundColor(it) }
            } else if (Constants.type == Constants.Type.AYUBO) {
                img_btn_notifications.setImageResource(R.drawable.active_ayubo_life_home)
            }
        } else {
            bottom_menu_view_lifeplus.visibility = View.GONE
        }


        initView(isShowBottomBar)


        val googleSupportServices: GoogleSupportServices =
            GoogleSupportServices(activity)
        if (!googleSupportServices.isGooglePlayServicesAvailable) {
            // Initialize SettingController.
            if (pref.isRunFirstTime.equals("false")) {
                huaweiInitService()
                connectHuaweiSignIn()
            }

        }
        pref.disableAllTabs = false
        return mainView;

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView(isShowBottomBar: Boolean) {


        tabLayout = mainView.findViewById<TabLayout>(R.id.tabLayoutMain)
        viewPager = mainView.findViewById<ViewPager>(R.id.tabLayoutViewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Dashboard"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Calendar"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Cards"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Records"))


        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout!!.setBackgroundResource(R.color.white)
        tabLayout!!.elevation = 20F

        tabLayout!!.setTabTextColors(
            resources.getColor(R.color.grey),
            resources.getColor(R.color.add_a_goal_font_color)
        );


        print(isShowBottomBar)

        val adapter = context?.let {
            activity?.supportFragmentManager?.let { it1 ->
                TabLayoutPageAdapter(
                    it,
                    it1,
                    tabLayout!!.tabCount,
                    isShowBottomBar
                )
            }
        }
        viewPager!!.adapter = adapter

//        tabLayout!!.setupWithViewPager(viewPager);

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager!!.offscreenPageLimit = 0;

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val b = pref.disableAllTabs;
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

    }


    fun huaweiInitService() {
        val fitnessOptions: HiHealthOptions = HiHealthOptions.builder().build();
        val signInHuaweiId: AuthHuaweiId =
            HuaweiIdAuthManager.getExtendedAuthResult(fitnessOptions);
        mSettingController = HuaweiHiHealth.getSettingController(requireContext(), signInHuaweiId);
    }

    fun connectHuaweiSignIn() {
        /**
         * Sign-in and authorization method. The authorization screen will display if the current account has not granted authorization.
         */
        Log.i(TAG, "begin sign in");
        val scopeList: ArrayList<com.huawei.hms.support.api.entity.auth.Scope> =
            ArrayList<com.huawei.hms.support.api.entity.auth.Scope>();

        val huaweiScopes: HuaweiScopes = HuaweiScopes();

        // Add scopes to apply for. The following only shows an example. You need to add scopes according to your specific needs.
        scopeList.add(huaweiScopes.healthKitBoth) // View and save step counts in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.healthKitHeartRateBoth) // View and save height and weight in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.healthKitWeightBoth) // View and save the heart rate data in HUAWEI Health Kit.
        scopeList.add(huaweiScopes.healthKitStepRealTime)

        // Configure authorization parameters.
        val authParamsHelper: HuaweiIdAuthParamsHelper = HuaweiIdAuthParamsHelper(
            HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM
        );
        val authParams: HuaweiIdAuthParams = authParamsHelper.setIdToken()
            .setAccessToken()
            .setScopeList(scopeList)
            .createParams();

        // Initialize the HuaweiIdAuthService object.
        val huaweiAuthService: HuaweiIdAuthService =
            HuaweiIdAuthManager.getService(context, authParams);

        // Silent sign-in. If authorization has been granted by the current account, the authorization screen will not display. This is an asynchronous method.
        val authHuaweiIdTask: Task<AuthHuaweiId> = huaweiAuthService.silentSignIn();

        authHuaweiIdTask.addOnSuccessListener {
            // The silent sign-in is successful.
            Log.i(TAG, "silentSignIn success");
//                loadMainServiceForGoals();
            //=======Sending and afterthat Getting Latest Steps===========================
//                GetSteps task = new GetSteps();
//                task.execute();

            requireActivity().startActivityForResult(
                huaweiAuthService.signInIntent,
                REQUEST_LOGIN_CODE
            );
        }

        authHuaweiIdTask.addOnFailureListener {
            // Call the sign-in API using the getSignInIntent() method.
            val signInIntent: Intent = huaweiAuthService.signInIntent;

            // Display the authorization screen by using the startActivityForResult() method of the activity.
            // You can change HihealthKitMainActivity to the actual activity.
            requireActivity().startActivityForResult(signInIntent, REQUEST_SIGN_IN_LOGIN);
        }


    }


    override fun onResume() {
        super.onResume()

        val googleSupportServices: GoogleSupportServices =
            GoogleSupportServices(activity);

        getPopupData();

        if ((Constants.type == Constants.Type.AYUBO) || (Constants.type == Constants.Type.LIFEPLUS) || (Constants.type == Constants.Type.HEMAS)) {

            if (googleSupportServices.isGooglePlayServicesAvailable) {
                //START In Resume, every time checking that IS GOOGLEFIT CONNECTED
                if (pref.isGoogleFitEnabled.equals("false")) {
                    val bb: Boolean = fitInstalled();
                    if (bb) {

                        try {

                            googleFitAlertDialog!!.dismiss()

                        } catch (e: Exception) {
                            e.printStackTrace();
                        }


                        createGoogleClient();
                    } else {

                        if (!isOpenedDialog) {
                            openGoogleFitInPlayStore();
                            isOpenedDialog = true;
                        }


                    }
                }
                //END In Resume, every time checking that IS GOOGLEFIT CONNECTED


                if (pref.isHomeFirsttime.equals("false")) {
                    // System.out.println("==========isHomeFirsttime==========false");
                    val deviceName: String? = pref.deviceData["stepdevice"];

                    if (deviceName != null) {
                        //    System.out.println("========deviceName=========not null");
                        if (deviceName == "activity_AYUBO") {


                            //      System.out.println("========deviceName=========activity_AYUBO");
                            if (pref.isGoogleFitEnabled.equals("true")) {
                                //  System.out.println("========isGoogleFitEnabled=========true");

//                                GoogleClientForDiscover(context, activity, false);


                            } else {
                                //   System.out.println("========isGoogleFitEnabled=========true");

                            }
                        } else {
                            //  System.out.println("========deviceName=========null");
                        }
                    } else {
                        //System.out.println("==========isHomeFirsttime=======not===false"+pref.isHomeFirsttime());
                    }

                }

            }


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val googleSupportServices: GoogleSupportServices =
            GoogleSupportServices(activity);

        if (googleSupportServices.isGooglePlayServicesAvailable) {
            if (requestCode == REQUEST_OAUTH) {
                authInProgress = false;
                if (resultCode == Activity.RESULT_OK) {
                    if (!mApiClient.isConnecting && !mApiClient.isConnected) {
                        mApiClient.connect();
                        Log.e("GoogleFit", "connected-------------");
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.e("GoogleFit", "RESULT_CANCELED");
                }
            } else {
                Log.e("GoogleFit", "requestCode NOT request_oauth");
            }

        } else {

            pref.setIsRunFirstTime("true");
            if (requestCode == REQUEST_LOGIN_CODE) {
                val huaweiIdAuthManager: Task<AuthHuaweiId> =
                    HuaweiIdAuthManager.parseAuthResultFromIntent(data);
                if (huaweiIdAuthManager.isSuccessful()) {
                    val huaweiResult: HuaweiResult = HuaweiResult();
                    huaweiResult.authResults =
                        HuaweiIdAuthAPIManager.HuaweiIdAuthAPIService.parseHuaweiIdFromIntent(data);
                    if (!isLinkedHuawei) {
                        checkAndAuthorizeHealthApp();
                    }

                } else if (huaweiIdAuthManager.isCanceled()) {
                    Toast.makeText(
                        getContext(),
                        "Authorization was not granted",
                        Toast.LENGTH_SHORT
                    ).show();
                }
            } else if (requestCode == REQUEST_HEALTH_AUTH) {
                Log.i(TAG, "REQUEST_WAS_SUCCESS");
                val task: GetSteps = GetSteps();
                task.execute();
            } else {
                System.out.println("else");
            }
        }

    }

    override fun onStart() {
        super.onStart()


    }

    fun getPopupData() {
        // pref=new PrefManager(getContext());
        appToken = pref.getUserToken();
        val apiService: ApiInterface = ApiClient.getNewApiClient().create(ApiInterface::class.java);
        val call: Call<PopupMainResponse> = apiService.getPopupData(appToken);

        call.enqueue(object : Callback<PopupMainResponse> {
            override fun onResponse(
                call: Call<PopupMainResponse>,
                response: Response<PopupMainResponse>
            ) {

                val status: Boolean = response.isSuccessful;
                if (status) {
                    if (response.body() != null) {

                        val dataObj: PopupMainData? = response.body()?.data
                        val reslt: Int? = response.body()?.result;

                        if (dataObj != null) {
                            context?.let {
//                                showAlert_Add(it, dataObj)
                                ShowCommonAlertPopUp().showAlertPopUp(
                                    it,
                                    requireActivity(),
                                    dataObj
                                )

                            }
                        }


                    }
                }
            }

            override fun onFailure(call: Call<PopupMainResponse>, t: Throwable) {
            }


        });
    }


    fun setReadPopup(id: String) {

        appToken = pref.userToken;
        val apiService: ApiInterface = ApiClient.getNewApiClient().create(ApiInterface::class.java);
        val call: Call<Any>? = apiService.setReadPopup(appToken, id);

        if (call != null) {
            call.enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    val status: Boolean = response.isSuccessful;
                    if (status) {
                        if (response.body() != null) {

                        }
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    System.out.println("========t======" + t);
                }
            });
        }
    }

    fun deleteAPost(postobj: Post) {

        val apiService: ApiInterface = ApiClient.getNewApiClient().create(ApiInterface::class.java);


        val call: Call<LikeMainResponse> = apiService.DeleteAPost(
            AppConfig.APP_BRANDING_ID,
            appToken,
            postobj.postId.toString()
        );

        call.enqueue(object : Callback<LikeMainResponse> {
            override fun onResponse(
                call: Call<LikeMainResponse>,
                response: Response<LikeMainResponse>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        if (response.body()?.result == 401) {
                            val intent: Intent = Intent(context, LoginActivity_First::class.java);
                            startActivity(intent);
                        }
                        if (response.body()?.getResult() == 0) {
                            // Code here .............
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LikeMainResponse>, t: Throwable) {
            }


        });


    }

    fun checkAndAuthorizeHealthApp() {
        Log.i(TAG, "Begin to checkOrAuthorizeHealthApp");
        mSettingController = HuaweiHiHealth.getSettingController(
            requireContext(),
            HuaweiIdAuthManager.getExtendedAuthResult(
                HiHealthOptions
                    .builder()
                    .build()
            )
        );

        val mSettingControllerQueryTask: Task<Boolean> =
            mSettingController.getHealthAppAuthorization();

        mSettingControllerQueryTask.addOnSuccessListener {
            if (it) {
                Log.i(TAG, "queryHealthAuthorization get result is authorized");
                val uri: Uri = Uri.parse(HEALTH_SCHEME);
                val intent: Intent = Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(
                        Objects.requireNonNull(
                            Objects.requireNonNull(
                                requireContext()
                            )
                        ).packageManager
                    ) != null
                ) {
                    requireActivity().startActivityForResult(intent, REQUEST_HEALTH_AUTH);

                }
            } else {
                Log.i(TAG, "queryHealthAuthorization get result is unauthorized");
                Toast.makeText(
                    getContext(),
                    "Please link the app with Huawei Health",
                    Toast.LENGTH_LONG
                ).show();
                val uri: Uri = Uri.parse(HEALTH_SCHEME);
                val intent: Intent = Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(
                        Objects.requireNonNull(
                            Objects.requireNonNull(
                                requireContext()
                            )
                        ).packageManager
                    ) != null
                ) {
                    requireActivity().startActivityForResult(intent, REQUEST_HEALTH_AUTH);

                }
            }
        }



        mSettingControllerQueryTask.addOnFailureListener {

            if (it != null) {
                Log.i(TAG, "queryHealthAuthorization has exception");
                Toast.makeText(
                    getContext(),
                    "Health Authorization has exception",
                    Toast.LENGTH_SHORT
                ).show();
            }

        }


    }


    private fun fitInstalled(): Boolean {
        try {
            activity?.packageManager?.getPackageInfo(PACKAGE_NAME, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (e: PackageManager.NameNotFoundException) {
            return false;
        }
    }

    private fun init(view: View) {

//        width = getScreenWidth()

//        view.img_search_in_videocall.setOnClickListener {
//            DiscoverSearchActivity.startActivity(context)
//        }

        loggerFB = AppEventsLogger.newLogger(context)

//        view.go_to_home.setOnClickListener {
//            val intent = Intent(context, NewHomeWithSideMenuActivity::class.java);
//            startActivity(intent);
//        }

//        view.btn_me.setOnClickListener {
////            if (Constants.type == Constants.Type.LIFEPLUS) {
////                val intent = Intent(context, NotificationsListActivity::class.java);
////                startActivity(intent);
////            } else {
////                val intent = Intent(context, ProfileActivity::class.java);
//            val intent = Intent(context, ProfileNew::class.java);
//            startActivity(intent);
////            }
//        };

    }

    private fun createGoogleClient() {

        mApiClient = activity?.let {
            GoogleApiClient.Builder(it)
                .addApi(ActivityRecognition.API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.SENSORS_API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ)
                .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        }!!;

        mApiClient.connect();
    }

    override fun onConnected(p0: Bundle?) {
        pref.setGoogleFitEnabled("true");
        //=======Sending and afterthat Getting Latest Steps===========================
//        val task: GetSteps = GetSteps();
//        task.execute();

//        GoogleClientForDiscover(context, activity, true);

        try {
            googleFitAlertDialog!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_OAUTH);

            } catch (e: IntentSender.SendIntentException) {

                e.printStackTrace();
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
    }

    private fun openGoogleFitInPlayStore() {
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) };
        val inflater: LayoutInflater =
            context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val layoutView: View = inflater.inflate(R.layout.alert_install_googlefit, null, false);


        if (Constants.type == Constants.Type.AYUBO) {
            layoutView.findViewById<TextView>(R.id.lbl_app_version)
                .setText("AyuboLife needs to fetch your step count from GoogleFit and other Google services.")
        }

        if (builder != null) {
            builder.setView(layoutView)
            builder.setPositiveButton(
                "YES",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    googleFitAlertDialog!!.dismiss();
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness")
                        )
                    );


                });
            googleFitAlertDialog = builder.create();
            googleFitAlertDialog!!.show();
        };

    }

    internal inner class GetSteps : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            var ste: String = "0";
            jsonStepsForDailyArrayForNewSave = ArrayList<StepObj>();
            jsonStepsForWeekArrayForNewSave = ArrayList<StepObj>();

            val googleSupportServices: GoogleSupportServices =
                GoogleSupportServices(
                    getActivity()
                );

            if (googleSupportServices.isGooglePlayServicesAvailable()) {
                val readRequest: DataReadRequest = queryFitnessData(0);
                val dataReadResult: DataReadResult =
                    Fitness.HistoryApi.readData(mApiClient, readRequest).await(1, TimeUnit.MINUTES);
                ste = printData(dataReadResult);
                sendLatestStepsToServer(ste);
            } else {
                val hiHealthOptions: HiHealthOptions = HiHealthOptions.builder()
                    .addDataType(
                        com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT,
                        HiHealthOptions.ACCESS_READ
                    )
                    .addDataType(
                        com.huawei.hms.hihealth.data.DataType.DT_INSTANTANEOUS_HEIGHT,
                        HiHealthOptions.ACCESS_WRITE
                    )
                    .addDataType(
                        com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA,
                        HiHealthOptions.ACCESS_READ
                    )
                    .addDataType(
                        com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA,
                        HiHealthOptions.ACCESS_WRITE
                    )
                    .build();
                val signInHuaweiId: AuthHuaweiId =
                    HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);

                val dataController: DataController? =
                    activity?.let { HuaweiHiHealth.getDataController(it, signInHuaweiId) };


                val todaySummationTask: Task<SampleSet> =
                    dataController?.readTodaySummation(com.huawei.hms.hihealth.data.DataType.DT_CONTINUOUS_STEPS_DELTA) as Task<SampleSet>;
                todaySummationTask.addOnSuccessListener {
                    isLinkedHuawei = true;
                    if (it != null) {
                        val steps: String = showSampleSet(it);
                        sendLatestStepsToServer(steps);
                    }
                }
                todaySummationTask.addOnFailureListener {
                    isLinkedHuawei = true;
                    Toast.makeText(getContext(), it.message, Toast.LENGTH_SHORT).show();
                }
            }

            return null;
        }


    }

    private fun showSampleSet(sampleSet: SampleSet): String {
        var stepCount: String = "0";
        val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (dp in sampleSet.getSamplePoints()) {
            for (field in dp.getDataType().getFields()) {
                stepCount = dp.getFieldValue(field).toString();
            }

            jsonStepsForDailyArrayForNewSave.add(
                createGFitStepsDataArrayForNewSave(
                    stepCount,
                    dp.getStartTime(TimeUnit.MILLISECONDS),
                    dp.getEndTime(TimeUnit.MILLISECONDS)
                )
            );

        }

        return stepCount;
    }

    fun sendLatestStepsToServer(stps: String) {
        todayStepsToSendToServer = stps;
        var task: sendAyuboStepsToServer_ForTenDays0 = sendAyuboStepsToServer_ForTenDays0();
        task.execute();
    }

    internal inner class sendAyuboStepsToServer_ForTenDays0 : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            sendAyuboSteps_ForTenDays0();
            return null;
        }
    }

    private fun sendAyuboSteps_ForTenDays0() {

        val httpClient: HttpClient = DefaultHttpClient();
        val httpPost: HttpPost = HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        val nameValuePair: ArrayList<NameValuePair> = ArrayList<NameValuePair>();

        val cal: Calendar = Calendar.getInstance();
        cal.setTime(Date());
        val database_date_format_sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd");

        val dateFromDB_forADay0: String = database_date_format_sdf.format(cal.getTime());


//        val task: newServiceAPIFor0DaysGFIT = newServiceAPIFor0DaysGFIT();
//        task.execute(jsonStepsForDailyArrayForNewSave);

        val value: Float = todayStepsToSendToServer.toFloat();
        val sMets: String;
        val sCals: String;
        val sDis: String;
        val metsVal: Double = (value / 130) * 4.5;
        sMets = String.format("%.0f", metsVal);
        val calories: Double = (metsVal * 3.5 * 70) / 200;
        sCals = String.format("%.0f", calories);
        val num = 100000;
        val distance: Double = (value * 78) / num.toDouble();
        sDis = String.format("%.2f", distance);
        val versionName: String = BuildConfig.VERSION_NAME;
        val device_modal: String = Build.MODEL;

        val jsonStr: String =
            "{" +
                    "\"userid\": \"" + userid_ExistingUser + "\"," +
                    "\"activity\": \"" + "activity_AYUBO" + "\"," +
                    "\"energy\": \"" + sMets + "\"," +
                    "\"steps\": \"" + todayStepsToSendToServer + "\"," +
                    "\"calorie\": \"" + sCals + "\", " +
                    "\"duration\": \"" + "0" + "\"," +
                    "\"distance\": \"" + sDis + "\"," +
                    "\"date\": \"" + dateFromDB_forADay0 + "\"," +
                    "\"walk_count\": \"" + todayStepsToSendToServer + "\"," +
                    "\"run_count\": \"" + "15" + "\"," +
                    "\"version\": \"" + versionName + "\"," +
                    "\"osType\": \"" + "android" + "\"," +
                    "\"device_modal\": \"" + device_modal + "\"" +
                    "}";

        nameValuePair.add(BasicNameValuePair("method", "addDailyActivitySummary"));
        nameValuePair.add(BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(BasicNameValuePair("rest_data", jsonStr));


        //  System.out.println("....Today......addDailyActivitySummary....." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(UrlEncodedFormEntity(nameValuePair));

        } catch (e: UnsupportedEncodingException) {
            // log exception
            e.printStackTrace();
        }


        var response: HttpResponse? = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (e: IOException) {
            e.printStackTrace();
        }
//        val sc: String =  response.getStatusLine().getStatusCode();
        val sc: String = response?.statusLine?.statusCode.toString();


        if (sc.equals("200")) {

            var responseString: String? = null;
            try {
                if (response != null) {
                    responseString = EntityUtils.toString(response.getEntity())
                };
                System.out.println("=====responseString====addDailyActivitySummary===" + responseString);
            } catch (e: IOException) {
                e.printStackTrace();
            }

        }


    }

    fun queryFitnessData(days: Int): DataReadRequest {

        val secondDay: Calendar = Calendar.getInstance();
        secondDay.add(Calendar.DATE, -days);

        val format1: SimpleDateFormat = SimpleDateFormat("dd-M-yyyy");
        val dateString: String = format1.format(secondDay.getTime());

        var endTime: Long = 0L;
        var midNight: Long = 0L;

        val sdf: SimpleDateFormat = SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        val startDateString: String = dateString + " 00:00:01";
        val endDateString: String = dateString + " 23:59:59";

        var calendarStart: Calendar? = null;
        var calendarEnd: Calendar? = null;
        try {
            //formatting the dateString to convert it into a Date
            val sdate: Date = sdf.parse(startDateString);
            val edate: Date = sdf.parse(endDateString);

            calendarStart = Calendar.getInstance();
            calendarEnd = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendarStart.setTime(sdate);
            //  System.out.println("Given Time in milliseconds Staart: "+calendars.getTimeInMillis());
            calendarEnd.setTime(edate);
            //  System.out.println("Given Time in milliseconds End: "+calendare.getTimeInMillis());

        } catch (e: Exception) {
            e.printStackTrace();
        }
        if (calendarStart != null) {
            midNight = calendarStart.getTimeInMillis()
        };
        if (calendarEnd != null) {
            endTime = calendarEnd.getTimeInMillis()
        };
        //===================================================

        val ESTIMATED_STEP_DELTAS: DataSource = DataSource.Builder()
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setType(DataSource.TYPE_DERIVED)
            .setStreamName("estimated_steps")
            .setAppPackageName("com.google.android.gms")
            .build();

        val readRequest: DataReadRequest = DataReadRequest.Builder()
            .aggregate(ESTIMATED_STEP_DELTAS, DataType.TYPE_STEP_COUNT_DELTA)
            .bucketByTime(24, TimeUnit.HOURS)
            .setTimeRange(midNight, endTime, TimeUnit.MILLISECONDS)
            .build();

        return readRequest;
    }

    fun printData(dataReadResult: DataReadResult): String {
        var step: String = "0";
        if (dataReadResult.getBuckets().size > 0) {
            Log.i(
                TAG, "Number of returned buckets of DataSets is: "
                        + dataReadResult.getBuckets().size
            );

            for (bucket in dataReadResult.getBuckets()) {
                val dataSets: List<DataSet> = bucket.getDataSets();
                for (dataSet in dataSets) {
                    step = dumpDataSet(dataSet);
                }
            }


        } else if (dataReadResult.getDataSets().size > 0) {
            Log.i(
                TAG, "Number of returned DataSets is: "
                        + dataReadResult.getDataSets().size
            );

            for (dataSet in dataReadResult.getDataSets()) {
                step = dumpDataSet(dataSet);
            }

        }
        return step;
        // [END parse_read_data_result]
    }

    private fun dumpDataSet(dataSet: DataSet): String {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        val dateFormat: DateFormat = DateFormat.getTimeInstance();
        var stepCount: String = "0";
        if (dataSet.isEmpty()) {

            stepCount = "0";
        } else {

            for (dp in dataSet.getDataPoints()) {
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());

                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

                for (field in dp.getDataType().getFields()) {
                    Log.i(
                        TAG, "\tField: " + field.getName() +
                                "GFit Steps Value: " + dp.getValue(field)
                    );
                    stepCount = "0";
                    stepCount = dp.getValue(field).toString();

                    System.out.println("==============GFit Steps Value:==================" + stepCount);


                }


                jsonStepsForDailyArrayForNewSave.add(
                    createGFitStepsDataArrayForNewSave(
                        stepCount,
                        dp.getStartTime(TimeUnit.MILLISECONDS),
                        dp.getEndTime(TimeUnit.MILLISECONDS)
                    )
                );

            }
        }

        return stepCount;
    }

    fun createGFitStepsDataArrayForNewSave(steps: String, startTime: Long, endTime: Long): StepObj {
        val stepObj: StepObj = StepObj();
        stepObj.setStepCount(Integer.parseInt(steps));
        stepObj.setStartDateTime(startTime);
        stepObj.setEndDateTime(endTime);
        return stepObj;
    }

    fun showAlert_Add(c: Context, dataObj: PopupMainData) {
        val dialogView: android.app.AlertDialog;

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(c);
        val inflater: LayoutInflater =
            c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val layoutView: View = inflater.inflate(R.layout.alert_question_views, null, false);

        val display: Display = requireActivity().windowManager.getDefaultDisplay();
        val width: Int = display.getWidth();
        val ratio: Double = ((width)) / 300.0;
        val height: Int = (ratio * 50).toInt();

        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width, 300);
        layoutView.setLayoutParams(layoutParams);

        builder.setView(layoutView);
        dialogView = builder.create();
        dialogView.setCancelable(false);

        val obj: PopupMainData = dataObj;
        val imageURL: String = obj.imageUrl;
        val headerText: String = obj.header;
        val headerBody: String = obj.body;
        val buttonAction: String = obj.button.action;
        val buttonMeta: String = obj.button.meta;
        val buttonText: String = obj.button.text;

        val requestOptionsSamll: RequestOptions =
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        val img_bg_image_bg: ImageView = layoutView.findViewById(R.id.img_bg_image_bg);

        Glide.with(requireContext()).load(imageURL)
            .apply(requestOptionsSamll)
            .into(img_bg_image_bg);

        val btn_view_report: Button = layoutView.findViewById(R.id.btn_view_report_action);
        btn_view_report.setText(buttonText);
        val lay_close_pop: LinearLayout = layoutView.findViewById(R.id.lay_close_pop);
        val txt_heading: TextView = layoutView.findViewById(R.id.txt_heading);
        val txt_sub_heading: TextView = layoutView.findViewById(R.id.txt_sub_heading);
        txt_heading.setText(headerText);
        txt_sub_heading.setText(headerBody);

        val btn_close_pop: ImageButton = layoutView.findViewById(R.id.btn_close_pop);

        btn_close_pop.setTag(obj);
        btn_close_pop.setOnClickListener {

            dialogView.cancel();
            val obj: PopupMainData = it.getTag() as PopupMainData;
            setReadPopup(obj.getId().toString());

        }


        lay_close_pop.setTag(obj);
        lay_close_pop.setOnClickListener {

            dialogView.cancel();
            val obj: PopupMainData = it.getTag() as PopupMainData;
            setReadPopup(obj.getId().toString());

        }

        btn_view_report.setTag(obj);
        btn_view_report.setOnClickListener {


            dialogView.cancel();
            val obj: PopupMainData = it.getTag() as (PopupMainData);
            setReadPopup(obj.getId().toString());
            processAction(obj.getButton().getAction(), obj.getButton().getMeta());


        }

        dialogView.show();
    }

    fun processAction(action: String, meta: String) {

        if (action.equals("openbadgenative")) {
            onOpenBadgersClick(meta);
        }
        if (action.equals("programtimeline")) {
            onProgramNewDesignClick(meta);
        }
        if (action.equals("program")) {
            onProgramPostClick(meta);
        }
        if (action.equals("challenge")) {
            onMapChallangeClick(meta);
        }
        if (action.equals("chat")) {
            onAskClick(meta);
        }
        if (action.equals("leaderboard")) {
            onLeaderboardClick(meta);
        }
        if (action.equals("commonview")) {
            onCommonViewClick(meta);
        }
        if (action.equals("web")) {
            onBowserClick(meta);
        }
        if (action.equals("help")) {
            onHelpClick(meta);
        }
        if (action.equals("reports")) {
            onReportsClick(meta);
        }
        if (action.equals("goal")) {
            onGoalClick(meta);
        }
        if (action.equals("videocall")) {
            onVideoCallClick(meta);
        }
        if (action.equals("channeling")) {
            onButtonChannelingClick(meta);
        }
        if (action.equals("janashakthiwelcome")) {
            onJanashakthiWelcomeClick(meta);
        }
        if (action.equals("janashakthireports")) {
            onJanashakthiReportsClick(meta);
        }
        if (action.equals("dynamicquestion")) {
            onDyanamicQuestionClick(meta);
        }
        if (action.equals("post")) {
            onPostClick(meta);
        }
        if (action.equals("native_post")) {
            onNativePostClick(meta);
        }
        if (action.equals("native_post_json")) {
            onJSONNativePostClick(meta);
        }
        if (action.equals("paynow")) {
            onPayNowClick(meta);
        }
        if (action.equals("discover")) {

//            val intent: Intent = Intent(context, LifePlusProgramActivity::class.java);
//            val intent: Intent = Intent(context, NewDiscoverActivity::class.java);
            val intent = SetDiscoverPage().getDiscoverIntent(requireContext())
            intent.putExtra("isFromSearchResults", false);
            startActivity(intent);
        }


    }

    fun onAskClick(meta: String) {
        if (meta == null) {
            val intent: Intent = Intent(context, AskActivity::class.java);
            startActivity(intent);
        } else if (meta.equals("")) {
            val intent: Intent = Intent(context, AskActivity::class.java);
            startActivity(intent);
        } else {
            val intent: Intent = Intent(context, AyuboChatActivity::class.java);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        }
    }

    fun onMapChallangeClick(meta: String) {
        val intent: Intent = Intent(context, MapChallangeKActivity::class.java);
        intent.putExtra(EXTRA_CHALLANGE_ID, meta);
        startActivity(intent);
    }

    fun startDoctorsActivity(doctorID: String) {
        val parameters: DocSearchParameters;
        parameters = DocSearchParameters();
        val pref: PrefManager = PrefManager(context);
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        val intent: Intent = Intent(context, SearchActivity::class.java);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }

    fun onButtonChannelingClick(meta: String) {
        if (meta != null) {
            startDoctorsActivity(meta);
        } else {
            val intent: Intent = Intent(context, DashboardActivity::class.java);
            startActivity(intent);
        }
    }

    fun onVideoCallClick(meta: String) {
        if (meta != null) {
            val activity: String = "my_doctor";
            val intent: Intent = Intent(context, MyDoctorLocations_Activity::class.java);
            intent.putExtra("doctor_id", meta);
            intent.putExtra("activityName", activity);
            startActivity(intent);
        } else {
            val intent: Intent = Intent(context, MyDoctor_Activity::class.java);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        }


    }

    fun onProgramNewDesignClick(meta: String) {
        if (meta != null) {
            val intent: Intent = Intent(context, ProgramActivity::class.java);
            intent.putExtra("meta", meta);
            startActivity(intent);
        }
    }

    fun onOpenBadgersClick(meta: String) {
        if (meta != null) {
            val intent: Intent = Intent(context, Badges_Activity::class.java);
            startActivity(intent);
        }
    }

    fun onProgramPostClick(meta: String) {
        if (meta != null) {
            val intent: Intent = Intent(getContext(), SingleTimeline_Activity::class.java);
            intent.putExtra("related_by_id", meta);
            intent.putExtra("type", "program");
            startActivity(intent);
        }
    }

    fun onGoalClick(meta: String) {
        val prefManager: PrefManager = PrefManager(context);
        val status: String? = prefManager.getMyGoalData().get("my_goal_status");

        if (status.equals("Pending")) {
            val intent: Intent = Intent(getContext(), AchivedGoal_Activity::class.java);
            startActivity(intent);
        }
        if (status.equals("Pick")) {


            val intent: Intent = Intent(getContext(), PickAGoal_Activity::class.java);
            startActivity(intent);
        }
        if (status.equals("Completed")) {
            val serviceObj: HomePage_Utility = HomePage_Utility(context);
            serviceObj.showAlert_Deleted(
                context,
                "This goal has been achieved for the day. Please pick another goal tomorrow"
            );
        }

    }

    fun onPayNowClick(meta: String) {
        val intent: Intent = Intent(context, PaymentActivity::class.java);
        intent.putExtra("paymentmeta", meta);
        startActivity(intent);
    }

    fun onJSONNativePostClick(meta: String) {
        val intent: Intent = Intent(context, NativePostJSONActivity::class.java);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    fun onNativePostClick(meta: String) {
        val intent: Intent = Intent(context, NativePostActivity::class.java);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    fun onPostClick(meta: String) {
        val intent: Intent = Intent(context, OpenPostActivity::class.java);
        intent.putExtra("postID", meta);
        startActivity(intent);
    }

    fun onJanashakthiWelcomeClick(meta: String) {
        val pref: PrefManager = PrefManager(context);
        pref.setRelateID(meta);
        pref.setIsJanashakthiWelcomee(true);
        val intent: Intent = Intent(context, JanashakthiWelcomeActivity::class.java);
        startActivity(intent);
    }

    fun onDyanamicQuestionClick(meta: String) {
        val pref: PrefManager = PrefManager(getContext());
        pref.setIsJanashakthiDyanamic(true);
        pref.setRelateID(meta);
        val intent: Intent = Intent(context, IntroActivity::class.java);
        startActivity(intent);
    }

    fun onJanashakthiReportsClick(meta: String) {
        val intent: Intent = Intent(context, MedicalUpdateActivity::class.java);
        startActivity(intent);
    }

    fun onReportsClick(meta: String) {
        val intent: Intent = Intent(context, ReportDetailsActivity::class.java);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    fun onLifePointsClick(activityName: String, meta: String) {
        val intent: Intent = Intent(context, LifePointActivity::class.java);
        startActivity(intent);
    }

    fun onHelpClick(meta: String) {
        val intent: Intent = Intent(context, HelpFeedbackActivity::class.java);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    fun onLeaderboardClick(meta: String) {
        if (meta.length > 0) {
            val intent: Intent = Intent(context, NewLeaderBoardActivity::class.java);
            startActivity(intent);
        }
    }

    fun onBowserClick(meta: String) {
        val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    fun onCommonViewClick(meta: String) {
        val intent: Intent = Intent(context, CommonWebViewActivity::class.java);
        intent.putExtra("URL", meta);
        startActivity(intent);
    }


}