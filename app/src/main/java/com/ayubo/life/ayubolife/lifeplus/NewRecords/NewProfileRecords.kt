package com.ayubo.life.ayubolife.lifeplus.NewRecords

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.login.LoginActivity_First
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo
import com.ayubo.life.ayubolife.reports.activity.*
import com.ayubo.life.ayubolife.reports.adapters.ReportDetailsAdapter
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse
import com.ayubo.life.ayubolife.reports.model.DeleteReportObject
import com.ayubo.life.ayubolife.reports.model.DeleteReportResponse
import com.ayubo.life.ayubolife.reports.utility.Utility_Report
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.utility.Utility
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewProfileRecords.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewProfileRecords : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var mainView: View;
    lateinit var editText: EditText;
    lateinit var errorMsg: TextView;
    lateinit var errorMsg_lay: LinearLayout;
    lateinit var main_recycler: RecyclerView;

    //    lateinit var mProgressDialog: ProgressDialog;
    lateinit var dateView_programs: LinearLayout;

    //    lateinit var btn_backImgBtn_layout: LinearLayout;
//    lateinit var btn_back_Button: ImageButton;
    lateinit var fab: LinearLayout;
    lateinit var selectedUuserId: String;
    lateinit var pref: PrefManager;
    lateinit var userid_ExistingUser: String;
    var firstReportID: String = "";
    lateinit var listDataHeader: List<String>;
    lateinit var gson: Gson;
    var listDataChild: HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>>? = null;
    var memberList: ArrayList<AllRecordsMainResponse.AllRecordsMember>? = null;
    var dataList: ArrayList<Object>? = null;
    var source: List<Object>? = null;
    lateinit var adapter: ReportDetailsAdapter;
    lateinit var linearLayoutManager: LinearLayoutManager;
    var strUrl: String = "";

    var isVisibleView: Boolean = false;
    var isViewCreated: Boolean = false;
    lateinit var new_records_loading: ProgressAyubo;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_new_profile_records, container, false)
        new_records_loading = mainView.findViewById(R.id.new_records_loading);
        uiConstructor();
        dataConstructor();
        pref.setReportLastUpdated(true);
        return mainView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true;
        if (isVisibleView) {
            getAllReportsData(true, selectedUuserId, true);
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleView = isVisibleToUser;

        if (isVisibleView && isViewCreated) {
            getAllReportsData(true, selectedUuserId, true);
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewProfileRecords.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewProfileRecords().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun uiConstructor() {

        val view_ask: View = mainView.findViewById(R.id.layout_search_button);
        editText = view_ask.findViewById(R.id.edt_search_value);


        val editText: EditText = view_ask.findViewById(R.id.edt_search_value);
//        val txt_activity_heading: TextView = view_ask.findViewById(R.id.txt_activity_heading);
//        txt_activity_heading.setText("Reports");
        editText.setHint("Search reports here");

        errorMsg = mainView.findViewById(R.id.errorMsg);
        errorMsg_lay = mainView.findViewById(R.id.errorMsg_lay);
        errorMsg_lay.setVisibility(View.INVISIBLE);
        main_recycler = mainView.findViewById(R.id.main_recycler) as RecyclerView;
//        mProgressDialog = ProgressDialog(this@NewProfileRecords.context);
//        mProgressDialog.setMessage("Please wait...");
//        mProgressDialog.setCancelable(false);
        dateView_programs = mainView.findViewById(R.id.dateView) as LinearLayout;
//        btn_backImgBtn_layout = mainView.findViewById(R.id.btn_backImgBtn_layout) as LinearLayout;
//        btn_back_Button = mainView.findViewById(R.id.btn_backImgBtn) as ImageButton;

        fab = mainView.findViewById(R.id.fab) as LinearLayout;
//        fab.setImageDrawable(this@NewProfileRecords.context?.let { ContextCompat.getDrawable(it, R.drawable.plusimages) });
        fab.setOnClickListener {


//            if (selectedUuserId.equals("all")) {
//
//                this@NewProfileRecords.context?.let { it1 -> showAlert_Add(it1, "", "Select a member before adding a new report") };
//
//            }

//            else {


//                val ureel: String = ApiClient.BASE_URL + "index.php?module=PC_MedicalTestResults&action=manualMedicalResultForm&user=" + selectedUuserId;
//                val newUrl: String = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), ureel);
            val intent: Intent =
                Intent(this@NewProfileRecords.context, UploadMediaReportActivity::class.java);
            intent.putExtra("memberList", memberList);
            intent.putExtra("dataList", dataList);
            intent.putExtra("firstReportID", firstReportID);
            intent.putExtra("selectedUuserId", selectedUuserId);
            startActivity(intent);


//            }

        }
    }

    fun dataConstructor() {
        pref = PrefManager(this@NewProfileRecords.context);
        userid_ExistingUser = pref.getLoginUser().get("uid").toString();
        gson = Gson();
        selectedUuserId = "all";
    }

    fun getAllReportsData(isLoadingNeed: Boolean, patient_id: String, isNeedToCash: Boolean) {


//        if (isVisibleView) {
        val appToken: String = pref.getUserToken();
        selectedUuserId = patient_id;
//            mProgressDialog.show();
        new_records_loading.visibility = View.VISIBLE


        val task: NewProfileRecords.getAllReportsDataAsync =
            getAllReportsDataAsync(appToken, patient_id);
        task.execute();


//        }


    }


    internal inner class getAllReportsDataAsync(appToken: String, patient_id: String) :
        AsyncTask<Void, Void, Void>() {
        val appToken = appToken;
        val patientId = patient_id;
        override fun doInBackground(vararg p0: Void?): Void? {
            val apiService: ApiInterface =
                ApiClient.getReportsApiClientNewForHemas().create(ApiInterface::class.java);

            val call: Call<AllRecordsMainResponse> =
                apiService.getAllReportsNew(AppConfig.APP_BRANDING_ID, appToken);

            call.enqueue(object : Callback<AllRecordsMainResponse> {
                override fun onResponse(
                    call: Call<AllRecordsMainResponse>,
                    response: Response<AllRecordsMainResponse>
                ) {
//                    if (mProgressDialog != null) {
//                        mProgressDialog.cancel();
//                    }

                    new_records_loading.visibility = View.GONE;

                    if (response.isSuccessful()) {

                        if (response.body() != null) {
                            if (response.body()!!.result == 401) {
                                val intent: Intent = Intent(
                                    this@NewProfileRecords.context,
                                    LoginActivity_First::class.java
                                );
                                startActivity(intent);
                            }
                            if (response.body()!!.getResult() == 0) {
                                // Code here .............
                                val gson1: Gson = Gson();
                                val dataObj: AllRecordsMainResponse =
                                    response.body() as AllRecordsMainResponse;
                                pref.setReportLoadingStatus("0");
                                loadView(dataObj);
                            }
                        }


                    }
                }

                override fun onFailure(call: Call<AllRecordsMainResponse>, t: Throwable) {
                    new_records_loading.visibility = View.GONE

                    try {
                        Toast.makeText(
                            context,
                            R.string.servcer_not_available,
                            Toast.LENGTH_LONG
                        ).show();
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    System.out.println("========t======" + t);
                }

            })
            return null;
        }
    }

    fun showAlert_Add(c: Context, title: String, msg: String) {

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(c);
        val inflater: LayoutInflater =
            c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val layoutView: View = inflater.inflate(R.layout.alert_common_with_ok_only, null, false);
        builder.setView(layoutView);

        val dialog: android.app.AlertDialog = builder.create();
        dialog.setCancelable(false);

        val lbl_alert_message: TextView =
            layoutView.findViewById(R.id.lbl_alert_message) as TextView;
        lbl_alert_message.setText(msg);

        val btn_no: TextView = layoutView.findViewById(R.id.btn_ok) as TextView;
        btn_no.setText("CANCEL");

        btn_no.setOnClickListener {
            dialog.cancel();
        }

        dialog.show();
    }

    fun loadView(mainData: AllRecordsMainResponse) {


        listDataChild = HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>>();
        listDataHeader = ArrayList<String>();
        if ((memberList != null) && (memberList!!.size > 0)) {
            memberList!!.clear()
        }
        memberList = mainData.data.members as ArrayList<AllRecordsMainResponse.AllRecordsMember>
        memberList!!.add(AllRecordsMainResponse.AllRecordsMember("Add", "Add", "Add", "", "", ""));

        val reportsList: List<AllRecordsMainResponse.AllRecordsReport> = mainData.data.reports

        if ((dataList != null && (dataList!!.size > 0))) {
            dataList!!.clear();
        }

        dataList =
            Utility_Report.getreportDataList(firstReportID, reportsList) as ArrayList<Object>;
        source = ArrayList<Object>(dataList);


        if (dataList!!.size > 1) {
            errorMsg_lay.setVisibility(View.GONE);
            errorMsg.setVisibility(View.GONE);
        } else {
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg_lay.setVisibility(View.VISIBLE);
        }

        adapter = ReportDetailsAdapter(
            this@NewProfileRecords.context,
            dataList as List<Any>?,
            memberList,
            firstReportID,
            selectedUuserId
        );
        linearLayoutManager = LinearLayoutManager(
            this@NewProfileRecords.context,
            LinearLayoutManager.VERTICAL,
            false
        );

        main_recycler.setLayoutManager(linearLayoutManager);
        main_recycler.setItemAnimator(DefaultItemAnimator());
        main_recycler.setAdapter(adapter);

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.getFilter().filter(charSequence.toString());
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        adapter.setOnSelectAssignUserListener {

            reAssignReport(it);


        }
        adapter.setOnClickNotAssignIconListener {
            assignReport(it);

        }
        adapter.setOnClickFamilyMemberListener {

            displaySelectedMemberReports(it);

        }

        adapter.setOnClickDownloadReportListener { history, position ->
            callDownloadView(history);
            adapter.notifyDataSetChanged();
        }
        adapter.setOnClickViewReportListener { history, position -> callChartView(history); }


        adapter.setOnClickShareReportListener {
            callShareView(it);
        }

        adapter.setOnClickDeleteReportListener { reportObj, position ->
            deleteMemberReportShowAlert(reportObj, position);
        }


    }

    fun deleteMemberReportShowAlert(reportObj: AllRecordsMainResponse.AllRecordsReport, pos: Int) {

        showAlert_ConfirmDelete(requireContext(), reportObj, pos, "Are you sure ?");

    }


    fun showAlert_ConfirmDelete(
        c: Context,
        reportO: AllRecordsMainResponse.AllRecordsReport,
        pos: Int,
        msg: String
    ) {

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(c);
        val inflater: LayoutInflater =
            c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as (LayoutInflater);
        val layoutView: View =
            inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        val dialog: android.app.AlertDialog = builder.create();
        dialog.setCancelable(false);
        val lbl_alert_message: TextView =
            layoutView.findViewById(R.id.lbl_alert_message) as (TextView);
        lbl_alert_message.setText(msg);


        val btn_yes: TextView = layoutView.findViewById(R.id.btn_yes) as (TextView);
        btn_yes.setOnClickListener {

            dialog.cancel();

            dataList!!.removeAt(pos);
            adapter.notifyDataSetChanged();

            deleteMemberReport(reportO, reportO.getEncId(), reportO.getTestorder_id());


        }
        val btn_no: TextView = layoutView.findViewById(R.id.btn_no) as (TextView);
        btn_no.setOnClickListener {
            dialog.cancel();
        }
        dialog.show();
    }

    fun deleteMemberReport(
        obj: AllRecordsMainResponse.AllRecordsReport,
        enc_id: String,
        table_id: String
    ) {
        //   mProgressDialog.show();
        val appToken: String = pref.getUserToken();
        val report_type: String = obj.getReportType();

        val apiService: ApiInterface =
            ApiClient.getReportsApiClientNewReadRecord().create(ApiInterface::class.java);

        val deleteReportObj = DeleteReportObject(enc_id, obj.id, report_type, table_id)

        val call: Call<DeleteReportResponse> = apiService.deletedMemberReportsNew(
            AppConfig.APP_BRANDING_ID,
            appToken,
            deleteReportObj
        );
        call.enqueue(object : Callback<DeleteReportResponse> {
            override fun onFailure(call: Call<DeleteReportResponse>, t: Throwable) {
                Toast.makeText(context, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }

            override fun onResponse(
                call: Call<DeleteReportResponse>,
                response: Response<DeleteReportResponse>
            ) {
                if (response.isSuccessful()) {
                    System.out.println("=======t======");
                    getAllReportsData(true, "all", true);
                }
            }


        });


    }

    fun reAssignReport(reportObj: AllRecordsMainResponse.AllRecordsReport) {

        if ((reportObj.getHosUid() != null) && (reportObj.getAssign_user_id() != null)) {
            val intent: Intent =
                Intent(this@NewProfileRecords.context, AssignUserActivity::class.java);
            //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("hos_uid", reportObj.getHosUid().toString());
            startActivity(intent);

        } else {
            Toast.makeText(
                this@NewProfileRecords.context,
                "You can not assign this report",
                Toast.LENGTH_SHORT
            ).show();
        }
    }

    fun assignReport(reportObj: AllRecordsMainResponse.AllRecordsReport) {

        val intent: Intent =
            Intent(this@NewProfileRecords.context, AssignReportActivity::class.java);
        intent.putExtra("current_user_id", userid_ExistingUser);
        intent.putExtra("patient_id", "");
        intent.putExtra("hos_uid", reportObj.getHosUid().toString());
        startActivity(intent);
    }

    fun callShareView(reportObj: AllRecordsMainResponse.AllRecordsReport) {

        strUrl = "";
        strUrl = reportObj.getDownload_url();

        if ((strUrl != null) && (strUrl.length > 5)) {
//            checkPermissionOpenSDCard();
        } else {
            Toast.makeText(this@NewProfileRecords.context, "File not available", Toast.LENGTH_SHORT)
                .show();
        }

    }

    fun callChartView(reportObj: AllRecordsMainResponse.AllRecordsReport) {


        readReports(reportObj, reportObj.getEncId(), reportObj.tableId);
        val intent: Intent =
            Intent(this@NewProfileRecords.context, TestReportsActivity::class.java);

//            if(selectedUuserId.equals("all")){
//                selectedUuserId=userid_ExistingUser;
//
//
//            }else{
//                intent.putExtra("patient_id", selectedUuserId);
//            }
        if (reportObj.getAssign_user_id() != null) {
            intent.putExtra("report_name", reportObj.getReportName());
            intent.putExtra("patient_id", reportObj.getAssign_user_id());
            intent.putExtra("enc_id", reportObj.getEncId());
            intent.putExtra("report_type", reportObj.getReportType());
            startActivity(intent);
        }

    }

    fun callDownloadView(reportObj: AllRecordsMainResponse.AllRecordsReport) {

        if (reportObj.getHosUid() == null) {
            Toast.makeText(this@NewProfileRecords.context, "File not available", Toast.LENGTH_SHORT)
                .show();
        } else {
            val url: String = reportObj.getDownload_url();

            if ((reportObj.getDownload_url() == null) && (reportObj.getDownload_url().length < 6)) {

                Toast.makeText(
                    this@NewProfileRecords.context,
                    "File not available",
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                readReports(reportObj, reportObj.getEncId(), reportObj.tableId);
                val browserIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

        }


    }

    fun displaySelectedMemberReports(obj: AllRecordsMainResponse.AllRecordsMember) {

        // txt_goalname.setTextColor(Color.parseColor("#ff860b"));
        //  img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background);

        selectedUuserId = obj.getId();

        if (obj.getName().equals("Add")) {
            val intent: Intent =
                Intent(this@NewProfileRecords.context, SelectFamilyMemberActivity::class.java);
            startActivity(intent);
        } else {
            val patientID: String = obj.getId();
            pref.setFamilyMemberId(patientID);
            val isLoadingNeed: Boolean = true;

            if (Utility.isInternetAvailable(this@NewProfileRecords.context)) {
                getAllReportsData(isLoadingNeed, patientID, false);
            } else {
                Toast.makeText(
                    this@NewProfileRecords.context,
                    R.string.toast_no_internet,
                    Toast.LENGTH_LONG
                ).show();
            }
        }
    }

    fun readReports(
        allRecordsReport: AllRecordsMainResponse.AllRecordsReport,
        enc_id: String,
        table_id: String
    ) {
        val appToken: String = pref.getUserToken();
        val report_type: String = "dataReport";

        val apiService: ApiInterface =
            ApiClient.getReportsApiClientNew().create(ApiInterface::class.java);

        val deleteReportObj = DeleteReportObject(enc_id, allRecordsReport.id, report_type, table_id)
        val call: Call<DeleteReportResponse> =
            apiService.getReadReportsNew(AppConfig.APP_BRANDING_ID, appToken, deleteReportObj);

        call.enqueue(object : Callback<DeleteReportResponse> {
            override fun onFailure(call: Call<DeleteReportResponse>, t: Throwable) {
                Toast.makeText(context, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }

            override fun onResponse(
                call: Call<DeleteReportResponse>,
                response: Response<DeleteReportResponse>
            ) {

                if (response.isSuccessful) {
                    getAllReportsData(true, "all", true);
                }
//
            }

        })


    }


}