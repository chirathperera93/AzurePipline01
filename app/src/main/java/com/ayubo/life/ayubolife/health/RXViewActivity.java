package com.ayubo.life.ayubolife.health;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
//import static com.facebook.login.widget.ProfilePictureView.TAG;

public class RXViewActivity extends AppCompatActivity {

    ImageView ivImage, ivCamera;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri fileUri;
    String image_absolute_path, userid_ExistingUser;
    ProgressDialog prgDialog;
    File compressedImageFile;
    long totalSize = 0;
    ImageButton btn_backImgBtn;

    LinearLayout lay_take_photo, lay_from_device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_rxview);
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lay_take_photo = (LinearLayout) findViewById(R.id.lay_take_photo);
        lay_from_device = (LinearLayout) findViewById(R.id.lay_from_device);

        lay_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraCamNew();
            }
        });
        lay_from_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageFromDevice();
            }
        });


    }

    private void launchUploadActivity(String uri) {
        Intent i = new Intent(RXViewActivity.this, RXView_SecondActivity.class);
        Ram.setImageAbsoulutePath(uri);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        try {
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                    ur = fileUri.getPath();
                    System.out.println("======URI======from Camera================" + ur);
                    image_absolute_path = fileUri.getPath();

                    launchUploadActivity(fileUri.getPath());


                    //    uploadProfileImageAsFile();

                } else if (requestCode == SELECT_FILE) {

                    onSelectFromGalleryResult(data);
                    // System.out.println("========URI===from Gallery===================" + ur);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void uploadProfileImageAsFile() {
        try {
            if (Utility.isInternetAvailable(RXViewActivity.this)) {
                try {
                    uploadProfileImage();
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                Toast.makeText(RXViewActivity.this, "No internet connection. Can't upload profile image. ",
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
//                compressedImageFile = new Compressor(RXViewActivity.this).compressToFile(sourceFile);
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
                responseString = e.toString();
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


            Toast.makeText(RXViewActivity.this, result,
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
        //  String[] proj = { MediaStore.Images.Media.DATA };

//        CursorLoader cursorLoader = new CursorLoader( getContext(),contentUri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int column_index =
//                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
        //   Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //  data.getData();
        // Uri selectedImageUri = data.getData();
        //  String urii= getRealPathFromURI(data.getData());
        //   String uri=  getRealPathFromURI(getContext(),data.getData());
        //    System.out.println("============+++++++++++++++++++++++++++++++++++++======================2======="+uri);
        //    System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
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
        try {
            data.getData();
            Uri selectedImageUri = data.getData();
            String urii = getRealPathFromURI(data.getData());
            String uri = getRealPathFromURI(RXViewActivity.this, data.getData());
            System.out.println("============+++++++++++++++++++++++++++++++++++++======================2=======" + uri);
            System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
            Cursor cursor;


            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

//        Toast.makeText(this, selectedImageUri.toString(), Toast.LENGTH_SHORT)
//                .show();

            cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;
                System.out.println("=============================" + image_absolute_path);

                launchUploadActivity(image_absolute_path);

            } else {
                Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
                Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
                ivImage.setImageBitmap(circularBitmap);
            }
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
        try {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void showImageFromDevice() {
        try {
            // Check if the Camera permission is already available.
            if (ActivityCompat.checkSelfPermission(RXViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // EXTERNAL_STORAGE permission has not been granted.
                requestCameraPermission();
            } else {
                // EXTERNAL_STORAGE permissions is already available, show the camera preview.
                selectImagePopup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCameraCamNew() {
        try {
            // Check if the Camera permission is already available.
            if (ActivityCompat.checkSelfPermission(RXViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Camera permission has not been granted.
                requestCameraPermission();
            } else {
                // Camera permissions is already available, show the camera preview.
                captureImage_camera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureImage_camera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(RXViewActivity.this, "Camera opening error. Please check the permission", Toast.LENGTH_LONG).show();
        }
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(RXViewActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions(RXViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(RXViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        // END_INCLUDE(camera_permission_request)
    }


}

