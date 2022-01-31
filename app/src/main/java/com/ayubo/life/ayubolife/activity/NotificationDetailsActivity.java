package com.ayubo.life.ayubolife.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.body.ShareEntity;
import com.ayubo.life.ayubolife.body.WorkoutShareListActivity;
import com.ayubo.life.ayubolife.model.DoctorAvailablesObj;
import com.ayubo.life.ayubolife.model.NotificationObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.LruBitmapCache;
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

public class NotificationDetailsActivity extends AppCompatActivity {
    // List view
    private ListView lv;
   ProgressDialog progressDialog;
    // Listview Adapter
    SearchableAdapter adapter;
   // ArrayAdapter<String> adapter;
    ArrayList<NotificationObj> dataList=null;
    private PrefManager pref;
    // Search EditText
    Context globalContext;
    EditText inputSearch;
    String userid_ExistingUser,statusFromServiceAPI_db;
    ImageLoader imageLoader;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    ImageButton btn_backImgBtn;
    private List<NotificationObj>filteredDataM = null;
    ArrayList<HashMap<String, String>> productList;
    String notification_id,web_link;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);



// <!--set_user_notification_read-->
//                <!--get_user_notifications-->
            // Listview Data
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

            lv = (ListView) findViewById(R.id.list_view_notification);
            inputSearch = (EditText) findViewById(R.id.inputSearch_text);
      //  inputSearch.setInputType(InputType.TYPE_NULL);
       // inputSearch.setFocusable(false);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NotificationObj obj= filteredDataM.get(position);
                notification_id =obj.getId().toString();
                String readSt= obj.getRead();
                web_link= obj.getLink();
                System.out.println("Read===Status========="+readSt);
                if(notification_id!=null){
                        Intent intent = new Intent(NotificationDetailsActivity.this, NotificationDetailsViewActivity.class);
                        intent.putExtra("URL", web_link);
                        intent.putExtra("notification_id", notification_id);
                        intent.putExtra("readStatus", readSt);
                        startActivity(intent);
                       // finish();
                }
                    System.out.println("Already read============");

            }
        });



            getNoti();

           inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                NotificationDetailsActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


       InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);


    }


    public class SearchableAdapter extends BaseAdapter implements Filterable {

        private List<NotificationObj>originalData = null;
        private List<NotificationObj>filteredData = null;
        private List<NotificationObj> dataSet;

        private LayoutInflater mInflater;
        private ItemFilter mFilter = new ItemFilter();

        private  class ViewHolder {
            TextView txt_text,txt_time;
            RelativeLayout layout_cell_gb;
            ImageView btn_book;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView userImage;
        }

        public SearchableAdapter(Context context, List<NotificationObj> data) {
            this.filteredData = data ;
            this.originalData = data ;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return filteredData.size();
        }

        public Object getItem(int position) {
            return filteredData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unnecessary calls
            // to findViewById() on each row.
            SearchableAdapter.ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.notification_list_item_layout, null);
                holder = new SearchableAdapter.ViewHolder();
                holder = new ViewHolder();

                holder.txt_text = (TextView) convertView.findViewById(R.id.txt_text);
                holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);

                holder.layout_cell_gb= (RelativeLayout) convertView.findViewById(R.id.layout_cell_gb);

                holder.userImage = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) convertView.findViewById(R.id.notiicon_com_list_image);

                NotificationObj obj=filteredData.get(position);
                filteredDataM=filteredData;
                String imageURL = obj.getIcon();

                String title=obj.getTitle();
                int srtLen=title.length();
                String text=obj.getText();
                String fullString=title+" : "+text;


                String readStatus=obj.getRead();
                System.out.println("=========readStatus============="+readStatus);
                if(readStatus.equals("false")){

                    holder.layout_cell_gb.setBackgroundColor(Color.parseColor("#d3d3d3"));

                }else{
                    holder.layout_cell_gb.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                String upToNCharacters = fullString.substring(0, Math.min(fullString.length(), 80));
                upToNCharacters=upToNCharacters+" More..";
                SpannableStringBuilder str = new SpannableStringBuilder(upToNCharacters);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,srtLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txt_text.setText(str);

                String timestamp=obj.getDatetime();

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
                holder.txt_time.setText(part1);

                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();

                holder.userImage.setImageUrl(imageURL, imageLoader);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }



        public Filter getFilter() {
            return mFilter;
        }

        private class ItemFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<NotificationObj> list = originalData;

                int count = list.size();
                 ArrayList<NotificationObj> nlist =null;
                nlist = new ArrayList<NotificationObj>(count);
                NotificationObj filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = list.get(i);


                    String desc=filterableString.getText();
                    System.out.println("================================="+filterString);

                    if (desc.toLowerCase().contains(filterString)) {
                        nlist.add(filterableString);
                        System.out.println("================================="+desc);
                    }
                }

                results.values = nlist;
                results.count = nlist.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredData = (ArrayList<NotificationObj>) results.values;
                filteredDataM=filteredData;

                adapter.notifyDataSetChanged();
                notifyDataSetChanged();
            }

        }
    }

    private void clearNoti(){
        if (Utility.isInternetAvailable(this)) {
           // progressDialog=new ProgressDialog(NotificationDetailsActivity.this);
           // progressDialog.show();
           // progressDialog.setMessage("Loading..");
            updateClearNotifications task = new updateClearNotifications();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }
    }
    private class updateClearNotifications extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateClearNotifications();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
          //  progressDialog.cancel();
            Intent intent = new Intent(NotificationDetailsActivity.this, CommonWebViewActivity.class);
            intent.putExtra("URL", web_link);
            startActivity(intent);
            finish();
