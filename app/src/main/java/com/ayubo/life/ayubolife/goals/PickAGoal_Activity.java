package com.ayubo.life.ayubolife.goals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickAGoal_Activity extends AppCompatActivity {
    ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> data;
    ListView health_track_list;
    CustomListAdapterLite adapter;
    ImageLoader imageLoader;
    ImageButton btn_back_Button;
    ProgressDialog mProgressDialog;
    String selectedgoalId=null;
    com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum selectedGoal=null;
    ImageButton btn_backImgBtn;
    PrefManager pref;
    String userid_ExistingUser;
    enum cell_type{
        DEFAULT, CUSTOM, SPONSERED;
    }
    FloatingActionButton fab;
    Gson gson =null;
    com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum selectedGoalObject=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_agoal_);
        gson = new Gson();
        pref = new PrefManager(PickAGoal_Activity.this);
        mProgressDialog=new ProgressDialog(PickAGoal_Activity.this);
        mProgressDialog.setMessage("Loading...");

        userid_ExistingUser=pref.getLoginUser().get("uid");
        data=new ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum>();
        health_track_list=(ListView)findViewById(R.id.health_track_list_new);
        View footer = getLayoutInflater().inflate(R.layout.empty_bottom_layout, health_track_list, false);
        health_track_list.addFooterView(footer, null, false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PickAGoal_Activity.this, AddGoals_Activity.class);
                intent.putExtra("FROM_ACTIVITY", "AddNew");
                startActivity(intent);
            }
        });

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        health_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                selectedGoalObject= data.get(position);

                clickGoalPick(selectedGoalObject);


            }

        });

        boolean isNeedCallService=false;
        String products_list=null;
        DBString dataObj=  DBRequest.getCashDataByType(PickAGoal_Activity.this,"goal_data_in_pickagoal");

        if(dataObj==null){
            isNeedCallService=true;
        }else {
            products_list = dataObj.getId();
            if ((products_list != null) && (products_list.length() > 5)) {
                Type type = new TypeToken<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse>() {
                }.getType();
                com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse resp = gson.fromJson(products_list, type);
                data = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum>) resp.getData();
                updateView(data);
            }

