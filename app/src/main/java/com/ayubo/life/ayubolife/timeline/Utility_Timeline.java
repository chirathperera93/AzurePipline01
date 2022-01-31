package com.ayubo.life.ayubolife.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.notification.activity.SingleTimeline_Activity;
import com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse;
import com.ayubo.life.ayubolife.pojo.timeline.Post;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.timeline.pagination.PaginationAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Utility_Timeline {

    public static boolean isValidList(ArrayList<Post> timelinePostList) {
        boolean isValid=false;

        if((timelinePostList!=null) && (timelinePostList.size()>0)){
            isValid=true;
        }

        return isValid;
    }
    public static void showAlert_Add(Context c, final String appToken, String msg, final Post post,final PaginationAdapter adapter) {

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
    public static void sendALike(Integer postid, int status,String appToken) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);
        String post_id = Integer.toString(postid);

        Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call = apiService.LikeAPost(AppConfig.APP_BRANDING_ID,appToken, post_id, status);
        call.enqueue(new Callback<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Response<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> response) {
                if (response.isSuccessful()) {
                    com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse obj = response.body();
                    int res = obj.getResult();
                    System.out.println("=========res==========");
                }
            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }
    public static void deleteAPost(String appToken,final Post postobj) {

        ApiInterface apiService = ApiClient.getNewApiClient().create(ApiInterface.class);


        Call<LikeMainResponse> call = apiService.DeleteAPost(AppConfig.APP_BRANDING_ID,appToken, Integer.toString(postobj.getPostId()));
        call.enqueue(new Callback<LikeMainResponse>() {
            @Override
            public void onResponse(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Response<LikeMainResponse> response) {
                if (response.isSuccessful()) {
                    com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse obj = response.body();
                    int res = obj.getResult();
                    if (res == 0) {
                        //   System.out.println("======List size=====" + timelinePostList.size());
                        //  timelinePostList.remove(postobj);
                        // System.out.println("======List size=====" + timelinePostList.size());

                        //   CustomAdapterFresh customAdapter = new CustomAdapterFresh(getContext(), R.id.text, timelinePostList);
                        //   mainListview.setAdapter(customAdapter);

                        System.out.println("=======Successfully Deleted=====");
                    }
                }

            }

            @Override
            public void onFailure(Call<com.ayubo.life.ayubolife.pojo.timeline.LikeMainResponse> call, Throwable t) {
                System.out.print("");
            }
        });
    }
    public static String getImageUrlFromType(Post obj) {
        String imageURL = null;
        obj.getType();
        if (obj.getType().equals("IMAGE_POST")) {
            imageURL = obj.getPostImageUrl();
        }
        if (obj.getType().equals("VIDEO_POST")) {
            imageURL = obj.getVideoThumbnail();
        }
        if (obj.getType().equals("GIF_POST")) {
            imageURL = obj.getGifThumbnail();
        }
        if (obj.getType().equals("LINK_POST")) {
            imageURL = obj.getLinkInfo().getThumbnailUrl();
        }
        if (obj.getType().equals("SYSTEM_POST")) {
            imageURL = obj.getPostImageUrl();
        }
        if (obj.getType().equals("PROGRAM_POST")) {
            imageURL = obj.getPostImageUrl();
        }
        if (obj.getType().equals("IMAGE_POST")) {
            imageURL = obj.getPostImageUrl();
        }
        //  System.out.println("==============================================="+obj.getType());
        //   System.out.println("===u=="+imageURL);
        return imageURL;
    }
    public static String getLikesStringFromCount(Integer liked_count) {
        String likeCounts = null;
        if (liked_count > 1) {
            likeCounts = Integer.toString(liked_count) + " Likes";
        } else if (liked_count == 1) {
            likeCounts = Integer.toString(liked_count) + " Like";
        } else if (liked_count == 0) {
            likeCounts = Integer.toString(liked_count) + " Like";
        }

        return likeCounts;
    }
    public static String getTimeStringFromInteger(Integer timeInInteger) {

        long number = Long.valueOf(timeInInteger);
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

        return part1;
    }



}
