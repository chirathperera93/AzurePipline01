package com.ayubo.life.ayubolife.lifeplus


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity
import com.ayubo.life.ayubolife.fragments.HomePage_Utility
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity
import com.ayubo.life.ayubolife.map_challenges.Badges_Activity
import com.ayubo.life.ayubolife.map_challenges.MapChallengeActivity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity
import com.ayubo.life.ayubolife.payment.activity.EnterMobileNumberActivityPayment
import com.ayubo.life.ayubolife.payment.activity.OtherPaymentActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentActivity
import com.ayubo.life.ayubolife.payment.activity.PaymentConfirmActivity
import com.ayubo.life.ayubolife.payment.model.PriceList
import com.ayubo.life.ayubolife.post.activity.NativePostActivity
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.data.model.Experts
import com.ayubo.life.ayubolife.programs.viewexperts.ViewExpertsActivity
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity
import com.ayubo.life.ayubolife.timeline.OpenPostActivity
import com.ayubo.life.ayubolife.utility.Ram
import kotlinx.android.synthetic.main.fragment_dynamic.*


class DynamicFragment : Fragment(),MainTabAdapter.OnItemClickListener {


    override fun onProcessAction(action: String, meta: String) {
        processAction(action,meta)
    }



    lateinit var contxt: Context
    var dataList:ArrayList<ProgramList>?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val page = arguments!!.getInt(PAGE_NUM)
        dataList = arguments!!.getParcelableArrayList<ProgramList>("list")
        return inflater.inflate(R.layout.fragment_dynamic, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mlayoutManager = LinearLayoutManager(context)
        val timelineAapter =  MainTabAdapter(context!!, dataList!!)
        timelineAapter.onitemClickListener =  this@DynamicFragment
        tab_recycleview.apply {
            layoutManager = mlayoutManager as RecyclerView.LayoutManager?
            adapter = timelineAapter
        }
    }
         companion object {
            val PAGE_NUM = "PAGE_NUM"
            fun newInstance(page: Int, datList:ArrayList<ProgramList>): DynamicFragment {
                val fragment = DynamicFragment()
                val args = Bundle()
                args.putParcelableArrayList("list",datList)
                args.putInt(PAGE_NUM, page)
                fragment.setArguments(args)
                return fragment
            }
        }



    fun processAction(action: String,meta: String) {

        Log.d("......action....", "$action $meta")

        if (action == "process_payment") {
            onClickPaymentConfirm(meta)
        }
        if (action == "other_payments") {
            onClickOtherPayment(meta)
        }
        if (action == "paynow") {
            onPayNowClick()
        }
        if (action == "addtobill") {
            onClickAddToBill(meta)
        }

        if (action == "videocall") {
            onVideoCallClick(meta)
        }
        if (action == "goal") {
            onGoalClick()
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
        if (action == "commonview") {
            onClickCommonWebView(meta)
        }
        if (action == "native_post") {
            onClickNativeView(meta)
        }
        else if (action == "post") {
            onPostClick(meta)
        }
        else if (action == "challenge") {
            onMapChallangeClick(meta)
        }
        else if (action == "chat") {
            onChatClick(meta)
        }
        else if (action == "programtimeline") {
            onProgramNewDesignClick(meta)
        }
        else if (action == "program") {
            onProgramPostClick(meta)
        }
        else if (action == "channeling") {
            onButtonChannelingClick(meta)
        }
        else if (action == "janashakthiwelcome") {
            onJanashakthiWelcomeClick(meta)
        }
        else if (action == "dynamicquestion") {
            onDyanamicQuestionClick(meta)
        }
        else if (action == "janashakthireports") {
            onJanashakthiReportsClick(meta)
        }
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

    fun onPayNowClick() {
        val intent = Intent(context, PaymentActivity::class.java)
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
            serviceObj.showAlert_Deleted(context, "This goal has been achieved for the day. Please pick another goal tomorrow")
        }
    }


    fun onProgramNewDesignClick(meta: String) {
        if (meta.length > 0) {
            val intent = Intent(context, ProgramActivity::class.java)
            intent.putExtra("meta", meta)
            startActivity(intent)
        }
    }

    fun onClickNativeView(meta: String)
    {
        val intent = Intent(context, NativePostActivity::class.java)
        intent.putExtra("meta", meta)
        startActivity(intent)
    }


    fun onPaymentProcessed(obj: PriceList){
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

        if(meta.isNotEmpty()){
            Log.d("......meta......1...",meta)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(meta)
            startActivity(i)
        }else{
            Log.d("......meta......0...",meta)
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
        MedicalUpdateActivity.startActivity(context!!)
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
        intent.putExtra("meta",meta)
        startActivity(intent)
    }

    fun onPostClick(meta: String) {
        val intent = Intent(context, OpenPostActivity::class.java)
        intent.putExtra("postID",meta)
        startActivity(intent)
    }
    private fun startDoctorsActivity(doctorID:String) {
        val parameters = DocSearchParameters()
        parameters.doctorId = doctorID
        parameters.locationId = ""
        parameters.specializationId = ""
        parameters.date=""
        val pref = PrefManager(context)
        val user_id = pref.loginUser["uid"]
        parameters.user_id = user_id

        val intent = Intent(context, SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, SelectDoctorAction(parameters))
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "")
        startActivity(intent)

    }

    fun onButtonChannelingClick(meta: String) {
        if(meta.length>0){
            startDoctorsActivity(meta)
        }else{
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
        }
    }

    fun onVideoCallClick(meta: String) {
        if(meta.length>0){
            val activity="my_doctor"
            val intent = Intent(context, MyDoctorLocations_Activity::class.java)
            intent.putExtra("doctor_id", meta)
            intent.putExtra("activityName", activity)
            startActivity(intent)
        }else{
            val intent = Intent(context, MyDoctor_Activity::class.java)
            intent.putExtra("activityName", "myExperts")
            startActivity(intent)
        }
    }

    fun onMapChallangeClick(meta: String) {

        if(meta.length>0){
//            val serviceObj = MapChallangesServices(this, meta)
//            serviceObj.Service_getChallengeMapData_ServiceCall()

            val intent = Intent(context, MapChallengeActivity::class.java)
            intent.putExtra("challenge_id", meta)
            startActivity(intent)

        }else{
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
    fun onAskClick(activityName: String,meta: String) {
        val intent = Intent(context, AskQuestion_Activity::class.java)
        startActivity(intent)
    }

    fun onProgramPostClick(meta: String) {
        if(meta.length>0){
            val intent = Intent(context, SingleTimeline_Activity::class.java)
            intent.putExtra("related_by_id", meta)
            intent.putExtra("type", "program")
            startActivity(intent)
        }
    }

}
