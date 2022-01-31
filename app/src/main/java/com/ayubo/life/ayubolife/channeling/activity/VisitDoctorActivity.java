package com.ayubo.life.ayubolife.channeling.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.channeling.model.AyuboSearchParameters;
import com.ayubo.life.ayubolife.channeling.model.DocSearchParameters;
import com.ayubo.life.ayubolife.channeling.util.DatePickerFragment;
import com.ayubo.life.ayubolife.channeling.view.SearchDoctorAction;
import com.ayubo.life.ayubolife.channeling.view.SearchLocationAction;
import com.ayubo.life.ayubolife.channeling.view.SearchSpecialtyAction;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;
import com.ayubo.life.ayubolife.channeling.view.SelectDoctorAction;

public class VisitDoctorActivity extends AppCompatActivity {

    //constants
    public static final int SEARCH_DOCTOR = 101;
    public static final int SEARCH_SPECIALTY = 102;
    public static final int SEARCH_HOSPITAL = 103;

    //instances
    private AyuboSearchParameters searchParam;

    //views
    private View view_doctor;
    private View view_specialty;
    private View view_location;
    @SuppressLint("StaticFieldLeak")
    private static View view_date;
    private View errorView;
//    private TextView txtErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_doctor);

        setView();
    }

    private void setView() {
        TextView btnFindDoctor = findViewById(R.id.btn_find_doctor_visit_doctor);
        view_doctor = findViewById(R.id.layout_doctor_button);
        view_specialty = findViewById(R.id.layout_specialty_button);
        view_location = findViewById(R.id.layout_location_button);
        view_date = findViewById(R.id.layout_date_button);
//        progressBar = findViewById(R.id.progress_loading_availability);
        errorView = findViewById(R.id.layout_error);
//        txtErrorMessage = errorView.findViewById(R.id.txt_message_error);

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
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

        errorView.findViewById(R.id.btn_try_again_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorView.setVisibility(View.GONE);
            }
        });

        btnFindDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAvailability();
            }
        });

        view_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(SEARCH_DOCTOR);
            }
        });

        view_specialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(SEARCH_SPECIALTY);
            }
        });

        view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchActivity(SEARCH_HOSPITAL);
            }
        });

        view_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), getString(R.string.appointment_date));
                newFragment.setMinDate(Calendar.getInstance());
                newFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelected() {
                    @Override
                    public void onSelected(Calendar calendar) {
                        ((TextView) view_date.findViewById(R.id.txt_sub_name_button)).setText(getTime(calendar));
                    }
                });
            }
        });

        //set initial Details
        setDetails();
    }

    private void setDetails() {
        setDoctor(getString(R.string.search_doctor_expert));
        setSpecialty(getString(R.string.any));
        setLocation(getString(R.string.any));
        setDate(getString(R.string.any));
//        if (searchParam == null)
//            searchParam = new AyuboSearchParameters();
//        searchParam.setDate(TimeFormatter.millisecondsToString(Calendar.getInstance().getTimeInMillis(),
//                TimeFormatter.DATE_FORMAT_SHORT));
    }

    protected void setDoctor(String name) {
        setButton(view_doctor, getString(R.string.visit_a_doctor_two), name, R.drawable.search_doctor);
    }

    protected void setSpecialty(String specialty) {
        setButton(view_specialty, getString(R.string.specialty), specialty, R.drawable.search_speciality);
    }

    protected void setLocation(String location) {
        setButton(view_location, getString(R.string.hospital), location, R.drawable.search_location);
    }

    private void setDate(String message) {
        setButton(view_date, getString(R.string.date), message, R.drawable.search_date);
    }

    private void setButton(View view, String name, String subName, int drawable) {
        ((ImageView) view.findViewById(R.id.img_icon_button)).setImageDrawable(ContextCompat.getDrawable(this, drawable));
        ((TextView) view.findViewById(R.id.txt_name_button)).setText(name);
        ((TextView) view.findViewById(R.id.txt_sub_name_button)).setText(subName);
    }

    private static String getTime(Calendar calendar) {
        return TimeFormatter.millisecondsToString(calendar.getTimeInMillis(), TimeFormatter.DATE_FORMAT_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null && data.getExtras().containsKey(SearchActivity.EXTRA_SEARCH_VALUE)) {
            String id;
            if (data.getExtras().containsKey(SearchActivity.EXTRA_SEARCH_ID))
                id = String.valueOf(data.getExtras().getInt(SearchActivity.EXTRA_SEARCH_ID));
            else
                id = "";

            String value = data.getExtras().getString(SearchActivity.EXTRA_SEARCH_VALUE);

            switch (requestCode) {
                case SEARCH_DOCTOR:
                    setDoctor(value);
                    searchParam.setDoc_id(String.valueOf(id));
                    break;
                case SEARCH_HOSPITAL:
                    setLocation(value);
                    searchParam.setHospital_id(String.valueOf(id));
                    break;
                case SEARCH_SPECIALTY:
                    setSpecialty(value);
                    searchParam.setSpecialization_id(String.valueOf(id));
                    break;
            }
        }
    }

    private void startSearchActivity(int searchCode) {
        PrefManager pref = new PrefManager(VisitDoctorActivity.this);
        String user_id = pref.getLoginUser().get("uid");


        if (searchParam == null)
            searchParam = new AyuboSearchParameters();

        searchParam.setUser_id(user_id);

        Intent intent = new Intent(VisitDoctorActivity.this, SearchActivity.class);

        switch (searchCode) {
            case SEARCH_DOCTOR:
                searchParam.setDoc_id("");
                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SearchDoctorAction(searchParam));
                break;
            case SEARCH_HOSPITAL:
                searchParam.setHospital_id("");
                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SearchLocationAction(searchParam));
                break;
            case VisitDoctorActivity.SEARCH_SPECIALTY:
                searchParam.setSpecialization_id("");
                intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SearchSpecialtyAction(searchParam));
                break;
        }
        startActivityForResult(intent, searchCode);
    }

