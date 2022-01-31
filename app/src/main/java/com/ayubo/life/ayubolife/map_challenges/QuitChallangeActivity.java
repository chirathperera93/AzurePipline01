package com.ayubo.life.ayubolife.map_challenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class QuitChallangeActivity extends AppCompatActivity {
    ProgressDialog prgDialog;
    ImageButton btn_backImgBtn;
    String url, chid;
    ImageLoader imageLoader;
    String userid_ExistingUser, setDeviceID_Success, today, serviceDataStatus, challenge_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quit_challange);

        prgDialog = new ProgressDialog(QuitChallangeActivity.this);
        prgDialog.setMessage("Please wait...");
        ProgressBar progressNewsList = (ProgressBar) findViewById(R.id.progressNewsList);
        NetworkImageView bg_image = (NetworkImageView) findViewById(R.id.main_bg_banner);


        userid_ExistingUser = getIntent().getStringExtra("user");
        challenge_id = getIntent().getStringExtra("challenge_id");


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);

        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        String imagePath = ApiClient.BASE_URL + "custom/include/images/w2k/" + challenge_id + ".png";


        if (imageLoader == null)
            imageLoader = App.getInstance().getImageLoader();
        bg_image.setImageUrl(imagePath, imageLoader);
        loadImage(imagePath, bg_image, progressNewsList);


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


    public void leaveChallengeNo(View v) {

        finish();

    }

    public void leaveChallenge(View v) {
        Service_LeaveAdventrueChallenge_ServiceCall();
    }

    private void Service_LeaveAdventrueChallenge_ServiceCall() {

        if (Utility.isInternetAvailable(QuitChallangeActivity.this)) {

            prgDialog.show();
            // prgDialog.setMessage("Leaving challenge..");
            prgDialog.setCancelable(false);
            Service_LeaveAdventrueChallenge task = new Service_LeaveAdventrueChallenge();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
        }
    }

    private class Service_LeaveAdventrueChallenge extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            makeLeaveChallenge();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            prgDialog.cancel();

            if (serviceDataStatus != null) {
                if (serviceDataStatus.equals("0")) {
//                    Intent intent = new Intent(QuitChallangeActivity.this, LifePlusProgramActivity.class);
//                    Intent intent = new Intent(QuitChallangeActivity.this, NewDiscoverActivity.class);
                    Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                    startActivity(intent);
                } else {
//                    Toast.makeText(getApplicationContext(), "Leaving Challenge Error",
//                            Toast.LENGTH_LONG).show();
                }
            }


        }
    }

    private void makeLeaveChallenge() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();


        String jsonStr =
                "{" +
                        "\"userID\": \"" + userid_ExistingUser + "\"," +
                        "\"challenge_id\": \"" + challenge_id + "\"" +
                        "}";

        //   challenge_id

        nameValuePair.add(new BasicNameValuePair("method", "leave_adventure_challenge"));
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
            if (response != null) {

                String responseString = null;
                try {
                    responseString = EntityUtils.toString(response.getEntity());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseString);
                    String res = jsonObj.optString("result");
                    int result = Integer.parseInt(res);

                    if (result == 0) {
                        serviceDataStatus = "0";
                    } else {
                        serviceDataStatus = "99";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
