package com.ayubo.life.ayubolife.hemas_hospital_specific;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.goals_extention.ShareGoals_Activity;
import com.ayubo.life.ayubolife.model.FamilyMember;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SelectFamilyMemberActivity extends AppCompatActivity {
    // List view

    String familyDetails,nid, isDeviceIDSet, filePath, image_absolute_path, userid_ExistingUser;
    ProgressDialog prgDialog;
    File compressedImageFile;
    long totalSize = 0;
    ImageView imageView_profile;
    private PrefManager pref;

    ArrayList<FamilyMember> familyMemberList = null;
    ArrayList<DBString> reportTypesList_DBString = null;
    ArrayList<String> reportTypesList = null;
    EditText txt_report_type, txt_report_of, txt_instition, txt_comments;
    String report_of, report_type;
    String memberName;

    String sComment, report_Id;
    String sInstition, hashToken;
    ImageButton btn_backImgBtn;

    private ListView lv;
    // Listview Adapter
    ListViewAdapter adapter;
    // Search EditText
    EditText inputSearch;
    //  ArrayList<String> familyMemberList=null;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    ArrayList<FamilyMember> arraylist = new ArrayList<FamilyMember>();
    ProgressDialog progressDialog;

    String patient_id,hospital_id,isLocalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_family_member);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        hashToken = pref.getLoginUser().get("hashkey");
        progressDialog = new ProgressDialog(SelectFamilyMemberActivity.this);

        lv = (ListView) findViewById(R.id.list_view_searchfamily);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(SelectFamilyMemberActivity.this, R.drawable.plusimages));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SelectFamilyMemberActivity.this, AddNewMember_Activity.class);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


        ImageButton btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectFamilyMemberActivity.this, DisplayFamilyMemeberActivity.class));
                finish();
            }
        });

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });





    }

    private void loadViewWithLocalData(){
        JSONArray myDataListsAll = null;
        try {
            myDataListsAll = new JSONArray(familyDetails);
            familyMemberList = new ArrayList<FamilyMember>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sNamee = pref.getLoginUser().get("name");
        userid_ExistingUser = pref.getLoginUser().get("uid");

        for (int i = 0; i < myDataListsAll.length(); i++) {
            String memberId = null;
            String memberName = null;
            String relationship = null;
            String assigned_user_id = null;
            String user_pic = null;
            String uhid = null;

            JSONObject jsonMainNode3 = null;
            try {
                jsonMainNode3 = (JSONObject) myDataListsAll.get(i);

                memberId = jsonMainNode3.optString("id").toString();
                memberName = jsonMainNode3.optString("name").toString();
                relationship = jsonMainNode3.optString("relationship").toString();
                assigned_user_id = jsonMainNode3.optString("assigned_user_id").toString();
                user_pic = jsonMainNode3.optString("user_pic").toString();
                uhid = jsonMainNode3.optString("uhid").toString();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            FamilyMember item = new FamilyMember(memberId, memberName, relationship, assigned_user_id, user_pic, uhid);
            familyMemberList.add(item);
        }
        adapter = new ListViewAdapter(SelectFamilyMemberActivity.this, familyMemberList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String patientId, name, relationship, assigned_user_id, user_pic, uhid;

                FamilyMember o = familyMemberList.get(position);
                patientId = o.getId();
                name = o.getName();
                relationship = o.getRelationship();
                assigned_user_id = o.getAssigned_user_id();
                user_pic = o.getUser_pic();
                uhid = o.getUhid();

                Ram.setReportsId(patientId);
                Ram.setReportsName(name);
                Ram.setReportsRelationship(relationship);
                Ram.setReportsAssigned_user_id(assigned_user_id);
                Ram.setReportsUser_pic(user_pic);
                Ram.setReportsUhid(uhid);
                Ram.setReportsType("fromBack");



                patient_id=patientId;
                Service_setUserFamilyHospitalIDs_ServiceCall();

            }
        });
        lv.setAdapter(adapter);
    }

    //===========================================

    private void Service_getFamilyDetails_ServiceCall() {

        if (Utility.isInternetAvailable(SelectFamilyMemberActivity.this)) {
            progressDialog.show();
            progressDialog.setMessage("Loading..");
            Service_getFamilyDetails task = new Service_getFamilyDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            Toast.makeText(SelectFamilyMemberActivity.this, "Cannot find active internet connection",
                    Toast.LENGTH_LONG).show();

        }
    }
    private void getFamilyDetails() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        String jsonStr =
                "{" +

                        "\"userID\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getUserFamily"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            isDeviceIDSet = "11";
            e.printStackTrace();
        }
        HttpResponse response = null;
        String responseString = null;
        int resCode;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e1) {
            isDeviceIDSet = "11";
            e1.printStackTrace();
        }

        if (response != null) {
            resCode = response.getStatusLine().getStatusCode();
            if (resCode == 200) {

                try {
                    responseString = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                String res = jsonObj.optString("result").toString();
                String data = jsonObj.optString("data").toString();

                if (res.equals("0")) {
                    isDeviceIDSet = "0";
                    familyDetails = data;
                } else {
                    isDeviceIDSet = "11";
                }


            }
        }else{
            isDeviceIDSet = "11";
        }

    }
    private class Service_getFamilyDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            getFamilyDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (isDeviceIDSet.equals("0")) {

                JSONArray myDataListsAll = null;
                try {
                    myDataListsAll = new JSONArray(familyDetails);
                    familyMemberList = new ArrayList<FamilyMember>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String sNamee = pref.getLoginUser().get("name");
                userid_ExistingUser = pref.getLoginUser().get("uid");

                for (int i = 0; i < myDataListsAll.length(); i++) {
                    String memberId = null;
                    String memberName = null;
                    String relationship = null;
                    String assigned_user_id = null;
                    String user_pic = null;
                    String uhid = null;

                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) myDataListsAll.get(i);

                        memberId = jsonMainNode3.optString("id").toString();
                        memberName = jsonMainNode3.optString("name").toString();
                        relationship = jsonMainNode3.optString("relationship").toString();
                        assigned_user_id = jsonMainNode3.optString("assigned_user_id").toString();
                        user_pic = jsonMainNode3.optString("user_pic").toString();
                        uhid = jsonMainNode3.optString("uhid").toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FamilyMember item = new FamilyMember(memberId, memberName, relationship, assigned_user_id, user_pic, uhid);
                    familyMemberList.add(item);
                }
                adapter = new ListViewAdapter(SelectFamilyMemberActivity.this, familyMemberList);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String nid,name,relationship,assigned_user_id,user_pic,uhid;

                        FamilyMember o = familyMemberList.get(position);
                        nid=o.getId();
                        name=o.getName();
                        relationship=o.getRelationship();
                        assigned_user_id=o.getAssigned_user_id();
                        user_pic=o.getUser_pic();
                        uhid=o.getUhid();

                        Ram.setReportsId(nid);
                        Ram.setReportsName(name);
                        Ram.setReportsRelationship(relationship);
                        Ram.setReportsAssigned_user_id(assigned_user_id);
                        Ram.setReportsUser_pic(user_pic);
                        Ram.setReportsUhid(uhid);
                        Ram.setReportsType("fromBack");

                        patient_id=nid;
                        Service_setUserFamilyHospitalIDs_ServiceCall();


                    }
                });
                lv.setAdapter(adapter);
            }



        }
    }

    //=====================================



    private void Service_setUserFamilyHospitalIDs_ServiceCall() {

        if (Utility.isInternetAvailable(SelectFamilyMemberActivity.this)) {
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("Loading..");
            Service_setUserFamilyHospitalIDs task = new Service_setUserFamilyHospitalIDs();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            Toast.makeText(SelectFamilyMemberActivity.this, "Cannot find active internet connection",
                    Toast.LENGTH_LONG).show();

        }
    }

    private class Service_setUserFamilyHospitalIDs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            setUserFamilyHospitalIDs();
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (isDeviceIDSet.equals("0")) {

                startActivity(new Intent(SelectFamilyMemberActivity.this, DisplayFamilyMemeberActivity.class));
                finish();
            }
        }
    }

    private void setUserFamilyHospitalIDs() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        userid_ExistingUser = pref.getLoginUser().get("uid");

        String jsonStr =
                "{" +
                        "\"user_id\": \"" + userid_ExistingUser + "\"," +
                        "\"patient_id\": \"" + patient_id + "\"," +
                        "\"hospital_id\": \"" + hospital_id + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "setUserFamilyHospitalIDs"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            isDeviceIDSet = "11";
            e.printStackTrace();
        }
        HttpResponse response = null;
        String responseString = null;
        int resCode;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e1) {
            isDeviceIDSet = "11";
            e1.printStackTrace();
        }

        if (response != null) {
            resCode = response.getStatusLine().getStatusCode();
            if (resCode == 200) {

                try {
                    responseString = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                String res = jsonObj.optString("result").toString();
                //  String data = jsonObj.optString("data").toString();
                //  String family = jsonObj.optString("family").toString();

                if (res.equals("0")) {
                    isDeviceIDSet = "0";
                    //  familyDetails = data;
                    //  familtyData=family;
                } else {
                    isDeviceIDSet = "11";
                }


            }
        }else{
            isDeviceIDSet = "11";
        }

    }



    //========================================


    public class ListViewAdapter extends BaseAdapter {

        // Declare Variables
        Context mContext;
        LayoutInflater inflater;
        private List<FamilyMember> familyMemberlist = null;
        private ArrayList<FamilyMember> arraylist;
        ImageLoader imageLoader;

        public ListViewAdapter(Context context,
                               List<FamilyMember> worldpopulationlist) {
            mContext = context;
            this.familyMemberlist = worldpopulationlist;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<FamilyMember>();
            this.arraylist.addAll(worldpopulationlist);
        }

        public class ViewHolder {
            TextView text_name;
            TextView country;
            TextView population;
            ProgressBar progressNewsList;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView user_icon;
        }

        @Override
        public int getCount() {
            return familyMemberlist.size();
        }

        @Override
        public FamilyMember getItem(int position) {
            return familyMemberlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("WrongViewCast")
        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.search_familymember_raw_layout, null);

                holder.text_name = (TextView) view.findViewById(R.id.text_name);
                holder.user_icon = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) view.findViewById(R.id.member_user_list_image);
                //  holder.progressNewsList = (ProgressBar) view.findViewById(R.id.progressNewsList);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String name = familyMemberlist.get(position).getName();
            String image = familyMemberlist.get(position).getUser_pic();
            // Set the results into TextViews
            holder.text_name.setText(name);
            if (imageLoader == null)
                imageLoader = App.getInstance().getImageLoader();

            System.out.println("=================" + ApiClient.BASE_URL + image);

            RequestOptions requestOptions1 = new RequestOptions()
                    .transform(new CircleTransform(SelectFamilyMemberActivity.this))
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(SelectFamilyMemberActivity.this).load(ApiClient.BASE_URL + image)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions1)
                    .into(holder.user_icon);

            //  holder.user_icon.setImageUrl(ApiClient.BASE_URL+image, imageLoader);
            // loadImage(ApiClient.BASE_URL+image,  holder.user_icon, holder.progressNewsList);

