package com.ayubo.life.ayubolife.reports.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_CONNECT;

public class CreateFamilyMemberActivity extends AppCompatActivity {
    //  ProgressDialog mProgressDialog;
    String userid_ExistingUser;
    PrefManager pref;
    String URL = null;
    ProgressDialog prgDialog;
    String appToken;
    long totalSize;
    int SELECT_FILE = 1;
    File newf;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    String image_absolute_path;
    private Uri fileUri;
    Bitmap selectedImageBitmap;
    de.hdodenhof.circleimageview.CircleImageView img_user_image;
    Uri selectedImageUri;
    ImageButton img_btn_male;
    ImageButton img_btn_female;
    String age, name, gender;
    EditText txt_fname, txt_mobile, txt_age;
    TextView lbl_relationship;
    String[] cities = {
            "Relationship",
            "Father",
            "Mother",
            "Son",
            "Daughter",
            "Other"
    };
    String slesctedRelationship;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_create_family_member);

        prgDialog = new ProgressDialog(CreateFamilyMemberActivity.this);
        prgDialog.setMessage("Loading...");
        pref = new PrefManager(CreateFamilyMemberActivity.this);
        gender = "5";
        appToken = pref.getUserToken();

        img_user_image = findViewById(R.id.img_user_image);

        txt_fname = findViewById(R.id.txt_fname);
        txt_age = findViewById(R.id.txt_age);
        txt_mobile = findViewById(R.id.txt_mobile);

        // lbl_relationship =  findViewById(R.id.lbl_relationship);


        ImageButton txt_proceed_pay = findViewById(R.id.txt_proceed_pay);
        img_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard();
            }
        });
        txt_proceed_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewMember();

            }
        });

        txt_fname = findViewById(R.id.txt_fname);
        txt_age = findViewById(R.id.txt_age);
        txt_mobile = findViewById(R.id.txt_mobile);

        //   lbl_relationship =  findViewById(R.id.lbl_relationship);


        img_btn_male = findViewById(R.id.img_btn_male);
        img_btn_female = findViewById(R.id.img_btn_female);
        img_btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "1";
                img_btn_male.setBackgroundResource(R.drawable.gender_selected_male);
                img_btn_female.setBackgroundResource(R.drawable.female_ic);
            }
        });
        img_btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gender = "0";
                img_btn_male.setBackgroundResource(R.drawable.gender_select_male);
                img_btn_female.setBackgroundResource(R.drawable.female_ic_selected);
            }
        });

        final Spinner spinnerDropDown;

        spinnerDropDown = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.
                R.layout.simple_spinner_dropdown_item, cities);

        spinnerDropDown.setAdapter(adapter);

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid = spinnerDropDown.getSelectedItemPosition();
                slesctedRelationship = cities[sid];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


    }

    void createNewMember() {


        if (validated()) {

            name = txt_fname.getText().toString();
            age = txt_age.getText().toString();


            if (newf != null) {
                new UploadImage().execute();
            }
        }

    }

    boolean validated() {
        boolean isValid = true;
        txt_fname = findViewById(R.id.txt_fname);
        txt_age = findViewById(R.id.txt_age);
        txt_mobile = findViewById(R.id.txt_mobile);

        if (txt_fname.getText().toString().equals("")) {
            isValid = false;
            txt_fname.setError(getString(R.string.enter_name));
        }
        if (slesctedRelationship.equals("Relationship")) {
            isValid = false;
            Toast.makeText(CreateFamilyMemberActivity.this, "Please select relationship", Toast.LENGTH_LONG).show();
        }
        if (txt_age.getText().toString().equals("")) {
            isValid = false;
            txt_age.setError(getString(R.string.enter_age));
        }
        if (txt_age.getText().toString().equals("0")) {
            isValid = false;
            txt_age.setError(getString(R.string.enter_valid_age));
        }
        if (gender.equals("5")) {
            isValid = false;
            Toast.makeText(CreateFamilyMemberActivity.this, "Please select gender", Toast.LENGTH_LONG).show();
        }
        if (newf == null) {
            isValid = false;
            Toast.makeText(CreateFamilyMemberActivity.this, "Please select photo", Toast.LENGTH_LONG).show();
        }


        return isValid;
    }

    class ViewHolder {

        EditText txt_fname, txt_age;
        Spinner spinner1;

        ViewHolder() {
            txt_fname = findViewById(R.id.txt_fname);
            txt_age = findViewById(R.id.txt_age);
            spinner1 = findViewById(R.id.spinner1);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // API 21
                txt_fname.setShowSoftInputOnFocus(false);
            } else { // API 11-20
                txt_fname.setTextIsSelectable(true);
            }


        }


        private void hideKeyboard() {
            View view = CreateFamilyMemberActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

        void setUserDateToView(String name, String mob, String email) {

            String fullName = pref.getLoginUser().get("name");
            String[] splited = fullName.split("\\s+");
            String fn = splited[0];
            String ln = splited[1];
            txt_fname.setText(fn);

        }


        boolean validate() {
            boolean isValid = true;
            txt_fname = findViewById(R.id.txt_fname);
            txt_age = findViewById(R.id.txt_age);
            txt_mobile = findViewById(R.id.txt_mobile);
            if (txt_fname.getText().toString().equals("")) {
                isValid = false;
                txt_fname.setError(getString(R.string.enter_name));
            }
            if (slesctedRelationship.equals("Relationship")) {
                isValid = false;
                Toast.makeText(CreateFamilyMemberActivity.this, "Please select relationship", Toast.LENGTH_LONG).show();
            }
            if (txt_age.getText().toString().equals("")) {
                isValid = false;
                txt_age.setError(getString(R.string.enter_age));
            }
            if (txt_age.getText().toString().equals("0")) {
                isValid = false;
                txt_age.setError(getString(R.string.enter_valid_age));
            }


            return isValid;
        }

        private void setUser() {

        }
    }


    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(CreateFamilyMemberActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (CreateFamilyMemberActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(CreateFamilyMemberActivity.this.findViewById(android.R.id.content),
                        "This app needs storage permission.",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            selectImagePopup();


        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ur = null;
        //  clearView();
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {
                    ur = fileUri.getPath();
                    image_absolute_path = fileUri.getPath();
                    setImageToUI(fileUri.getPath());
                    //  btn_deleted_image.setVisibility(View.VISIBLE);
                } else if (requestCode == SELECT_FILE) {
                    // postType="IMAGE";
                    //  btn_deleted_image.setVisibility(View.VISIBLE);
                    onSelectFromGalleryResult(data);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        //  Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        try {
            data.getData();
            selectedImageUri = data.getData();

            //    selectedImageType=getImageType(selectedImageUri);

            Cursor cursor;
            String[] projection = {MediaStore.MediaColumns.DATA};
            String selectedImagePath = null;

            cursor = CreateFamilyMemberActivity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                selectedImagePath = cursor.getString(column_index);
                image_absolute_path = selectedImagePath;
                System.out.println("=============================" + image_absolute_path);


                setImageToUI(image_absolute_path);

            } else {
                Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
                //  Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
                img_user_image.setImageBitmap(myBitmap);
            }
            //========================
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSIONS_MULTIPLE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    selectImagePopup();

                } else {
                    // write your logic code if permission already granted
                    //    captureImage_camera();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void setImageToUI(String uri) {


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
            img_user_image.setImageBitmap(rotatedBitmap);
        } else if (orientation == 180) {
            matrix.postRotate(180);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            img_user_image.setImageBitmap(rotatedBitmap);
        } else if (orientation == 270) {
            matrix.postRotate(270);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, 200, 200, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            img_user_image.setImageBitmap(rotatedBitmap);
        } else {
            img_user_image.setImageBitmap(selectedImageBitmap);
        }


        newf = resizeAndCompressImageBeforeSend(CreateFamilyMemberActivity.this, image_absolute_path, "postImage.jpeg");

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    public File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);

        Bitmap bmpPicNew = null;

        int compressQuality = 5; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);

        try {

            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(context.getCacheDir() + fileName);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sp = context.getCacheDir() + "/" + fileName;
        //return the path of resized and compressed file
        return destination;
    }

    public int getOrientation(String path) {
        int rotate = 0;
        try {
            //getContentResolver().notifyChange(photoUri, null);

            File imageFile = new File(path);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

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

    private class UploadImage extends AsyncTask<Void, Integer, String> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(MAIN_URL_CONNECT + "api/v1/report/member/new");

            httppost.addHeader("Authorization", appToken);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });


                entity.addPart("name", new StringBody(name));
                entity.addPart("relationship", new StringBody(slesctedRelationship));
                entity.addPart("gender", new StringBody(gender));
                entity.addPart("date_of_birth", new StringBody(age));
                entity.addPart("mobile", new StringBody(""));
                entity.addPart("profile_picture", new FileBody(newf));


                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
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
            if (prgDialog != null) {
                prgDialog.cancel();
            }
            //
            System.out.println("=============result==============" + result);
            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();

                if (res.equals("0")) {
                    Intent in = new Intent(CreateFamilyMemberActivity.this, SelectFamilyMemberActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(CreateFamilyMemberActivity.this, "Error", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {

                e.printStackTrace();
            }


        }

    }


}
