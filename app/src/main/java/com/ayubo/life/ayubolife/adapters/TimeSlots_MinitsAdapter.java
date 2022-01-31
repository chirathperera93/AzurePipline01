package com.ayubo.life.ayubolife.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;

import com.ayubo.life.ayubolife.model.AppointmentRawObj;
import com.ayubo.life.ayubolife.model.DoctorAvailablesObj;
import com.ayubo.life.ayubolife.model.TimeSlotObj;

import java.util.ArrayList;

public class TimeSlots_MinitsAdapter extends ArrayAdapter<AppointmentRawObj> implements View.OnClickListener {



    private ArrayList<AppointmentRawObj> dataSet;
    Context mContext;


    String userid_ExistingUser;


    ArrayList<TimeSlotObj> songsList;
    String consultant_id;
    String expert_time;
    String expert_date;
    ProgressDialog prgDialog;

    @Override
    public void onClick(View view) {

        int position=(Integer) view.getTag();
        Object object= getItem(position);
        AppointmentRawObj dataModel=(AppointmentRawObj)object;

        if(dataModel.isAppointment().equals("1")){
        }
       else if(dataModel.isAppointment().equals("2")){
        }
        else {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getRootView().getContext());
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layoutView = inflater.inflate(R.layout.confirm_appointment_alert, null, false);
            builder2.setView(layoutView);
            builder2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                    System.out.println("=======eee=======");
                }
            });
            builder2.show();
        }
    }


    // View lookup cache
    private static class ViewHolder {
        TextView lbl_endTime;
        TextView lbl_startTime;
        TextView lbl_appointment;
        Button btn_booknow;
        ImageView btn_delete;
        ImageView info;
        LinearLayout cell_background_view;
    }



    public TimeSlots_MinitsAdapter(ArrayList<AppointmentRawObj> data, Context context) {
        super(context, R.layout.timeslot_raw, data);
        this.dataSet = data;
        this.mContext=context;


    }





    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimeSlots_MinitsAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        AppointmentRawObj dataModel = getItem(position);
        final View result;
        String startTime=dataModel.getStartTime();
        String endTime=dataModel.getEndTime();
        String anam=dataModel.getAppointment_time();

        String appTime=startTime+" "+endTime;
        if (convertView == null) {


            viewHolder = new TimeSlots_MinitsAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.timeslot_minits_raw, parent, false);

            viewHolder.cell_background_view = (LinearLayout) convertView.findViewById(R.id.bg_minits_cell);
            viewHolder.lbl_endTime = (TextView) convertView.findViewById(R.id.lbl_startTime);
            viewHolder.lbl_startTime = (TextView) convertView.findViewById(R.id.lbl_startTime);
            viewHolder.lbl_appointment = (TextView) convertView.findViewById(R.id.lbl_appointment);
            viewHolder.btn_delete = (ImageView) convertView.findViewById(R.id.lbl_bbbb);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (TimeSlots_MinitsAdapter.ViewHolder) convertView.getTag();

            result=convertView;
        }


        viewHolder.lbl_endTime = (TextView) convertView.findViewById(R.id.lbl_endTime);
        viewHolder.lbl_startTime = (TextView) convertView.findViewById(R.id.lbl_startTime);
        viewHolder.lbl_appointment = (TextView) convertView.findViewById(R.id.lbl_appointment);

        viewHolder.lbl_startTime.setText(startTime);
        viewHolder.lbl_endTime.setText(endTime);

        viewHolder.btn_delete.setOnClickListener(this);
        viewHolder.btn_delete.setTag(position);

        viewHolder.cell_background_view.setOnClickListener(this);
        viewHolder.cell_background_view.setTag(position);


        String isAppointment=dataModel.isAppointment();
        if(isAppointment.equals("1")){
            viewHolder.btn_delete.setVisibility(View.VISIBLE);
            //appTime
            viewHolder.lbl_appointment.setText("Appointment");
            viewHolder.lbl_appointment.setTextColor(Color.parseColor("#006622"));
            viewHolder.cell_background_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.appintment_hour_color));
        }
        else if(isAppointment.equals("2")){
            viewHolder.btn_delete.setVisibility(View.GONE);
            viewHolder.lbl_appointment.setText("No sessions available");
            viewHolder.lbl_appointment.setTextColor(Color.parseColor("#ff8000"));
            viewHolder.cell_background_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }else{
            viewHolder.btn_delete.setVisibility(View.GONE);
            viewHolder.lbl_appointment.setText("Available");
            viewHolder.lbl_appointment.setTextColor(Color.parseColor("#ff8000"));
            viewHolder.cell_background_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        return convertView;
    }





}
