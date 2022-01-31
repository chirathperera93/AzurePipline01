package com.ayubo.life.ayubolife.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.model.DB4String;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EnterPromoCodeActivity extends AppCompatActivity {
    // ProgressDialog progressDialog;
    String statusFromServiceAPI_db, userid_ExistingUser, fullCode, fromClass;
    PrefManager pref;
    ArrayList<DB4String> dataList = null;
    EditText code_1, code_2, code_3, code_4, code_5;
    TextView txt_plswait, txt_donthvpromocode, connectingCommunityText, txt_desc;
    LinearLayout linearLayoutForConnecting;
    NetworkImageView img_person;
    Context globalContext;
    int imagePos;
    int imagePos2;
    DB4String ob;
    Timer timer_foutTimePerDay_Go, timer_foutTimePerDay_0, timer_foutTimePerDay_1, timer_foutTimePerDay_2;
    TimerTask timer_foutTimePerDay_doAsynchronousTask_Go, timer_foutTimePerDay_doAsynchronousTask_0, timer_foutTimePerDay_doAsynchronousTask_1, timer_foutTimePerDay_doAsynchronousTask_2;
    private Handler handler = new Handler();

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

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
    public void onBackPressed() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

            System.out.println(fromClass);

            if (!fromClass.equals("no")) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                return false;
            } else {
                finish();
                return true;
            }

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();


            Drawable background = getResources().getDrawable(R.drawable.community_key_bg);


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }


        setContentView(R.layout.activity_enter_promo_code);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        fromClass = getIntent().getStringExtra("fromClass");

        code_1 = (EditText) findViewById(R.id.code_1);
        code_2 = (EditText) findViewById(R.id.code_2);
        code_3 = (EditText) findViewById(R.id.code_3);
        code_4 = (EditText) findViewById(R.id.code_4);
        code_5 = (EditText) findViewById(R.id.code_5);

        code_1.requestFocus();


        img_person = (NetworkImageView) findViewById(R.id.img_person);
        //    NetworkImageView avatar = (NetworkImageView)view.findViewById(R.id.twitter_avatar);
        //  avatar.setImageUrl("http://someurl.com/image.png",mImageLoader);
        imagePos2 = 0;

        txt_plswait = (TextView) findViewById(R.id.txt_plswait);
        linearLayoutForConnecting = (LinearLayout) findViewById(R.id.linearLayoutForConnecting);
        txt_desc = (TextView) findViewById(R.id.txt_desc);
        connectingCommunityText = (TextView) findViewById(R.id.connectingCommunityText);
        txt_donthvpromocode = (TextView) findViewById(R.id.txt_donthvpromocode);

        txt_plswait.setVisibility(View.GONE);
        linearLayoutForConnecting.setVisibility(View.GONE);
        txt_donthvpromocode.setVisibility(View.VISIBLE);

        txt_donthvpromocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(EnterPromoCodeActivity.this, NewHomeWithSideMenuActivity.class);