//            if(statusFromServiceAPI_db!=null){
//
//
//                if(statusFromServiceAPI_db.equals("0")){
//
//                    if(dataList.size() > 0){
//                        adapter=new SearchableAdapter(NotificationDetailsActivity.this,dataList);
//                        lv.setAdapter(adapter);
//                    }
//                }
//            }

        }

        private void makePostRequest_updateClearNotifications() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(NotificationDetailsActivity.this);
            userid_ExistingUser=pref.getLoginUser().get("uid");
            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"," +
                            "\"notification_id\": \"" + notification_id + "\"" +
                            "}";



            nameValuePair.add(new BasicNameValuePair("method", "set_user_notification_read"));
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
                            statusFromServiceAPI_db="0";
                            System.out.println(".........Set Read............." + statusFromServiceAPI_db);

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



    private void getNoti(){
        if (Utility.isInternetAvailable(this)) {

            progressDialog=new ProgressDialog(NotificationDetailsActivity.this);
            progressDialog.show();
            progressDialog.setMessage("Loading..");
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
            progressDialog.cancel();

            if(statusFromServiceAPI_db!=null){


                if(statusFromServiceAPI_db.equals("0")){

                            if(dataList.size() > 0){


                                adapter=new SearchableAdapter(NotificationDetailsActivity.this,dataList);
                                lv.setAdapter(adapter);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationDetailsActivity.this);
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

        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(NotificationDetailsActivity.this);
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
                            statusFromServiceAPI_db="0";
                            String dat = jsonObj.optString("data").toString();

                            JSONArray myListsAll = null;

                            dataList = new ArrayList<NotificationObj>();
                            try {
                                myListsAll = new JSONArray(dat);
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
                                    String fullText=title+" : "+text;
                                    String icon = datajsonObj.optString("Icon").toString();
                                    String datetime = datajsonObj.optString("DateTime").toString();
                                    String link = datajsonObj.optString("link").toString();
                                    String read = datajsonObj.optString("Read").toString();
                                    String type = datajsonObj.optString("Type").toString();
                                    NotificationObj sen = new NotificationObj(id, title, fullText, icon, datetime, link, read,type);
                                  //  String id, title,text, icon, datetime, link, read, type;
                                    dataList.add(sen);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

    public class CustomAdapter extends ArrayAdapter<NotificationObj>{

        private List<NotificationObj> dataSet;
        Context mContext;

        private  class ViewHolder {
            TextView txt_text,txt_time;
            RelativeLayout layout_cell_gb;
            ImageView btn_book;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView userImage;
        }

        public CustomAdapter( List<NotificationObj> moviesList, Context context) {
            super(context, R.layout.share_company_list_rawlayout, moviesList);
            this.dataSet = moviesList;
            this.mContext=context;

        }



        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            NotificationObj dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {
              //  TextView txtTitle,txt_text;
                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.notification_list_item_layout, parent, false);

                viewHolder.layout_cell_gb= (RelativeLayout) convertView.findViewById(R.id.layout_cell_gb);
              //  viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                viewHolder.txt_text = (TextView) convertView.findViewById(R.id.txt_text);
                viewHolder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);

              //  viewHolder.btn_book = (ImageView) convertView.findViewById(R.id.txt_time);
                viewHolder.userImage = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) convertView.findViewById(R.id.notiicon_com_list_image);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result=convertView;
            }
            try {
                NotificationObj obj = dataSet.get(position);
                //  String imageURL="http://taprobanes.com/tickets/"+obj.getBanner();

                String imageURL = obj.getIcon();

                String title=obj.getTitle();
                int srtLen=title.length();
                String fullString=obj.getText();
               // String fullString=title+" : "+text;


                String readStatus=obj.getRead();
                if(readStatus.equals("false")){
                    viewHolder.layout_cell_gb.setBackgroundColor(Color.parseColor("#d3d3d3"));


                }else{
                    viewHolder.layout_cell_gb.setBackgroundColor(Color.parseColor("#ffffff"));
                }



               // viewHolder.txtTitle.setText();
              //  viewHolder.txt_text.setText(obj.getText());

                SpannableStringBuilder str = new SpannableStringBuilder(fullString);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,srtLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
               // TextView tv=new TextView(context);
                viewHolder.txt_text.setText(str);

                String timestamp=obj.getDatetime();
                SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy z");
                long number = Long.valueOf(timestamp);
                Date date=new Date(number);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                String sd = formatter.format(date.getTime());

              // String strDate = formatter.format(date);

                viewHolder.txt_time.setText(sd);

                if (imageLoader == null)
                    imageLoader= App.getInstance().getImageLoader();

                viewHolder.userImage.setImageUrl(imageURL, imageLoader);

              //  viewHolder.userImage.setImageUrl(imageURL, imageLoader);



//                try {
//                    int imageResource = getResources().getIdentifier(imageURL, null, getPackageName());
//                    Drawable res = getResources().getDrawable(imageResource);
//                    viewHolder.userImage.setBackground(res);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }

                //   viewHolder.btn_book.setOnClickListener(this);





            }catch(Exception e){
                e.printStackTrace();
            }

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


    }

}