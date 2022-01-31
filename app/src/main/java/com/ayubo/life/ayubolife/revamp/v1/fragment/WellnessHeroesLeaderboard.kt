package com.ayubo.life.ayubolife.revamp.v1.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.PointUser
import com.ayubo.life.ayubolife.lifeplus.PointsUsersItemAdapter
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.ActionMetaForFragment
import com.ayubo.life.ayubolife.revamp.v1.model.LeaderBoardTabs
import com.ayubo.life.ayubolife.revamp.v1.model.V1GetLeaderboardResponse
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_wellness_heroes_leaderboard.*
import kotlinx.android.synthetic.main.fragment_wellness_heroes_leaderboard.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WellnessHeroesLeaderboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class WellnessHeroesLeaderboard(str2: String) : Fragment(),
    PointsUsersItemAdapter.OnPointUserItemClickListener {

    lateinit var mainView: View;
    lateinit var user1LeaderBoardItem: PointUser
    lateinit var user2LeaderBoardItem: PointUser
    lateinit var user3LeaderBoardItem: PointUser

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var communityKey: String? = str2
    lateinit var pref: PrefManager
    lateinit var appToken: String;
    lateinit var pointsUsersRecyclerViewLayoutManger: LinearLayoutManager;

    var infoAction: String = "";
    var infoMeta: String = "";
    var isLoadTabs: Boolean = false

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

        mainView = inflater.inflate(R.layout.fragment_wellness_heroes_leaderboard, container, false)
        pref = PrefManager(context)
        appToken = pref.userToken;
        loadLeaderboardData()

        mainView.user1.setOnClickListener {
            try {
                ActionMetaForFragment().processAction(
                    requireContext(),
                    user1LeaderBoardItem.action,
                    user1LeaderBoardItem.meta
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mainView.user2.setOnClickListener {
            val x = user2LeaderBoardItem
            print(x)
            try {
                ActionMetaForFragment().processAction(
                    requireContext(),
                    user2LeaderBoardItem.action,
                    user2LeaderBoardItem.meta
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mainView.user3.setOnClickListener {
            try {
                ActionMetaForFragment().processAction(
                    requireContext(),
                    user3LeaderBoardItem.action,
                    user3LeaderBoardItem.meta
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Inflate the layout for this fragment
        return mainView
    }

    private fun loadLeaderboardData() {
        setPointsRecyclerView()
    }

    private fun setPointsRecyclerView() {
        mainView.points_leader_board_loading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);
        val timeZone: TimeZone = TimeZone.getDefault();
        val timeZoneId = timeZone.id;
        val device_modal: String = Build.MODEL;
        val call: Call<V1GetLeaderboardResponse> = apiService.v1NewGetPointsLeaderBoardData(
            AppConfig.APP_BRANDING_ID,
            appToken,
            communityKey,
            "points_leaderboard",
            System.currentTimeMillis(),
            timeZoneId,
            device_modal
        );
        call.enqueue(object : Callback<V1GetLeaderboardResponse> {
            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<V1GetLeaderboardResponse>,
                response: Response<V1GetLeaderboardResponse>
            ) {

                if (response.isSuccessful) {

                    if (response.body()!!.data.tabs != null && !isLoadTabs) {
                        setButtonTypes(response.body()!!.data.tabs)
                        isLoadTabs = true
                    }


                    pointsUsersRecyclerViewLayoutManger =
                        LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)

                    mainView.points_users_recycler_view.layoutManager =
                        pointsUsersRecyclerViewLayoutManger

                    if (response.body()!!.data.more_info.icon_url != "") {
                        Glide.with(requireContext()).load(response.body()!!.data.more_info.icon_url)
                            .into(mainView.info_icon);

                    }

                    infoAction = response.body()!!.data.more_info.action
                    infoMeta = response.body()!!.data.more_info.meta

                    val pointUserArrayList = ArrayList<PointUser>()
                    var count = -1;
                    var setPositionToScroll = 0;

                    if (response.body()!!.data.leaderboard.size > 0) {
                        mainView.gift_layout_text.text = response.body()!!.data.reward.lable

                        if (response.body()!!.data.reward.lable != "") {
                            mainView.gift_layout.visibility = View.VISIBLE
                            if (response.body()!!.data.reward.icon_url != "") {
                                Glide.with(requireContext())
                                    .load(response.body()!!.data.reward.icon_url)
                                    .into(mainView.gift_image);

                            }
                        }


                        mainView.empty_msg.visibility = View.GONE
                        mainView.mainRelativeLayout.visibility = View.VISIBLE
                        mainView.scroll_view_list_linear_layout.visibility = View.VISIBLE


                        try {
                            for (leaderBoardItem in response.body()!!.data.leaderboard) {
                                val pos = leaderBoardItem.position
                                if (pos != 1 && pos != 2 && pos != 3) {

                                    count += 1;

                                    if (leaderBoardItem.me) {
                                        setPositionToScroll = count;
                                    }

                                    pointUserArrayList.add(leaderBoardItem)
                                } else {

                                    val pattern: String = "###,###,###";
                                    val decimalFormat: DecimalFormat = DecimalFormat(pattern);
                                    val stepsValueFormat: String =
                                        decimalFormat.format(leaderBoardItem.points);

                                    if (pos == 1) {
                                        user1LeaderBoardItem = leaderBoardItem

                                        Glide.with(requireContext())
                                            .load(leaderBoardItem.image_url)
                                            .into(mainView.user1_image)

                                        user1_name.text = leaderBoardItem.first_name

                                        Glide.with(requireContext())
                                            .load(response.body()!!.data.challenge_icon)
                                            .into(mainView.user1_point_image)
//                                        ) {
                                        mainView.main_user1_manual_steps_label.visibility =
                                            View.VISIBLE


                                        if (leaderBoardItem.record_label.icon_url != "") {
                                            Glide.with(requireContext())
                                                .load(
                                                    leaderBoardItem.record_label.icon_url
                                                )
                                                .into(mainView.user1_manual_step_icon)
                                        }


                                        if (leaderBoardItem.record_label.text != "") {
                                            mainView.user1_manual_step_label.text =
                                                leaderBoardItem.record_label.text
                                        }

                                        if (leaderBoardItem.record_label.color != "") {
                                            mainView.user1_manual_step_label.setTextColor(
                                                Color.parseColor(leaderBoardItem.record_label.color)
                                            )
                                        }

                                        mainView.user1_point_value.text = stepsValueFormat;
                                        mainView.user1_image_position.visibility = View.VISIBLE
                                    } else if (pos == 2) {
                                        user2LeaderBoardItem = leaderBoardItem
                                        Glide.with(requireContext()).load(leaderBoardItem.image_url)
                                            .into(mainView.user2_image)
                                        mainView.user2_name.text = leaderBoardItem.first_name

                                        Glide.with(requireContext())
                                            .load(response.body()!!.data.challenge_icon)
                                            .into(mainView.user2_point_image)
                                        mainView.main_user2_manual_steps_label.visibility =
                                            View.VISIBLE

                                        if (leaderBoardItem.record_label.icon_url != "") {
                                            Glide.with(requireContext())
                                                .load(leaderBoardItem.record_label.icon_url)
                                                .into(mainView.user2_manual_step_icon)
                                        }


                                        if (leaderBoardItem.record_label.text != "") {
                                            mainView.user2_manual_step_label.text =
                                                leaderBoardItem.record_label.text
                                        }


                                        if (leaderBoardItem.record_label.color != "") {
                                            mainView.user2_manual_step_label.setTextColor(
                                                Color.parseColor(leaderBoardItem.record_label.color)
                                            )
                                        }

                                        mainView.user2_point_value.text = stepsValueFormat;
                                        mainView.user2_image_position.visibility = View.VISIBLE
                                    } else if (pos == 3) {
                                        user3LeaderBoardItem = leaderBoardItem
                                        Glide.with(requireContext())
                                            .load(leaderBoardItem.image_url)
                                            .into(mainView.user3_image)
                                        mainView.user3_name.text = leaderBoardItem.first_name
                                        Glide.with(requireContext())
                                            .load(response.body()!!.data.challenge_icon)
                                            .into(mainView.user3_point_image)
                                        mainView.main_user3_manual_steps_label.visibility =
                                            View.VISIBLE

                                        if (leaderBoardItem.record_label.icon_url != "") {
                                            Glide.with(requireContext())
                                                .load(leaderBoardItem.record_label.icon_url)
                                                .into(mainView.user3_manual_step_icon)
                                        }


                                        if (leaderBoardItem.record_label.text != "") {
                                            mainView.user3_manual_step_label.text =
                                                leaderBoardItem.record_label.text

                                        }


                                        if (leaderBoardItem.record_label.color != "") {
                                            mainView.user3_manual_step_label.setTextColor(
                                                Color.parseColor(leaderBoardItem.record_label.color)
                                            )
                                        }
                                        mainView.user3_point_value.text = stepsValueFormat;
                                        mainView.user3_image_position.visibility = View.VISIBLE
                                    }

                                }


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        mainView.empty_msg.visibility = View.VISIBLE
                        mainView.mainRelativeLayout.visibility = View.GONE
                        mainView.gift_layout.visibility = View.GONE
                        mainView.scroll_view_list_linear_layout.visibility = View.GONE
                        mainView.empty_main_header.text = response.body()!!.data.empty_page.title
                        mainView.empty_sub_header.text =
                            response.body()!!.data.empty_page.description

                    }


                    val pointsUsersItemAdapter = PointsUsersItemAdapter(
                        requireContext(),
                        pointUserArrayList,
                        this@WellnessHeroesLeaderboard
                    )
                    mainView.points_users_recycler_view.adapter = pointsUsersItemAdapter

                    pointsUsersRecyclerViewLayoutManger.scrollToPositionWithOffset(
                        setPositionToScroll,
                        pointUserArrayList.size
                    );



                    mainView.leader_board_topic.text = response.body()!!.data.community_name
                    Glide.with(requireContext())
                        .load(response.body()!!.data.backgroud_image).centerCrop()
                        .into(mainView.main_background_image_card)
                    mainView.points_leader_board_loading.visibility = View.GONE
                } else {
                    mainView.points_leader_board_loading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT)
                        .show()

                }


            }

            override fun onFailure(call: Call<V1GetLeaderboardResponse>, t: Throwable) {
                mainView.points_leader_board_loading.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed loading data", Toast.LENGTH_SHORT).show()
            }


        });


    }

    private fun setButtonTypes(tabs: ArrayList<LeaderBoardTabs>?) {

        print(tabs)
        if (tabs != null) {
            if (tabs.size == 3) {
                mainView.tab1.visibility = View.VISIBLE
                mainView.tab1.text = tabs[0].name


                mainView.tab2.visibility = View.VISIBLE
                mainView.tab2.text = tabs[1].name


                mainView.tab3.visibility = View.VISIBLE
                mainView.tab3.text = tabs[2].name
            }


            if (tabs.size == 2) {
                mainView.tab1.visibility = View.VISIBLE
                mainView.tab1.text = tabs[0].name


                mainView.tab2.visibility = View.VISIBLE
                mainView.tab2.text = tabs[1].name
            }

            mainView.tab1.setBackgroundResource(R.drawable.button_outline_select)
            mainView.tab1.setTextColor(resources.getColor(R.color.color_4B4C4A))
            mainView.tab1.setOnClickListener {
                mainView.tab1.setBackgroundResource(R.drawable.button_outline_select)
                mainView.tab1.setTextColor(resources.getColor(R.color.color_4B4C4A))

                mainView.tab2.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab2.setTextColor(resources.getColor(R.color.white))

                mainView.tab3.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab3.setTextColor(resources.getColor(R.color.white))

                selectTab(tabs[0])
            }


            mainView.tab2.setOnClickListener {
                mainView.tab2.setBackgroundResource(R.drawable.button_outline_select)
                mainView.tab2.setTextColor(resources.getColor(R.color.color_4B4C4A))

                mainView.tab1.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab1.setTextColor(resources.getColor(R.color.white))

                mainView.tab3.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab3.setTextColor(resources.getColor(R.color.white))

                selectTab(tabs[1])
            }


            mainView.tab3.setOnClickListener {
                mainView.tab3.setBackgroundResource(R.drawable.button_outline_select)
                mainView.tab3.setTextColor(resources.getColor(R.color.color_4B4C4A))

                mainView.tab1.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab1.setTextColor(resources.getColor(R.color.white))

                mainView.tab2.setBackgroundResource(R.drawable.button_outline_deselect)
                mainView.tab2.setTextColor(resources.getColor(R.color.white))

                selectTab(tabs[2])
            }

        }


    }

    private fun selectTab(leaderBoardTab: LeaderBoardTabs) {
        print(leaderBoardTab)
        communityKey = leaderBoardTab.challenge_id
        setPointsRecyclerView()


    }

    override fun onItemClick(pointUser: PointUser) {
        print(pointUser)

//        points_leaderboard

        try {
            ActionMetaForFragment().processAction(
                requireContext(),
                pointUser.action,
                pointUser.meta
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}