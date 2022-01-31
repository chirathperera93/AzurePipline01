package com.ayubo.life.ayubolife.lifeplus

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.payment.EXTRA_LEADER_BOARD_COMMUNITY
import com.ayubo.life.ayubolife.payment.EXTRA_LEADER_BOARD_TYPE
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dashboard_welcome_card.view.*
import kotlinx.android.synthetic.main.points_leaderboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.TimeZone

class PointsLeaderboardActivity : BaseActivity(),
    PointsUsersItemAdapter.OnPointUserItemClickListener {

    lateinit var pref: PrefManager;
    lateinit var appToken: String;
    var type: String = "";
    var community: String = "";
    var infoAction: String = "";
    var infoMeta: String = "";
    lateinit var pointsUsersRecyclerViewLayoutManger: LinearLayoutManager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.points_leaderboard)

        pref = PrefManager(this);
        appToken = pref.getUserToken();

        empty_msg.visibility = View.GONE
        readExtras();


        points_leader_board_loading.visibility = View.VISIBLE

        setPointsRecyclerView();

        page_back_btn.setOnClickListener {
            finish();
        }

        info_icon.setOnClickListener {
            processAction(infoAction, infoMeta)
        }
    }

    private fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_LEADER_BOARD_COMMUNITY) && bundle.containsKey(
                EXTRA_LEADER_BOARD_TYPE
            )
        ) {
            community = bundle.getSerializable(EXTRA_LEADER_BOARD_COMMUNITY) as String
            type = bundle.getSerializable(EXTRA_LEADER_BOARD_TYPE) as String

        }
    }

    private fun setPointsRecyclerView() {
        val apiService: ApiInterface =
            ApiClient.getBaseApiClientNew().create(ApiInterface::class.java);
        val timeZone: TimeZone = TimeZone.getDefault();
        val timeZoneId = timeZone.getID();
        val device_modal: String = Build.MODEL;
        val call: Call<ProfileDashboardResponseData> = apiService.getPointsLeaderBoardData(
            AppConfig.APP_BRANDING_ID,
            appToken,
            community,
            type,
            System.currentTimeMillis(),
            timeZoneId,
            device_modal
        );
        call.enqueue(object : Callback<ProfileDashboardResponseData> {
            @SuppressLint("WrongConstant")
            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {

                if (response.isSuccessful) {
                    val leaderBoardMainData: JsonObject =
                        Gson().toJsonTree(response.body()!!.data).asJsonObject;
                    val leaderBoard: JsonArray = leaderBoardMainData.get("leaderboard").asJsonArray
                    pointsUsersRecyclerViewLayoutManger =
                        LinearLayoutManager(baseContext, LinearLayout.VERTICAL, false)
                    points_users_recycler_view.layoutManager = pointsUsersRecyclerViewLayoutManger


                    val moreInfoJsonObject: JsonObject =
                        leaderBoardMainData.get("more_info").asJsonObject

                    val rewardJsonObject: JsonObject =
                        leaderBoardMainData.get("reward").asJsonObject

                    if (!moreInfoJsonObject.get("icon_url").asString.equals("")) {
                        Glide.with(baseContext).load(moreInfoJsonObject.get("icon_url").asString)
                            .into(info_icon);

                    }

                    infoAction = moreInfoJsonObject.get("action").asString
                    infoMeta = moreInfoJsonObject.get("meta").asString


                    if (!rewardJsonObject.get("icon_url").asString.equals("")) {
                        Glide.with(baseContext).load(rewardJsonObject.get("icon_url").asString)
                            .into(gift_image);

                    }


                    val pointUserArrayList = ArrayList<PointUser>()
                    var count = -1;
                    var setPositionToScroll = 0;
                    if (leaderBoard.size() > 0) {
                        gift_layout_text.setText(rewardJsonObject.get("lable").asString)

                        if (!rewardJsonObject.get("lable").asString.equals("")) {
                            gift_layout.visibility = View.VISIBLE
                        }
                        empty_msg.visibility = View.GONE
                        mainRelativeLayout.visibility = View.VISIBLE
                        scroll_view_list_linear_layout.visibility = View.VISIBLE
                        try {
                            for (leaderBoardItem in leaderBoard) {
                                val pos = leaderBoardItem.asJsonObject.get("position").asInt
                                if (pos != 1 && pos != 2 && pos != 3) {

                                    count += 1;

                                    if (leaderBoardItem.asJsonObject.get("me").asBoolean) {
                                        setPositionToScroll = count;
                                    }

                                    pointUserArrayList.add(
                                        PointUser(
                                            leaderBoardItem.asJsonObject.get("points").asInt,
                                            leaderBoardItem.asJsonObject.get("points_label").asString,
                                            leaderBoardItem.asJsonObject.get("position").asInt,
                                            leaderBoardItem.asJsonObject.get("user_id").asString,
                                            leaderBoardItem.asJsonObject.get("last_updated_time").asLong,
                                            leaderBoardItem.asJsonObject.get("me").asBoolean,
                                            if (leaderBoardItem.asJsonObject.get("first_name") == null) "" else leaderBoardItem.asJsonObject.get(
                                                "first_name"
                                            ).asString,
                                            if (leaderBoardItem.asJsonObject.get("last_name") == null) "" else leaderBoardItem.asJsonObject.get(
                                                "last_name"
                                            ).asString,
                                            leaderBoardItem.asJsonObject.get("image_url").asString,
                                            if (leaderBoardItem.asJsonObject.get("manual_steps") == null) false else leaderBoardItem.asJsonObject.get(
                                                "manual_steps"
                                            ).asBoolean,
                                            RecordLabel(
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "text"
                                                ).asString,
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "icon_url"
                                                ).asString,
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "color"
                                                ).asString

                                            )
                                        )

                                    );
                                } else {

                                    val pattern: String = "###,###,###";
                                    val decimalFormat: DecimalFormat = DecimalFormat(pattern);
                                    val stepsValueFormat: String =
                                        decimalFormat.format(leaderBoardItem.asJsonObject.get("points").asInt);

                                    if (pos == 1) {
//                                        main_user3_manual_steps_label
                                        Glide.with(baseContext)
                                            .load(leaderBoardItem.asJsonObject.get("image_url").asString)
                                            .into(user1_image)
                                        user1_name.text =
                                            leaderBoardItem.asJsonObject.get("first_name").asString
                                        Glide.with(baseContext)
                                            .load(leaderBoardMainData.get("challenge_icon").asString)
                                            .into(user1_point_image)

//                                        if (leaderBoardItem.asJsonObject.get("manual_steps") != null
//                                            &&
//                                            leaderBoardItem.asJsonObject.get("manual_steps").asBoolean
//                                        ) {
                                        main_user1_manual_steps_label.visibility = View.VISIBLE


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "icon_url"
                                            ).asString != ""
                                        ) {
                                            Glide.with(baseContext)
                                                .load(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "icon_url"
                                                    ).asString
                                                )
                                                .into(user1_manual_step_icon)
                                        }


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "text"
                                            ).asString != ""
                                        ) {
                                            user1_manual_step_label.text =
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "text"
                                                ).asString
                                        }

                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "color"
                                            ).asString != ""
                                        ) {
                                            user1_manual_step_label.setTextColor(
                                                Color.parseColor(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "color"
                                                    ).asString
                                                )
                                            )
                                        }


