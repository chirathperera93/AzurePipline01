package com.ayubo.life.ayubolife.timeline.pagination;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.pojo.timeline.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;
    LinearLayout layout_main_menu_horizontal, layout_services_menu_horizontal, bottom_menu_view;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<Post> movieResults;
    private Context context;
    int intWidth;
    float hi;
    int intHeight;
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

        // SETUP TIMELINE  e====================
        DisplayMetrics displayMetrics = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        intWidth = 720;
        DeviceScreenDimension deviceScreenDimension = new DeviceScreenDimension(context);

        intWidth = deviceScreenDimension.getDisplayWidth();
        hi = (intWidth / 4) * 3;
        intHeight = (int) hi;

    }

    public List<Post> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Post> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        intWidth = 720;
        intHeight = 540;
        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.native_timeline_cell, parent, false);
                viewHolder = new PostCell(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.new_home_widgets_layout, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holde, int position) {
        final Post obj = movieResults.get(position); // PostCell

        switch (getItemViewType(position)) {

            case HERO:
//                final HeroVH heroVh = (HeroVH) holder;
//
//                heroVh.mMovieTitle.setText(result.getTitle());
//                heroVh.mYear.setText(formatYearLabel(result));
//                heroVh.mMovieDesc.setText(result.getOverview());
//
//                loadImage(result.getPostThumbnailUrl())
//                        .into(heroVh.mPosterImg);
                break;

            case ITEM:
                final PostCell holder = (PostCell) holde;

                holder.title_panel_heading_text.setText(obj.getBody());
                System.out.println("===============Body==========" + obj.getBody());
                Integer postId = obj.getPostId();
                cellType = obj.getType();
                holder.title_panel.setVisibility(View.GONE);

                String postedUserId = obj.getUser().getId().toString();
                if (userid_ExistingUser.equals(postedUserId)) {
                    holder.img_btn_close_intop.setVisibility(View.VISIBLE);
                    holder.img_btn_close_intop.setTag(obj);
                    holder.img_btn_close_bglayout.setTag(obj);
                } else {
                    holder.img_btn_close_intop.setVisibility(View.GONE);
                }


                holder.user_reaction_panel_liked_user_list.setTag(obj);
                holder.user_reaction_panel_like_count.setTag(postId);
                holder.user_reaction_panel_like_text.setTag(obj);
                holder.user_reaction_panel_comment_clicked.setTag(obj);

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
                if (obj.getLikeCount() > 0) {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                }


                holder.img_btn_close_intop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post obj = (Post) holder.img_btn_close_intop.getTag();

                        //   showAlert_Add(getContext(), "", "Are you sure you want to delete post?", obj);

                    }
                });

                holder.img_btn_close_bglayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post obj = (Post) holder.img_btn_close_intop.getTag();

                        // showAlert_Add(getContext(), "", "Are you sure you want to delete post?", obj);

                    }
                });


                holder.user_reaction_panel_like_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Post pobj = (Post) holder.user_reaction_panel_like_text.getTag();

                        String likedUsersText = obj.getLikedUsersText();
                        Integer likeCount = obj.getLikeCount();

