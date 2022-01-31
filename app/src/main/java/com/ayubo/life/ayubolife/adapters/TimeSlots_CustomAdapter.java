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

import com.ayubo.life.ayubolife.R;

import com.ayubo.life.ayubolife.model.TimeSlotObj;

import java.util.ArrayList;

public class TimeSlots_CustomAdapter extends ArrayAdapter<TimeSlotObj> implements View.OnClickListener{



private ArrayList<TimeSlotObj> dataSet;
        Context mContext;


        String userid_ExistingUser;

        //CustomAdapter_No adapter;
        ArrayList<TimeSlotObj> songsList;
        String consultant_id;
        String expert_time;
        String expert_date;
        ProgressDialog prgDialog;


// View lookup cache
private static class ViewHolder {
    TextView txt_description;
    TextView txtTime;
    TextView txtVersion;
    Button btn_booknow;
    ImageView info;
    LinearLayout colorview;
}



    public TimeSlots_CustomAdapter(ArrayList<TimeSlotObj> data, Context context) {
        super(context, R.layout.timeslot_raw, data);
        this.dataSet = data;
        this.mContext=context;


    }


    @Override
    public void onClick(View v) {


    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimeSlots_CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        TimeSlotObj dataModel = getItem(position);
        final View result;

        if (convertView == null) {


            viewHolder = new TimeSlots_CustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.timeslot_hour_raw, parent, false);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.lbl_time);
          //  viewHolder.txtTime = (TextView) convertView.findViewById(R.id.lbl_time);
            viewHolder.txt_description = (TextView) convertView.findViewById(R.id.txt_description);

            viewHolder.colorview = (LinearLayout) convertView.findViewById(R.id.colorview);

//            viewHolder.txtType = (TextView) convertView.findViewById(R.id.txt_center9);
//            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.txt_category9);
//            viewHolder.btn_booknow = (Button) convertView.findViewById(R.id.btn_booknow);

          //  viewHolder.info = (ImageView) convertView.findViewById(R.id.list_image);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeSlots_CustomAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }




        viewHolder.txtTime.setText(dataModel.getTime());
        String sss=dataModel.getName();
        viewHolder.txt_description.setText(sss);

        String type=dataModel.getAppintment();
        if(type.equals("0")){
            viewHolder.colorview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_green));
        }
        if(type.equals("1")){
            viewHolder.colorview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_login));
        }
        if(type.equals("2")){
            viewHolder.colorview.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

       // viewHolder.txtType.setText("cccccccccc");
       // viewHolder.txtVersion.setText("cssssssssssss");
       // viewHolder.btn_booknow.setOnClickListener(this);
       // viewHolder.btn_booknow.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }





}
