package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.*
import android.provider.CalendarContract
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.BuildConfig
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.activity.UploadActivity
import com.ayubo.life.ayubolife.channeling.activity.VisitDoctorActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.goals_extention.StepHistory_Activity
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity
import com.ayubo.life.ayubolife.health.OMCommon
import com.ayubo.life.ayubolife.health.OMMainPage
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.lifeplus.*
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.new_payment.activity.NewPaymentMainActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.payment.EXTRA_TREASURE_KEY
import com.ayubo.life.ayubolife.payment.activity.PaymentActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentPinSubmitActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentSummaryViewActivity
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.reports.activity.TestReportsActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.ActionMetaForFragment
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.WalkWinMainActivity
import com.bumptech.glide.Glide
import com.facebook.appevents.AppEventsLogger
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.gson.*
import kotlinx.android.synthetic.main.feed_back_popup.*
import kotlinx.android.synthetic.main.fragment_new_profile_dashboard.*
import kotlinx.android.synthetic.main.fragment_new_profile_dashboard.view.*
import kotlinx.android.synthetic.main.new_app_main_bar_dis_to_dash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.TimeZone
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [NewProfileDashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewProfileDashboard : Fragment(), WeeklyRelatedCardTrackerAdapter.OnItemClickListener,
    SubscriptionCardAdapter.OnSubscriptionItemClickListener,
    NotificationCardAdapter.OnNotificationItemClickListener,
    AnnouncementCardAdapter.OnAnnouncementItemClickListener {
    private lateinit var mainView: View
    lateinit var pref: PrefManager
    lateinit var appToken: String
    lateinit var groupId: String
    var loggedInUserId: String = ""
    lateinit var hydrationTrackerDialog: ProgressDialog
    lateinit var newDashboardLoading: ProgressAyubo
    lateinit var dashboardIntroText: TextView
    lateinit var dashboardMainTopic: TextView
    lateinit var dataBadgesLinearLayout: LinearLayout
    lateinit var dataBadgesMainLinearLayout: LinearLayout
    lateinit var topic: RelativeLayout
    lateinit var lifePointsLinearLayout: LinearLayout
    lateinit var subscriptionCardIndicatorsLayout: LinearLayout
    lateinit var myBadgesLink: TextView
    lateinit var subscriptionRecyclerview: RecyclerView
    var dotsImageViews: ArrayList<ImageView> = ArrayList<ImageView>()
    private var dotsCount: Int = 0
    lateinit var tabLayout: TabLayout
    lateinit var tabLayoutViewPager: ViewPager


    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainView = inflater.inflate(R.layout.fragment_new_profile_dashboard, container, false)
        newDashboardLoading = mainView.findViewById(R.id.new_dashboard_loading) as ProgressAyubo
        myBadgesLink = mainView.findViewById(R.id.my_badges_link) as TextView
        dataBadgesMainLinearLayout =
            mainView.findViewById(R.id.data_badges_main_linear_layout) as LinearLayout
        dashboardIntroText = mainView.findViewById(R.id.dashboard_intro_text) as TextView
        dashboardMainTopic = mainView.findViewById(R.id.dashboard_main_topic) as TextView
        topic = mainView.findViewById(R.id.topic) as RelativeLayout
        lifePointsLinearLayout =
            mainView.findViewById(R.id.life_points_linear_layout) as LinearLayout
        dataBadgesLinearLayout =
            mainView.findViewById(R.id.data_badges_linear_layout) as LinearLayout
        lifePointsLinearLayout.visibility = View.GONE
        newDashboardLoading.visibility = View.VISIBLE
        hydrationTrackerDialog = ProgressDialog(this@NewProfileDashboard.context)
        subscriptionCardIndicatorsLayout =
            mainView.findViewById(R.id.subscription_card_indicators_layout) as LinearLayout

        subscriptionRecyclerview =
            mainView.findViewById(R.id.subscriptionRecyclerview) as RecyclerView

        subscriptionCardIndicatorsLayout.removeAllViews()
        pref = PrefManager(context)

        val getNewDashBoardData = pref.newDashBoardData

        if (getNewDashBoardData.equals("")) {
            if (Constants.type == Constants.Type.LIFEPLUS) {
                groupId = "wnw_dashboard"
            } else {
                groupId = "user_dashboard"
            }


        } else {
            groupId = getNewDashBoardData
        }
        appToken = pref.userToken
        loggedInUserId = pref.loginUser["uid"].toString();

        myBadgesLink.setOnClickListener {
            val intent: Intent = Intent(context, MyBadgesActivity::class.java)
            startActivity(intent)
        }



        tabLayout = activity?.findViewById<TabLayout>(R.id.tabLayoutMain)!!
        tabLayoutViewPager = activity?.findViewById<ViewPager>(R.id.tabLayoutViewPager)!!

        return mainView
    }


    fun openBottomSlider(settingItemArrayList: ArrayList<SettingItem>) {

        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.dashboard_bottom_sheet);
        bottomSheetDialog.show();


        val actionsMainLinearLayout: LinearLayout? =
            bottomSheetDialog.findViewById<LinearLayout>(R.id.actions_main_linear_layout)
        actionsMainLinearLayout?.removeAllViews()

        for (item in 0 until settingItemArrayList.size) {
            val scrollSettingItem: SettingItem = settingItemArrayList[item]

            val mainLinearLayout = LinearLayout(context)
            val subImageView = ImageView(context)
            val subTextView = TextView(context)

            val paramsForMainLinearLayout: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
            paramsForMainLinearLayout.setMargins(32, 8, 0, 32);
            mainLinearLayout.layoutParams = paramsForMainLinearLayout;


            val paramsForSubImageView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                45,
                45
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            };
            paramsForSubImageView.setMargins(16, 0, 16, 0);
            subImageView.layoutParams = paramsForSubImageView;
            Glide.with(this).load(scrollSettingItem.icon).into(subImageView)
            mainLinearLayout.addView(subImageView)

            val paramsForSubTextView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            };
            paramsForSubTextView.setMargins(16, 0, 0, 0);
            subTextView.layoutParams = paramsForSubTextView;

            val face: Typeface? =
                context?.let { ResourcesCompat.getFont(it, R.font.montserrat_medium) };
            subTextView.setTypeface(face);

//                subTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_6_px))
            subTextView.textSize = 14F
            subTextView.setTextColor(resources.getColor(R.color.color_3B3B3B))
            subTextView.text = scrollSettingItem.title
            mainLinearLayout.addView(subTextView)


            mainLinearLayout.setOnClickListener {
                bottomSheetDialog.dismiss()
                ActionMetaForFragment().processAction(
                    requireContext(),
                    scrollSettingItem.action,
                    scrollSettingItem.meta
                )
//                processAction(scrollSettingItem.action, scrollSettingItem.meta)
            }


            actionsMainLinearLayout?.addView(mainLinearLayout)

        }


