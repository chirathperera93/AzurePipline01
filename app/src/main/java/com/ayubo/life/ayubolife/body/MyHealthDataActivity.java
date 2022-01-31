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
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.WaterActivity;
import com.ayubo.life.ayubolife.health.Health_TracksFragment;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.rest.ApiClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyHealthDataActivity extends AppCompatActivity {
    PrefManager pref;
    String userid_ExistingUser;ListView health_track_list;

    CustomListAdapterLite adapter;
ImageButton btn_backImgBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health_data);

        pref = new PrefManager(this);
        userid_ExistingUser=pref.getLoginUser().get("uid");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        health_track_list=(ListView)findViewById(R.id.health_track_list);
        HashMap<String, String> song = new HashMap<String, String>();

        ArrayList<SImpleListString> data=new ArrayList<SImpleListString>();

        Bitmap bitmap1= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.water_icn);
        data.add(new SImpleListString(bitmap1,"WATER INTAKE"));

        Bitmap bitmap2= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.vital_icn);
        data.add(new SImpleListString(bitmap2,"VITALS"));

        Bitmap bitmap3= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.sugar_fasting_icn);
        data.add(new SImpleListString(bitmap3,"FASTING BLOOD SUGAR"));

        Bitmap bitmap4= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.sugar_random_icn);
        data.add(new SImpleListString(bitmap4,"RANDOM BLOOD SUGAR"));

        Bitmap bitmap5= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.hbac1);
        data.add(new SImpleListString(bitmap5,"HBA1C"));

        Bitmap bitmap6= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.presure_icn);
        data.add(new SImpleListString(bitmap6,"BLOOD PRESSURE"));

        Bitmap bitmap7= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.cholesterol_icn);
        data.add(new SImpleListString(bitmap7,"CHOLESTEROL"));

        Bitmap bitmap8= BitmapFactory.decodeResource(MyHealthDataActivity.this.getResources(), R.drawable.temp_icn);
        data.add(new SImpleListString(bitmap8,"TEMPERATURE"));


        adapter=new CustomListAdapterLite(MyHealthDataActivity.this, data);

        health_track_list.setAdapter(adapter);
        health_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position==0){
                    Intent in = new Intent(MyHealthDataActivity.this, WaterActivity.class);
                    startActivity(in);
                }
                if(position==1){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=vitals&form_name=VITALS";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);

                }
                if(position==2){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=blood_sugar&form_name=FASTING%20BLOOD%20SUGAR";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==3){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=random_blood_sugar&form_name=RANDOM%20BLOOD%20SUGAR";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==4){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=hba1c&form_name=HBA1C";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==5){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=blood_pressure&form_name=BLOOD%20PRESSURE";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==6){
                    String url=ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=lipid_profile&form_name=CHOLESTEROL";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
                if(position==7){
                    String url= ApiClient.BASE_URL_entypoint+"mobile_health_tracker&form_type=temperature&form_name=TEMPERATURE#addDataView";
                    Intent intent = new Intent(MyHealthDataActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }



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