//                Intent intent = new Intent(EnterPromoCodeActivity.this, LifePlusProgramActivity.class);
                Intent intent = new Intent(EnterPromoCodeActivity.this, ProfileNew.class);
                startActivity(intent);

                finish();

            }
        });

        pref = new PrefManager(EnterPromoCodeActivity.this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        //  progressDialog=new ProgressDialog(EnterVerificationCodeActivity.this);

        code_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("=======afterTextChanged=================");
                //  toggle.setEnabled(txtFrecuencia.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("=========vbeforeTextChanged===============");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String code1 = code_1.getText().toString();
                if (code1.length() > 0) {
                    code_2.requestFocus();
                }

            }
        });
        code_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    System.out.println("=========this is for backspace=1==============");
                    //this is for backspace
                    code_1.setText("");
                }
                return false;
            }
        });
        //===================================

        code_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("=======afterTextChanged=================");
                //  toggle.setEnabled(txtFrecuencia.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("=========vbeforeTextChanged===============");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String code1 = code_2.getText().toString();
                if (code1.length() > 0) {
                    code_3.requestFocus();
                }

            }
        });
        code_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code_1.requestFocus();
                    code_1.setText("");
                }
                return false;
            }
        });
        //=============================================
        code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("=======afterTextChanged=================");
                //  toggle.setEnabled(txtFrecuencia.length() > 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                System.out.println("=========vbeforeTextChanged===============");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String code1 = code_3.getText().toString();
                if (code1.length() > 0) {
                    code_4.requestFocus();
                }

            }
        });
        code_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code_2.requestFocus();
                    code_2.setText("");
                }
                return false;
            }
        });
        //======================================

        code_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String code1 = code_4.getText().toString();
                if (code1.length() > 0) {
                    code_5.requestFocus();
                }
            }
        });
        code_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code_3.requestFocus();
                    code_3.setText("");
                }
                return false;
            }
        });
        //=================================
        code_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Check if no view has focus:
                View view = EnterPromoCodeActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String code1 = code_1.getText().toString();
                String code2 = code_2.getText().toString();
                String code3 = code_3.getText().toString();
                String code4 = code_4.getText().toString();
                String code5 = code_5.getText().toString();

                fullCode = code1 + code2 + code3 + code4 + code5;

                if (fullCode.length() == 5) {

                    //  FirebaseAnalytics Adding
                    Bundle bPromocode_used = new Bundle();
                    bPromocode_used.putString("status", "successful");
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Promocode_used", bPromocode_used);
                    }
                    sendPromoCodeToServer();
                } else {
                    //  FirebaseAnalytics Adding
                    Bundle bPromocode_used = new Bundle();
                    bPromocode_used.putString("status", "unsuccessful");
                    if (SplashScreen.firebaseAnalytics != null) {
                        SplashScreen.firebaseAnalytics.logEvent("Promocode_used", bPromocode_used);
                    }
                }


            }
        });

        code_5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    code_4.requestFocus();
                    code_4.setText("");
                }
                return false;
            }
        });
        //===========================================

    }

    private void sendPromoCodeToServer() {
        if (Utility.isInternetAvailable(this)) {
            txt_plswait.setVisibility(View.VISIBLE);
            linearLayoutForConnecting.setVisibility(View.VISIBLE);
            txt_donthvpromocode.setVisibility(View.GONE);
//            txt_plswait.setText("Please wait..");
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
            //   progressDialog.cancel();
            txt_plswait.setVisibility(View.GONE);
            linearLayoutForConnecting.setVisibility(View.GONE);
            txt_donthvpromocode.setVisibility(View.VISIBLE);
//            txt_plswait.setText("");
            if (statusFromServiceAPI_db != null) {

                if (statusFromServiceAPI_db.equals("0")) {


                    if ((dataList != null) && (dataList.size() > 0)) {

                        hideKeyboard(EnterPromoCodeActivity.this);

                        if (mImageLoader == null)
                            mImageLoader = App.getInstance().getImageLoader();


                        linearLayoutForConnecting.setVisibility(View.VISIBLE);
                        txt_donthvpromocode.setVisibility(View.GONE);

                        if (dataList.size() == 1) {
                            timer_foutTimePerDay_0 = new Timer();
                            initializeTimerTaskS_0();
                            timer_foutTimePerDay_0.schedule(timer_foutTimePerDay_doAsynchronousTask_0, 0, 5000);

                            timer_foutTimePerDay_Go = new Timer();
                            initializeTimerTaskS_Go();
                            timer_foutTimePerDay_Go.schedule(timer_foutTimePerDay_doAsynchronousTask_Go, 6000, 95000);
                        } else if (dataList.size() == 2) {

                            timer_foutTimePerDay_0 = new Timer();
                            initializeTimerTaskS_0();
                            timer_foutTimePerDay_0.schedule(timer_foutTimePerDay_doAsynchronousTask_0, 0, 5000);

                            timer_foutTimePerDay_1 = new Timer();
                            initializeTimerTaskS_1();
                            timer_foutTimePerDay_1.schedule(timer_foutTimePerDay_doAsynchronousTask_1, 4000, 5000);

                            System.out.println("===========is==================");
                            timer_foutTimePerDay_Go = new Timer();
                            initializeTimerTaskS_Go();
                            timer_foutTimePerDay_Go.schedule(timer_foutTimePerDay_doAsynchronousTask_Go, 8000, 95000);


                        } else if (dataList.size() == 3) {

                            timer_foutTimePerDay_0 = new Timer();
                            initializeTimerTaskS_0();
                            timer_foutTimePerDay_0.schedule(timer_foutTimePerDay_doAsynchronousTask_0, 0, 5000);

                            timer_foutTimePerDay_1 = new Timer();
                            initializeTimerTaskS_1();
                            timer_foutTimePerDay_1.schedule(timer_foutTimePerDay_doAsynchronousTask_1, 5000, 5000);

                            timer_foutTimePerDay_2 = new Timer();
                            initializeTimerTaskS_2();
                            timer_foutTimePerDay_2.schedule(timer_foutTimePerDay_doAsynchronousTask_2, 8000, 5000);

                            System.out.println("===========finish==================");
                            timer_foutTimePerDay_Go = new Timer();
                            initializeTimerTaskS_Go();
                            timer_foutTimePerDay_Go.schedule(timer_foutTimePerDay_doAsynchronousTask_Go, 11000, 95000);

                        }
                    }

                } else {
                    txt_plswait.setVisibility(View.GONE);
//                    txt_plswait.setText("");
                    Toast.makeText(EnterPromoCodeActivity.this, "Invalid promo code !",
                            Toast.LENGTH_LONG).show();
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

            pref = new PrefManager(EnterPromoCodeActivity.this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"user_id\": \"" + userid_ExistingUser + "\"," +
                            "\"promo_code\": \"" + fullCode + "\"" +
                            "}";


            nameValuePair.add(new BasicNameValuePair("method", "setupPromoCode"));
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
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r = 0;

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);

                        statusFromServiceAPI_db = jsonObj.optString("result").toString();

                        //  {"result":23,"message":"Invalid promo code"}
//14725
                        if (statusFromServiceAPI_db.equals("0")) {
                            statusFromServiceAPI_db = "0";
                            String dat = jsonObj.optString("data").toString();

                            JSONObject jsonObj2 = new JSONObject(dat);
                            String communities = jsonObj2.optString("communities").toString();
                            System.out.println("======responseString============" + communities);
                            JSONArray myListsAll = null;

                            dataList = new ArrayList<DB4String>();
                            try {
                                myListsAll = new JSONArray(communities);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < myListsAll.length(); i++) {

                                JSONObject datajsonObj = null;
                                try {
                                    datajsonObj = (JSONObject) myListsAll.get(i);
                                    String ids = datajsonObj.optString("id").toString();
                                    String name = datajsonObj.optString("name").toString();
                                    String image = datajsonObj.optString("image").toString();

                                    // SpesilitiesObj sen = new SpesilitiesObj(ids, name, image);
                                    //  String id, title,text, icon, datetime, link, read, type;
                                    //  dataList.add( new SpesilitiesObj(ids, name, image));
                                    dataList.add(new DB4String(ids, name, image, ""));

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
                } else {
                    statusFromServiceAPI_db = "999";
                }
            }

        }
    }

    public void initializeTimerTaskS_Go() {
        timer_foutTimePerDay_doAsynchronousTask_Go = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        if (fromClass.equals("register")) {

                            Intent in = new Intent(EnterPromoCodeActivity.this, Welcome_Animation_Activity.class);
                            startActivity(in);
                            finish();

                        } else {
//                            Intent intent = new Intent(EnterPromoCodeActivity.this, NewHomeWithSideMenuActivity.class);
//                            Intent intent = new Intent(EnterPromoCodeActivity.this, LifePlusProgramActivity.class);
                            Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                            startActivity(intent);
                        }

                        if (timer_foutTimePerDay_doAsynchronousTask_Go != null) {
                            timer_foutTimePerDay_doAsynchronousTask_Go.cancel();
                            timer_foutTimePerDay_doAsynchronousTask_Go = null;
                        }

                    }
                });
            }

        };
    }


    public void initializeTimerTaskS_0() {
        timer_foutTimePerDay_doAsynchronousTask_0 = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        ob = dataList.get(0);

//                        connectingCommunityText.setText("Connecting you to the " + ob.getName() + " Community");
                        linearLayoutForConnecting.setVisibility(View.GONE);
                        txt_desc.setText("Connecting you to the " + ob.getName() + " Community");
                        img_person.setImageUrl(ob.getSpecility().toString(), mImageLoader);

                        if (timer_foutTimePerDay_doAsynchronousTask_0 != null) {
                            timer_foutTimePerDay_doAsynchronousTask_0.cancel();
                            timer_foutTimePerDay_doAsynchronousTask_0 = null;
                        }

                    }
                });
            }

        };
    }

    public void initializeTimerTaskS_1() {
        timer_foutTimePerDay_doAsynchronousTask_1 = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        ob = dataList.get(1);
//                        connectingCommunityText.setText("Connecting you to the " + ob.getName() + " Community");
                        linearLayoutForConnecting.setVisibility(View.GONE);
                        txt_desc.setText("Connecting you to the " + ob.getName() + " Community");

                        img_person.setImageUrl(ob.getSpecility().toString(), mImageLoader);

                        if (timer_foutTimePerDay_doAsynchronousTask_1 != null) {
                            timer_foutTimePerDay_doAsynchronousTask_1.cancel();
                            timer_foutTimePerDay_doAsynchronousTask_1 = null;
                        }

                    }
                });
            }

        };
    }

    public void initializeTimerTaskS_2() {
        timer_foutTimePerDay_doAsynchronousTask_2 = new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        ob = dataList.get(2);


//                        connectingCommunityText.setText("Connecting you to the " + ob.getName() + " Community");
                        linearLayoutForConnecting.setVisibility(View.GONE);
                        txt_desc.setText("Connecting you to the " + ob.getName() + " Community");

                        img_person.setImageUrl(ob.getSpecility().toString(), mImageLoader);

                        if (timer_foutTimePerDay_doAsynchronousTask_2 != null) {
                            timer_foutTimePerDay_doAsynchronousTask_2.cancel();
                            timer_foutTimePerDay_doAsynchronousTask_2 = null;
                        }

                    }
                });
            }

        };
    }


    public class ScheduledTask extends TimerTask {
        private ImageLoader mImageLoader;
        private RequestQueue mRequestQueue;

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

        Date now; // to display current time

        // Add your task here
        public void run() {

            if (mImageLoader == null)
                mImageLoader = App.getInstance().getImageLoader();

            now = new Date(); // initialize date
            System.out.println("Time is :" + now); // Display current time

            //img_person = (ImageView)findViewById(R.id.img_person);
            ob = dataList.get(imagePos);

            connectingCommunityText.setText(ob.getName());
            txt_desc.setText(ob.getName());


        }
    }

    public class SchedulerMain {
        public void main(String args[]) throws InterruptedException {

            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
            time.schedule(st, 0, 1000); // Create Repetitively task for every 1 secs

            //for demo only.
            for (int i = 0; i <= 5; i++) {
                System.out.println("Execution in Main Thread...." + i);
                Thread.sleep(2000);
                if (i == 5) {
                    System.out.println("Application Terminates");
                    System.exit(0);
                }
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
