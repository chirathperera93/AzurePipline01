package com.ayubo.life.ayubolife.satalite_menu;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.flavors.changes.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelpFeedbackActivity extends AppCompatActivity {
    String image_absolute_path;
    private static final int WRITE_EXTERNAL_STORAGE = 132;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage, btn_backImgBtn;
    private String userChoosenTask;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btn_add_image, btn_sendMail;
    EditText txt_msg;
    Uri selectedImageURIA;
    LinearLayout lay_btnBack, activity_help_feedback_recycler_view_layout;
    private PrefManager pref;
    String appVersion, phoneModel, sentFrom, manufacture, phonebrand, osVesion;
    StringBuilder sb = null;
    String userEmail, mobile, name;

    public ArrayList<Uri> imageArr;
    private ArrayList<Bitmap> mUploadDocumentList;
    private RecyclerView.LayoutManager mLayoutManager;
    private UploadImageAdapter mUploadImageAdapter;

    public void goBackToHome() {
        finish();
    }


    public void shareToGMail(String content) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);


        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@lifeplus.com.bd"});

        // emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@ayubo.life"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailHeading);

        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
        finish();
    }

    String appName = "";
    String emailHeading = "";
    TextView txt_help_footer_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_help_feedback);
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);

        txt_help_footer_text = findViewById(R.id.txt_help_footer_text);

        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });

        lay_btnBack = findViewById(R.id.lay_btnBack);
        activity_help_feedback_recycler_view_layout = findViewById(R.id.activity_help_feedback_recycler_view_layout);

        createUploadDocumentList();
        buildRecyclerView();
        lay_btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToHome();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        txt_msg = (EditText) findViewById(R.id.txt_msg);
        btn_sendMail = (Button) findViewById(R.id.btn_sendmail);
        final String appVersion, phoneModel, sentFrom, manufacture;

        appVersion = Build.VERSION.RELEASE;
        phoneModel = Build.MODEL;
        manufacture = Build.MANUFACTURER;
        phonebrand = Build.BRAND;
        osVesion = Build.VERSION.SDK;
        sentFrom = "sent from Android Device";

        //   mobile
        pref = new PrefManager(this);

        name = pref.getLoginUser().get("name");
        userEmail = pref.getLoginUser().get("email");
        mobile = pref.getLoginUser().get("mobile");

        if (Constants.type == Constants.Type.LIFEPLUS) {
            appName = "lifeplus";
            txt_help_footer_text.setText(R.string.help_lifeplus);
        }

        if (Constants.type == Constants.Type.AYUBO) {
            appName = "ayubo.life";
            txt_help_footer_text.setText(R.string.help_andfeed_new);
        }

        if (Constants.type == Constants.Type.HEMAS) {
            appName = "Hemas Hospitals";
            txt_help_footer_text.setText("Please call our support line on 0117888-888 for further assistance.");
        }

        if (Constants.type == Constants.Type.SHESHELLS) {
            appName = "Future Care";
            txt_help_footer_text.setText(R.string.help_andfeed_new);
        }

        emailHeading = "Support request " + appName;


        Log.i("TAG", "SERIAL: " + Build.SERIAL);
        Log.i("TAG", "MODEL: " + Build.MODEL);
        Log.i("TAG", "ID: " + Build.ID);
        Log.i("TAG", "Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG", "brand: " + Build.BRAND);
        Log.i("TAG", "type: " + Build.TYPE);
        Log.i("TAG", "user: " + Build.USER);
        Log.i("TAG", "BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG", "INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("TAG", "SDK  " + Build.VERSION.SDK);
        Log.i("TAG", "BOARD: " + Build.BOARD);
        Log.i("TAG", "BRAND " + Build.BRAND);
        Log.i("TAG", "HOST " + Build.HOST);
        Log.i("TAG", "FINGERPRINT: " + Build.FINGERPRINT);
        Log.i("TAG", "Version Code: " + Build.VERSION.RELEASE);


//        ivImage = (ImageView) findViewById(R.id.image_feedback);


//        btn_add_image = (Button) findViewById(R.id.btn_add_image);
//        btn_add_image.setVisibility(View.VISIBLE);

//        btn_add_image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                //     galleryIntent();
//                getPermissions();
//            }
//        });

        btn_sendMail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //  FirebaseAnalytics Adding
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("Feedback_sent", null);
                }

                try {
                    String versionName = BuildConfig.VERSION_NAME;
                    String message = txt_msg.getText().toString();

                    Intent email = new Intent(Intent.ACTION_SEND_MULTIPLE);

                    if (Constants.type == Constants.Type.LIFEPLUS) {
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@lifeplus.com.bd"});
                    } else if (Constants.type == Constants.Type.HEMAS) {
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@hemashospitals.com", "ronaldg@hemashospitals.com", "saravanacanth@hemashospitals.com"});
                    } else {
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@ayubo.life"});
                    }


                    email.putExtra(Intent.EXTRA_SUBJECT, emailHeading);
                    sb = new StringBuilder();
                    sb.append("Message : " + message);
                    sb.append('\n');
                    sb.append("name : " + name);
                    sb.append('\n');
                    sb.append("email : " + userEmail);
                    sb.append('\n');
                    sb.append("mobile : " + mobile);
                    sb.append('\n');
                    sb.append("Model : " + phoneModel);
                    sb.append('\n');
                    sb.append("Manufacture : " + manufacture);
                    sb.append('\n');
                    sb.append("OS API : " + osVesion);
                    sb.append('\n');
                    sb.append("OS Version : " + appVersion);
                    sb.append('\n');
                    sb.append("App Name : " + appName);
                    sb.append('\n');
                    sb.append("App Version : " + versionName);
                    sb.append('\n');


                    sb.append(sentFrom);
                    String body = sb.toString();
                    System.out.println("==============ssssbbbb================" + sb);
                    System.out.println(body);
                    System.out.println("==============================");
                    email.putExtra(Intent.EXTRA_TEXT, body);
                    //email.setType("message/rfc822");
                    email.setType("message/rfc822");
                    //need this to prompts email client only
                    if (message.equals("")) {
                        Toast.makeText(HelpFeedbackActivity.this, "Please enter message",
                                Toast.LENGTH_LONG).show();
                        return;

                    } else {

//                        shareToGMail(body);


//                        if (image_absolute_path == null) {
                        if (imageArr.size() == 0) {
                            // email.setType("image/png");

                            email.setType("text/plain");
                            email.putExtra(android.content.Intent.EXTRA_TEXT, body);
                            final PackageManager pm = getPackageManager();
                            final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                            ResolveInfo best = null;
                            for (final ResolveInfo info : matches)
                                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                    best = info;
                            if (best != null)
                                email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                            startActivity(email);
                            finish();

                            //   startActivity(Intent.createChooser(email, "Choose an Email client :"));


                        } else {

//                            File F = new File(image_absolute_path);
//                            Uri uri = Uri.fromFile(F);
//                            email.putExtra(Intent.EXTRA_STREAM, uri);
                            // email.setType("image/png");

                            email.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageArr);
                            email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //  email.setType("text/plain");
                            email.putExtra(android.content.Intent.EXTRA_TEXT, body);
                            final PackageManager pm = getPackageManager();
                            final List<ResolveInfo> matches = pm.queryIntentActivities(email, 0);
                            ResolveInfo best = null;
                            for (final ResolveInfo info : matches)
                                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                                    best = info;
                            if (best != null)
                                email.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                            startActivity(email);

                            finish();
                            //  startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        }
                    }

                } catch (Exception e) {
                    System.out.println("==============================" + e);
                }
            }
        });

    }

    public void createUploadDocumentList() {
        mUploadDocumentList = new ArrayList<>();
        imageArr = new ArrayList<>();
    }


    public void buildRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.activity_help_feedback_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mUploadImageAdapter = new UploadImageAdapter(mUploadDocumentList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setOnItemClickListener(new UploadImageAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                if (mUploadDocumentList.size() == 0) {
                    activity_help_feedback_recycler_view_layout.setVisibility(View.GONE);
                }
            }
        });
    }


    public void removeItem(int position) {
        mUploadDocumentList.remove(position);
        imageArr.remove(position);
        mUploadImageAdapter.notifyItemRemoved(position);
    }

    public void insertItem(int position, Bitmap imageView) {
        mLayoutManager.scrollToPosition(0);
        mUploadDocumentList.add(position, imageView);
        mUploadImageAdapter.notifyItemInserted(position);
    }


    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

                galleryIntent();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HelpFeedbackActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(HelpFeedbackActivity.this);
                    builder.setTitle("Need external storage Permission");
                    builder.setMessage("This app needs external storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(HelpFeedbackActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(HelpFeedbackActivity.this, "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("==================================" + resultCode);
        System.out.println("==================================");
        System.out.println("==================================");

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

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

//        ivImage.setImageBitmap(thumbnail);
        // btn_add_image.setVisibility(View.GONE);
    }

    public void getPermissions() {
        if (ActivityCompat.checkSelfPermission(HelpFeedbackActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HelpFeedbackActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpFeedbackActivity.this);
                builder.setTitle("Need external storage Permission");
                builder.setMessage("This app needs external storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HelpFeedbackActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpFeedbackActivity.this);
                builder.setTitle("Need external storage Permission");
                builder.setMessage("This app needs external storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", HelpFeedbackActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(HelpFeedbackActivity.this, "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(HelpFeedbackActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            galleryIntent();
        }

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

//        Uri selectedImageURI = data.getData();
//        setProfileImage(selectedImageURI.toString());
//        selectedImageURIA = selectedImageURI;
        if (data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    imageArr.add(imageUri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        insertItem(0, bitmap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                imageArr.add(imageUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    insertItem(0, bitmap);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    private void setProfileImage(String selectedImageUri) {
        String selectedImagePath = null;

        Cursor cursor;
        String[] projection = {MediaStore.MediaColumns.DATA};

        Uri ur = Uri.parse(selectedImageUri);

        cursor = getContentResolver().query(ur, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);

            // Ram.setHomeFragmentNumber("1");
            System.out.println(selectedImagePath);
            image_absolute_path = selectedImagePath;
            System.out.println("============Path=================" + image_absolute_path);

            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            //bm = BitmapFactory.decodeFile(selectedImagePath, options);

            //=========================
            int photoW = options.outWidth;
            int photoH = options.outHeight;
//            int targetW = ivImage.getWidth();
//            int targetH = ivImage.getHeight();

            /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
//            if ((targetW > 0) || (targetH > 0)) {
//                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//            }

            /* Set bitmap options to scale the image decode target */
            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;
            options.inPurgeable = true;

            /* Decode the JPEG file into a Bitmap */
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            //Ram.setProfileImageUri(selectedImageUri.toString());
            // Ram.setProfileImagePath(selectedImagePath);
            System.out.println("======================IMAGE 0=========================" + selectedImageUri.toString() + "   " + selectedImagePath);
            //   int orientation = getOrientation(HelpFeedbackActivity.this, Uri.parse(selectedImageUri), selectedImagePath);
            //=======================
            //  int orientation=getOrientation(this,selectedImageUri);
//            ivImage.setImageBitmap(bm);
//            System.out.println("============================orientation=======================================" + orientation);
//            Matrix matrix = new Matrix();
//            if (orientation == 90) {
//                matrix.postRotate(90);
//                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                ivImage.setImageBitmap(bm);
//            }
//            if (orientation == 180) {
//                matrix.postRotate(180);
//                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                ivImage.setImageBitmap(bm);
//            }
//            if (orientation == 270) {
//                matrix.postRotate(270);
//                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                ivImage.setImageBitmap(bm);
//            } else {
//                ivImage.setImageBitmap(bm);
//            }
            //  btn_add_image.setVisibility(View.GONE);
        } else {
            //  Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.aaa2);
            //  Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
            // ivImage.setImageBitmap(myBitmap);
        }


        //========================
    }

    public int getOrientation(Context context, Uri photoUri, String path) {
        int rotate = 0;
        try {
            //getContentResolver().notifyChange(photoUri, null);
            //File imageFile = new File(photoUri.getAbsolutePath());
            File imageFile = new File(path);

            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            //Log.v(Common.TAG, "Exif orientation: " + orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
        /****** Image rotation ****/

    }

    public void onHelpDeleteImageClick(View view) {

    }

    public void onHelpAddImageClick(View view) {
        activity_help_feedback_recycler_view_layout.setVisibility(View.VISIBLE);
        getPermissions();
    }

}
