package com.ayubo.life.ayubolife.hemas_hospital_specific;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewMember_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ApiInterface apiService;
    long totalSize = 0;
    EditText txt_name, txt_mobile, txt_relation;
    ImageButton btnDate;
    TextView txt_dateofbirth;
    String birthday_db, sName, sRelation, sDateOFBirth, sMobile, image_absolute_path;
    String sDate, sMonth, sYear;

    int selectYear, year, month, day;
    File newf;
    Calendar calendar;
    Bitmap bitmapProfile = null;
    LinearLayout date_picker_layout;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ProgressDialog prgDialog;

    boolean firsttime = false;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    boolean male = false;
    boolean female = false;
    String userid_ExistingUser;
    HashMap<String, String> user = null;
    private PrefManager prefManager;
    Button btn_create;
    TextView txt_privacypolicy, txt_termscondition;
    ImageButton btn_backImgBtn;
    ImageView imgProfile;
    PrefManager pref;
    Spinner spinner;

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        sRelation = spinner.getSelectedItem().toString();

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member_);


        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        prgDialog = new ProgressDialog(AddNewMember_Activity.this);

        localConstructor();


        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNewMember_Activity.this, SelectFamilyMemberActivity.class);
                intent.putExtra("isLocalData", "no");
                startActivity(intent);
                finish();
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionOpenSDCard();
            }
        });
        txt_dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(view);

            }
        });


    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        String path = null;
        File dir = null;
        try {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
            dir = new File(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!dir.exists())
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImagePopup();
                    //  newf=saveBitmap(bitmapProfile,"screnshot.png");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
        }
        System.out.println("============+++++++++++++++++++++++++++++++++++++=============================");
    }

    //======================================
    @SuppressWarnings("deprecation")
