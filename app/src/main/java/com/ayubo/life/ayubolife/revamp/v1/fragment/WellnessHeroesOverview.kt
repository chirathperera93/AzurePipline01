package com.ayubo.life.ayubolife.revamp.v1.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.ActionMetaForFragment
import com.ayubo.life.ayubolife.revamp.v1.adapter.BadgesEarnedAdapter
import com.ayubo.life.ayubolife.revamp.v1.adapter.UpcomingChallengeCardSliderAdapter
import com.ayubo.life.ayubolife.revamp.v1.model.*
import kotlinx.android.synthetic.main.fragment_wellness_heroes_overview.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WellnessHeroesOverview.newInstance] factory method to
 * create an instance of this fragment.
 */
class WellnessHeroesOverview(str1: String) : Fragment(),
    BadgesEarnedAdapter.OnBadgeItemClickListener,
    UpcomingChallengeCardSliderAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mainView: View;
    var ytdId: String = str1
    lateinit var pref: PrefManager

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

        mainView = inflater.inflate(R.layout.fragment_wellness_heroes_overview, container, false)
        pref = PrefManager(context)
        loadData()

        mainView.redeem_point_card.setOnClickListener {
            try {
                ActionMetaForFragment().processAction(requireContext(), "redeem_points", "")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Inflate the layout for this fragment
        return mainView
    }

    private fun loadData() {

        mainView.wellnessHeroesLoading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForDashBoard().create(ApiInterface::class.java);


        val call: Call<GetYTDSummaryResponse> = apiService.getYtdSummary(
            AppConfig.APP_BRANDING_ID,
            pref.userToken,
            ytdId
        )


        call.enqueue(object : Callback<GetYTDSummaryResponse> {
            override fun onResponse(
                call: Call<GetYTDSummaryResponse>,
                response: Response<GetYTDSummaryResponse>
            ) {
                mainView.wellnessHeroesLoading.visibility = View.GONE

                if (response.isSuccessful) {

                    print(response)

                    setDataForBoxes(response.body()!!.data)
                    setBadgesEarnedRecyclerview(response.body()!!.data)
                    setUpcomingChallengesCards(response.body()!!.data)

                    if (response.body()!!.data.bottom_lable.action != "" || response.body()!!.data.bottom_lable.meta != "") {
                        mainView.row5.visibility = View.VISIBLE
                    }

                    mainView.bottom_lable.text = response.body()!!.data.bottom_lable.text
                    mainView.bottom_lable.setTextColor(Color.parseColor(response.body()!!.data.bottom_lable.color))
                    mainView.bottom_lable.setOnClickListener {
                        try {
                            ActionMetaForFragment().processAction(
                                requireContext(),
                                response.body()!!.data.bottom_lable.action,
                                response.body()!!.data.bottom_lable.meta
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }




                    if (response.body()!!.data.offers_button.action != "" || response.body()!!.data.offers_button.meta != "") {
                        mainView.row6.visibility = View.VISIBLE
                    }
                    mainView.offers_button.text = response.body()!!.data.offers_button.text
                    mainView.offers_button.setOnClickListener {
                        try {
                            ActionMetaForFragment().processAction(
                                requireContext(),
                                response.body()!!.data.offers_button.action,
                                response.body()!!.data.offers_button.meta
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    mainView.row7.visibility = View.VISIBLE
                    mainView.disclaimer.text = response.body()!!.data.disclaimer

                } else {
                    Toast.makeText(context, R.string.service_loading_fail, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<GetYTDSummaryResponse>, t: Throwable) {
                mainView.wellnessHeroesLoading.visibility = View.GONE
                Toast.makeText(context, R.string.service_loading_fail, Toast.LENGTH_SHORT).show()

            }


        })
    }

    private fun setDataForBoxes(data: YTDData) {

        mainView.row1.visibility = View.VISIBLE
        mainView.row2.visibility = View.VISIBLE

        mainView.box1_title.text = data.info_box_1.title
        mainView.box1_value.text = data.info_box_1.value

        mainView.box2_title.text = data.info_box_2.title
        mainView.box2_value.text = data.info_box_2.value

        mainView.box3_title.text = data.info_box_3.title
        mainView.box3_value.text = data.info_box_3.value

        mainView.box4_title.text = data.info_box_4.title
        mainView.box4_value_1.text = data.info_box_4.value_1
        mainView.box4_value_2.text = " " + data.info_box_4.value_2

    }

    private fun setBadgesEarnedRecyclerview(data: YTDData) {

        mainView.sub_heading_1.text = data.sub_heading_1

        try {


            mainView.badgesEarnedRecyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


            val badgesEarnedAdapter =
                context?.let {
                    BadgesEarnedAdapter(
                        it,
                        data.badges,
                        false
                    )
                }

            badgesEarnedAdapter!!.onBadgeItemClickListener = this@WellnessHeroesOverview

            mainView.badgesEarnedRecyclerview.adapter = badgesEarnedAdapter


        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpcomingChallengesCards(data: YTDData) {

        mainView.sub_heading_2.text = data.sub_heading_2


        val upcomingChallengeCardSliderAdapter: UpcomingChallengeCardSliderAdapter =
            UpcomingChallengeCardSliderAdapter(context, data.challenges);
        upcomingChallengeCardSliderAdapter.setOnItemClickListener(this@WellnessHeroesOverview)
        mainView.upcomingChallengesViewPager.adapter = upcomingChallengeCardSliderAdapter;
        mainView.upcomingChallengesViewPager.pageMargin = 16;




        mainView.upcomingChallengesTabDots.setupWithViewPager(
            mainView.upcomingChallengesViewPager,
            false
        );
    }


    override fun onBadgeItemClick(action: String, meta: String) {
        try {
            ActionMetaForFragment().processAction(requireContext(), action, meta)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onItemClick(action: String?, meta: String?) {
        try {
            ActionMetaForFragment().processAction(requireContext(), action!!, meta!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}