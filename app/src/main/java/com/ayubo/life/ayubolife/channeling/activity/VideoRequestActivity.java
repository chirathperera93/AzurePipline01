package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.adapter.DoctorsAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoRequestActivity extends AppCompatActivity {

    //constants
    private static final String DOCTOR_FULL_NAME_WITH_TITLE = "%s. %s";
    private static final String DOCTOR_FULL_NAME_WITHOUT_TITLE = "%s";

    //instances
    private List<Expert> experts;
    private List<Expert> fullExperts;
    //    private Calendar selectedDate = Calendar.getInstance();
    private DoctorsAdapter adapter;

    //views
    private TextView txtDoctorName, txtDoctorSpecialty, txtTimeRemaining, txtDateAvailable, txtPrice;
    private DiscreteScrollView recyclerDoctors;
    //    private ShimmerRecyclerView recyclerSchedules;
    private ProgressBar progressBar;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_reqest);

        initView();
        setButtons();
        setDoctorDetails("", "", null, "");
        getLifeStylists();
    }

    private void initView() {
        txtDoctorName = findViewById(R.id.txt_name_doctor_request);
        txtDoctorSpecialty = findViewById(R.id.txt_specialty_doctor_request);
        EditText edtSearch = findViewById(R.id.edt_search_doctor_request);
        progressBar = findViewById(R.id.progress_loading_doctors);
        txtTimeRemaining = findViewById(R.id.txt_video_available_value);
        txtDateAvailable = findViewById(R.id.txt_video_available_date);
        txtPrice = findViewById(R.id.txt_video_price);
//        txtTime = findViewById(R.id.txt_video_available_time);
        group = findViewById(R.id.group);
//        findViewById(R.id.btn_calendar_request).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerFragment newFragment = new DatePickerFragment();
//                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
//                newFragment.setMinDate(Calendar.getInstance());
//                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
//                    @Override
//                    public void onSelected(Calendar calendar) {
//                        selectedDate = calendar;
//                        int currentItem = recyclerDoctors.getCurrentItem();
//                        if (experts != null && experts.size() > currentItem)
//                            getSchedules(experts.get(currentItem).getId());
//                    }
//                });
//            }
//        });

        findViewById(R.id.txt_video_book_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (experts != null && experts.size() > recyclerDoctors.getCurrentItem()) {
                    Intent intent = new Intent(VideoRequestActivity.this, UploadActivity.class);
                    intent.putExtra(UploadActivity.EXTRA_EXPERT_OBJECT, experts.get(recyclerDoctors.getCurrentItem()));
                    startActivity(intent);
                } else {
                    Toast.makeText(VideoRequestActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.txt_video_book_later).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (experts != null && experts.size() > recyclerDoctors.getCurrentItem()) {
                    Intent intent = new Intent(VideoRequestActivity.this, VideoSessionActivity.class);
                    intent.putExtra(VideoSessionActivity.EXTRA_EXPERT_OBJECT, experts.get(recyclerDoctors.getCurrentItem()));
                    startActivity(intent);
                } else {
                    Toast.makeText(VideoRequestActivity.this, "Please wait...", Toast.LENGTH_LONG).show();
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (experts != null) {
                            experts.clear();
                            experts.addAll(filterExperts(charSequence.toString()));
                            adapter.notifyDataSetChanged();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (experts.size() > 0)
                                        recyclerDoctors.scrollToPosition(0);
                                }
                            });
                        }
                    }
                }).run();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        group.setVisibility(View.GONE);

        recyclerDoctors = findViewById(R.id.recycler_doctors_request);
//        recyclerSchedules = findViewById(R.id.recycler_schedules_request);
//        recyclerSchedules.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerDoctors.setSlideOnFling(true);
        recyclerDoctors.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.BOTTOM)
                .build());
        recyclerDoctors.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                if (experts != null && adapterPosition >= 0) {
                    if (experts.size() > adapterPosition)
                        setExpertInfo(adapterPosition);
                } else
                    setDoctorDetails("", "", null, "");
            }
        });