//		Glide.with(view.getContext()).load(ApiClient.BASE_URL+image)
//				.crossFade()
//				.thumbnail(0.5f)
//				.bitmapTransform(new CircleTransform(view.getContext()))
//				.diskCacheStrategy(DiskCacheStrategy.NONE)
//				.into(holder.user_icon);

            //holder.user_icon.setImageResource(worldpopulationlist.get(position).getFlag());
            // Listen for ListView Item Click
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//
//				System.out.println("===========99ygh==========");
//			}
//		});

            return view;
        }

        private void loadImage(String imageUrl, final com.android.volley.toolbox.NetworkImageView imageView, final ProgressBar progressBar) {
            progressBar.setVisibility(View.VISIBLE);
            if (imageUrl != null) {
                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageBitmap(response.getBitmap());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //	Log.e("onErrorResponse ", error.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            familyMemberlist.clear();
            if (charText.length() == 0) {
                familyMemberlist.addAll(arraylist);
            } else {
                for (FamilyMember wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        familyMemberlist.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        isLocalData = getIntent().getStringExtra("isLocalData");
        if(isLocalData.equals("yes")){
            familyDetails = getIntent().getStringExtra("familtyData");
            hospital_id = getIntent().getStringExtra("hospital_id");
            System.out.println("=====yes=========hospital_id================"+hospital_id);
            loadViewWithLocalData();
        }
        else{
           // hospital_id= Ram.getHospitalId();
            System.out.println("=====no=========hospital_id================"+hospital_id);
            Service_getFamilyDetails_ServiceCall();
        }

    }


}
