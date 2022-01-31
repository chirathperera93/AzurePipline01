package com.ayubo.life.ayubolife.map_challenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.rest.ApiClient;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ChallengeCompletedView_Activity extends AppCompatActivity {
    ProgressDialog prgDialog;
    PrefManager pref;
    WebView webView;
    ProgressDialog web_prgDialog;
    String userid_ExistingUser, setDeviceID_Success, today, serviceDataStatus, challenge_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_challenge_completed_view_);

        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");

        prgDialog = new ProgressDialog(ChallengeCompletedView_Activity.this);
        challenge_id = getIntent().getStringExtra("challenge_id");
        //  total_steps = getIntent().getStringExtra("steps");


        Button btn_backImgBtn = (Button) findViewById(R.id.btn_chcom_backImgBtn);
        ImageButton btn_imageButton_GoMap = (ImageButton) findViewById(R.id.btn_imageButton_GoMap);

        btn_imageButton_GoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  finish();
                Service_Service_getStepsData_ServiceCall();
            }
        });


        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void Service_Service_getStepsData_ServiceCall() {

        if (Utility.isInternetAvailable(ChallengeCompletedView_Activity.this)) {

            prgDialog.show();
            prgDialog.setMessage("Loading..");
            prgDialog.setCancelable(false);
            Service_getStepsData task = new Service_getStepsData();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
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

            prgDialog.cancel();

            Intent intent = new Intent(ChallengeCompletedView_Activity.this, NewHomeWithSideMenuActivity.class);
            startActivity(intent);

        }
    }

    private void makeSetDefaultDevice() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String jsonStr =
                "{" +
                        "\"userid\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "completeAdventureChallengeViewCalled"));
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
                String res = jsonObj.optString("result").toString();

                int result = Integer.parseInt(res);

                if (result == 0) {
                    System.out.println("========Service Calling============" + res);
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


}
