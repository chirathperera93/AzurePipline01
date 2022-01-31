package com.ayubo.life.ayubolife.home;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.ayubo.life.ayubolife.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.ShareEntity;

import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.model.SImpleListString;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.CircularNetworkImageView;
import com.ayubo.life.ayubolife.utility.LruBitmapCache;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDataList_Activity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView list; File newf;
    long totalSize = 0;
    Bitmap b=null;


    String sMets_ToIntent,sCals_ToIntent,sDis_ToIntent,serviceResponse,statusFromServiceAPI_db;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    ArrayList<ShareEntity> data;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    Context globalContext;
    private Context _context;
    int pos;ShareEntity en;
    TextView btn_back_btn; String image_absolute_path,userid_ExistingUser,postType,communityID;
    ImageLoader imageLoader;
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(globalContext);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    String fromActivity;
    boolean hasCashed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_share_list);

        list=(ListView)findViewById(R.id.health_track_list);
        HashMap<String, String> song = new HashMap<String, String>();

        fromActivity = getIntent().getStringExtra("from");


        pref = new PrefManager(this);
        userid_ExistingUser=pref.getLoginUser().get("uid");
        progressDialog=new ProgressDialog(PostDataList_Activity.this);
        data=new ArrayList<ShareEntity>();
        data.add(new ShareEntity("001","Public","Public","Wakensys_Logo_400x400.jpg","1","ayubo.life","image"));


        if(pref.getCommiunityData().equals("")){

          }else{
              hasCashed=true;
              getDataArray(pref.getCommiunityData());
        }


        if (Utility.isInternetAvailable(this)) {
            if(!hasCashed){
                progressDialog.show();
                progressDialog.setMessage("Loading..");
            }
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String s = list.getItemAtPosition(position).toString();
                pos=position;
                en= data.get(pos);

                if(pos==0){
                    communityID= "0";
                    postType= "1";
                }
                else{
                    communityID= en.getId();
                    postType= "2";
                }
                String cName= en.getName();


                pref.setUserSelectedCommiunity(communityID);
                pref.setUserSelectedCommiunityName(cName);

                finish();



            }
        });


        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.setUserSelectedCommiunity("0");
                pref.setUserSelectedCommiunityName("Public");
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        pref.setUserSelectedCommiunity("0");
        pref.setUserSelectedCommiunityName("Public");
        finish();
    }

    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(progressDialog!=null){
                progressDialog.cancel();
            }
            if(serviceResponse.length()>5){

                getDataArray(serviceResponse);
            }
            else{
                displayView(data);
            }

        }

        private void makePostRequest_updateOnlineWorkoutDetails() {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(PostDataList_Activity.this);
            userid_ExistingUser=pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getCommunityListByUser"));
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
                            serviceResponse = jsonObj.optString("data").toString();

                            if(serviceResponse.length()<4){

                            }else{
                                pref.setCommiunityData(serviceResponse);
                            }





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

    void displayView( ArrayList<ShareEntity> dataa){

        if (dataa.size()>0) {
            adapter=new CustomAdapter(dataa, PostDataList_Activity.this);
            list.setAdapter(adapter);
        }
    }
    void getDataArray(String dat){

        JSONArray myListsAll = null;
        if(data.size()>0){
            data.clear();
        }
        data.add(new ShareEntity("001","Public","Public","Wakensys_Logo_400x400.jpg","1","ayubo.life","image"));

        try {
            myListsAll = new JSONArray(dat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < myListsAll.length(); i++) {

            JSONObject datajsonObj = null;
            try {
                datajsonObj = (JSONObject) myListsAll.get(i);
                String id = datajsonObj.optString("id");
                String name = datajsonObj.optString("name");
                String community_type_c = datajsonObj.optString("community_type_c");
                String company_logo_c = datajsonObj.optString("company_logo_c");
                String company_enrolled_c = datajsonObj.optString("company_enrolled_c");
                String no_members = datajsonObj.optString("no_members");
                String logo_image = datajsonObj.optString("logo_image");
                ShareEntity sen = new ShareEntity(id, name, community_type_c, company_logo_c, company_enrolled_c, no_members, logo_image);
                int s = data.size();
                data.add(sen);
                int ss = data.size();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        displayView(data);


    }

    public class CustomAdapter extends ArrayAdapter<ShareEntity>{

        private List<ShareEntity> dataSet;
        Context mContext;

        private  class ViewHolder {
            TextView txtName,txt_details;
            ImageView btn_book;
            com.android.volley.toolbox.NetworkImageView userImage;
        }

        public CustomAdapter( List<ShareEntity> moviesList, Context context) {
            super(context, R.layout.share_company_list_rawlayout, moviesList);
            this.dataSet = moviesList;
            this.mContext=context;

        }


        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ShareEntity dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.share_company_list_rawlayout, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txt_details = (TextView) convertView.findViewById(R.id.details);
                viewHolder.btn_book = (ImageView) convertView.findViewById(R.id.btn_next);
                viewHolder.userImage = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.share_com_list_image);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result=convertView;
            }
            try {
                ShareEntity obj = dataSet.get(position);
                //  String imageURL="http://taprobanes.com/tickets/"+obj.getBanner();

                String imageURL = obj.getLogo_image();

                viewHolder.txtName.setText(obj.getName());


                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();

             //   viewHolder.btn_book.setOnClickListener(this);


                if(obj.getName().equals("Public")){
                    viewHolder.txt_details.setText(obj.getNo_members());
                    String uri = "@drawable/ayubo_wok";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    viewHolder.userImage.setBackground(res);
                }
                else if(obj.getName().equals("Share external")){
                    try {
                        viewHolder.txt_details.setText(obj.getNo_members());
                        String uri = "@drawable/share_wok";  // where myresource (without the extension) is the file
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        viewHolder.userImage.setBackground(res);
                    }catch(Exception e){
                        System.out.println("Error "+e);
                    }
                }
                else{
                    String noofmem=obj.getNo_members()+" Members";
                    viewHolder.txt_details.setText(noofmem);
                    viewHolder.userImage.setImageUrl(imageURL, imageLoader);
                }



            }catch(Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }




}

