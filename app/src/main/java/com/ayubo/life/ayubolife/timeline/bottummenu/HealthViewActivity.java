package com.ayubo.life.ayubolife.timeline.bottummenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.db.DBRequest;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.ImageListObj;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.progress_dialog.CustomProgressDialog;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.MyProgressLoading;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
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

public class HealthViewActivity extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<DBString> listDataHeader;
    HashMap<String, List<ImageListObj>> listDataChild;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;

    String userid_ExistingUser,statusFromServiceAPI_db,main_service_data;
    ArrayList<ShareEntity> data;
    int sectionCount=0;
    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    PrefManager pref;
    private ProgressDialog pd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relax_view);

        inflatert = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(HealthViewActivity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        pd = CustomProgressDialog.ctor(this, "Loading");
        pd.setCancelable(true);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //   pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        pref=new PrefManager(HealthViewActivity.this);

        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<ImageListObj>>();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setDividerHeight(-10);


        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        DBString dataObj= DBRequest.getCashDataByType(HealthViewActivity.this,"health_data");

        String dat=null;

        if(dataObj!=null){
            dat=dataObj.getId();
            loadRelaxDataView(dat);
        }

        if(pref.getRefreshDataType().equals("health")){
            if(pref.getFromSendToBaseView().equals("yes")){
                pref.setFromSendToBaseView("no");
                pref.setRefreshDataType("no");
            }
        }


        if (Utility.isInternetAvailable(this)) {
            if ((dat != null) && (dat.length() > 10)) {
            } else {
                MyProgressLoading.showLoading(HealthViewActivity.this, "Please wait...");
            }
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            textt.setText("Unable to detect an active internet connection");
            toastt.setView(layoutt);
            toastt.show();
        }


    }

    void loadRelaxDataView(String main_service_data){

        if (main_service_data.length()> 10) {

            listDataHeader = new ArrayList<DBString>();
            listDataChild = new HashMap<String, List<ImageListObj>>();
            //    expListView.setDividerHeight(-10);

            List<ImageListObj> videosList=null;
            List<ImageListObj> tracksList =null;
            List<ImageListObj> programsList =null;

            JSONObject jsonObjdat = null;
            try {
                jsonObjdat = new JSONObject(main_service_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data=new ArrayList<ShareEntity>();
            String tracks  = jsonObjdat.optString("tracks").toString();
            String videos  = jsonObjdat.optString("videos").toString();
            String programs  = jsonObjdat.optString("programs").toString();

            tracksList = new ArrayList<ImageListObj>();
            videosList = new ArrayList<ImageListObj>();
            programsList= new ArrayList<ImageListObj>();

            JSONArray videosmyListsAll = null;
            JSONArray programsListsAll = null;
            JSONArray tracksmyListsAll = null;
            int vCount=0;
            int tCount=0;  int pCount=0;
            try {
                if(programs.length()>2){
                    programsListsAll = new JSONArray(programs);
                    pCount= programsListsAll.length();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(tracks.length()>2){
                     tracksmyListsAll = new JSONArray(tracks);
                     tCount= tracksmyListsAll.length();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(videos.length()>2){
                    videosmyListsAll = new JSONArray(tracks);
                    vCount= tracksmyListsAll.length();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            sectionCount=0;


            if(tCount!=0){//for tracks
                sectionCount++;
                listDataHeader.add(new DBString("Stay","Healthy"));//for tracks
                for (int i = 0; i < tracksmyListsAll.length(); i++) {

                    JSONObject tracksjsonObj = null;
                    try {
                        tracksjsonObj = (JSONObject) tracksmyListsAll.get(i);
                        String name = tracksjsonObj.optString("name").toString();
                        String image = tracksjsonObj.optString("image").toString();
                        String link1 = tracksjsonObj.optString("link").toString();
                        String link2 = tracksjsonObj.optString("link").toString();
                        String action = tracksjsonObj.optString("action").toString();
                        String meta = tracksjsonObj.optString("meta").toString();
                        tracksList.add(new ImageListObj(name,image,link1,link2,action,meta));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("Healthy", tracksList);//for tracks
            }
            //=================================================
            if(vCount!=0){
                sectionCount++;
                listDataHeader.add(new DBString("Stay","Healthy"));  //for video
                for (int a = 0; a < videosmyListsAll.length(); a++) {

                    JSONObject videosjsonObj = null;
                    try {
                        videosjsonObj = (JSONObject) videosmyListsAll.get(a);
                        String name = videosjsonObj.optString("name").toString();
                        String image = videosjsonObj.optString("image").toString();
                        String link1 = videosjsonObj.optString("link").toString();
                        String link2 = videosjsonObj.optString("link").toString();
                        String action = videosjsonObj.optString("action").toString();
                        String meta = videosjsonObj.optString("meta").toString();
                        videosList.add(new ImageListObj(name,image,link1,link2,action,meta));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("Healthy", videosList); //for video

            }
            //=================================================
            if(pCount!=0){
                sectionCount++;       //for programs
                listDataHeader.add(new DBString("Stay","Healthy"));//for programs
                for (int a = 0; a < programsListsAll.length(); a++) {

                    JSONObject programsjsonObj = null;
                    try {
                        programsjsonObj = (JSONObject) programsListsAll.get(a);
                        String name = programsjsonObj.optString("name").toString();
                        String image = programsjsonObj.optString("image").toString();
                        String link1 = programsjsonObj.optString("link").toString();
                        String link2 = programsjsonObj.optString("link").toString();


                        String action = programsjsonObj.optString("action").toString();
                        String meta = programsjsonObj.optString("meta").toString();
                        programsList.add(new ImageListObj(name,image,link1,link2,action,meta));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("Healthy", programsList);//for programs

            }


            listAdapter = new ExpandableListAdapter(HealthViewActivity.this, listDataHeader, listDataChild);

            expListView.setAdapter(listAdapter);

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

            //============ON CLICK LISTNER===========================
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return false;
                }
            });

            expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    int count =  listAdapter.getGroupCount();

                }
            });
            // Listview on child click listener
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    // TODO Auto-generated method stub
                    int si=  listDataHeader.size();
                    String headerType=listDataHeader.get(groupPosition).getName();
                    pref.setRefreshDataType("health");
                    ImageListObj obj=listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition);


                    String webLInk=listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition).getLink1();

                    //  FirebaseAnalytics Adding
                    Bundle bPromocode_used = new Bundle();
                    bPromocode_used.putString("name",webLInk);


                    if(SplashScreen.firebaseAnalytics!=null){
                        SplashScreen.firebaseAnalytics.logEvent("Health_program_clicked", bPromocode_used);
                    }

                    if(webLInk.equals("-")){

                    }else{
                        processAction(obj.getAction(),obj.getMeta());
                    }



                    return false;
                }
            });
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
            MyProgressLoading.dismissDialog();

            if (statusFromServiceAPI_db.equals("0")) {
                loadRelaxDataView(main_service_data);
            }

        }
    }

    private void makePostRequest_updateOnlineWorkoutDetails() {
        //  prgDialog.show();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String walk, run, jump, energy, cals, distance, dateOfBirth;

        pref = new PrefManager(HealthViewActivity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getCategoryHealthList"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

    //    System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            statusFromServiceAPI_db="999";
            e.printStackTrace();
        }
        HttpResponse response =null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            statusFromServiceAPI_db="999";
            e.printStackTrace();
        }
        int r=0;

        String responseString = null;

        if(response!=null){
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                statusFromServiceAPI_db="999";
                e.printStackTrace();
            }
        }


        if (response != null) {
            r= response.getStatusLine().getStatusCode();
            if(r==200){
                try {
                    System.out.println("..........Health............." + responseString);
                    statusFromServiceAPI_db="999";
                    JSONObject jsonObj = new JSONObject(responseString);
                    statusFromServiceAPI_db = jsonObj.optString("result").toString();


                    if (statusFromServiceAPI_db.equals("0")) {
                        main_service_data = jsonObj.optString("data").toString();

                        DBRequest.updateDoctorData(HealthViewActivity.this,main_service_data,"health_data");
                       // pref.setHealthData("a");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    statusFromServiceAPI_db="999";
                    e.printStackTrace();
                }
            }else{
                statusFromServiceAPI_db="999";
            }
        }

    }




    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        ImageLoader imageLoader;
        private Context _context;
        ProgressBar progressBar;
        ImageView relax_cell_image;
        private List<DBString> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<ImageListObj>> _listDataChild;

        public ExpandableListAdapter(Context context, List<DBString> listDataHeader,
                                     HashMap<String, List<ImageListObj>> listChildData) {
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

            String imageLine=_listDataChild.get(_listDataHeader.get(groupPosition).getName()).get(childPosition).getImage();
            String childName=_listDataChild.get(_listDataHeader.get(groupPosition).getName()).get(childPosition).getName();

            final String childText ="Test";
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.relax_list_item_layout, null);
            }
            imageLine=ApiClient.BASE_URL+imageLine;

            //  ProgressBar progressNewsList = (ProgressBar) convertView.findViewById(R.id.progressNewsList);

            relax_cell_image = (ImageView) convertView.findViewById(R.id.img_child_image);

            progressBar = (ProgressBar) findViewById(R.id.progress);
            progressBar=new ProgressBar(HealthViewActivity.this);

            RequestOptions requestOptions1 = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(HealthViewActivity.this).load(imageLine)
                    .apply(requestOptions1)
                    .into(relax_cell_image);

            return convertView;
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

            TextView lblListHeader1 = (TextView) convertView.findViewById(R.id.lblListHeader1);
            TextView lblListHeader2 = (TextView) convertView.findViewById(R.id.lblListHeader2);

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








}