//        recyclerDoctors.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);
    }

    private void setExpertInfo(int pos) {
        Expert expert = experts.get(pos);
        String name;
        if (expert.getTitle() != null && expert.getTitle().equals("null"))
            name = String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITH_TITLE, expert.getTitle(), expert.getName());
        else
            name = String.format(Locale.getDefault(), DOCTOR_FULL_NAME_WITHOUT_TITLE, expert.getName());

        Date time = null;
        String price = "Not Available";

        if (expert.getLocations() != null && expert.getLocations().size() > 0) {
            time = TimeFormatter.stringToDate(expert.getLocations().get(0).getNext_available(), "yyyy-MM-dd HH:mm:ss");
            price = expert.getLocations().get(0).getFee();
        }
        setDoctorDetails(name, expert.getSpeciality(), time, price);
//        getSchedules(expert.getId());
    }

    private void setButtons() {

        View view_video = findViewById(R.id.layout_video_button);
        setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_selected), ButtonStatus.selected);
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_channel = findViewById(R.id.layout_channel_button);
        setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.channel), ButtonStatus.enable);
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_report = findViewById(R.id.layout_review_button);
        setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_disable), ButtonStatus.disabled);
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.ask_disable), ButtonStatus.disabled);
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setButton(View view, String name, Drawable drawable, ButtonStatus status) {
        ((ImageView) view.findViewById(R.id.img_profile_doctor_row)).setImageDrawable(drawable);
        TextView txtTitle = view.findViewById(R.id.txt_name_doctor_row);
        txtTitle.setTextSize(12);
        txtTitle.setText(name);

        switch (status) {
            case enable:
                txtTitle.setTextColor(ContextCompat.getColor(this, R.color.text_color_secondary));
                break;
            case disabled:
                txtTitle.setTextColor(ContextCompat.getColor(this, R.color.text_color_quinary));
                break;
            default:
                txtTitle.setTextColor(ContextCompat.getColor(this, R.color.text_color_primary));
        }
    }

    private void setDoctorDetails(String name, String specialty, Date time, String price) {
        txtDoctorName.setText(name);
        txtDoctorSpecialty.setText(specialty);
        txtPrice.setText(price);
        if (time != null) {
            txtDateAvailable.setText(TimeFormatter.millisecondsToString(time.getTime(), "EEEE, MMM dd, yyyy h:mmaa").toUpperCase());
//            txtTime.setText(TimeFormatter.millisecondsToString(time.getTime(), "h:mm aa"));
            txtTimeRemaining.setText(TimeFormatter.getRelativeShort(this, time.getTime()));
        } else {
            txtDateAvailable.setText("");
            txtTimeRemaining.setText("");
        }
    }

//    private void setSessionRecyclerView(List<VideoSession> dataArray) {
//        ChannelDoctorAdapter sessionAdapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(dataArray), ChannelDoctorAdapter.VIEW_TYPE_SESSION);
//        sessionAdapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
//            @Override
//            public void onChannelClicked(Object channelDoctor) {
//                Intent intent = new Intent(VideoRequestActivity.this, DetailActivity.class);
//                intent.putExtra(DetailActivity.EXTRA_DETAIL_ACTION_OBJECT, new DetailVideoView(experts.
//                        get(recyclerDoctors.getCurrentItem()), (VideoSession) channelDoctor));
//                startActivity(intent);
//            }
//
////            @Override
////            public void onGetMoreSessionsClicked(String location) {
////
////            }
//        });
//        recyclerSchedules.setAdapter(sessionAdapter);
//    }

    private void setDoctorsRecyclerView() {
        adapter = new DoctorsAdapter(this, experts, null);
        recyclerDoctors.setAdapter(adapter);
    }

//    private void getDoctors() {
//        progressBar.setVisibility(View.VISIBLE);
//        getData(AppConfig.METHOD_SOAP_VIDEO_DOCTORS, new DownloadData.DownloadListener() {
//            @Override
//            public void onDownloadSuccess(String response, int what, int code) {
//                getLifeStylists();
//                readExperts(response);
//            }
//
//            @Override
//            public void onDownloadFailed(String errorMessage, int what, int code) {
//                getLifeStylists();
//            }
//        });
//    }

    private void getLifeStylists() {
        progressBar.setVisibility(View.VISIBLE);
        getData(AppConfig.METHOD_SOAP_VIDEO_EXPERTS, new DownloadData.DownloadListener() {
            @Override
            public void onDownloadSuccess(String response, int what, int code) {
                progressBar.setVisibility(View.GONE);
                readExperts(response);
                setDoctorsRecyclerView();
            }

            @Override
            public void onDownloadFailed(String errorMessage, int what, int code) {
                progressBar.setVisibility(View.GONE);
                setDoctorsRecyclerView();
            }
        });
    }

