package com.ayubo.life.ayubolife.health;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.insurances.Adapters.UploadDocumentAdapter;
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.CameraUtils;
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
import java.util.List;

import static com.ayubo.life.ayubolife.health.Medicine_ViewActivity.BITMAP_SAMPLE_SIZE;

public class MedicineView_PaymentActivity extends AppCompatActivity {
    String paymentMethod, address;
    String familyDetails, isDeviceIDSet, filePath, image_absolute_path, userid_ExistingUser;
    ProgressDialog prgDialog;
    File compressedImageFile;
    long totalSize = 0;
    ImageView imageView_profile;
    private PrefManager pref;
    ArrayList<String> familyMemberList = null;
    ArrayList<String> reportTypesList = null;
    EditText txt_address;
    EditText txt_add_notes;
    String memberName;
    ImageButton btn_backImgBtn;
    boolean checked;
    String fromActivity, media_url;
    WebView webview = null;
    ImageView imageView_file;
    RelativeLayout lay_medicine;
    Button btn_download_pdf;
    boolean isChat = false;
    String medicineNotes = "";

    List<String> array_image_absolute_path = new ArrayList<String>();
    List<File> compressedImageFileArray = new ArrayList<File>();

    LinearLayout medicine_upload_document_recycler_view_layout;

    private ArrayList<UploadDocumentItem> mUploadDocumentList;
    private UploadDocumentAdapter mUploadDocumentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_for_prescription);


        fromActivity = getIntent().getStringExtra("from");

        if (fromActivity.equals("chat")) {
            isChat = true;
            media_url = getIntent().getStringExtra("media_url");
        }


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        medicine_upload_document_recycler_view_layout = findViewById(R.id.medicine_upload_document_recycler_view_layout);
        medicine_upload_document_recycler_view_layout.setVisibility(View.GONE);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pref = new PrefManager(MedicineView_PaymentActivity.this);
        prgDialog = new ProgressDialog(MedicineView_PaymentActivity.this);

        userid_ExistingUser = pref.getLoginUser().get("uid");

        imageView_file = findViewById(R.id.imageView_file);
        lay_medicine = findViewById(R.id.lay_medicine);
        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);
        btn_download_pdf = findViewById(R.id.btn_download_pdf);
        mUploadDocumentList = new ArrayList<>();
        buildRecyclerView();

        if (isChat) {

            if (media_url.contains("pdf")) {
                btn_download_pdf.setText("View Prescription");
            } else {
                btn_download_pdf.setText("View Prescription");
            }
            btn_download_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  media_url= "http://www.pdf995.com/samples/pdf.pdf";

                    if (media_url.contains("pdf")) {
                        Uri myUri = Uri.parse(media_url);
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(myUri, "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        startActivity(intent);
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(media_url));
                        startActivity(browserIntent);
                    }

                }
            });
        }

        if ((fromActivity != null) && (fromActivity.equals("chat"))) {

            media_url = getIntent().getStringExtra("media_url");
            imageView_profile.setVisibility(View.INVISIBLE);

            btn_download_pdf.setVisibility(View.VISIBLE);
            imageView_file.setVisibility(View.VISIBLE);

        } else {
            array_image_absolute_path = new ArrayList<String>();
            if (Ram.getMultipleImageAbsolutePath() != null && Ram.getMultipleImageAbsolutePath().size() > 0) {
                medicine_upload_document_recycler_view_layout.setVisibility(View.VISIBLE);
                imageView_profile.setVisibility(View.GONE);
                lay_medicine.setVisibility(View.GONE);
                array_image_absolute_path = Ram.getMultipleImageAbsolutePath();

                for (int i = 0; i < array_image_absolute_path.size(); i++) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, array_image_absolute_path.get(i));

                    insertItem(0, bitmap, null, null, null, null);
                }


            } else {
                medicine_upload_document_recycler_view_layout.setVisibility(View.GONE);
                image_absolute_path = Ram.getImageAbsoulutePath();
                imageView_profile.setVisibility(View.VISIBLE);
                imageView_file.setVisibility(View.INVISIBLE);
                btn_download_pdf.setVisibility(View.INVISIBLE);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, image_absolute_path);
                imageView_profile.setImageBitmap(bitmap);
            }


        }

        txt_address = (EditText) findViewById(R.id.txt_address);
        txt_add_notes = (EditText) findViewById(R.id.txt_add_notes);
        txt_address.setInputType(InputType.TYPE_CLASS_TEXT);
        txt_address.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void buildRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.medicine_upload_document_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mUploadDocumentAdapter = new UploadDocumentAdapter(mUploadDocumentList, getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mUploadDocumentAdapter);
        mUploadDocumentAdapter.setOnItemClickListener(new UploadDocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                if (mUploadDocumentList.size() == 0) {
                    medicine_upload_document_recycler_view_layout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void removeItem(int position) {
        mUploadDocumentList.remove(position);
        array_image_absolute_path.remove(position);
        mUploadDocumentAdapter.notifyItemRemoved(position);

        if (mUploadDocumentList.size() == 0) {
            medicine_upload_document_recycler_view_layout.setVisibility(View.GONE);
        }
    }

    public void insertItem(int position, Bitmap imageView, String imgUrl, String mediaName, String mediaFolder, String mediaType) {
        mLayoutManager.scrollToPosition(0);
        mUploadDocumentList.add(position, new UploadDocumentItem(imgUrl, mediaName, mediaFolder, mediaType, ""));
        mUploadDocumentAdapter.notifyItemInserted(position);
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();
        System.out.println("=============onRadioButtonClicked===================" + checked);
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_bycash:
                if (checked)
                    paymentMethod = "cash";
                break;
            case R.id.radio_bycard:
                if (checked)
                    paymentMethod = "card";
                break;
        }
    }


    public void sendUplodDetails(View v) {

        //  FirebaseAnalytics Adding
        if (SplashScreen.firebaseAnalytics != null) {
            SplashScreen.firebaseAnalytics.logEvent("Prescription_uploaded", null);
        }

        address = txt_address.getText().toString();
        medicineNotes = txt_add_notes.getText().toString();
        if (address == null || address.isEmpty() || address.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter delivery address", Toast.LENGTH_LONG).show();
            return;
        }
        if (!checked) {
            Toast.makeText(getApplicationContext(), "Please select payment method", Toast.LENGTH_LONG).show();
            System.out.println("=============onRadioButton========= != ==========" + checked);
            return;

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MedicineView_PaymentActivity.this);
            LayoutInflater inflater = (LayoutInflater) MedicineView_PaymentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layoutView = inflater.inflate(R.layout.confirm_order_alert, null, false);
            builder.setView(layoutView);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    uploadProfileImageAsFile();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    private void uploadProfileImageAsFile() {

        try {

            if (Utility.isInternetAvailable(MedicineView_PaymentActivity.this)) {

                try {


                    if (isChat) {
                        uploadProfileImage();

                    } else {
                        if (array_image_absolute_path.size() == 0 && image_absolute_path == null) {
                            Toast.makeText(MedicineView_PaymentActivity.this, "Error accessing image file",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            uploadProfileImage();

                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                Toast.makeText(MedicineView_PaymentActivity.this, "No internet connection. Can't upload profile image. ",
                        Toast.LENGTH_LONG).show();
                // finish();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void uploadProfileImage() {
        try {
            new UploadFileToServer().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            prgDialog.setMessage("Prescription uploading...");
            prgDialog.setCancelable(false);
            prgDialog.show();

            if (isChat) {

            } else {
                compressedImageFileArray = new ArrayList<File>();
                if (array_image_absolute_path != null && array_image_absolute_path.size() > 0) {

                    for (int i = 0; i < array_image_absolute_path.size(); i++) {
                        File sourceFile = new File(array_image_absolute_path.get(i));
                        compressedImageFileArray.add(sourceFile);

//                        try {
//                            File compressedImageFile = new Compressor(MedicineView_PaymentActivity.this).compressToFile(sourceFile);
//                            compressedImageFileArray.add(compressedImageFile);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }


                } else {
                    File sourceFile = new File(image_absolute_path);
                    compressedImageFile = sourceFile;
//                    try {
//                        compressedImageFile = new Compressor(MedicineView_PaymentActivity.this).compressToFile(sourceFile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }


            }


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {

            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();

            String uu = ApiClient.BASE_URL_entypoint + "uploadPrescription";
            HttpPost httppost = new HttpPost(uu);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body
                if (isChat) {
                    entity.addPart("userid", new StringBody(userid_ExistingUser));
                    entity.addPart("address", new StringBody(address));
                    entity.addPart("payment_type", new StringBody(paymentMethod));
                    entity.addPart("link", new StringBody(media_url));
                } else {
                    entity.addPart("userid", new StringBody(userid_ExistingUser));
                    entity.addPart("address", new StringBody(address));
                    entity.addPart("payment_type", new StringBody(paymentMethod));
                    entity.addPart("note", new StringBody(medicineNotes));

                    if (compressedImageFileArray != null && compressedImageFileArray.size() > 0) {

                        for (int i = 0; i < compressedImageFileArray.size(); i++) {
                            entity.addPart("prescription", new FileBody(compressedImageFileArray.get(i)));
                        }

                    } else {
                        entity.addPart("prescription", new FileBody(compressedImageFile));
                    }


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


                AlertDialog.Builder builder = new AlertDialog.Builder(MedicineView_PaymentActivity.this);
                LayoutInflater inflater = (LayoutInflater) MedicineView_PaymentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflater.inflate(R.layout.successfully_alert, null, false);
                builder.setView(layoutView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MedicineView_PaymentActivity.this, "Prescription successfully uploaded",
                                Toast.LENGTH_LONG).show();
//                        Intent in = new Intent(MedicineView_PaymentActivity.this, NewHomeWithSideMenuActivity.class);
//                        Intent in = new Intent(MedicineView_PaymentActivity.this, LifePlusProgramActivity.class);
//                        Intent in = new Intent(MedicineView_PaymentActivity.this, NewDiscoverActivity.class);
                        Intent in = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                        startActivity(in);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            } else {
                Toast.makeText(MedicineView_PaymentActivity.this, "Error submiting prescription",
                        Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(result);


        }

    }


    private void Service_getFamilyDetails_ServiceCall() {

        if (Utility.isInternetAvailable(MedicineView_PaymentActivity.this)) {

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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

                    familyMemberList.add(new String(memberName));
                }

            }
            Ram.setAvailableFmilyMemberList(familyMemberList);
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


