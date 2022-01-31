package com.ayubo.life.ayubolife.notification.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.timeline.AddComments_Activity;
import com.ayubo.life.ayubolife.timeline.LikedUsers_Activity;
import com.ayubo.life.ayubolife.timeline.TimelineGif_Activity;
import com.ayubo.life.ayubolife.timeline.TimelineImage_Activity;
import com.ayubo.life.ayubolife.timeline.TimelineVideo_Activity;
import com.ayubo.life.ayubolife.timeline.Utility_Timeline;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapterCallback;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationScrollListener;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SingleTimeline_Activity extends AppCompatActivity implements PaginationAdapterCallback{
    LinearLayout errorLayout;
    ProgressDialog proDialog;
    int intWidth,intHeight;
    float hi;
    public PaginationAdapter adapter;
    public ArrayList<Post> timelinePostlist;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    Button btnRetry;
    TextView txtError;
    private final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    Context contextt;
    int offset = 10;
    PrefManager pref;
    String userid_ExistingUser,hasToken,maxPostId,appToken,created_by_id;
     String type;
   // LinearLayout bottom_menu_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_timeline_);

        UIConstructor();
        type = getIntent().getStringExtra("type");
        created_by_id = getIntent().getStringExtra("related_by_id");
       // created_by_id="9f4aa4a4-0c80-8bfd-ba41-5894069edb38";

        DataConstructor();

        loadFirstPage_Posts(appToken);


    }

    void UIConstructor(){

        proDialog=new ProgressDialog(this);
        proDialog.setMessage("Please wait...");
        proDialog.setCancelable(false);
        pref = new PrefManager(this);

        rv = findViewById(R.id.main_recycler);
        rv.setHasFixedSize(false);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        bottom_menu_view = (LinearLayout) findViewById(R.id.bottom_menu_view);


        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                //bottom_menu_view.setVisibility(View.GONE);
                loadNextPage(appToken);

                System.out.println("===========loadNextPage========================");
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage_Posts(appToken);
            }
        });


        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);

        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    void DataConstructor(){

        userid_ExistingUser = pref.getLoginUser().get("uid");
        String name = pref.getLoginUser().get("name");
        hasToken = pref.getLoginUser().get("hashkey");
        contextt=SingleTimeline_Activity.this;
        appToken = pref.getUserToken();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(getBaseContext());
        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;

        System.out.println("===========intWidth==================" + intWidth);
        System.out.println("===========intHeight==================" + intHeight);

    }




    void loadTimeline(ArrayList<Post> timelinePostList) {

        //  hideErrorView();
        MyProgressLoading.dismissDialog();
        adapter.clear();
        adapter.addAll(timelinePostList);
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);


