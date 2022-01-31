package com.ayubo.life.ayubolife.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.login.LetsGo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;

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
import java.util.List;
import java.util.Locale;

/**
 * Created by appdev on 5/18/2017.
 */

public class Mobile_Login_Activity extends AppCompatActivity {
    EditText txt_name, txt_mobile;
    Button btn_next1;
    ApiInterface apiService;
    ProgressDialog prgDialog;
    private PrefManager pref;

    String mobile, name;
    String SMSResult, SMSCode;
    String userRegisterStatus;
    int index;
    String loginStatus = "";
    String user_id, fName, lName, email, gender, profile_pic, healthData, code, hashToken;

    void localConstructor() {

        pref = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        apiService = null;
        mobile = null;
        name = null;
        SMSResult = null;
        SMSCode = null;
        userRegisterStatus = null;

        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_name.setVisibility(View.GONE);
        btn_next1 = (Button) findViewById(R.id.btn_next1);
        btn_next1.setVisibility(View.GONE);
        txt_mobile = (EditText) findViewById(R.id.txt_mobile);
        txt_mobile.setVisibility(View.GONE);

        String language = pref.getLanguage();
        System.out.println("==================================");
        System.out.println("==================================" + language);
        System.out.println("==================================");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.animate_welcome);

        localConstructor();

//        Intent in = new Intent(Mobile_Login_Activity.this, WelcomeHome.class);
//        startActivity(in);
//        finish();

        //Welcome text animation
        int SPLASH_TIME_OUT = 1000;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                TextView animation_text = (TextView) findViewById(R.id.animation_text);
//                String animatetext_welcome = getString(R.string.animatetext_welcome);
////                String[] sentences = {animatetext_welcome};
//
////                if (animation_text instanceof HTextView) {
////                    if (index + 1 >= sentences.length) {
////                        index = 0;
////                    }
////                    ((HTextView) animation_text).animateText(sentences[index++]);
////                }
//            }
//        }, SPLASH_TIME_OUT);

        //Please enter name text animation
//        int SPLASH_TIME_OUT2 = 4000;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                TyperTextView ani_name_lbl = (TyperTextView) findViewById(R.id.ani_name_lbl);
//                if (ani_name_lbl instanceof HTextView) {
//                    String animatetext_entername = getString(R.string.animatetext_entername);
//                    String[] sentences2 = {animatetext_entername};
//
//                    if (index + 1 >= sentences2.length) {
//                        index = 0;
//                    }
//                    ((HTextView) ani_name_lbl).animateText(sentences2[index++]);
//                }
//            }
//        }, SPLASH_TIME_OUT2);

        //Mobile number entering listner for 10 character
        txt_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txt_name.getWindowToken(), 0);

                    mobile = txt_mobile.getText().toString();
                    name = txt_name.getText().toString();
                    pref.setMobileNumber(mobile);
                    pref.setPrefName(name);

                    //Calling service for getting SMS code
                    clickMobileLogin();


                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        //summery text animation
        int SPLASH_TIME_OUT3 = 6000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                txt_name.setVisibility(View.VISIBLE);
                txt_name.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txt_name, InputMethodManager.SHOW_IMPLICIT);
                btn_next1.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT3);
    }


    public void clickNext(View v) {

        name = txt_name.getText().toString();
        name = name.trim();

        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
            return;
        } else {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(txt_name.getWindowToken(), 0);

            int SPLASH_TIME_OUT5 = 3500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txt_mobile.setVisibility(View.VISIBLE);
                    txt_mobile.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txt_mobile, InputMethodManager.SHOW_IMPLICIT);

                }
            }, SPLASH_TIME_OUT5);

            String strText2 = getString(R.string.animatetext_entermobile);
            String hi = getString(R.string.animatetext_hi);
            String[] sentences4 = {hi + " " + name + strText2};


            txt_name.setVisibility(View.GONE);
            btn_next1.setVisibility(View.GONE);

            TextView ani_enterMobileNumber = (TextView) findViewById(R.id.ani_mobile_lbl);
            ani_enterMobileNumber.setText(hi + " " + name + strText2);