//            long minits=  Util.getTimestampAsMinits(dataObj.getName());
//            if(minits>5){
//                isNeedCallService=true;
//            }
        }



            System.out.println("========Calling Service ======================");
            callViewGoals("viewGoals");




    }

    public void pickAGoal(String goalId) {
        mProgressDialog.show();
        userid_ExistingUser=pref.getLoginUser().get("uid");

        TimeZone tz = TimeZone.getDefault();
        String serviceName="pickGoal";
        String timeZone=tz.getDisplayName(false, TimeZone.SHORT);
        timeZone=timeZone.substring(3);



        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"goal_id\": \"" + goalId + "\"," +
                        "\"time_zone\": \"" + timeZone + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiService.pickAGoal(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if(response.isSuccessful()) {


                    JSONObject responseBody = null; int res=5;
                    try {
                        responseBody=  castObjectToJSON(response);
                        res = responseBody.getInt("result");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(res==0){

                        showAlert_OkThenFinish(PickAGoal_Activity.this, "Goal set for the day. Once achieved, select goal again to pick another.");

                    }
                    else if(res==23){
                        JSONObject erroeBody = null;
                        String  erro =null;  String  data =null;
//                        try {
//                           // data = responseBody.getString("data");
//                           // erroeBody = new JSONObject(data);
//                           // erro = erroeBody.getString("error");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        Toast.makeText(PickAGoal_Activity.this, "You can't pick a goal.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(PickAGoal_Activity.this, "Goal picking error", Toast.LENGTH_SHORT).show();
                    }


                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    JSONObject castObjectToJSON(Response<Object> response) {

        Gson gson1 = new Gson();
        String jsonCars = gson1.toJson(response);

        JSONObject jresponse = null;
        JSONObject responseBody = null;
        try {
            jresponse = new JSONObject(jsonCars);
            String body = jresponse.getString("body");
            responseBody = new JSONObject(body);

        }catch (Exception e){
            e.printStackTrace();
        }

        return responseBody;
    }


    public void callViewGoals(String serviceName) {

        userid_ExistingUser=pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> call = apiService.viewGoals(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> response) {

                if(response.isSuccessful()) {
                    if(response.body()!=null){
                        if(response.body().getResult()==401){
                            Intent in = new Intent(PickAGoal_Activity.this, LoginActivity_First.class);
                            startActivity(in);
                        }
                        if(response.body().getResult()==0){
                            // Code here .............
                            Gson gson1 = new Gson();
                            String jsonCars = gson1.toJson(response.body());

                            //  pref.setGoalList("a");

                            DBRequest.updateDoctorData(PickAGoal_Activity.this,jsonCars,"goal_data_in_pickagoal");

                            if((data!=null) && (data.size()>0)){
                                data.clear();
                            }
                            data = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum>) response.body().getData();
                            com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum ddd=new com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum();

                            updateView(data);
                        }
                    }


                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse> call, Throwable t) {

                Toast.makeText(PickAGoal_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }


    void updateView(ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> dat){
        adapter=new CustomListAdapterLite(PickAGoal_Activity.this, dat);

        health_track_list.setAdapter(adapter);
    }

    public void deleteGoalsService(String serviceName) {
        mProgressDialog.show();
        userid_ExistingUser=pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"user_goal_id\": \"" + selectedgoalId + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call = apiService.goalDelete(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if(response.isSuccessful()) {
                    com.ayubo.life.ayubolife.pojo.goals.deleteGoal.Data obj= response.body().getData();
                   if(obj.getStatus()){

                       Toast.makeText(PickAGoal_Activity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(PickAGoal_Activity.this, PickAGoal_Activity.class);
                       startActivity(intent);
                       finish();
                   }else{
                       Toast.makeText(PickAGoal_Activity.this, "Deleting error", Toast.LENGTH_SHORT).show();
                   }
                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call, Throwable t) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                Toast.makeText(PickAGoal_Activity.this, "Deleting error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goalAdd(String serviceName) {
//        {
//            "user_id":"a47eaf31-1a4d-ba9f-6108-5af184058002",
//                "goal_name":"New100",
//                "goal_type":"private",
//                "goal_image_id":1,
//                "goal_category_id":1}

        userid_ExistingUser=pref.getLoginUser().get("uid");

        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"user_goal_id\": \"" + selectedgoalId + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call = apiService.goalDelete(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> response) {

                if(response.isSuccessful()) {

                    com.ayubo.life.ayubolife.pojo.goals.deleteGoal.Data obj= response.body().getData();
                    if(obj.getStatus()){
                        Toast.makeText(PickAGoal_Activity.this, "Sussessfully deleted", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(PickAGoal_Activity.this, "Deleting error", Toast.LENGTH_SHORT).show();

                    }

                    System.out.println("============");
                    // }
                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.deleteGoal.MainResponse> call, Throwable t) {
                Toast.makeText(PickAGoal_Activity.this, "Deleting error", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.pick_goal_list_item, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
          //  TextView txt_sponsered_text = (TextView)vi.findViewById(R.id.txt_sponsered_text); // title


           // LinearLayout top_space_line=(LinearLayout)vi.findViewById(R.id.top_space_line);
          //  LinearLayout bottom_space_line=(LinearLayout)vi.findViewById(R.id.bottom_space_line);

            LinearLayout layout_edit_section=(LinearLayout)vi.findViewById(R.id.layout_edit_section);


            NetworkImageView imgview_sponsered_logo=(NetworkImageView)vi.findViewById(R.id.imgview_sponsered_logo);
            ImageView remote_image_icon=(ImageView)vi.findViewById(R.id.list_image);
            ProgressBar sponserProgress = (ProgressBar) vi.findViewById(R.id.sponserProgress);
            final ImageButton img_btn_edit = (ImageButton)vi.findViewById(R.id.img_btn_edit);
            final ImageButton img_btn_delete = (ImageButton)vi.findViewById(R.id.img_btn_delete);

            HashMap<String, String> song = new HashMap<String, String>();

            com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum obj = data.get(position);

            if(obj.getGoalName()!=null){
                title.setText(obj.getGoalName());

                layout_edit_section.setVisibility(View.GONE);

            if((obj.getType().equals("global")) &&(obj.getSponserName().length()>2) ){

                layout_edit_section.setVisibility(View.GONE);
                imgview_sponsered_logo.setVisibility(View.VISIBLE);
                sponserProgress.setVisibility(View.VISIBLE);




                String sDen= Utility.getDeviceDensityName();
                String sponserimageURL;
                sponserimageURL=obj.getSponserImageUrl();

                if (sponserimageURL.contains("zoom_level")) {
                    sponserimageURL=sponserimageURL.replace("zoom_level",sDen);
                }
                System.out.print("===========sponserimageURL=====ddd========="+sponserimageURL);
                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();
                imgview_sponsered_logo.setImageUrl(sponserimageURL, imageLoader);
                loadImage(sponserimageURL,imgview_sponsered_logo,sponserProgress);

            }
            else if((obj.getType().equals("global")) &&(obj.getSponserName().length()==0) ){
                //Show Default
                layout_edit_section.setVisibility(View.GONE);

                imgview_sponsered_logo.setVisibility(View.GONE);
                sponserProgress.setVisibility(View.INVISIBLE);

            }
            else if(obj.getType().equals("private")){
                //Show Custome
                layout_edit_section.setVisibility(View.VISIBLE);
                img_btn_edit.setVisibility(View.VISIBLE);
                img_btn_delete.setVisibility(View.VISIBLE);
                imgview_sponsered_logo.setVisibility(View.GONE);
                sponserProgress.setVisibility(View.INVISIBLE);

                img_btn_edit.setTag(Integer.toString(position));
                img_btn_delete.setTag(Integer.toString(position));

                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum obj= data.get(position);

                      clickGoalPick(obj);


                    }
                });


                img_btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String pos= img_btn_edit.getTag().toString();
                        selectedGoal = data.get(Integer.parseInt(pos));
                        selectedgoalId=selectedGoal.getId().toString();

                        showAlert_Add(PickAGoal_Activity.this,"","Edit this goal?");
                    }
                });
                img_btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pos= img_btn_delete.getTag().toString();
                        selectedGoal = data.get(Integer.parseInt(pos));
                        selectedgoalId=selectedGoal.getId().toString();

                        showAlert_Deleted(PickAGoal_Activity.this,"","Delete this goal?");

                       // Toast.makeText(PickAGoal_Activity.this, "Delete Clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{}


            String imageURL=obj.getImageId();
         //   String sDen= Utility.getDeviceDensityName();
            imageURL=imageURL.replace("zoom_level","xxxhdpi");

            int imageSize=Utility.getImageSizeFor_DeviceDensitySize(75);

           // System.out.println("=======imageURL========"+imageURL);


                RequestOptions requestOptions = new RequestOptions().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(PickAGoal_Activity.this).load(imageURL)
                        .apply(requestOptions)
                        .into(remote_image_icon);



            }




            return vi;
        }
    }


    private void clickGoalPick(com.ayubo.life.ayubolife.pojo.goals.viewGoals.Datum obj){

        String categoryId=obj.getId();

        //  FirebaseAnalytics Adding
        Bundle bPromocode_used = new Bundle();
        bPromocode_used.putString("name",obj.getGoalName());
        if(SplashScreen.firebaseAnalytics!=null) {
            if(obj.getType().equals("private")){
                SplashScreen.firebaseAnalytics.logEvent("Custom_goal_picked", bPromocode_used);
            }else{
                SplashScreen.firebaseAnalytics.logEvent("System_goal_picked", bPromocode_used);
            }

        }

        showAlert_Add(PickAGoal_Activity.this, "", "Pick this goal?", categoryId);
    }

    public void showAlert_OkThenFinish(Context c, String msg) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_ok_and_share, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);


        TextView btn_share = (TextView) layoutView.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                pref.setIsGoalAdded("yes");

                String sponserURl= selectedGoalObject.getSponserImageUrl();
                String shareImageURl= selectedGoalObject.getShareImageUrl();
                String goalName= selectedGoalObject.getGoalName();

                Intent intent = new Intent(PickAGoal_Activity.this, ShareGoals_Activity.class);
                intent.putExtra("sponserURl", sponserURl);
                intent.putExtra("shareImageURl", shareImageURl);
                intent.putExtra("goalName", goalName);
                intent.putExtra("from", "pick");
                startActivity(intent);
                finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_ok);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                pref.setIsGoalAdded("yes");
                finish();
            }
        });
        dialog.show();
    }


    public void showAlert_Add(Context c, String title, String msg,final  String postId) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (Utility.isInternetAvailable(PickAGoal_Activity.this)) {
                    pickAGoal(postId);
                }else{
                    Toast.makeText(PickAGoal_Activity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            //    deleteAPost(postId);

            }
        });
        dialog.show();
    }

    public void showAlert_Add(Context c,String title,String msg){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

      //  TextView lbl_alert_title = (TextView) layoutView.findViewById(R.id.lbl_alert_title);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);
//        if(title.length()>3){
//            lbl_alert_title.setVisibility(View.VISIBLE);
//            lbl_alert_title.setText(title);
//        }else{
//            lbl_alert_title.setVisibility(View.INVISIBLE);
//        }

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                String goalName=selectedGoal.getGoalName().toString();
                String goalCategoryId=selectedGoal.getGoalCategoryId().toString();

                Intent intent = new Intent(PickAGoal_Activity.this, AddGoals_Activity.class);
                intent.putExtra("FROM_ACTIVITY", "Edit");
                intent.putExtra("goalName", goalName);
                intent.putExtra("goalCategoryId", selectedgoalId);
                startActivity(intent);

            }
        });
        dialog.show();
    }

    public void showAlert_Deleted(Context c,String title,String msg){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        // alert_ask_go_back_to_map
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                //   finish();

            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (Utility.isInternetAvailable(PickAGoal_Activity.this)) {
                    deleteGoalsService("goalDelete");
                }else{
                    Toast.makeText(PickAGoal_Activity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }


    private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        if (imageUrl != null) {
            imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    //	Log.e("onErrorResponse ", error.toString());
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }



}
