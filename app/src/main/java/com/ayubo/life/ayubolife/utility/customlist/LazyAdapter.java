package com.ayubo.life.ayubolife.utility.customlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.DoctorAvailablesObj;

import java.util.ArrayList;
import java.util.HashMap;



public class LazyAdapter extends BaseAdapter implements View.OnClickListener{
    int p;
    private Activity activity;
    private ArrayList<DoctorAvailablesObj> data=new ArrayList<DoctorAvailablesObj>();
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<DoctorAvailablesObj> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DoctorAvailablesObj dataModel=(DoctorAvailablesObj)object;
        System.out.println( "========asdsdsdfsdf===== User==id========");

    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.txt_doctorName9); // title
        TextView artist = (TextView)vi.findViewById(R.id.txt_center9); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.txt_category9); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

          p=position;
        DoctorAvailablesObj song = data.get(position);
        String sname= song.getName();
        String lo= song.getLocation();
        String sp= song.getSpecility();



        title.setText(sname);
        artist.setText(lo);
        duration.setText(sp);

       // vi.setOnClickListener((View.OnClickListener) convertView.getContext());
       // imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}