//                        int start = mainListview.getFirstVisiblePosition();
//                        int last = mainListview.getLastVisiblePosition();
//                        int currentPosition =position;

                        if (pobj.getIsLiked()) {
                            String likeCounts = null;
                            pobj.setIsLiked(false);
                            //  sendALike(pobj.getPostId(),0);

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
                            }
                            pobj.setLikedUsersText(sss);
                            pobj.setLikeCount(liked_count);

                            holder.user_reaction_panel_like_count.setText(likeCounts);
                            holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#000000"));
                            holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_ic);

                            //  String likedUsers=obj.getLikedUsersText();

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
//                        if(likedUsersText.contains("You and ")){
//                            likedUsersText.substring(8);
//                        }
//                        if(likedUsersText.contains("You, ")){
//                            likedUsersText.substring(5);
//                        }
//                        if(likedUsersText.contains("You")){
//                            likedUsersText.substring(3);
//                        }


                            String likeCounts = null;
                            pobj.setIsLiked(true);
                            Integer liked_count = obj.getLikeCount();
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
                            //    sendALike(pobj.getPostId(),1);
                            holder.user_reaction_panel_like_text.setTextColor(Color.parseColor("#0662b8"));
                            holder.user_reaction_panel_like_clicked_image.setBackgroundResource(R.drawable.timeline_like_blue_ic);

                            // String likedUsers="You and "+obj.getLikedUsersText();
                            holder.user_reaction_panel_liked_user_list.setText(likedUsersText);
                        }
                    }
                });

                holder.user_reaction_panel_comment_clicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Post objjj = (Post) holder.user_reaction_panel_comment_clicked.getTag();
                        Gson gson = new Gson();
                        String json = gson.toJson(objjj);

                        //  pref.setSelectedPostDataString(json);

                        // Intent intent = new Intent(getContext(), AddComments_Activity.class);
                        // startActivity(intent);
                    }
                });
                holder.txt_panel_b_play_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (obj.getType().equals("IMAGE_POST")) {
//                            Intent intent = new Intent(getContext(), TimelineImage_Activity.class);
//                            startActivity(intent);
                        }
                        if (obj.getType().equals("VIDEO_POST")) {
                            //    Intent intent = new Intent(getContext(), TimelineVideo_Activity.class);
//                            intent.putExtra("URL", obj.getVideoUrl());
//                            startActivity(intent);
                        }
                        if (obj.getType().equals("GIF_POST")) {
//                            Intent intent = new Intent(getContext(), TimelineGif_Activity.class);
//                            intent.putExtra("URL", obj.getGifUrl());
//                            startActivity(intent);
                        }
                    }
                });

                holder.user_reaction_panel_liked_user_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Post obj = (Post) holder.user_reaction_panel_liked_user_list.getTag();

                        Integer pID = obj.getPostId();
                        Integer likeCount = obj.getLikeCount();

//                        Intent intent = new Intent(getContext(), LikedUsers_Activity.class);
//                        intent.putExtra("postId", Integer.toString(pID));
//                        intent.putExtra("likeCount", Integer.toString(likeCount));
//                        startActivity(intent);
                    }
                });
                holder.user_reaction_panel_like_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Integer postId = (Integer) holder.user_reaction_panel_like_count.getTag();
                        Integer pID = (Integer) holder.user_reaction_panel_like_count.getTag();

