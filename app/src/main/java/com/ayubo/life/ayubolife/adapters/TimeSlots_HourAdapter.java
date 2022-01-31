package com.ayubo.life.ayubolife.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;

import com.ayubo.life.ayubolife.model.DoctorAvailablesObj;
import com.ayubo.life.ayubolife.model.TimeSlotObj;

import java.util.ArrayList;

public class TimeSlots_HourAdapter extends ArrayAdapter<TimeSlotObj> implements View.OnClickListener{



    private ArrayList<TimeSlotObj> dataSet;
    Context mContext;


    String userid_ExistingUser;


    ArrayList<TimeSlotObj> songsList;
    String consultant_id;
    String expert_time;
    String expert_date;
    ProgressDialog prgDialog;


    // View lookup cache
    private static class ViewHolder {
        TextView txt_description;
        TextView txtTime;
        TextView txtAvailbility;
        Button btn_booknow;
        ImageView info;
        LinearLayout cell_background_view;
    }



    public TimeSlots_HourAdapter(ArrayList<TimeSlotObj> data, Context context) {
        super(context, R.layout.timeslot_raw, data);
        this.dataSet = data;
        this.mContext=context;


    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TimeSlotObj dataModel=(TimeSlotObj)object;


//     String statu=dataModel.getAppintment();
//        String status=null;
//        if(statu.equals("0")){
//            status="Available";
//        }
//        if(statu.equals("0")){
//            status="Block";
//        }



    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimeSlots_HourAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        TimeSlotObj dataModel = getItem(position);
        final View result;
        String nam=dataModel.getName();
        String anam=dataModel.getAppintment();

        if (convertView == null) {


            viewHolder = new TimeSlots_HourAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.timeslot_hour_raw, parent, false);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.lbl_time);
             viewHolder.txtAvailbility = (TextView) convertView.findViewById(R.id.lbl_name);
         //   viewHolder.txt_description = (TextView) convertView.findViewById(R.id.txt_description);

            viewHolder.cell_background_view = (LinearLayout) convertView.findViewById(R.id.cell_background_view);
        //    viewHolder.cell_background_view.setOnClickListener(this);
//            viewHolder.txtType = (TextView) convertView.findViewById(R.id.txt_center9);
//            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.txt_category9);
//            viewHolder.btn_booknow = (Button) convertView.findViewById(R.id.btn_booknow);

            //  viewHolder.info = (ImageView) convertView.findViewById(R.id.list_image);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeSlots_HourAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }




        viewHolder.txtTime.setText(dataModel.getTime());
         nam=dataModel.getName();

       //viewHolder.txtAvailbility.setText(nam);


        if(nam.equals("0")){
            viewHolder.cell_background_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
           viewHolder.txtAvailbility.setText("   Unavailable");

        }else{
            viewHolder.cell_background_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.appintment_hour_color));
            viewHolder.txtAvailbility.setText("   Available");
        }


        // viewHolder.txtType.setText("cccccccccc");
        // viewHolder.txtVersion.setText("cssssssssssss");
        // viewHolder.btn_booknow.setOnClickListener(this);
        // viewHolder.btn_booknow.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }





}
