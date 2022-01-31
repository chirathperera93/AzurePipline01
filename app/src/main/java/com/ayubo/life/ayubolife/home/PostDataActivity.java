package com.ayubo.life.ayubolife.home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.NewHomeWithSideMenuActivity;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.body.ShareWorkoutActivity;
import com.ayubo.life.ayubolife.body.WorkoutShareListActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.fragments.CircleTransform;
import com.ayubo.life.ayubolife.health.MedicineView_PaymentActivity;
import com.ayubo.life.ayubolife.health.Medicine_ViewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.DoctorWebviewActivity;
import com.ayubo.life.ayubolife.home_popup_menu.MyDoctorLocations_Activity;
import com.ayubo.life.ayubolife.model.DoctorDetailsObj;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.crash.FirebaseCrash;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
//import static com.facebook.login.widget.ProfilePictureView.TAG;

public class PostDataActivity extends AppCompatActivity {
    CircleImageView imgProfile;
    private PrefManager pref;
     boolean isImageAvailable=false;
    String imagepath_db;
     ImageButton btn_backImgBtn;
    EditText txt_post_message;
    TextView txt_url_toast,txt_btn_post,txt_btn_commiunities_name,txt_btn_all_commiunities_header;
    private Uri fileUri;
   LinearLayout lay_post_url_toast_lay,btn_lay_from_gallery,btn_lay_from_camera;
    RelativeLayout activity_postdata;
    int REQUEST_CAMERA = 0;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    int SELECT_FILE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    String image_absolute_path,userid_ExistingUser;
     ImageView  post_img; long totalSize = 0;
    ProgressDialog prgDialog; File newf;
    String  communityID,postType,message;
     Bitmap selectedImageBitmap;
    String cId,cName,userName,shareMessage;
    ImageButton btn_deleted_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data);

        pref = new PrefManager(this);
        prgDialog=new ProgressDialog(PostDataActivity.this);
        userid_ExistingUser=pref.getLoginUser().get("uid");
        userName=pref.getLoginUser().get("name");


        imgProfile = (CircleImageView)findViewById(R.id.img_profile);
        txt_post_message = (EditText)findViewById(R.id.txt_post_message);
        lay_post_url_toast_lay= (LinearLayout)findViewById(R.id.lay_post_url_toast_lay);
        activity_postdata= (RelativeLayout)findViewById(R.id.activity_postdata);
        post_img = (ImageView)findViewById(R.id.post_img);
        btn_deleted_image = (ImageButton)findViewById(R.id.btn_deleted_image);
        btn_deleted_image.setVisibility(View.GONE);

        btn_lay_from_gallery= (LinearLayout)findViewById(R.id.btn_lay_from_gallery);
        btn_lay_from_camera= (LinearLayout)findViewById(R.id.btn_lay_from_camera);

        txt_btn_all_commiunities_header = (TextView)findViewById(R.id.txt_btn_all_commiunities_header);
        txt_btn_commiunities_name = (TextView)findViewById(R.id.txt_btn_commiunities_name);
        txt_btn_post = (TextView)findViewById(R.id.txt_btn_post);


        txt_url_toast = (TextView)findViewById(R.id.txt_url_toast);
        txt_url_toast.setVisibility(View.GONE);
        lay_post_url_toast_lay.setVisibility(View.GONE);

        btn_deleted_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.whitebg);
                    post_img.setImageBitmap(bitmap);
                }else {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.whitebg);
                    Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                    post_img.setImageBitmap(myLogo);
                }
                selectedImageBitmap=null;
                btn_deleted_image.setVisibility(View.GONE);
            }
        });

        try {

            setupUI(activity_postdata);
            imagepath_db=pref.getLoginUser().get("image_path");


            cId="";cName="";

            cId = getIntent().getStringExtra("cId");
            cName = getIntent().getStringExtra("cName");
            postType = getIntent().getStringExtra("postType");
            txt_btn_all_commiunities_header.setText(userName);

            if ((!cId.equals("aa") && (!cName.equals("aa")))) {

                txt_btn_commiunities_name.setText(cName);
                shareMessage = Ram.getShareMessage();

                txt_post_message.setText(shareMessage);
                selectedImageBitmap = Ram.getMapSreenshot();
                if ((txt_post_message != null) && (!txt_post_message.equals(""))) {
                    txt_btn_post.setTextColor(Color.parseColor("#4797db"));
                }
                if (selectedImageBitmap != null) {
                    post_img.setImageBitmap(selectedImageBitmap);
                    newf = saveBitmap(selectedImageBitmap, "timelinepost.png");
                }



                communityID = cId;

            } else {
                communityID = "001";
                postType = "1";

                selectedImageBitmap = Ram.getMapSreenshot();

                if (selectedImageBitmap != null) {
                    post_img.setImageBitmap(selectedImageBitmap);
                    newf = saveBitmap(selectedImageBitmap, "timelinepost.png");
                }

            }

            if (!imagepath_db.equals("") && (imagepath_db != null)) {

                Random rand = new Random();
                int n = rand.nextInt(50) + 1;
                String rannum = Integer.toString(n);
                try {
                    String burlImg = MAIN_URL_LIVE_HAPPY + imagepath_db + "&cache=" + rannum;
                    System.out.println("===============");
                    System.out.println("===============");
                    System.out.println("=====Profile URL===fromServer=======" + burlImg);
                    System.out.println("===============");
                    RequestOptions requestOptions1 = new RequestOptions()
                            .transform(new CircleTransform(PostDataActivity.this))
                            .override(50, 50)
                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(PostDataActivity.this).load(burlImg)
                            .transition(withCrossFade())
                            .into(imgProfile);

                    Ram.setProfileImageStatus(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            txt_btn_all_commiunities_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                        String msg = txt_post_message.getText().toString();
                        Ram.setMapSreenshot(selectedImageBitmap);
                        Ram.setShareMessage(msg);
                        Intent intent = new Intent(PostDataActivity.this, PostDataList_Activity.class);
                        intent.putExtra("from", "PostData");
                        startActivity(intent);
                        finish();


                }
            });
            txt_btn_commiunities_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String msg = txt_post_message.getText().toString();


                        Ram.setMapSreenshot(selectedImageBitmap);
                        Ram.setShareMessage(msg);
                        Intent intent = new Intent(PostDataActivity.this, PostDataList_Activity.class);
                        startActivity(intent);
                        finish();

                }
            });

            txt_btn_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    System.out.println("=========btn_post==============");
                    String msg = txt_post_message.getText().toString();
                    System.out.println("=========btn_post=2============="+msg);



                    if ((msg.equals("")) && (selectedImageBitmap == null)) {
                        Toast.makeText(getBaseContext(), "Please type a message", Toast.LENGTH_LONG).show();
                    } else {

                        System.out.println("=========btn_post=====Else=========");
                        if (selectedImageBitmap == null) {
                            isImageAvailable=false;
                            //   newf = saveEmptyBitmap(selectedImageBitmap, "timelinepost.png");
                        } else {
                            isImageAvailable=true;
                     //       newf = saveBitmap(selectedImageBitmap, "timelinepost.png");
                            System.out.println("=========btn_post==========timelinepost.png====");
                        }

                        message = txt_post_message.getText().toString();
                        uploadProfileImage();
                    }


                }
            });


            btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
            btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btn_lay_from_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    checkPermissionOpenSDCard();
                }
            });
            btn_lay_from_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    checkFullPermission();
                  //  checkPermissionForCamera();

                }
            });


            txt_post_message.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if (!s.equals("")) { //do your work here }
                        String textURL = txt_post_message.getText().toString();

                        boolean isUrl = containsLink(textURL);

                        if (isUrl) {
                            txt_url_toast.setVisibility(View.VISIBLE);
                            lay_post_url_toast_lay.setVisibility(View.VISIBLE);
                        } else {
                            txt_url_toast.setVisibility(View.GONE);
                            lay_post_url_toast_lay.setVisibility(View.GONE);
                        }

//                    boolean isUrl= Patterns.WEB_URL.matcher(textURL).matches();

                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    private static File saveEmptyBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Aubopost";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file=null;
        try {
            file = new File(dir, fileName);
//            FileOutputStream fOut = new FileOutputStream(file);
//            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
//            fOut.flush();
//            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    private static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Aubopost";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void uploadProfileImage()
    {
        new UploadFileToServer().execute();
    }
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setMessage("Sharing post...");
            prgDialog.show();

            System.out.println("=========btn_post==========onPreExecute====");

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
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_entypoint+"submitTimelinePost");
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body
                if(isImageAvailable){
                    entity.addPart("pictureFile", new FileBody(newf));
                }else{
                    entity.addPart("pictureFile",new StringBody(""));
                }
                entity.addPart("message",new StringBody(message));
                entity.addPart("createdBy",new StringBody(userid_ExistingUser));
                entity.addPart("postType",new StringBody(postType));
                entity.addPart("communityID",new StringBody(communityID));
                entity.addPart("link",new StringBody(""));

                totalSize = entity.getContentLength();
                httpPost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity r_entity = response.getEntity();
                r_entity.toString();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = org.apache.http.util.EntityUtils.toString((HttpEntity) r_entity);

                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            // showing the server response in an alert dialog
            //  showAlert(result);

            prgDialog.cancel();
            super.onPostExecute(result);
            JSONObject jsonObj = null;  String res =null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if(res!=null){
                    if(res.equals("0")){
                        Toast.makeText(PostDataActivity.this, "Message posted to timeline successfully", Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(PostDataActivity.this, NewHomeWithSideMenuActivity.class);
                          startActivity(intent);
                          finish();
                    }else{
                        System.out.println("Message posting error "+result);
                        Toast.makeText(PostDataActivity.this, "Message posting error", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(PostDataActivity.this, "Message posting error ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }






        }

    }

    private void checkFullPermission() {
        if (ContextCompat.checkSelfPermission(PostDataActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(PostDataActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (PostDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (PostDataActivity.this, Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(PostDataActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            captureImage_camera();

        }
    }


    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(PostDataActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (PostDataActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(PostDataActivity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                            }
                        }).show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            // write your logic code if permission already granted
            selectImagePopup();



        }
    }

    private void checkPermissionForCamera() {
        if (ContextCompat
                .checkSelfPermission(PostDataActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                            (PostDataActivity.this, Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(PostDataActivity.this.findViewById(android.R.id.content),
                        "This feature needs camera permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.CAMERA},
                                            CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                            }
                        }).show();
            }
            else
            {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        } else {
            // write your logic code if permission already granted

            captureImage_camera();


        }
    }


    public int getOrientation(String path) {
        int rotate = 0;
        try {
            //getContentResolver().notifyChange(photoUri, null);
            //File imageFile = new File(photoUri.getAbsolutePath());
            File imageFile = new File(path); ExifInterface exif =null;
           try {
               exif = new ExifInterface(imageFile.getAbsolutePath());
           }catch(Exception e){
               e.printStackTrace();
           }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    rotate = 0;
                    break;
            }
            //Log.v(Common.TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
        /****** Image rotation ****/

    }

    private void selectImagePopup() {
        try{
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try{
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
                    ur = fileUri.getPath();
                    System.out.println("======URI======from Camera================" + ur);
                    image_absolute_path = fileUri.getPath();

                    launchUploadActivity(fileUri.getPath());

                } else if (requestCode == SELECT_FILE) {

                    onSelectFromGalleryResult(data);
                }

            }
        }catch (Exception e){

            e.printStackTrace();

        }
    }




    private void launchUploadActivity(String uri){

        int orientation = getOrientation(uri);
        System.out.println("==========================orientation=======" + orientation);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        selectedImageBitmap = BitmapFactory.decodeFile(image_absolute_path, options);
        Matrix matrix = new Matrix();

        if (orientation == 90) {
            matrix.postRotate(90);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        }
        else if (orientation == 180) {
            matrix.postRotate(180);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        }
        else if (orientation == 270) {
            matrix.postRotate(270);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            post_img.setImageBitmap(rotatedBitmap);
        }else{
            post_img.setImageBitmap(selectedImageBitmap);
        }
        btn_deleted_image.setVisibility(View.VISIBLE);


        newf = saveBitmap(selectedImageBitmap, "timelinepost.png");

        Ram.setImageAbsoulutePath(uri);


    }




    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        //  Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        try{
            data.getData();
            Uri selectedImageUri = data.getData();
         //   String urii= getRealPathFromURI(data.getData());
            String uri=  getRealPathFromURI(PostDataActivity.this,data.getData());
            System.out.println("============+++++++++++++++++++++++++++++++++++++======================2======="+uri);
            System.out.println("===================" + selectedImageUri.toString());
            Cursor cursor;

            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

            cursor = PostDataActivity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);
                image_absolute_path=selectedImagePath;
                System.out.println("============================="+image_absolute_path);

                launchUploadActivity(image_absolute_path);

            }

            else{
                Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
                Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
                post_img.setImageBitmap(circularBitmap);
            }
            //========================
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //final float densityMultiplier = Welcome.getContext().getResources().getDisplayMetrics().density;
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        //final float roundPx = 30;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case   PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    System.out.println("===================================");
                    System.out.println("===================Camera On================");
                    System.out.println("===================================");

                    captureImage_camera();

                } else {
                    // write your logic code if permission already granted
                //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    captureImage_camera();
                } else {
                    // write your logic code if permission already granted
                    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    selectImagePopup();
                } else {
                    // write your logic code if permission already granted
                    selectImagePopup();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utility.hideSoftKeyboard(PostDataActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void captureImage_camera() {
        try{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, PERMISSIONS_MULTIPLE_REQUEST);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(PostDataActivity.this, "Camera opening error. Please check the permission",Toast.LENGTH_LONG).show();
        }
    }
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    private File getOutputMediaFile(int type) {
        File mediaFile=null;
        try{
            // External sdcard location
            File mediaStorageDir =null;

            if (Build.VERSION.SDK_INT >= 24) {
                mediaFile=createImageFile_HighVersions();
            } else {
                mediaStorageDir=createImageFile_LawVersions();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return mediaFile;
    }


    private File createImageFile_LawVersions() throws IOException {
        File mediaStorageDir=null;
        try {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    System.currentTimeMillis() + ".jpg");
        }catch (Exception e){
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {

                    return null;
                }
                mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        System.currentTimeMillis() + ".jpg");
            }
        }
        return mediaStorageDir;
    }

    private File createImageFile_HighVersions() throws IOException {
        // Create an image file name
        File image=null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        try{
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );}
        catch (Exception e){
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {

                    return null;
                }
            }
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }
        return image;
    }

    public static boolean containsLink(String input) {
        boolean result = false;

        String[] parts = input.split("\\s+");

        for (String item : parts) {
            if (android.util.Patterns.WEB_URL.matcher(item).matches()) {
                result = true;
                break;
            }
        }

        return result;
    }
//
//    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
//
//    Pattern p = Pattern.compile(URL_REGEX);
//    Matcher m = p.matcher("example.com");//replace with string to compare
//    m.
//
//    if(m.) {
//        System.out.println("String contains URL");
//    }

}
