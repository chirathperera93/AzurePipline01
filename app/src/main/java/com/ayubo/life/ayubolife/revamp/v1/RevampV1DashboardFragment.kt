package com.ayubo.life.ayubolife.revamp.v1

import android.graphics.Typeface
import android.os.*
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.BuildConfig
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.GoogleClientForDiscover
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.activity.*
import com.ayubo.life.ayubolife.revamp.v1.adapter.*
import com.ayubo.life.ayubolife.revamp.v1.model.*
import com.bumptech.glide.Glide
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_new_profile_dashboard.*
import kotlinx.android.synthetic.main.fragment_revamp_v1_dashboard.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RevampV1DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RevampV1DashboardFragment : Fragment(),
    V1DashboardMainCardSliderAdapter.OnUpComingItemClickListener,
    V1DashboardMainCardSlider2Adapter.OnCardSet2ItemClickListener,
    V1LifePointCardAdapter.OnPointsItemClickListener,
    V1TipCardAdapter.OnDailyTipItemClickListener {

    private lateinit var mainView: View
    private var v1LifePointCardAdapter: V1LifePointCardAdapter? = null
    private var v1TipCardAdapter: V1TipCardAdapter? = null
    lateinit var tabLayout: TabLayout
    lateinit var pref: PrefManager
    var PACKAGE_NAME: String = "com.google.android.apps.fitness"
    var isOpenedDialog: Boolean = false
    var groupId: String = "user_dashboard"
    lateinit var userToken: String
    var loggedInUserId: String = ""
    lateinit var tabLayoutViewPager: ViewPager

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var changeFragmentInterface: ChangeFragmentInterface

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RevampV1DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RevampV1DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

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

        mainView = inflater.inflate(R.layout.fragment_revamp_v1_dashboard, container, false)
        tabLayout = activity?.findViewById<TabLayout>(R.id.tabLayoutMain)!!
        tabLayoutViewPager = activity?.findViewById<ViewPager>(R.id.tabLayoutViewPager)!!
        tabLayout.visibility = View.VISIBLE
        pref = PrefManager(context)
        userToken = pref.userToken
        loggedInUserId = pref.loginUser["uid"].toString();


        // Inflate the layout for this fragment
        return mainView;

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    interface ChangeFragmentInterface {
        fun onGotoToDoClick(action: String)
    }

    private fun loadData() {
        if (pref.savedV1DashboardData == null) {
            mainView.new_v1_dashboard_loading.visibility = View.VISIBLE
            val task = updateV1DashboardBackgroundData()
            task.execute()
        } else {
            mainView.new_v1_dashboard_loading.visibility = View.GONE
            try {


                if (Constants.type == Constants.Type.LIFEPLUS) {
                    mainView.syncing_data_main_revamp_v1.setBackgroundResource(R.drawable.life_plus_gradient_rectangle_corners)

                } else {
                    mainView.syncing_data_main_revamp_v1.setBackgroundResource(R.drawable.ayubo_life_gradient_rectangle_corners)
                }


                mainView.syncing_data_main_revamp_v1.visibility = View.VISIBLE


                val task = updateV1DashboardBackgroundData()
                task.execute()

                setV1DashboardAllData(pref.savedV1DashboardData)

            } catch (err: java.lang.Exception) {
                Log.d("Error", err.toString())
            }


        }

    }

    internal inner class updateV1DashboardBackgroundData : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {

            val timeZone: TimeZone = TimeZone.getDefault()
            val timeZoneId = timeZone.id

            try {
                GoogleClientForDiscover(context, activity, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }



            Handler(Looper.getMainLooper()).postDelayed({


                val apiService: ApiInterface =
                    ApiClient.getAzureApiClientV3ForDashBoard()
                        .create(ApiInterface::class.java)
                val deviceModal: String = Build.MODEL


                val version: String = "and:" + BuildConfig.VERSION_NAME
                val call: Call<GetV1DashboardResponse> = apiService.getNewV1DashboardData(
                    AppConfig.APP_BRANDING_ID,
                    userToken,
                    groupId,
                    System.currentTimeMillis(),
                    timeZoneId,
                    deviceModal,
                    version
                )


                call.enqueue(object : Callback<GetV1DashboardResponse> {
                    override fun onResponse(
                        call: Call<GetV1DashboardResponse>,
                        response: Response<GetV1DashboardResponse>
                    ) {
                        mainView.new_v1_dashboard_loading.visibility = View.GONE
                        mainView.syncing_data_main_revamp_v1.visibility = View.GONE
                        if (response.isSuccessful) {
                            println(response)

                            try {


                                pref.saveV1DashboardData(response.body()!!.data)
                                setV1DashboardAllData(response.body()!!.data)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {
                            Toast.makeText(
                                context,
                                "There is an issue when loading data, Please contact admin.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<GetV1DashboardResponse>,
                        t: Throwable
                    ) {
                        t.printStackTrace()
                        mainView.new_v1_dashboard_loading.visibility = View.GONE;
                        mainView.syncing_data_main_revamp_v1.visibility = View.GONE
                    }

                });


            }, 4000)

            return null;
        }
    }

    private fun setV1DashboardAllData(data: V1DashboardData) {
        // set tabs
        tabLayout.visibility = View.VISIBLE
        pref.disableAllTabs = false
        if (data.tabs != null) {
            if (data.tabs.todo == "inactive" && data.tabs.perks == "inactive" && data.tabs.records == "inactive") {

                tabLayout.visibility = View.GONE
                tabLayoutViewPager.beginFakeDrag();
                tabLayoutViewPager.currentItem = 0;


                pref.disableAllTabs = true;


            } else if (data.tabs.todo == "inactive") {
                tabLayout.removeTabAt(1)
            } else if (data.tabs.perks == "inactive") {
                tabLayout.removeTabAt(2)
            } else if (data.tabs.records == "inactive") {
                tabLayout.removeTabAt(3)
            }
        }


        // set settings
        if (data.settings != null) {
            try {
                if (groupId != "user_dashboard" && groupId != "wnw_dashboard") {
                    try {
                        val dashboardSettingsLinearLayout: LinearLayout =
                            activity?.findViewById(R.id.dashboard_settings_liner_layout) as (LinearLayout);
                        dashboardSettingsLinearLayout.setOnClickListener {
                            openBottomSlider(data.settings);
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


        // set page title
        val toolbar: Toolbar? = activity?.findViewById(R.id.toolbar_for_newsidemenu)
        val txtExpertHeading: TextView? = toolbar?.findViewById(R.id.txt_expert_heading)
        if (data.dashboard_title !== null) {
            if (txtExpertHeading != null) {
                txtExpertHeading.text = data.dashboard_title
                txtExpertHeading.setTextColor(resources.getColor(R.color.color_3B3B3B))
            }

        } else {
            if (txtExpertHeading != null) {
                txtExpertHeading.text = ""
            }
        }


        setCardSet1(data.upcoming)
        setCardSet2(data.points)
        setCardSet3(data.cards)
        setCardSet4(data.daily_tip)
    }

    private fun setCardSet1(upcoming: ArrayList<DashboardCardItem>) {
        val adapter: V1DashboardMainCardSliderAdapter =
            V1DashboardMainCardSliderAdapter(context, upcoming)
        adapter.setOnUpComingItemClickListener(this@RevampV1DashboardFragment)
        mainView.mainViewPager.adapter = adapter
        mainView.mainViewPager.pageMargin = 16
        mainView.mainTabDots.setupWithViewPager(mainView.mainViewPager, false)
    }

    private fun setCardSet2(points: V1DashboardPointsData) {
        try {
            mainView.life_points_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val notificationCardItemsArrayList = ArrayList<V1DashboardPointsData>()
            notificationCardItemsArrayList.add(points)
            v1LifePointCardAdapter =
                V1LifePointCardAdapter(
                    requireContext(),
                    notificationCardItemsArrayList,
                    false
                )
            v1LifePointCardAdapter!!.onPointsItemClickListener = this@RevampV1DashboardFragment
            mainView.life_points_recycler_view.adapter = v1LifePointCardAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCardSet3(cardsArrayList: ArrayList<V1DashboardWidgetCard>) {
        mainView.mainViewPager2.removeAllViews()
        val adapter: V1DashboardMainCardSlider2Adapter =
            V1DashboardMainCardSlider2Adapter(
                context,
                cardsArrayList
            )
        adapter.setOnItemClickListener(this@RevampV1DashboardFragment)
        mainView.mainViewPager2.adapter = adapter
        mainView.mainViewPager2.pageMargin = 8
        mainView.mainTabDots2.setupWithViewPager(mainView.mainViewPager2, false)
    }

    private fun setCardSet4(dailyTip: V1DashboardDailyTip) {
        try {
            mainView.cardSet4RecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val v1DashboardDailyTipArrayList = ArrayList<V1DashboardDailyTip>()
            v1DashboardDailyTipArrayList.add(dailyTip)
            v1TipCardAdapter =
                V1TipCardAdapter(
                    requireContext(),
                    v1DashboardDailyTipArrayList,
                    false
                )
            v1TipCardAdapter!!.onDailyTipItemClickListener = this@RevampV1DashboardFragment
            mainView.cardSet4RecyclerView.adapter = v1TipCardAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openBottomSlider(settingItemArrayList: ArrayList<V1DashboardSettingsData>) {

        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.dashboard_bottom_sheet);
        bottomSheetDialog.show();


        val actionsMainLinearLayout: LinearLayout? =
            bottomSheetDialog.findViewById<LinearLayout>(R.id.actions_main_linear_layout)
        actionsMainLinearLayout?.removeAllViews()

        for (item in 0 until settingItemArrayList.size) {
            val scrollSettingItem: V1DashboardSettingsData = settingItemArrayList[item]

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
                try {
                    ActionMetaForFragment().processAction(
                        requireContext(),
                        scrollSettingItem.action, scrollSettingItem.meta
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


            actionsMainLinearLayout?.addView(mainLinearLayout)

        }


    }

    override fun upComingItemClick(
        dashboardCardItemArrayList: ArrayList<DashboardCardItem>,
        card: DashboardCardItem,
        action: String?,
        meta: String?
    ) {
        try {
            if (action!! == "close_notify") {
                setCardSet1(dashboardCardItemArrayList)
                doCloseUpcomingEvent().execute(card.id)
            } else if (action == "todo") {
                changeFragmentInterface = requireContext() as (ChangeFragmentInterface)
                changeFragmentInterface.onGotoToDoClick(action)
            } else {
                ActionMetaForFragment().processAction(requireContext(), action, meta!!)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    internal inner class doCloseUpcomingEvent : AsyncTask<String, Void, Void>() {
        var cardId: String = "";

        override fun doInBackground(vararg params: String?): Void? {
            cardId = params[0].toString()

            val apiService: ApiInterface = ApiClient.getAzureApiClientV3ForDashBoard()
                .create(ApiInterface::class.java)

            val call: Call<GetV1DashboardItemCloseResponse> = apiService.closeUpcomingEvent(
                AppConfig.APP_BRANDING_ID,
                userToken,
                cardId
            )


            call.enqueue(object : Callback<GetV1DashboardItemCloseResponse> {
                override fun onResponse(
                    call: Call<GetV1DashboardItemCloseResponse>,
                    response: Response<GetV1DashboardItemCloseResponse>
                ) {

                }

                override fun onFailure(call: Call<GetV1DashboardItemCloseResponse>, t: Throwable) {

                }


            })

            return null;


        }
    }

    override fun onCardSet2ItemClickListener(action: String?, meta: String?) {
        try {
            ActionMetaForFragment().processAction(requireContext(), action!!, meta!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun pointsItemClick(action: String, meta: String) {
        try {
            ActionMetaForFragment().processAction(requireContext(), action, meta)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dailyTipItemClick(action: String, meta: String) {
        try {
            ActionMetaForFragment().processAction(requireContext(), action, meta)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

