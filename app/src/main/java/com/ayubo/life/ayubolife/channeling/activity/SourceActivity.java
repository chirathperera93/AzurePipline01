package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.adapter.ChannelDoctorAdapter;
import com.ayubo.life.ayubolife.channeling.model.Appointment;
import com.ayubo.life.ayubolife.channeling.model.ChannelDoctor;
import com.ayubo.life.ayubolife.channeling.model.Session;

public class SourceActivity extends AppCompatActivity {
    public static final String EXTRA_DOCTOR_DETAILS = "doctor_details";
    //instances
    public Appointment appointment;
    ChannelDoctor doctor;
//    //primary data
//    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        readData(getIntent());
//        initView();
        setRecyclerView();

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

    }

    private void readData(Intent intent) {
        if (intent.getExtras() != null)
            appointment = (Appointment) intent.getExtras().getSerializable(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT);
        doctor = (ChannelDoctor) getIntent().getExtras().getSerializable(EXTRA_DOCTOR_DETAILS);
    }

    private void setRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.recycler_source);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Session.Info> sources = Arrays.asList(appointment.getSession().getBooking_info());
        ChannelDoctorAdapter adapter = new ChannelDoctorAdapter(this, new ArrayList<Object>(sources),
                ChannelDoctorAdapter.VIEW_TYPE_SOURCE);
        adapter.setOnScheduleClickListener(new ChannelDoctorAdapter.OnChannelClickListener() {
            @Override
            public void onChannelClicked(Object channelDoctor) {

                Session.Info source = (Session.Info) channelDoctor;
                Intent intent = new Intent(SourceActivity.this, UserDetailsActivity.class);
                appointment.setSource(source.getFrom());
                intent.putExtra(UserDetailsActivity.EXTRA_APPOINTMENT_OBJECT, appointment);
                startActivity(intent);
                finish();

            }
        });
        recyclerView.setAdapter(adapter);
    }

}
