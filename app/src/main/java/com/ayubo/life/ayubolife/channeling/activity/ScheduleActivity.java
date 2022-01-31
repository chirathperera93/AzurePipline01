package com.ayubo.life.ayubolife.channeling.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.adapter.ChannelDoctorAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.Appointment;
import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.channeling.model.DownloadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.Hospital;
import com.ayubo.life.ayubolife.channeling.model.Session;
import com.ayubo.life.ayubolife.channeling.model.SessionParent;
import com.ayubo.life.ayubolife.channeling.model.SessionSearchParams;
import com.ayubo.life.ayubolife.channeling.util.AppHandler;
import com.ayubo.life.ayubolife.channeling.util.DatePickerFragment;
import com.ayubo.life.ayubolife.channeling.util.DownloadData;
import com.ayubo.life.ayubolife.channeling.util.DownloadManager;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

public class ScheduleActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_LOCATION_LIST = "location_lists";
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";
    public static final String EXTRA_LOCATION_DETAILS = "location_details";

    //instances
    private List<Session> sessions;
    private SessionParent parent;
    private ChannelDoctor doctor;
    private List<Hospital> locations;

    //primary data
    private boolean isInitial = true;
    LottieAnimationView imgFavorite;
    //views
    private ProgressBar progressBar;
    private TextView txtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton btn_back_Button = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_back_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (getIntent().getExtras() != null) {
            doctor = (ChannelDoctor) getIntent().getExtras().getSerializable(EXTRA_DOCTOR_DETAILS);
            Hospital hospital = (Hospital) getIntent().getExtras().getSerializable(EXTRA_LOCATION_DETAILS);
            locations = (List<Hospital>) getIntent().getExtras().getSerializable(EXTRA_LOCATION_LIST);
            if (doctor != null) {
                setView(hospital, doctor.getDoctor_name(), doctor.getSpecialization(), doctor.getDoc_image());
            }
        }
    }

    private void setView(final Hospital hospital, String name, String specialty, String imgUrl) {
        progressBar = findViewById(R.id.progress_loading_schedule);

        setBasicView(name, specialty, "", imgUrl);

        ImageView imgPlus = findViewById(R.id.img_calendar_schedule);
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setMinDate(Calendar.getInstance());
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
//                        calendar.set(Calendar.HOUR, 0);
//                        calendar.set(Calendar.MINUTE, 0);

                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        filterByDate(calendar.getTimeInMillis());
                    }
                });
            }
        });

        Spinner spnUsers = findViewById(R.id.spn_spinner_custom_view);
        spnUsers.setAdapter(new SpinnerAdapter(R.layout.custom_location_spinner_row, locations));
        spnUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isInitial) {
                    isInitial = false;
                    getData(doctor, hospital);
                } else
                    getData(doctor, locations.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnUsers.setSelection(getPosition(hospital));

        txtNoData = findViewById(R.id.txt_schedule_no_data);

        imgFavorite = findViewById(R.id.img_favorite_doctor);
        //     img_favourite_icon
        // IF HEMAS APP .........
        if(Constants.type == Constants.Type.HEMAS){

            if(doctor!=null){
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

        }else{
            imgFavorite.setVisibility(View.INVISIBLE);
        }

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
    private void addFavorites() {

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_ADD_FAVORITE, new FavoriteParams(
                        String.valueOf(doctor.getDoctor_code()), String.valueOf(doctor.getSpecialization_id())).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(ScheduleActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.added_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(ScheduleActivity.this, R.string.failed_add_favourite, Toast.LENGTH_LONG).show();
                        startCheckAnimation();
                    }
                }).execute();
    }

    private void removeFavorites() {


        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_REMOVE_FAVORITE, new FavoriteParams(
                        String.valueOf(doctor.getDoctor_code()), String.valueOf(doctor.getSpecialization_id())).getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        Toast.makeText(ScheduleActivity.this, String.format(Locale.getDefault(),
                                getString(R.string.remove_favourite), doctor.getDoctor_name()), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        Toast.makeText(ScheduleActivity.this, R.string.failed_remove_favourite, Toast.LENGTH_LONG).show();
                        // startCheckAnimation();
                    }
                }).execute();
    }

    class FavoriteParams extends SoapBasicParams {
        private String doctorId;
        private String specialization_id;
//        private String description;

        FavoriteParams(String docId, String specId) {

            this.doctorId = docId;
//            this.description = desc;
            this.specialization_id = specId;
        }

        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();

            Context c=null;
            c=  Ram.getMycontext();
            if(c!=null){

                PrefManager pref = new PrefManager(c);
                user_id = pref.getLoginUser().get("uid");
                Log.d("getSearchParams","=================================Context not null");
            }else{
                Log.d("getSearchParams","==============================Context  null");
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

    private int getPosition(Hospital hospital) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getHospital_id().equals(hospital.getHospital_id()))
                return i;
        }
        return 0;
    }

    private void setBasicView(String name, String specialty, String note, String imgUrl) {
        ImageView imageView = findViewById(R.id.img_doc_doctor_info);
        try {
            Glide.with(this).load(imgUrl).into(imageView);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String sstrDocName= Utility.upperCaseWords(name);

        ((TextView) findViewById(R.id.txt_doctor_name_doctor_info)).setText(sstrDocName);
        ((TextView) findViewById(R.id.txt_specialty_doctor_info)).setText(specialty);
        if (note == null || note.equals(""))
            ((TextView) findViewById(R.id.txt_note_doctor_info)).setText(R.string.special_note);
        else
            ((TextView) findViewById(R.id.txt_note_doctor_info)).setText(String.format(Locale.getDefault(),
                    "%session %session", getString(R.string.special_note), note));
    }

    private void readSessions(final List<Session> dataArray) {

        progressBar.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sessions = new ArrayList<>();

                if (dataArray != null && dataArray.size() > 0) {
                    parentThread:
                    for (Session session : dataArray) {
                        Date sessionDate = TimeFormatter.stringToDate(session.getShow_date()
                                + " " + session.getTime(), "yyyy-MM-dd HH:mm");
                        for (int i = 0; i < sessions.size(); i++) {
                            Date temp = TimeFormatter.stringToDate(sessions.get(i).getShow_date()
                                    + " " + sessions.get(i).getTime(), "yyyy-MM-dd HH:mm");
                            if (sessionDate.compareTo(temp) < 0) {
                                sessions.add(i, session);
                                continue parentThread;
                            }
                        }
                        sessions.add(session);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        setRecyclerView();
                    }
                });
            }
        }).run();
    }

    private List<Session> readSessions(String respond, int docId) {
        List<Session> dataArray = null;
        try {
            JSONObject jsonObject = new JSONObject(respond);

            String ssss=jsonObject.getJSONObject("data").getJSONObject(String.valueOf(docId)).toString();

            parent = new Gson().fromJson(jsonObject.getJSONObject("data").getJSONObject(String.valueOf(docId))
                    .toString(), SessionParent.class);

            //  setBasicView(parent.getDoctor_name(), parent.getSpecialization_name(), parent.getSpecial_notes(), parent.getDoc_image());

            if (parent.getSessions() != null)
                dataArray = new ArrayList<>(parent.getSessions().values());
            else
                dataArray = new ArrayList<>();

            if (parent.getSessions() == null || parent.getSessions().size() == 0)
                txtNoData.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    private void setRecyclerView() {
        RecyclerView recycler_schedules = findViewById(R.id.recycler_schedule);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_schedules.setLayoutManager(linearLayoutManager);
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(sessions), ChannelDoctorAdapter.VIEW_TYPE_SESSION);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {
                Intent intent;
                Appointment appointment = new Appointment();
                appointment.setSession((Session) channelDoctor);
                appointment.setSessionParent(parent);
                String []  sourceList= appointment.getSessionParent().getSource();

                if(Constants.type == Constants.Type.AYUBO){
                    intent = new Intent(ScheduleActivity.this, SourceActivity.class);
                    intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                    intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                    startActivity(intent);
                }
               else if(Constants.type == Constants.Type.SHESHELLS){
                    intent = new Intent(ScheduleActivity.this, SourceActivity.class);
                    intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                    intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                    startActivity(intent);
                }
                else{
                    if(sourceList.length==1){
                        //  legacy
                        appointment.setSource("legacy");
                        Intent inten = new Intent(ScheduleActivity.this, UserDetailsActivity.class);
                        inten.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                        startActivity(inten);
                        finish();
                    }else{
                        intent = new Intent(ScheduleActivity.this, SourceActivity.class);
                        intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                        intent.putExtra(ScheduleActivity.EXTRA_DOCTOR_DETAILS, doctor);
                        startActivity(intent);
                    }

                }

            }

//            @Override
//            public void onGetMoreSessionsClicked(String location) {
//            }
        });
        recycler_schedules.setAdapter(adapter);
    }



    Long DateCase(Session session){
        String ori=session.getShow_date();

        String oris= ori.replace("-","/");

        Long timedata=null;
        String s = oris+"/"+session.getTime()+":00";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");

        try
        {
            Date date = simpleDateFormat.parse(s);
            timedata=date.getTime();
            System.out.println("date : "+simpleDateFormat.format(date));
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }

        return timedata;
    }


    private void filterByDate(Long date) {

        List<Session> channelDoctors = ((App) getApplication()).getChannelDoctors();
        List<Session> filteredList = new ArrayList<>();

        if (sessions != null) {

            for (Session session : sessions) {
                Long sessionDate=   DateCase(session);


                if (sessionDate > date) {
                    filteredList.add(session);
                }else{
                }
            }
        }

        sessions.clear();
        sessions.addAll(filteredList);
        setRecyclerView();

    }

    class SpinnerAdapter extends ArrayAdapter<Hospital> {

        private LayoutInflater mInflater;
        private List<Hospital> items;
        private int mRes;

        private SpinnerAdapter(int resource, @NonNull List<Hospital> objects) {
            super(ScheduleActivity.this, resource, objects);
            this.mInflater = LayoutInflater.from(ScheduleActivity.this);
            this.items = objects;
            this.mRes = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return createItemView(position, convertView, parent);
        }

        private View createItemView(int position, View convertView, ViewGroup parent) {
            final View view = mInflater.inflate(mRes, parent, false);

            ImageView imgLocation = view.findViewById(R.id.img_location_spinner_row);
            TextView txtLocation = view.findViewById(R.id.txt_location_spinner_row);

            txtLocation.setText(items.get(position).getHospital_name());
            Glide.with(ScheduleActivity.this).load(items.get(position).getHospital_image()).into(imgLocation);

            return view;
        }
    }

    private void getData(final ChannelDoctor doctor, Hospital location) {

        progressBar.setVisibility(View.VISIBLE);
        txtNoData.setVisibility(View.GONE);

        if (sessions != null) {
            sessions.clear();
            setRecyclerView();
        }

        SessionSearchParams sessionParams = new SessionSearchParams();
        sessionParams.setDoctorId(String.valueOf(doctor.getDoctor_code()));
        sessionParams.setSpecializationId(String.valueOf(doctor.getSpecialization_id()));
        sessionParams.setLocationId(location.getHospital_id());
        sessionParams.setDirect(location.getDirect());
        sessionParams.setSource(location.getSource());

        PrefManager pref = new PrefManager(ScheduleActivity.this);
        sessionParams.setUser_id(pref.getLoginUser().get("uid"));


//        sessionParams.setDoctorId("966");
//        sessionParams.setSpecializationId("32");
//        sessionParams.setLocationId("150");
//        sessionParams.setDirect("");
//        sessionParams.setSource(new String[]{"echannelling"});

        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_GET_CHANNELLING_SESSION, sessionParams.getSearchParams())).
                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
                    @Override
                    public void onDownloadSuccess(String response, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        List<Session> sessions = readSessions(response, doctor.getDoctor_code());
                        if (sessions != null && sessions.size() > 0) {
                            ((App) getApplication()).setChannelDoctors(sessions);
                            readSessions(sessions);
                        }
                    }

                    @Override
                    public void onDownloadFailed(String errorMessage, int what, int code) {
                        progressBar.setVisibility(View.GONE);
                        showErrorView(getString(R.string.server_error));
                    }
                }).execute();
    }

    private void showErrorView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
