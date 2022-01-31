package com.ayubo.life.ayubolife.reports.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.reports.adapters.ReportDetailsAdapter;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.reports.model.DeleteReportObject;
import com.ayubo.life.ayubolife.reports.model.DeleteReportResponse;
import com.ayubo.life.ayubolife.reports.utility.Utility_Report;
import com.ayubo.life.ayubolife.reports.view.ReportDetailsView;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends AppCompatActivity {


    ReportDetailsView reportAction;
    String firstReportID;
    List<String> listDataHeader;
    HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>> listDataChild;
    ReportDetailsAdapter adapter;
    LinearLayout dateView_programs;
    LayoutInflater inflater;
    List<AllRecordsMainResponse.AllRecordsMember> memberList;
    List<Object> dataList;


    Gson gson = null;
    String selectedUuserId, report_name;
    PrefManager pref;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    Uri downloadUri;
    ProgressDialog mProgressDialog;
    String strUrl = "";
    String userid_ExistingUser;
    LinearLayout btn_backImgBtn_layout;
    ImageButton btn_back_Button;
    LinearLayout errorMsg_lay;
    private static final String TAG = "Download Task";
    TextView errorMsg;
    TextView txt_add_reports1, txt_add_reports2;
    FloatingActionButton uploadMediaRecord;
    EditText editText;
    private List<Object> source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        uiConstructor();

        dataConstructor();

        pref.setReportLastUpdated(true);

        getAllReportsData(true, selectedUuserId, true);


    }


    void dataConstructor() {

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        gson = new Gson();
        selectedUuserId = "all";

    }

    void uiConstructor() {

        View view_ask = findViewById(R.id.layout_search_button);
        editText = view_ask.findViewById(R.id.edt_search_value);


        EditText editText = view_ask.findViewById(R.id.edt_search_value);
        TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);
        txt_activity_heading.setText("Reports");
        editText.setHint("Search reports here");

        errorMsg = findViewById(R.id.errorMsg);
        errorMsg_lay = findViewById(R.id.errorMsg_lay);
        errorMsg_lay.setVisibility(View.INVISIBLE);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        mProgressDialog = new ProgressDialog(ReportDetailsActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        dateView_programs = (LinearLayout) findViewById(R.id.dateView);
        btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);

        uploadMediaRecord = (FloatingActionButton) findViewById(R.id.uploadMediaRecord);
        uploadMediaRecord.setImageDrawable(ContextCompat.getDrawable(ReportDetailsActivity.this, R.drawable.plusimages));
        uploadMediaRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                if (selectedUuserId.equals("all")) {
//
//                    showAlert_Add(ReportDetailsActivity.this, "", "Select a member before adding a new report");
//
//                } else {
//                    String ureel = ApiClient.BASE_URL + "index.php?module=PC_MedicalTestResults&action=manualMedicalResultForm&user=" + selectedUuserId;
//                    String newUrl = Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"), ureel);
//                    Intent intent = new Intent(ReportDetailsActivity.this, CommonWebViewActivity.class);
//                    intent.putExtra("URL", newUrl);
//                    startActivity(intent);
//                }

                Intent intent = new Intent(ReportDetailsActivity.this, UploadMediaReportActivity.class);

                intent.putExtra("memberList", (Serializable) memberList);
                intent.putExtra("dataList", (Serializable) dataList);
                intent.putExtra("firstReportID", (Serializable) firstReportID);
                intent.putExtra("selectedUuserId", (Serializable) selectedUuserId);
                startActivity(intent);


            }
        });


        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setFamilyMemberId("");
                finish();


            }
        });

        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setFamilyMemberId("");
                finish();
            }
        });
    }

    public void showAlert_Add(Context c, String title, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_only, null, false);
        builder.setView(layoutView);

        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_no.setText("CANCEL");

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();


            }
        });

        dialog.show();
    }


    void loadViewFromCash() {

        DBString dataObj = DBRequest.getCashDataByType(ReportDetailsActivity.this, "reports_all_reports_data");
        if (dataObj != null) {
            String products_list = null;
            products_list = dataObj.getId();
            if ((products_list != null) && (products_list.length() > 5)) {
                Type type = new TypeToken<AllRecordsMainResponse>() {
                }.getType();

                AllRecordsMainResponse dataObjmain = (AllRecordsMainResponse) gson.fromJson(products_list, type);
                ;

                loadView(dataObjmain);

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        pref.setFamilyMemberId("");

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isLoadingNeed = false;

        if ((pref.getReportLoadingStatus().equals("1"))) {

            if (Utility.isInternetAvailable(ReportDetailsActivity.this)) {
                getAllReportsData(isLoadingNeed, "all", true);
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        }

    }


    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(ReportDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (ReportDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(ReportDetailsActivity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            new DownloadingTask().execute();

        }
    }

    private class DownloadingTask extends AsyncTask<String, String, String> {

        File apkStorage = null;
        File outputFile = null;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(ReportDetailsActivity.this);
            //     this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            progressDialog.setMessage("Downloading report. Please wait...");
            this.progressDialog.show();
        }

        /**
         * Updating progress bar
         */
//        protected void onProgressUpdate(String... progress) {
//            // setting progress percentage
//            progressDialog.setProgress(Integer.parseInt(progress[0]));
//        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (downloadUri != null) {
                openShareView(downloadUri);
            }
            if (progressDialog != null) {
                progressDialog.cancel();
            }


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = null;
                int lengthOfFile = 0;
                HttpURLConnection urlConnection = null;
                //  strUrl="http://androhub.com/demo/demo.pdf";
                try {
                    url = new URL(strUrl);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Accept-Encoding", "identity");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Get File if SD card is present
                if (Utility_Report.isSDCardPresent()) {
                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + "Download");
                } else
                    Toast.makeText(ReportDetailsActivity.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

//                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e(TAG, "Directory Created.");
                }

                try {

                    outputFile = new File(apkStorage, getFileFirstName());//Create Output file in Main File

                    //Create New File if not present
                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                        Log.e(TAG, "File Created");
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                    // input stream to read file - with 8k buffer
                    //    InputStream input = new BufferedInputStream(url.openStream(), 8192);

                    lengthOfFile = 599999;
                    //   publishProgress("" + (int) ((10000 * 100) / lengthOfFile));

                    InputStream input = urlConnection.getInputStream();//Get InputStream for connection
                    //  InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    lengthOfFile = urlConnection.getContentLength();


                    // int totalSize = urlConnection.getContentLength();

                    Log.e(TAG, "................. ");
                    byte[] buffer = new byte[1024];//Set buffer type
                    int count = 0;//init length
                    long total = 0;

                    while ((count = input.read(buffer)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        //  publishProgress("" + (int) ((total * 100) / lengthOfFile));
                        Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                        // writing data to file
                        fos.write(buffer, 0, count);
                    }

                    if (Constants.type == Constants.Type.AYUBO) {
                        downloadUri = FileProvider.getUriForFile(ReportDetailsActivity.this, "com.ayubo.life.ayubolife", outputFile);
                    } else if (Constants.type == Constants.Type.SHESHELLS) {
                        downloadUri = FileProvider.getUriForFile(ReportDetailsActivity.this, "com.ayubo.life.sheshells", outputFile);
                    } else {
                        downloadUri = FileProvider.getUriForFile(ReportDetailsActivity.this, "com.ayubo.life.hemas", outputFile);
                    }

                    ReportDetailsActivity.this.grantUriPermission(ReportDetailsActivity.this.getPackageName(), downloadUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fos.close();
                    input.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

    public String getFileFirstName() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String firstName = String.valueOf(timestamp.getTime());

        firstName = firstName + "_TestReport" + ".pdf";

        return firstName;
    }

    public void deleteMemberReport(AllRecordsMainResponse.AllRecordsReport obj, String enc_id, String table_id) {
        //   mProgressDialog.show();
        String appToken = pref.getUserToken();
        String report_type = obj.getReportType();

        ApiInterface apiService = ApiClient.getReportsApiClientNewReadRecord().create(ApiInterface.class);
        DeleteReportObject deleteReportObj = new DeleteReportObject(enc_id, obj.getId(), report_type, table_id);
        Call<DeleteReportResponse> call = apiService.deletedMemberReportsNew(AppConfig.APP_BRANDING_ID, appToken, deleteReportObj);

        call.enqueue(new Callback<DeleteReportResponse>() {
            @Override
            public void onResponse(Call<DeleteReportResponse> call, Response<DeleteReportResponse> response) {

                if (response.isSuccessful()) {

                    //  pref.setReportLoadingStatus("1");

                    getAllReportsData(true, "all", true);
                    System.out.println("=======t======");

                }
            }

            @Override
            public void onFailure(Call<DeleteReportResponse> call, Throwable t) {

                Toast.makeText(ReportDetailsActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }

    public void readReports(AllRecordsMainResponse.AllRecordsReport allRecordsReport, String enc_id, String table_id) {
        String appToken = pref.getUserToken();
        String report_type = "dataReport";

        ApiInterface apiService = ApiClient.getReportsApiClientNewReadRecord().create(ApiInterface.class);
        DeleteReportObject deleteReportObj = new DeleteReportObject(enc_id, allRecordsReport.getId(), report_type, table_id);

        Call<DeleteReportResponse> call = apiService.getReadReportsNew(AppConfig.APP_BRANDING_ID, appToken, deleteReportObj);

        call.enqueue(new Callback<DeleteReportResponse>() {
            @Override
            public void onResponse(Call<DeleteReportResponse> call, Response<DeleteReportResponse> response) {

                if (response.isSuccessful()) {

                    //  pref.setReportLoadingStatus("1");
                    System.out.println("========t======");

                }
            }

            @Override
            public void onFailure(Call<DeleteReportResponse> call, Throwable t) {

                //  Toast.makeText(ReportDetailsActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======" + t);
            }
        });
    }

    public void getAllReportsData(boolean isLoadingNeed, String patient_id, final boolean isNeedToCash) {
        String appToken = pref.getUserToken();
        selectedUuserId = patient_id;
        mProgressDialog.show();


        ApiInterface apiService = ApiClient.getReportsApiClientNewForHemas().create(ApiInterface.class);
//        Call<AllRecordsMainResponse> call = apiService.getAllReportsNew(AppConfig.APP_BRANDING_ID, appToken, patient_id);
        Call<AllRecordsMainResponse> call = apiService.getAllReportsNew(AppConfig.APP_BRANDING_ID, appToken);

        call.enqueue(new Callback<AllRecordsMainResponse>() {
            @Override
            public void onResponse(Call<AllRecordsMainResponse> call, Response<AllRecordsMainResponse> response) {
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }

                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        if (response.body().getResult() == 401) {
                            Intent in = new Intent(ReportDetailsActivity.this, LoginActivity_First.class);
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
                if (mProgressDialog != null) {
                    mProgressDialog.cancel();
                }
                Toast.makeText(ReportDetailsActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
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

                    new DownloadingTask().execute();

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

                    new DownloadingTask().execute();
                }

                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void openShareView(Uri selectedImageUri) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
        shareIntent.setType("application/pdf");
        startActivity(Intent.createChooser(shareIntent, "Title.."));

    }


    List<String> getSortedDateList(List<String> dateHeadersListDust) {
        List<String> dateHeadersList = null;
        dateHeadersList = new ArrayList<>();
        for (int j = 0; j < dateHeadersListDust.size(); j++) {


            Date latestDate = null;
            for (int jj = 0; jj < dateHeadersListDust.size(); jj++) {

                if (jj < (dateHeadersListDust.size() - 1)) {


                    String strDate1 = dateHeadersListDust.get(jj);
                    String strDate2 = dateHeadersListDust.get(jj + 1);

                    Date date1 = null;
                    Date date2 = null;

                    try {
                        date1 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate1);
                        date2 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date1.compareTo(date2) > 0) {
                        System.out.println("Date1 is New than Date2");
                        latestDate = date1;
                    } else if (date1.compareTo(date2) < 0) {
                        System.out.println("Date1 is Old than Date2");
                        latestDate = date2;
                    } else {
                        System.out.println("Date1 is equal to Date2");
                    }
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            String strDate = dateFormat.format(latestDate);
            dateHeadersList.add(strDate);
            dateHeadersListDust.remove(strDate);

        }
        System.out.println("===================");

        return dateHeadersList;
    }


    void loadView(AllRecordsMainResponse mainData) {

        listDataChild = new HashMap<String, List<AllRecordsMainResponse.AllRecordsReport>>();
        listDataHeader = new ArrayList<String>();
        if ((memberList != null) && (memberList.size() > 0)) {
            memberList.clear();
        }

        memberList = mainData.getData().getMembers();
        pref.setReportMembers(memberList);
        memberList.add(new AllRecordsMainResponse.AllRecordsMember("Add", "Add", "Add", "", "", ""));

        List<AllRecordsMainResponse.AllRecordsReport> reportsList = mainData.getData().getReports();

        if ((dataList != null && (dataList.size() > 0))) {
            dataList.clear();
        }

        dataList = Utility_Report.getreportDataList(firstReportID, reportsList);
        source = new ArrayList<>(dataList);


        if (dataList.size() > 1) {
            errorMsg_lay.setVisibility(View.GONE);
            errorMsg.setVisibility(View.GONE);
        } else {
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg_lay.setVisibility(View.VISIBLE);
        }

        adapter = new ReportDetailsAdapter(this, dataList, memberList, firstReportID, selectedUuserId);
        linearLayoutManager = new LinearLayoutManager(ReportDetailsActivity.this, LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

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


        adapter.setOnSelectAssignUserListener(new ReportDetailsAdapter.OnClickAssignUserImage() {
            @Override
            public void onSelectAssignUser(AllRecordsMainResponse.AllRecordsReport reportObj) {
                reAssignReport(reportObj);

            }
        });
        adapter.setOnClickNotAssignIconListener(new ReportDetailsAdapter.OnClickNotAssignIcon() {
            @Override
            public void onSelectNotAssignIcon(AllRecordsMainResponse.AllRecordsReport reportObj) {
                assignReport(reportObj);
            }
        });
        adapter.setOnClickFamilyMemberListener(new ReportDetailsAdapter.OnClickONFamilyMemberIcon() {
            @Override
            public void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember obj) {

                displaySelectedMemberReports(obj);
            }
        });
        adapter.setOnClickDownloadReportListener(new ReportDetailsAdapter.OnClickDownloadReportListner() {
            @Override
            public void onSelectDownload(AllRecordsMainResponse.AllRecordsReport reportObj, int position) {
                callDownloadView(reportObj);

                //    ((AllRecordsMainResponse.AllRecordsReport) dataList.get(position)).setRead(1);

                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnClickViewReportListener(new ReportDetailsAdapter.OnClickViewReportListner() {
            @Override
            public void onSelectView(AllRecordsMainResponse.AllRecordsReport reportObj, int position) {

                callChartView(reportObj);

                //  ((AllRecordsMainResponse.AllRecordsReport) dataList.get(position)).setRead(1);

                adapter.notifyDataSetChanged();


            }
        });
        adapter.setOnClickShareReportListener(new ReportDetailsAdapter.OnClickShareReportListner() {
            @Override
            public void onSelectShare(AllRecordsMainResponse.AllRecordsReport reportObj) {
                callShareView(reportObj);
            }
        });
        adapter.setOnClickDeleteReportListener(new ReportDetailsAdapter.OnClickDeleteReportListner() {
            @Override
            public void onSelectDelete(AllRecordsMainResponse.AllRecordsReport reportObj, int position) {
                deleteMemberReport(reportObj, position);

            }


        });


    }

    void deleteMemberReport(AllRecordsMainResponse.AllRecordsReport reportObj, int pos) {

        showAlert_ConfirmDelete(ReportDetailsActivity.this, reportObj, pos, "Are you sure ?");

    }

    void showAlert_ConfirmDelete(Context c, final AllRecordsMainResponse.AllRecordsReport reportO, final int pos, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);


        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                dataList.remove(pos);
                adapter.notifyDataSetChanged();

                deleteMemberReport(reportO, reportO.getEncId(), reportO.getTestorder_id());


            }
        });
        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

            }
        });
        dialog.show();
    }

    void reAssignReport(AllRecordsMainResponse.AllRecordsReport reportObj) {

        if ((reportObj.getHosUid() != null) && (reportObj.getAssign_user_id() != null)) {
            Intent intent = new Intent(ReportDetailsActivity.this, AssignUserActivity.class);
            //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("hos_uid", reportObj.getHosUid().toString());
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "You can not assign this report", Toast.LENGTH_SHORT).show();
        }
    }

    void assignReport(AllRecordsMainResponse.AllRecordsReport reportObj) {

        Intent intent = new Intent(ReportDetailsActivity.this, AssignReportActivity.class);
        intent.putExtra("current_user_id", userid_ExistingUser);
        intent.putExtra("patient_id", "");
        intent.putExtra("hos_uid", reportObj.getHosUid().toString());
        startActivity(intent);
        finish();
    }

    void callShareView(AllRecordsMainResponse.AllRecordsReport reportObj) {

        strUrl = "";
        strUrl = reportObj.getDownload_url();

        if ((strUrl != null) && (strUrl.length() > 5)) {
            checkPermissionOpenSDCard();
        } else {
            Toast.makeText(ReportDetailsActivity.this, "File not available", Toast.LENGTH_SHORT).show();
        }

    }

    void callChartView(AllRecordsMainResponse.AllRecordsReport reportObj) {


        readReports(reportObj, reportObj.getEncId(), reportObj.getTableId());
        Intent intent = new Intent(ReportDetailsActivity.this, TestReportsActivity.class);

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
            intent.putExtra("report_type", reportObj.getReportType());
            startActivity(intent);
        }

    }

    void callDownloadView(AllRecordsMainResponse.AllRecordsReport reportObj) {

        if (reportObj.getHosUid() == null) {
            Toast.makeText(ReportDetailsActivity.this, "File not available", Toast.LENGTH_SHORT).show();
        } else {
            String url = reportObj.getDownload_url();

            if ((reportObj.getDownload_url() == null) && (reportObj.getDownload_url().length() < 6)) {

                Toast.makeText(ReportDetailsActivity.this, "File not available", Toast.LENGTH_SHORT).show();
            } else {
                readReports(reportObj, reportObj.getEncId(), reportObj.getTableId());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

        }


    }

    void displaySelectedMemberReports(AllRecordsMainResponse.AllRecordsMember obj) {

        // txt_goalname.setTextColor(Color.parseColor("#ff860b"));
        //  img_profile_shadow.setBackgroundResource(R.drawable.orange_circle_background);

        selectedUuserId = obj.getId();

        if (obj.getName().equals("Add")) {
            Intent in = new Intent(ReportDetailsActivity.this, SelectFamilyMemberActivity.class);
            startActivity(in);
        } else {
            String patientID = obj.getId();
            pref.setFamilyMemberId(patientID);
            boolean isLoadingNeed = true;

            if (Utility.isInternetAvailable(ReportDetailsActivity.this)) {
                getAllReportsData(isLoadingNeed, patientID, false);
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        }
    }


}
