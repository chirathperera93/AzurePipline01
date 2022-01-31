package com.ayubo.life.ayubolife.prochat.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ayubo.life.ayubolife.R
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
import com.ayubo.life.ayubolife.common.SetDiscoverPage
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
import com.ayubo.life.ayubolife.lifeplus.*
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.HydrationTrackerDialog
import com.ayubo.life.ayubolife.login.SplashScreen
import com.ayubo.life.ayubolife.login.UserProfileActivity
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.new_payment.PUSH_ACTION
import com.ayubo.life.ayubolife.new_payment.PUSH_META
import com.ayubo.life.ayubolife.new_payment.activity.NewPaymentMainActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.EXTRA_CHALLANGE_ID
import com.ayubo.life.ayubolife.payment.EXTRA_TREASURE_KEY
import com.ayubo.life.ayubolife.payment.activity.*
import com.ayubo.life.ayubolife.payment.model.PaymentConfirmMainDataNew
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.payment.vm.PaymentConfirmVM
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.post.activity.NativePostJSONActivity
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity
import com.ayubo.life.ayubolife.prochat.appointment.NewChatActivity
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.programs.viewexperts.ViewExpertsActivity
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.reports.activity.TestReportsActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.reports.upload.UploadReportActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.*
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.walk_and_win.WalkWinMainActivity
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.*
import java.io.InputStream
import javax.inject.Inject


open class BaseActivity : AppCompatActivity() {
    val REQUEST_EXTERNAL_STORAGE_WRITE_RECORD_AUDIO = 20

    open val subscription = CompositeDisposable()
    var toast: Toast? = null

    @Inject
    open lateinit var paymentConfirmVM: PaymentConfirmVM

    lateinit var dataObjNew: PaymentConfirmMainDataNew;


    open fun getResizedBitmapImage(bitmap: Bitmap): Bitmap? {
        val squareBitmapWidth = "70"
        println("=============squareBitmapWidth=============$squareBitmapWidth")
        val outpu = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val w = bitmap.width
        val sss: Float = squareBitmapWidth.toFloat()
        val ratio = sss / 100
        val ratioWidth = ratio * bitmap.width
        val ratioHeight = ratio * bitmap.height
        return Bitmap.createScaledBitmap(
            bitmap, Math.round(ratioWidth),
            Math.round(ratioHeight), true
        )
    }

    open fun getDensityName(): String {
        val density = this@BaseActivity.resources.displayMetrics.density
        if (density >= 4.0) {
            Log.d("====xxxhdpi=======", density.toString())
            return "xxxhdpi"
        } else if (density >= 3.0) {
            Log.d("====xxhdpi=======", density.toString())
            return "xxxhdpi"
        } else if (density >= 2.0) {
            Log.d("====xhdpi=======", density.toString())
            return "xxxhdpi"
        } else if (density >= 1.5) {
            Log.d("====hdpi=======", density.toString())
            return "xxhdpi"
        } else {
            return "xhdpi"
        }
    }

    open fun getResizedBitmapImport(bitmap: Bitmap): Bitmap? {
        val squareBitmapWidth = "70"
        //println("=============squareBitmapWidth=============$squareBitmapWidth")
        val outpu = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val w = bitmap.width
        val sss: Float = squareBitmapWidth.toFloat()
        val ratio = sss / 100 + 1
        val ratioWidth = ratio * bitmap.width
        val ratioHeight = ratio * bitmap.height
        return Bitmap.createScaledBitmap(
            bitmap, Math.round(ratioWidth),
            Math.round(ratioHeight), true
        )
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val bundle = intent!!.extras
        if (bundle != null && (bundle.containsKey(PUSH_META) || bundle.containsKey(PUSH_ACTION))) {

            if (bundle.getSerializable(PUSH_ACTION) != null && bundle.getSerializable(PUSH_META) != null) {
                val action = bundle.getSerializable(PUSH_ACTION) as String
                val meta = bundle.getSerializable(PUSH_META) as String
                processAction(action, meta)
            }


        }
    }

