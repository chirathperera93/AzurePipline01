package com.ayubo.life.ayubolife.login;

import static com.ayubo.life.ayubolife.new_payment.NewPaymentConstantsKt.PUSH_ACTION;
import static com.ayubo.life.ayubolife.new_payment.NewPaymentConstantsKt.PUSH_META;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramActivity;
import com.ayubo.life.ayubolife.lifeplus.ProfileNew;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.twilio.TwilioHomeActivity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.facebook.appevents.AppEventsLogger;
import com.flavors.changes.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SplashScreen extends BaseActivity {
    private static int SPLASH_TIME_OUT = 10;
    //    TextView btn_reload;
//    ProgressDialog prgDialog;
    private PrefManager pref;
    String userid_ExistingUser, checkAppVersion_status, userStatus, errorData;

    public static FirebaseAnalytics firebaseAnalytics;
    //    TextView txt_moblie_number;
//    TextView brand_text;
    AppEventsLogger loggerFB;
//    ImageView logo_image;

    String actionValue, metaValue;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);


        actionValue = null;
        metaValue = null;
        Intent intentForDeepLink = getIntent();
        String action = intentForDeepLink.getAction();
        Uri data = intentForDeepLink.getData();


        if (action != null && data != null) {
            if (data.getHost().equals(AppConfig.APP_BRANDING_ID)) {
                if (data.getHost() != null && data.getPath() != null) {
//                    ayubo.life://ayubo.life/filtered_videocall
                    String[] arrOfStr = data.getPath().split("/");
                    actionValue = arrOfStr[1];
                    if (arrOfStr.length <= 2) {
                        metaValue = "";
                    } else {
                        metaValue = arrOfStr[2];
                    }


                }
            } else {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                    NewHomeWithSideMenuActivity.getInstance().finish();
                    System.exit(0);

                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
//                    NewHomeWithSideMenuActivity.getInstance().finish();
                    System.exit(0);
                }

            }
        }


        String noticolor = "#c49f12";
        int whiteInt = Color.parseColor(noticolor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(whiteInt);

            Window window = getWindow();
            Drawable background = getResources().getDrawable(R.drawable.ayubo_splash_screen_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setContentView(R.layout.your_activity);


        setContentView(R.layout.activity_splash_screen);


        Ram.setTopMenuTabName("home");
        userStatus = "555";
        pref = new PrefManager(SplashScreen.this);

        pref.setHomeFirsttime("false");
        pref.setVideocall("no");


        AppEventsLogger loggerFB = AppEventsLogger.newLogger(this);
        loggerFB = AppEventsLogger.newLogger(this);

//        Bundle params = new Bundle();
//        params.putString(AppConfig.FACEBOOK_EVENT_ID_SCREEN_NAME, AppConfig.FACEBOOK_EVENT_ID_SPLASH);
//        loggerFB.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, params);


        // FirebaseAnalytics Configurations...................
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
//        firebaseAnalytics.setMinimumSessionDuration(10000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(500);

//        txt_moblie_number = (TextView) findViewById(R.id.txt_moblie_number);
//        brand_text = (TextView) findViewById(R.id.brand_text);
//        logo_image = (ImageView) findViewById(R.id.logo_image);


        if (Constants.type == Constants.Type.LIFEPLUS) {
//            logo_image.setImageResource(R.drawable.life_plus_logo);
//            brand_text.setText("Powered by lifeplus");
        } else {
//            logo_image.setImageResource(R.drawable.logo_mobile);
//            brand_text.setText("Powered by ayubolife");
        }

//        btn_reload = (TextView) findViewById(R.id.btn_reload);
//        btn_reload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_reload.setText("Refreshing..");
//                btn_reload.setTextColor(Color.parseColor("#ff860b"));
//                userid_ExistingUser = pref.getLoginUser().get("uid");
//
//                if (userid_ExistingUser != null && userid_ExistingUser.length() > 5) {
////                    Intent in = new Intent(SplashScreen.this, NewHomeWithSideMenuActivity.class);
////                    Intent in = new Intent(SplashScreen.this, LifePlusProgramActivity.class);
//                    Intent in = new Intent(SplashScreen.this, ProfileNew.class);
//                    startActivity(in);
//                    finish();
//                    System.out.println("============Current User================");
//                } else {
//                    pref.setLastDataUpdateTimestamp("5000000");
//
//                    Intent in = new Intent(SplashScreen.this, LoginActivity_First.class);
//                    startActivity(in);
//                    finish();
//
//                    //New user ...........................
//
//                    System.out.println("============New User================");
//                }
//            }
//        });
//        btn_reload.setVisibility(View.GONE);


        try {

//            prgDialog = new ProgressDialog(SplashScreen.this);

            userid_ExistingUser = pref.getLoginUser().get("uid");

            if (pref.getVideocall().equals("yes")) {

                String token = pref.getVRoomToken();
                String callerName = pref.getVCallerName();
                String callerID = pref.getVCallerId();
                String message = pref.getPrefVdioCallerName();
                String room = pref.getVRoomName();

                Intent launchIntent = new Intent(getApplicationContext(), TwilioHomeActivity.class);
                launchIntent.putExtra("twillio_caller_name", callerName);
                launchIntent.putExtra("twillio_room_name", room);
                launchIntent.putExtra("twillio_access_token", token);
                launchIntent.putExtra("caller_id", callerID);
                launchIntent.setClass(getBaseContext(), TwilioHomeActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                launchIntent.setAction(Intent.ACTION_MAIN);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                launchIntent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                launchIntent.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                launchIntent.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                launchIntent.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                startActivity(launchIntent);
                pref.setVideocall("no");

                finish();
            } else if (userid_ExistingUser != null && userid_ExistingUser.length() > 5) {
                if (actionValue != null && metaValue != null) {
                    processAction(actionValue, metaValue);
                    finish();
                } else {
//                    Intent in = new Intent(SplashScreen.this, NewHomeWithSideMenuActivity.class);
//                    Intent in = new Intent(SplashScreen.this, LifePlusProgramActivity.class);

                    String appToken = pref.getUserToken();


                    if (!appToken.equals("") && appToken != null) {

                        try {

                            String token = "";

                            token = appToken.replace("Bearer ", "");

                            if (token != null && !token.equals("")) {

                                try {
                                    DecodedJWT jwt = JWT.decode(token);
                                    Date jwtExpiredDate = jwt.getExpiresAt();
                                    if (jwtExpiredDate.before(new Date())) {
                                        openConfirmationDialog();
                                    } else {
                                        Intent in = new Intent(SplashScreen.this, ProfileNew.class);
                                        startActivity(in);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                        } catch (JWTDecodeException exception) {
                            System.out.println(exception.getMessage());
                        }
                    } else {
                        Intent in = new Intent(SplashScreen.this, EnterMobileNumberActivity.class);
                        startActivity(in);
                        finish();
                    }


                }


            } else {
                //New user ...........................

                //should check app id
//                Intent in = new Intent(SplashScreen.this, EnterMobileNumberActivity.class);

                if (Constants.type == Constants.Type.LIFEPLUS || Constants.type == Constants.Type.AYUBO) {
                    Intent in = new Intent(SplashScreen.this, NewGetStartedActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(SplashScreen.this, EnterMobileNumberActivity.class);
                    startActivity(in);
                    finish();
                }

            }
        } catch (Exception e) {

        }


    }

    private void openConfirmationDialog() {

        AlertDialog dialogView;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashScreen.this);
        LayoutInflater inflater = (LayoutInflater) SplashScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = inflater.inflate(R.layout.new_common_alert_popup, null, false);
        alertDialogBuilder.setView(layoutView);
        dialogView = alertDialogBuilder.create();
        dialogView.setCancelable(false);
        dialogView.show();


        TextView title = (TextView) layoutView.findViewById(R.id.title);
        title.setText("Attention!");

        TextView description = (TextView) layoutView.findViewById(R.id.description);
        description.setText("Your authentication token has expired. Please login again to continue.");


        TextView no_btn = (TextView) layoutView.findViewById(R.id.no_btn);
        no_btn.setVisibility(View.GONE);


        Button yes_btn = (Button) layoutView.findViewById(R.id.yes_btn);

        if (Constants.type == Constants.Type.LIFEPLUS) {
            yes_btn.setBackgroundResource(R.drawable.life_plus_all_corners_rounded_gradient);
        } else {
            yes_btn.setBackgroundResource(R.drawable.ayubo_life_all_corners_rounded_gradient);
        }

        yes_btn.setText("Login");
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.setUserToken(null);
                pref.createLoginUser("", "", "", "", "", "", "");
                Intent in = new Intent(SplashScreen.this, EnterMobileNumberActivity.class);
                startActivity(in);
                finish();

            }
        });
    }


    private void checkAppVersion_Rest() {

        if (Utility.isInternetAvailable(SplashScreen.this)) {

            Service_checkAppVersion task = new Service_checkAppVersion();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {

//            Intent in = new Intent(SplashScreen.this, NewHomeWithSideMenuActivity.class);
            Intent in = new Intent(SplashScreen.this, LifePlusProgramActivity.class);
            startActivity(in);

            Toast.makeText(SplashScreen.this, "Cannot find active internet connection",
                    Toast.LENGTH_LONG).show();

        }
    }

    private class Service_checkAppVersion extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            checkAppVersion();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (userStatus.equals("0")) {
                if (checkAppVersion_status.equals("mobile")) {
                    //Registration No Need, Direct to Home page
                    if (actionValue != null && metaValue != null) {
                        processAction(actionValue, metaValue);
                        finish();
                    } else {
//                        Intent in = new Intent(SplashScreen.this, NewHomeWithSideMenuActivity.class);
//                        Intent in = new Intent(SplashScreen.this, LifePlusProgramActivity.class);

                        Intent in = new Intent(SplashScreen.this, ProfileNew.class);
                        startActivity(in);
                    }


                } else {
                    //Registration Required, Direct to Register page
                    Intent in = new Intent(SplashScreen.this, LoginActivity_First.class);
                    startActivity(in);
                }
            } else if (userStatus.equals("17")) {
                Toast.makeText(getBaseContext(), errorData, Toast.LENGTH_LONG).show();
                startActivity(new Intent(SplashScreen.this, LoginActivity_First.class));
                finish();
            } else {
                Toast.makeText(getBaseContext(), "Login error", Toast.LENGTH_LONG).show();
                startActivity(new Intent(SplashScreen.this, LoginActivity_First.class));
                finish();
            }

        }

        private void checkAppVersion() {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String jsonStree =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getUsername"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStree));
            System.out.println(".............getUsername................" + jsonStree);

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            int resCode = 0;
            //making POST request.
            try {

                HttpResponse response = httpClient.execute(httpPost);
                if (response != null) {
                    resCode = response.getStatusLine().getStatusCode();

                    if (resCode == 200) {
                        System.out.println("..........response 2..........." + response);

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

                        String res = jsonObj.optString("result").toString();


                        //  {"isMobileUser":true,"result":0}
                        if (res.equals("0")) {
                            userStatus = "0";
                            String isMobileUser = jsonObj.optString("isMobileUser").toString();
                            if (isMobileUser.equals("true")) {
                                checkAppVersion_status = "mobile";
                                System.out.println("...........User Name from Shukri.........mobile......");
                            } else {
                                checkAppVersion_status = "non";
                            }
                        } else if (res.equals("17")) {
                            userStatus = "17";
                            errorData = jsonObj.optString("error").toString();
                        } else {
//                            Toast.makeText(LoginActivity_First.this, "Network Error !",
//                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }

            } catch (ClientProtocolException e) {
                checkAppVersion_status = "net";
                System.out.println(".........Slow Network Connection..error.......");
                System.out.println(".........Slow Network Connection..error......." + e);
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }

        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null && (bundle.containsKey(PUSH_META) || bundle.containsKey(PUSH_ACTION))) {
            String action = bundle.getSerializable(PUSH_ACTION).toString();
            String meta = bundle.getSerializable(PUSH_META).toString();
            processAction(action, meta);
        }
    }
}