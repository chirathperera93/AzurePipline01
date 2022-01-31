package com.ayubo.life.ayubolife.revamp.v1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.book_videocall.BookVideoCallActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.activity.UploadActivity
import com.ayubo.life.ayubolife.channeling.activity.VisitDoctorActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.goals_extention.StepHistory_Activity
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity
import com.ayubo.life.ayubolife.health.OMCommon
import com.ayubo.life.ayubolife.health.OMMainPage
import com.ayubo.life.ayubolife.health.OMPage5TrackOrder
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.lifeplus.DiscoverToNewDashboard
import com.ayubo.life.ayubolife.lifeplus.FeedBackPopupActivity
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.HydrationTrackerDialog
import com.ayubo.life.ayubolife.lifeplus.PointsLeaderboardActivity
import com.ayubo.life.ayubolife.lifeplus.StepSummaryDashboardActivity
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.login.UserProfileActivity
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.new_payment.activity.NewPaymentMainActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.payment.EXTRA_TREASURE_KEY
import com.ayubo.life.ayubolife.payment.activity.*
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity
import com.ayubo.life.ayubolife.prochat.appointment.NewChatActivity
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.programs.viewexperts.ViewExpertsActivity
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.reports.activity.TestReportsActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.*
import com.ayubo.life.ayubolife.revamp.v1.fragment.V1NewFeedActivity
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.WalkWinMainActivity
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.gson.Gson
import com.google.gson.JsonObject

class ActionMetaForFragment {

    var context: Context? = null;

    fun processAction(ctx: Context, action: String, meta: String) {
        context = ctx
        Log.d("====action====", action)
        Log.d("====meta====", meta)

        if (action == "treasure") {
            onTreasureClick(meta)
        }

        if (action == "program_dash") {
            goToNewDashboard(meta)
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
            context!!.startActivity(intent)
        }

        if (action == "process_payment") {
            onClickPaymentConfirm(meta)
        }
        if (action == "other_payments") {
            onClickOtherPayment(meta)
        }
        if (action == "paynow") {
            if (meta == "daily") {
                onPayNowClick("4_11")
            } else if (meta == "weekly") {
                onPayNowClick("5_11")
            } else {
                onPayNowClick(meta)
            }
        }
        if (action == "addtobill") {
            onClickAddToBill(meta)
        }

        if (action == "filtered_videocall") {
            val loggerFB = AppEventsLogger.newLogger(context)
            val params = Bundle()

            params.putString(
                AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME,
                action
            )
            params.putString(AppConfig.FACEBOOK_EVENT_ID_META, meta)
            loggerFB.logEvent(
                AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
                params
            )


            onVideoCallClick(meta)
        }
        if (action == "videocall") {
            onVideoCallClick("")
        }
        if (action == "goal") {
            onGoalClick()
        }

        if (action == "reports") {
            onReportsClick()

//            goToTestReports("123456789:766549757370345466504e47552f30544545546a4e62424f41724b42595475336c44563751706175775838667358396d77416972647258664366366c763251347446633870346c4b6e644a305a4a63464f4c447941513d3d:6c612f51645859466f795649684b716239654f4544676935776f6c416352506e4d343549563934773574493d")
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
        } else if (action == "User_GetStarted") {
            goToSplashScreen()
        } else if (action == "Daily_Leaderboard" || action == "Weekly_Leaderboard" || action == "Monthly_Leaderboard") {
            openPointsLeaderBoard(action, meta)
        } else if (action == "payment") {
            goToPayHerePayment(meta)
        } else if (action == "open_conversation") {
            openNewChat(meta)
        } else if (action == "track_order") {
            goToPaymentTrackPage(meta)
        } else if (action == "ytd_dashboard") {
            goToYtdDashboard(meta)
        } else if (action == "my_steps_v3") {
            val intent = Intent(context, MyStepActivity::class.java)
            context!!.startActivity(intent);
        } else if (action == "my_weight_v3") {
            val intent = Intent(context, MyWeightActivity::class.java)
            context!!.startActivity(intent);
        } else if (action == "step_details") {
            val intent = Intent(context, MyStepDetailsActivity::class.java)
            context!!.startActivity(intent);
        } else if (action == "widget_settings") {
            val intent = Intent(context, AddMoreWidgetActivity::class.java)
            context!!.startActivity(intent);
        } else if (action == "feed") {
            val intent = Intent(context, V1NewFeedActivity::class.java)
            context!!.startActivity(intent);
        } else if (action == "add_report") {
            openAddReportDialog(meta)
        } else if (action == "step_count_details") {
            onStepCountDetailsClick(meta)
        } else if (action == "update_city") {
            openAddCityDialog(meta)
        } else if (action == "campaign_join") {
            openCampaignJoin(meta)
        } else if (action == "update_corporate_email") {
            openAddCorporateEmailDialog(meta)
        } else if (action == "redeem_points") {
            goToRedeemPointPage(meta)
        } else if (action == "user_profile") {
            goToUserProfile()
        }


    }

