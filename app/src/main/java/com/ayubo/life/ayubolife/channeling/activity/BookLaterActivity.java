package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import java.util.Iterator;
import java.util.List;
import java.util.Map;



import com.ayubo.life.ayubolife.channeling.adapter.VideoSessionAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.VideoCallSession;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DatePickerFragment;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.util.DateDeserializer;

public class BookLaterActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_EXPERT_OBJECT = "expert_object";

    //instances
    private List<VideoCallSession> originalSessions;
    private List<VideoCallSession> sessions;
    private VideoSessionAdapter adapter;
    private Expert expert;

    //views
    private TextView txtName, txtSpeciality;
    private ImageView imgProfile;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_later);

        Toolbar toolbar = findViewById(R.id.toolbar_book_later);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        initViews();
        setButtons();
        readExtra(getIntent());
    }

    private void initViews() {
        txtName = findViewById(R.id.txt_later_name);
        txtSpeciality = findViewById(R.id.txt_later_specialty);
        imgProfile = findViewById(R.id.img_later_doctor);
        recyclerView = findViewById(R.id.recycler_later_schedules);
        progressBar = findViewById(R.id.prg_later_progress);

        findViewById(R.id.img_later_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setMinDate(Calendar.getInstance());
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
                        calendar.set(Calendar.HOUR, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        filterByDate(calendar.getTimeInMillis());
                    }
                });
            }
        });
    }

    private void readExtra(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_EXPERT_OBJECT))
            setExpertDetails((Expert) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT));
    }

    private void setExpertDetails(Expert expert) {
        this.expert = expert;

        txtName.setText(String.format("%s %s", expert.getTitle(), expert.getName()));
        txtSpeciality.setText(expert.getSpeciality());
        Glide.with(this).load(expert.getPicture()).into(imgProfile);

        getVideoSessions();
    }

    private void setButtons() {
        View view_channel = findViewById(R.id.layout_channel_button);
        setButton(view_channel, getString(R.string.channel), ContextCompat.getDrawable(this, R.drawable.channel), VideoRequestActivity.ButtonStatus.enable);
        view_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        View view_video = findViewById(R.id.layout_video_button);
        setButton(view_video, getString(R.string.video_short_title), ContextCompat.getDrawable(this, R.drawable.video_selected), VideoRequestActivity.ButtonStatus.selected);
        view_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        View view_report = findViewById(R.id.layout_review_button);
        setButton(view_report, getString(R.string.review), ContextCompat.getDrawable(this, R.drawable.review_disable), VideoRequestActivity.ButtonStatus.disabled);
        view_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View view_ask = findViewById(R.id.layout_ask_button);
        setButton(view_ask, getString(R.string.ask), ContextCompat.getDrawable(this, R.drawable.ask_disable), VideoRequestActivity.ButtonStatus.disabled);
        view_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setButton(View view, String name, Drawable drawable, VideoRequestActivity.ButtonStatus status) {
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

    private void filterByDate(final long time) {
        sessions.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (VideoCallSession session : originalSessions) {
                    long available = session.getStart().getTime();
                    if (available > time) {
                        sessions.add(session);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void getVideoSessions() {
        progressBar.setVisibility(View.VISIBLE);
        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_VIDEO_SESSIONS, new VideoSessionParams().getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(BookLaterActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(final String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                readVideoSession(response);
                                sessions = new ArrayList<>(originalSessions);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        setVideoSessionAdapter();
                                    }
                                });
                            }
                        }).start();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                    }
                }).execute();
    }

    private void readVideoSession(String response) {
        originalSessions = new ArrayList<>();

//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getInt("result") == 0) {
//                JSONObject dataObject = jsonObject.getJSONObject("data");
//                Type sessionType = new TypeToken<HashMap<String, VideoCallSession>>() {
//                }.getType();
//
//                Gson gson = new GsonBuilder()
//                        .registerTypeAdapter(Date.class, new DateDeserializer())
//                        .create();
//                HashMap<String, VideoCallSession> sessions = gson.fromJson(dataObject.toString(), sessionType);
//                Iterator it = sessions.entrySet().iterator();
//                parent:
//                while (it.hasNext()) {
//                    Map.Entry pair = (Map.Entry) it.next();
//                    VideoCallSession videoCallSession = (VideoCallSession) pair.getValue();
//                    int i = 0;
//                    for (VideoCallSession item : originalSessions) {
//                        if (compareToDay(item.getStart(), videoCallSession.getStart()) > 0) {
//                            originalSessions.add(i, videoCallSession);
//                            it.remove();
//                            continue parent;
//                        }
//                        i++;
//                    }
//                    originalSessions.add(videoCallSession);
//                    it.remove();
//                }
//            } else {
//                Toast.makeText(BookLaterActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class VideoSessionParams extends SoapBasicParams {

        private String doctorId;
        private String locationId;
        private String date;

        VideoSessionParams() {

            locationId = AppConfig.LOCATION_VIDEO_ID;
            doctorId = expert.getId();
            date = "";
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("doctorID", doctorId);
                jsonObject.put("locationID", locationId);
                jsonObject.put("start", date);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    private void setVideoSessionAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new VideoSessionAdapter(this, sessions);
        recyclerView.setAdapter(adapter);
        adapter.setOnVideoSessionListener(new VideoSessionAdapter.OnVideoSessionListener() {
            @Override
            public void onVideoSessionClicked(VideoCallSession location) {
                if (progressBar.getVisibility() == View.GONE) {
                    Intent intent = new Intent(BookLaterActivity.this, VideoSessionActivity.class);
                    intent.putExtra(VideoSessionActivity.EXTRA_VIDEO_SESSION, location);
                    intent.putExtra(VideoSessionActivity.EXTRA_VIDEO_SESSIONS, (Serializable) originalSessions);
                    intent.putExtra(VideoSessionActivity.EXTRA_EXPERT_OBJECT, expert);
                    startActivity(intent);
                }
            }
        });
    }

}