    fun readJSONFromAsset(challangeID: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = this@BaseActivity.openFileInput("$challangeID.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun getZoomLevel(distance: Double): Float {

        //  val distance = current_distance.toInt()
        val dens: Int = getScreenWidth()

        var fZoom = 8.3f
        fZoom = if (0.298 * dens > distance) {
            19.00f
        } else if (0.596 * dens > distance) {
            18.00f
        } else if (1.193 * dens > distance) {
            17.00f
        } else if (2.387 * dens > distance) {
            16.00f
        } else if (4.773 * dens > distance) {
            15.00f
        } else if (9.547 * dens > distance) {
            14.00f
        } else if (19.093 * dens > distance) {
            13.00f
        } else if (38.187 * dens > distance) {
            12.00f
        } else if (76.373 * dens > distance) {
            11.00f
        } else if (152 * dens > distance) {
            10.00f
        } else if (305 * dens > distance) {
            9.00f
        } else if (610 * dens > distance) {
            8.00f
        } else if (1222 * dens > distance) {
            7.00f
        } else if (2444 * dens > distance) {
            6.00f
        } else if (4888 * dens > distance) {
            5.00f
        } else {
            12.00f
        }

        Log.d("======fZoom===========", fZoom.toString())

        return fZoom
    }

    fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext);
        return deviceScreenDimension.displayWidth
    }

    @SuppressLint("MissingPermission")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    fun paymentActionProcess(action: String, meta: String) {


        if (action == "webpay") {

            if (meta.isNotEmpty()) {
                Log.d("......meta......1...", meta)

                val inten = Intent(Intent.ACTION_VIEW, Uri.parse(meta))
                inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(inten)
            }
        }
        if (action == "commonpay") {
            onClickCommonWebViewPay(meta)
        }
        if (action == "apipay") {

        }

    }


    fun onDiscoverClick() {
//        intent = Intent(baseContext, LifePlusProgramActivity::class.java)

//        if (Constants.type == com.flavors.changes.Constants.Type.LIFEPLUS) {
//            intent = Intent(baseContext, NewDiscoverActivity::class.java)
//        } else {
//            intent = Intent(baseContext, LifePlusProgramActivity::class.java)
//        }


        intent = SetDiscoverPage().getDiscoverIntent(this);
        intent.putExtra("isFromSearchResults", false)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun onChatClickFilter(meta: String) {
        // AskActivity.startActivity(this,meta)
        intent = Intent(baseContext, AskActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)

    }

    fun onReportReviewFiltered(meta: String) {
        intent = Intent(baseContext, GetAReviewActivity::class.java)
        intent.putExtra("filtereKey", meta)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    fun processPaymentAction(obj: PriceList) {
        onPaymentProcessed(obj)
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

            val loggerFB = AppEventsLogger.newLogger(this)
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
        if (action == "filtered_reportreview") {
            val loggerFB = AppEventsLogger.newLogger(this)
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
            onReportReviewFiltered(meta)
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
        } else if (action == "patient_report_review") {
            patientReportReview(meta)
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
        val intent = Intent(baseContext, UserProfileActivity::class.java)
        startActivity(intent)
    }

    private fun openCampaignJoin(meta: String) {
        val intent = Intent(baseContext, CampaignJoinActivity::class.java)
        intent.putExtra("campaign_id", meta)
        startActivity(intent);
    }

    private fun goToRedeemPointPage(meta: String) {
        val intent = Intent(baseContext, RedeemPointActivity::class.java)
        startActivity(intent)
    }

    private fun openAddCorporateEmailDialog(meta: String) {
        val intent = Intent(baseContext, AddCorporateEmailDialog::class.java)
        startActivity(intent)
    }

    private fun openAddCityDialog(meta: String) {
        val intent = Intent(baseContext, AddCityDialog::class.java)
        startActivity(intent)
    }

    private fun onStepCountDetailsClick(meta: String?) {
        val i: Intent = Intent(baseContext, StepHistory_Activity::class.java);
        startActivity(i);

    }

    private fun openAddReportDialog(meta: String) {
        val intent = Intent(baseContext, HydrationTrackerDialog::class.java)
        intent.putExtra("report_id", meta)
        startActivity(intent);
    }

    private fun goToYtdDashboard(meta: String) {
        val intent = Intent(baseContext, WellnessHeroesActivity::class.java)
        intent.putExtra("ytdId", meta);
        startActivity(intent);

    }

    private fun goToPaymentTrackPage(meta: String) {
        val intent = Intent(baseContext, OMPage5TrackOrder::class.java)
        intent.putExtra("selectedOrderId", meta);
        startActivity(intent);

    }

    private fun openNewChat(meta: String) {
        val intent: Intent = Intent(baseContext, NewChatActivity::class.java);
        intent.putExtra("sessionId", meta);
        startActivity(intent);

    }

    private fun goToPayHerePayment(meta: String) {
        val intent = Intent(this, NewPaymentMainActivity::class.java)
        intent.putExtra("payhere_payment_id", meta)
        startActivity(intent)
    }

    private fun goToSplashScreen() {
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToStepSummaryDashboard() {
        val intent = Intent(this, StepSummaryDashboardActivity::class.java)
        startActivity(intent)
    }

    private fun openPointsLeaderBoard(action: String, meta: String) {
        val intent = Intent(this, PointsLeaderboardActivity::class.java)
        intent.putExtra("leader_board_type", action)
        intent.putExtra("leader_board_community", meta)


        val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(this);


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


        startActivity(intent)
    }

    private fun openFeedbackPage(meta: String) {
        val intent = Intent(this, FeedBackPopupActivity::class.java)
        intent.putExtra("feedback_detail", meta)
        startActivity(intent);
    }

    private fun goToTestReports(meta: String) {
        if (meta != "") {
            val intent = Intent(this, TestReportsActivity::class.java)
            intent.putExtra("report_detail", meta)
            startActivity(intent);
        }
    }

    private fun patientReportReview(meta: String) {
        UploadReportActivity.startActivity(this, "", "", "", "", "", meta)
    }

    private fun doctorVideoCall(meta: String) {
        val intent = Intent(this, UploadActivity::class.java)
        intent.putExtra("doctor_id", meta)
        startActivity(intent);
    }

    open fun onTreasureClick(meta: String?) {
        val intent = Intent(this, TreasureViewActivity::class.java)
        intent.putExtra(EXTRA_TREASURE_KEY, meta)
        startActivity(intent)
    }

    private fun goToNewDashboard(meta: String) {
        val intent = Intent(this, DiscoverToNewDashboard::class.java)
        intent.putExtra("dashboard_meta", meta)
        startActivity(intent);
    }


    private fun goToWalkAndWin(meta: String) {
        val intent = Intent(this, WalkWinMainActivity::class.java)
        intent.putExtra("challenge_id", meta)
        startActivity(intent);
    }

    private fun openPaymentSummary() {
        val intent = Intent(this, PaymentSummaryViewActivity::class.java)
        startActivity(intent);
    }


    private fun openPayPin() {
        val intent = Intent(this, PaymentPinSubmitActivity::class.java)
        startActivity(intent);
    }

    fun onOpenBadgersClick() {
        val intent = Intent(this, Badges_Activity::class.java)
        startActivity(intent)
    }

    fun onLeaderboardClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(this, NewLeaderBoardActivity::class.java)
            intent.putExtra("challange_id", meta)
            startActivity(intent)
        }
    }

    fun onPayNowClick(meta: String) {
        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("paymentmeta", meta)


        val loggerFB: AppEventsLogger = AppEventsLogger.newLogger(getBaseContext());
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


        startActivity(intent)
    }

    fun onHelpClick() {
        val intent = Intent(this, HelpFeedbackActivity::class.java)
        startActivity(intent)
    }

    fun onReportsClick() {
        val intent = Intent(this, ReportDetailsActivity::class.java)
        intent.putExtra("data", "all")
        Ram.setReportsType("fromHome")
        startActivity(intent)
    }

    fun onGoalClick() {

        val prefManager = PrefManager(this)
        val status = prefManager.myGoalData["my_goal_status"]

        if (status === "Pending") {
            val intent = Intent(this, AchivedGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Pick") {
            val intent = Intent(this, PickAGoal_Activity::class.java)
            startActivity(intent)
        } else if (status === "Completed") {
            val serviceObj = HomePage_Utility(this)
            serviceObj.showAlert_Deleted(
                this,
                "This goal has been achieved for the day. Please pick another goal tomorrow"
            )
        }
    }


    fun onProgramNewDesignClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(this, ProgramActivity::class.java)
            intent.putExtra("meta", meta)
            startActivity(intent)
        }
    }

    fun onJSONNativePostClick(meta: String) {
        val intent = Intent(this, NativePostJSONActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
//        finish()
    }

    fun onClickNativeView(meta: String) {
        val intent = Intent(this, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }


    fun onPaymentProcessed(obj: PriceList) {


//        PaymentConfirmActivity.startActivity(this,
//                obj.item_price_master_id,
//                obj.text,
//                obj.amount,
//                obj.relate_type_id,
//                obj.related_id,
//                obj.payment_source_id,
//                obj.payment_frequency,
//                obj.service_payment_frequency_source_id,
//                obj.custom_param,
//                obj.user_payment_method_id
//        );

        getConfirmationData(
            obj.item_price_master_id,
            obj.text,
            obj.amount,
            obj.relate_type_id,
            obj.related_id,
            obj.payment_source_id,
            obj.payment_frequency,
            obj.service_payment_frequency_source_id,
            obj.custom_param,
            obj.user_payment_method_id
        );
    }

    private fun getConfirmationData(
        item_price_master_id: Int,
        text: String,
        amount: String,
        relate_type_id: String,
        related_id: String,
        payment_source_id: String,
        payment_frequency: String,
        payment_frequency_id: String,
        custom_param: String,
        user_payment_method_id: String
    ) {
        subscription.add(paymentConfirmVM.getPaymentConfirmation(
            item_price_master_id,
            text,
            amount,
            relate_type_id,
            related_id,
            payment_source_id,
            payment_frequency,
            payment_frequency_id,
            custom_param,
            user_payment_method_id
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({

                if (it.isSuccess) {

                    dataObjNew = paymentConfirmVM.dataObj;

                    if (dataObjNew.action == "paysummary" || dataObjNew.action == "paypin") {
                        val pref = PrefManager(this)
                        pref.paymentId = dataObjNew.meta;
                    }
                    processAction(dataObjNew.action, dataObjNew.meta);


                } else {
                    showMessage(R.string.service_loading_fail)
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage(R.string.service_loading_fail)
            })

        )
    }


    fun onClickOtherPayment(meta: String) {
        OtherPaymentActivity.startActivity(this, meta)
    }

    fun onClickAddToBill(meta: String) {
        EnterMobileNumberActivityPayment.startActivity(this, meta)
    }

    fun onClickPaymentConfirm(meta: String) {
        // PaymentConfirmActivity.startActivity(this,meta)
    }

    fun onClickStoreGroupView(meta: String) {
        if (meta.isNotEmpty()) {
            GroupViewActivity.startActivity(this, meta)
        }
    }

    fun onClickEChannel(meta: String) {
        startActivity(Intent(this, VisitDoctorActivity::class.java))
    }

    fun onClickPrescription(meta: String) {
        startActivity(Intent(this, Medicine_ViewActivity::class.java))

//        val i = Intent(this, OMMainPage::class.java)
//        val jsonObject: JsonObject =
//            Gson().toJsonTree(OMCommon(this).retrieveFromDraftSingleton()).asJsonObject;
//        OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
//        startActivity(i)
    }

    fun openOrderMedicine(meta: String) {
//        startActivity(Intent(this, Medicine_ViewActivity::class.java))
//        startActivity(Intent(this, OrderMedicineMain::class.java))
        val i = Intent(this, OMMainPage::class.java)
        val jsonObject: JsonObject =
            Gson().toJsonTree(OMCommon(this).retrieveFromDraftSingleton()).asJsonObject;
        OMCommon(baseContext).saveToCommonSingletonAndRetrieve(jsonObject);
        startActivity(i)
    }


    fun onClickCommonWebViewPay(meta: String) {
        val intent = Intent(this, PayCommonActivity::class.java)
        intent.putExtra("URL", meta)
        startActivity(intent)
    }


    fun onClickCommonWebView(meta: String) {
        val intent = Intent(this, CommonWebViewActivity::class.java)
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
        if (meta == "") {
            val intent = Intent(this, AskActivity::class.java)
            startActivity(intent)
        } else {
            AyuboChatActivity.startActivity(this, meta, false, false, "")
        }
    }

    fun onChatQuesClick(meta: String) {

        var pref: PrefManager? = PrefManager(this)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        IntroActivity.startActivity(this)
    }

    fun onJanashakthiWelcomeClick(meta: String) {
        val pref = PrefManager(this)
        pref.relateID = meta
        pref.setIsJanashakthiWelcomee(true)
        val intent = Intent(this, JanashakthiWelcomeActivity::class.java)
        startActivity(intent)
    }

    fun onDyanamicQuestionClick(meta: String) {
        var pref: PrefManager? = PrefManager(this)
        pref?.relateID = meta
        pref?.setIsJanashakthiDyanamic(true)
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onJanashakthiReportsClick(meta: String) {
        MedicalUpdateActivity.startActivity(this)
    }

    fun onOpenVideoClick(meta: String) {
        val intent = Intent(this, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenImageClick(meta: String) {
        val intent = Intent(this, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    fun onOpenNativePost(meta: String) {
        val intent = Intent(this, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }

    fun onPostClick(meta: String) {
        val intent = Intent(this, OpenPostActivity::class.java)
        intent.putExtra("postID", meta)
        startActivity(intent)
    }

    private fun startDoctorsActivity(doctorID: String) {
        val parameters = DocSearchParameters()
        parameters.doctorId = doctorID
        parameters.locationId = ""
        parameters.specializationId = ""
        parameters.date = ""
        val pref = PrefManager(this)
        val user_id = pref.loginUser["uid"]
        parameters.user_id = user_id

        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(parameters))
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "")
        startActivity(intent)
        finish()
    }

    fun onButtonChannelingClick(meta: String) {
        if (meta.length > 0) {
            startDoctorsActivity(meta)
        } else {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun onVideoCallClick(meta: String) {
        //  startActivity(Intent(this, BookVideoCallActivity::class.java))


        BookVideoCallActivity.startActivity(this, meta)


//        val intent = Intent(this, PointsLeaderboardActivity::class.java)
//        startActivity(intent)
    }

    fun onMapChallangeClick(meta: String) {

        if (meta.isNotEmpty()) {
            val intent = Intent(this, MapChallangeKActivity::class.java)
            intent.putExtra(EXTRA_CHALLANGE_ID, meta)
            startActivity(intent)

//            val intent = Intent(this, MapChallengeActivity::class.java)
//            intent.putExtra("challenge_id", meta)
//            startActivity(intent)
        }
    }

    fun onShowExperts(experts: ArrayList<Experts>) {
        ViewExpertsActivity.startActivity(this, experts)
//        val intent = Intent(this,ViewExpertsActivity::class.java)
//        intent.putExtra("program_experts", experts)
//        startActivity(intent)
    }

    fun onAskClick(activityName: String, meta: String) {
        val intent = Intent(this, AskQuestion_Activity::class.java)
        startActivity(intent)
    }

    fun onProgramPostClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(this, SingleTimeline_Activity::class.java)
            intent.putExtra("related_by_id", meta)
            intent.putExtra("type", "program")
            startActivity(intent)
        }
    }


    fun showAlert(message: String, title: String?, onClickOk: DialogInterface.OnClickListener) {
        val alertDialogBuilder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog)
        alertDialogBuilder
            .setMessage(message)
            .setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", onClickOk)
        val alert = alertDialogBuilder.create()
        alert!!.show()
    }

    fun showAlertOneButton(
        title: String,
        message: String,
        buttonText: String,
        listener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText, listener).show()
    }

    fun showAlertOneButton(
        @StringRes title: Int,
        @StringRes message: Int,
        @StringRes buttonText: Int,
        listener: DialogInterface.OnClickListener
    ) {
        showAlertOneButton(getString(title), getString(message), getString(buttonText), listener)
    }

    fun showAlertTwoButton(
        title: Int,
        message: Int,
        positiveButtonText: Int,
        negativeButtonText: Int,
        positive: DialogInterface.OnClickListener,
        negative: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, positive)
            .setNegativeButton(negativeButtonText, negative)
            .show()
    }

    fun showMessage(msg: Int) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    fun showMessage(msg: String) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast!!.show()
    }

    fun requestPermissions(): Boolean {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                REQUEST_EXTERNAL_STORAGE_WRITE_RECORD_AUDIO
            )
            Log.d("permissions", "requestStorageWrite")
            return false
        }
        return true
    }


    override fun onDestroy() {
        super.onDestroy()
        if (toast != null) {
            toast!!.cancel()
        }
    }


}