    private fun goToUserProfile() {
        val intent = Intent(context, UserProfileActivity::class.java)
        context!!.startActivity(intent)
    }

    private fun openCampaignJoin(meta: String) {
        val intent = Intent(context, CampaignJoinActivity::class.java)
        intent.putExtra("campaign_id", meta)
        context!!.startActivity(intent);
    }

    private fun goToRedeemPointPage(meta: String) {
        val intent = Intent(context, RedeemPointActivity::class.java)
        context!!.startActivity(intent)
    }

    private fun openAddCorporateEmailDialog(meta: String) {
        val intent = Intent(context, AddCorporateEmailDialog::class.java)
        context!!.startActivity(intent)
    }

    private fun openAddCityDialog(meta: String) {
        val intent = Intent(context, AddCityDialog::class.java)
        context!!.startActivity(intent)
    }

    private fun onStepCountDetailsClick(meta: String?) {
        val i: Intent = Intent(context, StepHistory_Activity::class.java);
        context!!.startActivity(i);

    }

    private fun openAddReportDialog(meta: String) {
        val intent = Intent(context, HydrationTrackerDialog::class.java)
        intent.putExtra("report_id", meta)
        context!!.startActivity(intent);
    }


    private fun goToYtdDashboard(meta: String) {
        val intent = Intent(context!!, WellnessHeroesActivity::class.java)
        intent.putExtra("ytdData", meta);
        context!!.startActivity(intent);

    }

    private fun goToPaymentTrackPage(meta: String) {
        val intent = Intent(context!!, OMPage5TrackOrder::class.java)
        intent.putExtra("selectedOrderId", meta);
        context!!.startActivity(intent);

    }

    private fun openNewChat(meta: String) {
        val intent: Intent = Intent(context!!, NewChatActivity::class.java);
        intent.putExtra("sessionId", meta);
        context!!.startActivity(intent);

    }

    private fun goToPayHerePayment(meta: String) {
        val intent = Intent(context!!, NewPaymentMainActivity::class.java)
        intent.putExtra("payhere_payment_id", meta)
        context!!.startActivity(intent)
    }

    private fun goToSplashScreen() {
        val intent = Intent(context!!, SplashScreen::class.java)
        context!!.startActivity(intent)
    }

    private fun goToStepSummaryDashboard() {
        val intent = Intent(context, StepSummaryDashboardActivity::class.java)
        context!!.startActivity(intent)
    }

    private fun openPointsLeaderBoard(action: String, meta: String) {
        val intent = Intent(context!!, PointsLeaderboardActivity::class.java)
        intent.putExtra("leader_board_type", action)
        intent.putExtra("leader_board_community", meta)


        val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(context);


        if (meta.equals("Daily_Leaderboard")) {
            loggerFB.logEvent("Daily_Leaderboard")
        }

        if (meta.equals("Weekly_Leaderboard")) {
            loggerFB.logEvent("Weekly_Leaderboard")
        }

        if (meta.equals("Monthly_Leaderboard")) {
            loggerFB.logEvent("Monthly_Leaderboard")
        } else {
            loggerFB.logEvent(meta)
        }


        context!!.startActivity(intent)
    }

    private fun openFeedbackPage(meta: String) {
        val intent = Intent(context!!, FeedBackPopupActivity::class.java)
        intent.putExtra("feedback_detail", meta)
        context!!.startActivity(intent);
    }

    private fun goToTestReports(meta: String) {
        if (meta != "") {
            val intent = Intent(context!!, TestReportsActivity::class.java)
            intent.putExtra("report_detail", meta)
            context!!.startActivity(intent);
        }
    }


