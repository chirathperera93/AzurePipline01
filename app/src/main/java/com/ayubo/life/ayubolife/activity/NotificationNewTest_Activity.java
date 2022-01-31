package com.ayubo.life.ayubolife.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.model.NotificationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotificationNewTest_Activity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<NotificationObj> dataList=null;
    private PrefManager pref;
    String userid_ExistingUser,statusFromServiceAPI_db;
    ImageLoader imageLoader;
    ImageButton btn_backImgBtn;
    String notification_id,web_link;
    ArrayList<String> stringdataList=null;
    ListView lv;
    SearchView sv;
    String main_service_data;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_new_test_);

        pref = new PrefManager(NotificationNewTest_Activity.this);
        stringdataList=new  ArrayList<String>();
        context=NotificationNewTest_Activity.this;
        lv=(ListView) findViewById(R.id.listView1);
        sv=(SearchView) findViewById(R.id.searchView1);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.onActionViewExpanded();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NotificationObj obj= dataList.get(position);
                notification_id =obj.getId().toString();
                String readSt= obj.getRead();
                web_link= obj.getLink();
                System.out.println("Read===Status========="+readSt);
                System.out.println("Read===web_link========="+web_link);
                if(notification_id!=null){
                    Intent intent = new Intent(NotificationNewTest_Activity.this, NotificationDetailsViewActivity.class);
                    intent.putExtra("URL", web_link);
                    intent.putExtra("notification_id", notification_id);
                    intent.putExtra("readStatus", readSt);
                    startActivity(intent);
                    // finish();
                }
                System.out.println("Already read============");

            }
        });

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      //  CALLING API AND LOADING LIST VIEW
        String dat=pref.getAllNotificationData();
        if(dat!=null){
            showView(dat);
        }


        getNoti();

    }
    void showView(String dataString) {

        if (dataString.length() > 10) {
            JSONArray myListsAll = null;

            dataList = new ArrayList<NotificationObj>();
            try {
                myListsAll = new JSONArray(dataString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < myListsAll.length(); i++) {

                JSONObject datajsonObj = null;
                try {
                    datajsonObj = (JSONObject) myListsAll.get(i);
                    String id = datajsonObj.optString("id").toString();
                    String title = datajsonObj.optString("Title").toString();
                    String text = datajsonObj.optString("text").toString();
                    // String fullText=title+" : "+text;
                    String icon = datajsonObj.optString("Icon").toString();
                    String datetime = datajsonObj.optString("DateTime").toString();
                    String link = datajsonObj.optString("link").toString();
                    String read = datajsonObj.optString("Read").toString();
                    String type = datajsonObj.optString("Type").toString();
                    stringdataList.add(text);
                    //  players.add(new Player(title,icon));
                    NotificationObj sen = new NotificationObj(id, title, text, icon, datetime, link, read, type);
                    //  String id, title,text, icon, datetime, link, read, type;
                    dataList.add(sen);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (dataList.size() > 0) {
                final AdapterMy adapter = new AdapterMy(context, dataList);
                lv.setAdapter(adapter);

                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String arg0) {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        // TODO Auto-generated method stub

                        adapter.getFilter().filter(query);

                        return false;
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationNewTest_Activity.this);
                LayoutInflater inflat = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflat.inflate(R.layout.alert_no_notifications, null, false);
                builder.setView(layoutView);

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }

    }
    private void getNoti(){
        if (Utility.isInternetAvailable(this)) {

//            progressDialog=new ProgressDialog(NotificationNewTest_Activity.this);
//            progressDialog.show();
//            progressDialog.setMessage("Loading..");
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }
    }
    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
          //  progressDialog.cancel();

            if(statusFromServiceAPI_db!=null){
                if(statusFromServiceAPI_db.equals("0")){
                    showView(main_service_data);
                }
            }

        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;


            userid_ExistingUser=pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "get_user_notifications"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            HttpResponse response =null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r=0;



            if (response != null) {
                r= response.getStatusLine().getStatusCode();
                if(r==200){
                    try {

                        String responseString = null;
                        try {
                            responseString = EntityUtils.toString(response.getEntity());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {
                            statusFromServiceAPI_db="0";
                            main_service_data = jsonObj.optString("data").toString();
                            pref.setAllNotificationData(main_service_data);


                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    statusFromServiceAPI_db="999";
                }
            }

        }
    }

    class AdapterMy extends BaseAdapter implements Filterable {

        Context c;
        ArrayList<NotificationObj> players;
        CustomFilter filter;
        ArrayList<NotificationObj> filterList;

        public AdapterMy(Context ctx,ArrayList<NotificationObj> players) {
            // TODO Auto-generated constructor stub

            this.c=ctx;
            this.players=players;
            this.filterList=players;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return players.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return players.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            // TODO Auto-generated method stub
            return players.indexOf(getItem(pos));
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.notification_list_item_layout, null);
            }

            TextView nameTxt=(TextView) convertView.findViewById(R.id.txt_text);
            TextView txt_time = (TextView) convertView.findViewById(R.id.txt_time);

            RelativeLayout layout_cell_gb= (RelativeLayout) convertView.findViewById(R.id.layout_cell_gb);

            com.ayubo.life.ayubolife.utility.CircularNetworkImageView img=(com.ayubo.life.ayubolife.utility.CircularNetworkImageView) convertView.findViewById(R.id.notiicon_com_list_image);
         //=============================================
            String imageURL = players.get(pos).getIcon();

            String title=players.get(pos).getTitle();
            int srtLen=title.length();
            String text=players.get(pos).getText();
            String fullString=title+" : "+text;


            String readStatus=players.get(pos).getRead();
            System.out.println("=========readStatus============="+readStatus);
            if(readStatus.equals("false")){

                layout_cell_gb.setBackgroundColor(Color.parseColor("#d3d3d3"));

            }else{
                layout_cell_gb.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            String upToNCharacters = fullString.substring(0, Math.min(fullString.length(), 80));
            upToNCharacters=upToNCharacters+" More..";
            SpannableStringBuilder str = new SpannableStringBuilder(upToNCharacters);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,srtLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameTxt.setText(str);

            String timestamp=players.get(pos).getDatetime();

            //  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            //    sdf = new SimpleDateFormat("yyyy-MM-dd");
            long number = Long.valueOf(timestamp);
            number=number*1000;
            // Date date=new Date(number);
            Calendar cal = Calendar.getInstance();
            Calendar todayCal = Calendar.getInstance();
            Date d = new Date(number);
            cal.setTime(d);

            int date = cal.get(Calendar.DATE);
            Date todayDate = new Date();

            todayCal.setTime(todayDate);
            int cameDate = cal.get(Calendar.DATE);
            int currDate = todayCal.get(Calendar.DATE);
            SimpleDateFormat formatter =null;
            if(cameDate==currDate){
                formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss'X' z");
            }else{
                formatter = new SimpleDateFormat("E, dd MMM yyyy'X'HH:mm:ss z");
            }
            String dateFromDB_forADay = formatter.format(cal.getTime());
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(new Date());
//                String sd = formatter.format(date.getTime());
            String[] parts = dateFromDB_forADay.split("X");
            String part1 = parts[0];
            txt_time.setText(part1);


            //=================================

            if (imageLoader == null)
                imageLoader= App.getInstance().getImageLoader();

            img.setImageUrl(imageURL, imageLoader);

            return convertView;
        }

        @Override
        public Filter getFilter() {
            // TODO Auto-generated method stub
            if(filter == null)
            {
                filter=new CustomFilter();
            }

            return filter;
        }

        //INNER CLASS
        class CustomFilter extends Filter
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                // TODO Auto-generated method stub

                FilterResults results=new FilterResults();

                if(constraint != null && constraint.length()>0)
                {
                    //CONSTARINT TO UPPER
                    constraint=constraint.toString().toUpperCase();

                    ArrayList<NotificationObj> filters=new ArrayList<NotificationObj>();

                    //get specific items
                    for(int i=0;i<filterList.size();i++)
                    {
                        if(filterList.get(i).getText().toUpperCase().contains(constraint))
                        {
                         //   String id, title,text, icon, datetime, link, read, type;
                            NotificationObj p=new NotificationObj(filterList.get(i).getId(),filterList.get(i).getTitle(),filterList.get(i).getText(),filterList.get(i).getIcon(),filterList.get(i).getDatetime(),filterList.get(i).getLink(),filterList.get(i).getRead(),filterList.get(i).getType());

                            filters.add(p);
                        }
                    }

                    results.count=filters.size();
                    results.values=filters;

                }else
                {
                    results.count=filterList.size();
                    results.values=filterList;

                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // TODO Auto-generated method stub

                players=(ArrayList<NotificationObj>) results.values;
                notifyDataSetChanged();
            }

        }

    }

}
