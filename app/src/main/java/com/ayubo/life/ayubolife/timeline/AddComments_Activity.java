package com.ayubo.life.ayubolife.timeline;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.fragments.NewHomeDesign;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.pojo.timeline.CommentDatum;
import com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.pojo.timeline.User;
import com.ayubo.life.ayubolife.pojo.timeline.commentadd_response.MainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AddComments_Activity extends AppCompatActivity {
    // ArrayList<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> timelinePostList=null;
    int intWidth;
    float hi;
    int intHeight;
    String appToken;
    ListView mainListview;

    String cellType, message, postId;
    File imageFile = null;
    long totalSize;
    ProgressDialog prgDialog;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    int SELECT_FILE = 1;
    String image_absolute_path, userid_ExistingUser;
    Uri selectedImageUri;
    //  String commentURL="http://businesscrmdev.cloudapp.net/API/public/api/v1/post/"+postId+"/comment/new";
    Integer commentCountInt = null;
    TextView user_reaction_panel_comment_count, user_reaction_panel_like_count;
    EditText txt_comment_message;

    String URL = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v1/";
    ImageView user_reaction_panel_like_clicked_image;
    TextView user_reaction_panel_like_text;
    TextView user_reaction_panel_liked_user_list;
    ImageButton btn_back_Button;
    PrefManager pref;
    LinearLayout user_reaction_panel_liked_user_list_layout;
    public static Integer parentPosition;
    List<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> globleCommentsList = null;
    String type;

    View header;

    // FOOTER DATA IN  TIMELINE

    ImageButton btn_comment_camera;
    ImageButton btn_comment_send;

    ImageView img_timeline_post_click;

    //  TYPE TIMELINE
    LinearLayout user_panel;
    LinearLayout title_panel;
    LinearLayout text_panel;
    ConstraintLayout media_panel;
    LinearLayout user_link_panel;
    LinearLayout user_reaction_panel;

    // panel G
    Button txt_panel_b_view_button;
    TextView txt_panel_b_text_heading;
    TextView txt_panel_b_text_day;

    // button Play
    ImageView txt_panel_b_play_button;

    ImageView txt_post_main_background_image;

    // user_panel
     ImageView img_user_picture;
     TextView txt_user_name;
     TextView txt_time_ago;
     ImageButton img_btn_close_intop;

     ImageButton link_panel_timeline_readme;

     TextView link_panel_text_url;
     TextView link_panel_text_desc;

    Post obj=null;
    TextView user_reaction_panel_comment_clicked;
    ImageView user_reaction_panel_liked_user_list_icon;
    // title_panel
    TextView title_panel_heading_text;
    // text_panel
    TextView text_panel_text_message;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            pref.setIsNewCommentAdded("yes");
        }
        return super.onKeyDown(keyCode, event);
    }


    void UIConstructor() {

        mainListview = (ListView) findViewById(R.id.newtimeline_listview);
        header = getLayoutInflater().inflate(R.layout.native_timeline_cell, mainListview, false);
        mainListview.addHeaderView(header, null, false);

        // FOOTER DATA IN  TIMELINE
        txt_comment_message = (EditText) findViewById(R.id.txt_comment_message);
         btn_comment_camera = (ImageButton) findViewById(R.id.btn_comment_camera);
       btn_comment_send = (ImageButton) findViewById(R.id.btn_comment_send);

         img_timeline_post_click = (ImageView) header.findViewById(R.id.img_timeline_post_click);

        //  TYPE TIMELINE
         user_panel = (LinearLayout) header.findViewById(R.id.user_panel);
         title_panel = (LinearLayout) header.findViewById(R.id.title_panel);
         text_panel = (LinearLayout) header.findViewById(R.id.content_panel);
         media_panel = (ConstraintLayout) header.findViewById(R.id.media_panel);
         user_link_panel = (LinearLayout) header.findViewById(R.id.user_link_panel);
         user_reaction_panel = (LinearLayout) header.findViewById(R.id.user_reaction_panel);
        user_reaction_panel_liked_user_list_layout = (LinearLayout) header.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

        // panel G
         txt_panel_b_view_button = (Button) header.findViewById(R.id.txt_panel_b_view_button);
         txt_panel_b_text_heading = (TextView) header.findViewById(R.id.program_panel_text_heading);
         txt_panel_b_text_day = (TextView) header.findViewById(R.id.txt_panel_b_text_day);

        // button Play
          txt_panel_b_play_button = (ImageView) header.findViewById(R.id.txt_panel_b_play_button);
        user_reaction_panel_like_clicked_image = (ImageView) header.findViewById(R.id.user_reaction_panel_like_clicked_image);
          txt_post_main_background_image = (ImageView) header.findViewById(R.id.imageView2);

        // user_panel
          img_user_picture = (ImageView) header.findViewById(R.id.img_user_picture);
          txt_user_name = (TextView) header.findViewById(R.id.txt_user_name);
          txt_time_ago = (TextView) header.findViewById(R.id.txt_time_ago);
          img_btn_close_intop = (ImageButton) header.findViewById(R.id.img_btn_close_intop);

          link_panel_timeline_readme = (ImageButton) header.findViewById(R.id.link_panel_timeline_readme);

          link_panel_text_url = (TextView) header.findViewById(R.id.link_panel_text_url);
          link_panel_text_desc = (TextView) header.findViewById(R.id.link_panel_text_desc);

        user_reaction_panel_like_text = (TextView) header.findViewById(R.id.user_reaction_panel_like_text);
        user_reaction_panel_like_count = (TextView) header.findViewById(R.id.user_reaction_panel_like_count);
        user_reaction_panel_comment_count = (TextView) header.findViewById(R.id.user_reaction_panel_comment_count);
          user_reaction_panel_comment_clicked = (TextView) header.findViewById(R.id.user_reaction_panel_comment_clicked);
        user_reaction_panel_liked_user_list = (TextView) header.findViewById(R.id.user_reaction_panel_liked_user_list);
          user_reaction_panel_liked_user_list_icon = (ImageView) header.findViewById(R.id.user_reaction_panel_liked_user_list_icon);
        // title_panel
          title_panel_heading_text = (TextView) header.findViewById(R.id.title_panel_heading_text);
        // text_panel
         text_panel_text_message = (TextView) header.findViewById(R.id.text_panel_text_message);


        prgDialog = new ProgressDialog(AddComments_Activity.this);
        prgDialog.setMessage("Updating comment...");
        prgDialog.setCancelable(false);

        btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.setIsNewCommentAdded("yes");

                finish();
            }
        });

        pref = new PrefManager(AddComments_Activity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        appToken = pref.getUserToken();

    }

    void DataConstructor() {

        URL = MAIN_URL_LIVE_HAPPY + "api.ayubo.life/public/api/v1/";

        // SETUP TIMELINE  e====================
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;


    }

    void loadFirstPage_Posts(Integer postID) {
        appToken = pref.getUserToken();



            MyProgressLoading.showLoading(AddComments_Activity.this, "Please wait...");


        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> call = apiService.getSinglePost(AppConfig.APP_BRANDING_ID,appToken, postID);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> response) {
                MyProgressLoading.dismissDialog();

                if (response.isSuccessful()) {

                    obj= response.body().getData();
                    cellType=obj.getType();
                    postId = Integer.toString(obj.getPostId());
                    processPostObject(obj);
                  System.out.println("sssssssss");
                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.single_post.MainResponse> call, Throwable t) {
                MyProgressLoading.dismissDialog();
               // Toast.makeText(getContext(), R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                //   showErrorView(t);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comments_);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        UIConstructor();
        DataConstructor();

        Post postObj=null;
        parentPosition = getIntent().getIntExtra("position", 0);
        type = getIntent().getStringExtra("type");

        if(type.equals("notifications")){

           String positi = getIntent().getStringExtra("position");
            loadFirstPage_Posts(Integer.parseInt(positi));

        }
         else if(type.equals("timeline")){

            if (NewHomeDesign.timelinePostlist != null) {
                postObj = NewHomeDesign.timelinePostlist.get(parentPosition);

                cellType = postObj.getType();
                postId = Integer.toString(postObj.getPostId());
                processPostObject(postObj);
            }

        }







    }

    void processPostObject(Post postObj){


        obj= postObj;

        if(obj!=null){

            user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));

            btn_comment_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermissionOpenSDCard();
                }
            });

            btn_comment_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message = txt_comment_message.getText().toString();
                    if ((message.length() < 1) && (imageFile == null)) {
                        Toast.makeText(AddComments_Activity.this, "Please type a comment", Toast.LENGTH_LONG).show();
                    } else {
                        new UploadImage().execute();
                    }
                }
            });

            if (obj.getIsLiked() != null) {
                if (obj.getIsLiked()) {
                    user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                    user_reaction_panel_like_text.setText("Like");
                    user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                }
            }
            user_reaction_panel_like_count.setTag(obj);
            img_btn_close_intop.setVisibility(View.GONE);


            user_reaction_panel_like_text.setTag(obj);

            user_reaction_panel_like_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Post pobj = (Post) user_reaction_panel_like_text.getTag();
                    if (pobj.getIsLiked()) {
                        sendALike(pobj.getPostId(), 0);
                    } else {
                        sendALike(pobj.getPostId(), 1);
                    }

                }
            });


            txt_panel_b_play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (obj.getType().equals("IMAGE_POST")) {
                        Intent intent = new Intent(AddComments_Activity.this, TimelineImage_Activity.class);
                        startActivity(intent);
                    }
                    if (obj.getType().equals("VIDEO_POST")) {
                        Intent intent = new Intent(AddComments_Activity.this, TimelineVideo_Activity.class);
                        intent.putExtra("URL", obj.getVideoUrl());
                        startActivity(intent);
                    }
                    if (obj.getType().equals("GIF_POST")) {
                        Intent intent = new Intent(AddComments_Activity.this, TimelineGif_Activity.class);
                        intent.putExtra("URL", obj.getGifUrl());
                        startActivity(intent);
                    }
                }
            });

            user_reaction_panel_liked_user_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Post obj = (Post) user_reaction_panel_like_count.getTag();

                    Integer pID = obj.getPostId();
                    Integer likeCount = obj.getLikeCount();

                    Intent intent = new Intent(AddComments_Activity.this, LikedUsers_Activity.class);
                    intent.putExtra("postId", Integer.toString(pID));
                    intent.putExtra("likeCount", Integer.toString(likeCount));
                    startActivity(intent);
                }
            });
            user_reaction_panel_like_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post obj = (Post) user_reaction_panel_like_count.getTag();

                    Integer pID = obj.getPostId();
                    Integer likeCount = obj.getLikeCount();

                    Intent intent = new Intent(AddComments_Activity.this, LikedUsers_Activity.class);
                    intent.putExtra("postId", Integer.toString(pID));
                    intent.putExtra("likeCount", Integer.toString(likeCount));
                    startActivity(intent);
                }
            });
            //===========================

            try {

                User user = obj.getUser();
                String name;
                String profilePicture;

                name = user.getName();
                profilePicture = user.getProfilePicture();
                RequestOptions requestOptions1 = new RequestOptions()
                        .transform(new CircleTransform(AddComments_Activity.this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(AddComments_Activity.this).load(profilePicture)
                        .transition(withCrossFade())
                        .thumbnail(0.5f)
                        .apply(requestOptions1)
                        .into(img_user_picture);

                txt_user_name.setText(name);

                String timeInMinists = getTimeInMinits(obj.getTimestamp());

                Integer sdfsdf = obj.getTimestamp();
                //  java.util.Date dateeee=new java.util.Date((long)sdfsdf*1000);

                long number = Long.valueOf(sdfsdf);
                number = number * 1000;
                // Date date=new Date(number);
                Calendar cal = Calendar.getInstance();
                Calendar todayCal = Calendar.getInstance();
                Date d = new Date(number);
                cal.setTime(d);

                int date = cal.get(Calendar.DATE);
                Date todayDate = new Date();

                todayCal.setTime(todayDate);
                int cameDate = cal.get(Calendar.DATE);
                int currDate = todayCal.get(Calendar.DATE);
                SimpleDateFormat formatter = null;
                if (cameDate == currDate) {
                    formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
                } else {
                    formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
                }
                String dateFromDB_forADay = formatter.format(cal.getTime());
                String[] parts = dateFromDB_forADay.split("X");
                String part1 = parts[0];
                //  txt_time.setText(part1);
                txt_time_ago.setText(part1);

                if (obj.getLikeCount() != null) {
                    int liked_count = obj.getLikeCount();
                    String likeCounts = null;

                    if (liked_count > 1) {
                        likeCounts = Integer.toString(liked_count) + " Likes";
                    } else if (liked_count == 1) {
                        likeCounts = Integer.toString(liked_count) + " Like";
                    } else if (liked_count == 0) {
                        likeCounts = Integer.toString(liked_count) + " Like";
                    }
                    user_reaction_panel_like_count.setText(likeCounts);
                }
                if (obj.getCommentCount() != null) {
                    String cc = Integer.toString(obj.getCommentCount());
                    user_reaction_panel_comment_count.setText(cc);
                }
                if (obj.getLikedUsersText() != null) {
                    String cc = obj.getLikedUsersText();
                    if (cc.length() > 5) {

                        user_reaction_panel_liked_user_list.setText(cc);
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                    } else {
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                    }

                } else {
                    user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                }
                if (obj.getTitle() != null) {
                    title_panel_heading_text.setText(obj.getTitle());
                }
                if (obj.getBody() != null) {
                    text_panel_text_message.setText(obj.getBody());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            img_btn_close_intop.setVisibility(View.GONE);
            //=================================
            //-=================================
            if (cellType.equals("NORMAL_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A


                media_panel.setVisibility(View.GONE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E

                if (obj.getTitle() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional
                }


                text_panel.setVisibility(View.VISIBLE); //  C  Optional
                user_link_panel.setVisibility(View.GONE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);
                txt_panel_b_text_day.setVisibility(View.GONE);

                txt_panel_b_play_button.setVisibility(View.GONE);

            } else if (cellType.equals("IMAGE_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E

                if (obj.getTitle() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional
                }
                if (obj.getBody().length() > 3) {
                    text_panel.setVisibility(View.VISIBLE); //  C  Optional
                } else {
                    text_panel.setVisibility(View.GONE);
                }

                user_link_panel.setVisibility(View.GONE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);
                txt_panel_b_text_day.setVisibility(View.GONE);

                txt_panel_b_play_button.setVisibility(View.GONE);

                if (obj.getPostImageUrl() != null) {
                    String postThumbnailImageUrl = obj.getPostImageUrl();
                    String postImageUrl = obj.getPostThumbnailUrl();
                    if (postThumbnailImageUrl.length() > 10) {
                        RequestOptions requestOptions1 = new RequestOptions()
                                .fitCenter()
                                .override(intWidth, intHeight)
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                        Glide.with(AddComments_Activity.this).load(postThumbnailImageUrl)
                                .apply(requestOptions1)
                                .into(txt_post_main_background_image);
                    }
                }
            } else if (cellType.equals("VIDEO_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E

                if (obj.getTitle() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional
                }
                if (obj.getBody().length() > 3) {
                    text_panel.setVisibility(View.VISIBLE); //  C  Optional
                } else {
                    text_panel.setVisibility(View.GONE);
                }
                user_link_panel.setVisibility(View.GONE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);
                txt_panel_b_text_day.setVisibility(View.GONE);


                txt_panel_b_play_button.setVisibility(View.VISIBLE);
                txt_panel_b_play_button.setImageResource(R.drawable.timeline_play_ic);

                String postThumbnailImageUrl = obj.getVideoThumbnail();
                String postImageUrl = obj.getPostThumbnailUrl();
                if (postThumbnailImageUrl.length() > 10) {
                    RequestOptions requestOptions1 = new RequestOptions()
                            .fitCenter()
                            .override(intWidth, intHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(AddComments_Activity.this).load(postThumbnailImageUrl)
                            .apply(requestOptions1)
                            .into(txt_post_main_background_image);
                }

            } else if (cellType.equals("GIF_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E

                if (obj.getTitle() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional
                }
                if (obj.getBody().length() > 3) {
                    text_panel.setVisibility(View.VISIBLE); //  C  Optional
                } else {
                    text_panel.setVisibility(View.GONE);
                }
                user_link_panel.setVisibility(View.GONE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);


                txt_panel_b_text_day.setVisibility(View.GONE);

                txt_panel_b_play_button.setVisibility(View.VISIBLE);
                txt_panel_b_play_button.setImageResource(R.drawable.timeline_gif_icon);

                String postThumbnailImageUrl = obj.getGifThumbnail();
                String postImageUrl = obj.getPostThumbnailUrl();
                if (postThumbnailImageUrl.length() > 10) {
                    RequestOptions requestOptions1 = new RequestOptions()
                            .fitCenter()
                            .override(intWidth, intHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(AddComments_Activity.this).load(postThumbnailImageUrl)
                            .apply(requestOptions1)
                            .into(txt_post_main_background_image);
                }

            } else if (cellType.equals("LINK_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E


                if (obj.getLinkInfo() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional

                    link_panel_text_url.setText(obj.getLinkInfo().getLinkUrl());
                    link_panel_text_desc.setText(obj.getLinkInfo().getTitle());
                    link_panel_timeline_readme.setTag(obj.getLinkInfo().getLinkUrl());

                    String title = obj.getLinkInfo().getTitle();
                    String url = obj.getLinkInfo().getLinkUrl();
                    String thumbnailUrl = obj.getLinkInfo().getThumbnailUrl();
                    link_panel_text_url.setText(url);
                    link_panel_text_desc.setText(title);

                    link_panel_timeline_readme.setTag(url);

                    if (thumbnailUrl.length() > 10) {

                        RequestOptions requestOptions1 = new RequestOptions()
                                .fitCenter()
                                .override(intWidth, intHeight)
                                .diskCacheStrategy(DiskCacheStrategy.ALL);

                        Glide.with(AddComments_Activity.this).load(thumbnailUrl)
                                .apply(requestOptions1)
                                .into(txt_post_main_background_image);
                    }

                    link_panel_timeline_readme.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String url = (String) link_panel_timeline_readme.getTag();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        }
                    });
                }

                if (obj.getBody().length() > 3) {
                    text_panel.setVisibility(View.VISIBLE); //  C  Optional
                } else {
                    text_panel.setVisibility(View.GONE);
                }
                user_link_panel.setVisibility(View.VISIBLE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);
                txt_panel_b_text_day.setVisibility(View.GONE);

                txt_panel_b_play_button.setVisibility(View.GONE);
            } else if (cellType.equals("SYSTEM_POST")) {
                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D

                if (obj.isInteractionsEnabled()) {
                    user_reaction_panel.setVisibility(View.VISIBLE);   //E  Optional
                } else {
                    user_reaction_panel.setVisibility(View.GONE);
                }


                if (obj.getTitle() == null) {
                    title_panel.setVisibility(View.GONE);
                } else {
                    title_panel.setVisibility(View.VISIBLE); // B  Optional
                }
                if (obj.getBody().length() > 3) {
                    text_panel.setVisibility(View.VISIBLE); //  C  Optional
                } else {
                    text_panel.setVisibility(View.GONE);
                }
                user_link_panel.setVisibility(View.GONE);   //F
                // G
                txt_panel_b_view_button.setVisibility(View.GONE);
                txt_panel_b_text_heading.setVisibility(View.GONE);
                txt_panel_b_text_day.setVisibility(View.GONE);

                txt_panel_b_play_button.setVisibility(View.GONE);

                if ((obj.getPostThumbnailUrl() == null) || (obj.getPostThumbnailUrl().equals(""))) {

                } else {
                    RequestOptions requestOptions1 = new RequestOptions()
                            .fitCenter()
                            .override(intWidth, intHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(AddComments_Activity.this).load(obj.getPostThumbnailUrl())
                            .apply(requestOptions1)
                            .into(txt_post_main_background_image);

                }

            } else if (cellType.equals("PROGRAM_POST")) {

                user_panel.setVisibility(View.VISIBLE);      // A
                media_panel.setVisibility(View.VISIBLE);        // D
                user_reaction_panel.setVisibility(View.VISIBLE);   //E

                title_panel.setVisibility(View.GONE); // B  Optional
                text_panel.setVisibility(View.GONE); //  C  Optional
                user_link_panel.setVisibility(View.GONE);   //F

                // G
                txt_panel_b_view_button.setVisibility(View.VISIBLE);
                txt_panel_b_text_heading.setVisibility(View.VISIBLE);
                txt_panel_b_text_day.setVisibility(View.VISIBLE);

                txt_panel_b_play_button.setVisibility(View.GONE);


                txt_panel_b_text_heading.setText(obj.getTitle());
                txt_panel_b_text_day.setText(obj.getSubTitle());
                txt_panel_b_view_button.setText(obj.getButtonText());


                String postThumbnailImageUrl = obj.getPostThumbnailUrl();
                String postImageUrl = obj.getPostThumbnailUrl();
                if (postThumbnailImageUrl.length() > 10) {
                    RequestOptions requestOptions1 = new RequestOptions()
                            .fitCenter()
                            .override(intWidth, intHeight)
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(AddComments_Activity.this).load(postThumbnailImageUrl)
                            .apply(requestOptions1)
                            .into(txt_post_main_background_image);
                }

                txt_panel_b_view_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddComments_Activity.this, Timeline_NewPost_Activity.class);
                        startActivity(intent);
                    }
                });


            }


            //timelinePostList= new ArrayList();
            List<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> dataLis = new ArrayList<>();
            CommentDatum dataObj = new CommentDatum();
            dataLis.add(dataObj);

            CustomAdapterFresh customAdapter = new CustomAdapterFresh(AddComments_Activity.this, R.id.text, (ArrayList<CommentDatum>) dataLis);
            mainListview.setAdapter(customAdapter);


            getAllComment(postId);

        }
    }

    void getAllComment(String post_id) {
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<com.ayubo.life.ayubolife.pojo.timeline.Comment> call = apiService.getAllComment(AppConfig.APP_BRANDING_ID,appToken, post_id);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.Comment>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.Comment> call, Response<com.ayubo.life.ayubolife.pojo.timeline.Comment> response) {

                com.ayubo.life.ayubolife.pojo.timeline.Comment obj = response.body();

                if (obj != null) {
                    globleCommentsList = obj.getData();
                    if (globleCommentsList == null) {
                        globleCommentsList = new ArrayList<>();
                        CommentDatum dataObj = new CommentDatum();
                        globleCommentsList.add(dataObj);
                        CustomAdapterFresh customAdapter = new CustomAdapterFresh(AddComments_Activity.this, R.id.text, (ArrayList<CommentDatum>) globleCommentsList);
                        mainListview.setAdapter(customAdapter);
                    } else {

                        CustomAdapterFresh customAdapter = new CustomAdapterFresh(AddComments_Activity.this, R.id.text, (ArrayList<CommentDatum>) globleCommentsList);
                        mainListview.setAdapter(customAdapter);
                    }
                }


            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.Comment> call, Throwable t) {
                System.out.print("");
            }
        });
    }

    void deleteAComment(String posId, String commentID, final String position) {
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        Call<Object> call = apiService.DeleteAComment(AppConfig.APP_BRANDING_ID,appToken, posId, commentID);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                obj.setCommentCount(obj.getCommentCount() - 1);
                user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));

                globleCommentsList.remove(Integer.parseInt(position));

                CustomAdapterFresh customAdapter = new CustomAdapterFresh(AddComments_Activity.this, R.id.text, (ArrayList<CommentDatum>) globleCommentsList);
                mainListview.setAdapter(customAdapter);

                //    getAllComment(postId);


            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                System.out.print("");
            }
        });
    }


    private class UploadImage extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideKeyboard(AddComments_Activity.this);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String commentURL = URL + "post/" + postId + "/comment/new";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(commentURL);
            httppost.addHeader("Authorization", appToken);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                if (imageFile != null) {
                    entity.addPart("image", new FileBody(imageFile));
                } else {
                    entity.addPart("body", new StringBody(message));
                }
                //
                //  entity.addPart("image", new FileBody(new File("")));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (prgDialog != null) {
                prgDialog.cancel();
            }

            imageFile = null;
            txt_comment_message.setText("");
            NewHomeDesign.timelinePostlist.get(parentPosition).setCommentCount(obj.getCommentCount() + 1);
            // obj.setCommentCount(obj.getCommentCount()+1);


            Gson gson = new GsonBuilder().create();
            MainResponse objsss = gson.fromJson(result, MainResponse.class);

            if (objsss != null) {

                com.ayubo.life.ayubolife.pojo.timeline.CommentDatum freshObject = objsss.getData();

                user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));
                pref.setIsNewCommentAdded("yes");

                globleCommentsList.add(freshObject);

                CustomAdapterFresh customAdapter = new CustomAdapterFresh(AddComments_Activity.this, R.id.text, (ArrayList<CommentDatum>) globleCommentsList);
                mainListview.setAdapter(customAdapter);
            }

        }

    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(AddComments_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (AddComments_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(AddComments_Activity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            selectImagePopup();


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

                    selectImagePopup();

                } else {
                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void selectImagePopup() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAlert_PostImage(Context c, String msg) {

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

        TextView btn_cancel = (TextView) layoutView.findViewById(R.id.btn_no);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageFile = null;
                dialog.cancel();

            }
        });

        TextView btn_no = (TextView) layoutView.findViewById(R.id.btn_yes);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                new UploadImage().execute();

            }
        });

        dialog.show();
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            data.getData();
            selectedImageUri = data.getData();
            Cursor cursor;
            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

            cursor = AddComments_Activity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;

                imageFile = new File(image_absolute_path);
            }

            imageFile = resizeAndCompressImageBeforeSend(AddComments_Activity.this, image_absolute_path, "postImage.jpeg");

            showAlert_PostImage(AddComments_Activity.this, "Post image?");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);

        Bitmap bmpPicNew = null;

        int compressQuality = 5; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {

            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sp = context.getCacheDir() + "/" + fileName;
        //return the path of resized and compressed file
        return destination;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }


    public class CustomAdapterFresh extends ArrayAdapter<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> {

        private List<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> objectsList;

        private class ViewHolder {
            ImageButton comment_cell_delete;
            ImageView comment_cell_image, img_user_picture;
            TextView comment_cell_username, comment_cell_text, comment_cell_time;
            LinearLayout comment_cell_main_lay, comment_cell_main_seperator;
        }

        public CustomAdapterFresh(Context context, int resource, ArrayList<com.ayubo.life.ayubolife.pojo.timeline.CommentDatum> objects) {
            super(context, resource, objects);
            this.objectsList = objects;
        }

        @Override
        public View getView(int position, View vconvertView, ViewGroup parent) {

            View convertView = vconvertView;
            final com.ayubo.life.ayubolife.pojo.timeline.CommentDatum obj = objectsList.get(position);
            // String cellType = obj.getType();


            final View result;
            ViewHolder viewHolder = null;
            // View rowView = convertView;
            if (convertView == null) {
                viewHolder = new CustomAdapterFresh.ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.timeline_comment_cell, parent, false);

                // panel G
                viewHolder.comment_cell_main_lay = (LinearLayout) convertView.findViewById(R.id.comment_cell_main_lay);
                viewHolder.comment_cell_main_seperator = (LinearLayout) convertView.findViewById(R.id.comment_cell_main_seperator);

                viewHolder.comment_cell_delete = (ImageButton) convertView.findViewById(R.id.comment_cell_delete);

                viewHolder.img_user_picture = (ImageView) convertView.findViewById(R.id.img_user_picture);
                viewHolder.comment_cell_image = (ImageView) convertView.findViewById(R.id.comment_cell_image);
                viewHolder.comment_cell_username = (TextView) convertView.findViewById(R.id.comment_cell_username);
                viewHolder.comment_cell_text = (TextView) convertView.findViewById(R.id.comment_cell_text);
                viewHolder.comment_cell_time = (TextView) convertView.findViewById(R.id.comment_cell_time);

                convertView.setTag(viewHolder);

            }

            final ViewHolder holder = (ViewHolder) convertView.getTag();


            //Dont cheange this bellow code block ...value 0 is hard coded in the code
            if (obj.getCommentId() == null) {
                holder.comment_cell_main_lay.setVisibility(View.GONE);
                holder.comment_cell_main_seperator.setVisibility(View.GONE);
            } else {

                String profilePicture = obj.getUser().getProfilePicture();
                String postImageUrl = "";

                String comment = null;
                String timesTamp = obj.getTimestamp().toString();
                String name = obj.getUser().getName();

                holder.comment_cell_delete.setTag(new DBString(Integer.toString(position), obj.getCommentId()));


                if (userid_ExistingUser.equals(obj.getUser().getId())) {
                    holder.comment_cell_delete.setVisibility(View.VISIBLE);
                } else {
                    holder.comment_cell_delete.setVisibility(View.GONE);
                }

                if (obj.getPictureUrl() != null) {
                    postImageUrl = obj.getPictureUrl().toString();
                }
                if ((postImageUrl == null) || ((postImageUrl.length() < 10))) {
                    holder.comment_cell_image.setVisibility(View.GONE);
                } else {
                    holder.comment_cell_image.setVisibility(View.VISIBLE);
                    holder.comment_cell_text.setVisibility(View.GONE);
                    RequestOptions requestOptions1 = new RequestOptions()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(getContext()).load(postImageUrl)
                            .apply(requestOptions1)
                            .into(holder.comment_cell_image);

                }
                if (obj.getBody() != null) {
                    comment = obj.getBody();
                    holder.comment_cell_text.setVisibility(View.VISIBLE);
                }

                holder.comment_cell_username.setText(name);
                holder.comment_cell_text.setText(comment);
                holder.comment_cell_time.setText(getTimeAsReadable(timesTamp));

                RequestOptions requestOptions1 = new RequestOptions()
                        .transform(new CircleTransform(getContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(getContext()).load(profilePicture)
                        .transition(withCrossFade())
                        .thumbnail(0.5f)
                        .apply(requestOptions1)
                        .into(holder.img_user_picture);

                holder.comment_cell_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        DBString object = (DBString) holder.comment_cell_delete.getTag();

                        showAlert_Delete_Comment(AddComments_Activity.this, "Delete this comment?", object.getId(), object.getName());


                    }
                });
            }

            return convertView;
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    void sendALike(Integer postid, final int status) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        String post_id = Integer.toString(postid);

        Call<LikeMainResponse> call = apiService.LikeAPost(AppConfig.APP_BRANDING_ID,appToken, post_id, status);
        call.enqueue(new Callback<LikeMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Response<LikeMainResponse> response) {

                obj.setCommentCount(obj.getCommentCount() + 1);
                String likedUsersText = obj.getLikedUsersText();
                String sss = null;
                String likeText = "";
                Integer currentCount = obj.getLikeCount();
                if (status == 1) {

                    if (currentCount == 0) {
                        sss = "You";
                    } else if (currentCount == 1) {
                        sss = "You and " + likedUsersText;
                    } else if (currentCount > 1) {
                        sss = "You, " + likedUsersText;
                    }

                    currentCount = currentCount + 1;
                    obj.setIsLiked(true);
                    obj.setLikeCount(currentCount);


                    if (currentCount == 0) {
                        likeText = "0 Like";
                        user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);
                        user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                    }
                    if (currentCount == 1) {
                        likeText = Integer.toString(obj.getLikeCount()) + " Like";
                        user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                        user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                    } else if (currentCount > 1) {
                        likeText = Integer.toString(obj.getLikeCount()) + " Likes";
                        user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                        user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                    }

                    if (currentCount > 0) {
                        user_reaction_panel_liked_user_list.setText(sss);
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                    } else {
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                    }


                    obj.setLikedUsersText(sss);
                    user_reaction_panel_liked_user_list.setText(sss);
                    user_reaction_panel_like_count.setText(likeText);

                    // UPDATING MAIN POST CELL ....................................
                    pref.setIsNewCommentAdded("yes");
                    NewHomeDesign.timelinePostlist.get(parentPosition).setLikeCount(currentCount);
                    NewHomeDesign.timelinePostlist.get(parentPosition).setLikedUsersText(sss);
                } else if (status == 0) {
                    currentCount = currentCount - 1;
                    obj.setIsLiked(false);
                    obj.setLikeCount(currentCount);

                    if (likedUsersText.contains("You and ")) {
                        sss = likedUsersText.substring(8);
                    } else if (likedUsersText.contains("You, ")) {
                        sss = likedUsersText.substring(5);
                    } else if (likedUsersText.contains("You")) {
                        sss = likedUsersText.substring(3);
                    }
                    user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);
                    user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));

                    if (currentCount == 0) {
                        likeText = "0 Like";
                    }
                    if (currentCount == 1) {
                        likeText = "1 Like";
                    } else if (currentCount > 1) {
                        likeText = Integer.toString(obj.getLikeCount()) + " Likes";
                    }

                    if (currentCount > 0) {
                        user_reaction_panel_liked_user_list.setText(sss);
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                    } else {
                        user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                    }

                    obj.setLikedUsersText(sss);
                    user_reaction_panel_liked_user_list.setText(sss);
                    user_reaction_panel_like_count.setText(likeText);

                    // UPDATING MAIN POST CELL ....................................
                    pref.setIsNewCommentAdded("yes");
                    NewHomeDesign.timelinePostlist.get(parentPosition).setLikeCount(currentCount);
                    NewHomeDesign.timelinePostlist.get(parentPosition).setLikedUsersText(sss);

                }


            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }


    String getTimeInMinits(Integer sMin) {
        String minits = null;
        long now = System.currentTimeMillis(); // See note below obj.getTimestamp()
        // long firstTime =Long.parseLong(sMin);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now - sMin);
        minits = Long.toString(minutes);// return minits+" hours ago";
        return minits;
    }

    String getTimeAsReadable(String timeInLong) {
        String timeText = "";

//        Integer sdfsdf = obj.getTimestamp();
//        //  java.util.Date dateeee=new java.util.Date((long)sdfsdf*1000);

        long number = Long.valueOf(timeInLong);
        number = number * 1000;
        // Date date=new Date(number);
        Calendar cal = Calendar.getInstance();
        Calendar todayCal = Calendar.getInstance();
        Date d = new Date(number);
        cal.setTime(d);

        int date = cal.get(Calendar.DATE);
        Date todayDate = new Date();

        todayCal.setTime(todayDate);
        int cameDate = cal.get(Calendar.DATE);
        int currDate = todayCal.get(Calendar.DATE);
        SimpleDateFormat formatter = null;
        if (cameDate == currDate) {
            formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
        } else {
            formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
        }
        String dateFromDB_forADay = formatter.format(cal.getTime());
        String[] parts = dateFromDB_forADay.split("X");
        timeText = parts[0];

        return timeText;
    }

    public void showAlert_Delete_Comment(Context c, String msg, final String postion, final String comId) {

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
                deleteAComment(postId, comId, postion);
                dialog.cancel();

            }
        });
        dialog.show();
    }


}
