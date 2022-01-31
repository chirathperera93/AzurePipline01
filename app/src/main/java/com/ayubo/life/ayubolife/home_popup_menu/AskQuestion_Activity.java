package com.ayubo.life.ayubolife.home_popup_menu;

import android.content.Intent;
import android.os.AsyncTask;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PaymentCheck_Activity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.quick_links.experts.AskAdapter;
import com.ayubo.life.ayubolife.quick_links.experts.Quick_Utility;
import com.ayubo.life.ayubolife.reports.adapters.FamilyMemberAdapter;
import com.ayubo.life.ayubolife.rest.ApiClient;

import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Util;
import com.ayubo.life.ayubolife.utility.Utility;
import com.flavors.changes.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AskQuestion_Activity extends AppCompatActivity {
    PrefManager pref;
    String userid_ExistingUser, hasToken, statusFromServiceAPI_db;

    ArrayList<Object> songsList;
    RecyclerView recyclerView;
    AskAdapter adapter;

    String activityName;
    String serviceName, activityHeading;
    TextView txt_activity_heading, header_menu_back;

    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    EditText editText;
    int trial;
    String status;
    String fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question_);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref = new PrefManager(AskQuestion_Activity.this);

        userid_ExistingUser = pref.getLoginUser().get("uid");
        hasToken = pref.getLoginUser().get("hashkey");

        //  FirebaseAnalytics Adding
        Bundle bPromocode_used = new Bundle();
        bPromocode_used.putString("section", "Ask");

        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Ask_section_visited", bPromocode_used);
        }

        songsList = new ArrayList<>();


        recyclerView = findViewById(R.id.list_chat_people_list);

        inflatert = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(AskQuestion_Activity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
        String data = null;
        boolean isNeedCallService = false;

        DBString dataObj = DBRequest.getCashDataByType(AskQuestion_Activity.this, "asq_data");
        if (dataObj == null) {
            isNeedCallService = true;
        } else {

            data = dataObj.getId();
            if ((data != null) && (data.length() > 10)) {
                displayView(data);
            }
            long minits = Util.getTimestampAsMinits(dataObj.getName());
            if (minits > 5) {
                isNeedCallService = true;
            }

        }
        isNeedCallService = true;

        if (isNeedCallService) {
            if (Utility.isInternetAvailable(this)) {
//                if((data!=null) && (data.length()>10)){
//                }else{
//                    MyProgressLoading.showLoading(AskQuestion_Activity.this,"Please wait...");
//                }
                MyProgressLoading.showLoading(AskQuestion_Activity.this, "Please wait...");
                updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
                task.execute(new String[]{ApiClient.BASE_URL_live});
            } else {
                textt.setText("Unable to detect an active internet connection");
                toastt.setView(layoutt);
                toastt.show();
            }
        }


    }

    public ArrayList<Object> getreportDataList(ArrayList<ChatObj> reportsList) {
        ArrayList<Object> dataList = null;
        dataList = new ArrayList<>();
        List<Object> wantToRemoveThisList = null;
        wantToRemoveThisList = new ArrayList<>();

        //Removing un wanted objects..........
        for (int j = 0; j < reportsList.size(); j++) {

            Object obj = reportsList.get(j);
            ChatObj docObj = (ChatObj) obj;

            if (!wantToRemoveThisList.contains(docObj.getSpeciality())) {
                wantToRemoveThisList.add(docObj.getSpeciality());
                String specilization = docObj.getSpeciality();
                dataList.add(specilization);
                System.out.println("=specilization=======================" + specilization);
                for (int jj = 0; jj < reportsList.size(); jj++) {
                    if (reportsList.get(jj).getSpeciality().equals(specilization)) {
                        dataList.add(reportsList.get(jj));
                    }
                }
            }

        }

        //======= Ended  Sorting Based OnDatess.... Reports ========================================


        return dataList;
    }

    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            MyProgressLoading.dismissDialog();

            if (statusFromServiceAPI_db.equals("0")) {


                if (Constants.type == Constants.Type.AYUBO) {
                    DBString dataObj = DBRequest.getCashDataByType(AskQuestion_Activity.this, "asq_data");
                    if (dataObj != null) {
                        displayView(dataObj.getId());
                    }
//                    if((status.equals("Paid")) || (status.equals("Trial"))){
//                        DBString dataObj=  DBRequest.getCashDataByType(AskQuestion_Activity.this,"asq_data");
//                        if(dataObj!=null){
//                            displayView(dataObj.getId());
//                        }
//                    }
//                    else if((status.equals("Unpaid")) && (trial==0)){
//
//                        Intent intent = new Intent(AskQuestion_Activity.this, PaymentCheck_Activity.class);
//                        intent.putExtra("status", "no");
//                        startActivity(intent);
//                        finish();
//                    }
//                    else if((status.equals("Unpaid")) && (trial==1)){
//
//                        Intent intent = new Intent(AskQuestion_Activity.this, PaymentCheck_Activity.class);
//                        intent.putExtra("status", "hide");
//                        startActivity(intent);
//                        finish();
//                    }else{
//                        System.out.println("................" );
//                    }


                } else if (Constants.type == Constants.Type.HEMAS) {
                    DBString dataObj = DBRequest.getCashDataByType(AskQuestion_Activity.this, "asq_data");
                    if (dataObj != null) {
                        displayView(dataObj.getId());
                    }
                } else if (Constants.type == Constants.Type.SHESHELLS) {

                    if ((status.equals("Paid")) || (status.equals("Trial"))) {
                        DBString dataObj = DBRequest.getCashDataByType(AskQuestion_Activity.this, "asq_data");
                        if (dataObj != null) {
                            displayView(dataObj.getId());
                        }
                    } else if ((status.equals("Unpaid")) && (trial == 0)) {

                        Intent intent = new Intent(AskQuestion_Activity.this, PaymentCheck_Activity.class);
                        intent.putExtra("status", "no");
                        startActivity(intent);
                        finish();
                    } else if ((status.equals("Unpaid")) && (trial == 1)) {

                        Intent intent = new Intent(AskQuestion_Activity.this, PaymentCheck_Activity.class);
                        intent.putExtra("status", "hide");
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println("................");
                    }
                }


            } else if (statusFromServiceAPI_db.equals("999")) {
                Toast.makeText(AskQuestion_Activity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AskQuestion_Activity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(AskQuestion_Activity.this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getChatExperts"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getMedicalExperts............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                System.out.println(".......getMedicalExperts...response..........." + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r = 0;

            String responseString = "";

            if (response != null) {

                r = response.getStatusLine().getStatusCode();
                if (r == 200) {
                    try {
                        responseString = EntityUtils.toString(response.getEntity());
                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {

                            String dat = jsonObj.optString("data").toString();

                            if (Constants.type == Constants.Type.AYUBO) {

//                                JSONObject payObject2 = new JSONObject(dat);
//                                trial = payObject2.optInt("trial");
//                                status = payObject2.optString("status");
//                                fee = payObject2.optString("fee");
//                                String experts = payObject2.optString("experts");

                                DBRequest.updateDoctorData(AskQuestion_Activity.this, dat, "asq_data");
                            } else if (Constants.type == Constants.Type.HEMAS) {
                                DBRequest.updateDoctorData(AskQuestion_Activity.this, dat, "asq_data");
                            } else if (Constants.type == Constants.Type.SHESHELLS) {

                                JSONObject payObject2 = new JSONObject(dat);
                                trial = payObject2.optInt("trial");
                                status = payObject2.optString("status");
                                fee = payObject2.optString("fee");
                                String experts = payObject2.optString("experts");


//                                String expert = payObject.optString("experts");
//
//                                JSONObject payObject2 = new JSONObject(expert);

                                DBRequest.updateDoctorData(AskQuestion_Activity.this, experts, "asq_data");
                            } else {

                            }


                            pref.setAskData("a");

                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    statusFromServiceAPI_db = "999";
                }
            }

        }
    }

    void displayView(String data) {

        JSONArray myListsAll = null;
        if (songsList != null) {
            if (songsList.size() > 0) {
                songsList.clear();
            }
        }

        try {
            myListsAll = new JSONArray(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myListsAll != null) {


            for (int i = 0; i < myListsAll.length(); i++) {
                DoctorObj docObj = null;
                JSONObject datajsonObj = null;
                try {
                    // String id,name,link,speciality,image,unread,status;

                    datajsonObj = (JSONObject) myListsAll.get(i);
                    String id = datajsonObj.optString("id");
                    String name = datajsonObj.optString("name");
                    String image = datajsonObj.optString("image");
                    String link = datajsonObj.optString("chatLink");
                    String unread = datajsonObj.optString("unread");
                    String speciality = datajsonObj.optString("speciality");
                    String status = datajsonObj.optString("status");

                    songsList.add(new ChatObj(id, name, link, speciality, image, unread, status));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        if ((songsList != null) && (songsList.size() > 0)) {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            ArrayList<Object> rearrangedDataList = Quick_Utility.getSortedAskDataList(songsList);

            adapter = new AskAdapter(this, rearrangedDataList);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);

            DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.my_recycle_divider));
            recyclerView.addItemDecoration(divider);

            View layout_back_to_home = findViewById(R.id.layout_back_to_home);
            ImageButton btn_backImgBtn = layout_back_to_home.findViewById(R.id.btn_backImgBtn);
            LinearLayout btn_backImgBtn_layout = layout_back_to_home.findViewById(R.id.btn_backImgBtn_layout);

            btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });

            View view_ask = findViewById(R.id.layout_search_button);
            editText = view_ask.findViewById(R.id.edt_search_value);

            TextView txt_activity_heading = view_ask.findViewById(R.id.txt_activity_heading);
            txt_activity_heading.setText("Ask an Expert");


            editText.setHint("Search any Speciality");

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

            adapter.setOnItemClickListener(new FamilyMemberAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Object object) {

                    ChatObj dataModel = (ChatObj) object;
//                    String weblink=dataModel.getLink().toString();
//                    Intent intent = new Intent(AskQuestion_Activity.this, DoctorWebviewActivity.class);
//                    String finalURL = ApiClient.BASE_URL + weblink;
//                    intent.putExtra("URL", finalURL);
//                    startActivity(intent);

                    System.out.println("==========ask==========" + dataModel.getId());
                    AyuboChatActivity.Companion.startActivity(AskQuestion_Activity.this, dataModel.getId().toString(), false, false, "");

                }
            });

        }


    }


}
