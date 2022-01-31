package com.ayubo.life.ayubolife.our_service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.home_popup_menu.AppointmentCommonObj;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorObj;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowDoctoresActivity extends AppCompatActivity {
    PrefManager pref;
    String userid_ExistingUser,appointmentid;
    String statusFromServiceAPI_db="";

    ArrayList<Object> songsList;


    ImageButton btn_backImgBtn;
    String activityName;
    String serviceName,activityHeading;

    Button secondButton;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<DBString> listDataHeader;
    HashMap<String, List<AppointmentCommonObj>> listDataChild;
    int sectionCount=0;
    ProgressDialog progressDialog;

    ArrayList<AppointmentCommonObj> appointmentsTodayServerList;
    ArrayList<AppointmentCommonObj> appointmentsUpcomingServerList;
    ArrayList<AppointmentCommonObj> appointmentsPreviourServerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctores);

        pref = new PrefManager(ShowDoctoresActivity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");

        progressDialog=new ProgressDialog(ShowDoctoresActivity.this);

        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<AppointmentCommonObj>>();

        songsList = new ArrayList<>();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

     //   list=(ListView)findViewById(R.id.list_doctor_search2);

        appointmentsTodayServerList= new ArrayList<AppointmentCommonObj>();
        appointmentsUpcomingServerList= new ArrayList<AppointmentCommonObj>();
        appointmentsPreviourServerList= new ArrayList<AppointmentCommonObj>();

        songsList= Ram.getDoctorDataList();

        listDataHeader= Ram.getListDataHeader();
        listDataChild= Ram.getListDataChild();
        sectionCount=Ram.getAppointRawCount();

        if(listDataHeader!=null){
        if(listDataHeader.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowDoctoresActivity.this);
            LayoutInflater inflat = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layoutView = inflat.inflate(R.layout.alert_no_appinments_layout, null, false);
            builder.setView(layoutView);

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{

            listAdapter = new ExpandableListAdapter(ShowDoctoresActivity.this, listDataHeader, listDataChild);

            expListView.setAdapter(listAdapter);

            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true; // This way the expander cannot be collapsed
                }
            });

            if(sectionCount==1){
                expListView.expandGroup(0);
            }
            if(sectionCount==2){
                expListView.expandGroup(0);
                expListView.expandGroup(1);
            }
            if(sectionCount==3){
                expListView.expandGroup(0);
                expListView.expandGroup(1);
                expListView.expandGroup(2);
            }

        }
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                String headerType=listDataHeader.get(groupPosition).getName();

                String weblink=listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition).getLink();


                if(weblink.length() > 5){
                    System.out.println("==============================="+weblink);
                    Intent intent = new Intent(ShowDoctoresActivity.this, CommonWebViewActivity.class);
                    intent.putExtra("URL", ApiClient.BASE_URL+weblink);
                    startActivity(intent);
                }else{
                    System.out.println("===========weblink===================="+weblink);
                }



                return false;
            }
        });


    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        ImageLoader imageLoader;
        private Context _context;
        private List<DBString> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<AppointmentCommonObj>> _listDataChild;

        public ExpandableListAdapter(Context context, List<DBString> listDataHeader,
                                     HashMap<String, List<AppointmentCommonObj>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            AppointmentCommonObj obj=_listDataChild.get(_listDataHeader.get(groupPosition).getName()).get(childPosition);

            String docName=obj.getDocname();
            String specialization=obj.getSpecialization_c();
            String appDate=obj.getEnds();
            String location=obj.getLocation();
            String appId=obj.getId();
            String link=obj.getLink();
            String cancel=obj.getCancel();
            String status=obj.getStatus();


            final String childText ="Test";
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.raw_appointment_new_layout, null);
            }

            TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            TextView txt_spec = (TextView) convertView.findViewById(R.id.txt_spec);
            TextView txt_date = (TextView) convertView.findViewById(R.id.txt_date);
            TextView txt_place = (TextView) convertView.findViewById(R.id.txt_place);

            ImageView locationIcon = (ImageView) convertView.findViewById(R.id.locationIcon);
            ImageView btn_cancel = (ImageView) convertView.findViewById(R.id.btn_cancel);

            btn_cancel.setTag(appId+","+status);

            btn_cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String tagString= v.getTag().toString();
                    String[] tagArray = tagString.split(",");
                    appointmentid = tagArray[0];
                    String status = tagArray[1];

                    if(status.equals("Active")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDoctoresActivity.this);
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View layoutView = inflater.inflate(R.layout.alert_confirm_cancel_appointment, null, false);
                        builder.setView(layoutView);

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Utility.isInternetAvailable(ShowDoctoresActivity.this)) {
                                    progressDialog.show();
                                    progressDialog.setMessage("Cancelling appointment..");
                                    cancelAppintmentsService task = new cancelAppintmentsService();
                                    task.execute(new String[]{ApiClient.BASE_URL_live});
                                }
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();


                        System.out.println("===========onClick==========  Active ");
                    }else{
                        System.out.println("====================   ");
                    }

                }
            });


            txt_name.setText(docName);
            txt_spec.setText(specialization);
            txt_date.setText(appDate);
            txt_place.setText(location);


            if(location.equals("Video Chat")){
                //    show video icon
                //  Bitmap bitmap= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.videochat);
                //  locationIcon.setImageBitmap(bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.videochat);
                    locationIcon.setImageBitmap(bitmap);
                }else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.videochat);
                    Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                    locationIcon.setImageBitmap(myLogo);
                }

            }else{
                //    show build icon
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.cal);
                    locationIcon.setImageBitmap(bitmap);
                }else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.cal);
                    Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                    locationIcon.setImageBitmap(myLogo);
                }

            }


            if(cancel.equals("yes")) {
                System.out.println("======Red===============   "+cancel+"      "+status+"==================="+appDate);
                //  hide red button
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.cancels);
                    btn_cancel.setImageBitmap(bitmap);
                }else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.cancels);
                    Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                    btn_cancel.setImageBitmap(myLogo);
                }

                //  Bitmap bitmap= BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cancels);
                // btn_cancel.setImageBitmap(bitmap);
            }else{
                System.out.println("======No Red===============   "+cancel+"      "+status+"==================="+appDate);

            }
            if(status.equals("Canceled")) {
                // show grey
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.cancelleds);
                    btn_cancel.setImageBitmap(bitmap);
                }else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.cancelleds);
                    Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                    btn_cancel.setImageBitmap(myLogo);
                }

            }
            else{
                System.out.println("======No Canceled===============   "+cancel+"      "+status+"==================="+appDate);

            }

            if(!link.equals("-")) {
//                Intent intent = new Intent(getContext(), CommonWebViewActivity.class);
//                intent.putExtra("URL", link);
//                startActivity(intent);
            }


            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            int coun=_listDataChild.get(this._listDataHeader.get(groupPosition).getName()).size();
            System.out.println("=========getChildrenCount============================="+coun);
            return coun;

        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            DBString headerTitle = (DBString) getGroup(groupPosition);
            String title,heading;
            heading=headerTitle.getName();
            title=headerTitle.getId();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.relax_list_item_header_layout, null);
            }
            System.out.println("=========Header==============================="+headerTitle);
            TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblListHeader1);
            TextView lblListHeader2 = (TextView) convertView.findViewById(R.id.lblListHeader2);
            //lblListHeader1.setTypeface(null, Typeface.BOLD);
            lblListHeader1.setText(title);
            lblListHeader2.setText(heading);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
    private class cancelAppintmentsService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_cancelAppintmentsService();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();

            if(statusFromServiceAPI_db!=null){

                if (statusFromServiceAPI_db.equals("0")) {

                    if (Utility.isInternetAvailable(ShowDoctoresActivity.this)) {
                        progressDialog.show();
                        progressDialog.setMessage("Refreshing..");
                        refreshAppointmentData task = new refreshAppointmentData();
                        task.execute(new String[]{ApiClient.BASE_URL_live});
                    }


                }else{
                    Toast.makeText(ShowDoctoresActivity.this, statusFromServiceAPI_db,
                            Toast.LENGTH_LONG).show();

                }
            }
        }

        private void makePostRequest_cancelAppintmentsService() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(ShowDoctoresActivity.this);
            userid_ExistingUser=pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"appointmentid\": \"" + appointmentid + "\"," +
                            "\"userid\": \"" + userid_ExistingUser
                            + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "cancelAppointment"));
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

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                r= response.getStatusLine().getStatusCode();
                if(r==200){
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {


                        } else {
                            statusFromServiceAPI_db = jsonObj.optString("error").toString();
                            // statusFromServiceAPI_db = "55";
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

    private class refreshAppointmentData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listDataHeader.clear(); Ram.setListDataHeader(listDataHeader);
            listDataChild.clear(); Ram.setListDataChild(listDataChild);
            Ram.setAppointRawCount(0);
            sectionCount=0;
        }

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_refreshAppointmentData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();

            if(statusFromServiceAPI_db!=null) {
                if (statusFromServiceAPI_db.equals("0")) {
                    Ram.setDoctorDataList(songsList);

                    Toast.makeText(ShowDoctoresActivity.this, "Appointment canceled successfully", Toast.LENGTH_LONG).show();

                    listDataHeader = Ram.getListDataHeader();
                    listDataChild = Ram.getListDataChild();
                    sectionCount = Ram.getAppointRawCount();

                    listAdapter = new ExpandableListAdapter(ShowDoctoresActivity.this, listDataHeader, listDataChild);

                    expListView.setAdapter(listAdapter);

                    if (sectionCount == 1) {
                        expListView.expandGroup(0);
                    }
                    if (sectionCount == 2) {
                        expListView.expandGroup(0);
                        expListView.expandGroup(1);
                    }
                    if (sectionCount == 3) {
                        expListView.expandGroup(0);
                        expListView.expandGroup(1);
                        expListView.expandGroup(2);
                    }
                } else {
                    Toast.makeText(ShowDoctoresActivity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void makePostRequest_refreshAppointmentData() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            serviceName= Ram.getServiceName();

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", serviceName));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........refreshAppointmentData............." + nameValuePair.toString());

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
                System.out.println(".......getMedicalExperts...response..........." + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r=0;

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {

                r= response.getStatusLine().getStatusCode();
                if(r==200){
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();

                        JSONArray myMainListsAll = null;
                        if (statusFromServiceAPI_db.equals("0")) {
                            String data = jsonObj.optString("data").toString();
                            JSONObject jsonObj2 = new JSONObject(data);
                            String appointments = jsonObj2.optString("appointments").toString();
                            JSONObject appointmentsJSON = new JSONObject(appointments);


                            String appointmentsToday = appointmentsJSON.optString("today").toString();
                            String appointmentsUpcoming = appointmentsJSON.optString("upcoming").toString();
                            String appointmentsPreviour = appointmentsJSON.optString("previous").toString();

                            // GETTING PREVIOUS DATA
                            // Today=========================================
                            JSONArray appointmentsToday_List = null;
                            try {
                                appointmentsToday_List = new JSONArray(appointmentsToday);

                                if(appointmentsToday_List.length()>0) {
                                    listDataHeader.add(new DBString("","Today"));//for Today
                                    sectionCount++;
                                    for (int i = 0; i < appointmentsToday_List.length(); i++) {

                                        DoctorObj docObj = null;
                                        JSONObject datajsonObj = null;
                                        try {
                                            datajsonObj = (JSONObject) appointmentsToday_List.get(i);
                                            String docname = datajsonObj.optString("docname").toString();
                                            String ends = datajsonObj.optString("ends").toString();
                                            String location = datajsonObj.optString("location").toString();
                                            String patient = datajsonObj.optString("patient").toString();
                                            String specialization_c = datajsonObj.optString("specialization_c").toString();
                                            String link = datajsonObj.optString("link").toString();
                                            String cancel = datajsonObj.optString("cancel").toString();
                                            String id = datajsonObj.optString("id").toString();
                                            String status = datajsonObj.optString("status").toString();
                                            appointmentsTodayServerList.add(new AppointmentCommonObj(docname, ends, location, patient, specialization_c,
                                                    cancel, link, id, status));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    listDataChild.put("Today", appointmentsTodayServerList);//for tracks
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            // GETTING PREVIOUS DATA=========================================
                            // GETTING PREVIOUS DATA

                            JSONArray appointmentsUpcoming_List = null;
                            try {
                                appointmentsUpcoming_List = new JSONArray(appointmentsUpcoming);

                                if(appointmentsUpcoming_List.length()>0) {
                                    listDataHeader.add(new DBString("","Upcoming"));//for Upcoming
                                    sectionCount++;
                                    for (int i = 0; i < appointmentsUpcoming_List.length(); i++) {

                                        DoctorObj docObj= null;
                                        JSONObject datajsonObj = null;
                                        try {
                                            datajsonObj = (JSONObject) appointmentsUpcoming_List.get(i);
                                            String docname = datajsonObj.optString("docname").toString();
                                            String ends = datajsonObj.optString("ends").toString();
                                            String location = datajsonObj.optString("location").toString();
                                            String patient = datajsonObj.optString("patient").toString();
                                            String specialization_c = datajsonObj.optString("specialization_c").toString();
                                            String link = datajsonObj.optString("link").toString();
                                            String cancel = datajsonObj.optString("cancel").toString();
                                            String id = datajsonObj.optString("id").toString();
                                            String status = datajsonObj.optString("status").toString();

                                            appointmentsUpcomingServerList.add(new AppointmentCommonObj(docname,ends,location,patient,specialization_c,
                                                    cancel,link,id,status));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    listDataChild.put("Upcoming", appointmentsUpcomingServerList);//for Upcoming
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            // GETTING PREVIOUS DATA=========================================

                            JSONArray appointmentsPreviour_List = null;
                            try {
                                appointmentsPreviour_List = new JSONArray(appointmentsPreviour);
                                if(appointmentsPreviour_List.length()>0) {
                                    listDataHeader.add(new DBString("", "Previous"));//for Previous
                                    sectionCount++;
                                    for (int i = 0; i < appointmentsPreviour_List.length(); i++) {

                                        DoctorObj docObj = null;
                                        JSONObject datajsonObj = null;
                                        try {
                                            datajsonObj = (JSONObject) appointmentsPreviour_List.get(i);
                                            String docname = datajsonObj.optString("docname").toString();
                                            String ends = datajsonObj.optString("ends").toString();
                                            String location = datajsonObj.optString("location").toString();
                                            String patient = datajsonObj.optString("patient").toString();
                                            String specialization_c = datajsonObj.optString("specialization_c").toString();
                                            String link = datajsonObj.optString("link").toString();
                                            String cancel = datajsonObj.optString("cancel").toString();
                                            String id = datajsonObj.optString("id").toString();
                                            String status = datajsonObj.optString("status").toString();

                                            appointmentsPreviourServerList.add(new AppointmentCommonObj(docname, ends, location, patient, specialization_c,
                                                    cancel, link, id, status));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    listDataChild.put("Previous", appointmentsPreviourServerList);//for Previous

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Ram.setListDataHeader(listDataHeader);
                            Ram.setListDataChild(listDataChild);
                            Ram.setAppointRawCount(sectionCount);
//==========================================================

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

}

