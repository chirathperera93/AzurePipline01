package com.ayubo.life.ayubolife.notification.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.notification.adapter.NotificationsAdapter;
import com.ayubo.life.ayubolife.notification.model.NotificationData;
import com.ayubo.life.ayubolife.notification.model.NotificationsMainResponse;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.prochat.appointment.CommonPayload;
import com.ayubo.life.ayubolife.prochat.appointment.NewChatActivity;
import com.ayubo.life.ayubolife.prochat.appointment.NewMessage;
import com.ayubo.life.ayubolife.programs.ProgramActivity;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.signalr.SignalRSingleton;
import com.ayubo.life.ayubolife.timeline.AddComments_Activity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsListActivity extends AppCompatActivity {


    Gson gson = null;
    String userid_ExistingUser;
    PrefManager pref;

    ProgressDialog mProgressDialog;
    String strUrl = "";
    //    LinearLayout btn_backImgBtn_layout;
    ImageButton btn_backImgBtn_layout;
    ImageButton btn_back_Button;
    LinearLayout errorMsg_lay;
    private static final String TAG = "Download Task";
    TextView errorMsg;
    TextView txt_add_reports1, txt_add_reports2;
    FloatingActionButton fab;
    EditText editText;
    ArrayList<NotificationData> dataList;
    private static ArrayList<NotificationData> oldDataList = new ArrayList<NotificationData>();
    private List<NotificationData> source;
    NotificationsAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView main_recycler;
    SignalRSingleton signalRSingleton;
    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        signalRSingleton = SignalRSingleton.getInstance(getBaseContext());

        uiConstructor();


    }


    void uiConstructor() {

        editText = findViewById(R.id.edt_search_value);
        errorMsg = findViewById(R.id.errorMsg);
        errorMsg.setVisibility(View.INVISIBLE);
        errorMsg_lay = findViewById(R.id.errorMsg_lay);
        main_recycler = (RecyclerView) findViewById(R.id.main_recycler);
        mProgressDialog = new ProgressDialog(NotificationsListActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);

//        btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout = (ImageButton) findViewById(R.id.btn_backImgBtn_layout);
//        btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);


        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setFamilyMemberId("");
                finish();


            }
        });