//    private boolean validate() {
//        return searchParam != null && !searchParam.getDoc_id().equals("");
//    }

    private void checkAvailability() {

        if (searchParam != null && (!searchParam.getDoc_id().equals("") || !searchParam.getHospital_id().equals("") ||
                !searchParam.getSpecialization_id().equals(""))) {

            final DocSearchParameters parameters = new DocSearchParameters();
            parameters.setDoctorId(searchParam != null ? searchParam.getDoc_id() : "");
            parameters.setLocationId(searchParam != null ? searchParam.getHospital_id() : "");
            parameters.setSpecializationId(searchParam != null ? searchParam.getSpecialization_id() : "");

            String date = ((TextView) view_date.findViewById(R.id.txt_sub_name_button)).getText().toString();
            if (date.equals(getString(R.string.any)))
//            parameters.setDate(TimeFormatter.millisecondsToString(System.currentTimeMillis(), TimeFormatter.DATE_FORMAT_VIDEO));
                parameters.setDate("");
            else
                parameters.setDate(date);

            startDoctorsActivity(parameters);
        } else
            Toast.makeText(this, R.string.no_search_criteria, Toast.LENGTH_LONG).show();
    }

    private void startDoctorsActivity(DocSearchParameters params) {

        PrefManager pref = new PrefManager(VisitDoctorActivity.this);
        String user_id = pref.getLoginUser().get("uid");
        params.setUser_id(user_id);

        Intent intent = new Intent(VisitDoctorActivity.this, SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_SEARCH_OBJECT, new SelectDoctorAction(params));
        intent.putExtra(SearchActivity.EXTRA_TO_DATE, ((TextView) view_date.findViewById(R.id.txt_sub_name_button))
                .getText().toString());
        startActivity(intent);
    }
}
