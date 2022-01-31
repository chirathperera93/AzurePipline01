package com.ayubo.life.ayubolife.home;

import android.app.ProgressDialog;
import android.content.Context;
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

public class EatViewActivity extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<DBString> listDataHeader;
    HashMap<String, List<ImageListObj>> listDataChild;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    String userid_ExistingUser,statusFromServiceAPI_db,main_service_data;
    ArrayList<ShareEntity> data;
    int sectionCount=0;

    TextView textt;
    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_view);

        pref=new PrefManager(EatViewActivity.this);


        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<ImageListObj>>();

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setDividerHeight(-20);

        inflatert = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(EatViewActivity.this);
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);

        String dat=null;

        DBString dataObj= DBRequest.getCashDataByType(EatViewActivity.this,"eat_data");


            if(dataObj!=null){
                dat=dataObj.getId();
                loadRelaxDataView(dat);
            }

        if(pref.getRefreshDataType().equals("eat")){
            if(pref.getFromSendToBaseView().equals("yes")){
                pref.setFromSendToBaseView("no");
                pref.setRefreshDataType("no");
            }
        }



                if (Utility.isInternetAvailable(this)) {
                    if((dat!=null) && (dat.length()>10)){
                    }else{
                        MyProgressLoading.showLoading(EatViewActivity.this,"Please wait...");
                    }
                    updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
                    task.execute(new String[]{ApiClient.BASE_URL_live});
                }else{
                    textt.setText("Unable to detect an active internet connection");
                    toastt.setView(layoutt);
                    toastt.show();
                }





        LinearLayout btn_backImgBtn_layout=(LinearLayout)findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  prepareListData();
        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });




    }

    void loadRelaxDataView(String main_service_data) {

        listDataHeader = new ArrayList<DBString>();
        listDataChild = new HashMap<String, List<ImageListObj>>();

        if (main_service_data.length() > 10) {
            List<ImageListObj> videosList = null;
            List<ImageListObj> tracksList = null;
            List<ImageListObj> programsList = null;
            JSONObject jsonObjdat = null;
            try {
                jsonObjdat = new JSONObject(main_service_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String tracks = jsonObjdat.optString("tracks").toString();
            String videos = jsonObjdat.optString("videos").toString();
            String programs = jsonObjdat.optString("programs").toString();

            tracksList = new ArrayList<ImageListObj>();
            videosList = new ArrayList<ImageListObj>();
            programsList = new ArrayList<ImageListObj>();

            JSONArray videosmyListsAll = null;
            JSONArray programsListsAll = null;
            JSONArray tracksmyListsAll = null;

            try {
                programsListsAll = new JSONArray(programs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tracksmyListsAll = new JSONArray(tracks);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                videosmyListsAll = new JSONArray(videos);
            } catch (Exception e) {
                e.printStackTrace();
            }

            sectionCount=0;
            int tCount = tracksmyListsAll.length();
            int vCount = videosmyListsAll.length();
            int pCount = programsListsAll.length();

            if (pCount != 0) {
                sectionCount++;       //for programs
                listDataHeader.add(new DBString("Eat to", "Nourish"));//for programs
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

                        programsList.add(new ImageListObj(name,image,link1,link1,action,meta));



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("Nourish", programsList);//for programs

            }
            //==============================================
            if (tCount != 0) {//for tracks
                sectionCount++;
                listDataHeader.add(new DBString("Time to", "Chill Out"));//for tracks
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

                        tracksList.add(new ImageListObj(name,image,link1,link1,action,meta));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("Chill Out", tracksList);//for tracks
            }
            //=================================================
            if (vCount != 0) {
                sectionCount++;
                listDataHeader.add(new DBString("Watch", "What Experts Say"));  //for video
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

                        videosList.add(new ImageListObj(name,image,link1,link1,action,meta));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                listDataChild.put("What Experts Say", videosList); //for video

            }
            //=================================================

            listAdapter = new ExpandableListAdapter(EatViewActivity.this, listDataHeader, listDataChild);
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
//=============ON CLICK LISTNER ===========================================

            // Listview Group click listener
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

                    String headerType=listDataHeader.get(groupPosition).getName();
                    pref.setRefreshDataType("eat");

                    ImageListObj obj=listDataChild.get(listDataHeader.get(groupPosition).getName()).get(childPosition);

                    //  FirebaseAnalytics Adding
                    Bundle bPromocode_used = new Bundle();
                    bPromocode_used.putString("name",obj.getMeta());
                    if(SplashScreen.firebaseAnalytics!=null) {
                        SplashScreen.firebaseAnalytics.logEvent("Eat_program_clicked", bPromocode_used);
                    }

                    if(obj.getMeta().equals("-")){

                    }else{

                        if(headerType.equals("Nourish")){
                            processAction(obj.getAction(),obj.getMeta());
                        }
                        else if(headerType.equals("Chill Out")){
                            processAction(obj.getAction(),obj.getMeta());
                        }
                        else if(headerType.equals("What Experts Say")){

                            processAction("web",obj.getLink1());

                        }

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

            else if (statusFromServiceAPI_db.equals("999")) {

                Toast.makeText(EatViewActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(EatViewActivity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;
            statusFromServiceAPI_db="999";
            pref = new PrefManager(EatViewActivity.this);
            userid_ExistingUser=pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getCategoryEatList"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));



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
            if (response != null) {
                r= response.getStatusLine().getStatusCode();
                if(r==200){

                    String responseString = null;
                    try {
                        if(response!=null){
                            responseString = EntityUtils.toString(response.getEntity());
                        }

                    } catch (IOException e) {
                        statusFromServiceAPI_db="999";
                        e.printStackTrace();
                    }

                    try {

                        System.out.println("...........Eat............." + responseString);

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {

                            data=new ArrayList<ShareEntity>();
                            main_service_data = jsonObj.optString("data").toString();
                            DBRequest.updateDoctorData(EatViewActivity.this,main_service_data,"eat_data");



                        } else {
                            statusFromServiceAPI_db = "55";
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
    }





    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        ImageLoader imageLoader;
        private Context _context;
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

            ImageView eat_cell_image = (ImageView) convertView.findViewById(R.id.img_child_image);
            RequestOptions requestOptions1 = new RequestOptions()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(EatViewActivity.this).load(imageLine)
                    .apply(requestOptions1)
                    .into(eat_cell_image);

            //	loadImage(imageLine,img_child_image,progressNewsList);


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





}