//        btn_back_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pref.setFamilyMemberId("");
//                finish();
//            }
//        });


    }

    void dataConstructor() {

        pref = new PrefManager(this);


        userid_ExistingUser = pref.getLoginUser().get("uid");
        gson = new Gson();

        if (active) {
            signalRSingleton.setSignalRObjectListener(new SignalRSingleton.SignalRObjectListener() {
                @Override
                public void onReceiveNewMessageReady(CommonPayload commonPayload) {
                    NewMessage newMessage = new Gson().fromJson(commonPayload.getPayload(), NewMessage.class);

                    if (active) {

                        System.out.println(newMessage);


                        ArrayList<String> currentUserArrayList = new ArrayList<String>();
                        currentUserArrayList.add(pref.getLoginUser().get("uid").toString());

                        signalRSingleton.sendSignal(
                                "message_delivered",
                                new Gson().toJsonTree(newMessage).getAsJsonObject(),
                                currentUserArrayList
                        );

                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getSession_id().equals(newMessage.getSession_id())) {
                                dataList.get(i).setTitle(newMessage.getUser().getName() + ": " + newMessage.getMessage());
                                dataList.get(i).setUnread_count(dataList.get(i).getUnread_count() + 1);
                                dataList.get(i).setTimestamp(newMessage.getCreate_datetime());
                                JsonArray jsonArray = new Gson().toJsonTree(dataList).getAsJsonArray();
                                pref.setOldNotificationData(jsonArray);
                                runOnUiThread(new Runnable() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {
                                        dataList.sort(new NotificationDateSorter());
                                        System.out.println(dataList);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }


                }

                @Override
                public void onMessageTyping(CommonPayload commonPayload) {
                    if (active) {
                        System.out.println(commonPayload);
                        System.out.println(dataList);
                        NotificationData notificationData = null;

                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getSession_id().equals(commonPayload.getPayload().get("session_id").getAsString())) {

                                notificationData = dataList.get(i);
                            }
                        }

                        if (notificationData != null) {
                            if (commonPayload.getPayload().get("status").getAsString().equals("on")) {
                                notificationData.setTitle(commonPayload.getPayload().get("text").getAsString());


                            } else {
                                System.out.println(notificationData);
                                String s = pref.getOldNotificationData();

                                JsonParser parser = new JsonParser();
                                JsonElement tradeElement = parser.parse(s);
                                JsonArray trade = tradeElement.getAsJsonArray();
                                System.out.println(trade);

                                for (JsonElement jsonElement : trade) {
                                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                                    NotificationData notificationDataObj = new Gson().fromJson(jsonObject, NotificationData.class);
                                    if (notificationDataObj.getSession_id().equals(notificationData.getSession_id())) {
                                        notificationData.setTitle(notificationDataObj.getTitle());
                                    }

                                }


                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println(dataList);
                                    adapter.notifyDataSetChanged();

                                }
                            });
                        }
                    }

                }


                @Override
                public void onMessageAcknowledgement(CommonPayload commonPayload) {
                }

                @Override
                public void onUserStats(JsonObject jsonObject) {

                }
            });
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        active = true;
        dataConstructor();
        getNotificationsData();


    }

    void loadView(ArrayList<NotificationData> datalist) {

        if ((dataList != null && (dataList.size() > 0))) {
            dataList.clear();

        }
        dataList = datalist;
        source = new ArrayList<>(dataList);

        if (dataList.size() > 0) {
            errorMsg_lay.setVisibility(View.GONE);
            errorMsg.setVisibility(View.GONE);

        } else {
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg_lay.setVisibility(View.VISIBLE);
        }


        adapter = new NotificationsAdapter(this, dataList, userid_ExistingUser);

        adapter.setOnSelectNotificationListener(new NotificationsAdapter.OnClickNotificationCell() {
            @Override
            public void onSelectNotificationCell(NotificationData notiData) {
                ///mProgressDialog.show();
                clickNotificationCell(notiData);
            }


        });

        linearLayoutManager = new LinearLayoutManager(NotificationsListActivity.this, LinearLayoutManager.VERTICAL, false);
        main_recycler.setLayoutManager(linearLayoutManager);
        main_recycler.setItemAnimator(new DefaultItemAnimator());
        main_recycler.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


//    public void setReadStatus(String noti_id) {
//
//        String appToken = pref.getUserToken();
//
//        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
//        Call<Object> call = apiService.setReadNotification(AppConfig.APP_BRANDING_ID, appToken, noti_id);
//
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//
//
//            }
//        });
//    }

    public void setReadStatus(String noti_id) {

        String appToken = pref.getUserToken();

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<Object> call = apiService.setReadNotification(AppConfig.APP_BRANDING_ID, appToken, noti_id);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {


            }
        });
    }

    private void clickNotificationCell(NotificationData reportObj) {

        try {
            String notificationType = reportObj.getType();
            // reportObj.getIsRead()

            if (reportObj.getNotificationMasterId() == null) {


                Intent intent = new Intent(getBaseContext(), NewChatActivity.class);
                intent.putExtra("sessionId", reportObj.getSession_id());
                startActivity(intent);


            } else {
                setReadStatus(reportObj.getNotificationMasterId().toString());
                if (notificationType.equals("MEDICAL")) {
                    AyuboChatActivity.Companion.startActivity(this, reportObj.getRelateId().toString(), false, reportObj.getNewAPI(), reportObj.getSession_id());
                }
                if (notificationType.equals("COMMUNITY_POST")) {
                    Intent intent = new Intent(NotificationsListActivity.this, SingleTimeline_Activity.class);
                    intent.putExtra("related_by_id", reportObj.getRelateId().toString());
                    intent.putExtra("type", "system");
                    startActivity(intent);
                }
                if (notificationType.equals("SINGLE_POST")) {
                    String postID = reportObj.getRelateId().toString();
                    Intent intent = new Intent(NotificationsListActivity.this, AddComments_Activity.class);
                    intent.putExtra("type", "notifications");
                    intent.putExtra("position", postID);
                    startActivity(intent);
                }
                if (notificationType.equals("PROGRAM_POST")) {

                    Intent intent = new Intent(NotificationsListActivity.this, SingleTimeline_Activity.class);
                    intent.putExtra("related_by_id", reportObj.getRelateId().toString());
                    intent.putExtra("type", "program");
                    startActivity(intent);
                }
                if (notificationType.equals("REPORT")) {
                    Intent intent = new Intent(NotificationsListActivity.this, ReportDetailsActivity.class);
                    startActivity(intent);
                }
                if (notificationType.equals("JANASHAKTHI_POLICY")) {
                    String meta = reportObj.getRelateId().toString();
                    Intent in = new Intent(NotificationsListActivity.this, ProgramActivity.class);
                    in.putExtra("meta", meta);
                    startActivity(in);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //
    }


    public void getNotificationsData() {
        final ProgressDialog progressDialog = new ProgressDialog(NotificationsListActivity.this);
        progressDialog.setMessage("Please wait...");

        if (dataList == null) {
            progressDialog.show();
        }

        String appToken = pref.getUserToken();


        ApiInterface baseApiClientNewApiInterface = ApiClient.getBaseApiClientNew().create(ApiInterface.class);

        Call<ProfileDashboardResponseData> baseApiClientNewApiInterfaceCall = baseApiClientNewApiInterface.getSessions(AppConfig.APP_BRANDING_ID, appToken);


        baseApiClientNewApiInterfaceCall.enqueue(new Callback<ProfileDashboardResponseData>() {


            @Override
            public void onResponse(Call<ProfileDashboardResponseData> getSessionsCall, Response<ProfileDashboardResponseData> getSessionsResponse) {
//                if (progressDialog != null) {
//                    progressDialog.cancel();
//                }


                ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
                Call<NotificationsMainResponse> call = apiService.getAllNotifications(appToken);

                call.enqueue(new Callback<NotificationsMainResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<NotificationsMainResponse> call, Response<NotificationsMainResponse> response) {
                        ArrayList<NotificationData> notificationData = new ArrayList<>();
                        if (progressDialog != null) {
                            progressDialog.cancel();
                        }
                        if (response.isSuccessful()) {


                            if (response.body() != null) {
                                if (response.body().getResult() == 401) {
                                    Intent in = new Intent(NotificationsListActivity.this, LoginActivity_First.class);
                                    startActivity(in);
                                }
                                if (response.body().getResult() == 0) {

                                    // Code here .............
                                    NotificationsMainResponse res = response.body();

                                    for (int i = 0; i < res.getData().size(); i++) {
                                        NotificationData notification = res.getData().get(i);
                                        notification.setNewAPI(false);
                                        notification.setSession_id("");
                                        notification.setTimestamp(notification.getTimestamp() * 1000);
                                        if (notification.getIsRead()) {
                                            notification.setUnread_count(1);
                                        } else {
                                            notification.setUnread_count(0);
                                        }
                                        notification.setUnread_info(new JsonObject());
                                        notification.setStatus("active");

                                    }


                                    notificationData = (ArrayList<NotificationData>) res.getData();


                                }
                            }

                        } else {

                            errorMsg.setVisibility(View.VISIBLE);
                            errorMsg_lay.setVisibility(View.VISIBLE);
                            System.out.println("=========================");
                        }


                        if (getSessionsResponse.isSuccessful()) {
                            System.out.println(getSessionsResponse);
                            JsonArray sessionsResponseData = new Gson().toJsonTree(getSessionsResponse.body().getData()).getAsJsonArray();
                            System.out.println(sessionsResponseData);
                            List<NotificationData> sessionsData;
                            oldDataList = new ArrayList<NotificationData>();

                            try {
                                if (sessionsResponseData.size() > 0) {
                                    for (int i = 0; i < sessionsResponseData.size(); i++) {
                                        JsonObject sessionsResponseObj = sessionsResponseData.get(i).getAsJsonObject();
                                        NotificationData notificationDataObj = new NotificationData();
                                        notificationDataObj.setNotificationMasterId(null);
                                        notificationDataObj.setType(sessionsResponseObj.get("type").getAsString());
                                        notificationDataObj.setRelateId(null);
                                        notificationDataObj.setTitle(sessionsResponseObj.get("user_details").getAsJsonObject().get("message_summary").getAsString());
                                        notificationDataObj.setHeader(sessionsResponseObj.get("user_details").getAsJsonObject().get("name").getAsString());
                                        notificationDataObj.setTimestamp(sessionsResponseObj.get("lastmessage_timestamp").getAsLong());

                                        if (sessionsResponseObj.get("lastmessage_timestamp").getAsInt() > 0) {
                                            notificationDataObj.setIsRead(false);
                                        } else {
                                            notificationDataObj.setIsRead(true);
                                        }


                                        notificationDataObj.setPatientId(false);
                                        notificationDataObj.setCommunityId(null);
                                        notificationDataObj.setIcon(sessionsResponseObj.get("user_details").getAsJsonObject().get("image_url").getAsString());


                                        int userUnreadCount = 0;


                                        try {
                                            JSONObject jObj = new JSONObject(String.valueOf(sessionsResponseObj.get("unread_info").getAsJsonObject()));
                                            Iterator<String> keys = jObj.keys();
                                            while (keys.hasNext()) {
                                                String key = keys.next();

                                                if (key.equals(userid_ExistingUser)) {
                                                    userUnreadCount = sessionsResponseObj.get("unread_info").getAsJsonObject().get(key).getAsInt();
                                                }


                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        notificationDataObj.setUnread_count(userUnreadCount);


                                        notificationDataObj.setNewAPI(true);
                                        notificationDataObj.setSession_id(sessionsResponseObj.get("id").getAsString());
                                        notificationDataObj.setUnread_info(sessionsResponseObj.get("unread_info").getAsJsonObject());

                                        notificationDataObj.setStatus(sessionsResponseObj.get("status").getAsString());


                                        oldDataList.add(notificationDataObj);


                                        notificationData.add(notificationDataObj);

                                        JsonArray jsonArray = new Gson().toJsonTree(oldDataList).getAsJsonArray();
                                        pref.setOldNotificationData(jsonArray);
                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                        if (notificationData.size() > 0) {
                            notificationData.sort(new NotificationDateSorter());
                            loadView(notificationData);
                        } else {
                            errorMsg.setVisibility(View.VISIBLE);
                            errorMsg_lay.setVisibility(View.VISIBLE);
                        }


                    }

                    @Override
                    public void onFailure(Call<NotificationsMainResponse> call, Throwable t) {
                        if (progressDialog != null) {
                            progressDialog.cancel();
                            t.printStackTrace();
                            System.out.println(t);
                        }

                    }
                });


            }

            @Override
            public void onFailure(Call<ProfileDashboardResponseData> call, Throwable throwable) {
//                if (progressDialog != null) {
//                    progressDialog.cancel();
//                }

                throwable.printStackTrace();
                System.out.println(throwable);


            }
        });


    }

    public class NotificationDateSorter implements Comparator<NotificationData> {
        @Override
        public int compare(NotificationData o1, NotificationData o2) {

            Date date1 = new Date(o1.getTimestamp());
            Date date2 = new Date(o2.getTimestamp());

            return date2.compareTo(date1);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        active = false;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }
}






