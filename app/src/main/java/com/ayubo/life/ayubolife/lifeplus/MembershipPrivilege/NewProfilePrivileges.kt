package com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.activity.VisitDoctorActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.common.SetDiscoverPage
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity
import com.ayubo.life.ayubolife.health.OMCommon
import com.ayubo.life.ayubolife.health.OMMainPage
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.lifeplus.DiscoverToNewDashboard
import com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide.CardFragmentPagerAdapter
import com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide.CardItem
import com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide.CardPagerAdapter
import com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege.CardSlide.ShadowTransformer
import com.ayubo.life.ayubolife.login.model.MembershipCardResponse
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
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
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.WalkWinMainActivity
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_walk_win_claim_award.*
import kotlinx.android.synthetic.main.activity_walk_win_claim_award.view.*
import kotlinx.android.synthetic.main.new_profile_privileges.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewProfilePrivileges.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewProfilePrivileges : Fragment(), CompoundButton.OnCheckedChangeListener,
    PrivilegeRecyclerAdapter.OnPrivilegeItemClickListner {

    private var param1: String? = null
    private var param2: String? = null


    private lateinit var mViewPager: ViewPager;

    private lateinit var mCardAdapter: CardPagerAdapter;
    private lateinit var mCardShadowTransformer: ShadowTransformer;
    private lateinit var mFragmentCardAdapter: CardFragmentPagerAdapter;
    private lateinit var mFragmentCardShadowTransformer: ShadowTransformer;
    private lateinit var profile_privilege_loading: ProgressAyubo;
    private lateinit var terms_and_condition_text_view: TextView;
    private lateinit var privilege_support_btn: ImageView;
    private lateinit var profile_privilege_topic: TextView;
    private lateinit var profile_privilege_card_detail: TextView;
    private lateinit var privilege_empty_linear_layout: LinearLayout;
    lateinit var groupId: String;
    private var dotsCount: Int = 0;
    lateinit var sliderDots: LinearLayout;
    lateinit var pref: PrefManager;
    lateinit var appToken: String;
    lateinit var termsAction: String;
    lateinit var termsMeta: String;
    lateinit var supoortAction: String;
    lateinit var supoortTermsMeta: String;
    lateinit var mainView: View;
    lateinit var scroll_view_list_linear_layout: LinearLayout;
    lateinit var slidingup_layout: SlidingUpPanelLayout;
    lateinit var card_number: TextView;
    lateinit var swipe_up_bar: ImageView;
    lateinit var mainRelativeLayout: RelativeLayout;

    var dots: ArrayList<ImageView> = ArrayList<ImageView>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun onProcessAction(action: String, meta: String) {
        processAction(action, meta)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        mainView = inflater.inflate(R.layout.fragment_new_profile_privileges, container, false);
        mainView = inflater.inflate(R.layout.new_profile_privileges, container, false);

        pref = PrefManager(getContext());

        mViewPager = mainView.findViewById(R.id.viewPagerPrivileges);
        mViewPager.removeAllViews();
        profile_privilege_topic = mainView.findViewById(R.id.profile_privilege_topic);
        slidingup_layout = mainView.findViewById(R.id.slidingup_layout);
        profile_privilege_card_detail = mainView.findViewById(R.id.profile_privilege_card_detail);
        swipe_up_bar = mainView.findViewById(R.id.swipe_up_bar);
        scroll_view_list_linear_layout = mainView.findViewById(R.id.scroll_view_list_linear_layout);
        card_number = mainView.findViewById(R.id.card_number);
        sliderDots = mainView.findViewById(R.id.SliderDots) as LinearLayout;
        sliderDots.removeAllViews();
        profile_privilege_topic.visibility = View.GONE
        profile_privilege_card_detail.visibility = View.GONE
        swipe_up_bar.visibility = View.GONE
        profile_privilege_loading = mainView.findViewById(R.id.profile_privilege_loading);
        terms_and_condition_text_view = mainView.findViewById(R.id.terms_and_condition_text_view);
        privilege_support_btn = mainView.findViewById(R.id.privilege_support_btn);
        mainRelativeLayout = mainView.findViewById(R.id.mainRelativeLayout);
        privilege_empty_linear_layout =
            mainView.findViewById(R.id.privilege_empty_linear_layout) as LinearLayout
        privilege_support_btn.visibility = View.GONE
        terms_and_condition_text_view.visibility = View.GONE
//        profile_privilege_loading.visibility = View.VISIBLE

        mCardAdapter = CardPagerAdapter(context);


        getMembershipCards();

        terms_and_condition_text_view.setOnClickListener {
            onProcessAction(termsAction, termsMeta);
        }

        privilege_support_btn.setOnClickListener {
            onProcessAction(supoortAction, supoortTermsMeta);
        }

        return mainView
    }


//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        isVisibleView = isVisibleToUser;
//        getMembershipCards()
//    }

    fun getMembershipCards() {
//        if (isVisibleView) {
        termsAction = "";
        termsMeta = "";
        supoortAction = "";
        supoortTermsMeta = "";
        appToken = pref.getUserToken();

        val getNewDashBoardData = pref.newDashBoardData;

        if (getNewDashBoardData.equals("")) {
            groupId = "user_dashboard"
        } else {
            groupId = getNewDashBoardData;
        }
        privilege_empty_linear_layout.visibility = View.GONE

        profile_privilege_topic.visibility = View.GONE
        profile_privilege_card_detail.visibility = View.GONE
        swipe_up_bar.visibility = View.GONE
        privilege_support_btn.visibility = View.GONE
        terms_and_condition_text_view.visibility = View.GONE

        profile_privilege_loading.visibility = View.VISIBLE
        val apiService: ApiInterface = ApiClient.getNewApiClient().create(ApiInterface::class.java);
        val call: Call<MembershipCardResponse> =
            apiService.getMembershipCards(appToken, AppConfig.APP_BRANDING_ID, groupId);
        call.enqueue(object : Callback<MembershipCardResponse> {

            override fun onResponse(
                call: Call<MembershipCardResponse>,
                response: Response<MembershipCardResponse>
            ) {
                val status: Boolean = response.isSuccessful;
                profile_privilege_loading.visibility = View.GONE
                privilege_empty_linear_layout.visibility = View.GONE
                if (status) {
                    if (response.body() != null) {
                        if (response.body()!!.data.size > 0) {
                            startGif5()


                            slidingup_layout!!.addPanelSlideListener(object :
                                SlidingUpPanelLayout.PanelSlideListener {
                                override fun onPanelSlide(panel: View, slideOffset: Float) {

                                    if (0.70 < slideOffset) {
                                        card_number.visibility = View.VISIBLE
                                        System.out.println("img_btn_down_arrow visible")
                                    } else {
                                        card_number.visibility = View.GONE
                                        System.out.println("img_btn_down_arrow gone")


//
                                    }

                                }

                                override fun onPanelStateChanged(
                                    panel: View?,
                                    previousState: SlidingUpPanelLayout.PanelState?,
                                    newState: SlidingUpPanelLayout.PanelState?
                                ) {

                                }
                            }
                            )


                            profile_privilege_loading.visibility = View.GONE
                            terms_and_condition_text_view.visibility = View.VISIBLE
                            profile_privilege_topic.visibility = View.VISIBLE
                            profile_privilege_card_detail.visibility = View.VISIBLE
                            swipe_up_bar.visibility = View.VISIBLE
                            privilege_support_btn.visibility = View.VISIBLE
                            terms_and_condition_text_view.visibility = View.VISIBLE


                            val membershipCardsJsonArray: JsonArray =
                                Gson().toJsonTree(response.body()!!.data).getAsJsonArray();


                            if (membershipCardsJsonArray.size() > 0) {
                                profile_privilege_topic.visibility = View.VISIBLE
                                profile_privilege_card_detail.visibility = View.VISIBLE
                                swipe_up_bar.visibility = View.VISIBLE
                                privilege_support_btn.visibility = View.VISIBLE
                                terms_and_condition_text_view.visibility = View.VISIBLE
                                for (i in 0 until membershipCardsJsonArray.size()) {
                                    val membershipCard = membershipCardsJsonArray.get(i)
                                    mCardAdapter.addCardItem(
                                        CardItem(
                                            membershipCard.asJsonObject.get("card_id").asString,
                                            membershipCard.asJsonObject.get("background_image").asString,
                                            membershipCard.asJsonObject.get("title").asString,
                                            membershipCard.asJsonObject.get("type").asString,
                                            membershipCard.asJsonObject.get("Benifits").asJsonArray,
                                            membershipCard.asJsonObject.get("support").asJsonObject,
                                            membershipCard.asJsonObject.get("terms").asJsonObject,
                                            membershipCard.asJsonObject.get("expire_date").asString,
                                            membershipCard.asJsonObject.get("full_name").asString,
                                            membershipCard.asJsonObject.get("main_logo").asString,
                                            membershipCard.asJsonObject.get("sponsor_logo").asString
                                        )
                                    );


                                }


                                mFragmentCardAdapter = context?.let { dpToPixels(2, it) }?.let {
                                    CardFragmentPagerAdapter(
                                        activity?.supportFragmentManager,
                                        it
                                    )
                                }!!;

                                mCardShadowTransformer =
                                    ShadowTransformer(mViewPager, mCardAdapter);
                                mFragmentCardShadowTransformer =
                                    ShadowTransformer(mViewPager, mFragmentCardAdapter);

                                mViewPager.setAdapter(mCardAdapter);
                                mViewPager.setPageTransformer(false, mCardShadowTransformer);

                                dotsCount = mCardAdapter.getCount();
                                sliderDots.removeAllViewsInLayout()
                                dots = ArrayList<ImageView>()

                                for (i in 0 until dotsCount) {
                                    dots.add(ImageView(context))
                                    dots.get(i).setImageDrawable(
                                        ContextCompat.getDrawable(
                                            context!!,
                                            R.drawable.nonactive_dot
                                        )
                                    );
                                    val params: LinearLayout.LayoutParams =
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                    params.setMargins(8, 0, 8, 0);
                                    val imageView: ImageView = dots.get(i);
                                    sliderDots.addView(imageView, params);
                                }

                                dots.get(0).setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context!!,
                                        R.drawable.active_dot
                                    )
                                );

                                val membershipDetailCardArrayList: ArrayList<MembershipDetailCard> =
                                    ArrayList<MembershipDetailCard>(0)

                                for (i in 0 until mCardAdapter.getCardItem(0).benefits.size()) {
                                    val item = mCardAdapter.getCardItem(0).benefits.get(i)
                                    membershipDetailCardArrayList.add(
                                        MembershipDetailCard(
                                            item.asJsonObject.get("heading").asString,
                                            item.asJsonObject.get("text").asString,
                                            item.asJsonObject.get("action").asString,
                                            item.asJsonObject.get("meta").asString
                                        )
                                    )
                                }

                                val recyclerView =
                                    mainView.findViewById(R.id.membership_card_details_recycler_view) as RecyclerView
                                recyclerView.removeAllViews()
                                recyclerView.layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                val adapter = PrivilegeRecyclerAdapter(
                                    membershipDetailCardArrayList,
                                    this@NewProfilePrivileges
                                )
                                recyclerView.adapter = adapter


                                val terms = mCardAdapter.getCardItem(0).terms.asJsonObject;
                                val support = mCardAdapter.getCardItem(0).support.asJsonObject;
                                termsAction = terms.get("action").asString
                                termsMeta = terms.get("meta").asString
                                supoortAction = support.get("action").asString
                                supoortTermsMeta = support.get("meta").asString

                                card_number.setText(mCardAdapter.getCardItem(0).cardId)

                                mViewPager.addOnPageChangeListener(object :
                                    ViewPager.OnPageChangeListener {
                                    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                                    }

                                    override fun onPageSelected(position: Int) {
                                        termsAction = "";
                                        termsMeta = "";


                                        for (i in 0 until dotsCount) {
                                            dots.get(i).setImageDrawable(
                                                ContextCompat.getDrawable(
                                                    context!!,
                                                    R.drawable.nonactive_dot
                                                )
                                            );
                                        }

                                        dots.get(position).setImageDrawable(
                                            ContextCompat.getDrawable(
                                                context!!,
                                                R.drawable.active_dot
                                            )
                                        );

                                        val membershipDetailCardArrayList: ArrayList<MembershipDetailCard> =
                                            ArrayList<MembershipDetailCard>(0)
                                        for (i in 0 until mCardAdapter.getCardItem(position).benefits.size()) {
                                            val item =
                                                mCardAdapter.getCardItem(position).benefits.get(i)
                                            membershipDetailCardArrayList.add(
                                                MembershipDetailCard(
                                                    item.asJsonObject.get("heading").asString,
                                                    item.asJsonObject.get("text").asString,
                                                    item.asJsonObject.get("action").asString,
                                                    item.asJsonObject.get("meta").asString
                                                )
                                            )

                                        }


                                        val recyclerView =
                                            mainView.findViewById(R.id.membership_card_details_recycler_view) as RecyclerView
                                        recyclerView.removeAllViews()
                                        recyclerView.layoutManager = LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                        val adapter = PrivilegeRecyclerAdapter(
                                            membershipDetailCardArrayList,
                                            this@NewProfilePrivileges
                                        )
                                        recyclerView.adapter = adapter

                                        card_number.setText(mCardAdapter.getCardItem(position).cardId)

                                        termsAction =
                                            mCardAdapter.getCardItem(position).terms.asJsonObject.get(
                                                "action"
                                            ).asString
                                        termsMeta =
                                            mCardAdapter.getCardItem(position).terms.asJsonObject.get(
                                                "meta"
                                            ).asString

                                        supoortAction =
                                            mCardAdapter.getCardItem(position).support.asJsonObject.get(
                                                "action"
                                            ).asString
                                        supoortTermsMeta =
                                            mCardAdapter.getCardItem(position).support.asJsonObject.get(
                                                "meta"
                                            ).asString

                                    }

                                    override fun onPageScrollStateChanged(p0: Int) {
                                    }

                                })


                                val mainRelativeLayoutHeight = mainRelativeLayout.height


                                val displayMetrics: DisplayMetrics = DisplayMetrics();
                                activity?.windowManager?.getDefaultDisplay()
                                    ?.getMetrics(displayMetrics);
                                val height = displayMetrics.heightPixels;
                                val toolbar: Toolbar? =
                                    activity?.findViewById(R.id.toolbar_for_newsidemenu);
                                val tabLayoutMain: TabLayout? =
                                    activity?.findViewById(R.id.tabLayoutMain);

                                slidingup_layout.panelHeight =
                                    mainRelativeLayoutHeight - (mViewPager.height + toolbar!!.height + profile_privilege_topic.height + tabLayoutMain!!.height);
                            } else {
                                privilege_empty_linear_layout.visibility = View.VISIBLE

                            }


                        } else {
                            privilege_empty_linear_layout.visibility = View.VISIBLE
                        }


                    }
                }
            }

            override fun onFailure(call: Call<MembershipCardResponse>, t: Throwable) {
                profile_privilege_loading.visibility = View.GONE
                profile_privilege_topic.visibility = View.GONE
                profile_privilege_card_detail.visibility = View.GONE
                swipe_up_bar.visibility = View.GONE
                privilege_support_btn.visibility = View.GONE
                terms_and_condition_text_view.visibility = View.GONE
            }


        });