//            if (ani_enterMobileNumber instanceof HTextView) {
//                if (index + 1 >= sentences4.length) {
//                    index = 0;
//                }
//                ((HTextView) ani_enterMobileNumber).animateText(sentences4[index++]);
//            }

        }
    }

    public void clickMobileLogin() {

        mobile = txt_mobile.getText().toString();

        if (Utility.isValidPhoneNumber(mobile)) {

            if (Utility.isInternetAvailable(this)) {

                if (Utility.isInternetAvailable(this)) {
                    txt_mobile.setVisibility(View.GONE);

                    //=============SERVICE CALL=================================
                    //  sendMobileAndGetCode(mobile);
                    mobileLoginServiceCall();
                    //=============SERVICE CALL=================================

                    String strText2 = getString(R.string.animatetext_summery);
                    String thankyou = getString(R.string.animatetext_thankyou);

                    final String[] sentences4 = {thankyou + " " + name + strText2};
                    TextView ani_enterMobileNumber = (TextView) findViewById(R.id.ani_summery);
                    ani_enterMobileNumber.setText(thankyou + " " + name + strText2);
//                    if (ani_enterMobileNumber instanceof HTextView) {
//                        if (index + 1 >= sentences4.length) {
//                            index = 0;
//                        }
//                        ((HTextView) ani_enterMobileNumber).animateText(sentences4[index++]);
//                    }

                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_enter_valid_phonenumber, Toast.LENGTH_LONG).show();
        }
    }
//===================

    private void mobileLoginServiceCall() {


        if (Utility.isInternetAvailable(Mobile_Login_Activity.this)) {
            prgDialog.show();
            prgDialog.setMessage("Loading...");
            Service_userLogout task = new Service_userLogout();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {

        }
    }

    private class Service_userLogout extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            makePostRequest5();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();

            if (loginStatus.equals("0")) {

                if (healthData.length() > 10) {

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(healthData);
                        String res = jsonObj.optString("height").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (user_id.equals("0")) {
                    pref.setUserRegisterStatus("false");
                    System.out.println("==========NEW LOGIN===============");
                } else {
                    pref.createLoginUser(user_id, fName, email, mobile, hashToken, profile_pic, "");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Mobile_Login_Activity.this);
                LayoutInflater inflater = (LayoutInflater) Mobile_Login_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflater.inflate(R.layout.default_device_alert, null, false);
                builder.setView(layoutView);
                final EditText txt_verycode = (EditText) layoutView.findViewById(R.id.txt_verycode);
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String sCode = txt_verycode.getText().toString();
                        if (sCode.equals(code)) {

                            if (user_id.equals("0")) {
                                Intent in = new Intent(Mobile_Login_Activity.this, LetsGo.class);
                                startActivity(in);
                                finish();
                            }
                            if (user_id.length() > 10) {

                                pref.setUserRegisterStatus(user_id);

//                            Intent in = new Intent(Mobile_Login_Activity.this,   NewHomeWithSideMenuActivity.class);
//                            Intent in = new Intent(Mobile_Login_Activity.this,   LifePlusProgramActivity.class);
//                                Intent in = new Intent(Mobile_Login_Activity.this, NewDiscoverActivity.class);

                                Intent in = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                                startActivity(in);
                                finish();
                            }

                        } else {
                            Toast.makeText(Mobile_Login_Activity.this, "Incorrect code!",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Resend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(Mobile_Login_Activity.this, "Login error. Please try again!",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    private AlertDialog getVerificationCode() {

        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        final LayoutInflater inflater = this.getLayoutInflater();
        final View layoutView = inflater.inflate(R.layout.forgotpasswordalert, null, false);
        builder2.setView(layoutView);

        EditText txtEmailAddress = (EditText) layoutView.findViewById(R.id.txtEmailAddress);

//===============================================================================
        builder2.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {


            }
        });

        builder2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder2.create();

    }


    private void makePostRequest5() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        pref.setMobileNumber(mobile);
        String jsonStree =
                "{" +
                        "\"mobile_number\": \"" + mobile + "\"" +
                        "}";


        nameValuePair.add(new BasicNameValuePair("method", "loginVerficationWithMobile"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStree));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
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
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String res = jsonObj.optString("result").toString();

            if (res.equals("0")) {

                loginStatus = "0";

                user_id = jsonObj.optString("userId").toString();
                fName = jsonObj.optString("first_name").toString();
                lName = jsonObj.optString("first_name").toString();
                gender = jsonObj.optString("gender").toString();
                email = jsonObj.optString("email").toString();
                profile_pic = jsonObj.optString("profile_pic").toString();
                healthData = jsonObj.optString("healthData").toString();
                code = jsonObj.optString("code").toString();
                hashToken = jsonObj.optString("hashToken").toString();
                System.out.println("============User=========>" + user_id + fName + lName + email + "     " + code);
                System.out.println("===========healthData=========>" + healthData);

            } else {
                loginStatus = "11";

                System.out.println("=========================LOGIN FAIL=========>");
            }


        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }

    //===================

//    private void sendMobileAndGetCode(String mobile) {
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        String jsonStree =
//                "{" +
//                        "\"mobile_number\": \"" + mobile + "\"" +
//                        "}";
//
//        String method="loginVerficationWithMobile";
//        String JSON="JSON";
//        String JSON2="JSON";
//        String jsonStr=jsonStree;
//        Call<Login> call = apiService.callLoginVerficationWithMobile(method,JSON,JSON2,jsonStr);
//        call.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//
//                int statusCode = response.code();
//                if(statusCode==200){
//                    Login user=  response.body();
//                    if(user.getUserId().equals("false")){
//                        pref.setUserRegisterStatus("false");
//                        System.out.println("==========NEW LOGIN===============");
//                    }else{
//                        pref.setUserRegisterStatus(user.getUserId());
//                        System.out.println("==========EXISTING LOGIN==============="+user.getHashToken());
//                        String st=user.getHashToken();
//
//
//                    }
//
//                }
//            }
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//
//                Toast.makeText(getApplicationContext(),R.string.toast_try_again_later, Toast.LENGTH_LONG).show();

//            }
//        });
//
//
//
//    }


}
