package com.ayubo.life.ayubolife.hemas_hospital_specific;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.model.FamilyMember;
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DisplayFamilyMemeberActivity extends AppCompatActivity {
    String familyDetails, isDeviceIDSet, filePath, image_absolute_path, userid_ExistingUser,hosiptal_key,mobile,familtyData;
    ProgressDialog prgDialog;
    File compressedImageFile;
    long totalSize = 0;
    ImageView imageView_profile;
    private PrefManager pref;
    ArrayList<FamilyMember> myMemberList = null;
    ArrayList<FamilyMember> hospitalMemberList = null;
    ArrayList<DBString> reportTypesList_DBString = null;
    ArrayList<String> reportTypesList = null;
    EditText txt_report_type, txt_report_of, txt_instition, txt_comments;
    String report_of, report_type;
    String memberName;

    String sComment, report_Id;
    String sInstition,hashToken;
    ImageButton btn_backImgBtn;


    String patient_id,hospital_id;
    // Listview Adapter
    ListViewAdapter adapter;
    // Search EditText
    EditText inputSearch;
    TextView btn_done;
    //  ArrayList<String> familyMemberList=null;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    ArrayList<FamilyMember> arraylist = new ArrayList<FamilyMember>();
    ProgressDialog progressDialog;
    ListView family_member_lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hemas_display_family_member);

        progressDialog = new ProgressDialog(DisplayFamilyMemeberActivity.this);

        family_member_lv=(ListView)findViewById(R.id.family_member_lv);


        pref=new PrefManager(DisplayFamilyMemeberActivity.this);


        btn_backImgBtn=(ImageButton)findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        btn_done=(TextView) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DisplayFamilyMemeberActivity.this, SelectFamilyMemberActivity.class);
                intent.putExtra("familtyData",familtyData );
                startActivity(intent);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Service_getFamilyDetails_ServiceCall();
    }

    private void Service_getFamilyDetails_ServiceCall() {

        if (Utility.isInternetAvailable(DisplayFamilyMemeberActivity.this)) {
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setMessage("Loading..");
            Service_getFamilyDetails task = new Service_getFamilyDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            Toast.makeText(DisplayFamilyMemeberActivity.this, "Cannot find active internet connection",
                    Toast.LENGTH_LONG).show();

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

                JSONArray myHospitalData = null;
                JSONArray myFamilyData = null;
                try {
                    myHospitalData = new JSONArray(familyDetails);
                    hospitalMemberList = new ArrayList<FamilyMember>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    myFamilyData = new JSONArray(familtyData);
                    myMemberList = new ArrayList<FamilyMember>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < myFamilyData.length(); i++) {
                    String memberId = null;
                    String memberName = null;
                    String relationship = null;
                    String assigned_user_id = null;
                    String user_pic = null;
                    String uhid = null;

                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) myFamilyData.get(i);

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
                    myMemberList.add(item);
                }
                //==================================
                for (int i = 0; i < myHospitalData.length(); i++) {
                    String hospital_id = null;
                    String memberName = null;
                    String assigned_user_name = null;
                    String assigned_user_id = null;
                    String user_pic = null;
                    String assigned_user_rel = null;

                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) myHospitalData.get(i);
                        hospital_id = jsonMainNode3.optString("hospital_id").toString();
                        memberName = jsonMainNode3.optString("name").toString();
                        assigned_user_name = jsonMainNode3.optString("assigned_user_name").toString();
                        assigned_user_id = jsonMainNode3.optString("assigned_user_id").toString();
                        assigned_user_rel = jsonMainNode3.optString("assigned_user_rel").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FamilyMember item = new FamilyMember(hospital_id, memberName, assigned_user_rel, assigned_user_id, user_pic, assigned_user_name);
                    hospitalMemberList.add(item);
                }
                adapter = new ListViewAdapter(DisplayFamilyMemeberActivity.this, hospitalMemberList);
                family_member_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String nid,name,relationship,assigned_user_id,user_pic,uhid;

                        FamilyMember o = hospitalMemberList.get(position);
                        nid=o.getId();
                        name=o.getName();
                        relationship=o.getRelationship();
                        assigned_user_id=o.getAssigned_user_id();
                        user_pic=o.getUser_pic();
                        uhid=o.getUhid();

                        patient_id=assigned_user_id;
                        hospital_id=nid;

                        Ram.setReportsId(nid);
                        Ram.setReportsName(name);
                        Ram.setReportsRelationship(relationship);
                        Ram.setReportsAssigned_user_id(assigned_user_id);
                        Ram.setReportsUser_pic(user_pic);
                        Ram.setReportsUhid(uhid);
                        Ram.setReportsType("fromBack");

                        Intent intent = new Intent(DisplayFamilyMemeberActivity.this, SelectFamilyMemberActivity.class);
                        intent.putExtra("familtyData",familtyData );
                        intent.putExtra("patient_id",patient_id );
                        intent.putExtra("hospital_id",hospital_id );
                        intent.putExtra("isLocalData","yes" );
                      //  Ram.setHospitalId(hospital_id);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                family_member_lv.setAdapter(adapter);



            }



        }
    }

    private void getFamilyDetails() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        userid_ExistingUser = pref.getLoginUser().get("uid");
        mobile = pref.getLoginUser().get("mobile");
        hosiptal_key = pref.getLoginUser().get("hosiptal_key");

        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"mobile\": \"" + mobile + "\"," +
                        "\"key\": \"" + hosiptal_key + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getUserMobileHospitalRegistrations"));
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
                String family = jsonObj.optString("family").toString();

                if (res.equals("0")) {
                    isDeviceIDSet = "0";
                    familyDetails = data;
                    familtyData=family;
                } else {
                    isDeviceIDSet = "11";
                }


            }
        }else{
            isDeviceIDSet = "11";
        }

    }

    public class CustomAdapter extends ArrayAdapter<ShareEntity> {

        private List<ShareEntity> dataSet;
        Context mContext;

        private  class ViewHolder {
            TextView txtName,txt_details;
            ImageView btn_book;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView userImage;
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
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.share_company_list_rawlayout, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txt_details = (TextView) convertView.findViewById(R.id.details);
                viewHolder.btn_book = (ImageView) convertView.findViewById(R.id.btn_next);
                viewHolder.userImage = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) convertView.findViewById(R.id.share_com_list_image);
                //  com.ayubo.life.ayubolife.utility.CircularNetworkImageView imgProfile = (CircularNetworkImageView)vi.findViewById(R.id.user_pic);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }
            try {
                ShareEntity obj = dataSet.get(position);
                //  String imageURL="http://taprobanes.com/tickets/"+obj.getBanner();

                String imageURL = obj.getLogo_image();

                viewHolder.txtName.setText(obj.getName());


//                if (imageLoader == null)
//                    imageLoader= App.getInstance().getImageLoader();

                //    viewHolder.btn_book.setOnClickListener(this);

                String uri = "@drawable/ayubo_wok";  // where myresource (without the extension) is the file
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                viewHolder.userImage.setBackground(res);



            }catch(Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
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
            TextView txt_member_name;
            TextView txt_member_relationship;
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
                view = inflater.inflate(R.layout.hemas_familymember_listraw, null);

                holder.txt_member_name = (TextView) view.findViewById(R.id.txt_member_name);
                holder.txt_member_relationship = (TextView) view.findViewById(R.id.txt_member_relationship);
                holder.user_icon = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) view.findViewById(R.id.member_user_list_image);
                //  holder.progressNewsList = (ProgressBar) view.findViewById(R.id.progressNewsList);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            FamilyMember obj=hospitalMemberList.get(position);

                   // (hospital_id, memberName, assigned_user_rel, assigned_user_id, user_pic, assigned_user_name);
            String name=obj.getName();
           // String hospital_id=obj.getId();
            String assigned_user_id= obj.getAssigned_user_id();
            String relationship_name=obj.getUhid();
            // Set the results into TextViews
            holder.txt_member_name.setText(name);


          boolean isNameFilled=true;
            for (int i=0; i<myMemberList.size(); i++){
                FamilyMember ob = myMemberList.get(i);
                if(ob.getId().equals(assigned_user_id)){
                    isNameFilled=false;
                    holder.txt_member_relationship.setText(ob.getName());
                }

            }
            if(isNameFilled){
                String memname=pref.getLoginUser().get("name");
                holder.txt_member_relationship.setText(memname);
            }

//            if (imageLoader == null)
//                imageLoader = App.getInstance().getImageLoader();

          //  System.out.println("================="+ApiClient.BASE_URL+image);

//            Glide.with(DisplayFamilyMemeberActivity.this).load(ApiClient.BASE_URL+image)
//                    .crossFade()
//                    .thumbnail(0.5f)
//                    .bitmapTransform(new CircleTransform(DisplayFamilyMemeberActivity.this))
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(holder.user_icon);

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

}
