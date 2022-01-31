package com.ayubo.life.ayubolife.reports.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.reports.adapters.AddReportForReviewAdapter;

import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.google.gson.Gson;

import androidx.recyclerview.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ayubo.life.ayubolife.activity.PrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddReportForReviewActivity extends BaseActivity {


    public ArrayList<String> selectedReportIDList;
    String firstReportID;
    List<String> listDataHeader;
    HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>> listDataChild;
    AddReportForReviewAdapter adapter;
    LinearLayout dateView_programs;
    List<AllRecordsMainResponse.AllRecordsMember> memberList;
    List<Object> dataList;
    Gson gson = null;
    String selectedUuserId;
    PrefManager pref;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    LinearLayoutManager linearLayoutManager;
    RecyclerView add_report_recycler;
    Uri downloadUri;
    // ProgressDialog mProgressDialog;
    String strUrl = "";
    String userid_ExistingUser;
    LinearLayout btn_backImgBtn_layout;
    ImageButton btn_back_Button;
    //  LinearLayout errorMsg_lay;
    private static final String TAG = "Download Task";
    TextView errorMsg, txt_next_button;
    EditText editText;

    ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_report_for_review);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        uiConstructor();

        dataConstructor();

        pref.setReportLastUpdated(true);

        getAllReportsData(true, selectedUuserId, true);

    }


    void dataConstructor() {
        selectedReportIDList = new ArrayList<>();
        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        gson = new Gson();
        selectedUuserId = "all";

    }

    void uiConstructor() {

        View view_ask = findViewById(R.id.layout_search_button);
        editText = view_ask.findViewById(R.id.edt_search_value);
        txt_next_button = findViewById(R.id.txt_next_button);


        EditText editText = view_ask.findViewById(R.id.edt_search_value);
        TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);
        txt_activity_heading.setText("Reports");
        editText.setHint("Search reports here");

        errorMsg = findViewById(R.id.errorMsg);

        add_report_recycler = findViewById(R.id.add_report_recycler);
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(false);
        dateView_programs = findViewById(R.id.dateView);
        btn_backImgBtn_layout = findViewById(R.id.btn_backImgBtn_layout);
        btn_back_Button = findViewById(R.id.btn_backImgBtn);

        txt_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> currentList = new ArrayList<String>();
                currentList = Ram.getSelectedReportIDList();

                if (currentList != null) {
                    for (int c = 0; c < selectedReportIDList.size(); c++) {
                        if (currentList.contains(selectedReportIDList.get(c))) {
                            // Dont add to list
                        } else {
                            currentList.add(selectedReportIDList.get(c));
                        }
                    }
                } else {
                    currentList = selectedReportIDList;
                }


                Ram.setSelectedReportIDList(currentList);

                if (currentList.size() > 0) {
                    String data = currentList.toString();
                    Intent intent = new Intent();
                    intent.putExtra("REPORTS_LIST", data);
                    setResult(12, intent);
                    finish();

                } else {
                    Toast.makeText(AddReportForReviewActivity.this, "Please select a report", Toast.LENGTH_LONG).show();
                }

            }
        });


        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.setFamilyMemberId("");

                Ram.setSelectedReportIDList(selectedReportIDList);
                String data = selectedReportIDList.toString();
                Intent intent = new Intent();
                intent.putExtra("REPORTS_LIST", data);
                setResult(12, intent);
                finish();

            }
        });

        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setFamilyMemberId("");

                Ram.setSelectedReportIDList(selectedReportIDList);
                String data = selectedReportIDList.toString();
                Intent intent = new Intent();
                intent.putExtra("REPORTS_LIST", data);
                setResult(12, intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref.setFamilyMemberId("");

        Ram.setSelectedReportIDList(selectedReportIDList);
        String data = selectedReportIDList.toString();
        Intent intent = new Intent();
        intent.putExtra("REPORTS_LIST", data);
        setResult(12, intent);
        finish();


    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isLoadingNeed = false;

        if ((pref.getReportLoadingStatus().equals("1"))) {

            if (Utility.isInternetAvailable(AddReportForReviewActivity.this)) {
                getAllReportsData(isLoadingNeed, "all", true);
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        }

    }


    public void getAllReportsData(boolean isLoadingNeed, String patient_id, final boolean isNeedToCash) {
        String appToken = pref.getUserToken();
        selectedUuserId = patient_id;
        progressBar.show();


        ApiInterface apiService = ApiClient.getReportsApiClient().create(ApiInterface.class);
        Call<AllRecordsMainResponse> call = apiService.getAllReports(AppConfig.APP_BRANDING_ID, appToken, patient_id);

        call.enqueue(new Callback<AllRecordsMainResponse>() {
            @Override
            public void onResponse(Call<AllRecordsMainResponse> call, Response<AllRecordsMainResponse> response) {
                progressBar.dismiss();
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(AddReportForReviewActivity.this, LoginActivity_First.class);
                            startActivity(in);
                        }
                        if (response.body().getResult() == 0) {
                            // Code here .............
                            Gson gson1 = new Gson();
                            AllRecordsMainResponse dataObj = (AllRecordsMainResponse) response.body();
                            pref.setReportLoadingStatus("0");
                            loadView(dataObj);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<AllRecordsMainResponse> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(AddReportForReviewActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {

                    //    new AddReportForReviewActivity.DownloadingTask().execute();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // new AddReportForReviewActivity.DownloadingTask().execute();
                }

                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    void loadView(AllRecordsMainResponse mainData) {

        listDataChild = new HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>>();
        listDataHeader = new ArrayList<String>();
        if ((memberList != null) && (memberList.size() > 0)) {
            memberList.clear();
        }

        memberList = mainData.getData().getMembers();


        memberList.add(new AllRecordsMainResponse.AllRecordsMember("Add", "Add", "Add", "", "", ""));

        List<AllRecordsMainResponse.AllRecordsReport> rawReportsList = mainData.getData().getReports();
        List<AllRecordsMainResponse.AllRecordsReport> reportsList = new ArrayList<>();

        for (int c = 0; c < rawReportsList.size(); c++) {
            AllRecordsMainResponse.AllRecordsReport objReport = (AllRecordsMainResponse.AllRecordsReport) rawReportsList.get(c);

            if (objReport.getDownload_url() == null) {

            } else {
                if (objReport.getDownload_url().length() > 0) {
                    reportsList.add(objReport);
                }


            }
        }

        if ((dataList != null && (dataList.size() > 0))) {
            dataList.clear();
        }

        dataList = Utility_Report.getreportDataList(firstReportID, reportsList);


        if (dataList.size() > 1) {
            // errorMsg_lay.setVisibility(View.GONE);
            errorMsg.setVisibility(View.GONE);
        } else {
            errorMsg.setVisibility(View.VISIBLE);
            // errorMsg_lay.setVisibility(View.VISIBLE);
        }

        adapter = new AddReportForReviewAdapter(this, dataList, memberList, firstReportID, selectedUuserId);
        linearLayoutManager = new LinearLayoutManager(AddReportForReviewActivity.this, LinearLayoutManager.VERTICAL, false);
        add_report_recycler.setLayoutManager(linearLayoutManager);
        add_report_recycler.setItemAnimator(new DefaultItemAnimator());
        add_report_recycler.setAdapter(adapter);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filterData(charSequence.toString());

                adapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter.setOnClickViewReportListener(new AddReportForReviewAdapter.OnClickViewReportListner() {
            @Override
            public void onSelectView(AllRecordsMainResponse.AllRecordsReport report) {

                AllRecordsMainResponse.AllRecordsReport obj = (AllRecordsMainResponse.AllRecordsReport) report;


                if (selectedReportIDList.size() > 0) {

                    if (selectedReportIDList.contains(report.getId())) {
                        selectedReportIDList.remove(report.getId());
                    } else {
                        selectedReportIDList.add(report.getId());
                    }

                } else {
                    selectedReportIDList.add(report.getId());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onOpenPDF(AllRecordsMainResponse.AllRecordsReport dataa) {

                if (dataa.getDownload_url() != null) {
                    onClickWeb(dataa.getDownload_url());
                    System.out.println("============================" + dataa.getDownload_url());
                }


                Toast.makeText(AddReportForReviewActivity.this, dataa.getDownload_url(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember history) {
                displaySelectedMemberReports(history);
            }
        });


    }

    void displaySelectedMemberReports(AllRecordsMainResponse.AllRecordsMember obj) {

        selectedUuserId = obj.getId();

        if (obj.getName().equals("Add")) {
            Intent in = new Intent(AddReportForReviewActivity.this, SelectFamilyMemberActivity.class);
            startActivity(in);
        } else {
            String patientID = obj.getId();
            pref.setFamilyMemberId(patientID);
            boolean isLoadingNeed = true;

            if (Utility.isInternetAvailable(AddReportForReviewActivity.this)) {
                getAllReportsData(isLoadingNeed, patientID, false);
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        }
    }


}
