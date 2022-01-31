package com.ayubo.life.ayubolife.map_challenges;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.model.Achievement;
import com.ayubo.life.ayubolife.model.AchievementObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class Badges_Activity extends AppCompatActivity implements SectionedGridViewAdapter.OnGridItemClickListener {
    private Dataset dataSet;
    private SectionedGridViewAdapter adapterr = null;
    private LinkedHashMap<String, Cursor> cursorMap;
    String latest_id;
    String latest_title;
    String latest_image;

    String latest_status;
    String latest_share_image;
    String latest_last_achive_date;
    String latest_count;
    String latest_description;


    GridView gridView;
    ImageView img_latest_badge;
    ProgressDialog prgDialog;
    ProgressDialog prgDialog_Badges;
    int isLatestAvailable;
    PrefManager pref;
    String badgesJsonData;
    List<Map<String, List<Object>>> items = new ArrayList<Map<String, List<Object>>>();
    Map<String, String> sectionHeaderTitles = new HashMap<String, String>();
    ArrayList<Achievement> achievement = null;
    String userid_ExistingUser, serviceDataStatus, challenge_id;
    ArrayList<AchievementObj> achievementObj = null;
    //StickyGridHeadersGridView sghl;
    RecyclerView mRecyclerView;
    private GridView mGridView;
    String[] countryData = {"China", "USA", "UK", "Russia"};
    Context c;
    List<String> listDataHeader;
    HashMap<String, List<Achievement>> listDataChild;
    //  ArrayList<Achievement>
    List<String> mListHeaders;
    public String[] AUTHORS = new String[]{"Roberto Bola�o",
            "David Mitchell", "Haruki Murakami", "Thomas Pynchon"};
    ImageButton btn_backImgBtn;

    public String[][] BOOKS = new String[][]{
            {"The Savage Detectives", "2666"},
            {"Ghostwritten", "number9dream", "Cloud Atlas",
                    "Black Swan Green", "The Thousand Autumns of Jacob de Zoet"},
            {"A Wild Sheep Chase",
                    "Hard-Boiled Wonderland and the End of the World",
                    "Norwegian Wood", "Dance Dance Dance",
                    "South of the Border, West of the Sun",
                    "The Wind-Up Bird Chronicle", "Sputnik Sweetheart",
                    "Kafka on the Shore", "After Dark", "1Q84"},
            {"V.", "The Crying of Lot 49", "Gravity's Rainbow", "Vineland",
                    "Mason & Dixon", "Against the Day", "Inherent Vice"}};

    final String[] stateData = {"State A", "State B", "State C", "State D"};
    SectionedGridViewAdapter adapter;
    List<String> headerNamesList;
    Map<String, List<Object>> map;
    // List<Object> listMap;
    LinearLayout latest_layout_design;
    ArrayList<Achievement> listMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges_);
        prgDialog_Badges = new ProgressDialog(Badges_Activity.this);
        prgDialog = new ProgressDialog(Badges_Activity.this);
        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        // c = new Badges_Activity();
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Achievement>>();

        mListHeaders = new ArrayList<String>();

        headerNamesList = new ArrayList<String>();
        map = new HashMap<String, List<Object>>();

        gridView = (GridView) findViewById(R.id.sectionedGrid_list);

        img_latest_badge = (ImageView) findViewById(R.id.img_latest_badge);


        latest_layout_design = (LinearLayout) findViewById(R.id.latest_layout_design);

        latest_layout_design.setVisibility(View.GONE);


        Service_getAchievementBadges_ServiceCall();

    }


    void loadData() {
        achievement = new ArrayList<Achievement>();
        dataSet = new Dataset();
        System.out.println("=======badgesJsonData=====" + badgesJsonData);
        achievementObj = new ArrayList<AchievementObj>();
        if (badgesJsonData.length() > 5) {

            String all = null;
            String latest = null;
            try {
                JSONObject dataOb = new JSONObject(badgesJsonData);
                all = dataOb.optString("all").toString();
                latest = dataOb.optString("latest").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((latest != null) && (latest.length() > 5)) {

                JSONArray latestdataArray = null;
                try {
                    latestdataArray = new JSONArray(latest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < latestdataArray.length(); i++) {
                    isLatestAvailable = latestdataArray.length();
                    JSONObject mainObject_One = null;
                    try {
                        mainObject_One = (JSONObject) latestdataArray.get(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    latest_id = mainObject_One.optString("id").toString();
                    latest_title = mainObject_One.optString("name").toString();
                    latest_image = mainObject_One.optString("image").toString();
                    latest_last_achive_date = mainObject_One.optString("last_achive_date").toString();


                    latest_status = mainObject_One.optString("status").toString();
                    latest_share_image = mainObject_One.optString("share_image").toString();
                    latest_last_achive_date = mainObject_One.optString("last_achive_date").toString();
                    latest_count = mainObject_One.optString("count").toString();
                    latest_description = mainObject_One.optString("description").toString();

                    System.out.println("==================latest=================================");
                    //    System.out.println(id+"       "+title+"       "+image+"   "+last_achive_date);

                }
            }

            // ===================================================
            JSONArray dataArray = null;
            try {
                dataArray = new JSONArray(all);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("========Service Calling============" + all);

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject mainObject_One = null;
                try {
                    mainObject_One = (JSONObject) dataArray.get(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String id = mainObject_One.optString("id").toString();
                String title = mainObject_One.optString("title").toString();

                listDataHeader.add(title);

                //  sectionHeaderTitles.put(title, title);
                String achievements = mainObject_One.optString("achievements").toString();
                System.out.println("======achievements=========" + achievements);

                JSONArray sub_AchievementsArray = null;
                try {
                    sub_AchievementsArray = new JSONArray(achievements);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataSet.addSection(title, sub_AchievementsArray.length());
                System.out.println("======title=========" + title);
                System.out.println("======size=========" + sub_AchievementsArray.length());


                for (int t = 0; t < sub_AchievementsArray.length(); t++) {

                    JSONObject mainObject_two = null;
                    try {
                        mainObject_two = (JSONObject) sub_AchievementsArray.get(t);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String id2 = mainObject_two.optString("id").toString();
                    String name = mainObject_two.optString("name").toString();
                    String image = mainObject_two.optString("image").toString();
                    String status = mainObject_two.optString("status").toString();
                    String share_image = mainObject_two.optString("share_image").toString();
                    String last_achive_date = mainObject_two.optString("last_achive_date").toString();
                    String count = mainObject_two.optString("count").toString();
                    String description = mainObject_two.optString("description").toString();
                    Achievement achiObj = new Achievement(id2, name, image, status, share_image, last_achive_date, count, description);
                    achievement.add(achiObj);
                    System.out.println("========Badge Image============" + image);
                    //  listMap.add(achiObj);
                    ArrayList<Achievement> listMap = null;
                }

                // map.put(title, listMap);
                //  items.add(map);
                achievementObj.add(new AchievementObj(id, title, achievement));
                // achievementObj=new AchievementObj(id,title,achievement);

                listDataChild.put(title, achievement);
                System.out.println("=========9=======" + title);
                System.out.println("=========99======" + achievement.size());
                // dataSet.addSection(title, achievement.size());
            }

            System.out.println("================");

        }


    }


    @Override
    public void onGridItemClicked(String sectionName, int position, View v) {

    }

    private class Service_getStepsData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("========Service Calling============0");
            makeSetDefaultDevice();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //showBadgesView();
                        System.out.println("======showBadgesView==========");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    private void Service_getAchievementBadges_ServiceCall() {

        if (Utility.isInternetAvailable(Badges_Activity.this)) {

            prgDialog_Badges.show();
            prgDialog_Badges.setMessage("Loading badges..");
            prgDialog_Badges.setCancelable(false);
            Service_getBadgesData task = new Service_getBadgesData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_getBadgesData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            getBadgesData();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //
            prgDialog_Badges.dismiss();
            if (serviceDataStatus != null) {
                if (serviceDataStatus.equals("0")) {

                    loadData();
                    if (isLatestAvailable > 0) {
//                        String latest_id;
//                        String latest_title;
//                        String latest_image;
//                        String latest_last_achive_date;

                        latest_layout_design.setVisibility(View.VISIBLE);
                        latest_layout_design.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent in = new Intent(Badges_Activity.this, BadgesDetails_Activity.class);
                                in.putExtra("image", latest_image);
                                in.putExtra("name", latest_title);
                                in.putExtra("last_date", latest_last_achive_date);
                                in.putExtra("desc", latest_description);
                                in.putExtra("status", latest_status);
                                in.putExtra("share_image", latest_share_image);
                                v.getContext().startActivity(in);

                            }
                        });
                        Picasso.with(Badges_Activity.this).load(latest_image).into(img_latest_badge);

                    }
                    BadgesGridAdapter booksAdapter = new BadgesGridAdapter(Badges_Activity.this, achievement);
                    gridView.setAdapter(booksAdapter);


                } else {

                }
            }

            //    Intent in = new Intent(getContext(), WorkoutActivity.class);
            // Intent in = new Intent(getContext(), ChallengeCompletedView_Activity.class);


        }
    }

    private void getBadgesData() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +

                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getAchievementBadges"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............get Achievement Badges..................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);


            System.out.println("========Service Calling============0");

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(responseString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String data = jsonObj.optString("data");


            achievementObj = new ArrayList<AchievementObj>();
            if (data.length() > 5) {
                serviceDataStatus = "0";
                badgesJsonData = data;


            }


        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    View.OnClickListener mItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            int position = (Integer)v.getTag(R.id.row);
//            int col = (Integer)v.getTag(R.id.col);
//
//            Map<String, List<Object>> map = adapter.getItem(position);
//            String selectedItemType = adapter.getItemTypeAtPosition(position);
//            List<Object> list = map.get(selectedItemType);
//            GenericModel model = (GenericModel)list.get(col);
//            Toast.makeText(getApplicationContext(), "" + model.getHeader(), Toast.LENGTH_SHORT).show();
        }
    };


    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +

                        "\"user_id\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getAchievementBadges"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............getActivityDetails..................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObj = null;

            try {
                jsonObj = new JSONObject(responseString);
                String data = jsonObj.optString("data").toString();
                System.out.println("========Service Calling============" + data);
                achievementObj = new ArrayList<AchievementObj>();
                if (data.length() > 5) {

                    String all = null;
                    String latest = null;
                    try {
                        JSONObject dataOb = new JSONObject(data);
                        all = dataOb.optString("all").toString();
                        latest = dataOb.optString("latest").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dataSet = new Dataset();

                    JSONArray dataArray = new JSONArray(all);


                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject mainObject_One = null;
                        try {
                            mainObject_One = (JSONObject) dataArray.get(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String id = mainObject_One.optString("id").toString();
                        String title = mainObject_One.optString("title").toString();


                        listDataHeader.add(title);

                        //  sectionHeaderTitles.put(title, title);
                        String achievements = mainObject_One.optString("achievements").toString();
                        System.out.println("======achievements=========" + achievements);

                        JSONArray sub_AchievementsArray = new JSONArray(achievements);
                        ArrayList<Achievement> achievement = null;
                        achievement = new ArrayList<Achievement>();
                        for (int t = 0; t < sub_AchievementsArray.length(); t++) {

                            JSONObject mainObject_two = null;
                            try {
                                mainObject_two = (JSONObject) sub_AchievementsArray.get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String id2 = mainObject_two.optString("id").toString();
                            String name = mainObject_two.optString("name").toString();
                            String image = mainObject_two.optString("image").toString();
                            String status = mainObject_two.optString("status").toString();
                            String share_image = mainObject_two.optString("share_image").toString();
                            String last_achive_date = mainObject_two.optString("last_achive_date").toString();
                            String count = mainObject_two.optString("count").toString();
                            String description = mainObject_two.optString("description").toString();
                            Achievement achiObj = new Achievement(id2, name, image, status, share_image, last_achive_date, count, description);
                            achievement.add(achiObj);
                            listMap.add(achiObj);
                        }


                        // map.put(title, listMap);
                        //  items.add(map);
                        achievementObj.add(new AchievementObj(id, title, achievement));
                        // achievementObj=new AchievementObj(id,title,achievement);

                        listDataChild.put(title, achievement);
                        System.out.println("=========9=======" + title);
                        System.out.println("=========99======" + achievement.size());
                        // dataSet.addSection(title, achievement.size());
                    }

                    System.out.println("================");

                } else {

                    serviceDataStatus = "99";
                }
            } catch (Exception e) {

            }


        } catch (ClientProtocolException e) {
            System.out.println("========Service Calling============7");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("========Service Calling============8");
            e.printStackTrace();
        }

    }

    public class BookcaseAdapter extends ExpandableGridView implements
            View.OnClickListener {

        // For simplicity, we hard-code the headers and data. In an actual app, this
        // can come from the network, the filesystem, SQLite, or any of the
        // usual suspects.


        private Activity activity;

        public BookcaseAdapter(Activity activity, LayoutInflater inflater,
                               int rowLayoutID, int headerID, int itemHolderID, int resizeMode) {
            super(inflater, rowLayoutID, headerID, itemHolderID, resizeMode);
            this.activity = activity;
        }

        @Override
        public Object getItem(int position) {
            for (int i = 0; i < BOOKS.length; ++i) {
                String[] s = BOOKS[i];
                int sss = BOOKS[i].length;
                System.out.println("========0========== " + sss);
                if (position < BOOKS[i].length) {
                    String ss = BOOKS[i][position];
                    System.out.println("========1========== " + ss);
                    return BOOKS[i][position];
                }
                position -= BOOKS[i].length;
            }
            // This will never happen.
            return null;
        }

        @Override
        protected int getDataCount() {
            int total = 0;
            for (int i = 0; i < BOOKS.length; ++i) {
                total += BOOKS[i].length;
                System.out.println("========2========== " + total);
            }
            return total;
        }

        @Override
        protected int getSectionsCount() {
            System.out.println("========3========== " + BOOKS.length);
            return BOOKS.length;
        }

        @Override
        protected int getCountInSection(int index) {
            System.out.println("========4========== " + BOOKS[index].length);
            return BOOKS[index].length;
        }

        @Override
        protected int getTypeFor(int position) {
            int runningTotal = 0;
            int i = 0;
            for (i = 0; i < BOOKS.length; ++i) {
                int sectionCount = BOOKS[i].length;
                if (position < runningTotal + sectionCount)
                    return i;
                runningTotal += sectionCount;
                System.out.println("========5========== " + runningTotal);
            }
            // This will never happen.
            return -1;
        }

        @Override
        protected String getHeaderForSection(int section) {
            return AUTHORS[section];
        }

        @Override
        protected void bindView(View convertView, int position) {
//            String title = (String) getItem(position);
//            TextView label = (TextView) convertView
//                    .findViewById(R.id.bookItem_title);
//            label.setText(title);
//            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Intent i = new Intent(Intent.ACTION_SEARCH);
//            TextView label = (TextView) v.findViewById(R.id.bookItem_title);
//            String text = label.getText().toString();
//            i.putExtra(SearchManager.QUERY, text);
//            activity.startActivity(i);
        }

    }


    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<AchievementObj> achiementObj = null;
        // Keep all Images in array
        public Integer[] mThumbIds = {
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active, R.drawable.home_active,
                R.drawable.home_active
        };

        // Constructor
        public ImageAdapter(Context c, ArrayList<AchievementObj> achievementObj) {
            mContext = c;
            achiementObj = achievementObj;
        }

        @Override
        public int getCount() {
            return achiementObj.size();
        }

        @Override
        public Object getItem(int position) {
            return achiementObj.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);

            AchievementObj ob = achiementObj.get(position);
            String imgPath = ob.getTitle();
            String id = ob.getId();
            ArrayList<Achievement> ac = ob.getAchievement();

            //  imageView.setImageResource(mThumbIds[position]);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if (imgPath.length() > 10) {
                RequestOptions requestOptions1 = new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(Badges_Activity.this).load(imgPath)
                        .transition(withCrossFade())
                        .apply(requestOptions1)
                        .into(imageView);
            }


            imageView.setLayoutParams(new GridView.LayoutParams(200, 220));

            return imageView;
        }


    }


}
