package com.ayubo.life.ayubolife.goals;

import android.content.Context;
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
import com.ayubo.life.ayubolife.body.DisplayWorkoutList;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.pojo.goals.MainResponse;
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

public class SelectGoalCategory_Activity extends AppCompatActivity {
    ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum> categoryListdata;
    ListView health_track_list;
    CustomListAdapterLite adapter;
    ImageLoader imageLoader = App.getInstance().getImageLoader();
    ImageButton btn_back_Button;Gson gson =null;
    ImageButton btn_backImgBtn;PrefManager pref;String userid_ExistingUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_goal_category_);
        pref = new PrefManager(SelectGoalCategory_Activity.this);
        gson = new Gson();
        userid_ExistingUser=pref.getLoginUser().get("uid");
        categoryListdata=new ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum>();
        health_track_list=(ListView)findViewById(R.id.health_track_list_new);

        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String products_list= pref.getGoalCategoryList().get("goal_category_list");
        if((products_list!=null) && (products_list.length()>5)) {
            Type type = new TypeToken<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse>() {}.getType();
            com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse mainRes = gson.fromJson(products_list, type);
            if((categoryListdata!=null)&&(categoryListdata.size()>0)){
                categoryListdata.clear();
            }
            categoryListdata = (ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum>) mainRes.getData();
            updateView(categoryListdata);
        }

        if (Utility.isInternetAvailable(SelectGoalCategory_Activity.this)) {
            getGoalImages("goalCategories");
        }else{
            Toast.makeText(SelectGoalCategory_Activity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }


        health_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.ayubo.life.ayubolife.pojo.goalCategory.Datum obj= categoryListdata.get(position);
                String name=obj.getCategoryName();
                int catId=obj.getCategoryId();
                pref.setSelectedGoal(name,Integer.toString(catId));
                finish();
                //  Toast.makeText(AddGoals_Activity.this, name, Toast.LENGTH_SHORT).show();
            }

        });

    }
    void updateView(ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum> dat){
        adapter=new CustomListAdapterLite(SelectGoalCategory_Activity.this, dat);
        health_track_list.setAdapter(adapter);
    }
    public void getGoalImages(String serviceNamee) {
        categoryListdata=new ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum>();
        String serviceName="goalCategories";
        userid_ExistingUser=pref.getLoginUser().get("uid");
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse> call = apiService.getGoalCategories(AppConfig.APP_BRANDING_ID,serviceName, "JSON","JSON",jsonStr);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse> response) {

                boolean status=response.isSuccessful();
                if(status) {



                    Gson gson1 = new Gson();
                    String jsonCars = gson1.toJson(response.body());
                    pref.setGoalCategoryList(jsonCars);
                    if((categoryListdata!=null)&&(categoryListdata.size()>0)){
                        categoryListdata.clear();
                    }
                    categoryListdata = (ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum>) response.body().getData();
                    updateView(categoryListdata);


                    System.out.println("============");
                    // }
                }
            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.goalCategory.MainResponse> call, Throwable t) {

                //   Toast.makeText(this, t, Toast.LENGTH_LONG).show();
                System.out.println("========t======"+t);
            }
        });
    }

    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<com.ayubo.life.ayubolife.pojo.goalCategory.Datum> d) {
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

        public View getView(int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.select_goal_category_list_item, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> song = new HashMap<String, String>();

            com.ayubo.life.ayubolife.pojo.goalCategory.Datum obj = data.get(position);

            title.setText(obj.getCategoryName());
            if (imageLoader == null) {
                imageLoader = App.getInstance().getImageLoader();
            }

          //  NetworkImageView remote_image_icon=(NetworkImageView)vi.findViewById(R.id.list_image);
          //  ProgressBar progressNewsList = (ProgressBar) vi.findViewById(R.id.progressNewsList);
            if (imageLoader == null)
                imageLoader= App.getInstance().getImageLoader();
          //  String imageURL="https://livehappy.ayubo.life/custom/include/images/goal_images/xxhdpi/icon1.png";
          String imageURL=obj.getCategoryImageId().toString();
          //  sDen="xxhdpi";
           // imageURL=imageURL.replace("zoom_level",sDen);
            String sDen= Utility.getDeviceDensityName();
            imageURL=imageURL.replace("zoom_level","xxxhdpi");
          //  remote_image_icon.setImageUrl(imageURL, imageLoader);
          //  loadImage(imageURL,remote_image_icon,progressNewsList);
            int imageSize=Utility.getImageSizeFor_DeviceDensitySize(75);
            if(imageURL.length() > 10) {
//                Glide.with(PickAGoal_Activity.this).load(remote_image_icon)
//                        .centerCrop()
//                        .override(imageSize, imageSize)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                RequestOptions requestOptions = new RequestOptions().override(imageSize, imageSize).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(SelectGoalCategory_Activity.this).load(imageURL)
                        .apply(requestOptions)
                        .into(thumb_image);
            }
            return vi;
        }
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
