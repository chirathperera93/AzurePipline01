package com.ayubo.life.ayubolife.channeling.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.adapter.ChannelDoctorAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.Ask;
import com.ayubo.life.ayubolife.channeling.model.Channel;
import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Hospital;
import com.ayubo.life.ayubolife.channeling.model.NewFavDoctor;
import com.ayubo.life.ayubolife.channeling.model.Review;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.Video;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DoctorActivity extends AppCompatActivity {

    //constant
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";

    //instances
    private ChannelDoctor doctor;

    //views
//    private ProgressBar progressBar;
    private View errorView;
    private LottieAnimationView imgFavorite;
    //    private TextView txtErrorMessage;
    ImageButton btn_back_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
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

        readExtras();

    }

    private void readExtras() {
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(EXTRA_DOCTOR_DETAILS)) {
                Object object = getIntent().getExtras().getSerializable(EXTRA_DOCTOR_DETAILS);
                //  Object object = Ram.getChannelDoctorList();
                if (object != null)
                    if (object instanceof ChannelDoctor) {
                        doctor = (ChannelDoctor) object;
                        Ram.setDoctorImage(doctor.getDoc_image());

                        if (doctor.getHospitals().size() == 1) {
                            ArrayList<Hospital> myList = new ArrayList<>(doctor.getHospitals().values());
                            Hospital hospital = myList.get(0);
                            Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
                            intent.putExtra(ScheduleActivity.EXTRA_LOCATION_DETAILS, hospital);
                            intent.putExtra(ScheduleActivity.EXTRA_LOCATION_LIST, new ArrayList<>(doctor.getHospitals().values()));
                            intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                            startActivity(intent);
                            finish();
                        } else {
                            setView(doctor.getDoctor_name(), doctor.getSpecialization(), doctor.getDoc_image());
                            setRecyclerView(new ArrayList<>(doctor.getHospitals().values()));
                        }
                    } else if (object instanceof NewFavDoctor) {
                        NewFavDoctor doctorNew = (NewFavDoctor) object;
                        setView(doctorNew.getDoctor_name(), doctorNew.getSpecialization(), doctorNew.getDoc_image());
                        //setRecyclerView(new ArrayList<>(doctor.getHospitals().values()));
                    }
            }
    }


    private void setView(String name, String specialty, String imgUrl) {
//        progressBar = findViewById(R.id.progress_loading_doctor);
        errorView = findViewById(R.id.layout_error);
//        txtErrorMessage = errorView.findViewById(R.id.txt_message_error);
        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getData();
            }
        });


        Ram.setDoctorImage(imgUrl);

        TextView txtDocName = findViewById(R.id.txt_name_doctor);
        TextView txtDocSpecialty = findViewById(R.id.txt_specialty_doctor);
        imgFavorite = findViewById(R.id.img_favorite_doctor);
        ImageView imgDoctor = findViewById(R.id.img_profile_doctor);

        String sstrDocName = Utility.upperCaseWords(name);

        txtDocName.setText(sstrDocName);
        txtDocSpecialty.setText(specialty);
        Glide.with(this).load(imgUrl).into(imgDoctor);

        if (doctor != null) {
            if (!doctor.getFavourite().equals("no")) startCheckAnimation();
        }

        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheckAnimation();
                if (doctor.getFavourite().equals("no")) {
                    addFavorites();
                    doctor.setFavourite("yes");
                } else {
                    removeFavorites();
                    doctor.setFavourite("no");
                }
            }
        });

        setButtons();
    }


    private void startCheckAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                imgFavorite.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if (imgFavorite.getProgress() == 0f) {
            animator.start();
        } else {
            imgFavorite.setProgress(0f);
        }
    }

    private void setButtons() {
        View view_video = findViewById(R.id.layout_video_button);

        final Video v = doctor.getVideo();
        if (v.getEnable().equals("1")) {
            setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_call_normal), false);
        } else if (v.getEnable().equals("0")) {
            setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_call_disable), false);
        } else {
            setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_call_disable), false);
        }

        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.getEnable().equals("1")) {

                    String activityName = "my_doctor";
                    Intent intent = new Intent(DoctorActivity.this, MyDoctorLocations_Activity.class);
                    intent.putExtra("doctor_id", v.getMeta());
                    intent.putExtra("activityName", activityName);
                    startActivity(intent);
                }


                System.out.println();
            }
        });
        View view_channel = findViewById(R.id.layout_channel_button);
        Channel c = doctor.getChannel();
        if (c.getEnable().equals("1")) {
            //   txtTitle.setTypeface(null, Typeface.BOLD);
            setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.channel_selected), true);
        } else if (c.getEnable().equals("0")) {
            setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.channel_enable), true);
        }
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_report = findViewById(R.id.layout_review_button);

        Review rv = doctor.getReview();
        if (rv.getEnable().equals("1")) {
            setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_disable), false);
        } else if (rv.getEnable().equals("0")) {
            setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_disable), false);
        } else {
            setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_disable), false);
        }
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);

        Ask ask = doctor.getAsk();
        if (ask.getEnable().equals("1")) {
            setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.ask), false);

        } else if (ask.getEnable().equals("0")) {
            setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.ask_disable), false);
        } else {

        }

        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ask ask = doctor.getAsk();
                if (ask.getEnable().equals("1")) {
                    AyuboChatActivity.Companion.startActivity(DoctorActivity.this, String.valueOf(doctor.getDoctor_code()), false, false, "");
                }

            }
        });
    }

    private void setRecyclerView(final List<Hospital> locations) {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_available_schedules);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(locations), ChannelDoctorAdapter.VIEW_TYPE_HOSPITAL);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {
                Hospital hospital = (Hospital) channelDoctor;
                Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_DETAILS, hospital);
                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_LIST, new ArrayList<>(locations));
                intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                startActivity(intent);
            }

