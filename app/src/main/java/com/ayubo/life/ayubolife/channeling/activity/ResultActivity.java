package com.ayubo.life.ayubolife.channeling.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.channeling.model.Booking;
import com.ayubo.life.ayubolife.channeling.model.VideoBooking;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;

public class ResultActivity extends AppCompatActivity {

    //constants
    public static final String EXTRA_BOOKING_OBJECT = "booking_object";
    public static final String EXTRA_VIDEO_BOOKING_OBJECT = "video_booking_object";

    //primary data
    private boolean isVideoCall = false;

    //views
    private View doctorView;
    private View dateView;
    private View timeView;
    private View appointmentView;
    private View locationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();
        readData(getIntent());
    }

    private void readData(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_BOOKING_OBJECT))
            setData((Booking) intent.getExtras().getSerializable(EXTRA_BOOKING_OBJECT));
        else if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_VIDEO_BOOKING_OBJECT))
            setData((VideoBooking) intent.getExtras().getSerializable(EXTRA_VIDEO_BOOKING_OBJECT));
        else
            finish();
    }

    private void initViews() {
        doctorView = findViewById(R.id.layout_result_doctor);
        dateView = findViewById(R.id.layout_result_date);
        timeView = findViewById(R.id.layout_result_time);
        appointmentView = findViewById(R.id.layout_result_appointment);
        locationView = findViewById(R.id.layout_result_location);

        findViewById(R.id.txt_result_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if(isVideoCall)
                    intent = new Intent(ResultActivity.this, VideoRequestActivity.class);
                else
                    intent = new Intent(ResultActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setData(Booking booking) {
        setButton(doctorView, getString(R.string.doctor_result), booking.getAppointment().getDoctor_name());
        setButton(dateView, getString(R.string.date_result), booking.getBookingSession().getDate());
        setButton(timeView, getString(R.string.time_result), booking.getBookingSession().getTime());
        setButton(appointmentView, getString(R.string.appointment_result), String.valueOf(booking.getAppointment().getAppointment_no()));
        setButton(locationView, getString(R.string.location_result), booking.getAppointment().getHospital_name());
        isVideoCall = false;
    }

    private void setData(VideoBooking booking) {
        setButton(doctorView, getString(R.string.doctor_result), String.format("%s %s", booking.getExpert().
                getTitle(), booking.getExpert().getName()));
        Date date = booking.getStartTime();
        setButton(dateView, getString(R.string.date_result), TimeFormatter.millisecondsToString(date.getTime(), "EEEE, MMM dd, yyyy"));
        setButton(timeView, getString(R.string.time_result), TimeFormatter.millisecondsToString(date.getTime(), TimeFormatter.TIME_FORMAT));
        appointmentView.setVisibility(View.GONE);
        locationView.setVisibility(View.GONE);
        isVideoCall = true;
    }

    private void setButton(View view, String title, String value) {
        TextView txtTitle = view.findViewById(R.id.txt_tm_row_title);
        TextView txtValue = view.findViewById(R.id.txt_tm_row_value);

        txtTitle.setText(title);
        txtValue.setText(value);
    }
}
