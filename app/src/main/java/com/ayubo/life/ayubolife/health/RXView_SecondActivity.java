package com.ayubo.life.ayubolife.health;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.CommonListViewWithSearchActivity;
import com.ayubo.life.ayubolife.activity.CommonListView_ReportType;
import com.ayubo.life.ayubolife.activity.CommonWebViewActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.model.DBString;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import id.zelory.compressor.Compressor;


public class RXView_SecondActivity extends AppCompatActivity {
    String familyDetails, isDeviceIDSet, filePath, image_absolute_path, userid_ExistingUser;
    ProgressDialog prgDialog;
    File compressedImageFile;
    long totalSize = 0;
    ImageView imageView_profile;
    private PrefManager pref;
    ArrayList<String> familyMemberList = null;
    ArrayList<DBString> familyMemberList_DBString = null;
    ArrayList<DBString> reportTypesList_DBString = null;
    ArrayList<String> reportTypesList = null;
    EditText txt_report_type, txt_report_of, txt_instition, txt_comments;
    String report_of, report_type;
    String memberName;

    String sComment, report_Id;
    String sInstition;
    ImageButton btn_backImgBtn;

    //  JSONObject familyDetails;
    public void clickAddMember(View v) {
        String mainPath = ApiClient.BASE_URL;
        String url = ApiClient.BASE_URL + "index.php?module=User_FamilyUHID&action=setFamilyMember&ref=";
        Intent intent = new Intent(getBaseContext(), CommonWebViewActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_upload_prescription_sec);
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pref = new PrefManager(RXView_SecondActivity.this);

        userid_ExistingUser = pref.getLoginUser().get("uid");
        image_absolute_path = Ram.getImageAbsoulutePath();


        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);
        txt_report_of = (EditText) findViewById(R.id.txt_report_of);
        txt_report_type = (EditText) findViewById(R.id.txt_report_type);
        txt_instition = (EditText) findViewById(R.id.txt_instition);
        txt_comments = (EditText) findViewById(R.id.txt_comments);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(image_absolute_path, options);
        imageView_profile.setImageBitmap(bitmap);

        txt_report_type.setInputType(InputType.TYPE_NULL);
        txt_report_type.setTextIsSelectable(true);

        txt_report_of.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RXView_SecondActivity.this, CommonListViewWithSearchActivity.class);
                startActivity(in);

            }
        });
        txt_report_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(RXView_SecondActivity.this, CommonListView_ReportType.class);
                startActivity(in);

            }
        });

