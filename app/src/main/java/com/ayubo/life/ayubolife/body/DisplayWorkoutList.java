package com.ayubo.life.ayubolife.body;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayWorkoutList extends AppCompatActivity {
    ArrayList<SImpleListString> data;
    ListView health_track_list;
    CustomListAdapterLite adapter;
    ImageButton btn_backImgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_workout_list);

        data=new ArrayList<SImpleListString>();
        health_track_list=(ListView)findViewById(R.id.health_track_list_new);


        Bitmap bitmap1= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.run);
        data.add(new SImpleListString(bitmap1,"RUNNING"));

        Bitmap bitmap2= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.walking);
        data.add(new SImpleListString(bitmap2,"WALKING"));

        Bitmap bitmap3= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.hicking);
        data.add(new SImpleListString(bitmap3,"HIKING"));

        Bitmap bitmap4= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.bedmiton);
        data.add(new SImpleListString(bitmap4,"BADMINTON"));

        Bitmap bitmap5= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.cycling);
        data.add(new SImpleListString(bitmap5,"CYCLING"));

        Bitmap bitmap6= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.basketball);
        data.add(new SImpleListString(bitmap6,"BASKETBALL"));

        Bitmap bitmap7= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.swimming);
        data.add(new SImpleListString(bitmap7,"SWIMMING"));

        Bitmap bitmap8= BitmapFactory.decodeResource(DisplayWorkoutList.this.getResources(), R.drawable.golf);
        data.add(new SImpleListString(bitmap8,"GOLF"));

        adapter=new CustomListAdapterLite(DisplayWorkoutList.this, data);

        health_track_list.setAdapter(adapter);
        health_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){

                    String activityId = "1d29596b-9771-3c41-724e-5937c8878a2b";
                    String activityName = "RUNNING";
                    String mets="7";
                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();

                }
                if(position==2){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "HIKING";
                    String mets="6";

                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();
                }
                if(position==3){
                    String activityId = "b5504c46-4109-9724-d368-5948ad558767";
                    String activityName = "BADMINTON";
                    String mets="4";
                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);
                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();
                }
                if(position==4){
                    String activityId = "8ceb0f82-9b73-a12a-225f-59478c7063c1";
                    String activityName = "CYCLING";
                    String mets="8";

                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);

                    startActivity(intent);
                    finish();
                }
                if(position==5){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "BASKETBALL";
                    String mets="6";
                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);
                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();
                }
                if(position==6){
                    String activityId = "e9ec0886-bd3b-786c-0a89-5948adf1c19a";
                    String activityName = "SWIMMING";
                    String mets="6";
                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);

                    startActivity(intent);
                    finish();
                }
//                if(position==6){
//
//                    String url= ApiClient.BASE_URL_entypoint+"mobile_view_program&program=7b650dec-5ba5-9907-3f3e-596327e266d5";
//                    Intent intent = new Intent(DisplayWorkoutList.this, CommonWebViewActivity.class);
//                    intent.putExtra("URL", url);
//                    startActivity(intent);
//                    finish();
//                }
                if(position==1){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "WALKING";
                    String mets="3";

                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();
                }
                if(position==7){
                    String activityId = "8858f0db-36ed-6e9f-fafa-5948af946401";
                    String activityName = "GOLF";
                    String mets="2";

                    Intent intent = new Intent(DisplayWorkoutList.this, StartWorkoutActivity.class);
                    intent.putExtra("activityName", activityName);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("mets", mets);

                    Ram.setActivityName(activityName);
                    Ram.setActivityId(activityId);
                    Ram.setMets(mets);
                    startActivity(intent);
                    finish();
                }


            }
        });



        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class CustomListAdapterLite extends BaseAdapter {
        String myStrings[];

        private Context activity;
        private ArrayList<SImpleListString> data;
        private LayoutInflater inflater=null;
        // public ImageLoader imageLoader;

        public CustomListAdapterLite(Context a,ArrayList<SImpleListString> d) {
            activity = a;
            data=d;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                vi = inflater.inflate(R.layout.health_custome_list_raw, null);

            TextView title = (TextView)vi.findViewById(R.id.title); // title
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

            HashMap<String, String> song = new HashMap<String, String>();

            SImpleListString obj = data.get(position);

            title.setText(obj.getName());
            thumb_image.setImageBitmap(obj.getBitmap());

            return vi;
        }
    }


}
