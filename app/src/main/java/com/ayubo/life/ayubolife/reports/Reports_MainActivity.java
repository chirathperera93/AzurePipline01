package com.ayubo.life.ayubolife.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;

import com.ayubo.life.ayubolife.pojo.reports.Data;
import com.ayubo.life.ayubolife.pojo.reports.MainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;

import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Reports_MainActivity extends AppCompatActivity {
    CustomAdapter adapter;  ListView list;
    ProgressDialog progressDialog;
    private ImageLoader imageLoader;
    String statusFromServiceAPI_db,userid_ExistingUser;
    PrefManager pref;
    List<Data> data;
    String nid,name,relationship,assigned_user_id,user_pic,uhid,type;

    TextView reports_header_name;
    Button btn_reports_join;
    ImageButton btn_go_settings;
    ImageButton btn_backImgBtn;
    de.hdodenhof.circleimageview.CircleImageView user_icon;
    FloatingActionButton fab;
    String burlImg=null;
    String userName=null;
    String jsonStringData=null;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__main);

        data=new ArrayList<Data>();
        list=(ListView)findViewById(R.id.list_reports_view);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Data o = data.get(position);

                if(o!=null){

                    //  FirebaseAnalytics Adding
                    Bundle bPromocode_used = new Bundle();
                    bPromocode_used.putString("type",o.getName());
                    if(SplashScreen.firebaseAnalytics!=null) {
                        SplashScreen.firebaseAnalytics.logEvent("Record_viewed", bPromocode_used);
                    }
                    String c= o.getLink();
                    if((c!=null)&&(c.length()>5)){
                        Intent intent = new Intent(Reports_MainActivity.this, CommonWebViewActivity.class);
                        intent.putExtra("URL",c );
                        startActivity(intent);
                    }
                }


            }
        });


        pref = new PrefManager(this);
        progressDialog=new ProgressDialog(Reports_MainActivity.this);




         reports_header_name=(TextView)findViewById(R.id.reports_header_name);
         btn_reports_join=(Button)findViewById(R.id.btn_reports_join);
         btn_go_settings=(ImageButton)findViewById(R.id.btn_gi_settings);
         btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
         user_icon=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.img_profile);

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(Reports_MainActivity.this, R.drawable.plusimages));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  FirebaseAnalytics Adding
                if(SplashScreen.firebaseAnalytics!=null) {
                    SplashScreen.firebaseAnalytics.logEvent("Add_record_clicked", null);
                }
                String ureel=ApiClient.BASE_URL+"index.php?module=PC_MedicalTestResults&action=manualMedicalResultForm&user="+userid_ExistingUser;
                String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),ureel);
                Intent intent = new Intent(Reports_MainActivity.this, CommonWebViewActivity.class);
                intent.putExtra("URL",newUrl );
                startActivity(intent);
            }
        });



        btn_go_settings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ureel=ApiClient.BASE_URL+"index.php?module=PC_Hospitals&action=connect";
                String newUrl=Utility.appendAyuboLoginInfo(pref.getLoginUser().get("hashkey"),ureel);
                Intent intent = new Intent(Reports_MainActivity.this, CommonWebViewActivity.class);
                intent.putExtra("URL",newUrl );
                startActivity(intent);
            }
        });
        btn_reports_join.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Reports_MainActivity.this, SelectFamilyMember_Activity.class);
                startActivity(intent);
            }
        });


         jsonStringData=  pref.getRepostsData();
        if((jsonStringData!=null) && (jsonStringData.length()>10)){
            displayView(jsonStringData);
        }



       boolean isNeedCallService=false;

       // DBString dataObj=  DBRequest.getCashDataByType(Reports_MainActivity.this,"goal_data_in_pickagoal");
        DBString dataObj=null;

        //callViewGoals("viewGoals");

    }



    @Override
    protected void onResume() {
        super.onResume();



        String imagepath_db;
        Random rand = new Random();
        int  n = rand.nextInt(50) + 1;
        String rannum=Integer.toString(n);
        type= Ram.getReportsType();

        inflatert = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(Reports_MainActivity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        if(type!=null){
            if(type.equals("fromHome")){
                userid_ExistingUser=pref.getLoginUser().get("uid");
                System.out.println("=========fromHome================="+userid_ExistingUser);
                imagepath_db=pref.getLoginUser().get("image_path");
                userName=pref.getLoginUser().get("name");
                burlImg = MAIN_URL_LIVE_HAPPY +imagepath_db+"&cache="+rannum;
            }
            else if(type.equals("fromBack")){
                userid_ExistingUser=  Ram.getReportsId();
                System.out.println("=========fromBack================="+userid_ExistingUser);

                userName= Ram.getReportsName();
                relationship= Ram.getReportsRelationship();
                assigned_user_id=Ram.getReportsAssigned_user_id();
                user_pic=Ram.getReportsUser_pic();
                uhid= Ram.getReportsUhid();

                burlImg = MAIN_URL_LIVE_HAPPY +user_pic+"&cache="+rannum;

            }
        }
       else{
            userid_ExistingUser="";
            userName="";
            burlImg = "";
        }

        reports_header_name.setText(userName);

        RequestOptions requestOptions1 = new RequestOptions()
                .transform(new CircleTransform(Reports_MainActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(Reports_MainActivity.this).load(burlImg)
                .transition(withCrossFade())
                .thumbnail(0.5f)
                .apply(requestOptions1)
                .into(user_icon);

        if (Utility.isInternetAvailable(this)) {
            if((jsonStringData!=null) && (jsonStringData.length()>10)){
            }else{
                MyProgressLoading.showLoading(Reports_MainActivity.this,"Please wait...");
            }
            getDoctors();
        }else{
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }



    }

    public void clickJoinMember(View v){
    }

    void displayView(String jsonStringData){
        if(data!=null){
            data.clear();
        }
        if((jsonStringData!=null) && (jsonStringData.length()>10)){
            ObjectMapper mapper = new ObjectMapper();
            MainResponse mainRes = null;
            try {
                mainRes = mapper.readValue(jsonStringData,  MainResponse.class);
                data=  mainRes.getData();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if((data!=null) &&(data.size()>0)){
                adapter=new CustomAdapter(data,Reports_MainActivity.this);
                list.setAdapter(adapter);

                System.out.println("====UPDATED WITH CASH=======================");
            }


        }
    }

    public void getDoctors() {

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MainResponse> call = apiService.getReports(AppConfig.APP_BRANDING_ID,"getHistoryCategoryList", "JSON","JSON",jsonStr);
        call.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                MyProgressLoading.dismissDialog();
                if(response.isSuccessful()){

                    String json =null;
                    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                    try {
                        json = ow.writeValueAsString(response.body());
                        pref.setRepostsData(json);
                    }
                    catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    if(data!=null){
                        data.clear();
                    }
                    data= response.body().getData();

                    if(data!=null){
                        adapter=new CustomAdapter(data,Reports_MainActivity.this);
                        list.setAdapter(adapter);
                    }else{
                        data=new ArrayList<>();
                        Data d=new Data();
                        data.add(d);

                        adapter=new CustomAdapter(data,Reports_MainActivity.this);
                        list.setAdapter(adapter);
                    }


                }

            }
            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                //  Log.e(TAG, "Unable to submit post to API.");
                MyProgressLoading.dismissDialog();
                Toast.makeText(Reports_MainActivity.this, "Server is temporarily unavailable", Toast.LENGTH_LONG).show();



                System.out.println("========t======"+t);
            }
        });
    }

    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading..");
        }

        @Override
        protected String doInBackground(String... urls) {
            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(progressDialog!=null){
                progressDialog.cancel();
            }
            if (statusFromServiceAPI_db.equals("0")) {

                if(data.size()==0){
                    Toast.makeText(Reports_MainActivity.this, "No records found", Toast.LENGTH_SHORT).show();
                }

                adapter=new CustomAdapter(data,Reports_MainActivity.this);
                list.setAdapter(adapter);
            }
            else if (statusFromServiceAPI_db.equals("55")) {

                Toast.makeText(Reports_MainActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
                adapter=new CustomAdapter(data,Reports_MainActivity.this);
                list.setAdapter(adapter);

            }
            else if (statusFromServiceAPI_db.equals("999")) {
                Toast.makeText(Reports_MainActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Reports_MainActivity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;


            String jsonStr =
                    "{" +
                            "\"userId\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getHistoryCategoryList"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                statusFromServiceAPI_db = "55";
                e.printStackTrace();
            }
            HttpResponse response =null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                statusFromServiceAPI_db = "55";
                e.printStackTrace();
            }
            int r=0;

            String responseString = null;


            if (response != null) {
                r= response.getStatusLine().getStatusCode();
                if(r==200){
                    try {

                        try {
                            responseString = EntityUtils.toString(response.getEntity());
                        } catch (IOException e) {
                            statusFromServiceAPI_db = "55";
                            e.printStackTrace();
                        }

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {

                            String dat = jsonObj.optString("data").toString();
                            JSONArray myListsAll = null;

                            if(dat.length() > 10){
                            try {
                                myListsAll = new JSONArray(dat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < myListsAll.length(); i++) {
                                JSONObject datajsonObj = null;
                                datajsonObj = (JSONObject) myListsAll.get(i);
                                String id = datajsonObj.optString("id").toString();
                                String name = datajsonObj.optString("name").toString();
                                String type = datajsonObj.optString("type").toString();
                                String newda = datajsonObj.optString("new").toString();
                                String link = datajsonObj.optString("link").toString();
                                String icon = datajsonObj.optString("icon").toString();
                                String count = datajsonObj.optString("count").toString();
                                ShareEntity sen = new ShareEntity(id, name, type, icon, newda, link, count);
                               // data.add(sen);
                            }
                            }
//
//                                JSONObject datajsonObj = null;
//                                try {
//                                    datajsonObj = (JSONObject) myListsAll.get(i);
//                                    String id = datajsonObj.optString("id").toString();
//                                    String name = datajsonObj.optString("name").toString();
//                                    String community_type_c = datajsonObj.optString("community_type_c").toString();
//                                    String company_logo_c = datajsonObj.optString("company_logo_c").toString();
//                                    String company_enrolled_c = datajsonObj.optString("company_enrolled_c").toString();
//                                    String no_members = datajsonObj.optString("no_members").toString();
//                                    String logo_image = datajsonObj.optString("logo_image").toString();
//                                    ShareEntity sen = new ShareEntity(id, name, community_type_c, company_logo_c, company_enrolled_c, no_members, logo_image);
//                                    int s = data.size();
//                                    data.add(sen);
//                                    int ss = data.size();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }


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

    public class CustomAdapter extends ArrayAdapter<Data> {

        private List<Data> dataSet;
        Context mContext;

        private  class ViewHolder {
            LinearLayout bottom_line_seperator;
            TextView txtName;
            TextView txt_numberrrr;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView userImage;
        }

        public CustomAdapter( List<Data> moviesList, Context context) {
            super(context, R.layout.reports_listview_raw, moviesList);
            this.dataSet = moviesList;
            this.mContext=context;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            // reuse views
            Data obj = dataSet.get(position);
            if (rowView == null) {
              //  LayoutInflater inflater = context.getLayoutInflater();
              //  rowView = inflater.inflate(R.layout.rowlayout, null);

                LayoutInflater inflater = LayoutInflater.from(getContext());
                rowView = inflater.inflate(R.layout.reports_listview_raw, null);

                // configure view holder
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.bottom_line_seperator = (LinearLayout) rowView.findViewById(R.id.bottom_line_seperator);
                viewHolder.txtName = (TextView) rowView.findViewById(R.id.name);
                viewHolder.txt_numberrrr = (TextView) rowView.findViewById(R.id.txt_numberrrr);
                viewHolder.userImage = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) rowView.findViewById(R.id.share_com_list_image);

                rowView.setTag(viewHolder);
            }
            // fill data
            ViewHolder holder = (ViewHolder) rowView.getTag();


               if(obj!=null){


            holder.txtName.setText(obj.getName());

           if(obj.getCount()!=null){
               int count=obj.getCount();
               holder.txt_numberrrr.setText(Integer.toString(count));
           }else{
               holder.txt_numberrrr.setVisibility(View.GONE);
               holder.bottom_line_seperator.setVisibility(View.GONE);
           }

            String image= obj.getIcon();
                   RequestOptions requestOptions1 = new RequestOptions()
                           .transform(new CircleTransform(Reports_MainActivity.this))
                           .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(Reports_MainActivity.this).load(image)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions1)
                    .into(holder.userImage);
               }
            return rowView;
        }

    }

}