//    private void getSchedules(String expertId) {
//        ExpertScheduleParams params = new ExpertScheduleParams();
//        params.doctorID = expertId;
//        params.location = AppConfig.LOCATION_VIDEO_ID;
//        params.intervalStart = TimeFormatter.millisecondsToString(selectedDate.getTimeInMillis(), TimeFormatter.DATE_FORMAT_VIDEO);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(selectedDate.getTimeInMillis());
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 3);
//        params.intervalEnd = TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.DATE_FORMAT_VIDEO);
//
//        recyclerSchedules.showShimmerAdapter();
//        getData(AppConfig.METHOD_SOAP_GET_SESSION, params.getSearchParams(), new DownloadData.DownloadListener() {
//            @Override
//            public void onDownloadSuccess(final String response, int what, int code) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final List<VideoSession> videoSessions = readSessions(response);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                recyclerSchedules.hideShimmerAdapter();
//                                setSessionRecyclerView(videoSessions);
//                            }
//                        });
//                    }
//                }).run();
//            }
//
//            @Override
//            public void onDownloadFailed(String errorMessage, int what, int code) {
//                recyclerSchedules.hideShimmerAdapter();
//                setSessionRecyclerView(new ArrayList<VideoSession>());
//            }
//        });
//    }

    private void readExperts(String response) {
        fullExperts = new ArrayList<>();
        if (experts == null)
            experts = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(response);
            Type messageType = new TypeToken<List<Expert>>() {
            }.getType();

            List<Expert> dataArray = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), messageType);
            for (Expert expert : dataArray) {
                for (Expert.Location location : expert.getLocations()) {
                    if (location.getId().equals(AppConfig.LOCATION_VIDEO_ID)) {
                        experts.add(expert);
                        break;
                    }
                }
            }
            fullExperts.addAll(experts);
            group.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private List<VideoSession> readSessions(String response) {
//
//        List<VideoSession> sessions = new ArrayList<>();
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            Type messageType = new TypeToken<List<VideoSession>>() {
//            }.getType();
//
//            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();
//
//            List<VideoSession> dataArray = gson.fromJson(jsonObject.getJSONArray("data").toString(), messageType);
//
//            for (VideoSession videoSession : dataArray) {
//                if (videoSession.getTitle() == null || !videoSession.getTitle().equals("Unavailable"))
//                    sessions.add(videoSession);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return sessions;
//    }

    private void getData(String methodName, DownloadData.DownloadListener listener) {
        getData(methodName, new SoapBasicParams().getSearchParams(), listener);
    }

    private void getData(String methodName, String params, DownloadData.DownloadListener listener) {
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(methodName, params)).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), listener).execute();
    }

//    private class ExpertScheduleParams extends SoapBasicParams {
//        String doctorID = "";
//        String location = "";
//        String intervalStart = "";
//        String intervalEnd = "";
//
//        public String getSearchParams() {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("doctorID", doctorID);
//                jsonObject.put("location", location);
//                jsonObject.put("intervalStart", intervalStart);
//                jsonObject.put("intervalEnd", intervalEnd);
//                jsonObject.put("userid", user_id);
//                jsonObject.put("token_key", token_key);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonObject.toString();
//        }
//    }

    private List<Expert> filterExperts(String value) {
        value = value.toLowerCase();
        List<Expert> localExpert = new ArrayList<>();

        for (Expert expert : fullExperts) {
            if (expert.getName().toLowerCase().contains(value) || expert.getSpeciality().toLowerCase().contains(value))
                localExpert.add(expert);
        }

        return localExpert;
    }

    enum ButtonStatus {
        enable, selected, disabled
    }
}
