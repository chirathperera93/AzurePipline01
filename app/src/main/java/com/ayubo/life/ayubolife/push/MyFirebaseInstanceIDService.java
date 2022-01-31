package com.ayubo.life.ayubolife.push;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ayubo.life.ayubolife.rest.ApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

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

import static com.ayubo.life.ayubolife.push.Config.TOPIC_AYUBO;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    String token_id;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_AYUBO);
    }

    private void sendRegistrationToServer(final String token) {
        token_id = token;
        //sending gcm token to server

        int SPLASH_TIME_OUT_Device_ID = 20000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Service_Device_ID_ServiceCall();
            }
        }, SPLASH_TIME_OUT_Device_ID);

        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void Service_Device_ID_ServiceCall() {

        Service_set_Device_ID task = new Service_set_Device_ID();
        task.execute(new String[]{ApiClient.BASE_URL_live});

    }

    private class Service_set_Device_ID extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            send_Device_ID();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //  prgDialog.cancel();
        }
    }

    private void send_Device_ID() {

        SharedPreferences pref = this.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        String userid_ExistingUser = pref.getString("uid", "0");

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"deviceID\": \"" + token_id + "\"," +
                        "\"voipId\": \"" + "" + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "setMyNewDeviceID"));
        nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
        nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));


        System.out.println("............setMyNewDeviceID....in F Service..............." + nameValuePair.toString());
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
            System.out.println("..........response..........." + response);

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
            String number = jsonObj.optString("number").toString();

            if (number.isEmpty()) {
                String res = jsonObj.optString("result").toString();
                int result = Integer.parseInt(res);
                if (result == 11) {

                }
                if (result == 0) {
                    System.out.println("===============sent RegistrationToServer===>" + token_id);

                }

            } else {

            }


        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}