//        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//        else isLastPage = true;

    }

    @Override
    public void retryPageLoad() {
        loadNextPage(appToken);
    }

    void loadFirstPage_Posts(String appToke) {

        proDialog.show();
        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);

        if(type.equals("system")){


            Call<AllPostTimeline> call = apiService.getTimelineSinglePost(AppConfig.APP_BRANDING_ID,appToke,created_by_id, currentPage, offset, maxPostId);

            call.enqueue(new Callback<AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<AllPostTimeline> response) {
                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                    proDialog.cancel();
                    if (response.isSuccessful()) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data timeline_firstpage_data = obj.getData();

                        if (timeline_firstpage_data != null) {

                            ArrayList<Post> timelinePostLi = (ArrayList<Post>) timeline_firstpage_data.getPosts();
                            timeline_firstpage_data.getPagination().getCount();
                            TOTAL_PAGES =1;
                            currentPage = 1;
                            maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                            TOTAL_PAGES = timeline_firstpage_data.getPagination().getPages();
                            currentPage = timeline_firstpage_data.getPagination().getPage();
                            maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                            if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                                timelinePostlist.clear();
                            }
                            timelinePostlist = timelinePostLi;
                            // Online Loading Timeline array ...


                            if(Utility_Timeline.isValidList(timelinePostlist)){
                                adapter = new PaginationAdapter(contextt, userid_ExistingUser);
                                rv.setAdapter(adapter);
                                loadTimeline(timelinePostlist);
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    proDialog.cancel();
                    Toast.makeText(SingleTimeline_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                    //   showErrorView(t);
                }
            });


        }
        if(type.equals("program")){

            Call<AllPostTimeline> call = apiService.getTimelineProgramPost(AppConfig.APP_BRANDING_ID,appToke,created_by_id, currentPage, offset, maxPostId);
            call.enqueue(new Callback<AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<AllPostTimeline> response) {
                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                    proDialog.cancel();
                    if (response.isSuccessful()) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data timeline_firstpage_data = obj.getData();

                        if (timeline_firstpage_data != null) {

                            ArrayList<Post> timelinePostLi = (ArrayList<Post>) timeline_firstpage_data.getPosts();
                            timeline_firstpage_data.getPagination().getCount();
                            TOTAL_PAGES =1;
                            currentPage = 1;
                            maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                            TOTAL_PAGES = timeline_firstpage_data.getPagination().getPages();
                            currentPage = timeline_firstpage_data.getPagination().getPage();
                            maxPostId = timeline_firstpage_data.getPagination().getMaxPostId();

                            if ((timelinePostlist != null) && (timelinePostlist.size() > 0)) {
                                timelinePostlist.clear();
                            }
                            timelinePostlist = timelinePostLi;
                            // Online Loading Timeline array ...


                            if(Utility_Timeline.isValidList(timelinePostlist)){
                                adapter = new PaginationAdapter(contextt, userid_ExistingUser);
                                rv.setAdapter(adapter);
                                loadTimeline(timelinePostlist);
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    proDialog.cancel();
                    Toast.makeText(SingleTimeline_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                    //   showErrorView(t);
                }
            });


        }


    }


    void loadNextPage(String appToke) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);

        if(type.equals("system")) {


            Call<AllPostTimeline> call = apiService.getTimelineSinglePost(AppConfig.APP_BRANDING_ID, appToke, created_by_id, currentPage, offset, maxPostId);

            // Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call = apiService.getTimelineSinglePost(AppConfig.APP_BRANDING_ID,appToke, currentPage, offset, maxPostId);
            call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> response) {

                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                     //  bottom_menu_view.setVisibility(View.VISIBLE);
                    if (response.isSuccessful()) {
                        com.ayubo.life.ayubolife.pojo.timeline.Data data = obj.getData();

                        if (data != null) {
                            ArrayList<Post> timelinePostList = (ArrayList<Post>) data.getPosts();

                            if(timelinePostList.size()>0) {
                                addAllToArray(timelinePostList);


                                TOTAL_PAGES = data.getPagination().getPages();
                                currentPage = data.getPagination().getPage();
                                maxPostId = data.getPagination().getMaxPostId();

                                adapter.removeLoadingFooter();
                                isLoading = false;

                                System.out.println("============currentPage======================" + currentPage);
                                adapter.addAll(timelinePostList);
                                adapter.notifyDataSetChanged();


//                            if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                            else isLastPage = true;
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    System.out.print(t.toString());
                    //   adapter.showRetry(true, fetchErrorMessage(t));

                }
            });

        }

        if(type.equals("program")){

            Call<AllPostTimeline> call = apiService.getTimelineProgramPost(AppConfig.APP_BRANDING_ID,appToke,created_by_id, currentPage, offset, maxPostId);
            call.enqueue(new Callback<AllPostTimeline>() {
                @Override
                public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Response<AllPostTimeline> response) {
                    com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline obj = response.body();
                    proDialog.cancel();
                    if (response.isSuccessful()) {
                        final com.ayubo.life.ayubolife.pojo.timeline.Data data = obj.getData();

                        if (data != null) {

                            if (data != null) {
                                ArrayList<Post> timelinePostList = (ArrayList<Post>) data.getPosts();

                                if(timelinePostList.size()>0){


                                addAllToArray(timelinePostList);

                                TOTAL_PAGES = data.getPagination().getPages();
                                currentPage = data.getPagination().getPage();
                                maxPostId = data.getPagination().getMaxPostId();

                                adapter.removeLoadingFooter();
                                isLoading = false;

                                System.out.println("============currentPage======================" + currentPage);
                                adapter.addAll(timelinePostList);
                                adapter.notifyDataSetChanged();


//                                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                                else isLastPage = true;
                                 }
                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.AllPostTimeline> call, Throwable t) {
                    proDialog.cancel();
                    Toast.makeText(SingleTimeline_Activity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();
                    //   showErrorView(t);
                }
            });


        }
    }



    class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        boolean shouldShow_TitlePanel = true;
        boolean shouldShow_ContentPanel = true;
        boolean shouldShow_UserInteractionPanel = true;

        boolean shouldShow_MediaPanel = false;
        boolean shouldShow_ProgramPanel = false;
        boolean shouldShow_OverlayImage = false;
        boolean shouldShow_LinkPreviewPanel = false;

        // View Types
        private static final int ITEM = 0;
        private static final int LOADING = 1;


        private List<Post> movieResults;
        private Context context;

        private boolean isLoadingAdded = false;
        private boolean retryPageLoad = false;
        String userid_ExistingUser, cellType;
        private PaginationAdapterCallback mCallback;

        private String errorMsg;

        public PaginationAdapter(Context context, String userid_ExistingUser) {
            this.context = context;
            this.mCallback = (PaginationAdapterCallback) context;
            movieResults = new ArrayList<>();
            this.userid_ExistingUser = userid_ExistingUser;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            PostCell viewHolder = null;
            //  LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
//            View viewItem = inflater.inflate(R.layout.native_timeline_cell, parent, false);
//            viewHolder = new PostCell(viewItem);
            // View view;
            // System.out.println("========================onCreateViewHolder");
            // view = inflater.inflate(R.layout.native_timeline_cell, parent, false);
            // return new PostCell(view);

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case ITEM:
                    View viewItem = inflater.inflate(R.layout.native_timeline_cell, parent, false);
                    viewHolder = new PostCell(viewItem);
                    break;
                case LOADING:
                    View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                    break;

            }
            return viewHolder;

        }


//        public void remove(int position) {
//            movieResults.remove(position);
//            notifyItemRemoved(position);
//        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holde, final int position) {

            switch (getItemViewType(position)) {

                case ITEM:

                System.out.println("=========onBindViewHolder======================" + position);

                final Post obj = movieResults.get(position); // PostCell
                final PaginationAdapter.PostCell holder = (PaginationAdapter.PostCell) holde;

                if (obj.getPostId() == null) {
                    //   holder.user_panel.setVisibility(View.GONE);
                    holder.title_panel.setVisibility(View.GONE);
                    holder.content_panel.setVisibility(View.GONE);
                    holder.media_panel.setVisibility(View.GONE);
                    holder.user_link_panel.setVisibility(View.GONE);
                    holder.user_reaction_panel.setVisibility(View.GONE);
                } else {

                    cellType = obj.getType();
                    if (cellType == null) {
                        cellType = "0";
                    }

                    //======   SETUP CELL STATUS ============================================
                    setupCellStatus(obj);
                    //======   SETUP CELL VIEW ============================================
                    setupCellViewForPost(obj, holder);


                    try {

                        String postedUserId = null;

                        holder.img_btn_close_intop.setTag(obj);
                        holder.img_btn_close_bglayout.setTag(obj);
                        holder.user_reaction_panel_liked_user_list.setTag(obj);
                        holder.user_reaction_panel_like_count.setTag(obj);
                        holder.user_reaction_panel_like_text.setTag(obj);
                        holder.user_reaction_panel_comment_clicked.setTag(obj);
                        holder.user_reaction_panel_comment_second_text.setTag(obj);
                        holder.img_user_picture.setTag(obj);
                        holder.main_background_image.setTag(obj);
                        holder.txt_panel_b_view_button.setTag(obj);

                        holder.img_user_picture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Post p = (Post) v.getTag();
                                System.out.println("===============Post ID===================" + p.getPostId());
                                System.out.println("======getType=============" + p.getType());
                                System.out.println("======getBody=============" + p.getBody());
                                System.out.println("======getTitle=============" + p.getTitle());
                                System.out.println("======getVideoThumbnail=============" + p.getVideoThumbnail());
                                System.out.println("======getButtonText=============" + p.getButtonText());
                                System.out.println("======getSubTitle=============" + p.getSubTitle());
                                System.out.println("======getPostThumbnailUrl=============" + p.getPostThumbnailUrl());
                                System.out.println("============================================================");
                            }
                        });


                        //======iS Delete Enable =========================================
                        if (obj.getUser() != null) {
                            postedUserId = obj.getUser().getId().toString();
                            if (userid_ExistingUser.equals(postedUserId)) {
                                holder.img_btn_close_intop.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Post obj = (Post) holder.img_btn_close_intop.getTag();
                                        showAlert_Add(SingleTimeline_Activity.this, appToken, "Are you sure you want to delete post?", obj);
                                    }
                                });
                                holder.img_btn_close_bglayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Post obj = (Post) holder.img_btn_close_intop.getTag();
                                        showAlert_Add(SingleTimeline_Activity.this, appToken, "Are you sure you want to delete post?", obj);
                                    }
                                });
                            }
                        }


                        //======has Likes =========================================
                        if (obj.getIsLiked() != null) {
                            if (obj.getIsLiked()) {
                                holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                                holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                            } else {
                                holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);
                                holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                            }
                        }

                        //======Like Text Clicked =========================================
                        holder.user_reaction_panel_like_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Post pobj = (Post) holder.user_reaction_panel_like_text.getTag();

                                String likedUsersText = obj.getLikedUsersText();
                                Integer likeCount = obj.getLikeCount();

                                if (pobj.getIsLiked()) {
                                    String likeCounts = null;
                                    pobj.setIsLiked(false);

                                    Utility_Timeline.sendALike(pobj.getPostId(), 0, appToken);

                                    Integer liked_count = obj.getLikeCount();

                                    String sss = null;
                                    if (likedUsersText.contains("You and ")) {
                                        sss = likedUsersText.substring(8);
                                    } else if (likedUsersText.contains("You, ")) {
                                        sss = likedUsersText.substring(5);
                                    } else if (likedUsersText.contains("You")) {
                                        sss = likedUsersText.substring(3);
                                    }

                                    if (liked_count > 0) {
                                        liked_count = liked_count - 1;
                                    }

                                    if (liked_count > 1) {
                                        likeCounts = Integer.toString(liked_count) + " Likes";
                                    } else if (liked_count == 1) {
                                        likeCounts = Integer.toString(liked_count) + " Like";
                                    } else if (liked_count == 0) {
                                        likeCounts = Integer.toString(liked_count) + " Like";
                                        holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);

                                    }
                                    pobj.setLikedUsersText(sss);
                                    pobj.setLikeCount(liked_count);

                                    holder.user_reaction_panel_like_count.setText(likeCounts);
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);

                                    holder.user_reaction_panel_liked_user_list.setText(sss);


                                } else {
                                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);

                                    if (likeCount == 0) {
                                        likedUsersText = "You";
                                    } else if (likeCount == 1) {
                                        likedUsersText = "You and " + likedUsersText;
                                    } else {
                                        likedUsersText = "You, " + likedUsersText;
                                    }
                                    pobj.setLikedUsersText(likedUsersText);

                                    String likeCounts = null;
                                    pobj.setIsLiked(true);
                                    Integer liked_count = null;
                                    if (obj.getLikeCount() != null) {
                                        liked_count = obj.getLikeCount();
                                    }

                                    liked_count = liked_count + 1;

                                    if (liked_count > 1) {
                                        likeCounts = Integer.toString(liked_count) + " Likes";
                                    } else if (liked_count == 1) {
                                        likeCounts = Integer.toString(liked_count) + " Like";
                                    } else if (liked_count == 0) {
                                        likeCounts = Integer.toString(liked_count) + " Like";
                                    }

                                    pobj.setLikeCount(liked_count);

                                    holder.user_reaction_panel_like_count.setText(likeCounts);
                                    holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                                    holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);
                                    holder.user_reaction_panel_liked_user_list.setText(likedUsersText);

                                    Utility_Timeline.sendALike(pobj.getPostId(), 1, appToken);
                                }
                            }
                        });

                        //======Comments Clicked =========================================
                        holder.user_reaction_panel_comment_clicked.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SingleTimeline_Activity.this, AddComments_Activity.class);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });


                        //======Comments Clicked =========================================
                        holder.user_reaction_panel_comment_second_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SingleTimeline_Activity.this, AddComments_Activity.class);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });

                        //======Play Button Clicked =========================================
                        holder.txt_panel_b_play_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (obj.getType().equals("VIDEO_POST")) {
                                    Intent intent = new Intent(SingleTimeline_Activity.this, TimelineVideo_Activity.class);
                                    intent.putExtra("IURL", obj.getVideoThumbnail());
                                    intent.putExtra("URL", obj.getVideoUrl());
                                    startActivity(intent);
                                } else if (obj.getType().equals("GIF_POST")) {
                                    Intent intent = new Intent(SingleTimeline_Activity.this, TimelineGif_Activity.class);
                                    intent.putExtra("URL", obj.getGifUrl());
                                    startActivity(intent);
                                }
                            }
                        });

                        //======Liked Users Lists Text Clicked =========================================
                        holder.user_reaction_panel_liked_user_list.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Post obj = (Post) holder.user_reaction_panel_liked_user_list.getTag();

                                Integer pID = obj.getPostId();
                                Integer likeCount = obj.getLikeCount();

                                Intent intent = new Intent(SingleTimeline_Activity.this, LikedUsers_Activity.class);
                                intent.putExtra("postId", Integer.toString(pID));
                                intent.putExtra("likeCount", Integer.toString(likeCount));
                                startActivity(intent);
                            }
                        });

                        //======Liked Users Lists Clciked=========================================
                        holder.user_reaction_panel_like_count.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Post obj = (Post) holder.user_reaction_panel_like_count.getTag();
                                Integer pID = obj.getPostId();
                                Integer likeCount = obj.getLikeCount();

                                Intent intent = new Intent(SingleTimeline_Activity.this, LikedUsers_Activity.class);
                                intent.putExtra("postId", Integer.toString(pID));
                                intent.putExtra("likeCount", Integer.toString(likeCount));
                                startActivity(intent);
                            }
                        });


                        //======Setup Profile Picture=========================================
                        if (obj.getUser() == null) {
                            holder.img_user_picture.setVisibility(View.GONE);
                        } else {
                            holder.img_user_picture.setVisibility(View.VISIBLE);
                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .transform(new CircleTransform(context))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(obj.getUser().getProfilePicture())
                                    .thumbnail(0.5f)
                                    .apply(options)
                                    .transition(withCrossFade())
                                    .into(holder.img_user_picture);
                        }

                        //======Setup User Name =========================================
                        if (obj.getUser() != null) {
                            holder.txt_user_name.setText(obj.getUser().getName());
                        }

                        //======Set Post time =========================================
                        if (obj.getTimestamp() != null) {
                            holder.txt_time_ago.setText(Utility_Timeline.getTimeStringFromInteger(obj.getTimestamp()));
                        }

                        //======Set Like text =========================================
                        if (obj.getLikeCount() != null) {
                            holder.user_reaction_panel_like_count.setText(Utility_Timeline.getLikesStringFromCount(obj.getLikeCount()));
                        }

                        //======Set Comments count text =========================================
                        if (obj.getCommentCount() != null) {
                            holder.user_reaction_panel_comment_count.setText(Integer.toString(obj.getCommentCount()));
                        }

                        //======Set Likes Users List text =========================================
                        if (obj.getLikedUsersText() != null) {
                            holder.user_reaction_panel_liked_user_list.setText(obj.getLikedUsersText());
                        }

                        //======Set Post Title =========================================
                        if (obj.getTitle() != null) {
                            holder.title_panel_heading_text.setText(obj.getTitle());
                        }
                        //======Set Post description =========================================
                        if (obj.getBody() != null) {
                            holder.text_panel_text_message.setText(obj.getBody());
                        }


                        //============LINK VIEW===================================================
                        if (shouldShow_LinkPreviewPanel) {
                            if (obj.getLinkInfo() != null) {
                                holder.link_panel_text_url.setText(obj.getLinkInfo().getLinkUrl());
                                holder.link_panel_text_desc.setText(obj.getLinkInfo().getTitle());
                                holder.link_panel_timeline_readme.setTag(obj.getLinkInfo().getLinkUrl());
                            }
                            holder.link_panel_timeline_readme.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = (String) holder.link_panel_timeline_readme.getTag();
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(browserIntent);
                                }
                            });
                        }


                        //============PROGRAM VIEW===================================================
                        if (shouldShow_ProgramPanel) {
                            if (obj.getSubTitle() != null) {
                                holder.program_panel_text_normal.setText(obj.getSubTitle());
                            }
                            if (obj.getTitle() != null) {
                                holder.program_panel_text_heading.setText(obj.getTitle());
                            }
                            if (obj.getButtonText() != null) {
                                holder.txt_panel_b_view_button.setText(obj.getButtonText());
                            } else {
                                holder.txt_panel_b_view_button.setText("View");
                            }

                            holder.txt_panel_b_view_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {
                                        Post pot = (Post) holder.txt_panel_b_view_button.getTag();

                                        if (obj.isExternalLink()) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                            startActivity(browserIntent);
                                        } else {
                                            Intent intent = new Intent(SingleTimeline_Activity.this, CommonWebViewActivity.class);
                                            intent.putExtra("URL", pot.getRedirectUrl());
                                            startActivity(intent);
                                        }


                                    } else {
                                        Post objjj = (Post) holder.txt_panel_b_view_button.getTag();
                                        openZoomImage(position, objjj);

                                    }

                                }
                            });
                        }


                        //======Main Image click event =========================================
                        holder.main_background_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                    if (obj.isExternalLink()) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                        startActivity(browserIntent);
                                    } else {
                                        Intent intent = new Intent(context, CommonWebViewActivity.class);
                                        intent.putExtra("URL", obj.getRedirectUrl());
                                        startActivity(intent);
                                    }


                                } else {
                                    Post objjj = (Post) holder.main_background_image.getTag();
                                    openZoomImage(position, objjj);
                                }
                            }
                        });


                        //======Main Image Setup =========================================
                        String imageURL = Utility_Timeline.getImageUrlFromType(obj);
                        if ((imageURL == null) || (imageURL.equals(""))) {
                            holder.main_background_image.setVisibility(View.GONE);
                        }


                        RequestOptions options = new RequestOptions()
                                .override(intWidth, intHeight)
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                        Glide.with(context).load(imageURL)
                                .apply(options)
                                .into(holder.main_background_image);