    private fun doctorVideoCall(meta: String) {
        val intent = Intent(context, UploadActivity::class.java)
        intent.putExtra("doctor_id", meta)
        context!!.startActivity(intent);
    }

    open fun onTreasureClick(meta: String?) {
        val intent = Intent(context, TreasureViewActivity::class.java)
        intent.putExtra(EXTRA_TREASURE_KEY, meta)
        context!!.startActivity(intent)
    }

    private fun goToNewDashboard(meta: String) {
        val intent = Intent(context, DiscoverToNewDashboard::class.java)
        intent.putExtra("dashboard_meta", meta)
        context!!.startActivity(intent);
    }


    private fun goToWalkAndWin(meta: String) {
        val intent = Intent(context, WalkWinMainActivity::class.java)
        intent.putExtra("challenge_id", meta)
        context!!.startActivity(intent);
    }

    private fun openPaymentSummary() {
        val intent = Intent(context, PaymentSummaryViewActivity::class.java)
        context!!.startActivity(intent);
    }


    private fun openPayPin() {
        val intent = Intent(context, PaymentPinSubmitActivity::class.java)
        context!!.startActivity(intent);
    }

    fun onOpenBadgersClick() {
        val intent = Intent(context, Badges_Activity::class.java)
        context!!.startActivity(intent)
    }

