package com.ayubo.life.ayubolife.home_popup_menu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.adapters.CustomAdapter_Sc2;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.model.DB4String;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.DoctorDetailsObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.CircleNetworkImageView;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MyDoctorLocations_Activity extends AppCompatActivity {
    String hasToken;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;
    WebView webView;
    SharedPreferences prefs;
    String userid_ExistingUser,doctor_id;
    String serverWebLink;
    String encodedHashToken,statusFromServiceAPI_db;
    String url=null;
    PrefManager pref;
    ImageButton btn_backImgBtn;
    ProgressDialog prgDialog;
    private ArrayList<DoctorDetailsObj> finalList;

    String speciality,activityName,serviceName;
    String locations;
    String id;
    String name;
    String selectedItemURL;
    String picture;
    CustomAdapter_Sc3 adapter;
    ListView list;
    TextView doc_name,doc_specilty,header_menu_back;
    ImageLoader imageLoader;
    CircleNetworkImageView doctorPic;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor_locations_);

        pref = new PrefManager(MyDoctorLocations_Activity.this);
        prefs =getSharedPreferences("ayubolife", Context.MODE_PRIVATE);

        userid_ExistingUser=pref.getLoginUser().get("uid");
        hasToken=pref.getLoginUser().get("hashkey");

        doctor_id = getIntent().getStringExtra("doctor_id");


        list=(ListView)findViewById(R.id.doc_locationList_lv);
        imageLoader = App.getInstance().getImageLoader();

        prgDialog = new ProgressDialog(MyDoctorLocations_Activity.this);
        finalList = new ArrayList<DoctorDetailsObj>();
        inflatert = (LayoutInflater)MyDoctorLocations_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(MyDoctorLocations_Activity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);
        Ram.setIsOne_Medicine(true);

        doc_name=(TextView)findViewById(R.id.lbl_doctorName) ;
        doc_specilty=(TextView)findViewById(R.id.lbl_specityName) ;
        doctorPic=(CircleNetworkImageView)findViewById(R.id.img_contype) ;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                DoctorDetailsObj dataModel=finalList.get(position);
                String type= dataModel.getName();

                String weblink=dataModel.getLink().toString();
                selectedItemURL = ApiClient.BASE_URL + weblink;

                if(type.equals("Video Chat")){
                    checkPermission(dataModel);
                }else{
                    Intent intent = new Intent(MyDoctorLocations_Activity.this, DoctorWebviewActivity.class);
                    intent.putExtra("URL", selectedItemURL);
                    startActivity(intent);
                }

            }
        });

        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        url =doctor_id;
        if (Utility.isInternetAvailable(this)) {


            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }
  //  Manifest.permission.RECORD_AUDIO,
  //  Manifest.permission.CAMERA},

    private void checkPermission(DoctorDetailsObj dataModel) {
        if (ContextCompat.checkSelfPermission(MyDoctorLocations_Activity.this,
                Manifest.permission.RECORD_AUDIO) + ContextCompat
                .checkSelfPermission(MyDoctorLocations_Activity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (MyDoctorLocations_Activity.this, Manifest.permission.RECORD_AUDIO) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (MyDoctorLocations_Activity.this, Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(MyDoctorLocations_Activity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions to activate video call feature",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .RECORD_AUDIO, Manifest.permission.CAMERA},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
                   }
                   else
                    {
                          // No explanation needed, we can request the permission.
                        if (Build.VERSION.SDK_INT >= 23)
                         requestPermissions(
                        new String[]{Manifest.permission
                                .RECORD_AUDIO, Manifest.permission.CAMERA},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("is_video_call_enable", "true");
            editor.commit();

            Intent intent = new Intent(MyDoctorLocations_Activity.this, DoctorWebviewActivity.class);
            intent.putExtra("URL", selectedItemURL);
            startActivity(intent);

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

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("is_video_call_enable", "true");
                    editor.commit();

                    Intent intent = new Intent(MyDoctorLocations_Activity.this, DoctorWebviewActivity.class);
                    intent.putExtra("URL", selectedItemURL);
                    startActivity(intent);

                } else {
                    // write your logic code if permission already granted
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("is_video_call_enable", "false");
                    editor.commit();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.show();
            prgDialog.setMessage("Loading..");
        }

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();
            if (statusFromServiceAPI_db.equals("0")) {


                doc_name.setText(name);
                doc_specilty.setText(speciality);
                doctorPic.setImageUrl(ApiClient.BASE_URL+picture, imageLoader);

                adapter= new CustomAdapter_Sc3(finalList,MyDoctorLocations_Activity.this);
                list.setAdapter(adapter);



            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(MyDoctorLocations_Activity.this);
            userid_ExistingUser=pref.getLoginUser().get("uid");


            String jsonStr =
                    "{" +
                            "\"userID\": \"" + userid_ExistingUser + "\"," +
                            "\"consultId\": \"" + doctor_id
                            + "\"" +
                            "}";



            nameValuePair.add(new BasicNameValuePair("method", "getConsultant"));
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
            HttpResponse response =null;
            try {
                response = httpClient.execute(httpPost);
                System.out.println(".......getMedicalExperts...response..........." + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r=0;

            String responseString = null;

            try {
                assert response != null;
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {

                r= response.getStatusLine().getStatusCode();
                if(r==200){
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {
                            String dat = jsonObj.optString("data").toString();

                            JSONArray myListsAll = null;
                            try {
                                myListsAll = new JSONArray(dat);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonMainNode3=null;
                            // childDataList = new ArrayList<DBString>();
                            for (int i = 0; i < myListsAll.length(); i++) {
                                try {
                                    jsonMainNode3 = (JSONObject) myListsAll.get(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                 speciality = jsonMainNode3.optString("speciality");
                                 locations = jsonMainNode3.optString("locations");
                                 id = jsonMainNode3.optString("id");
                                 name = jsonMainNode3.optString("name");
                                 picture = jsonMainNode3.optString("picture");

                                System.out.println("==============locations============="+locations);
                                JSONArray myListsAll2 = null;
                                try {
                                    myListsAll2 = new JSONArray(locations);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                JSONObject jsonMainNode6=null;

                                for (int i2 = 0; i2 < myListsAll2.length(); i2++) {
                                    try {
                                        jsonMainNode6 = (JSONObject) myListsAll2.get(i2);
                                        String id = jsonMainNode6.optString("id");
                                        String name = jsonMainNode6.optString("name");
                                        String fee = jsonMainNode6.optString("fee");
                                        String next_avaialble = jsonMainNode6.optString("next_avaialble");
                                        String link = jsonMainNode6.optString("link");
                                        finalList.add(new DoctorDetailsObj(id,name,fee,link,next_avaialble));
                                        } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }


                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    statusFromServiceAPI_db="999";
                }
            }

        }
    }

    public class CustomAdapter_Sc3  extends ArrayAdapter<DoctorDetailsObj> implements View.OnClickListener{

        private ArrayList<DoctorDetailsObj> dataSet;
        private ArrayList<DoctorDetailsObj> datesListArray;
        Context mContext;
        // View lookup cache
        private  class ViewHolder {
            TextView txt_doctor_location;
            TextView txt_timeslot;
            TextView txt_center_name;
            TextView txt_center9;

            ImageView img_contype;
        }
        public CustomAdapter_Sc3(ArrayList<DoctorDetailsObj> data, Context context) {
            super(context, R.layout.raw_search_step2, data);
            this.dataSet = data;
            this.mContext=context;

        }
        @Override
        public void onClick(View v) {
            int position=(Integer) v.getTag();
            Object object= getItem(position);

            DoctorDetailsObj dataModel=(DoctorDetailsObj)object;
            String weblink=dataModel.getLink().toString();
            Intent intent = new Intent(getContext(), DoctorWebviewActivity.class);
            String finalURL = ApiClient.BASE_URL + weblink;
            intent.putExtra("URL", finalURL);
            startActivity(intent);


        }
        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DoctorDetailsObj dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
          ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {


                viewHolder = new CustomAdapter_Sc3.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.raw_search_step2, parent, false);

                viewHolder.txt_doctor_location = (TextView) convertView.findViewById(R.id.txt_doctor_location);
                viewHolder.txt_timeslot = (TextView) convertView.findViewById(R.id.txt_timeslot);
                viewHolder.txt_center_name = (TextView) convertView.findViewById(R.id.txt_center_name);
                viewHolder.txt_center9 = (TextView) convertView.findViewById(R.id.txt_center9);
                viewHolder.img_contype = (ImageView) convertView.findViewById(R.id.img_contype);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter_Sc3.ViewHolder) convertView.getTag();
                result=convertView;
            }


            lastPosition = position;

//            String sname= dataModel.getName();
//            String[] namesList = sname.split(",");
//            System.out.println(sname);
//            String location_id = namesList[0];
//            String location_name = namesList[1];
//            String name3 = namesList[2];
//            String timesstart = namesList[3];
           String sometome = dataModel.getName();
           String doctor_fee =dataModel.getFee();
            String timeslot = dataModel.getNext_avaialble();

            if(timeslot.equals("")){
                timeslot="No sessions available";
            }
            else if(timeslot.equals("0")){
                timeslot="No sessions available";
            }
            viewHolder.txt_doctor_location.setText(sometome);
            viewHolder.txt_timeslot.setText(timeslot);

            if(doctor_fee.equals("LKR 0")){
                doctor_fee="Free";
            }
            viewHolder.txt_center_name.setText(doctor_fee);
            viewHolder.txt_center9.setText("Next available timeslot");

            Drawable myIcon =null;
            //  viewHolder.img_contype.setOnClickListener(this);
//            viewHolder.txt_docfee.setOnClickListener((View.OnClickListener) this);
//            viewHolder.txt_timeslot.setOnClickListener((View.OnClickListener) this);
//            viewHolder.txt_center_name.setOnClickListener((View.OnClickListener) this);

            viewHolder.txt_doctor_location.setTag(sometome);
            viewHolder.txt_timeslot.setTag(position);
            viewHolder.txt_center_name.setTag(position);

            if(sometome.equals("Video Chat")){
                myIcon = convertView.getResources().getDrawable( R.drawable.videochat);
            }else {
                myIcon = convertView.getResources().getDrawable(R.drawable.cal);
            }

            viewHolder.img_contype.setBackground(myIcon);
            // Return the completed view to render on screen
            return convertView;
        }


    }


}