//                            if ((imageURL == null) || (imageURL.equals(""))) {
//                                holder.main_background_image.setVisibility(View.GONE);
//                            } else {
//                                holder.main_background_image.setVisibility(View.VISIBLE);
//                                holder.main_background_image.layout(0, 0, 0, 0);
//
//                                RequestOptions options = new RequestOptions()
//                                        .override(intWidth, intHeight)
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
//                                Glide.with(context).load(imageURL)
//                                        .apply(options)
//                                        .into(holder.main_background_image);
//                                System.out.println("==================imageURL==========================" + imageURL);
//                            }

                        holder.main_background_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {

                                    if (obj.isExternalLink()) {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getRedirectUrl()));
                                        startActivity(browserIntent);
                                    } else {
                                        Intent intent = new Intent(context, CommonWebViewActivity.class);
                                        intent.putExtra("URL", obj.getRedirectUrl());
                                        startActivity(intent);
                                    }


                                } else {

                                    Post objjj = (Post) holder.main_background_image.getTag();
                                    openZoomImage(position, objjj);

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    break;
            case LOADING:
           // LoadingVH loadingVH = (LoadingVH) holde;
//                final ServiceMenuView heroVh = (ServiceMenuView) holde;
//                final Post obj = movieResults.get(position); // PostCell
                final PaginationAdapter.LoadingVH loadingVH = (PaginationAdapter.LoadingVH) holde;
            if (retryPageLoad) {
                loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                loadingVH.loadmore_progress.setVisibility(View.VISIBLE);

                loadingVH.mErrorTxt.setText(
                        errorMsg != null ?
                                errorMsg :
                                context.getString(R.string.error_msg_unknown));

            } else {
                loadingVH.mErrorLayout.setVisibility(View.GONE);
                loadingVH.loadmore_progress.setVisibility(View.VISIBLE);
            }
            break;
        }}

        @Override
        public int getItemCount() {

            int finalCount;

            finalCount = movieResults == null ? 0 : movieResults.size();

            return finalCount;

        }

        void openZoomImage(Integer pos, Post pobj) {

            Intent intent = new Intent(SingleTimeline_Activity.this, TimelineImage_Activity.class);
            if (pobj.getPostThumbnailUrl() != null) {
                intent.putExtra("position", pos);
                intent.putExtra("TURL", pobj.getPostThumbnailUrl());
                intent.putExtra("URL", pobj.getPostImageUrl());
                startActivity(intent);
            }

        }






        private void setupCellStatus(Post postObj) {
            cellType = postObj.getType();

            if (postObj != null) {

                if (cellType.equals("NORMAL_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = false;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("IMAGE_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("VIDEO_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("GIF_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = true;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("LINK_POST")) {
                    shouldShow_ContentPanel = postObj.hasBody();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = true;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;
                    shouldShow_UserInteractionPanel = true;
                } else if (cellType.equals("SYSTEM_POST")) {
                    shouldShow_MediaPanel = postObj.hasImage();
                    shouldShow_TitlePanel = postObj.hasTitle();
                    shouldShow_ContentPanel = postObj.hasBody();

                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = false;

                    if (postObj.isInteractionsEnabled()) {
                        shouldShow_UserInteractionPanel = true;   //E  Optional
                    } else {
                        shouldShow_UserInteractionPanel = false;  //E  Optional
                    }

                } else if (cellType.equals("PROGRAM_POST")) {
                    shouldShow_MediaPanel = true;
                    shouldShow_LinkPreviewPanel = false;
                    shouldShow_OverlayImage = false;
                    shouldShow_ProgramPanel = true;
                    shouldShow_TitlePanel = false;
                    shouldShow_ContentPanel = false;

                    shouldShow_UserInteractionPanel = false;   //E  Optional
                }
            }
        }

        private void setupCellViewForPost(Post postObj, PaginationAdapter.PostCell holder) {

            // Title visibility ..........................
            if (shouldShow_TitlePanel) {
                holder.title_panel_heading_text.setVisibility(View.VISIBLE);
            } else {
                holder.title_panel_heading_text.setVisibility(View.GONE);
            }

            // Content visibility ..........................
            if (shouldShow_ContentPanel) {
                holder.content_panel.setVisibility(View.VISIBLE);
            } else {
                holder.content_panel.setVisibility(View.GONE);
            }

            // //Media panel visibility  ..........................
            if (shouldShow_MediaPanel) {
                holder.media_panel.setVisibility(View.VISIBLE);
            } else {
                holder.media_panel.setVisibility(View.GONE);
            }

            // Program panel visibility  ..........................
            if (shouldShow_ProgramPanel) {
                holder.program_panel_text_heading.setVisibility(View.VISIBLE);
                holder.txt_panel_b_view_button.setVisibility(View.VISIBLE);
                holder.program_panel_text_normal.setVisibility(View.VISIBLE);
            } else {
                holder.program_panel_text_heading.setVisibility(View.GONE);
                holder.txt_panel_b_view_button.setVisibility(View.GONE);
                holder.program_panel_text_normal.setVisibility(View.GONE);

            }

            //Overlay image visibility  ....................
            if (shouldShow_OverlayImage) {
                holder.txt_panel_b_play_button.setVisibility(View.VISIBLE);
            } else {
                holder.txt_panel_b_play_button.setVisibility(View.GONE);

            }

            //User Interaction Panel panel visibiity....................
            if (shouldShow_UserInteractionPanel) {
                holder.user_reaction_panel.setVisibility(View.VISIBLE);
                if ((postObj.getLikedUsersText() == null) || (postObj.getLikedUsersText().equals(""))) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                }


            } else {
                holder.user_reaction_panel.setVisibility(View.GONE);

            }

            //Link preview panel visibiity....................
            if (shouldShow_LinkPreviewPanel) {
                holder.user_link_panel.setVisibility(View.VISIBLE);
            } else {
                holder.user_link_panel.setVisibility(View.GONE);

            }

            // Close button visibility ....................
            if (postObj.getUser() == null) {
            } else {
                if (userid_ExistingUser.equals(postObj.getUser().getId())) {
                    holder.img_btn_close_intop.setVisibility(View.VISIBLE);
                    holder.img_btn_close_bglayout.setVisibility(View.VISIBLE);

                } else {
                    holder.img_btn_close_intop.setVisibility(View.GONE);
                    holder.img_btn_close_bglayout.setVisibility(View.GONE);
                }
            }

            // Liked users text list  visibility ....................
            if (postObj.getLikedUsersText() != null) {
                if (postObj.getLikedUsersText().length() > 1) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                }

            }


        }

        @Override
        public int getItemViewType(int position) {

                return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

        }


    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

        /**
         * @param result
         * @return [releasedate] | [2letterlangcode]
         */
        private String formatYearLabel(Post result) {
//        return result.getReleaseDate().substring(0, 4)  // we want the year only
//                + " | "
//                + result.getOriginalLanguage().toUpperCase();

            return "00000000";
        }