//                                        }
//                                    user1_point_value.text =
//                                        leaderBoardItem.asJsonObject.get("points").asInt.toString() + " " + leaderBoardItem.asJsonObject.get(
//                                            "points_label"
//                                        ).asString;

                                        user1_point_value.text = stepsValueFormat;
                                        user1_image_position.visibility = View.VISIBLE
                                    } else if (pos == 2) {
                                        Glide.with(baseContext)
                                            .load(leaderBoardItem.asJsonObject.get("image_url").asString)
                                            .into(user2_image)
                                        user2_name.text =
                                            leaderBoardItem.asJsonObject.get("first_name").asString
                                        Glide.with(baseContext)
                                            .load(leaderBoardMainData.get("challenge_icon").asString)
                                            .into(user2_point_image)
//                                    user2_point_value.text =
//                                        leaderBoardItem.asJsonObject.get("points").asInt.toString() + " " + leaderBoardItem.asJsonObject.get(
//                                            "points_label"
//                                        ).asString;

//                                        if (leaderBoardItem.asJsonObject.get("manual_steps") != null
//                                            &&
//                                            leaderBoardItem.asJsonObject.get("manual_steps").asBoolean
//                                        ) {
                                        main_user2_manual_steps_label.visibility = View.VISIBLE

                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "icon_url"
                                            ).asString != ""
                                        ) {
                                            Glide.with(baseContext)
                                                .load(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "icon_url"
                                                    ).asString
                                                )
                                                .into(user2_manual_step_icon)
                                        }


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "text"
                                            ).asString != ""
                                        ) {
                                            user2_manual_step_label.text =
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "text"
                                                ).asString
                                        }


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "color"
                                            ).asString != ""
                                        ) {
                                            user2_manual_step_label.setTextColor(
                                                Color.parseColor(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "color"
                                                    ).asString
                                                )
                                            )
                                        }


