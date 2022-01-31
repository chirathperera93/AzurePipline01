package com.ayubo.life.ayubolife.goals;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.login.LoginActivity_First;
import com.ayubo.life.ayubolife.pojo.goals.addGoal.Data;
import com.ayubo.life.ayubolife.pojo.goals.addNewGoal.AddAGoalResult;
import com.ayubo.life.ayubolife.pojo.goals.addNewGoal.ResultData;
import com.ayubo.life.ayubolife.pojo.goals.viewGoals.MainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGoals_Activity extends AppCompatActivity {
    ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> data;
    GridView gv;
    CustomListAdapterLite adapter;
    LinearLayout layount_select_category;
    Button btn_add_agoal_click;
    ImageButton  btn_back_Button;
    EditText txt_goal_name;
    PrefManager pref;
    String userid_ExistingUser,sDen;
    ImageLoader imageLoader = App.getInstance().getImageLoader();
    TextView select_goal_category_text;
    Gson gson =null;
    ProgressDialog mProgressDialog;
    String comingActivity=null;
    String goalName=null;
    String goalCategoryId=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goals_);

        UIConstructure();

        pref.setSelectedGoal("","");
        pref.setEnteredGoalName("");
        userid_ExistingUser=pref.getLoginUser().get("uid");
        sDen =Utility.getDeviceDensityName();

        comingActivity = getIntent().getStringExtra("FROM_ACTIVITY");

        String products_list= pref.getGoalImagesList().get("goal_images_list");

        if((products_list!=null) && (products_list.length()>5)) {
            Type type = new TypeToken<com.ayubo.life.ayubolife.pojo.goals.MainResponse>() {}.getType();
           com.ayubo.life.ayubolife.pojo.goals.MainResponse mainRes = gson.fromJson(products_list, type);
            ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> dat = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>) mainRes.getData();
            updateView(dat);
        }
        getGoalImages("getGoalImages");

        if(comingActivity.equals("AddNew")){

        }
        else if(comingActivity.equals("Edit")){
            goalName = getIntent().getStringExtra("goalName");
            goalCategoryId = getIntent().getStringExtra("goalCategoryId");
            txt_goal_name.setText(goalName);
            select_goal_category_text.setText("Select your category");
        }



        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.ayubo.life.ayubolife.pojo.goals.Data obj= data.get(position);
                String imageID=obj.getImageId();
                pref.setSelectedGoalImage(imageID);
              //  Toast.makeText(AddGoals_Activity.this, name, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String cateName=pref.getEnteredGoalName();
        if(cateName!=null && (cateName.length()>2)){
            txt_goal_name.setText(cateName);
        }


        String cate=pref.getSelectedGoal().get("selected_goal_catego");

       if(cate!=null && (cate.length()>2)){
           select_goal_category_text.setText(cate);
       }

    }



    void UIConstructure(){

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pref = new PrefManager(AddGoals_Activity.this);
        mProgressDialog=new ProgressDialog(AddGoals_Activity.this);
        mProgressDialog.setMessage("Loading...");
        gson = new Gson();

        txt_goal_name = (EditText) findViewById(R.id.txt_goal_name);
        select_goal_category_text= (TextView) findViewById(R.id.select_goal_category_text);
        gv = (GridView) findViewById(R.id.icon_layout_grid_view);

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back_Button = (ImageButton) findViewById(R.id.btn_back_Button);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_add_agoal_click = (Button) findViewById(R.id.btn_add_agoal_click);
        btn_add_agoal_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utility.isNotNull(txt_goal_name.getText().toString())){
                    if(txt_goal_name.getText().length()>20){
                        Toast.makeText(AddGoals_Activity.this, "Goal name must contain only 20 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String cate=pref.getSelectedGoal().get("selected_goal_catego");
                    if(cate.length()<3){
                        Toast.makeText(AddGoals_Activity.this, "Goal category cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String goalImage= pref.getSelectedGoalImage();
                    if(goalImage.equals("")){
                        Toast.makeText(AddGoals_Activity.this, "Please select goal icon", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        if(comingActivity.equals("AddNew")){
                            showAlert_AddGoal(AddGoals_Activity.this,"","Add this goal?");
                        }
                        else if(comingActivity.equals("Edit")){
                            showAlert_editGoal(AddGoals_Activity.this,"","Edit this goal?");
                        }
                    }

                }else{
                    Toast.makeText(AddGoals_Activity.this, "Goal name cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        layount_select_category = (LinearLayout) findViewById(R.id.layount_select_category);
        layount_select_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utility.isNotNull(txt_goal_name.getText().toString())){
                    String goalName= txt_goal_name.getText().toString();
                    pref.setEnteredGoalName(goalName);
                }

                Intent in=new Intent(AddGoals_Activity.this,SelectGoalCategory_Activity.class);
                startActivity(in);
            }
        });

    }

    void clearMemory(){

        pref.setSelectedGoalImage("");
        pref.setEnteredGoalName("");
        pref.setSelectedGoal("","");
    }

    public void getGoalImages(String serviceNamee) {
        data=new ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>();
        String serviceName="getGoalImages";
        userid_ExistingUser=pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.MainResponse> call = apiService.getGoalImages(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.MainResponse> response) {

                boolean status=response.isSuccessful();
               if(status) {
                   if(response.body()!=null){
                       if(response.body().getResult()==401){
                           Intent in = new Intent(AddGoals_Activity.this, LoginActivity_First.class);
                           startActivity(in);
                       }
                       if(response.body().getResult()==0){
                           // Code here .............
                           ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> da = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>) response.body().getData();
                           Gson gson1 = new Gson();
                           String jsonCars = gson1.toJson(response.body());
                           pref.setGoalImagesList(jsonCars);
                           updateView(da);
                       }
                   }


               }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.MainResponse> call, Throwable t) {

               //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    void updateView(ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> dat){
       adapter=new CustomListAdapterLite(AddGoals_Activity.this, dat);
       gv.setAdapter(adapter);
       gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View v,
                                   int position, long id) {
               adapter.setSelectedPosition(position);
               adapter.notifyDataSetChanged();
           }
       });
   }

    public void addGoals(String serviceNamee) {

        mProgressDialog.show();

        data=new ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>();
        String serviceName="addGoal";
        userid_ExistingUser=pref.getLoginUser().get("uid");
        String goalImage= pref.getSelectedGoalImage();
        String category=pref.getSelectedGoal().get("selected_goal_catego");
        String cateID=pref.getSelectedGoal().get("selected_goal_catego_id");

        String challengeName= txt_goal_name.getText().toString();
        String goal_type="private";
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"goal_name\": \"" + challengeName + "\"," +
                        "\"goal_type\": \"" + goal_type + "\"," +
                        "\"goal_image_id\": \"" + goalImage + "\"," +
                        "\"goal_category_id\": \"" + cateID + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse> call = apiService.goalAdd(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                boolean status=response.isSuccessful();
                if(status) {
                    Data obj=  response.body().getData();
                   if(obj.getStatus()){
                       System.out.println("====Successs====goal id===="+obj.getId());

                       clearMemory();

                       Toast.makeText(AddGoals_Activity.this, "Goad added successfully", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(AddGoals_Activity.this, PickAGoal_Activity.class);
                       startActivity(intent);
                       finish();

                   }else{
                       Toast.makeText(AddGoals_Activity.this, "Goad adding fail", Toast.LENGTH_SHORT).show();
                   }
                   // data = (ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>) response.body().getData();



                    // }
                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.addGoal.MainResponse> call, Throwable t) {
                Toast.makeText(AddGoals_Activity.this, "Goad adding error", Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    public void editGoals(String serviceNamee) {
        mProgressDialog.show();
        data=new ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data>();
        String serviceName="editGoal";
        userid_ExistingUser=pref.getLoginUser().get("uid");
        String goalImage= pref.getSelectedGoalImage();
        String category=pref.getSelectedGoal().get("selected_goal_catego");
        String cateID=pref.getSelectedGoal().get("selected_goal_catego_id");

        String challengeName= txt_goal_name.getText().toString();
        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"id\": \"" + goalCategoryId + "\"," +
                        "\"goal_name\": \"" + challengeName + "\"," +
                        "\"goal_image_id\": \"" + goalImage + "\"," +
                        "\"goal_category_id\": \"" + cateID + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse> call = apiService.editGoal(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse> response) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                boolean status=response.isSuccessful();
                if(status) {
                    clearMemory();
                    com.ayubo.life.ayubolife.pojo.goals.editGoals.Data obj=  response.body().getData();
                    if(obj.getStatus()){

                        Toast.makeText(AddGoals_Activity.this, "Goad edited successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddGoals_Activity.this, PickAGoal_Activity.class);
                        startActivity(intent);
                        finish();
                        System.out.println("====Successs========");
                    }else{
                        Toast.makeText(AddGoals_Activity.this, "Goad editing fail", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goals.editGoals.MainResponse> call, Throwable t) {
                Toast.makeText(AddGoals_Activity.this, "Goad editing error", Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    public void showAlert_AddGoal(Context c,String title,String msg){

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
                if (Utility.isInternetAvailable(AddGoals_Activity.this)) {
                    addGoals("addGoal");
                }
                else{
                    Toast.makeText(AddGoals_Activity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void showAlert_editGoal(Context c,String title,String msg){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutView = inflater.inflate(R.layout.alert_common_with_cancel_only, null, false);

        builder.setView(layoutView);
        final android.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView lbl_alert_message = (TextView) layoutView.findViewById(R.id.lbl_alert_message);
        lbl_alert_message.setText(msg);
        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        TextView btn_yes = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                editGoals("editGoal");
            }
        });
        dialog.show();
    }

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];
        private int selectedPosition = -1;
        private Context activity;
        private ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<com.ayubo.life.ayubolife.pojo.goals.Data> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public void setSelectedPosition(int position) {
            selectedPosition = position;
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

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
            vi = inflater.inflate(R.layout.goal_select_icon_image_view, null);

            RelativeLayout thumbnail=(RelativeLayout)vi.findViewById(R.id.thumbnail); // thumb image
            HashMap<String, String> song = new HashMap<String, String>();
            com.ayubo.life.ayubolife.pojo.goals.Data obj = data.get(position);

            if (imageLoader == null) {
                imageLoader = App.getInstance().getImageLoader();
            }

          //  NetworkImageView remote_image_icon=(NetworkImageView)vi.findViewById(R.id.select_icon_image);
            ImageView remote_image_icon=(ImageView)vi.findViewById(R.id.select_icon_image);
          //  ProgressBar progressNewsList = (ProgressBar) vi.findViewById(R.id.progressNewsList);
            if (imageLoader == null)
                imageLoader= App.getInstance().getImageLoader();

            String imageURL=obj.getImageUrl();
            sDen= Utility.getDeviceDensityName();
            imageURL=imageURL.replace("zoom_level","xxxhdpi");
            System.out.println(imageURL);
          //  remote_image_icon.setImageUrl(imageURL, imageLoader);
          //  loadImage(imageURL,remote_image_icon,progressNewsList);


            int imageSize=Utility.getImageSizeFor_DeviceDensitySize(80);

         //   System.out.println("=========4===imageSize========"+imageSize);
            if(imageURL.length() > 10) {
                RequestOptions requestOptions = new RequestOptions().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(AddGoals_Activity.this).load(imageURL)
                        .apply(requestOptions)
                        .into(remote_image_icon);
            }
//            Glide.with(AddGoals_Activity.this).load(imageURL)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(remote_image_icon);

         //   System.out.println("=======Add goal===imageURL============"+imageURL);

            if (position == selectedPosition) {
                thumbnail.setBackgroundResource(R.drawable.add_goal_bg);
                String imageID=obj.getImageId();
                pref.setSelectedGoalImage(imageID);
            } else {
                thumbnail.setBackgroundResource(R.drawable.add_goal_samecolor);
            }

            return vi;
        }
    }


}
