package com.ayubo.life.ayubolife.insurances.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.ClaimUploadPointsAdapter;
import com.ayubo.life.ayubolife.insurances.Adapters.UploadDocumentAdapter;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.insurances.Classes.MyTaskParams;
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaim;
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage;
import com.ayubo.life.ayubolife.insurances.Classes.RequestRemoveFileClaimImage;
import com.ayubo.life.ayubolife.insurances.GenericItems.ClaimUploadItem;
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.insurances.InsurancePrefManager;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClaimUploadDocumentActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ArrayList<UploadDocumentItem> mUploadDocumentList;
    private UploadDocumentAdapter mUploadDocumentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinearLayout claimUploadDocumentRecyclerViewLayout;
    private LinearLayout activityClaimUploadDocumentBackLayout;

    private Button activityClaimUploadDocumentSubmitBtn;

    String selectedClaimFileId, selectedClaimFileHeader;

    PrefManager prefManager;

    EditText activityClaimUploadDocumentAddNotes;

    ProgressAyubo activityClaimUploadDocumentProgressBar;

    public ArrayList<String> upLoadedImageList;

    public ArrayList<RequestFileClaimImage> imageArr;

    InsurancePrefManager insurancePrefManager;

    JSONObject selectedJsonObject;

    RecyclerView claim_upload_points_recycler_view;

    ProgressDialog progressDialog;

    private ClaimUploadPointsAdapter claimUploadPointsAdapter;

    TextView activity_claim_upload_document_detail4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_upload_document);

        prefManager = new PrefManager(this);
        insurancePrefManager = new InsurancePrefManager(this);

        LinearLayout activityClaimUploadDocumentDetail2Layout = findViewById(R.id.activity_claim_upload_document_detail2_layout);
        activityClaimUploadDocumentBackLayout = findViewById(R.id.activity_claim_upload_document_back_layout);
        activityClaimUploadDocumentSubmitBtn = findViewById(R.id.activity_claim_upload_document_submit_btn);
        activityClaimUploadDocumentAddNotes = findViewById(R.id.activity_claim_upload_document_add_notes);
        activityClaimUploadDocumentProgressBar = findViewById(R.id.activity_claim_upload_document__progress_bar);
        activity_claim_upload_document_detail4 = findViewById(R.id.activity_claim_upload_document_detail4);

        JSONObject obj = new JSONObject(insurancePrefManager.getSelectedClaimTypeDetail());

        try {
            selectedJsonObject = new JSONObject((String) obj.get("selected_claim_type"));

            if (selectedJsonObject.get("ct_type").equals("critical")) {
                activityClaimUploadDocumentDetail2Layout.setVisibility(View.GONE);
            }

            activity_claim_upload_document_detail4.setText(selectedJsonObject.get("ct_instructions").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        createUploadDocumentList();
        buildRecyclerView();
        setButtons();
    }

    public void createUploadDocumentList() {
        mUploadDocumentList = new ArrayList<>();
    }

    public void buildRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.activity_claim_upload_document_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mUploadDocumentAdapter = new UploadDocumentAdapter(mUploadDocumentList, getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mUploadDocumentAdapter);
        mUploadDocumentAdapter.setOnItemClickListener(new UploadDocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                if (mUploadDocumentList.size() == 0) {
                    claimUploadDocumentRecyclerViewLayout.setVisibility(View.GONE);
                }
            }
        });


        claim_upload_points_recycler_view = findViewById(R.id.claim_upload_points_recycler_view);
        claim_upload_points_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        try {
            ArrayList<ClaimUploadItem> claimUploadItemList = new ArrayList<ClaimUploadItem>();
            JSONArray jArray = selectedJsonObject.getJSONArray("ct_bulletpoints");
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    ClaimUploadItem claimUploadItem = new ClaimUploadItem(jArray.getString(i));
                    claimUploadItemList.add(claimUploadItem);
                }
            }

            claimUploadPointsAdapter = new ClaimUploadPointsAdapter(this, claimUploadItemList);
            claim_upload_points_recycler_view.setAdapter(claimUploadPointsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


//


    }

    public void setButtons() {
        ImageView buttonGalleryImageInsert = findViewById(R.id.activity_claim_upload_get_from_gallery);
        ImageView buttonCameraImageInsert = findViewById(R.id.activity_claim_upload_document_take_a_snap);
        claimUploadDocumentRecyclerViewLayout = findViewById(R.id.activity_claim_upload_document_recycler_view_layout);

        buttonGalleryImageInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ClaimUploadDocumentActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ClaimUploadDocumentActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        buttonCameraImageInsert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!checkPermissions(ClaimUploadDocumentActivity.this)) {
                    ActivityCompat.requestPermissions(ClaimUploadDocumentActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


        activityClaimUploadDocumentBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityClaimUploadDocumentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClaimUploadDocumentProgressBar.setVisibility(View.VISIBLE);


                if (upLoadedImageList.size() > 0 || !activityClaimUploadDocumentAddNotes.getText().toString().equals("")) {
                    CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
                    Call<InsuranceResponseDataObj> createInsuranceFileClaimRequestCall = null;


                    try {
                        createInsuranceFileClaimRequestCall = commonApiInterface.createInsuranceFileClaim(
                                AppConfig.APP_BRANDING_ID,
                                prefManager.getUserToken(),
                                new RequestFileClaim(
                                        selectedClaimFileHeader,
                                        selectedJsonObject.get("ct_name").toString(),
                                        upLoadedImageList,
                                        "",
                                        "",
                                        selectedJsonObject.get("ct_status").toString(),
                                        activityClaimUploadDocumentAddNotes.getText().toString())
                        );

                        if (Constants.type == Constants.Type.HEMAS) {
                            createInsuranceFileClaimRequestCall = commonApiInterface.createInsuranceFileClaimForHemas(
                                    AppConfig.APP_BRANDING_ID,
                                    prefManager.getUserToken(),
                                    new RequestFileClaim(
                                            selectedClaimFileHeader,
                                            selectedJsonObject.get("ct_name").toString(),
                                            upLoadedImageList,
                                            "",
                                            "",
                                            selectedJsonObject.get("ct_status").toString(),
                                            activityClaimUploadDocumentAddNotes.getText().toString())
                            );
                        }


                        createInsuranceFileClaimRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

                            @Override
                            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                                activityClaimUploadDocumentProgressBar.setVisibility(View.GONE);
                                activityClaimUploadDocumentAddNotes.getText().clear();
                                mUploadDocumentList = new ArrayList<>();
                                upLoadedImageList = new ArrayList<String>();
                                if (response.isSuccessful()) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Uploaded document successfully", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent = new Intent(ClaimUploadDocumentActivity.this, InsuranceOverviewActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong try again", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                                activityClaimUploadDocumentProgressBar.setVisibility(View.GONE);
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    activityClaimUploadDocumentProgressBar.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(getApplicationContext(), "You have to add either images or note", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void changeItem(int position, String text) {
        mUploadDocumentAdapter.notifyItemChanged(position);
    }

    public void removeItem(int position) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Removing image...");
        progressDialog.show();
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);


        Call<InsuranceResponseDataObj> removeFileClaimImagesRequestCall = commonApiInterface.removeFileClaimImages(
                AppConfig.APP_BRANDING_ID,
                prefManager.getUserToken(),
                new RequestRemoveFileClaimImage(mUploadDocumentList.get(position).getImgUrl(), mUploadDocumentList.get(position).getMediaName(), mUploadDocumentList.get(position).getMediaFolder())
        );


        if (Constants.type == Constants.Type.HEMAS) {
            removeFileClaimImagesRequestCall = commonApiInterface.removeFileClaimImagesForHemas(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.getUserToken(),
                    new RequestRemoveFileClaimImage(mUploadDocumentList.get(position).getImgUrl(), mUploadDocumentList.get(position).getMediaName(), mUploadDocumentList.get(position).getMediaFolder())
            );
        }

        removeFileClaimImagesRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    for (Object str : upLoadedImageList) {
                        if (str.equals(mUploadDocumentList.get(position).getImgUrl())) {
                            upLoadedImageList.remove(str);
                        }
                    }
                    mUploadDocumentList.remove(position);
                    mUploadDocumentAdapter.notifyItemRemoved(position);
                    Toast toast = Toast.makeText(getApplicationContext(), "Image removed successfully", Toast.LENGTH_SHORT);
                    toast.show();
                }

                if (mUploadDocumentList.size() == 0) {
                    claimUploadDocumentRecyclerViewLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(), "Server error can't delete image", Toast.LENGTH_SHORT);
                toast.show();
                if (mUploadDocumentList.size() == 0) {
                    claimUploadDocumentRecyclerViewLayout.setVisibility(View.GONE);
                }
            }
        });


    }

    public void insertItem(int position, Bitmap imageView, String imgUrl, String mediaName, String mediaFolder, String mediaType) {
        mLayoutManager.scrollToPosition(0);
        mUploadDocumentList.add(position, new UploadDocumentItem(imgUrl, mediaName, mediaFolder, mediaType, ""));
        mUploadDocumentAdapter.notifyItemInserted(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final List<Bitmap> bitmapArrayList = new ArrayList<>();
        imageArr = new ArrayList<>();
        upLoadedImageList = new ArrayList<String>();

        if (data != null) {
            ClipData clipData = data.getClipData();
            if (requestCode == 1 && resultCode == RESULT_OK) {
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ContentResolver contentResolver = getContentResolver();
                            MimeTypeMap mime = MimeTypeMap.getSingleton();
                            String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                            String mImE = contentResolver.getType(imageUri);
                            String imageBase64String = getBaseString(bitmap);
                            imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                            bitmapArrayList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Uri imageUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ContentResolver contentResolver = getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                        String mImE = contentResolver.getType(imageUri);
                        String imageBase64String = getBaseString(bitmap);
                        imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                        bitmapArrayList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(ClaimUploadDocumentActivity.this, photo);
                ContentResolver contentResolver = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                String mImE = contentResolver.getType(imageUri);
                String imageBase64String = getBaseString(photo);
                imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                bitmapArrayList.add(photo);
            }

            progressDialog = new ProgressDialog(ClaimUploadDocumentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            new UploadImageToServer().execute(0);

            if (bitmapArrayList.size() > 0) {
                claimUploadDocumentRecyclerViewLayout.setVisibility(View.VISIBLE);
            }

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (final Bitmap bitmap : bitmapArrayList) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                ImageView image = new ImageView(ClaimUploadDocumentActivity.this);
////                                image.setImageBitmap(bitmap);
////                                insertItem(0, bitmap);
//                            }
//                        });
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();


        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasPermissionInManifest(ClaimUploadDocumentActivity.this, android.provider.MediaStore.ACTION_IMAGE_CAPTURE) && requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ClaimUploadDocumentActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null && declaredPermissions.length > 0) {
                for (String p : declaredPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private class UploadImageToServer extends AsyncTask<Integer, String, Void> {
        int count;
        ProgressAyubo activityClaimUploadDocumentProgressBar;

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////            activityClaimUploadDocumentProgressBar = new ProgressAyubo(ClaimUploadDocumentActivity.this);
////            activityClaimUploadDocumentProgressBar = findViewById(R.id.activity_claim_upload_document__progress_bar);
////            activityClaimUploadDocumentProgressBar.setVisibility(View.VISIBLE);
//        }


        @Override
        protected Void doInBackground(Integer... params) {
            count = params[0];
            if (count < imageArr.size()) {
                publishProgress("GO");
                CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);


                Call<InsuranceResponseDataObj> uploadFileClaimImagesRequestCall = commonApiInterface.uploadFileClaimImages(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.getUserToken(),
                        imageArr.get(count)
                );

                if (Constants.type == Constants.Type.HEMAS) {
                    uploadFileClaimImagesRequestCall = commonApiInterface.uploadFileClaimImagesForHemas(
                            AppConfig.APP_BRANDING_ID,
                            prefManager.getUserToken(),
                            imageArr.get(count)
                    );
                }


                uploadFileClaimImagesRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {
                    @Override
                    public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                        if (response.isSuccessful()) {
                            InsuranceResponseDataObj insuranceResponseDataObj = response.body();
                            JsonObject insuranceResponseDataJsonObject = new Gson().toJsonTree(insuranceResponseDataObj.getData()).getAsJsonObject();
                            JsonObject insuranceResponseDataJsonObjectMedia = insuranceResponseDataJsonObject.get("media").getAsJsonObject();
                            upLoadedImageList.add(insuranceResponseDataJsonObjectMedia.get("URL").getAsString());
                            new GetImageFromServer().execute(new MyTaskParams(
                                            insuranceResponseDataJsonObjectMedia.get("URL").getAsString(),
                                            count,
                                            insuranceResponseDataJsonObjectMedia.get("MediaName").getAsString(),
                                            insuranceResponseDataJsonObjectMedia.get("MediaFolder").getAsString(),
                                            imageArr.get(count).getFile_contentType(),
                                            ""
                                    )
                            );
                            Toast toast = Toast.makeText(getApplicationContext(), "Uploaded image successfully", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                        publishProgress("STOP");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                publishProgress("STOP");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            if (text[0].equals("GO")) {
                progressDialog.show();
            } else {
                progressDialog.cancel();
            }
        }
    }

    public String getBaseString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public class GetImageFromServer extends AsyncTask<MyTaskParams, Void, Bitmap> {
        ProgressAyubo activityClaimUploadDocumentProgressBar;
        private Bitmap image;
        private int count;
        private String imgStringURL;
        private String imgMediaName;
        private String imgMediaFolder;
        private String mediaType;

        protected void onPreExecute() {
            super.onPreExecute();
            activityClaimUploadDocumentProgressBar = new ProgressAyubo(ClaimUploadDocumentActivity.this);
            activityClaimUploadDocumentProgressBar = findViewById(R.id.activity_claim_upload_document__progress_bar);
            activityClaimUploadDocumentProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(MyTaskParams... params) {

            try {
                count = params[0].getCount();
                imgStringURL = params[0].getImageUrl();
                imgMediaName = params[0].getMediaName();
                imgMediaFolder = params[0].getMediaFolder();
                mediaType = params[0].getMediaType();
                URL url = new URL(params[0].getImageUrl().trim());
                URLConnection urlConnection = url.openConnection();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                image = BitmapFactory.decodeStream(urlConnection.getInputStream(), null, options);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            if (result != null) {
                activityClaimUploadDocumentProgressBar.setVisibility(View.GONE);
                ImageView image = new ImageView(ClaimUploadDocumentActivity.this);
                image.setImageBitmap(result);
                insertItem(0, result, imgStringURL, imgMediaName, imgMediaFolder, mediaType);
                new UploadImageToServer().execute(count + 1);
            }

        }


    }

}