//                        Intent intent = new Intent(getContext(), LikedUsers_Activity.class);
//                        intent.putExtra("postId", Integer.toString(pID));
//                        startActivity(intent);
                    }
                });
                //===========================

                try {

                    User user = obj.getUser();
                    String name;
                    String profilePicture;

                    name = user.getName();
                    profilePicture = user.getProfilePicture();
                    RequestOptions options = RequestOptions
                            .bitmapTransform(new CircleTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    Glide.with(context).load(profilePicture)
                            .thumbnail(0.5f)
                            .apply(options)
                            .transition(withCrossFade())
                            .into(holder.img_user_picture);

                    holder.txt_user_name.setText(name);

                    //   String timeInMinists = getTimeInMinits(obj.getTimestamp());

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
                    holder.txt_time_ago.setText(part1);

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
                        holder.user_reaction_panel_like_count.setText(likeCounts);
                    }
                    if (obj.getCommentCount() != null) {
                        String cc = Integer.toString(obj.getCommentCount());
                        holder.user_reaction_panel_comment_count.setText(cc);
                    }
                    if (obj.getLikedUsersText() != null) {
                        String cc = obj.getLikedUsersText();
                        if (cc.length() > 5) {

                            holder.user_reaction_panel_liked_user_list.setText(cc);
                            holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                        }

                    } else {
                        // holder.user_reaction_panel_liked_user_list_layout.setVisibility(View.GONE);
                    }
                    if (obj.getTitle() != null) {
                        holder.title_panel_heading_text.setText(obj.getTitle());
                        holder.title_panel.setVisibility(View.VISIBLE);
                    } else {
                        holder.title_panel.setVisibility(View.GONE);
                    }
                    if (obj.getBody() != null) {
                        holder.text_panel_text_message.setText(obj.getBody());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                //=================================
                //-=================================
                if (cellType.equals("NORMAL_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A

                    holder.media_panel.setVisibility(View.GONE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E

                    if (obj.getTitle() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }


                    holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    holder.user_link_panel.setVisibility(View.GONE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);
                    holder.txt_panel_b_text_day.setVisibility(View.GONE);

                    holder.txt_panel_b_play_button.setVisibility(View.GONE);

                } else if (cellType.equals("IMAGE_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E

                    if (obj.getTitle() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }
                    if (obj.getBody().length() > 3) {
                        holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    } else {
                        holder.text_panel.setVisibility(View.GONE);
                    }

                    holder.user_link_panel.setVisibility(View.GONE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);
                    holder.txt_panel_b_text_day.setVisibility(View.GONE);

                    holder.txt_panel_b_play_button.setVisibility(View.GONE);

                    String postThumbnailImageUrl = obj.getPostImageUrl();
                    if (postThumbnailImageUrl != null) {
                        if (postThumbnailImageUrl.length() > 10) {
                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(postThumbnailImageUrl)
                                    .apply(options)
                                    .into(holder.txt_panel_b_background_image);
                        } else {
                            holder.txt_panel_b_background_image.setVisibility(View.GONE);
                        }
                    }

                } else if (cellType.equals("VIDEO_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E

                    if (obj.getTitle() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }
                    if (obj.getBody().length() > 3) {
                        holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    } else {
                        holder.text_panel.setVisibility(View.GONE);
                    }
                    holder.user_link_panel.setVisibility(View.GONE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);
                    holder.txt_panel_b_text_day.setVisibility(View.GONE);


                    holder.txt_panel_b_play_button.setVisibility(View.VISIBLE);
                    holder.txt_panel_b_play_button.setImageResource(R.drawable.timeline_play_ic);

                    String postThumbnailImageUrl = obj.getVideoThumbnail();
                    String postImageUrl = obj.getPostThumbnailUrl();
                    if (postThumbnailImageUrl != null) {
                        if (postThumbnailImageUrl.length() > 10) {
                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(postThumbnailImageUrl)
                                    .apply(options)
                                    .into(holder.txt_panel_b_background_image);
                        } else {
                            holder.txt_panel_b_background_image.setVisibility(View.GONE);
                        }
                    }


                } else if (cellType.equals("GIF_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E

                    if (obj.getTitle() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }
                    if (obj.getBody().length() > 3) {
                        holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    } else {
                        holder.text_panel.setVisibility(View.GONE);
                    }
                    holder.user_link_panel.setVisibility(View.GONE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);


                    holder.txt_panel_b_text_day.setVisibility(View.GONE);

                    holder.txt_panel_b_play_button.setVisibility(View.VISIBLE);
                    holder.txt_panel_b_play_button.setImageResource(R.drawable.timeline_gif_icon);

                    String postThumbnailImageUrl = obj.getGifThumbnail();
                    String postImageUrl = obj.getPostThumbnailUrl();
                    if (postThumbnailImageUrl != null) {
                        if (postThumbnailImageUrl.length() > 10) {
                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(postThumbnailImageUrl)
                                    .apply(options)
                                    .into(holder.txt_panel_b_background_image);
                        } else {
                            holder.txt_panel_b_background_image.setVisibility(View.GONE);
                        }
                    }


                } else if (cellType.equals("LINK_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E


                    if (obj.getLinkInfo() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.GONE); // B  Optional

                        String title = obj.getLinkInfo().getTitle();
                        String url = obj.getLinkInfo().getLinkUrl();
                        String thumbnailUrl = obj.getLinkInfo().getThumbnailUrl();
                        holder.link_panel_text_url.setText(url);
                        holder.link_panel_text_desc.setText(title);

                        holder.link_panel_timeline_readme.setTag(url);

                        if (thumbnailUrl != null) {
                            if (thumbnailUrl.length() > 10) {
                                RequestOptions options = new RequestOptions()
                                        .override(intWidth, intHeight)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(context).load(thumbnailUrl)
                                        .apply(options)
                                        .into(holder.txt_panel_b_background_image);
                            } else {
                                holder.txt_panel_b_background_image.setVisibility(View.GONE);
                            }
                        }


                        holder.link_panel_timeline_readme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String url = (String) holder.link_panel_timeline_readme.getTag();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                //  startActivity(browserIntent);
                            }
                        });
                    }


//                        if(obj.getTitle().length()>3){
//                            viewHolder.title_panel.setVisibility(View.VISIBLE); // B  Optional
//                        }else{
//                            viewHolder.title_panel.setVisibility(View.GONE);
//                        }
                    if (obj.getBody().length() > 3) {
                        holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    } else {
                        holder.text_panel.setVisibility(View.GONE);
                    }
                    holder.user_link_panel.setVisibility(View.VISIBLE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);
                    holder.txt_panel_b_text_day.setVisibility(View.GONE);

                    holder.txt_panel_b_play_button.setVisibility(View.GONE);
                } else if (cellType.equals("SYSTEM_POST")) {
                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D

                    if (obj.getPostImageUrl() != null) {
                        String postThumbnailImageUrl = null;
                        postThumbnailImageUrl = obj.getPostImageUrl();
                        if (postThumbnailImageUrl != null) {
                            if (postThumbnailImageUrl.length() > 10) {
                                RequestOptions options = new RequestOptions()
                                        .override(intWidth, intHeight)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                                Glide.with(context).load(postThumbnailImageUrl)
                                        .apply(options)
                                        .into(holder.txt_panel_b_background_image);
                            } else {
                                holder.txt_panel_b_background_image.setVisibility(View.GONE);
                            }
                        }
                    }
                    holder.txt_panel_b_background_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((obj.getRedirectUrl() != null) && (obj.getRedirectUrl().length() > 5)) {
                                Intent intent = new Intent(context, CommonWebViewActivity.class);
                                intent.putExtra("URL", obj.getRedirectUrl());
                                // startActivity(intent);
                            }
                        }
                    });


                    if (obj.isInteractionsEnabled()) {
                        holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E  Optional
                    } else {
                        holder.user_reaction_panel.setVisibility(View.GONE);   //E  Optional
                    }

                    if (obj.getTitle() == null) {
                        holder.title_panel.setVisibility(View.GONE);
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }
                    if (obj.getBody().length() > 3) {
                        holder.text_panel.setVisibility(View.VISIBLE); //  C  Optional
                    } else {
                        holder.text_panel.setVisibility(View.GONE);
                    }
                    holder.user_link_panel.setVisibility(View.GONE);   //F
                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.GONE);
                    holder.program_panel_text_heading.setVisibility(View.GONE);
                    holder.txt_panel_b_text_day.setVisibility(View.GONE);

                    holder.txt_panel_b_play_button.setVisibility(View.GONE);
                } else if (cellType.equals("PROGRAM")) {

                    holder.user_panel.setVisibility(View.VISIBLE);      // A
                    holder.media_panel.setVisibility(View.VISIBLE);        // D
                    holder.user_reaction_panel.setVisibility(View.VISIBLE);   //E

                    if (obj.getTitle() != null) {
                        holder.title_panel.setVisibility(View.GONE); // B  Optional
                    } else {
                        holder.title_panel.setVisibility(View.VISIBLE); // B  Optional
                    }

                    holder.text_panel.setVisibility(View.GONE); //  C  Optional
                    holder.user_link_panel.setVisibility(View.GONE);   //F

                    // G
                    holder.txt_panel_b_view_button.setVisibility(View.VISIBLE);
                    holder.program_panel_text_heading.setVisibility(View.VISIBLE);
                    holder.txt_panel_b_text_day.setVisibility(View.VISIBLE);

                    holder.txt_panel_b_play_button.setVisibility(View.GONE);


                    holder.program_panel_text_heading.setText(obj.getTitle());
                    holder.txt_panel_b_text_day.setText("sub Title");

                    String postThumbnailImageUrl = obj.getPostThumbnailUrl();
                    String postImageUrl = obj.getPostThumbnailUrl();
                    if (postThumbnailImageUrl != null) {
                        if (postThumbnailImageUrl.length() > 10) {
                            RequestOptions options = new RequestOptions()
                                    .override(intWidth, intHeight)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(context).load(postThumbnailImageUrl)
                                    .apply(options)
                                    .into(holder.txt_panel_b_background_image);
                        } else {
                            holder.txt_panel_b_background_image.setVisibility(View.GONE);
                        }
                    }
                    holder.txt_panel_b_view_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //   Intent intent = new Intent(getContext(), Timeline_NewPost_Activity.class);
                            //   startActivity(intent);
                        }
                    });

                }
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holde;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HERO;
        } else {
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }
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

