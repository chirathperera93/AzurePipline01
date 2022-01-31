package com.ayubo.life.ayubolife.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.FamilyMember;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.timeline.AddComments_Activity;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SelectFamilyMember_Activity extends AppCompatActivity {
    // List view

    String familyDetails, isDeviceIDSet, filePath, image_absolute_path, userid_ExistingUser;
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
    String sInstition,hashToken;
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


    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_family_member_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        hashToken=pref.getLoginUser().get("hashkey");
        inflatert = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(SelectFamilyMember_Activity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(SelectFamilyMember_Activity.this, R.drawable.plusimages));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ureel=ApiClient.BASE_URL+"index.php?module=User_FamilyUHID&action=setFamilyMember&ref="+hashToken;
                Intent intent = new Intent(SelectFamilyMember_Activity.this, CommonWebViewActivity.class);
                intent.putExtra("URL",ureel );
                startActivity(intent);
            }
        });


        ImageButton btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv = (ListView) findViewById(R.id.list_view_searchfamily);
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
                String assignType=null;
                if(userid_ExistingUser.equals(assigned_user_id)){
                    assignType="user";
                }else{
                    assignType="family_member";
                }

                //  FirebaseAnalytics Adding
                Bundle bPromocode_used = new Bundle();
                bPromocode_used.putString("user_type",assignType);
                if(SplashScreen.firebaseAnalytics!=null) {
                    SplashScreen.firebaseAnalytics.logEvent("Records_account_switched", bPromocode_used);
                }

                Ram.setReportsId(nid);
                Ram.setReportsName(name);
                Ram.setReportsRelationship(relationship);
                Ram.setReportsAssigned_user_id(assigned_user_id);
                Ram.setReportsUser_pic(user_pic);
                Ram.setReportsUhid(uhid);
                Ram.setReportsType("fromBack");
                finish();
            }}
        );

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



    private class Service_getFamilyDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            getFamilyDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
          MyProgressLoading.dismissDialog();

            if((familyDetails!=null)&&(familyDetails.length()>10)){
                pref.setFamilyMemberData(familyDetails);
                displayView(familyDetails);
            }
        }
    }

    void displayView(String familyData){


            JSONArray myDataListsAll = null;
            try {
                myDataListsAll = new JSONArray(familyData);
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
            adapter = new ListViewAdapter(SelectFamilyMember_Activity.this, familyMemberList);

            lv.setAdapter(adapter);
        }



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

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.search_familymember_raw_layout, null);

                holder.text_name = (TextView) view.findViewById(R.id.text_name);
                holder.user_icon = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) view.findViewById(R.id.member_user_list_image);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            String name=familyMemberlist.get(position).getName();
            String image=familyMemberlist.get(position).getUser_pic();
            // Set the results into TextViews
            holder.text_name.setText(name);
            if (imageLoader == null)
                imageLoader = App.getInstance().getImageLoader();

            RequestOptions requestOptions1 = new RequestOptions()
                    .transform(new CircleTransform(SelectFamilyMember_Activity.this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(SelectFamilyMember_Activity.this).load(ApiClient.BASE_URL+image)
                    .transition(withCrossFade())
                    .thumbnail(0.5f)
                    .apply(requestOptions1)
                    .into(holder.user_icon);


            return view;
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


       String jsonStringData= pref.getFamilyMemberData();
        if((jsonStringData!=null) && (jsonStringData.length()>10)){
            displayView(jsonStringData);
        }
        if (Utility.isInternetAvailable(this)) {
            if((jsonStringData!=null) && (jsonStringData.length()>10)){
            }else{
                MyProgressLoading.showLoading(SelectFamilyMember_Activity.this,"Please wait...");
            }
            Service_getFamilyDetails task = new Service_getFamilyDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }else{
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
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
}
