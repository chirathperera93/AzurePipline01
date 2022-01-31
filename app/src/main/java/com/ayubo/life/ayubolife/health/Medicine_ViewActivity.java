package com.ayubo.life.ayubolife.health;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.activity.PrescriptionHistoryViewActivity;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.flavors.changes.Constants;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import static com.facebook.login.widget.ProfilePictureView.TAG;

public class Medicine_ViewActivity extends BaseActivity {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    //  public static final String GALLERY_DIRECTORY_NAME = "Ayubo Camera";
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;

    private TextView txtDescription;
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;


    ImageView ivImage, ivCamera;
    //private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;
    final int PERMISSIONS_MULTIPLE_REQUEST = 333;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;
    Point point;
    PopupWindow changeStatusPopUp;
    String image_absolute_path, userid_ExistingUser;
    ProgressDialog prgDialog;
    LinearLayout drop_menu_holder;
    File compressedImageFile;
    long totalSize = 0;
    ImageButton btn_backImgBtn;
    PrefManager pref;
    String hasToken, encodedHashToken;
    TextView tems;
    ImageButton btn_delivery_chares;
    Context context;

    List<String> array_image_absolute_path = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_upload_view);
        context = Medicine_ViewActivity.this;

        btn_delivery_chares = (ImageButton) findViewById(R.id.btn_delivery_chares);
        btn_delivery_chares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAction("native_post", "550");
                // Intent i = new Intent(Medicine_ViewActivity.this, TermsDiliveMedicinActivity.class);
                // startActivity(i);
                //  finish();
            }
        });

        LinearLayout btn_backImgBtn_layout = (LinearLayout) findViewById(R.id.btn_backImgBtn_layout);
        btn_backImgBtn_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        pref = new PrefManager(Medicine_ViewActivity.this);

        userid_ExistingUser = pref.getLoginUser().get("uid");
        hasToken = pref.getLoginUser().get("hashkey");
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        prgDialog = new ProgressDialog(Medicine_ViewActivity.this);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showCamera();
            }
        });

        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  showCameraCamNew();
                checkFullPermission();


            }
        });

        final ImageButton main_menu_part_four_image = (ImageButton) findViewById(R.id.btn_more);

        main_menu_part_four_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context wrapper = new ContextThemeWrapper(Medicine_ViewActivity.this, R.style.MyPopupMenu);

                PopupMenu popup = new PopupMenu(wrapper, main_menu_part_four_image);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        try {
                            Intent intent = new Intent(Medicine_ViewActivity.this, PrescriptionHistoryViewActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });


    }

    static final int REQUEST_IMAGE_CAPTURE = 11;


    private void checkFullPermission() {
        if (ContextCompat.checkSelfPermission(Medicine_ViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(Medicine_ViewActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (Medicine_ViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (Medicine_ViewActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(Medicine_ViewActivity.this.findViewById(android.R.id.content),
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
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {

            dispatchTakePictureIntent();

        }
    }


    private void launchUploadActivity(String uri) {
        Intent i = new Intent(Medicine_ViewActivity.this, MedicineView_PaymentActivity.class);
        i.putExtra("from", "medicine");
        Ram.setImageAbsoulutePath(uri);
        Ram.setMultipleImageAbsolutePath(new ArrayList<String>());
        startActivity(i);
    }

    private void launchMultipleUploadActivity(List<String> uri) {
        Intent i = new Intent(Medicine_ViewActivity.this, MedicineView_PaymentActivity.class);
        i.putExtra("from", "medicine");
        Ram.setMultipleImageAbsolutePath(uri);
        startActivity(i);
    }


    static final int REQUEST_TAKE_PHOTO = 441;


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Prescription_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        image_absolute_path = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                if (Constants.type == Constants.Type.AYUBO) {
                    fileUri = FileProvider.getUriForFile(this, "com.ayubo.life.ayubolife", photoFile);
                } else if (Constants.type == Constants.Type.SHESHELLS) {
                    fileUri = FileProvider.getUriForFile(this, "com.ayubo.life.sheshells", photoFile);
                } else if (Constants.type == Constants.Type.LIFEPLUS) {
                    fileUri = FileProvider.getUriForFile(this, "com.ayubo.life.lifeplus", photoFile);
                } else {
                    fileUri = FileProvider.getUriForFile(this, "com.ayubo.life.hemas", photoFile);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try {
            if (resultCode == Activity.RESULT_OK) {


                if (requestCode == REQUEST_TAKE_PHOTO) {
                    launchUploadActivity(image_absolute_path);
                } else if (requestCode == SELECT_FILE) {
                    onSelectFromGalleryResult(data);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadProfileImageAsFile() {
        try {
            if (Utility.isInternetAvailable(Medicine_ViewActivity.this)) {
                try {
                    uploadProfileImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(Medicine_ViewActivity.this, "No internet connection. Can't upload profile image. ",
                        Toast.LENGTH_LONG).show();
                // finish();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void uploadProfileImage() {
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            File sourceFile = new File(image_absolute_path);
            compressedImageFile = sourceFile;
//            try {
//                compressedImageFile = new Compressor(Medicine_ViewActivity.this).compressToFile(sourceFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            prgDialog.setMessage("Prescription uploading...");
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

            HttpPost httppost = new HttpPost(ApiClient.BASE_URL_entypoint + "uploadPrescription");
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                //  MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,);

                //  File sourceFile = new File(image_absolute_path);

                // Adding file data to http body
                entity.addPart("prescription", new FileBody(compressedImageFile));

                System.out.println("=======Image ab path=========" + image_absolute_path.toString());
                //  Bitmap bitmap = Bitmap.createScaledBitmap(capturedImage, width, height, true);
                // Extra parameters if you want to pass to server
                entity.addPart("userid", new StringBody(userid_ExistingUser));

                entity.addPart("app_id", new StringBody(AppConfig.APP_BRANDING_ID));

                //   entity.addPart("prescription", new StringBody("png"));

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


            Toast.makeText(Medicine_ViewActivity.this, result,
                    Toast.LENGTH_LONG).show();
            // showing the server response in an alert dialog
            //  showAlert(result);

            super.onPostExecute(result);


        }

    }


    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getRealPathFromURI(Uri selectedImageUri) {
        Cursor cursor;

        String[] projection = {MediaStore.MediaColumns.DATA};
        String selectedImagePath = null;

        cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            selectedImagePath = cursor.getString(column_index);
        }
        return selectedImagePath;
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        //  Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        array_image_absolute_path = new ArrayList<String>();
        try {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                //multiple images selecetd
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    array_image_absolute_path.add(FileUtils.getPath(getApplicationContext(), imageUri));
                }
                launchMultipleUploadActivity(array_image_absolute_path);
            } else {
                data.getData();
                Uri selectedImageUri = data.getData();
                String filePath = FileUtils.getPath(getApplicationContext(), selectedImageUri);
                image_absolute_path = filePath;
                launchUploadActivity(image_absolute_path);
            }


//            Cursor cursor;
//
//            String[] projection = {MediaStore.MediaColumns.DATA};
//            String selectedImagePath = null;
//
//            cursor = Medicine_ViewActivity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);
//
//            if (cursor != null && cursor.getCount() > 0) {
//
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                cursor.moveToFirst();
//
//                selectedImagePath = cursor.getString(column_index);
//                image_absolute_path = selectedImagePath;
//                System.out.println("=============================" + image_absolute_path);
//
//                launchUploadActivity(image_absolute_path);
//
//            } else {
//                Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
//                Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
//                //  ivImage.setImageBitmap(circularBitmap);
//            }
            //========================
        } catch (Exception e) {
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

    private void selectImagePopup() {

        if (ActivityCompat.checkSelfPermission(Medicine_ViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Medicine_ViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
//                    return;
        } else {
            try {
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void showCamera() {
        try {
            // Check if the Camera permission is already available.
            if (ActivityCompat.checkSelfPermission(
                    Medicine_ViewActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Camera permission has not been granted.
                requestCameraPermission();
            } else {
                // Camera permissions is already available, show the camera preview.
                selectImagePopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage_camera() {
        try {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //   fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


            File fileUrif = getOutputMediaFile5(MEDIA_TYPE_IMAGE);
            if (fileUrif != null) {
                imageStoragePath = fileUrif.getAbsolutePath();
            }
            fileUri = getOutputMediaFileUri(Medicine_ViewActivity.this, fileUrif);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, PERMISSIONS_MULTIPLE_REQUEST);

            //    File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            //  fileUri= getOutputMediaFileUri(getOutputMediaFile5(MEDIA_TYPE_IMAGE));
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            startActivityForResult(intent, PERMISSIONS_MULTIPLE_REQUEST);
            //   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //  startActivityForResult(cameraIntent, PERMISSIONS_MULTIPLE_REQUEST);
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, PERMISSIONS_MULTIPLE_REQUEST);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Medicine_ViewActivity.this, "Camera opening error. Please check the permission", Toast.LENGTH_LONG).show();
        }
    }

    public static File getOutputMediaFile5(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Medicine_ViewActivity.GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Medicine_ViewActivity.GALLERY_DIRECTORY_NAME, "Oops! Failed create "
                        + Medicine_ViewActivity.GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Medicine_ViewActivity.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + "." + Medicine_ViewActivity.IMAGE_EXTENSION);
        } else if (type == Medicine_ViewActivity.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + "." + Medicine_ViewActivity.VIDEO_EXTENSION);
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        Uri uir = null;
        try {
            uir = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uir;

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaFile = null;
        try {
            // External sdcard location
            File mediaStorageDir = null;

            if (Build.VERSION.SDK_INT >= 24) {
                mediaFile = createImageFile_HighVersions();
            } else {
                mediaStorageDir = createImageFile_LawVersions();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return mediaFile;
    }

    private File createImageFile_LawVersions() throws IOException {
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    System.currentTimeMillis() + ".jpg");
        } catch (Exception e) {
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
//                    Log.d(TAG, "Oops! Failed create "
//                            + System.currentTimeMillis() + ".jpg");
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
        File image = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (Exception e) {
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
//                    Log.d(TAG, "Oops! Failed create "
//                            + System.currentTimeMillis() + ".jpg");
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

    private void requestCameraPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(Medicine_ViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions(Medicine_ViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

//            Snackbar.make(mLayout, R.string.permission_camera_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    REQUEST_CAMERA);
//                        }
//                    })
//                    .show();
        } else if (ActivityCompat.checkSelfPermission(Medicine_ViewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Medicine_ViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
//                    return;
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(Medicine_ViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        // END_INCLUDE(camera_permission_request)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    System.out.println("===================================");
                    System.out.println("===================Camera On================");
                    System.out.println("===================================");

                    dispatchTakePictureIntent();
                    //     captureImage_camera();


                } else {

                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // write your logic code if permission already granted
                    selectImagePopup();
                } else {
                    // write your logic code if permission already granted
                    // selectImagePopup();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            default: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImagePopup();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
