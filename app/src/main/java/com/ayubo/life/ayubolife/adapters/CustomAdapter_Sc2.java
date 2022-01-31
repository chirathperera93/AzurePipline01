package com.ayubo.life.ayubolife.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DB4String;
import com.ayubo.life.ayubolife.model.DBString;

import java.util.ArrayList;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CustomAdapter_Sc2 extends ArrayAdapter<DB4String> implements View.OnClickListener{
    boolean isNorecordsFound=false;
    boolean isServiceSuccess=false;
    boolean isDataAvailable=false;

    private ArrayList<DB4String> dataSet;
    private ArrayList<DBString> datesListArray;
    String doctorID,intervalStart,intervalEnd,userid,data_str1;
    String consultant_id,location;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txt_docfee;
        TextView txt_timeslot;
        TextView txt_center_name;
        Button btn_booknow;
        ImageView img_contype;
    }



    public CustomAdapter_Sc2(ArrayList<DB4String> data, Context context) {
        super(context, R.layout.raw_search_step2, data);
        this.dataSet = data;
        this.mContext=context;
    }
    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
      //  DoctorAvailablesObj dataModel=(DoctorAvailablesObj)object;
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DB4String dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.raw_search_step2, parent, false);

            viewHolder.txt_docfee = (TextView) convertView.findViewById(R.id.txt_doctor_location);
            viewHolder.txt_timeslot = (TextView) convertView.findViewById(R.id.txt_timeslot);
           viewHolder.txt_center_name = (TextView) convertView.findViewById(R.id.txt_center_name);
            viewHolder.img_contype = (ImageView) convertView.findViewById(R.id.img_contype);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        String sname= dataModel.getName();
        String[] namesList = sname.split(",");
        System.out.println(sname);
        String location_id = namesList[0];
        String location_name = namesList[1];
        String name3 = namesList[2];
        String timesstart = namesList[3];
        String sometome = namesList[4];
        String doctor_fee = namesList[5];
       String timeslot = namesList[6];

        if(timeslot.equals("")){
            timeslot="No sessions available";
        }
        else if(timeslot.equals("0")){
            timeslot="No sessions available";
        }
        viewHolder.txt_docfee.setText("Consultant Fee Rs "+doctor_fee+"/=");
        viewHolder.txt_timeslot.setText(timeslot);
        viewHolder.txt_center_name.setText(location_name);

        Drawable myIcon =null;
      //  viewHolder.img_contype.setOnClickListener(this);

        if(location_name.equals("Video Chat")){
             myIcon = convertView.getResources().getDrawable( R.drawable.video_large);
        }else {
             myIcon = convertView.getResources().getDrawable(R.drawable.building_large);
        }

        viewHolder.img_contype.setBackground(myIcon);
        // Return the completed view to render on screen
        return convertView;
    }


}