//        private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
//            return Glide
//                    .with(context)
//                    .load(posterPath)
//                    .override(intWidth, intHeight)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
//                    .centerCrop()
//                    .crossFade();
//        }


        /*
            Helpers - Pagination
       _________________________________________________________________________________________________
        */
        public void addToTop(Post r) {
            movieResults.add(1, r);
            //  movieResults.add(r);
            notifyItemInserted(1);
        }

        public void add(Post r) {
            movieResults.add(r);
            int count=(movieResults.size() - 1);
            notifyItemInserted(count);
        }

        public void addAll(List<Post> moveResults) {
            for (Post result : moveResults) {
                add(result);
            }
        }

        public void remove(Post r) {
            int position = movieResults.indexOf(r);
            if (position > -1) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            isLoadingAdded = false;
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new Post());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movieResults.size() - 1;
            Post result = getItem(position);

            if (result != null) {
                movieResults.remove(position);
                notifyItemRemoved(position);
            }
        }

        public Post getItem(int position) {
            return movieResults.get(position);
        }

        /**
         * Displays Pagination retry footer view along with appropriate errorMsg
         *
         * @param show
         * @param errorMsg to display if page load fails
         */
        public void showRetry(boolean show, @Nullable String errorMsg) {
            retryPageLoad = show;
            notifyItemChanged(movieResults.size() - 1);

            if (errorMsg != null) this.errorMsg = errorMsg;
        }


        /**
         * Main list's content ViewHolder
         */
        protected class PostCell extends RecyclerView.ViewHolder {

            LinearLayout img_btn_close_bglayout, user_reaction_panel_liked_user_list_layout, user_panel, title_panel, content_panel, user_link_panel, user_reaction_panel;
            ImageView user_reaction_panel_like_clicked_image, user_reaction_panel_liked_user_list_icon, img_user_picture, main_background_image, txt_panel_b_play_button;
            TextView txt_user_name, txt_time_ago, user_reaction_panel_like_text;
            ImageButton img_btn_close_intop, link_panel_timeline_readme;
            ConstraintLayout media_panel;
            Button txt_panel_b_view_button;
            TextView program_panel_text_heading, link_panel_text_url, link_panel_text_desc;
            TextView program_panel_text_normal, title_panel_heading_text, text_panel_text_message;
            TextView user_reaction_panel_liked_user_list, user_reaction_panel_like_count, user_reaction_panel_comment_count, user_reaction_panel_comment_clicked;
            TextView user_reaction_panel_comment_second_text;
            LinearLayout main_cell_view;

            public PostCell(View convertView) {
                super(convertView);
                main_cell_view = (LinearLayout) convertView.findViewById(R.id.main_cell_view);
                //  TYPE TIMELINE
                user_panel = (LinearLayout) convertView.findViewById(R.id.user_panel);
                title_panel = (LinearLayout) convertView.findViewById(R.id.title_panel);
                content_panel = (LinearLayout) convertView.findViewById(R.id.content_panel);
                media_panel = (ConstraintLayout) convertView.findViewById(R.id.media_panel);
                user_link_panel = (LinearLayout) convertView.findViewById(R.id.user_link_panel);
                user_reaction_panel = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel);
                user_reaction_panel_liked_user_list_layout = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

                // panel G
                txt_panel_b_view_button = (Button) convertView.findViewById(R.id.txt_panel_b_view_button);
                program_panel_text_heading = (TextView) convertView.findViewById(R.id.program_panel_text_heading);
                program_panel_text_normal = (TextView) convertView.findViewById(R.id.txt_panel_b_text_day);

                // button Play
                txt_panel_b_play_button = (ImageView) convertView.findViewById(R.id.txt_panel_b_play_button);

                user_reaction_panel_like_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_text);
                user_reaction_panel_like_clicked_image = (ImageView) convertView.findViewById(R.id.user_reaction_panel_like_clicked_image);
                user_reaction_panel_comment_second_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_second_text);

                main_background_image = (ImageView) convertView.findViewById(R.id.imageView2);

                // user_panel
                img_user_picture = (ImageView) convertView.findViewById(R.id.img_user_picture);
                txt_user_name = (TextView) convertView.findViewById(R.id.txt_user_name);
                txt_time_ago = (TextView) convertView.findViewById(R.id.txt_time_ago);
                img_btn_close_intop = (ImageButton) convertView.findViewById(R.id.img_btn_close_intop);
                img_btn_close_bglayout = (LinearLayout) convertView.findViewById(R.id.img_btn_close_bglayout);

                link_panel_timeline_readme = (ImageButton) convertView.findViewById(R.id.link_panel_timeline_readme);

                link_panel_text_url = (TextView) convertView.findViewById(R.id.link_panel_text_url);
                link_panel_text_desc = (TextView) convertView.findViewById(R.id.link_panel_text_desc);

                // viewHolder.user_reaction_panel_like_button = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_clicked);
                user_reaction_panel_like_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_count);
                user_reaction_panel_comment_count = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_count);
                user_reaction_panel_comment_clicked = (TextView) convertView.findViewById(R.id.user_reaction_panel_comment_clicked);
                user_reaction_panel_liked_user_list = (TextView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list);
                user_reaction_panel_liked_user_list_icon = (ImageView) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_icon);
                // title_panel
                title_panel_heading_text = (TextView) convertView.findViewById(R.id.title_panel_heading_text);

                // text_panel
                text_panel_text_message = (TextView) convertView.findViewById(R.id.text_panel_text_message);

            }
        }


        protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ProgressBar loadmore_progress;
            private ImageButton mRetryBtn;
            private TextView mErrorTxt;
            private LinearLayout mErrorLayout;

            public LoadingVH(View itemView) {
                super(itemView);

                loadmore_progress = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
                mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
                mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
                mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

                mRetryBtn.setOnClickListener(this);
                mErrorLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.loadmore_retry:
                    case R.id.loadmore_errorlayout:

                        showRetry(false, null);
                        mCallback.retryPageLoad();

                        break;
                }
            }
        }

    }


    public void addAllToArray(ArrayList<Post> moveResults) {
        for (Post result : moveResults) {
            addOne(result);
        }
    }

    public void addOne(Post r) {
        timelinePostlist.add(r);
    }

    public void showAlert_Add(Context c, String title, String msg, final Post post) {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
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
                adapter.remove(post);

                Utility_Timeline.deleteAPost(appToken,post);
            }
        });
        dialog.show();
    }


}
