package com.ayubo.life.ayubolife.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData;
import com.ayubo.life.ayubolife.login.model.SendVerificationObject;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterMobileNumberActivity extends AppCompatActivity implements CountryCodesListAdapter.OnItemClickListener {
    EditText txt_moblie_number;
//    CountryCodePicker ccp;

    String mobile, country_code;

    ApiInterface apiService;
    // ProgressDialog prgDialog;
    private PrefManager pref;
    String user_id, Name, lName, email, gender, profile_pic, healthData, code, hashToken, userid_ExistingUser, lumenToken, dateOfBirth;

    AppCompatEditText edtPhoneNumber;
    int index;
    String loginStatus = "";
    LinearLayout btnNext;
    String notiBarColor = "";
    ProgressDialog countryCodePickerDialog;
    TextView view_country_name;
    TextView app_name;
    TextView enter_mobile_description_TextView;
    ImageView selected_country_flag;
    ImageView logo_image;
    ImageView down_arrow;
    ProgressAyubo loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Constants.type == Constants.Type.LIFEPLUS) {
//            notiBarColor = "#000000";
//
//        } else {
//            notiBarColor = "#c49f12";
//        }
//        int whiteInt = Color.parseColor(notiBarColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            Window window = getWindow();

            Drawable background = getResources().getDrawable(R.drawable.ayubo_enter_mob_bg);
            if (Constants.type == Constants.Type.LIFEPLUS) {
                background = getResources().getDrawable(R.drawable.life_plus_enter_mob_bg);
            } else {
                background = getResources().getDrawable(R.drawable.ayubo_enter_mob_bg);
            }


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);


//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(whiteInt);
        }
        setContentView(R.layout.activity_enter_mobile_number);
        countryCodePickerDialog = new ProgressDialog(EnterMobileNumberActivity.this);

        pref = new PrefManager(this);

        userid_ExistingUser = pref.getLoginUser().get("uid");

//        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext = (LinearLayout) findViewById(R.id.btnNext);
        view_country_name = (TextView) findViewById(R.id.view_country_name);
        selected_country_flag = (ImageView) findViewById(R.id.selected_country_flag);