//        txt_report_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    Intent in = new Intent(RXView_SecondActivity.this, CommonListView_ReportType.class);
//                    startActivity(in);
//                }
//            }
//        });
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("reporttype");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;
            reportTypesList = new ArrayList<String>();
            reportTypesList_DBString = new ArrayList<DBString>();

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                int formula_value = jo_inside.getInt("ServiceItemID");
                String url_value = jo_inside.getString("ReportName");
                System.out.println("====Type==============" + url_value);
                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                //   m_li.put("formule", formula_value);
                //   m_li.put("url", url_value);
                DBString item = new DBString(Integer.toString(formula_value), url_value);
                reportTypesList.add(url_value);
                reportTypesList_DBString.add(item);

                //  formList.add(m_li);
            }
            Ram.setAvailableReportTypeList(reportTypesList);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        System.out.println("======path Absu===============" + image_absolute_path);
    }

    public void sendUplodDetails(View v) {

        report_of = txt_report_of.getText().toString();
        if (report_of == null || report_of.isEmpty() || report_of.equals("")) {
            Toast.makeText(getApplicationContext(), "Please select report of", Toast.LENGTH_LONG).show();
            return;
        }
        report_type = txt_report_type.getText().toString();

        if (report_type == null || report_type.isEmpty() || report_type.equals("")) {
            Toast.makeText(getApplicationContext(), "Please select report type", Toast.LENGTH_LONG).show();
            return;
        }

        sComment = txt_comments.getText().toString();
        sInstition = txt_instition.getText().toString();


        for (int i = 0; i < reportTypesList.size(); i++) {
            DBString rep = reportTypesList_DBString.get(i);
            if (rep.getName().equals(report_type)) {
                report_Id = rep.getId();
            }
        }
        for (int i = 0; i < familyMemberList_DBString.size(); i++) {
            DBString rep = familyMemberList_DBString.get(i);
            if (rep.getName().equals(report_of)) {
                report_of = rep.getId();
            }
        }


        uploadProfileImageAsFile();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Service_getFamilyDetails_ServiceCall();


    }

    private void uploadProfileImageAsFile() {
        try {
            if (Utility.isInternetAvailable(RXView_SecondActivity.this)) {
                try {

                    if (image_absolute_path == null) {
                        Toast.makeText(RXView_SecondActivity.this, "Error accessing image file",
                                Toast.LENGTH_LONG).show();
                    } else {
                        uploadProfileImage();
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                Toast.makeText(RXView_SecondActivity.this, "No internet connection. Can't upload profile image. ",
                        Toast.LENGTH_LONG).show();
                // finish();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void uploadProfileImage() {
        prgDialog = new ProgressDialog(RXView_SecondActivity.this);
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            File sourceFile = new File(image_absolute_path);
            compressedImageFile = sourceFile;
//            try {
//                compressedImageFile = new Compressor(RXView_SecondActivity.this).compressToFile(sourceFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            prgDialog.setMessage("Uploading...");
            prgDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            String uu = ApiClient.BASE_URL_entypoint + "uploadReportPicture";

            HttpPost httppost = new HttpPost(uu);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                try {

                    // Adding file data to http body
                    entity.addPart("picture_file", new FileBody(compressedImageFile));
                    entity.addPart("userid", new StringBody(userid_ExistingUser));
                    entity.addPart("reportof", new StringBody(report_of));
                    entity.addPart("report_id", new StringBody(report_Id));
                    entity.addPart("report_name", new StringBody(report_type));
                    entity.addPart("comments", new StringBody(sComment));
                    entity.addPart("insitution", new StringBody(sInstition));
                } catch (Exception e) {

                }

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = null;
                int statusCode = 0;
                HttpEntity r_entity = null;
                try {
                    // Making server call
                    response = httpclient.execute(httppost);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    r_entity = response.getEntity();
                    r_entity.toString();
                    System.out.println("............................." + r_entity.toString());
                    statusCode = response.getStatusLine().getStatusCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (statusCode == 200) {

                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);
                    System.out.println("============================" + responseString);
                } else {
                    responseString = "Server error,Please try again later";
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("..........Upload Image Service...Result................"+responseString);
            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            prgDialog.cancel();
            String res = null;
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (res.equals("0")) {
                Toast.makeText(RXView_SecondActivity.this, "Uploaded successfully",
                        Toast.LENGTH_LONG).show();
                Intent in = new Intent(RXView_SecondActivity.this, Health_MainActivity.class);
                startActivity(in);
                finish();
            } else {
                Toast.makeText(RXView_SecondActivity.this, "Error uploading image",
                        Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(result);


        }

    }


    private void Service_getFamilyDetails_ServiceCall() {

        if (Utility.isInternetAvailable(RXView_SecondActivity.this)) {

            Service_getFamilyDetails task = new Service_getFamilyDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        } else {
            //   textt.setText("Unable to detect an active internet connection");

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

            if (isDeviceIDSet.equals("0")) {

                JSONArray myDataListsAll = null;
                try {
                    myDataListsAll = new JSONArray(familyDetails);
                    familyMemberList = new ArrayList<String>();
                    familyMemberList_DBString = new ArrayList<DBString>();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String sNamee = pref.getLoginUser().get("name");
                userid_ExistingUser = pref.getLoginUser().get("uid");

                DBString itemFirst = new DBString(userid_ExistingUser, sNamee);
                familyMemberList_DBString.add(itemFirst);
                familyMemberList.add(new String(sNamee));

                for (int i = 0; i < myDataListsAll.length(); i++) {
                    String memberId = null;
                    String memberName = null;
                    JSONObject jsonMainNode3 = null;
                    try {
                        jsonMainNode3 = (JSONObject) myDataListsAll.get(i);
                        memberId = jsonMainNode3.optString("id").toString();
                        memberName = jsonMainNode3.optString("name").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("=====================");
                    System.out.println("=====Family================" + memberId);
                    System.out.println("=====Family================" + memberName);
                    DBString item = new DBString(memberId, memberName);
                    familyMemberList_DBString.add(item);
                    familyMemberList.add(new String(memberName));

                }
                Ram.setAvailableFmilyMemberList(familyMemberList);

                String memName = Ram.getFamilyMemberName();
                if (memName == null) {
                } else {
                    txt_report_of.setText(memName);
                }

                String type = Ram.getReportType();
                if (type == null) {
                } else {
                    txt_report_type.setText(type);
                }
            }
            if (familyMemberList.size() == 0) {
                String sName = pref.getLoginUser().get("name");
                familyMemberList.add(new String(sName));
                Ram.setAvailableFmilyMemberList(familyMemberList);
            }


            System.out.println("=====================");

        }
    }

    private void getFamilyDetails() {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String email, password;

        String jsonStr =
                "{" +

                        "\"userID\": \"" + userid_ExistingUser + "\"" +
                        "}";

        nameValuePair.add(new BasicNameValuePair("method", "getUserFamily"));
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
        isDeviceIDSet = "99";
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


            String res = jsonObj.optString("result").toString();
            String data = jsonObj.optString("data").toString();
            int result = Integer.parseInt(res);


            System.out.println("==========================Result===============>" + result);
            if (result == 11) {
                isDeviceIDSet = "11";
            } else if (result == 0) {
                isDeviceIDSet = "0";
                familyDetails = data;
            } else if (result == 1) {
                isDeviceIDSet = "1";
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

    //=====================================
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("reporttype.json");
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


}