//        val builder: BottomSheet.Builder? = context?.let { BottomSheet.Builder(it) }
//        if (builder != null) {
//            builder.setTitle("Title")
//                .setView(R.layout.dashboard_bottom_sheet)
//                .setFullWidth(false)
//                .show()
//
//
//            val actionsMainLinearLayout: LinearLayout =
//                builder.view.findViewById<LinearLayout>(R.id.actions_main_linear_layout)
//            actionsMainLinearLayout.removeAllViews()
//            for (item in 0 until settingItemArrayList.size) {
//                val scrollSettingItem: SettingItem = settingItemArrayList[item]
//
//                val mainLinearLayout = LinearLayout(context)
//                val subImageView = ImageView(context)
//                val subTextView = TextView(context)
//
//                val paramsForMainLinearLayout: LinearLayout.LayoutParams =
//                    LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    ).apply {
//                        gravity = Gravity.CENTER_VERTICAL
//                    }
//                paramsForMainLinearLayout.setMargins(32, 8, 0, 32);
//                mainLinearLayout.layoutParams = paramsForMainLinearLayout;
//
//
//                val paramsForSubImageView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
//                    45,
//                    45
//                ).apply {
//                    gravity = Gravity.CENTER_VERTICAL
//                };
//                paramsForSubImageView.setMargins(16, 0, 16, 0);
//                subImageView.layoutParams = paramsForSubImageView;
//                Glide.with(this).load(scrollSettingItem.icon).into(subImageView)
//                mainLinearLayout.addView(subImageView)
//
//                val paramsForSubTextView: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    gravity = Gravity.CENTER_VERTICAL
//                };
//                paramsForSubTextView.setMargins(16, 0, 0, 0);
//                subTextView.layoutParams = paramsForSubTextView;
//
//                val face: Typeface? =
//                    context?.let { ResourcesCompat.getFont(it, R.font.montserrat_medium) };
//                subTextView.setTypeface(face);
//
////                subTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_6_px))
//                subTextView.setTextSize(14F)
//                subTextView.setTextColor(getResources().getColor(R.color.color_3B3B3B))
//                subTextView.text = scrollSettingItem.title
//                mainLinearLayout.addView(subTextView)
//
//
//                mainLinearLayout.setOnClickListener {
//                    builder.dismiss()
//                    processAction(scrollSettingItem.action, scrollSettingItem.meta)
//                }
//
//
//                actionsMainLinearLayout.addView(mainLinearLayout)
//
//            }
    }


    override fun circularProgressBarClick(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }

    override fun plusButtonItemClick(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }


    override fun onItemClick(action: String, meta: String) {
        onProcessAction(action, meta);
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewProfileDashboard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewProfileDashboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var weeklyRelatedCardTrackerAdapter: WeeklyRelatedCardTrackerAdapter? = null
    private var subscriptionCardAdapter: SubscriptionCardAdapter? = null
    private var notificationCardAdapter: NotificationCardAdapter? = null
    private var announcementCardAdapter: AnnouncementCardAdapter? = null

    override fun onPause() {
        super.onPause()
        if (weeklyRelatedCardTrackerAdapter != null)
            weeklyRelatedCardTrackerAdapter!!.release()

        if (subscriptionCardAdapter != null)
            subscriptionCardAdapter!!.release()
    }

    override fun onResume() {
        super.onResume();

        life_points_linear_layout.visibility = View.GONE


        print(groupId)



        if (groupId == "user_dashboard") {
            if (pref.savedDashboardData == null || pref.savedDashboardData == "") {
                try {
                    GoogleClientForDiscover(context, activity, true);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val timeZone: TimeZone = TimeZone.getDefault();
                val timeZoneId = timeZone.id;
                Handler(Looper.getMainLooper()).postDelayed({


                    val apiService: ApiInterface =
                        ApiClient.getAzureApiClientV1ForDashBoard()
                            .create(ApiInterface::class.java);
                    val device_modal: String = Build.MODEL;


                    val version: String = "and:" + BuildConfig.VERSION_NAME;
                    val call: Call<ProfileDashboardResponseData> = apiService.getNewDashboardData(
                        AppConfig.APP_BRANDING_ID,
                        appToken,
                        groupId,
                        System.currentTimeMillis(),
                        timeZoneId,
                        device_modal,
                        version
                    );


                    call.enqueue(object : Callback<ProfileDashboardResponseData> {
                        override fun onResponse(
                            call: Call<ProfileDashboardResponseData>,
                            response: Response<ProfileDashboardResponseData>
                        ) {
                            newDashboardLoading.visibility = View.GONE;
                            if (response.isSuccessful) {
                                println(response)
                                val dashboardMainData: JsonObject =
                                    Gson().toJsonTree(response.body()!!.data).asJsonObject;

                                val createDashboardLogObj =
                                    CreateDashboardLogObj(loggedInUserId, dashboardMainData)

                                if (dashboardMainData.get("user_id").asString != loggedInUserId) {


                                    val createDashboardLogCall: Call<ProfileDashboardResponseData> =
                                        apiService.createDashboardLog(
                                            AppConfig.APP_BRANDING_ID,
                                            appToken,
                                            createDashboardLogObj
                                        )

                                    createDashboardLogCall.enqueue(object :
                                        Callback<ProfileDashboardResponseData> {
                                        override fun onResponse(
                                            call: Call<ProfileDashboardResponseData>,
                                            response: Response<ProfileDashboardResponseData>
                                        ) {
                                            print(response)
                                        }

                                        override fun onFailure(
                                            call: Call<ProfileDashboardResponseData>,
                                            t: Throwable
                                        ) {
                                            t.printStackTrace()
                                        }
                                    })
                                }
                                print(groupId)

                                pref.saveDashboardData(dashboardMainData)
                                setDashboardAllData(dashboardMainData)


                            } else {
                                Toast.makeText(
                                    context,
                                    "There is an issue when loading data, Please contact admin.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<ProfileDashboardResponseData>,
                            t: Throwable
                        ) {
                            newDashboardLoading.visibility = View.GONE;
                        }

                    });


                }, 4000)
            } else {

                print(groupId)

                try {
                    val jsonParser: JsonParser = JsonParser();
                    val gsonObject: JsonObject =
                        jsonParser.parse(pref.savedDashboardData) as (JsonObject);


                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        syncing_data_main.setBackgroundResource(R.drawable.life_plus_gradient_rectangle_corners)

                    } else {
                        syncing_data_main.setBackgroundResource(R.drawable.ayubo_life_gradient_rectangle_corners)
                    }


                    syncing_data_main.visibility = View.VISIBLE

                    if (gsonObject.get("user_id").asString == loggedInUserId) {
                        val task = updateDashboardBackgroundData();
                        task.execute();
                    }




                    setDashboardAllData(gsonObject)
                    newDashboardLoading.visibility = View.GONE;

                } catch (err: java.lang.Exception) {
                    Log.d("Error", err.toString());
                }


            }
        } else {

            if (pref.savedWnwDashboardData == null || pref.savedWnwDashboardData == "") {
                try {
                    GoogleClientForDiscover(context, activity, true);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val timeZone: TimeZone = TimeZone.getDefault();
                val timeZoneId = timeZone.id;
                Handler(Looper.getMainLooper()).postDelayed({


                    val apiService: ApiInterface =
                        ApiClient.getAzureApiClientV1ForDashBoard()
                            .create(ApiInterface::class.java);
                    val device_modal: String = Build.MODEL;


                    val version: String = "and:" + BuildConfig.VERSION_NAME;
                    val call: Call<ProfileDashboardResponseData> = apiService.getNewDashboardData(
                        AppConfig.APP_BRANDING_ID,
                        appToken,
                        groupId,
                        System.currentTimeMillis(),
                        timeZoneId,
                        device_modal,
                        version
                    );


                    call.enqueue(object : Callback<ProfileDashboardResponseData> {
                        override fun onResponse(
                            call: Call<ProfileDashboardResponseData>,
                            response: Response<ProfileDashboardResponseData>
                        ) {
                            newDashboardLoading.visibility = View.GONE;
                            if (response.isSuccessful) {
                                println(response)
                                val dashboardMainData: JsonObject =
                                    Gson().toJsonTree(response.body()!!.data).asJsonObject;

                                val createDashboardLogObj =
                                    CreateDashboardLogObj(loggedInUserId, dashboardMainData)

                                if (dashboardMainData.get("user_id").asString != loggedInUserId) {
                                    val createDashboardLogCall: Call<ProfileDashboardResponseData> =
                                        apiService.createDashboardLog(
                                            AppConfig.APP_BRANDING_ID,
                                            appToken,
                                            createDashboardLogObj
                                        )

                                    createDashboardLogCall.enqueue(object :
                                        Callback<ProfileDashboardResponseData> {
                                        override fun onResponse(
                                            call: Call<ProfileDashboardResponseData>,
                                            response: Response<ProfileDashboardResponseData>
                                        ) {
                                            print(response)
                                        }

                                        override fun onFailure(
                                            call: Call<ProfileDashboardResponseData>,
                                            t: Throwable
                                        ) {
                                            t.printStackTrace()
                                        }
                                    })
                                }


                                print(groupId)

                                pref.saveWnwDashboardData(dashboardMainData)
                                setDashboardAllData(dashboardMainData)


                            } else {
                                Toast.makeText(
                                    context,
                                    "There is an issue when loading data, Please contact admin.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<ProfileDashboardResponseData>,
                            t: Throwable
                        ) {
                            newDashboardLoading.visibility = View.GONE;
                        }

                    });


                }, 4000)
            } else {

                print(groupId)

                try {
                    val jsonParser: JsonParser = JsonParser();
                    val gsonObject: JsonObject =
                        jsonParser.parse(pref.savedWnwDashboardData) as (JsonObject);


                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        syncing_data_main.setBackgroundResource(R.drawable.life_plus_gradient_rectangle_corners)

                    } else {
                        syncing_data_main.setBackgroundResource(R.drawable.ayubo_life_gradient_rectangle_corners)
                    }


                    syncing_data_main.visibility = View.VISIBLE
                    if (gsonObject.get("user_id").asString == loggedInUserId) {
                        val task = updateDashboardBackgroundData();
                        task.execute();
                    }

                    setDashboardAllData(gsonObject)
                    newDashboardLoading.visibility = View.GONE;

                } catch (err: java.lang.Exception) {
                    Log.d("Error", err.toString());
                }


            }

        }


    }

    fun setDashboardAllData(dashboardMainData: JsonObject) {
        var jsonArraySettings: JsonArray = JsonArray();


        setNotificationData(dashboardMainData.get("todo").asJsonArray);

        setAnnouncementData(dashboardMainData.get("announcements").asJsonArray)

        mainView.life_points_linear_layout.visibility = View.GONE

        if (dashboardMainData.get("enable_title").asString.equals("inactive")) {
            dashboardMainTopic.visibility = View.GONE
            topic.visibility = View.GONE
        } else {
            dashboardMainTopic.visibility = View.VISIBLE
            topic.visibility = View.VISIBLE
        }



        if (dashboardMainData.get("enable_description").asString.equals("inactive")) {
            dashboardIntroText.visibility = View.GONE
            mainView.imageViewForHideDashboardDescription.visibility = View.GONE
        } else {

            try {
                val isShowDashboardDescription: Boolean = pref.showDashboardDescription
                if (isShowDashboardDescription) {
                    topic.visibility = View.VISIBLE
                } else {
                    topic.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            mainView.imageViewForHideDashboardDescription.setOnClickListener {

                try {
                    pref.showDashboardDescription = false;
                    topic.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }


        val tabsJsonObject: JsonObject = dashboardMainData.get("tabs").asJsonObject;
        tabLayout.visibility = View.VISIBLE
        pref.disableAllTabs = false;

        if (tabsJsonObject.size() > 0) {
            if (tabsJsonObject.get("todo").asString.equals("inactive") && tabsJsonObject.get(
                    "perks"
                ).asString.equals("inactive") && tabsJsonObject.get("records").asString.equals(
                    "inactive"
                )
            ) {

                tabLayout.visibility = View.GONE
                tabLayoutViewPager.beginFakeDrag();
                tabLayoutViewPager.currentItem = 0;


                pref.disableAllTabs = true;


            } else if (tabsJsonObject.get("todo").asString.equals("inactive")) {
                tabLayout.removeTabAt(1)
            } else if (tabsJsonObject.get("perks").asString.equals("inactive")) {
                tabLayout.removeTabAt(2)
            } else if (tabsJsonObject.get("records").asString.equals("inactive")) {
                tabLayout.removeTabAt(3)
            }
        }




        if (!dashboardMainData.get("settings").equals("")) {

            try {
                jsonArraySettings = dashboardMainData.get("settings").asJsonArray
                val settingItemArrayList = ArrayList<SettingItem>()


                for (i in 0 until jsonArraySettings.size()) {
                    val settingItem = jsonArraySettings.get(i)

                    settingItemArrayList.add(
                        SettingItem(
                            settingItem.asJsonObject.get("icon").asString,
                            settingItem.asJsonObject.get("title").asString,
                            settingItem.asJsonObject.get("action").asString,
                            settingItem.asJsonObject.get("meta").asString

                        )

                    )


                }

                if (groupId != "user_dashboard" && groupId != "wnw_dashboard") {

                    try {
//                                        val dashboardSettingsImageView: ImageView =
//                                            this@NewProfileDashboard.activity!!.findViewById(R.id.dashboard_settings) as (ImageView);


                        val dashboardSettingsLinearLayout: LinearLayout =
                            activity?.findViewById(R.id.dashboard_settings_liner_layout) as (LinearLayout);



                        dashboardSettingsLinearLayout.setOnClickListener {

                            System.out.println(settingItemArrayList)
                            openBottomSlider(settingItemArrayList);
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT)
                            .show();
                    }


                }


            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
            }


        }

//                        val tabLayout: TabLayout? = activity?.findViewById<TabLayout>(R.id.tabLayoutMain);
//
//                        if (tabLayout != null) {
//                            tabLayout.removeTabAt(2);
//                        }

        try {
            if (dashboardMainData.get("enable_badges").asString == "active") {
                myBadgesLink.visibility = View.VISIBLE
                dataBadgesMainLinearLayout.visibility = View.VISIBLE
            } else {
                myBadgesLink.visibility = View.GONE
                dataBadgesMainLinearLayout.visibility = View.GONE
            }

            if (groupId != "user_dashboard" && groupId != "wnw_dashboard") {
                val textview: TextView =
                    activity?.findViewById(R.id.new_dashboard_header_topic) as (TextView);
                if (dashboardMainData.get("dashboard_title") !== null) {
                    textview.text = dashboardMainData.get("dashboard_title").asString;
                    textview.setTextColor(Color.parseColor("#3B3B3B"))
                } else {
                    textview.text = "";
                }
            } else {
                val toolbar: Toolbar? =
                    activity?.findViewById(R.id.toolbar_for_newsidemenu);
                val txtExpertHeading: TextView? =
                    toolbar?.findViewById(R.id.txt_expert_heading);

                if (dashboardMainData.get("dashboard_title") !== null) {
                    if (txtExpertHeading != null) {
                        txtExpertHeading.text =
                            dashboardMainData.get("dashboard_title").asString
                        txtExpertHeading.setTextColor(Color.parseColor("#3B3B3B"))
                    };

                } else {
                    if (txtExpertHeading != null) {
                        txtExpertHeading.text = ""
                    };
                }

//                            txtExpertHeading!!.setOnClickListener {
//                                openBottomSlider(jsonObjectSettings);
//                            }
            }

//                        for (i in 0 until dashboardMainData.get("cards").asJsonArray.size()) {
//                            val card = dashboardMainData.get("cards").asJsonArray.get(i).asJsonObject;
//
//                        }


            println(dashboardMainData)
            var subscriptionsObj: JsonObject = JsonObject();


            subscriptionRecyclerview.removeAllViews();
//            subscriptionRecyclerview = subscriptionRecyclerview

            if (dashboardMainData.get("subscriptions").asJsonObject.size() > 0) {

                subscriptionsObj =
                    dashboardMainData.get("subscriptions").asJsonObject

                if (subscriptionsObj.get("overview").asString !== "") {
                    bottom_info_linear_layout.visibility = View.VISIBLE
                } else {
                    bottom_info_linear_layout.visibility = View.GONE
                }
                bottom_info_text.text = subscriptionsObj.get("overview").asString;

                addSubscriptionCards(subscriptionsObj.get("cards").asJsonArray);


            } else {
                addSubscriptionCards(JsonArray());
            }

            addWeeklyRelatedCardTracker(
                mainView,
                dashboardMainData.get("cards").asJsonArray
            );

//                        if (dashboardMainData.get("dashboard_description").asString != "") {
//                            dashboard_intro_text.visibility = View.VISIBLE
//                        } else {
//                            dashboard_intro_text.visibility = View.GONE
//                        }
            dashboardIntroText.text = dashboardMainData.get("dashboard_description").asString


            if (dashboardMainData.get("enable_points").asString.equals("active") && dashboardMainData.get(
                    "points"
                ).asJsonObject.get("text") !== null && dashboardMainData.get("points").asJsonObject.get(
                    "value"
                ) !== null
            ) {
                lifePointsLinearLayout.visibility = View.VISIBLE
                data_points_text.text =
                    dashboardMainData.get("points").asJsonObject.get("text").asString
                data_points_value.text =
                    dashboardMainData.get("points").asJsonObject.get("value").asInt.toString()
            }
            val badgesJsonArray: JsonArray =
                dashboardMainData.get("badges").asJsonArray
            dataBadgesLinearLayout.removeAllViews()
            if (badgesJsonArray.size() > 0) {
                val layparam: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(130, 130);
                layparam.setMargins(0, 0, 24, 0);
                for (i in 0 until badgesJsonArray.size()) {
                    val item = badgesJsonArray.get(i)
                    val imageView: ImageView = ImageView(context);
                    context?.let {
                        Glide.with(it)
                            .load(item.asJsonObject.get("image_url_active").asString)
                            .into(imageView)
                    };
                    imageView.layoutParams = layparam;


                    if (i < 3) {
                        dataBadgesLinearLayout.addView(imageView);
                        openBadgePopUp(item)
                    }


                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
        }
    }

    private fun setAnnouncementData(announcementArrayList: JsonArray) {
        try {

            println(announcementArrayList)

            if (announcementArrayList.size() > 0) {
                mainView.announcement_recycler_view.visibility = View.VISIBLE

                mainView.announcement_recycler_view.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                val announcementCardItemsArrayList = ArrayList<AnnouncementCardItem>()

                for (i in 0 until announcementArrayList.size()) {

                    val announcementCard =
                        Gson().fromJson(announcementArrayList[i], AnnouncementCardItem::class.java);

                    announcementCardItemsArrayList.add(announcementCard)


                }
                announcementCardAdapter =
                    AnnouncementCardAdapter(requireContext(), announcementCardItemsArrayList, false)

                try {
                    announcementCardAdapter!!.onAnnouncementItemClickListener =
                        this@NewProfileDashboard
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mainView.announcement_recycler_view.adapter = announcementCardAdapter
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onAnnouncementCardItemClick(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }

    private fun setNotificationData(todoArrayList: JsonArray) {
        try {

            println(todoArrayList)

            mainView.notification_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val notificationCardItemsArrayList = ArrayList<NotificationCardItem>()
//
            for (i in 0 until todoArrayList.size()) {

                val notificationCardItem =
                    Gson().fromJson(todoArrayList[i], NotificationCardItem::class.java);


                notificationCardItemsArrayList.add(notificationCardItem)


            }


            notificationCardAdapter =
                context?.let {
                    NotificationCardAdapter(
                        it,
                        notificationCardItemsArrayList,
                        false
                    )
                }

            try {
                notificationCardAdapter!!.onNotificationItemClickListener =
                    this@NewProfileDashboard
            } catch (e: Exception) {
                e.printStackTrace()
            }


            mainView.notification_recycler_view.adapter = notificationCardAdapter


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addSubscriptionCards(subscriptionMainCardsData: JsonArray) {

        requireActivity().runOnUiThread {
            subscriptionCardIndicatorsLayout.visibility = View.GONE
            subscriptionRecyclerview.removeAllViews();
            try {


                subscriptionRecyclerview.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                val subscriptionCardItemsArrayList = ArrayList<SubscriptionCardItem>()

                for (i in 0 until subscriptionMainCardsData.size()) {
                    val subscriptionCard = subscriptionMainCardsData.get(i)

                    subscriptionCardItemsArrayList.add(
                        SubscriptionCardItem(
                            subscriptionCard.asJsonObject.get("title").asString,
                            subscriptionCard.asJsonObject.get("summary").asString,
                            subscriptionCard.asJsonObject.get("currency").asString,
                            subscriptionCard.asJsonObject.get("rate").asString,
                            subscriptionCard.asJsonObject.get("frequency").asString,
                            subscriptionCard.asJsonObject.get("list").asJsonArray,
                            subscriptionCard.asJsonObject.get("action").asString,
                            subscriptionCard.asJsonObject.get("meta").asString,
                            subscriptionCard.asJsonObject.get("button_text").asString

                        )

                    )


                }



                subscriptionCardAdapter =
                    context?.let {
                        SubscriptionCardAdapter(
                            it,
                            subscriptionCardItemsArrayList,
                            false
                        )
                    }
                try {
                    subscriptionCardAdapter!!.onSubscriptionItemClickListener =
                        this@NewProfileDashboard
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                subscriptionRecyclerview.adapter = subscriptionCardAdapter

                if (subscriptionCardItemsArrayList.size > 2) {
                    subscriptionCardIndicatorsLayout.visibility = View.VISIBLE
                    dotsCount = subscriptionCardAdapter!!.itemCount
                    subscriptionCardIndicatorsLayout.removeAllViewsInLayout()
                    dotsImageViews = ArrayList<ImageView>()


                    for (i in 0 until dotsCount) {
                        dotsImageViews.add(ImageView(context))
                        dotsImageViews.get(i)
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
                        val imageView: ImageView = dotsImageViews.get(i);
                        subscriptionCardIndicatorsLayout.addView(imageView, params);
                    }

                    dotsImageViews.get(0).setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.subscribe_slide_active_dot
                        )
                    );

                    val scrollListener = object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            super.onScrollStateChanged(recyclerView, newState)


                            for (i in 0 until dotsCount) {
                                dotsImageViews.get(i).setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context!!,
                                        R.drawable.nonactive_dot
                                    )
                                );
                            }

                            dotsImageViews[newState].setImageDrawable(
                                ContextCompat.getDrawable(
                                    context!!,
                                    R.drawable.subscribe_slide_active_dot
                                )
                            );
                        }
                    }
                    subscriptionRecyclerview.addOnScrollListener(scrollListener)
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }


    }

    fun addWeeklyRelatedCardTracker(view: View, dashboardMainCardsData: JsonArray) {
        try {
            val weeklyRelatedCardTrackerRecyclerView =
                view.findViewById(R.id.weekly_related_tracker_card_recycler_view) as RecyclerView
            weeklyRelatedCardTrackerRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            weeklyRelatedCardTrackerRecyclerView.isNestedScrollingEnabled = false;
            val weeklyRelatedCardTrackerItemsArrayList = ArrayList<WeeklyTrackerCardItem>()

//            // announcement
//            weeklyRelatedCardTrackerItemsArrayList.add(
//                WeeklyTrackerCardItem(
//                    "",
//                    "",
//                    "",
//                    "announcement",
//                    "",
//                    JsonObject(),
//                    JsonObject(),
//                    JsonObject(),
//                    JsonObject(),
//                    JsonObject(),
//                    JsonObject(),
//                    "",
//                    ""
//                )
//            )


            for (i in 0 until dashboardMainCardsData.size()) {


                val weeklyTrackerCard = dashboardMainCardsData.get(i)


                weeklyRelatedCardTrackerItemsArrayList.add(
                    WeeklyTrackerCardItem(
                        weeklyTrackerCard.asJsonObject.get("id").asString,
                        weeklyTrackerCard.asJsonObject.get("title").asString,
                        weeklyTrackerCard.asJsonObject.get("summary").asString,
                        weeklyTrackerCard.asJsonObject.get("type").asString,
                        weeklyTrackerCard.asJsonObject.get("category").asString,
                        weeklyTrackerCard.asJsonObject.get("description").asJsonObject,
                        if (weeklyTrackerCard.asJsonObject.get("plusbutton") == null) null else weeklyTrackerCard.asJsonObject.get(
                            "plusbutton"
                        ).asJsonObject,
                        weeklyTrackerCard.asJsonObject.get("click").asJsonObject,
                        weeklyTrackerCard.asJsonObject.get("options").asJsonObject,
                        weeklyTrackerCard.asJsonObject.get("bottom").asJsonObject,
                        if (weeklyTrackerCard.asJsonObject.get("chart_data") == null) null else weeklyTrackerCard.asJsonObject.get(
                            "chart_data"
                        ).asJsonObject,
                        if (weeklyTrackerCard.asJsonObject.get("badge_url") == null) null else weeklyTrackerCard.asJsonObject.get(
                            "badge_url"
                        ).asString,
                        if (weeklyTrackerCard.asJsonObject.get("badge_title") == null) null else weeklyTrackerCard.asJsonObject.get(
                            "badge_title"
                        ).asString

                    )

                )


            }





            weeklyRelatedCardTrackerAdapter = context?.let {
                WeeklyRelatedCardTrackerAdapter(
                    it,
                    weeklyRelatedCardTrackerItemsArrayList,
                    false
                )
            }
            try {
                weeklyRelatedCardTrackerAdapter!!.onItemClickListener = this@NewProfileDashboard
            } catch (e: Exception) {
                e.printStackTrace()
            }

            weeklyRelatedCardTrackerRecyclerView.adapter = weeklyRelatedCardTrackerAdapter
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

    }

    fun openBadgePopUp(badgeElement: JsonElement) {

        if (!badgeElement.asJsonObject.get("read_badge").asBoolean) {


            val progressDialogForSubscribe: ProgressDialog;
            progressDialogForSubscribe = ProgressDialog(context);
            progressDialogForSubscribe.show();
            progressDialogForSubscribe.window?.setGravity(Gravity.CENTER);
            progressDialogForSubscribe.setContentView(R.layout.badge_pop_up);

            progressDialogForSubscribe.window?.setBackgroundDrawableResource(
                android.R.color.transparent
            );


            var actualImageString = "";
            var instructionDetail = "";
            var congratulationText = "";

            if (badgeElement.asJsonObject.get("status").asString.equals("active")) {
                actualImageString = badgeElement.asJsonObject.get("image_url_active").asString
                instructionDetail = "Continue good works"
                congratulationText = "Congratulations !"
                progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility =
                    View.VISIBLE


            } else {
                instructionDetail = ""
                congratulationText = ""
                actualImageString =
                    badgeElement.asJsonObject.get("image_url_inactive").asString;
                progressDialogForSubscribe.window?.findViewById<LinearLayout>(R.id.badge_share_btn)?.visibility =
                    View.GONE
            }
            progressDialogForSubscribe.window?.findViewById<ImageView>(R.id.badgeImageView)
                ?.let { it1 ->
                    context?.let {
                        Glide.with(it).load(actualImageString).into(it1)
                    }
                }
            progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para1)
                ?.setText(badgeElement.asJsonObject.get("description").asString)
            progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para2)
                ?.setText(badgeElement.asJsonObject.get("title").asString)
            progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_congratulation_textview)
                ?.setText(congratulationText)
            progressDialogForSubscribe.window?.findViewById<TextView>(R.id.badge_para3)
                ?.setText(instructionDetail)
            progressDialogForSubscribe.window?.findViewById<TextView>(R.id.view_my_badges)
                ?.setOnClickListener {

                    activity?.let {
                        val intent = Intent(it, MyBadgesActivity::class.java)
                        it.startActivity(intent)

                    }
                }

            progressDialogForSubscribe.setOnDismissListener {
                System.out.println(badgeElement)

                val task = updateBadgeStatusDataAsync(
                    AppConfig.APP_BRANDING_ID,
                    appToken,
                    badgeElement.asJsonObject.get("id").asString
                );
                task.execute();


            }
        }

    }

    internal inner class updateBadgeStatusDataAsync(
        brandingAppId: String,
        appToken: String,
        badgeId: String
    ) : AsyncTask<Void, Void, Void>() {

        val brandingAppId = brandingAppId;
        val appToken = appToken;
        val badgeId = badgeId;


        override fun doInBackground(vararg p0: Void?): Void? {
            val apiService: ApiInterface =
                ApiClient.getAzureApiClientV1().create(ApiInterface::class.java);
            val profileDashboardResponseDataCall: Call<ProfileDashboardResponseData> =
                apiService.updateBadgeStatus(
                    brandingAppId,
                    appToken,
                    badgeId
                );

            profileDashboardResponseDataCall.enqueue(object :
                Callback<ProfileDashboardResponseData> {
                override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
                ) {
                    if (response.isSuccessful()) {
                        System.out.println(response)
                    }
                }

                override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {

                }
            });
            return null;
        }

    }

    internal inner class updateDashboardBackgroundData() : AsyncTask<Void, Void, Void>() {


        override fun doInBackground(vararg p0: Void?): Void? {

            val timeZone: TimeZone = TimeZone.getDefault();
            val timeZoneId = timeZone.id;

            try {
                GoogleClientForDiscover(context, activity, true);
            } catch (e: Exception) {
                e.printStackTrace()
            }


            val apiService: ApiInterface =
                ApiClient.getAzureApiClientV1ForDashBoard().create(ApiInterface::class.java);
            val device_modal: String = Build.MODEL;


            val version: String = "and:" + BuildConfig.VERSION_NAME;
            val call: Call<ProfileDashboardResponseData> = apiService.getNewDashboardData(
                AppConfig.APP_BRANDING_ID,
                appToken,
                groupId,
                System.currentTimeMillis(),
                timeZoneId,
                device_modal,
                version
            );


            call.enqueue(object : Callback<ProfileDashboardResponseData> {
                override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
                ) {
                    newDashboardLoading.visibility = View.GONE;
                    if (response.isSuccessful) {
                        println(response)



                        try {
                            val dashboardMainData: JsonObject =
                                Gson().toJsonTree(response.body()!!.data).asJsonObject;


                            val createDashboardLogObj =
                                CreateDashboardLogObj(loggedInUserId, dashboardMainData)

                            if (dashboardMainData.get("user_id").asString != loggedInUserId) {
                                val createDashboardLogCall: Call<ProfileDashboardResponseData> =
                                    apiService.createDashboardLog(
                                        AppConfig.APP_BRANDING_ID,
                                        appToken,
                                        createDashboardLogObj
                                    )

                                createDashboardLogCall.enqueue(object :
                                    Callback<ProfileDashboardResponseData> {
                                    override fun onResponse(
                                        call: Call<ProfileDashboardResponseData>,
                                        response: Response<ProfileDashboardResponseData>
                                    ) {
                                        print(response)
                                    }

                                    override fun onFailure(
                                        call: Call<ProfileDashboardResponseData>,
                                        t: Throwable
                                    ) {
                                        t.printStackTrace()
                                    }
                                })
                            }

                            if (groupId == "user_dashboard") {
                                pref.saveDashboardData(dashboardMainData)
                            } else {
                                pref.saveWnwDashboardData(dashboardMainData)
                            }


                            setDashboardAllData(dashboardMainData)


                            syncing_data_main.visibility = View.GONE
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }


                    }
                }

                override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                }

            });

            return null;
        }

    }

    fun onProcessAction(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }

    fun processAction(action: String, meta: String) {

        Log.d("====action====", action)
        Log.d("====meta====", meta)

        if (action == "treasure") {
            onTreasureClick(meta)
        }

        if (action == "add_report") {
            openAddReportDialog(meta)
        }

        if (action == "step_count_details") {
            onStepCountDetailsClick(meta)
        }

        if (action == "program_dash") {
            goToNewDashboard(meta)
        }

        if (action == "discover") {
            onDiscoverClick()
        }
        if (action == "prescription") {
            onClickPrescription(meta)
        }
        if (action == "ordermedicine") {
            openOrderMedicine(meta)
        }
        if (action == "echannel") {
            onClickEChannel(meta)
        }
        if (action == "store_group") {
            onClickStoreGroupView(meta)
        }
        if (action == "call") {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$meta")
            startActivity(intent)
        }

        if (action == "process_payment") {
            onClickPaymentConfirm(meta)
        }
        if (action == "other_payments") {
            onClickOtherPayment(meta)
        }
        if (action == "paynow") {
            if (action == "paynow") {
                if (meta == "daily") {
                    onPayNowClick("4_11")
                } else if (meta == "weekly") {
                    onPayNowClick("5_11")
                } else {
                    onPayNowClick(meta)
                }
            }
        }
        if (action == "addtobill") {
            onClickAddToBill(meta)
        }

        if (action == "filtered_videocall") {
            onVideoCallClick(meta)
        }
        if (action == "videocall") {
            onVideoCallClick("")
        }
        if (action == "goal") {
            onGoalClick()
        }
        if (action == "filtered_reportreview") {
            onReportReviewFiltered(meta)
        }
        if (action == "reports") {
            onReportsClick()
        }
        if (action == "help") {
            onHelpClick()
        }
        if (action == "leaderboard") {
            onLeaderboardClick(meta)
        }
        if (action == "openbadgenative") {
            onOpenBadgersClick()
        }

        if (action == "web") {
            onClickWeb(meta)
        }
        if (action == "common") {
            onClickCommonWebView(meta)
        }

        if (action == "commonpay") {
            onClickCommonWebView(meta)
        }

        if (action == "webapy") {
            onClickWeb(meta)
        }

        if (action == "paypin") {
            openPayPin()
        }

        if (action == "paysummary") {
            openPaymentSummary()
        }

        if (action == "commonview") {
            onClickCommonWebView(meta)
        }

        if (action == "data_challenge") {
            goToWalkAndWin(meta);
        }

        if (action == "native_post") {
            onClickNativeView(meta)
        }

        if (action == "native_post_json") {
            onJSONNativePostClick(meta)
        } else if (action == "post") {
            onPostClick(meta)
        } else if (action == "challenge") {
            onMapChallangeClick(meta)
        } else if (action == "chat") {
            onChatClick(meta)
        } else if (action == "filtered_chat") {
            onChatClickFilter(meta)
        } else if (action == "program_timeline") {
            onProgramNewDesignClick(meta)
        } else if (action == "programtimeline") {
            onProgramNewDesignClick(meta)
        } else if (action == "program") {
            onProgramPostClick(meta)
        } else if (action == "channeling") {
            onButtonChannelingClick(meta)
        } else if (action == "janashakthiwelcome") {
            onJanashakthiWelcomeClick(meta)
        } else if (action == "dynamicquestion") {
            onDyanamicQuestionClick(meta)
        } else if (action == "janashakthireports") {
            onJanashakthiReportsClick(meta)
        } else if (action == "doctor_booking") {
            doctorVideoCall(meta)
        } else if (action == "record_trends") {
            goToTestReports(meta)
        } else if (action == "get_feedback") {
            openFeedbackPage(meta)
        } else if (action == "points_leaderboard") {
            openPointsLeaderBoard(action, meta)
        } else if (action == "step_summary_dashboard") {
            goToStepSummaryDashboard()
        } else if (action == "payment") {
            goToPayHerePayment(meta)
        }


    }

    private fun goToPayHerePayment(meta: String) {
        val intent = Intent(context, NewPaymentMainActivity::class.java)
        intent.putExtra("payhere_payment_id", meta)
        startActivity(intent)
    }

    private fun goToStepSummaryDashboard() {
        val intent = Intent(context, StepSummaryDashboardActivity::class.java)
        startActivity(intent)
    }

    private fun openPointsLeaderBoard(action: String, meta: String) {
        val intent = Intent(context, PointsLeaderboardActivity::class.java)
        intent.putExtra("leader_board_type", action)
        intent.putExtra("leader_board_community", meta)

        val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(context);


        if (meta.equals("stepchallenge-daily")) {
            loggerFB.logEvent("Daily_Leaderboard")
        } else if (meta.equals("stepchallenge-weekly")) {
            loggerFB.logEvent("Weekly_Leaderboard")
        } else if (meta.equals("stepchallenge-monthly")) {
            loggerFB.logEvent("Monthly_Leaderboard")
        } else {
            loggerFB.logEvent(meta)
        }




        startActivity(intent)
    }

    private fun openAddReportDialog(meta: String) {
        val intent = Intent(context, HydrationTrackerDialog::class.java)
        intent.putExtra("report_id", meta)
        startActivity(intent);
    }

    private fun openFeedbackPage(meta: String) {
        val intent = Intent(context, FeedBackPopupActivity::class.java)
        intent.putExtra("feedback_detail", meta)
        startActivity(intent);
    }

    private fun goToTestReports(meta: String) {

        if (meta != "") {
            val intent = Intent(context, TestReportsActivity::class.java)
            intent.putExtra("report_detail", meta)
            startActivity(intent);
        }

    }

    private fun doctorVideoCall(meta: String) {
        val intent = Intent(context, UploadActivity::class.java)
        intent.putExtra("doctor_id", meta)
        startActivity(intent);
    }

    fun onChatClickFilter(meta: String) {
        // AskActivity.startActivity(this,meta)
        val intent = Intent(context, AskActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    fun onJSONNativePostClick(meta: String) {
        val intent = Intent(context, NativePostJSONActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    private fun goToWalkAndWin(meta: String) {
        val intent = Intent(context, WalkWinMainActivity::class.java)
        intent.putExtra("challenge_id", meta)
        startActivity(intent);
    }

    private fun openPaymentSummary() {
        val intent = Intent(context, PaymentSummaryViewActivity::class.java)
        startActivity(intent);
    }

    private fun openPayPin() {
        val intent = Intent(context, PaymentPinSubmitActivity::class.java)
        startActivity(intent);
    }

    fun onReportReviewFiltered(meta: String) {
        val intent = Intent(context, GetAReviewActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun onClickStoreGroupView(meta: String) {
        if (meta.isNotEmpty()) {
            GroupViewActivity.startActivity(context as Activity, meta)
        }
    }

    fun onClickEChannel(meta: String) {
        startActivity(Intent(context, VisitDoctorActivity::class.java))
    }

    fun onClickPrescription(meta: String) {
        startActivity(Intent(context, Medicine_ViewActivity::class.java))
    }

    fun openOrderMedicine(meta: String) {
//        startActivity(Intent(this, Medicine_ViewActivity::class.java))
//        startActivity(Intent(this, OrderMedicineMain::class.java))
        val i = Intent(context, OMMainPage::class.java)
        val jsonObject: JsonObject =
            Gson().toJsonTree(OMCommon(requireContext()).retrieveFromDraftSingleton()).asJsonObject;
        OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
        startActivity(i)
    }

    fun onDiscoverClick() {
//        val intent = Intent(context, LifePlusProgramActivity::class.java)
//        val intent = Intent(context, NewDiscoverActivity::class.java)
        val intent = SetDiscoverPage().getDiscoverIntent(requireContext())
        intent.putExtra("isFromSearchResults", false)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goToNewDashboard(meta: String) {
        val intent = Intent(context, DiscoverToNewDashboard::class.java)
        intent.putExtra("dashboard_meta", meta)
        startActivity(intent);
    }

    fun onTreasureClick(meta: String?) {
        val intent = Intent(context, TreasureViewActivity::class.java)
        intent.putExtra(EXTRA_TREASURE_KEY, meta)
        startActivity(intent)
    }


    fun onOpenBadgersClick() {
        val intent = Intent(context, Badges_Activity::class.java)
        startActivity(intent)
    }

    private fun onStepCountDetailsClick(meta: String?) {
        System.out.println("Redirect to my wellness")
        val i: Intent = Intent(context, StepHistory_Activity::class.java);
        startActivity(i);

    }

    fun onLeaderboardClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, NewLeaderBoardActivity::class.java)
            intent.putExtra("challange_id", meta)
            startActivity(intent)
        }
    }

    fun onPayNowClick(meta: String) {
        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra("paymentmeta", meta)
        startActivity(intent)
    }

    fun onHelpClick() {
        val intent = Intent(context, HelpFeedbackActivity::class.java)
        startActivity(intent)
    }

    fun onReportsClick() {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        intent.putExtra("data", "all")
        Ram.setReportsType("fromHome")
        startActivity(intent)
    }

    fun onGoalClick() {

        val prefManager = PrefManager(context)
        val status = prefManager.myGoalData["my_goal_status"]

        if (status === "Pending") {
            val intent = Intent(context, AchivedGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Pick") {
            val intent = Intent(context, PickAGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Completed") {
            val serviceObj = HomePage_Utility(context)
            serviceObj.showAlert_Deleted(
                context,
                "This goal has been achieved for the day. Please pick another goal tomorrow"
            )
        }
    }

    fun onProgramNewDesignClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, ProgramActivity::class.java)
            intent.putExtra("meta", meta)
            startActivity(intent)
        }
    }

    fun onClickNativeView(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    fun onPaymentProcessed(obj: PriceList) {
        // PaymentConfirmActivity.startActivity(context,obj.item_price_master_id,obj.text,obj.service_payment_frequency_source_id,obj.related_id,obj.payment_source_id,obj.payment_frequency)
    }

    fun onClickOtherPayment(meta: String) {
        //OtherPaymentActivity.startActivity(context,meta)
    }

    fun onClickAddToBill(meta: String) {
        // EnterMobileNumberActivityPayment.startActivity(context,meta)
    }

    fun onClickPaymentConfirm(meta: String) {
        // PaymentConfirmActivity.startActivity(this,meta)
    }

    fun onClickCommonWebView(meta: String) {
        val intent = Intent(context, CommonWebViewActivity::class.java)
        intent.putExtra("URL", meta)
        startActivity(intent)
    }

    fun onClickWeb(meta: String) {

        if (meta.isNotEmpty()) {
            Log.d("......meta......1...", meta)
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(meta)
                startActivity(i)
            } catch (e: Exception) {
                e.printStackTrace();
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        } else {
            Log.d("......meta......0...", meta)
        }
    }

    fun onChatClick(meta: String) {
        //  AyuboChatActivity.startActivity(context,meta, false)
    }

    fun onChatQuesClick(meta: String) {

        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        //  IntroActivity.startActivity(context)
    }

    fun onJanashakthiWelcomeClick(meta: String) {
        val pref = PrefManager(context)
        pref.relateID = meta
        pref.setIsJanashakthiWelcomee(true)
        val intent = Intent(context, JanashakthiWelcomeActivity::class.java)
        startActivity(intent)
    }

    fun onDyanamicQuestionClick(meta: String) {
        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        val intent = Intent(context, IntroActivity::class.java)
        startActivity(intent)
    }

    fun onJanashakthiReportsClick(meta: String) {
        MedicalUpdateActivity.startActivity(requireContext())
    }

    fun onOpenVideoClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenImageClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenNativePost(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    fun onPostClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    private fun startDoctorsActivity(doctorID: String) {
        val parameters = DocSearchParameters()
        parameters.doctorId = doctorID
        parameters.locationId = ""
        parameters.specializationId = ""
        parameters.date = ""
        val pref = PrefManager(context)
        val user_id = pref.loginUser["uid"]
        parameters.user_id = user_id

        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(parameters))
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "")
        startActivity(intent)

    }

    fun onButtonChannelingClick(meta: String) {
        if (meta.length > 0) {
            startDoctorsActivity(meta)
        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun onVideoCallClick(meta: String) {
        if (meta.length > 0) {
            val activity = "my_doctor"
            val intent = Intent(context, MyDoctorLocations_Activity::class.java)
            intent.putExtra("doctor_id", meta)
            intent.putExtra("activityName", activity)
            startActivity(intent)
        } else {
            val intent = Intent(context, MyDoctor_Activity::class.java)
            intent.putExtra("activityName", "myExperts")
            startActivity(intent)
        }
    }

    fun onMapChallangeClick(meta: String) {

        if (meta.isNotEmpty()) {
            val intent = Intent(context, MapChallangeKActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_ID, meta)
            startActivity(intent)

//            val intent = Intent(this, MapChallengeActivity::class.java)
//            intent.putExtra("challenge_id", meta)
//            startActivity(intent)
        }


        // old one
//        if (meta.length > 0) {
////            val serviceObj = MapChallangesServices(this, meta)
////            serviceObj.Service_getChallengeMapData_ServiceCall()
//
//            val intent = Intent(context, MapChallengeActivity::class.java)
//            intent.putExtra("challenge_id", meta)
//            startActivity(intent)
//
//        } else {
//            val intent = Intent(context, NewCHallengeActivity::class.java)
//            startActivity(intent)
//        }
    }

    fun onShowExperts(experts: ArrayList<Experts>) {
        // ViewExpertsActivity.startActivity(context,experts)
//        val intent = Intent(this,ViewExpertsActivity::class.java)
//        intent.putExtra("program_experts", experts)
//        startActivity(intent)
    }

    fun onAskClick(activityName: String, meta: String) {
        val intent = Intent(context, AskQuestion_Activity::class.java)
        startActivity(intent)
    }

    fun onProgramPostClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, SingleTimeline_Activity::class.java)
            intent.putExtra("related_by_id", meta)
            intent.putExtra("type", "program")
            startActivity(intent)
        }
    }

    override fun onSubscriptionItemClick(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }

    override fun onNotificationItemClick(action: String, meta: String) {
//        processAction(action, meta)
        ActionMetaForFragment().processAction(requireContext(), action, meta)
    }

    override fun onAddToButtonClick(notificationCardItem: NotificationCardItem) {
        System.out.println(notificationCardItem)


        val endTime = notificationCardItem.task_datetime + (30 * 60 * 1000);
        val contentResolver: ContentResolver = requireContext().getContentResolver();

//        val values = ContentValues().apply {
//            put(CalendarContract.Reminders.MINUTES, 30)
//            put(CalendarContract.Reminders.EVENT_ID, notificationCardItem.id)
//            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
//        }
//        val uri: Uri = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)!!

        val calendarIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
//            .setData(uri)
            .putExtra(CalendarContract.Events.TITLE, notificationCardItem.title)
//            .putExtra(CalendarContract.Events.EVENT_LOCATION, "EVENT_LOCATION")
            .putExtra(CalendarContract.Events.DESCRIPTION, notificationCardItem.description)
            .putExtra(
                CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                notificationCardItem.task_datetime
            )
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
//            .putExtra(Intent.EXTRA_EMAIL, "EXTRA_EMAIL");

        try {
            if (calendarIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(calendarIntent)
            } else {
                Toast.makeText(
                    context,
                    "There is no app that can support this action",
                    Toast.LENGTH_SHORT
                ).show();
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


}