//                                        }

                                        user2_point_value.text = stepsValueFormat;
                                        user2_image_position.visibility = View.VISIBLE
                                    } else if (pos == 3) {
                                        Glide.with(baseContext)
                                            .load(leaderBoardItem.asJsonObject.get("image_url").asString)
                                            .into(user3_image)
                                        user3_name.text =
                                            leaderBoardItem.asJsonObject.get("first_name").asString
                                        Glide.with(baseContext)
                                            .load(leaderBoardMainData.get("challenge_icon").asString)
                                            .into(user3_point_image)
//                                    user3_point_value.text =
//                                        leaderBoardItem.asJsonObject.get("points").asInt.toString() + " " + leaderBoardItem.asJsonObject.get(
//                                            "points_label"
//                                        ).asString;

//                                        if (leaderBoardItem.asJsonObject.get("manual_steps") != null
//                                            &&
//                                            leaderBoardItem.asJsonObject.get("manual_steps").asBoolean
//                                        ) {
                                        main_user3_manual_steps_label.visibility = View.VISIBLE

                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "icon_url"
                                            ).asString != ""
                                        ) {
                                            Glide.with(baseContext)
                                                .load(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "icon_url"
                                                    ).asString
                                                )
                                                .into(user3_manual_step_icon)
                                        }


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "text"
                                            ).asString != ""
                                        ) {
                                            user3_manual_step_label.text =
                                                leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                    "text"
                                                ).asString
                                        }


                                        if (leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                "color"
                                            ).asString != ""
                                        ) {
                                            user3_manual_step_label.setTextColor(
                                                Color.parseColor(
                                                    leaderBoardItem.asJsonObject.get("record_label").asJsonObject.get(
                                                        "color"
                                                    ).asString
                                                )
                                            )
                                        }

//                                        }
                                        user3_point_value.text = stepsValueFormat;
                                        user3_image_position.visibility = View.VISIBLE
                                    }

                                }


                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    } else {
                        empty_msg.visibility = View.VISIBLE
                        mainRelativeLayout.visibility = View.GONE
                        gift_layout.visibility = View.GONE
                        scroll_view_list_linear_layout.visibility = View.GONE
                        val emptyPage: JsonObject =
                            leaderBoardMainData.get("empty_page").asJsonObject
                        empty_main_header.text = emptyPage.get("title").asString
                        empty_sub_header.text = emptyPage.get("description").asString

                    }


                    val pointsUsersItemAdapter = PointsUsersItemAdapter(
                        baseContext,
                        pointUserArrayList,
                        this@PointsLeaderboardActivity
                    )
                    points_users_recycler_view.adapter = pointsUsersItemAdapter

//                    pointsUsersRecyclerViewLayoutManger.scrollToPosition(setPositionToScroll);
                    pointsUsersRecyclerViewLayoutManger.scrollToPositionWithOffset(
                        setPositionToScroll,
                        pointUserArrayList.size
                    );



                    leader_board_topic.text = leaderBoardMainData.get("community_name").asString
                    Glide.with(baseContext)
                        .load(leaderBoardMainData.get("backgroud_image").asString).centerCrop()
                        .into(main_background_image_card)
//                    main_background_image_card.layoutParams.height = mainRelativeLayout.height
                    points_leader_board_loading.visibility = View.GONE
                } else {
                    points_leader_board_loading.visibility = View.GONE
                    showMessage("Failed loading data")
                }


            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, t: Throwable) {
                points_leader_board_loading.visibility = View.GONE
                showMessage("Failed loading data")
            }


        });


    }

    override fun onItemClick(pointUser: PointUser) {
        System.out.println(pointUser)
    }


}