//        btnNext.setVisibility(View.GONE);
        txt_moblie_number = (EditText) findViewById(R.id.txt_moblie_number_new);
        enter_mobile_description_TextView = (TextView) findViewById(R.id.enter_mobile_description_TextView);
        loading = (ProgressAyubo) findViewById(R.id.loading);
        logo_image = (ImageView) findViewById(R.id.logo_image);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        app_name = (TextView) findViewById(R.id.app_name);
        txt_moblie_number.setInputType(InputType.TYPE_CLASS_NUMBER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        txt_moblie_number.requestFocus();

        down_arrow.setRotation(270);
        if (Constants.type == Constants.Type.LIFEPLUS) {
            logo_image.setImageResource(R.drawable.life_plus_logo);
            enter_mobile_description_TextView.setText(R.string.enter_mobile_description_life_plus);
            btnNext.setBackgroundResource(R.drawable.life_plus_next_green);
            app_name.setText("Powered by lifeplus");

        } else {
            logo_image.setImageResource(R.drawable.correct_ayubo_logo);
            enter_mobile_description_TextView.setText(R.string.enter_mobile_description_ayubo_life);
            btnNext.setBackgroundResource(R.drawable.ayubo_next_orange);
            app_name.setText("Powered by ayubo.life");
        }


//        app_name.setPaintFlags(app_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_moblie_number.addTextChangedListener(new TextWatcher() {
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
                String code1 = txt_moblie_number.getText().toString();
                if (code1.length() > 5) {
//                    btnNext.setVisibility(View.VISIBLE);
                }

            }
        });

        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso().toUpperCase();

        if (countryCode.equals("")) {
            countryCode = "LK";
        }


        String countryNumbersJson = loadJSONFromAsset();
        try {
            JSONArray jsonArray = new JSONArray(countryNumbersJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                if (jsonObject.get("isoCode").equals(countryCode)) {
//                    view_country_name.setText("(" + jsonObject.get("isoCode") + ")" + "  " + jsonObject.get("dialCode"));
                    view_country_name.setText(jsonObject.get("dialCode").toString());
                    Glide.with(getBaseContext()).load(jsonObject.get("flag").toString()).into(selected_country_flag);
                    country_code = jsonObject.get("dialCode").toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        ccp = (CountryCodePicker) findViewById(R.id.ccp_new);

//        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//        String countryCode = tm.getNetworkCountryIso();
//        ccp.setDefaultCountryUsingNameCode(countryCode.toUpperCase());

//        ccp.resetToDefaultCountry();

//        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//                country_code = ccp.getSelectedCountryCode();
//                //  Toast.makeText(EnterMobileNumberActivityPayment.this, "Updated " + country_code, Toast.LENGTH_SHORT).show();
//            }
//        });


        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCountrySelectPopUp();
            }
        });

        selected_country_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCountrySelectPopUp();
            }
        });

        view_country_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCountrySelectPopUp();
            }
        });


    }

    AlertDialog dialog;


    public void openCountrySelectPopUp() {
        // Create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String countryNumbersJson = loadJSONFromAsset();

        try {
            JSONArray jsonArray = new JSONArray(countryNumbersJson);

            final View customLayout = getLayoutInflater().inflate(R.layout.country_code_dialog, null);

            RecyclerView recyclerView = (RecyclerView) customLayout.findViewById(R.id.country_recyclerview);
            EditText countrySearch = (EditText) customLayout.findViewById(R.id.countrySearch);
            ArrayList<CountryItem> countryItemArrayList = new ArrayList<CountryItem>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                CountryItem countryItem = new CountryItem(
                        jsonObject.get("name").toString(),
                        jsonObject.get("dialCode").toString(),
                        jsonObject.get("isoCode").toString(),
                        jsonObject.get("flag").toString());

                countryItemArrayList.add(countryItem);
            }

            CountryCodesListAdapter adapter = new CountryCodesListAdapter(customLayout, customLayout.getContext(), countryItemArrayList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(EnterMobileNumberActivity.this);
            builder.setView(customLayout);
            dialog = builder.create();
            dialog.show();

            countrySearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence newText, int i, int i1, int i2) {
                    adapter.getFilter().filter(newText);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


//        countryCodePickerDialog.show();
//        countryCodePickerDialog.getWindow().setGravity(Gravity.CENTER);
//        countryCodePickerDialog.setContentView(R.layout.country_code_dialog);
//        countryCodePickerDialog.setCanceledOnTouchOutside(true);
//        String countryNumbersJson = loadJSONFromAsset();
//        try {
//            JSONArray jsonArray = new JSONArray(countryNumbersJson);
//            RecyclerView recyclerView = (RecyclerView) countryCodePickerDialog.getWindow().findViewById(R.id.country_recyclerview);
//            EditText countrySearch = (EditText) countryCodePickerDialog.getWindow().findViewById(R.id.countrySearch);
//            ArrayList<CountryItem> countryItemArrayList = new ArrayList<CountryItem>();
//
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                CountryItem countryItem = new CountryItem(
//                        jsonObject.get("name").toString(),
//                        jsonObject.get("dialCode").toString(),
//                        jsonObject.get("isoCode").toString(),
//                        jsonObject.get("flag").toString());
//
//                countryItemArrayList.add(countryItem);
//            }
//
//            CountryCodesListAdapter adapter = new CountryCodesListAdapter(countryCodePickerDialog, countryCodePickerDialog.getContext(), countryItemArrayList);
//            recyclerView.setHasFixedSize(true);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//            recyclerView.setAdapter(adapter);
//            adapter.setOnItemClickListener(EnterMobileNumberActivity.this);
//
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onItemClick(CountryItem countryItem) {
        System.out.println(countryItem);
//        view_country_name.setText("(" + countryItem.getIsoCode() + ")" + "  " + countryItem.getDialCode());
        view_country_name.setText(countryItem.getDialCode());
        Glide.with(getBaseContext()).load(countryItem.getFlag()).into(selected_country_flag);
        country_code = countryItem.getDialCode();
        dialog.dismiss();
    }


    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countrynumbers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void click_GoWithMobileNumber(View v) {

        mobile = txt_moblie_number.getText().toString();

        boolean isValid = false;

//        country_code = ccp.getSelectedCountryCode();


        if (country_code == null) {
            isValid = false;
            Toast.makeText(getApplicationContext(), R.string.toast_enter_valid_phonenumber, Toast.LENGTH_LONG).show();
        }
        if (country_code.equals("")) {
            country_code = "94";
        }


//        country_code = "+" + country_code;


        if (isValidMobile(mobile)) {
            isValid = true;
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_enter_valid_phonenumber, Toast.LENGTH_LONG).show();
        }

        if (isValid) {
            if (Utility.isInternetAvailable(this)) {
                mobileLoginServiceCall();
            } else {
                Toast.makeText(getApplicationContext(), R.string.toast_no_internet, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void mobileLoginServiceCall() {


        if (Utility.isInternetAvailable(EnterMobileNumberActivity.this)) {

            click_GoWithMobileNumberNew();

        } else {

            Toast.makeText(EnterMobileNumberActivity.this, "No active internet connection",
                    Toast.LENGTH_LONG).show();
        }
    }


//    private class Service_userLogout extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            makePostRequest5();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//
//            if (loginStatus != null) {
//                if (loginStatus.equals("0")) {
//
//
//                    Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
//                    in.putExtra("user_id", user_id);
//                    in.putExtra("Name", Name);
//                    in.putExtra("email", email);
//                    in.putExtra("mobile", mobile);
//                    in.putExtra("hashToken", hashToken);
//                    in.putExtra("profile_pic", profile_pic);
//                    in.putExtra("country_code", country_code);
//                    in.putExtra("date_of_birth", dateOfBirth);
//                    in.putExtra("gender", gender);
//                    in.putExtra("code", code);
//
//                    startActivity(in);
//                    finish();
//
//                } else if (loginStatus.equals("11")) {
//                    Toast.makeText(EnterMobileNumberActivity.this, "Login error. Please try later!",
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(EnterMobileNumberActivity.this, "Internal Server Error with code " + loginStatus,
//                            Toast.LENGTH_LONG).show();
//
//                }
//            }
//        }
//
//    }

    private void makePostRequest5() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        pref.setMobileNumber(mobile);
        String hashKey = pref.getLoginUser().get("hashkey");
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        httpPost.setHeader(new BasicHeader("api_key", hashKey));

        String jsonStr =
                "{" +
                        "\"mobile_number\": \"" + mobile + "\"," +
                        "\"country_code\": \"" + country_code + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "loginVerficationWithMobile"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("..............*-----..........................." + nameValuePair.toString());
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        int resCode = 0;
        //making POST request.
        HttpResponse response = null;
        String responseString = null;

        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null) {
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            resCode = response.getStatusLine().getStatusCode();

            if (resCode == 200) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String res = jsonObj.optString("result");

                if (res.equals("0")) {

                    loginStatus = "0";

                    user_id = jsonObj.optString("userId");
                    if (user_id.equals("false")) {
                        user_id = "0";
                    }
                    String fName = jsonObj.optString("first_name");
                    String lName = jsonObj.optString("last_name");

                    String lumen_token = jsonObj.optString("lumen_token");

                    Name = fName + " " + lName;

                    gender = jsonObj.optString("gender");
                    email = jsonObj.optString("email");
                    profile_pic = jsonObj.optString("profile_pic");
                    healthData = jsonObj.optString("healthData");
                    code = jsonObj.optString("code");
                    lumenToken = jsonObj.optString("lumen_token");
                    dateOfBirth = jsonObj.optString("date_of_birth");

                    pref.setUserToken("Bearer " + lumenToken);
                    hashToken = jsonObj.optString("hashToken");


                } else {
                    loginStatus = "11";

                    System.out.println("=========================LOGIN FAIL=========>");
                }


            } else {
                // HTTP Response not 200
                loginStatus = Integer.toString(resCode);
            }


        }


    }


    public void click_GoWithMobileNumberNew() {

//        final ProgressDialog progressDialog = new ProgressDialog(EnterMobileNumberActivity.this);
//        progressDialog.setMessage("Please wait...");
//        progressDialog.show();


        loading.setVisibility(View.VISIBLE);
        pref = new PrefManager(this);
        pref.setMobileNumber(mobile);


//        String serviceName = "loginVerficationWithMobile";
        String serviceName = "loginVerficationWithoutMobile";

        String jsonStr =
                "{" +
                        "\"mobile_number\": \"" + mobile + "\"," +
                        "\"country_code\": \"" + country_code + "\"" +
                        "}";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Object> call = apiService.loginVerficationWithoutMobile(AppConfig.APP_BRANDING_ID, AppConfig.SUGER_API_TOKEN, serviceName, "JSON", "JSON", jsonStr);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
//                progressDialog.cancel();
//                loading.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//
//                    if (response.body() != null) {
//
//                        Object obj = response.body();
//
//                        JSONObject jsonObj = null;
//                        int res = 5;
//                        try {
//                            jsonObj = castObjectToJSON(response);
//                            res = jsonObj.getInt("result");
//
//                            if (res == 0) {
//                                code = jsonObj.optString("code");
//                                user_id = jsonObj.optString("userId");
//                                if (user_id.equals("false")) {
//                                    user_id = "0";
//                                    if (Constants.type == Constants.Type.LIFEPLUS) {
//                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
//                                        in.putExtra("code", code);
//                                        in.putExtra("country_code", country_code);
//                                        in.putExtra("user_status", "false");
//                                        startActivity(in);
//                                        finish();
//                                    }
//                                    if (Constants.type == Constants.Type.AYUBO) {
//                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
//                                        in.putExtra("code", code);
//                                        in.putExtra("country_code", country_code);
//                                        in.putExtra("user_status", "false");
//                                        startActivity(in);
//                                        finish();
//                                    }
//                                    if (Constants.type == Constants.Type.HEMAS) {
//                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
//                                        in.putExtra("code", code);
//                                        in.putExtra("country_code", country_code);
//                                        in.putExtra("user_status", "false");
//                                        startActivity(in);
//                                        finish();
//                                    }
//                                    if (Constants.type == Constants.Type.SHESHELLS) {
//                                        Intent in = new Intent(EnterMobileNumberActivity.this, SeyShellsWelcome.class);
//                                        in.putExtra("code", code);
//                                        in.putExtra("country_code", country_code);
//                                        in.putExtra("user_status", "false");
//                                        startActivity(in);
//                                        finish();
//                                    }
//                                } else {
//                                    String fName = jsonObj.optString("first_name");
//                                    String lName = jsonObj.optString("last_name");
//
//                                    Name = fName + " " + lName;
//                                    gender = jsonObj.optString("gender");
//                                    email = jsonObj.optString("email");
//                                    profile_pic = jsonObj.optString("profile_pic");
//                                    healthData = jsonObj.optString("healthData");
//                                    code = jsonObj.optString("code");
//                                    lumenToken = jsonObj.optString("lumen_token");
//                                    dateOfBirth = jsonObj.optString("date_of_birth");
//                                    pref.setUserToken("Bearer " + lumenToken);
//                                    hashToken = jsonObj.optString("hashToken");
//
//                                    Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
//                                    in.putExtra("user_id", user_id);
//                                    in.putExtra("Name", Name);
//                                    in.putExtra("email", email);
//                                    in.putExtra("mobile", mobile);
//                                    in.putExtra("hashToken", hashToken);
//                                    in.putExtra("profile_pic", profile_pic);
//                                    in.putExtra("country_code", country_code);
//                                    in.putExtra("user_status", "true");
//                                    in.putExtra("code", code);
//                                    in.putExtra("date_of_birth", dateOfBirth);
//                                    in.putExtra("gender", gender);
//                                    startActivity(in);
//                                    finish();
//                                }
//
//                            } else {
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }


                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        Object obj = response.body();

                        JSONObject jsonObj = null;
                        int res = 5;
                        try {
                            jsonObj = castObjectToJSON(response);
                            res = jsonObj.getInt("result");

                            if (res == 0) {
                                code = jsonObj.optString("code");
                                user_id = jsonObj.optString("userId");


                                System.out.println(mobile);
                                System.out.println(country_code);


                                ////////////////// new verification ///////////////////////////

                                String mobileWithCountryCodeTo = country_code + mobile;
                                String mobileWithCountryCodeUserId = mobile;


//                                if (!user_id.equals("false")) {
//                                    mobileWithCountryCodeUserId = user_id;
//                                }

                                JSONObject jsonObjNew = jsonObj;
                                ApiInterface azureApiClientV1 = ApiClient.getAzureApiClientV1().create(ApiInterface.class);

                                Call<ProfileDashboardResponseData> sendVerificationCall = azureApiClientV1.sendVerification(
                                        AppConfig.APP_BRANDING_ID,
                                        new SendVerificationObject(mobileWithCountryCodeTo, "template:ayubo_otp_msg", mobileWithCountryCodeUserId)
                                );


                                sendVerificationCall.enqueue(new Callback<ProfileDashboardResponseData>() {

                                    @Override
                                    public void onResponse(Call<ProfileDashboardResponseData> call, Response<ProfileDashboardResponseData> response) {

                                        loading.setVisibility(View.GONE);

                                        if (response.isSuccessful()) {

                                            assert response.body() != null;
                                            JsonObject sendVerificationCallResponse = new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

                                            if (sendVerificationCallResponse.get("is_success").getAsBoolean()) {
                                                if (user_id.equals("false")) {
                                                    user_id = "0";
                                                    if (Constants.type == Constants.Type.LIFEPLUS) {
                                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
                                                        in.putExtra("code", code);
                                                        in.putExtra("country_code", country_code);
                                                        in.putExtra("mobile", mobile);
                                                        in.putExtra("user_status", "false");
                                                        startActivity(in);
                                                        finish();
                                                    }
                                                    if (Constants.type == Constants.Type.AYUBO) {
                                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
                                                        in.putExtra("code", code);
                                                        in.putExtra("country_code", country_code);
                                                        in.putExtra("mobile", mobile);
                                                        in.putExtra("user_status", "false");
                                                        startActivity(in);
                                                        finish();
                                                    }
                                                    if (Constants.type == Constants.Type.HEMAS) {
                                                        Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
                                                        in.putExtra("code", code);
                                                        in.putExtra("country_code", country_code);
                                                        in.putExtra("mobile", mobile);
                                                        in.putExtra("user_status", "false");
                                                        startActivity(in);
                                                        finish();
                                                    }
                                                    if (Constants.type == Constants.Type.SHESHELLS) {
                                                        Intent in = new Intent(EnterMobileNumberActivity.this, SeyShellsWelcome.class);
                                                        in.putExtra("code", code);
                                                        in.putExtra("country_code", country_code);
                                                        in.putExtra("mobile", mobile);
                                                        in.putExtra("user_status", "false");
                                                        startActivity(in);
                                                        finish();
                                                    }
                                                } else {
                                                    String fName = jsonObjNew.optString("first_name");
                                                    String lName = jsonObjNew.optString("last_name");

                                                    Name = fName + " " + lName;
                                                    gender = jsonObjNew.optString("gender");
                                                    email = jsonObjNew.optString("email");
                                                    profile_pic = jsonObjNew.optString("profile_pic");
                                                    healthData = jsonObjNew.optString("healthData");
                                                    code = jsonObjNew.optString("code");
                                                    lumenToken = jsonObjNew.optString("lumen_token");
                                                    dateOfBirth = jsonObjNew.optString("date_of_birth");
                                                    pref.setUserToken("Bearer " + lumenToken);
                                                    hashToken = jsonObjNew.optString("hashToken");

                                                    Intent in = new Intent(EnterMobileNumberActivity.this, EnterVerificationCodeActivity.class);
                                                    in.putExtra("user_id", user_id);
                                                    in.putExtra("Name", Name);
                                                    in.putExtra("email", email);
                                                    in.putExtra("mobile", mobile);
                                                    in.putExtra("hashToken", hashToken);
                                                    in.putExtra("profile_pic", profile_pic);
                                                    in.putExtra("country_code", country_code);
                                                    in.putExtra("user_status", "true");
                                                    in.putExtra("code", code);
                                                    in.putExtra("date_of_birth", dateOfBirth);
                                                    in.putExtra("gender", gender);
                                                    startActivity(in);
                                                    finish();
                                                }
                                            } else {
                                                try {
                                                    Toast.makeText(getBaseContext(), sendVerificationCallResponse.get("status_message").getAsString(), Toast.LENGTH_SHORT).show();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        } else {
                                            // resend code
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ProfileDashboardResponseData> call, Throwable throwable) {
                                        throwable.printStackTrace();
                                        Toast.makeText(getBaseContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                    }
                                });


                                //////////////////////////////////////////////////////////////////////////


                            } else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
//                progressDialog.cancel();
                loading.setVisibility(View.GONE);
                Toast.makeText(EnterMobileNumberActivity.this, R.string.servcer_not_available, Toast.LENGTH_LONG).show();

            }
        });
    }

    JSONObject castObjectToJSON(Response<Object> response) {

        Gson gson1 = new Gson();
        String jsonCars = gson1.toJson(response);

        JSONObject jresponse = null;
        JSONObject responseBody = null;
        try {
            jresponse = new JSONObject(jsonCars);
            String body = jresponse.getString("body");
            responseBody = new JSONObject(body);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }
}