//        }

    }

    fun startGif5() {


        requireActivity().runOnUiThread(Runnable {

            val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
            valueAnimator.addUpdateListener {
                val value = it.animatedValue as Float
                scroll_view_list_linear_layout.translationY = value
            }
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.duration = 3000
            valueAnimator.start()
        })

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewProfilePrivileges.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewProfilePrivileges().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun dpToPixels(dp: Int, context: Context): Float {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    override fun onCheckedChanged(p0: CompoundButton?, b: Boolean) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    override fun onItemClick(item: MembershipDetailCard, position: Int) {
        onProcessAction(item.action, item.meta);


    }

    fun processAction(action: String, meta: String) {

        Log.d("====action====", action)
        Log.d("====meta====", meta)

        if (action == "treasure") {
            onTreasureClick(meta)
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
            onPayNowClick(meta)
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
        }


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
        val intent = SetDiscoverPage().getDiscoverIntent(requireContext());
        intent.putExtra("isFromSearchResults", false)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun goToNewDashboard(meta: String) {
        val intent = Intent(context, DiscoverToNewDashboard::class.java)
        intent.putExtra("dashboard_meta", meta)
        startActivity(intent);
    }

    open fun onTreasureClick(meta: String?) {
        val intent = Intent(context, TreasureViewActivity::class.java)
        intent.putExtra(EXTRA_TREASURE_KEY, meta)
        startActivity(intent)
    }


    fun onOpenBadgersClick() {
        val intent = Intent(context, Badges_Activity::class.java)
        startActivity(intent)
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
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(meta)
            startActivity(i)
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

        if (meta.length > 0) {
//            val serviceObj = MapChallangesServices(this, meta)
//            serviceObj.Service_getChallengeMapData_ServiceCall()

            val intent = Intent(context, MapChallengeActivity::class.java)
            intent.putExtra("challenge_id", meta)
            startActivity(intent)

        } else {
            val intent = Intent(context, NewCHallengeActivity::class.java)
            startActivity(intent)
        }
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


}