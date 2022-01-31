package com.ayubo.life.ayubolife.channeling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.adapter.SessionAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.channeling.model.SessionTimeSlot;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.VideoCallSession;
import com.ayubo.life.ayubolife.channeling.util.DateDeserializer;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoSessionActivity extends AppCompatActivity {

    //constant
    public static final String EXTRA_VIDEO_SESSION = "session_object";
    public static final String EXTRA_VIDEO_SESSIONS = "session_list";
    public static final String EXTRA_EXPERT_OBJECT = "expert_object";
    public static final String EXTRA_DOCTOR_PAYMENT = "doctor_payment";
    public static final String EXTRA_APPOINTMENT_SOURCE = "appointment_source";

    //instances
//    private VideoCallSession videoSession;
    private List<VideoCallSession> videoSessions;
    private SessionTimeSlot dates;
    private Expert expert;
    private LinearLayoutManager timeLayoutManger;

    //views
    private ProgressBar progressBar;
    private RecyclerView recyclerView_time, recyclerView_slots, recyclerView_days;
    private TextView txtMonth, txtHour, txtSlot;

    private String isDoctorPaymentFree = "false";
    private String appointment_source = "";

    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_session);
        isDoctorPaymentFree = "false";
        appointment_source = "";

        pref = new PrefManager(this);
        readExtras(getIntent());
        initViews();
        getVideoSessions();
    }

    @SuppressWarnings("unchecked")
    private void readExtras(Intent intent) {
        if (intent.getExtras() != null) {
//            if (intent.getExtras().containsKey(EXTRA_VIDEO_SESSION))
//                videoSession = (VideoCallSession) intent.getExtras().getSerializable(EXTRA_VIDEO_SESSION);
            if (intent.getExtras().containsKey(EXTRA_EXPERT_OBJECT)) {
                expert = (Expert) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT);
                isDoctorPaymentFree = (String) intent.getExtras().getSerializable(EXTRA_DOCTOR_PAYMENT);
                appointment_source = (String) intent.getExtras().getSerializable(EXTRA_APPOINTMENT_SOURCE);
            }

//            if (intent.getExtras().containsKey(EXTRA_VIDEO_SESSIONS))
//                videoSessions = (List<VideoCallSession>) intent.getExtras().getSerializable(EXTRA_VIDEO_SESSIONS);
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progress_video_session);
        TextView txtDoctor = findViewById(R.id.txt_video_session_doctor);
        TextView txtSpeciality = findViewById(R.id.txt_video_session_speciality);

        txtDoctor.setText(String.format("%s %s", expert.getTitle(), expert.getName()).trim());
        txtSpeciality.setText(expert.getSpeciality());

        txtHour = findViewById(R.id.txt_video_session_hour);
        txtSlot = findViewById(R.id.txt_video_session_session);

        //setting day adapter
        txtMonth = findViewById(R.id.txt_video_session_month);
        recyclerView_days = findViewById(R.id.recycler_video_session_day);
        recyclerView_days.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        txtMonth.setText(TimeFormatter.millisecondsToString(videoSession.getStart().getTime(), "MMM yyyy"));

        //setting time adapter
        recyclerView_time = findViewById(R.id.recycler_video_session_hour);
        timeLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_time.setLayoutManager(timeLayoutManger);

        //setting slot adapter
        recyclerView_slots = findViewById(R.id.recycler_video_session_session);
        recyclerView_slots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setDayAdapter() {
        SessionAdapter dateAdapter = new SessionAdapter(this, new ArrayList<Object>(videoSessions),
                videoSessions != null && videoSessions.size() > 0 ? videoSessions.get(0) : null);
        recyclerView_days.setAdapter(dateAdapter);
        dateAdapter.setOnItemClickListener(new SessionAdapter.OnItemSelected() {
            @Override
            public void onItemClicked(Object item) {
                txtMonth.setText(TimeFormatter.millisecondsToString(((VideoCallSession) item).getStart().getTime(), "MMM yyyy"));
                getTimeSlots((VideoCallSession) item);
            }
        });
        if (videoSessions != null && videoSessions.size() > 0) {
            getTimeSlots(videoSessions.get(0));
            txtMonth.setVisibility(View.VISIBLE);
            txtMonth.setText(TimeFormatter.millisecondsToString(videoSessions.get(0).getStart().getTime(), "MMM yyyy"));
        }
    }

    private void setTimeAdapters() {
        if (dates != null && dates.getTime_slots() != null) {

            List<VideoCallTime> calendarList = new ArrayList<>();
            VideoCallTime selectedSession = null;
            int position = 0;

            if (dates.getTime_slots().size() > 0) {
                parent:
                for (int i = 0; i < 24; i++) {
                    Calendar session = getCalendarObject(dates.getTime_slots().get(0), i);
                    for (Date date : dates.getTime_slots()) {
                        String hourString = TimeFormatter.millisecondsToString(date.getTime(), "HH");
                        String calendarHour = String.valueOf(session.get(Calendar.HOUR_OF_DAY));
                        if (hourString.equals(calendarHour)) {
                            VideoCallTime time = new VideoCallTime(session, true);
                            if (selectedSession == null) {
                                position = i;
                                selectedSession = time;
                            }
                            calendarList.add(time);
                            continue parent;
                        }
                    }
                    calendarList.add(new VideoCallTime(session, false));
                }
            }
            txtHour.setVisibility(View.VISIBLE);
            SessionAdapter timeAdapter = new SessionAdapter(this, new ArrayList<Object>(calendarList), selectedSession);
            timeLayoutManger.scrollToPositionWithOffset(position, 100);
            timeAdapter.setOnItemClickListener(new SessionAdapter.OnItemSelected() {
                @Override
                public void onItemClicked(Object item) {
                    setSlotAdapter((VideoCallTime) item);
                }
            });
            recyclerView_time.setAdapter(timeAdapter);
            setSlotAdapter(selectedSession);


        } else {
            recyclerView_time.setAdapter(new SessionAdapter(this, new ArrayList<>(), null));
            recyclerView_slots.setAdapter(new SessionAdapter(this, new ArrayList<>(), null));
        }
    }

    private void setSlotAdapter(VideoCallTime time) {
        txtSlot.setVisibility(View.VISIBLE);
        List<VideoCallSlot> slots = createTimeSlotArray(time);
        SessionAdapter slotAdapter = new SessionAdapter(VideoSessionActivity.this,
                new ArrayList<Object>(slots), null);
        recyclerView_slots.setAdapter(slotAdapter);
        slotAdapter.setOnItemClickListener(new SessionAdapter.OnItemSelected() {
            @Override
            public void onItemClicked(Object item) {
//                showDialogToCreateAppointment(((VideoCallSlot) item).getStart().getTime());
                Intent intent = new Intent(VideoSessionActivity.this, UploadActivity.class);
                intent.putExtra(UploadActivity.EXTRA_EXPERT_OBJECT, expert);

                VideoCallSlot videoCallSlot = (VideoCallSlot) item;

                System.out.println(videoCallSlot);
                Date s = videoCallSlot.start.getTime();
                System.out.println(s);

                intent.putExtra(UploadActivity.EXTRA_SESSION_TIME, s);
                intent.putExtra(UploadActivity.EXTRA_DOCTOR_PAYMENT, isDoctorPaymentFree);
                intent.putExtra(UploadActivity.EXTRA_APPOINTMENT_SOURCE, appointment_source);
                startActivity(intent);
            }
        });
    }

    private Calendar getCalendarObject(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    private List<VideoCallSlot> createTimeSlotArray(VideoCallTime time) {
        List<VideoCallSlot> slots = new ArrayList<>();
        Calendar start;
        Calendar end = Calendar.getInstance();
        end.setTime(time.getCalendar().getTime());

        int i = 0;
        while (i < 60) {
            VideoCallSlot slot = new VideoCallSlot();
            start = Calendar.getInstance();
            start.setTime(end.getTime());
            end = Calendar.getInstance();
            end.setTime(start.getTime());

            i += dates.getDuration_minutes();
            end.set(Calendar.MINUTE, i);

            boolean found = false;
            for (Date date : dates.getTime_slots()) {
                if (TimeFormatter.millisecondsToString(date.getTime(), "HH:mm").equals(TimeFormatter.
                        millisecondsToString(start.getTimeInMillis(), "HH:mm"))) {
                    found = true;
                    break;
                }
            }
            slot.setStart(start);
            slot.setEnabled(found);
            slot.setEnd(end);
            slots.add(slot);
        }

        return slots;
    }

    private void getTimeSlots(VideoCallSession session) {
        progressBar.setVisibility(View.VISIBLE);


        String timeSlotsParams = new TimeSlotsParams(session).getSearchParams();
        String url = ApiClient.BASE_URL + "custom/service/v7/rest.php";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<VideoSessionResponseData> call = apiService.getVideoCallSessions(
                url,
                com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID,
                "videoCallTimeSlots",
                "JSON",
                "JSON",
                timeSlotsParams
        );

        call.enqueue(new Callback<VideoSessionResponseData>() {


            @Override
            public void onResponse(Call<VideoSessionResponseData> call, Response<VideoSessionResponseData> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);


                    assert response.body() != null;
                    JsonObject mainData =
                            new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

                    try {
                        JSONObject dataObj = new JSONObject(String.valueOf(mainData));

                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Date.class, new DateDeserializer())
                                .create();


                        dates = gson.fromJson(dataObj.toString(), SessionTimeSlot.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setTimeAdapters();


                } else {
                    progressBar.setVisibility(View.GONE);
                    dates = null;
                    Toast.makeText(VideoSessionActivity.this, "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VideoSessionResponseData> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                dates = null;
                setTimeAdapters();
                Toast.makeText(VideoSessionActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
            }
        });


//        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
//                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_VIDEO_TIME_SLOTS, new TimeSlotsParams(session).getSearchParams())).
//                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
//                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(String response, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getInt("result") == 0) {
//                                JSONObject dataObj = jsonObject.getJSONObject("data");
//
//                                Gson gson = new GsonBuilder()
//                                        .registerTypeAdapter(Date.class, new DateDeserializer())
//                                        .create();
//
//                                dates = gson.fromJson(dataObj.toString(), SessionTimeSlot.class);
//
//                            } else {
//                                dates = null;
//                                Toast.makeText(VideoSessionActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            dates = null;
//                            e.printStackTrace();
//                        }
//                        setTimeAdapters();
//                    }
//
//                    @Override
//                    public void onDownloadFailed(String errorMessage, int what, int code) {
//                        dates = null;
//                        setTimeAdapters();
//                        Toast.makeText(VideoSessionActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
//                    }
//                }).execute();
    }

//    private void showDialogToCreateAppointment(final Date timeSlot) {
//        new AlertDialog.Builder(this)
//                .setMessage(R.string.create_appointment)
//                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        createAnAppointment(timeSlot);
//                    }
//                })
//                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).create().show();
//    }

//    private void createAnAppointment(final Date startTime) {
//        progressBar.setVisibility(View.VISIBLE);
//        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
//                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_VIDEO_APPIONTMENT, new AppointmentParams(startTime).getSearchParams())).
//                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
//                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(String response, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getInt("result") == 0) {
//                                JSONObject dataObj = jsonObject.getJSONObject("data");
//
//                                VideoBooking booking = new VideoBooking();
//                                booking.setExpert(expert);
//                                booking.setStartTime(startTime);
//                                booking.setAppointmentId(dataObj.getString("appointment_id"));
//                                booking.setPaymentLink(dataObj.getString("hnb_payment_link"));
//                                booking.setAmount(Double.parseDouble(dataObj.getString("amount")));
//
//                                Intent intent;
//                                if (booking.getAmount() == 0)
//                                    intent = new Intent(VideoSessionActivity.this, ResultActivity.class);
//                                else
//                                    intent = new Intent(VideoSessionActivity.this, PayActivity.class);
//                                intent.putExtra(PayActivity.EXTRA_VIDEO_BOOKING_OBJECT, booking);
//                                startActivity(intent);
//                                return;
//                            } else {
//                                Toast.makeText(VideoSessionActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Toast.makeText(VideoSessionActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onDownloadFailed(String errorMessage, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(VideoSessionActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
//                    }
//                }).execute();
//    }

    private void getVideoSessions() {
        progressBar.setVisibility(View.VISIBLE);

        String searchParams = new VideoSessionParams().getSearchParams();
        String url = ApiClient.BASE_URL + "custom/service/v7/rest.php";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<VideoSessionResponseData> call = apiService.getVideoCallSessions(
                url,
                com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID,
                "videoCallSessions",
                "JSON",
                "JSON",
                searchParams
        );

        call.enqueue(new Callback<VideoSessionResponseData>() {

            @Override
            public void onResponse(Call<VideoSessionResponseData> call, Response<VideoSessionResponseData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    JsonObject mainData =
                            new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

                    videoSessions = readVideoSession(mainData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDayAdapter();
                        }
                    });

//                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<VideoSessionResponseData> call, Throwable throwable) {
                throwable.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });


//        new DownloadData(
//                this,
//                new DownloadDataBuilder().init(
//                        AppConfig.URL_AYUBO_SOAP_REQUEST,
//                        0,
//                        DownloadManager.POST_REQUEST
//                ).
//                        setParams(
//                                AppHandler.getSoapRequestParams(
//                                        AppConfig.METHOD_SOAP_GET_VIDEO_SESSIONS,
//                                        new VideoSessionParams().getSearchParams()
//                                )
//                        ).
//                        setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE)
//                        .setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)
//        ).
//                setOnDownloadListener(BookLaterActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(final String response, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                System.out.println(response);
//                                videoSessions = readVideoSession(response);
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        setDayAdapter();
//                                    }
//                                });
//                            }
//                        }).start();
//                    }
//
//                    @Override
//                    public void onDownloadFailed(String errorMessage, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                    }
//                }).execute();
    }


    private List<VideoCallSession> readVideoSession(JsonObject mainesponse) {
        List<VideoCallSession> originalSessions = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(mainesponse));
//            if (jsonObject.getInt("result") == 0) {
            JSONObject dataObject = jsonObject;


            Type sessionType = new TypeToken<HashMap<String, VideoCallSession>>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();
            HashMap<String, VideoCallSession> sessions = gson.fromJson(dataObject.toString(), sessionType);
            Iterator it = sessions.entrySet().iterator();
            parent:
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                VideoCallSession videoCallSession = (VideoCallSession) pair.getValue();
                int i = 0;
                for (VideoCallSession item : originalSessions) {
                    if (compareToDay(item.getStart(), videoCallSession.getStart()) > 0) {
                        originalSessions.add(i, videoCallSession);
                        it.remove();
                        continue parent;
                    }
                    i++;
                }
                originalSessions.add(videoCallSession);
                it.remove();
            }
//            } else {
//                Toast.makeText(VideoSessionActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return originalSessions;
    }