//
//    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
//        return Glide
//                .with(context)
//                .load(BASE_URL_IMG + posterPath)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
//                .centerCrop()
//                .crossFade();
//    }


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Post r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
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


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;

        public HeroVH(View itemView) {
            super(itemView);
            layout_main_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal);
            layout_services_menu_horizontal = (LinearLayout) itemView.findViewById(R.id.layout_main_menu_horizontal_footer);

            TextView txt_desc1 = (TextView) itemView.findViewById(R.id.txt_desc1);
            TextView txt_desc2 = (TextView) itemView.findViewById(R.id.txt_desc2);
            TextView btn_action = (TextView) itemView.findViewById(R.id.btn_action);
            //  LinearLayout btn_action_lay = (LinearLayout) itemView.findViewById(R.id.btn_action_lay);
            ImageView main_services_image_icon = (ImageView) itemView.findViewById(R.id.remote_image_icon);


        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class PostCell extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private ProgressBar mProgress;

        LinearLayout img_btn_close_bglayout, user_reaction_panel_liked_user_list_layout, user_panel, title_panel, text_panel, user_link_panel, user_reaction_panel;
        ImageView user_reaction_panel_like_clicked_image, user_reaction_panel_liked_user_list_icon, img_user_picture, txt_panel_b_background_image, txt_panel_b_play_button;
        TextView txt_user_name, txt_time_ago, user_reaction_panel_like_text;
        ImageButton img_btn_close_intop, link_panel_timeline_readme;
        ConstraintLayout media_panel;
        ImageButton txt_panel_b_view_button;
        TextView program_panel_text_heading, link_panel_text_url, link_panel_text_desc;
        TextView txt_panel_b_text_day, title_panel_heading_text, text_panel_text_message;
        TextView user_reaction_panel_liked_user_list, user_reaction_panel_like_count, user_reaction_panel_comment_count, user_reaction_panel_comment_clicked;


        public PostCell(View convertView) {
            super(convertView);

            //  TYPE TIMELINE
            user_panel = (LinearLayout) convertView.findViewById(R.id.user_panel);
            title_panel = (LinearLayout) convertView.findViewById(R.id.title_panel);
            text_panel = (LinearLayout) convertView.findViewById(R.id.content_panel);
            media_panel = (ConstraintLayout) convertView.findViewById(R.id.media_panel);
            user_link_panel = (LinearLayout) convertView.findViewById(R.id.user_link_panel);
            user_reaction_panel = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel);
            user_reaction_panel_liked_user_list_layout = (LinearLayout) convertView.findViewById(R.id.user_reaction_panel_liked_user_list_layout);

            // panel G
            txt_panel_b_view_button = (ImageButton) convertView.findViewById(R.id.txt_panel_b_view_button);
            program_panel_text_heading = (TextView) convertView.findViewById(R.id.program_panel_text_heading);
            txt_panel_b_text_day = (TextView) convertView.findViewById(R.id.txt_panel_b_text_day);

            // button Play
            txt_panel_b_play_button = (ImageView) convertView.findViewById(R.id.txt_panel_b_play_button);


            user_reaction_panel_like_text = (TextView) convertView.findViewById(R.id.user_reaction_panel_like_text);
            user_reaction_panel_like_clicked_image = (ImageView) convertView.findViewById(R.id.user_reaction_panel_like_clicked_image);

            txt_panel_b_background_image = (ImageView) convertView.findViewById(R.id.imageView2);

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
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
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
