package com.ayubo.life.ayubolife.timeline;

import android.app.ProgressDialog;
import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;

import com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikedUsers_Activity extends AppCompatActivity {
    ArrayList<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum> data;
    ListView liked_users_list_new;

    ImageLoader imageLoader = App.getInstance().getImageLoader();
    ImageButton btn_back_Button;
    ProgressDialog mProgressDialog;
    String selectedgoalId=null;
    ImageButton btn_backImgBtn;PrefManager pref;String userid_ExistingUser;
    enum cell_type{
        DEFAULT, CUSTOM, SPONSERED;
    }
    FloatingActionButton fab;
    Gson gson =null;
    String appToken; String postId,likeCount;
    TextView user_reaction_panel_like_clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_users_);

        postId = getIntent().getStringExtra("postId");
        likeCount= getIntent().getStringExtra("likeCount");

        if(likeCount==null){
            likeCount="0";
        }

        pref = new PrefManager(LikedUsers_Activity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");
        appToken= pref.getUserToken();

        gson = new Gson();

        mProgressDialog=new ProgressDialog(LikedUsers_Activity.this);
        mProgressDialog.setMessage("Loading...");


        data=new ArrayList<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum>();
        liked_users_list_new=(ListView)findViewById(R.id.liked_users_list_new);

        user_reaction_panel_like_clicked=(TextView)findViewById(R.id.user_reaction_panel_like_clicked);

        user_reaction_panel_like_clicked.setText(likeCount +" Likes");

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


        getLikedUsers(postId);

      ///  lay_btnBack

    }

    void getLikedUsers(String postid){
        mProgressDialog.show();

        ApiInterface apiService =ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse> call = apiService.getLikedUsers(AppConfig.APP_BRANDING_ID,appToken, postid);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse> response) {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }
                com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse obj=   response.body();
                if(obj!=null){
                    List<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum> userList= obj.getData();

                    int res=obj.getResult();
                    System.out.println("=========res==========");

                    CustomListAdapterLite adapter=new CustomListAdapterLite(LikedUsers_Activity.this, (ArrayList<Datum>) userList);
                    liked_users_list_new.setAdapter(adapter);
                }


            }
            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.LikedUsersMainResponse> call, Throwable t) {
                if(mProgressDialog!=null){
                    mProgressDialog.dismiss();
                }


            }
        });
    }


    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum> d) {
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
                vi = inflater.inflate(R.layout.raw_liked_users_layout, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
            TextView txt_liked_user_name = (TextView)vi.findViewById(R.id.txt_liked_user_name); // title

            com.ayubo.life.ayubolife.pojo.timeline.likedUsers.Datum obj = data.get(position);

            txt_liked_user_name.setText(obj.getName());
            if (imageLoader == null) {
                imageLoader = App.getInstance().getImageLoader();
            }

            ImageView txt_liked_user_image=(ImageView)vi.findViewById(R.id.txt_liked_user_image);

            String imageURL=obj.getProfilePicture();
            String sDen= Utility.getDeviceDensityName();
            imageURL=imageURL.replace("zoom_level","xxxhdpi");

            int imageSize=Utility.getImageSizeFor_DeviceDensitySize(45);
            if(imageURL.length() > 10) {
                RequestOptions requestOptions1 = new RequestOptions()
                        .override(imageSize, imageSize)
                        .transform(new CircleTransform(LikedUsers_Activity.this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(LikedUsers_Activity.this).load(imageURL)
                        .apply(requestOptions1)
                        .into(txt_liked_user_image);
            }

            return vi;
        }
    }


}
