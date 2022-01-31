package com.ayubo.life.ayubolife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.challenges.NewCHallengeActivity;
import com.ayubo.life.ayubolife.channeling.activity.DashboardActivity;
import com.ayubo.life.ayubolife.channeling.activity.SearchActivity;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;
import com.ayubo.life.ayubolife.experts.Activity.MyDoctor_Activity;
import com.ayubo.life.ayubolife.fragments.HomePage_Utility;
import com.ayubo.life.ayubolife.goals.AchivedGoal_Activity;
import com.ayubo.life.ayubolife.goals.PickAGoal_Activity;
import com.ayubo.life.ayubolife.home.ExpandableListAdapter;
import com.ayubo.life.ayubolife.home_popup_menu.AskQuestion_Activity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity;
import com.ayubo.life.ayubolife.janashakthionboarding.welcome.JanashakthiWelcomeActivity;
import com.ayubo.life.ayubolife.lifepoints.LifePointActivity;

import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity;
import com.ayubo.life.ayubolife.map_challenges.MapJoinChallenge_Activity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.ImageListObj;
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity;
import com.ayubo.life.ayubolife.post.activity.NativePostActivity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.programs.ProgramActivity;
import com.ayubo.life.ayubolife.reports.activity.ReportDetailsActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.satalite_menu.HelpFeedbackActivity;
import com.ayubo.life.ayubolife.timeline.OpenPostActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthViewActivityNew extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<DBString> listDataHeader;
    HashMap<String, List<ImageListObj>> listDataChild;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    String userid_ExistingUser, statusFromServiceAPI_db;
    ArrayList<ShareEntity> data;
    int sectionCount = 0;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_view_new);

        inflatert = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(HealthViewActivityNew.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        if (Utility.isInternetAvailable(this)) {
            progressDialog = new ProgressDialog(HealthViewActivityNew.this);
            progressDialog.show();
            progressDialog.setMessage("Loading..");
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }

        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<ImageListObj>>();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setDividerHeight(-10);

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  prepareListData();
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                int count = listAdapter.getGroupCount();
//                for (int i = 0; i <count ; i++)
//                    expListView.collapseGroup(i);

//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
//        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//
//
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                String headerType = listDataHeader.get(groupPosition).getName();

                ImageListObj obj = listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition);

                //String webLInk=listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition).getLink1();
                if (obj.getMeta().equals("-")) {

                } else {

                    if (headerType.equals("Healthy")) {
                        processAction(obj.getAction(), obj.getMeta());
                    } else if (headerType.equals("Chill Out")) {
                        processAction(obj.getAction(), obj.getMeta());
                    } else if (headerType.equals("What Experts Say")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getMeta()));
                        startActivity(browserIntent);
                    }

                }


                return false;
            }
        });

    }


    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (statusFromServiceAPI_db.equals("0")) {

                listAdapter = new ExpandableListAdapter(HealthViewActivityNew.this, listDataHeader, listDataChild);

                expListView.setAdapter(listAdapter);

                if (sectionCount == 1) {
                    expListView.expandGroup(0);
                }
                if (sectionCount == 2) {
                    expListView.expandGroup(0);
                    expListView.expandGroup(1);
                }
                if (sectionCount == 3) {
                    expListView.expandGroup(0);
                    expListView.expandGroup(1);
                    expListView.expandGroup(2);
                }

            } else if (statusFromServiceAPI_db.equals("999")) {

                Toast.makeText(HealthViewActivityNew.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(HealthViewActivityNew.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(HealthViewActivityNew.this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getCategoryHealthList"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                statusFromServiceAPI_db = "999";
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                statusFromServiceAPI_db = "999";
                e.printStackTrace();
            }
            int r = 0;

            String responseString = null;

            if (response != null) {
                try {
                    responseString = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    statusFromServiceAPI_db = "999";
                    e.printStackTrace();
                }
            }


            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {
                    try {
                        List<ImageListObj> videosList = null;
                        List<ImageListObj> tracksList = null;
                        List<ImageListObj> programsList = null;
                        statusFromServiceAPI_db = "999";
                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {

                            JSONArray videosmyListsAll = null;
                            JSONArray programsListsAll = null;
                            JSONArray tracksmyListsAll = null;

                            String dat = jsonObj.optString("data").toString();
                            JSONObject jsonObjdat = new JSONObject(dat);
                            data = new ArrayList<ShareEntity>();
                            String tracks = jsonObjdat.optString("tracks").toString();
                            String videos = jsonObjdat.optString("videos").toString();
                            String programs = jsonObjdat.optString("programs").toString();

                            tracksList = new ArrayList<ImageListObj>();
                            videosList = new ArrayList<ImageListObj>();
                            programsList = new ArrayList<ImageListObj>();

                            int tCount = 0;
                            int vCount = 0;
                            int pCount = 0;
                            try {
                                if (!programs.isEmpty()) {
                                    programsListsAll = new JSONArray(programs);
                                    pCount = programsListsAll.length();
                                }

                                if (!tracks.isEmpty()) {
                                    tracksmyListsAll = new JSONArray(tracks);
                                    tCount = tracksmyListsAll.length();
                                }

                                if (!videos.isEmpty()) {
                                    videosmyListsAll = new JSONArray(videos);
                                    vCount = videosmyListsAll.length();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (tCount != 0) {//for tracks
                                sectionCount++;
                                listDataHeader.add(new DBString("Time to", "Chill Out"));//for tracks
                                for (int i = 0; i < tracksmyListsAll.length(); i++) {

                                    JSONObject tracksjsonObj = null;
                                    try {
                                        tracksjsonObj = (JSONObject) tracksmyListsAll.get(i);
                                        String name = tracksjsonObj.optString("name").toString();
                                        String image = tracksjsonObj.optString("image").toString();
                                        String link1 = tracksjsonObj.optString("link").toString();
                                        String action = tracksjsonObj.optString("action").toString();
                                        String meta = tracksjsonObj.optString("meta").toString();

                                        tracksList.add(new ImageListObj(name, image, link1, link1, action, meta));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                listDataChild.put("Chill Out", tracksList);//for tracks
                            }
                            //=================================================
                            if (vCount != 0) {
                                sectionCount++;
                                listDataHeader.add(new DBString("Watch", "What Experts Say"));  //for video
                                for (int a = 0; a < videosmyListsAll.length(); a++) {

                                    JSONObject videosjsonObj = null;
                                    try {
                                        videosjsonObj = (JSONObject) videosmyListsAll.get(a);
                                        String name = videosjsonObj.optString("name").toString();
                                        String image = videosjsonObj.optString("image").toString();
                                        String link1 = videosjsonObj.optString("link").toString();
                                        String link2 = videosjsonObj.optString("link").toString();
                                        String action = videosjsonObj.optString("action").toString();
                                        String meta = videosjsonObj.optString("meta").toString();

                                        videosList.add(new ImageListObj(name, image, link1, link1, action, meta));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                listDataChild.put("What Experts Say", videosList); //for video

                            }
                            //=================================================
                            if (pCount != 0) {
                                sectionCount++;       //for programs


                                listDataHeader.add(new DBString("Stay", "Healthy"));//for programs
                                for (int a = 0; a < programsListsAll.length(); a++) {

                                    JSONObject programsjsonObj = null;
                                    try {
                                        programsjsonObj = (JSONObject) programsListsAll.get(a);
                                        String name = programsjsonObj.optString("name").toString();
                                        String image = programsjsonObj.optString("image").toString();
                                        String link1 = programsjsonObj.optString("link").toString();
                                        String link2 = programsjsonObj.optString("link").toString();


                                        String action = programsjsonObj.optString("action").toString();
                                        String meta = programsjsonObj.optString("meta").toString();

                                        programsList.add(new ImageListObj(name, image, link1, link1, action, meta));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                listDataChild.put("Healthy", programsList);//for programs

                            }


                            //==============================================

                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        statusFromServiceAPI_db = "999";
                        e.printStackTrace();
                    }
                } else {
                    statusFromServiceAPI_db = "999";
                }
            }

        }
    }


    void processAction(String action, String meta) {

        if (action.equals("programtimeline")) {
            if (meta.length() > 0) {
                onProgramNewDesignClick(meta);
            }
        }
        if (action.equals("program")) {
            onProgramPostClick(meta);
        }
        if (action.equals("challenge")) {
            onMapChallangeClick(meta);
        }
        if (action.equals("chat")) {
            onAskClick(meta);
        }
        if (action.equals("common")) {
            String imgUrl = ApiClient.BASE_URL + meta;
            onCommonViewClick(imgUrl);
        }
        if (action.equals("commonview")) {
            String imgUrl = ApiClient.BASE_URL + meta;
            onCommonViewClick(imgUrl);
        }
        if (action.equals("web")) {
            onBowserClick(meta);
        }
        if (action.equals("help")) {
            onHelpClick(meta);
        }
        if (action.equals("reports")) {
            onReportsClick(meta);
        }
        if (action.equals("goal")) {
            onGoalClick(meta);
        }
        if (action.equals("videocall")) {
            onVideoCallClick(meta);
        }
        if (action.equals("channeling")) {
            onButtonChannelingClick(meta);
        }
        if (action.equals("janashakthiwelcome")) {
            onJanashakthiWelcomeClick(meta);
        }
        if (action.equals("janashakthireports")) {
            onJanashakthiReportsClick(meta);
        }
        if (action.equals("dynamicquestion")) {
            onDyanamicQuestionClick(meta);
        }
        if (action.equals("post")) {
            onPostClick(meta);
        }
        if (action.equals("native_post")) {
            onNativePostClick(meta);
        }

    }


    void startDoctorsActivity(String doctorID) {
        DocSearchParameters parameters;
        parameters = new DocSearchParameters();
        PrefManager pref = new PrefManager(this);
        parameters.setUser_id(pref.getLoginUser().get("uid"));
        parameters.setDate("");
        parameters.setDoctorId(doctorID);
        parameters.setLocationId("");
        parameters.setSpecializationId("");

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(parameters));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, "");
        startActivity(intent);
    }

    void onButtonChannelingClick(String meta) {
        if (meta.length() > 0) {
            startDoctorsActivity(meta);
        } else {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    void onVideoCallClick(String meta) {
        if (meta.length() > 0) {
            String activity = "my_doctor";
            Intent intent = new Intent(this, MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", meta);
            intent.putExtra("activityName", activity);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MyDoctor_Activity.class);
            intent.putExtra("activityName", "myExperts");
            startActivity(intent);
        }


    }


    void onMapChallangeClick(String meta) {

        if (meta.length() > 0) {
            Intent intent = new Intent(this, MapChallangeKActivity.class);
            intent.putExtra("challenge_id", meta);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, NewCHallengeActivity.class);
            startActivity(intent);
        }
    }

    void onAskClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, AyuboChatActivity.class);
            intent.putExtra("doctorId", meta);
            intent.putExtra("isAppointmentHistory", false);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AskQuestion_Activity.class);
            startActivity(intent);
        }
    }

    void onProgramNewDesignClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, ProgramActivity.class);
            intent.putExtra("meta", meta);
            startActivity(intent);
        }
    }

    void onProgramPostClick(String meta) {
        if (meta.length() > 0) {
            Intent intent = new Intent(this, SingleTimeline_Activity.class);
            intent.putExtra("related_by_id", meta);
            intent.putExtra("type", "program");
            startActivity(intent);
        }
    }

    void onGoalClick(String meta) {

        if (meta.length() > 0) {
            PrefManager prefManager = new PrefManager(this);
            String status = prefManager.getMyGoalData().get("my_goal_status");

            if (status == "Pending") {
                Intent intent = new Intent(this, AchivedGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Pick") {
                Intent intent = new Intent(this, PickAGoal_Activity.class);
                startActivity(intent);
            } else if (status == "Completed") {
                HomePage_Utility serviceObj = new HomePage_Utility(this);
                serviceObj.showAlert_Deleted(this, "This goal has been achieved for the day. Please pick another goal tomorrow");
            }
        }
    }

    void onNativePostClick(String meta) {
        Intent intent = new Intent(this, NativePostActivity.class);
        intent.putExtra("meta", meta);
        startActivity(intent);
    }

    void onPostClick(String meta) {
        Intent intent = new Intent(this, OpenPostActivity.class);
        intent.putExtra("postID", meta);
        startActivity(intent);
    }

    void onJanashakthiWelcomeClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setRelateID(meta);
        pref.setIsJanashakthiWelcomee(true);
        Intent intent = new Intent(this, JanashakthiWelcomeActivity.class);
        startActivity(intent);
    }

    void onDyanamicQuestionClick(String meta) {
        PrefManager pref = new PrefManager(this);
        pref.setIsJanashakthiDyanamic(true);
        pref.setRelateID(meta);
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    void onJanashakthiReportsClick(String meta) {
        MedicalUpdateActivity.startActivity(this);
    }

    void onReportsClick(String meta) {
        Intent intent = new Intent(this, ReportDetailsActivity.class);
        intent.putExtra("data", "all");
        Ram.setReportsType("fromHome");
        startActivity(intent);
    }

    void onLifePointsClick(String activityName, String meta) {
        Intent intent = new Intent(this, LifePointActivity.class);
        startActivity(intent);
    }

    void onHelpClick(String meta) {
        Intent intent = new Intent(this, HelpFeedbackActivity.class);
        intent.putExtra("activityName", "myExperts");
        startActivity(intent);
    }

    void onBowserClick(String meta) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(meta));
        startActivity(browserIntent);
    }

    void onCommonViewClick(String meta) {
        Intent intent = new Intent(this, CommonWebViewActivity.class);
        intent.putExtra("URL", meta);
        startActivity(intent);
    }


}