//    private void onSelectFromGalleryResult(Intent data) {
//      //  Ram.setHomeFragmentNumber("1");
//        Uri selectedImageUri = data.getData();
//       // System.out.println("============+++++++++++++++++++++++++++++++++++++======================2=======");
//       // System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
//        Cursor cursor;
//
//        String[] projection = {MediaStore.MediaColumns.DATA};
//        String selectedImagePath = null;
//
//        cursor = AddNewMember_Activity.this.getContentResolver().query(selectedImageUri, projection, null, null, null);
//
//        if (cursor != null && cursor.getCount() > 0) {
//
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//
//            selectedImagePath = cursor.getString(column_index);
//            image_absolute_path=selectedImagePath;
//            System.out.println("============================="+image_absolute_path);
//
//            Bitmap bm;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(selectedImagePath, options);
//            final int REQUIRED_SIZE = 50;
//            int scale = 1;
//            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
//                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
//                scale *= 2;
//            options.inSampleSize = scale;
//            options.inJustDecodeBounds = false;
//            //bm = BitmapFactory.decodeFile(selectedImagePath, options);
//
//            //=========================
//            int photoW = options.outWidth;
//            int photoH = options.outHeight;
//            int targetW = imgProfile.getWidth();
//            int targetH = imgProfile.getHeight();
//
//		/* Figure out which way needs to be reduced less */
//            int scaleFactor = 1;
//            if ((targetW > 0) || (targetH > 0)) {
//                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//            }
//
//		/* Set bitmap options to scale the image decode target */
//            options.inJustDecodeBounds = false;
//            options.inSampleSize = scaleFactor;
//            options.inPurgeable = true;
//
//		/* Decode the JPEG file into a Bitmap */
//            bm = BitmapFactory.decodeFile(selectedImagePath, options);
//            bitmapProfile=bm;
//            //     Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, 100, 100, true);
//
//            Ram.setProfileImageUri(selectedImageUri.toString());
//            Ram.setProfileImagePath(selectedImagePath);
//
//
//            //db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), selectedImagePath);
//            System.out.println("=============IMAGE 0==2=======================" + selectedImageUri.toString() + "   " + selectedImagePath);
//
//            newf=saveBitmap(bm,"user_profile.png");
//
//            imgProfile.setImageBitmap(bm);
//
//          //  uploadProfileImageAsFile();
//        }
//
//        else{
//            Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aaaaa);
//            Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
//            imgProfile.setImageBitmap(circularBitmap);
//        }
//        //========================
//
//    }
//
//    private void onCaptureImageResult(Intent data) {
//       // Ram.setHomeFragmentNumber("1");
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//
//            if (ContextCompat.checkSelfPermission(AddNewMember_Activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//
//                Uri selectedImageUri = data.getData();
//
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
//
//                File destination = new File(Environment.getExternalStorageDirectory(),
//                        System.currentTimeMillis() + ".jpg");
//
//              //  db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
//              //  Ram.setProfileImageUri(selectedImageUri.toString());
//                System.out.println("======================destination===================================" + destination.toString());
//                FileOutputStream fo;
//                try {
//                    destination.createNewFile();
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                imgProfile.setImageBitmap(thumbnail);
//
//
//            } else if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewMember_Activity.this, Manifest.permission.CAMERA)) {
//                // Toast.makeText(getContext(), "Permission Issue 02..",Toast.LENGTH_SHORT).show();
//                // We've been denied once before. Explain why we need the permission, then ask again.
//                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
//
//
//            } else {
//                //  Toast.makeText(getContext(), "Permission Issue 03..",Toast.LENGTH_SHORT).show();
//                // We've never asked. Just do it.
//                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
//            }
//        } else {
//            Uri selectedImageUri = data.getData();
//
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");
//
//         //   db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
//          //  Ram.setProfileImageUri(selectedImageUri.toString());
//            System.out.println("======================destination===================================" + destination.toString());
//            FileOutputStream fo;
//            try {
//                destination.createNewFile();
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            imgProfile.setImageBitmap(thumbnail);
//        }
//
//
//    }


    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(AddNewMember_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (AddNewMember_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(AddNewMember_Activity.this.findViewById(android.R.id.content),
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
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        } else {
            // write your logic code if permission already granted
            // newf=saveBitmap(b,"screnshot.png");

            selectImagePopup();

        }
    }

    private void selectImagePopup() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    void localConstructor() {

        prefManager = new PrefManager(this);
        prgDialog = new ProgressDialog(this);
        apiService = null;


        txt_name = (EditText) findViewById(R.id.txt_name);
        // txt_relation =(EditText) findViewById(R.id.txt_relation);
        txt_dateofbirth = (TextView) findViewById(R.id.txt_dateofbirth);
        txt_mobile = (EditText) findViewById(R.id.txt_mobile);

        btn_create = (Button) findViewById(R.id.btn_create);
        imgProfile = (ImageView) findViewById(R.id.btn_userpic);
        spinner = (Spinner) findViewById(R.id.relation_spinner);

//        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
//                R.array.planets_array, R.layout.spinner_item_addmember);
//        adapter.setDropDownViewResource(R.layout.spinner_item_addmember_dropdown_item);
//
//        spinner.setAdapter(adapter);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(this);

        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showFirstDate(year, month + 1, day);
        birthday_db = "";


    }

    public void createMember(View v) {


        sName = txt_name.getText().toString();
        //   sRelation=txt_relation.getText().toString();
        sDateOFBirth = txt_dateofbirth.getText().toString();
        sMobile = txt_mobile.getText().toString();


        if (sName.equals("")) {
            Toast.makeText(getApplicationContext(), "Name can not be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (sRelation.equals("")) {
            Toast.makeText(getApplicationContext(), "Relation can not be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (sDateOFBirth.equals("")) {
            Toast.makeText(getApplicationContext(), "Date of birth can not be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (sMobile.equals("")) {
            Toast.makeText(getApplicationContext(), "Mobile number can not be empty", Toast.LENGTH_LONG).show();
            return;
        } else {

            if (newf != null) {
                uploadProfileImage();
            } else {
                Toast.makeText(getApplicationContext(), "Error in accessing SD card", Toast.LENGTH_LONG).show();
            }
            //    //=============SERVICE CALL=================================
            //  click_GoWithMobileNumber();
            //=============SERVICE CALL=================================
        }


    }

    private void uploadProfileImage() {
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setCancelable(false);
            prgDialog.setMessage("Saving data...");
            prgDialog.show();
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
            HttpPost httppost = new HttpPost(ApiClient.BASE_URL_entypoint + "uploadFamilyMember");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                // Adding file data to http body

                entity.addPart("picture", new FileBody(newf));
                entity.addPart("userid", new StringBody(userid_ExistingUser));
                entity.addPart("name", new StringBody(sName));
                entity.addPart("mobile", new StringBody(sMobile));
                entity.addPart("relationship", new StringBody(sRelation));
                entity.addPart("dob", new StringBody(sDateOFBirth));


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
            // showing the server response in an alert dialog
            //  showAlert(result);

            prgDialog.cancel();
            super.onPostExecute(result);
            JSONObject jsonObj = null;
            String res = null;
            try {
                jsonObj = new JSONObject(result);
                res = jsonObj.optString("result").toString();
                if (res != null) {

                    if (res.equals("0")) {

                        //  Toast.makeText(WorkoutShareListActivity.this, "Workout shared on timeline", Toast.LENGTH_LONG).show();
//sfddfsdf
                        Intent intent = new Intent(AddNewMember_Activity.this, SelectFamilyMemberActivity.class);
                        intent.putExtra("isLocalData", "no");
                        startActivity(intent);
                        finish();

                    } else {
                        System.out.println("Member adding error " + result);
                        //  Toast.makeText(WorkoutShareListActivity.this, "Workout sharing error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //  Toast.makeText(WorkoutShareListActivity.this, "Sharing error : "+result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                // Toast.makeText(WorkoutShareListActivity.this, "Sharing error : ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }

    }


    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    private void showFirstDate(int year, int month, int day) {
        selectYear = year;
        birthday_db = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        sDate = Integer.toString(day);
        sMonth = Integer.toString(month);
        sYear = Integer.toString(year);
        // txt_dateofbirth.setText(birthday_db);

    }

    private void showDate(int year, int month, int day) {
        selectYear = year;
        birthday_db = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);
        sDate = Integer.toString(day);
        sMonth = Integer.toString(month);
        sYear = Integer.toString(year);
        txt_dateofbirth.setText(birthday_db);

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(AddNewMember_Activity.this, myDateListener, year, month, day);
        }
        return null;
    }
}