    fun onLeaderboardClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, NewLeaderBoardActivity::class.java)
            intent.putExtra("challange_id", meta)
            context!!.startActivity(intent)
        }
    }

    fun onPayNowClick(meta: String) {
        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra("paymentmeta", meta)


        val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(context!!);
        val paramsNw: Bundle = Bundle();
        paramsNw.putString(
            AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME,
            AppConfig.FACEBOOK_EVENT_ID_DISCOVER
        );
        paramsNw.putString(AppConfig.FACEBOOK_EVENT_ID_ACTION_NAME, "paynow");

        if (meta.equals("4_11")) {
            paramsNw.putString("Daily", "Daily");
        } else if (meta.equals("5_11")) {
            paramsNw.putString("Monthly", "Monthly");
        } else {
            paramsNw.putString(AppConfig.FACEBOOK_EVENT_ID_META, "");
        }

        loggerFB.logEvent(AppEventsConstants.EVENT_NAME_SUBSCRIBE, paramsNw);


        context!!.startActivity(intent)
    }

    fun onHelpClick() {
        val intent = Intent(context, HelpFeedbackActivity::class.java)
        context!!.startActivity(intent)
    }

    fun onReportsClick() {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        intent.putExtra("data", "all")
        Ram.setReportsType("fromHome")
        context!!.startActivity(intent)
    }

    fun onGoalClick() {

        val prefManager = PrefManager(context)
        val status = prefManager.myGoalData["my_goal_status"]

        if (status === "Pending") {
            val intent = Intent(context, AchivedGoal_Activity::class.java)
            context!!.startActivity(intent)
        } else if (status === "Pick") {
            val intent = Intent(context, PickAGoal_Activity::class.java)
            context!!.startActivity(intent)
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
            context!!.startActivity(intent)
        }
    }

    fun onJSONNativePostClick(meta: String) {
        val intent = Intent(context, NativePostJSONActivity::class.java)
        intent.putExtra("meta", meta)
        context!!.startActivity(intent)
    }

    fun onClickNativeView(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        context!!.startActivity(intent)
    }


    fun onClickOtherPayment(meta: String) {
        OtherPaymentActivity.startActivity(context!! as Activity, meta)
    }

    fun onClickAddToBill(meta: String) {
        EnterMobileNumberActivityPayment.startActivity(context!!, meta)
    }

    fun onClickPaymentConfirm(meta: String) {
        // PaymentConfirmActivity.context!!.startActivity(context,meta)
    }

    fun onClickStoreGroupView(meta: String) {
        if (meta.isNotEmpty()) {
            GroupViewActivity.startActivity(context!! as Activity, meta)
        }
    }

    fun onClickEChannel(meta: String) {
        context!!.startActivity(Intent(context, VisitDoctorActivity::class.java))
    }

    fun onClickPrescription(meta: String) {
        context!!.startActivity(Intent(context, Medicine_ViewActivity::class.java))

//        val i = Intent(context, OMMainPage::class.java)
//        val jsonObject: JsonObject =
//            Gson().toJsonTree(OMCommon(context).retrieveFromDraftSingleton()).asJsonObject;
//        OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
//        context!!.startActivity(i)
    }

    fun openOrderMedicine(meta: String) {
//        context!!.startActivity(Intent(context, Medicine_ViewActivity::class.java))
//        context!!.startActivity(Intent(context, OrderMedicineMain::class.java))
        val i = Intent(context, OMMainPage::class.java)
        val jsonObject: JsonObject =
            Gson().toJsonTree(OMCommon(context!!).retrieveFromDraftSingleton()).asJsonObject;
        OMCommon(context!!).saveToCommonSingletonAndRetrieve(jsonObject);
        context!!.startActivity(i)
    }


    fun onClickCommonWebViewPay(meta: String) {
        val intent = Intent(context, PayCommonActivity::class.java)
        intent.putExtra("URL", meta)
        context!!.startActivity(intent)
    }


    fun onClickCommonWebView(meta: String) {
        val intent = Intent(context, CommonWebViewActivity::class.java)
        intent.putExtra("URL", meta)
        context!!.startActivity(intent)
    }

    fun onClickWeb(meta: String) {

        if (meta.isNotEmpty()) {
            Log.d("......meta......1...", meta)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(meta)
            context!!.startActivity(i)
        } else {
            Log.d("......meta......0...", meta)
        }
    }


    fun onChatClick(meta: String) {
        if (meta == "") {
            val intent = Intent(context, AskActivity::class.java)
            context!!.startActivity(intent)
        } else {
            AyuboChatActivity.startActivity(context!! as Activity, meta, false, false, "")
        }
    }

    fun onChatQuesClick(meta: String) {

        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        IntroActivity.startActivity(context!!)
    }

    fun onJanashakthiWelcomeClick(meta: String) {
        val pref = PrefManager(context)
        pref.relateID = meta
        pref.setIsJanashakthiWelcomee(true)
        val intent = Intent(context, JanashakthiWelcomeActivity::class.java)
        context!!.startActivity(intent)
    }

    fun onDyanamicQuestionClick(meta: String) {
        var pref: PrefManager? = PrefManager(context)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        val intent = Intent(context, IntroActivity::class.java)
        context!!.startActivity(intent)
    }

    fun onJanashakthiReportsClick(meta: String) {
        MedicalUpdateActivity.startActivity(context!!)
    }

    fun onOpenVideoClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        context!!.startActivity(intent)
    }

    fun onOpenImageClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        context!!.startActivity(intent)
    }

    fun onOpenNativePost(meta: String) {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        context!!.startActivity(intent)
    }

    fun onPostClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        context!!.startActivity(intent)
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
        context!!.startActivity(intent)
    }

    fun onButtonChannelingClick(meta: String) {
        if (meta.length > 0) {
            startDoctorsActivity(meta)
        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            context!!.startActivity(intent)
        }
    }

    fun onVideoCallClick(meta: String) {
        //  context!!.startActivity(Intent(context, BookVideoCallActivity::class.java))


        BookVideoCallActivity.startActivity(context, meta)


//        val intent = Intent(context, PointsLeaderboardActivity::class.java)
//        context!!.startActivity(intent)
    }

    fun onMapChallangeClick(meta: String) {

        if (meta.isNotEmpty()) {
            val intent = Intent(context, MapChallangeKActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_ID, meta)
            context!!.startActivity(intent)

//            val intent = Intent(context, MapChallengeActivity::class.java)
//            intent.putExtra("challenge_id", meta)
//            context!!.startActivity(intent)
        }
    }

    fun onShowExperts(experts: ArrayList<Experts>) {
        ViewExpertsActivity.startActivity(context!! as Activity, experts)
//        val intent = Intent(context,ViewExpertsActivity::class.java)
//        intent.putExtra("program_experts", experts)
//        context!!.startActivity(intent)
    }

    fun onAskClick(activityName: String, meta: String) {
        val intent = Intent(context, AskQuestion_Activity::class.java)
        context!!.startActivity(intent)
    }

    fun onProgramPostClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, SingleTimeline_Activity::class.java)
            intent.putExtra("related_by_id", meta)
            intent.putExtra("type", "program")
            context!!.startActivity(intent)
        }
    }


}