//    private List<VideoCallSession> readVideoSession(String response) {
//        List<VideoCallSession> originalSessions = new ArrayList<>();
//
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getInt("result") == 0) {
//                JSONObject dataObject = jsonObject.getJSONObject("data");
//
//
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
//                Toast.makeText(VideoSessionActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return originalSessions;
//    }

    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

//    class AppointmentParams extends SoapBasicParams {
//
//        private String locationID;
//        private String doctorID;
//        private String start;
//        private String patientID;
//
//        AppointmentParams(Date time) {
//            locationID = AppConfig.LOCATION_VIDEO_ID;
//            doctorID = expert.getId();
//            start = TimeFormatter.millisecondsToString(time.getTime(), "yyyy-MM-dd HH:mm:ss");
//            patientID = "";
//        }
//
//        public String getSearchParams() {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("user_id", user_id);
//                jsonObject.put("locationID", locationID);
//                jsonObject.put("doctorID", doctorID);
//                jsonObject.put("start", start);
//                jsonObject.put("patientID", patientID);
//                jsonObject.put("token_key", token_key);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return jsonObject.toString();
//        }
//    }

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

            PrefManager pref = new PrefManager(VideoSessionActivity.this);
            user_id = pref.getLoginUser().get("uid");
            Log.d("user_id----------", user_id);
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("doctorID", doctorId);
                jsonObject.put("locationID", locationId);
                jsonObject.put("start", date);
                jsonObject.put("token_key", token_key);
                jsonObject.put("free_appointment", isDoctorPaymentFree);
                jsonObject.put("appointment_source", appointment_source);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    class TimeSlotsParams extends SoapBasicParams {

        private String locationID;
        private String doctorID;
        private String start;
        private String date;
        private String end;

        TimeSlotsParams(VideoCallSession session) {

            locationID = AppConfig.LOCATION_VIDEO_ID;
            doctorID = expert.getId();
            start = TimeFormatter.millisecondsToString(session.getStart().getTime(), "yyyy-MM-dd HH:mm:ss");
            date = TimeFormatter.millisecondsToString(session.getStart().getTime(), "yyyy-MM-dd HH:mm:ss");
            end = TimeFormatter.millisecondsToString(session.getEnd().getTime(), "yyyy-MM-dd HH:mm:ss");
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();

            PrefManager pref = new PrefManager(VideoSessionActivity.this);
            user_id = pref.getLoginUser().get("uid");
            Log.d("user_id----------", user_id);

            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("doctorID", doctorID);
                jsonObject.put("locationID", locationID);
                jsonObject.put("date", date);
                jsonObject.put("start", start);
                jsonObject.put("end", end);
                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    public class VideoCallTime {
        private Calendar calendar;
        private boolean isEnabled;

        VideoCallTime(Calendar calendar, boolean isEnabled) {
            this.calendar = calendar;
            this.isEnabled = isEnabled;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }
    }

    public class VideoCallSlot {
        private Calendar start;
        private Calendar end;
        private boolean isEnabled;

        public Calendar getStart() {
            return start;
        }

        public void setStart(Calendar start) {
            this.start = start;
        }

        public Calendar getEnd() {
            return end;
        }

        public void setEnd(Calendar end) {
            this.end = end;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }
    }
}
