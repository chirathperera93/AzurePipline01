package com.ayubo.life.ayubolife.experts.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.db.DatabaseHandler;
import com.ayubo.life.ayubolife.experts.fragments.AppointmentLeftFragment;
import com.ayubo.life.ayubolife.home_popup_menu.AppointmentCommonObj;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.pojo.doctor_search.Appointments;
import com.ayubo.life.ayubolife.pojo.doctor_search.Data;
import com.ayubo.life.ayubolife.pojo.doctor_search.Expert;
import com.ayubo.life.ayubolife.pojo.doctor_search.MainResponse;
import com.ayubo.life.ayubolife.pojo.doctor_search.Previou;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.CircleNetworkImageView;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Util;
import com.ayubo.life.ayubolife.webrtc.App;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDoctor_Activity extends AppCompatActivity {
    PrefManager pref;
    String userid_ExistingUser, hasToken;

    ArrayList<Object> songsList;
    ArrayList<AppointmentCommonObj> appointmentsTodayServerList;
    ArrayList<AppointmentCommonObj> appointmentsUpcomingServerList;
    ArrayList<AppointmentCommonObj> appointmentsPreviourServerList;
    int sectionCount = 0;


    //    ListView list;
    CustomAdapter adapter;
    ImageButton btn_backImgBtn;

    String activityName;
    String serviceName, activityHeading;


    List<DBString> listDataHeader;
    HashMap<String, List<AppointmentCommonObj>> listDataChild;

    public static Data doctor_fullDataObject = null;
    public static Data expert_fullDataObject = null;
    List<Expert> expertList;
    List<Previou> previousList;
    List<Previou> todayList;
    List<Previou> nextList;
    boolean isNeedCallService = false;
    boolean isNeedShowProgress = false;
    String stringData = null;
    boolean hasCashedData = false;

    void uiConstructor() {


//        list = (ListView) findViewById(R.id.list_doctor_search2);


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


        loadFragment(new AppointmentLeftFragment());


    }

    void dataConstructor() {
        pref = new PrefManager(MyDoctor_Activity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        hasToken = pref.getLoginUser().get("hashkey");
        activityName = getIntent().getStringExtra("activityName");
        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<AppointmentCommonObj>>();

        appointmentsTodayServerList = new ArrayList<AppointmentCommonObj>();
        appointmentsUpcomingServerList = new ArrayList<AppointmentCommonObj>();
        appointmentsPreviourServerList = new ArrayList<AppointmentCommonObj>();

        //  FirebaseAnalytics Adding
        Bundle bPromocode_used = new Bundle();
        bPromocode_used.putString("section", activityName);
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Doctor_section_visited", bPromocode_used);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor_);

        uiConstructor();

        dataConstructor();


        if (activityName.equals("my_doctor")) {

            DBString dataObj = DBRequest.getCashDataByType(MyDoctor_Activity.this, "doctor_data");
            if (dataObj == null) {
                isNeedCallService = true;
                isNeedShowProgress = true;
            } else {
                stringData = dataObj.getId();
                long minits = Util.getTimestampAsMinits(dataObj.getName());
                if (minits > 5) {
                    isNeedCallService = true;
                }

            }
            activityHeading = "Medical Doctor";
            serviceName = "getMedicalExpertsNew";

            if (doctor_fullDataObject != null) {
                Log.d("Doctor Class", "data available");
                displayDataFromService(doctor_fullDataObject);
                hasCashedData = true;

            } else {

                if (stringData == null) {

                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    Data readValue = null;
                    try {
                        readValue = mapper.readValue(stringData, Data.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (readValue != null) {
                        displayDataFromService(readValue);
                    } else {
                        isNeedShowProgress = true;
                    }
                }
            }
            if (isNeedCallService) {
                System.out.println("========Calling Service ======================");
                getDoctors(serviceName);
            }
        } else if (activityName.equals("myExperts")) {

            DBString dataObj = DBRequest.getCashDataByType(MyDoctor_Activity.this, "expert_data");
            if (dataObj == null) {
                isNeedCallService = true;
                isNeedShowProgress = true;
            } else {
                stringData = dataObj.getId();
                long minits = Util.getTimestampAsMinits(dataObj.getName());

                if (minits > 5) {
                    isNeedCallService = true;
                }
            }


            serviceName = "getLifeStyleExpertsNew";
            activityHeading = "Expert";

            if (expert_fullDataObject != null) {
                displayDataFromService(expert_fullDataObject);
                hasCashedData = true;
            } else {

                if (stringData == null) {

                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    Data readValue = null;
                    try {
                        readValue = mapper.readValue(stringData, Data.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (readValue != null) {
                        displayDataFromService(readValue);
                    } else {
                        isNeedShowProgress = true;
                    }
                }
            }
            if (isNeedCallService) {
                System.out.println("========Calling Service ======================");
                getDoctors(serviceName);
            }
        } else if (activityName.equals("phy_chanelling")) {
            DBString dataObj = DBRequest.getCashDataByType(MyDoctor_Activity.this, "Physiotherapy");
            if (dataObj == null) {
                isNeedCallService = true;
                isNeedShowProgress = true;
            } else {
                stringData = dataObj.getId();
                long minits = Util.getTimestampAsMinits(dataObj.getName());

                if (minits > 5) {
                    isNeedCallService = true;
                }
            }


            serviceName = "getLifeStyleExpertsNew";
            activityHeading = "Physiotherapist";

            if (expert_fullDataObject != null) {
                displayDataFromService(expert_fullDataObject);
                hasCashedData = true;
            } else {

                if (stringData == null) {

                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    Data readValue = null;
                    try {
                        readValue = mapper.readValue(stringData, Data.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (readValue != null) {
                        displayDataFromService(readValue);
                    } else {
                        isNeedShowProgress = true;
                    }
                }
            }
            if (isNeedCallService) {
                System.out.println("====p====Calling Service ======================");
                getPhysio("getLifeStyleExpertsPhysioTherapy");
            }

        }


        Ram.setServiceName(serviceName);


        if (isNeedShowProgress) {
            MyProgressLoading.showLoading(MyDoctor_Activity.this, "Please wait...");
        }


    }


    // DownloadJSON AsyncTask
    private class DisplyFromDatabase extends AsyncTask<Void, Void, Data> {


        @Override
        protected Data doInBackground(Void... arg0) {
            Data data = null;
            String jsonStringData = null;


            ObjectMapper mapper = new ObjectMapper();

            if (activityName.equals("my_doctor")) {
                DBString jsonStringDataObj = DBRequest.getCashDataByType(MyDoctor_Activity.this, "doctor_data");

                jsonStringData = jsonStringDataObj.getId();
                // jsonStringData = pref.getDoctorData();
                if ((jsonStringData != null) && (jsonStringData.length() > 10)) {
                    hasCashedData = true;
                    try {
                        doctor_fullDataObject = mapper.readValue(jsonStringData, Data.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (activityName.equals("myExperts")) {

                DBString jsonStringDataObj = DBRequest.getCashDataByType(MyDoctor_Activity.this, "expert_data");

                jsonStringData = jsonStringDataObj.getId();

                if ((jsonStringData != null) && (jsonStringData.length() > 10)) {
                    hasCashedData = true;
                    try {
                        expert_fullDataObject = mapper.readValue(jsonStringData, Data.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(Data data) {

            MyProgressLoading.dismissDialog();

            String jsonStringData = null;
            if (activityName.equals("my_doctor")) {
                displayDataFromService(doctor_fullDataObject);
            } else if (activityName.equals("myExperts")) {
                displayDataFromService(expert_fullDataObject);
            }


        }

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    public void getDoctors(String serviceName) {
        userid_ExistingUser = pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResponse> call = apiService.getDoctors(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MyProgressLoading.dismissDialog();

                boolean status = response.isSuccessful();
                if (status) {


                    final Data data = response.body().getData();


                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            //TODO your background code
                            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            try {
                                String json = null;
                                json = ow.writeValueAsString(data);

                                if (activityName.equals("my_doctor")) {
                                    DBRequest.updateDoctorData(MyDoctor_Activity.this, json, "doctor_data");

                                } else if (activityName.equals("myExperts")) {
                                    DBRequest.updateDoctorData(MyDoctor_Activity.this, json, "expert_data");
                                }

                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    displayDataFromService(data);

                }

            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

                MyProgressLoading.dismissDialog();
                Toast.makeText(MyDoctor_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                System.out.println("========t======" + t);
            }
        });
    }

    public void getPhysio(String serviceName) {
        userid_ExistingUser = pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";


        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResponse> call = apiService.getPhysio(AppConfig.APP_BRANDING_ID, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MyProgressLoading.dismissDialog();

                boolean status = response.isSuccessful();
                if (status) {


                    final Data data = response.body().getData();


                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            //TODO your background code
                            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                            try {
                                String json = null;
                                json = ow.writeValueAsString(data);

                                if (activityName.equals("Physiotherapy")) {
                                    DBRequest.updateDoctorData(MyDoctor_Activity.this, json, "Physiotherapy");

                                }

                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    displayDataFromService(data);

                }

            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {

                MyProgressLoading.dismissDialog();
                Toast.makeText(MyDoctor_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

                System.out.println("========t======" + t);
            }
        });
    }

    void displayDataFromService(Data data) {

        if (data != null) {


            if ((expertList != null) && (expertList.size() > 0)) {
                expertList.clear();
            }
            if ((previousList != null) && (previousList.size() > 0)) {
                previousList.clear();
            }
            if ((todayList != null) && (todayList.size() > 0)) {
                todayList.clear();
            }
            if ((nextList != null) && (nextList.size() > 0)) {
                nextList.clear();
            }
            expertList = data.getExperts();
            Appointments appointmentList = data.getAppointments();

            if (appointmentList != null) {
                previousList = appointmentList.getPrevious();
                todayList = appointmentList.getToday();
                nextList = appointmentList.getUpcoming();
            }
            processDataFromService();

        }
    }

    void displayDataFromServiceOld(String strdata) {

        Gson gson = new GsonBuilder().create();
        Data data = gson.fromJson(strdata, Data.class);
        if (data != null) {

            expertList = data.getExperts();
            Appointments appointmentList = data.getAppointments();
            previousList = appointmentList.getPrevious();
            todayList = appointmentList.getToday();
            nextList = appointmentList.getUpcoming();
            processDataFromService();
            System.out.println("============");
        }
    }

    void processDataFromService() {

        try {
            if (todayList.size() > 0) {
                listDataHeader.add(new DBString("", "Today"));//for Today
                sectionCount++;
                for (int i = 0; i < todayList.size(); i++) {
                    Previou obj = todayList.get(i);
                    appointmentsTodayServerList.add(new AppointmentCommonObj(obj.getDocname(), obj.getEnds(), obj.getLocation(), obj.getPatient(), obj.getSpecializationC(),
                            obj.getCancel(), obj.getLink(), obj.getId(), obj.getStatus()));
                }
                listDataChild.put("Today", appointmentsTodayServerList);//for tracks
            }
            //======================================
            if (nextList.size() > 0) {
                listDataHeader.add(new DBString("", "Upcoming"));//for Upcoming
                sectionCount++;
                for (int i = 0; i < nextList.size(); i++) {
                    Previou obj = nextList.get(i);
                    appointmentsUpcomingServerList.add(new AppointmentCommonObj(obj.getDocname(), obj.getEnds(), obj.getLocation(), obj.getPatient(), obj.getSpecializationC(),
                            obj.getCancel(), obj.getLink(), obj.getId(), obj.getStatus()));
                }
                listDataChild.put("Upcoming", appointmentsUpcomingServerList);//for Upcoming
            }
            //===============================================
            if (previousList.size() > 0) {
                listDataHeader.add(new DBString("", "Previous"));//for Previous
                sectionCount++;
                for (int i = 0; i < previousList.size(); i++) {
                    Previou obj = previousList.get(i);
                    appointmentsPreviourServerList.add(new AppointmentCommonObj(obj.getDocname(), obj.getEnds(), obj.getLocation(), obj.getPatient(), obj.getSpecializationC(),
                            obj.getCancel(), obj.getLink(), obj.getId(), obj.getStatus()));
                }
                listDataChild.put("Previous", appointmentsPreviourServerList);//for Previous
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Ram.setListDataHeader(listDataHeader);
        Ram.setListDataChild(listDataChild);
        Ram.setAppointRawCount(sectionCount);

        songsList = new ArrayList<>();
//==========================================================
        //   expertList
        for (int i = 0; i < expertList.size(); i++) {
            try {
                Expert obj = expertList.get(i);

                String name = obj.getSalutation() + " " + obj.getFirstName() + " " + obj.getLastName();
                songsList.add(new DoctorObj(obj.getId(), name, obj.getSpecializationC(), obj.getPicture()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Ram.setDoctorDataList(songsList);


        Log.d("Doctor Class", "before isFinishing");
        if (!isFinishing()) {
            loadFragment(new AppointmentLeftFragment());
        }


    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        //  fragmentTransaction.commit(); // save the changes
        fragmentTransaction.commitAllowingStateLoss();
    }


    class CustomAdapter extends ArrayAdapter<DoctorObj> implements View.OnClickListener {


        // implements View.OnClickListener
        private ArrayList<DoctorObj> dataSet;
        Context mContext;
        Context mContext2;

        SharedPreferences prefs;
        String userid_ExistingUser;
        DatabaseHandler db;
        ListView list;

        ArrayList<DoctorObj> songsList;
        String consultant_id;
        String expert_time;
        String expert_date;
        ProgressDialog prgDialog;
        android.app.FragmentManager fm;
        StringBuilder locationString;


        ImageLoader imageLoader = App.getInstance().getImageLoader();

        @Override
        public void onClick(View view) {

            int position = (Integer) view.getTag();
            Object object = getItem(position);
            DoctorObj dataModel = (DoctorObj) object;
            String doctor_id = dataModel.getId().toString();
            Intent intent = new Intent(getContext(), MyDoctorLocations_Activity.class);
            intent.putExtra("doctor_id", doctor_id);
            intent.putExtra("activityName", activityName);

            startActivity(intent);

        }

        // View lookup cache
        private class ViewHolder {
            TextView txtName;
            LinearLayout middleText;
            TextView txtVersion, location1, location2;
            ImageView btn_booknow, btn_video, btn_build;
            // ImageView info;
            CircleNetworkImageView thumbNail;
        }


        public CustomAdapter(ArrayList<DoctorObj> data, Context context) {
            super(context, R.layout.list_row, data);
            this.dataSet = data;
            this.mContext = context;
            NewHomeWithSideMenuActivity bbb = new NewHomeWithSideMenuActivity();

        }


        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DoctorObj dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_row, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_doctorName9);
                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.txt_category9);
                viewHolder.btn_booknow = (ImageButton) convertView.findViewById(R.id.btn_booknow);
                viewHolder.middleText = (LinearLayout) convertView.findViewById(R.id.middleText);

                if (imageLoader == null) {
                    imageLoader = App.getInstance().getImageLoader();
                    viewHolder.thumbNail = (CircleNetworkImageView) convertView
                            .findViewById(R.id.list_image);
                }
                imageLoader = App.getInstance().getImageLoader();
                viewHolder.thumbNail = (CircleNetworkImageView) convertView
                        .findViewById(R.id.list_image);

                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result = convertView;
            }

            String sname = dataModel.getName();
            String imageUrl = dataModel.getImage();

            System.out.println("=================Img URl ======================" + imageUrl);
            lastPosition = position;
            //   String lo= dataModel.getLocation();
            List<DBString> myLocationsList = new ArrayList<>();
            //  myLocationsList= Utility.getLocationList(lo);
            StringBuilder locationString = new StringBuilder();
            boolean isVideo = false;
            boolean isWTC = false;
            String locv = "";
            String locl = "";
            for (DBString oneloca : myLocationsList) {
                System.out.println("=============location==================" + oneloca.getName());
                //  loc=oneloca.getName();
                locationString.append(oneloca.getName());
                locationString.append(",");
                if (oneloca.getName().equals("Video Chat")) {
                    isVideo = true;
                    locv = oneloca.getName();
                }
                if (oneloca.getName().equals("WTC .Life studio")) {
                    isWTC = true;
                    locl = oneloca.getName();
                }
                if (oneloca.getName().equals("WTC Wellness Studio")) {
                    isWTC = true;
                    locl = oneloca.getName();
                }
                if (oneloca.getName().equals("Orion Wellness Center")) {
                    isWTC = true;
                    locl = oneloca.getName();
                }


            }

            String sp = dataModel.getSpec();

            if (sp == null || sp == "null") {
                sp = "";
            }
            if (sname == null || sname == "null") {
                sname = "";
            }


            viewHolder.txtName.setText(sname);
            viewHolder.txtVersion.setText(sp);
            viewHolder.middleText.setOnClickListener((View.OnClickListener) this);
            viewHolder.btn_booknow.setOnClickListener((View.OnClickListener) this);
            viewHolder.btn_booknow.setTag(position);
            viewHolder.middleText.setTag(position);

            String imageURL = ApiClient.BASE_URL + imageUrl;
            System.out.println("===========0=====================" + imageUrl);
            viewHolder.thumbNail.setImageUrl(imageURL, imageLoader);
            // Return the completed view to render on screen
            return convertView;
        }


    }


}