//            @Override
//            public void onGetMoreSessionsClicked(String location) {
////                Intent intent = new Intent(DoctorActivity.this, ScheduleActivity.class);
////                intent.putExtra(ScheduleActivity.EXTRA_LOCATION_NAME, location);
////                startActivity(intent);
//            }
        });
        recycler_schedules.setAdapter(adapter);
    }

    private void setButton(View view, String name, Drawable drawable, boolean isSelected) {
        ((ImageView) view.findViewById(R.id.img_profile_doctor_row)).setImageDrawable(drawable);
        TextView txtTitle = view.findViewById(R.id.txt_name_doctor_row);

        txtTitle.setText(name);
        txtTitle.setTextColor(ContextCompat.getColor(this, isSelected ? R.color.text_color_secondary : R.color.text_color_primary));
    }

    private void addFavorites() {
        errorView.setVisibility(View.GONE);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_ADD_FAVORITE,
                        new FavoriteParams(String.valueOf(doctor.getDoctor_code()),
                                String.valueOf(doctor.getSpecialization_id())).getSearchParamsNEW())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(DoctorActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.added_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(DoctorActivity.this, R.string.failed_add_favourite, Toast.LENGTH_LONG).show();
                        startCheckAnimation();
                    }
                }).execute();
    }

    private void removeFavorites() {
        errorView.setVisibility(View.GONE);

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_REMOVE_FAVORITE,
                        new FavoriteParams(String.valueOf(doctor.getDoctor_code()),
                                String.valueOf(doctor.getSpecialization_id())).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(DoctorActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.remove_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(DoctorActivity.this, R.string.failed_remove_favourite, Toast.LENGTH_LONG).show();
                        startCheckAnimation();
                    }
                }).execute();
    }

    class FavoriteParams extends SoapBasicParams {
        private String doctorId;
        private String specialization_id;
//        private String description;

        FavoriteParams(String docId, String specId) {

            this.doctorId = docId;
            this.specialization_id = specId;
        }


        public String getSearchParamsNEW() {
            JSONObject jsonObject = new JSONObject();
            JsonArray array = new JsonArray();
            Context c = null;
            ArrayList<String> ar = null;
            c = Ram.getMycontext();
            if (c != null) {

                PrefManager pref = new PrefManager(c);
                user_id = pref.getLoginUser().get("uid");
                Log.d("getSearchParams", "=================================Context not null");
            } else {
                Log.d("getSearchParams", "==============================Context  null");
            }
            try {

                jsonObject.put("0", user_id);
                jsonObject.put("1", doctorId);
                jsonObject.put("2", specialization_id);
                jsonObject.put("3", "12345");
                jsonObject.put("4", token_key);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("============Sending============" + jsonObject.toString());


            return jsonObject.toString();
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();

            Context c = null;
            c = Ram.getMycontext();
            if (c != null) {

                PrefManager pref = new PrefManager(c);
                user_id = pref.getLoginUser().get("uid");
                Log.d("getSearchParams", "=================================Context not null");
            } else {
                Log.d("getSearchParams", "==============================Context  null");
            }
            try {
                jsonObject.put("userId", user_id);
                jsonObject.put("doctorId", doctorId);
                jsonObject.put("specialization_id", specialization_id);
                jsonObject.put("hospital_id", "");
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

//    class RemoveFavoriteParams extends SoapBasicParams {
////        private String doctorId;
//        private String specialization_id;
//
//        RemoveFavoriteParams(String docId, String specialization) {
//            this.doctorId = docId;
//            this.specialization_id = specialization;
//        }
//
//        public String getSearchParams() {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("user_id", user_id);
//                jsonObject.put("doctorId", doctorId);
//                jsonObject.put("specialization_id", specialization_id);
//                jsonObject.put("token_key", token_key);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonObject.toString();
//        }
//    }
}
