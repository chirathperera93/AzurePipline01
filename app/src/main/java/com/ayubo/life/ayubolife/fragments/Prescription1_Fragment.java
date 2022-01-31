package com.ayubo.life.ayubolife.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

//import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Prescription1_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Prescription1_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Prescription1_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btn_upload;
    PopupWindow changeStatusPopUp;
    private Uri fileUri; // file url to store image/video

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 123;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ProgressDialog prgDialog;
    File compressedImageFile;
    String image_absolute_path, userid_ExistingUser;
    private OnFragmentInteractionListener mListener;
    ImageView ivImage, ivCamera;
    long totalSize = 0;
    SharedPreferences prefs;
    private String userChoosenTask;
    Point point;

    Toast toastt;
    LayoutInflater inflatert;
    View layoutt;
    TextView textt;


    LinearLayout drop_menu_holder;

    public Prescription1_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Prescription1_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Prescription1_Fragment newInstance(String param1, String param2) {
        Prescription1_Fragment fragment = new Prescription1_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());

        View view = inflater.inflate(R.layout.fragment_prescription1_,
                container, false);

        prefs = getContext().getSharedPreferences("ayubolife", Context.MODE_PRIVATE);
        userid_ExistingUser = prefs.getString("uid", "0");
        //     hasToken= prefs.getString("hashToken", "0");


        inflatert = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflatert = getLayoutInflater(view.getRootView().get);
        layoutt = inflatert.inflate(R.layout.custom_toast,
                (ViewGroup) view.findViewById(R.id.custom_toast_container));
        textt = (TextView) layoutt.findViewById(R.id.text);
        toastt = new Toast(getContext());
        toastt.setGravity(Gravity.CENTER, 0, -150);
        toastt.setDuration(Toast.LENGTH_LONG);
        toastt.setView(layoutt);


        drop_menu_holder = (LinearLayout) view.findViewById(R.id.drop_menu_holder);
        final ImageButton main_menu_part_four_image = (ImageButton) view.findViewById(R.id.btn_more);
        main_menu_part_four_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] location = new int[2];
                drop_menu_holder.getLocationOnScreen(location);

                //Initialize the Point with x, and y positions
                Display mdisp = getActivity().getWindowManager().getDefaultDisplay();
                Point mdispSize = new Point();
                mdisp.getSize(mdispSize);
                int maxX = mdispSize.x;
                int maxY = mdispSize.y;

                Double yy = maxY * 0.275;
                // Double d = new Double(1.23);
                int yval = yy.intValue();
                int fyval = maxY - yval;
                System.out.println(yy + "==========================Full Y is " + maxY + " From Bottum is : " + yval + " Position is : " + fyval);
                point = new Point();
                point.x = location[0];
                //  point.y = fyval;
                point.y = 350;

                System.out.println("==========================X is " + point.x + "  Y is : " + point.y);

                showStatusPopup(getActivity(), point);

            }
        });

        prgDialog = new ProgressDialog(getContext());
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showCamera();
                //  selectImage();

            }
        });

        ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                captureImage_camera();
                //  selectImage();

            }
        });
        TextView tems = (TextView) view.findViewById(R.id.tems);
        SpannableString content = new SpannableString("Terms & Conditions");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tems.setText(content);


        //  textView.setText(R.string.hello_blank_fragment);
        return view;
    }

    private void showStatusPopup(final Activity context, Point p) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.drop_menu_holder);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.main_menu_popup, null);
        //  (R.layout.main_menu_popup,
        Button b = (Button) layout.findViewById(R.id.btn_pres_showweb);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                goToHomeLLLLL();

            }
        });


        // Creating the PopupWindow
        changeStatusPopUp = new PopupWindow(context);
        changeStatusPopUp.setContentView(layout);
        changeStatusPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeStatusPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -20;
        int OFFSET_Y = 20;

        //Clear the default translucent background
        changeStatusPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeStatusPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);
    }

    private void goToHomeLLLLL() {

        changeStatusPopUp.dismiss();

        Ram.setIsOne_Medicine(false);
        Ram.setIsTwo_Medicine(true);


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragOne = new Medicine_Holder2_Fragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean("shouldYouCreateAChildFragment", true);
        fragOne.setArguments(arguments);
        ft.replace(R.id.fr_medicine__holder_layout, fragOne);
        ft.commit();


    }

    private void captureImage_camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void selectImage() {
        galleryIntent();
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                boolean result=Utility.checkPermission(getContext());
//
//                if (items[item].equals("Take Photo")) {
//                    userChoosenTask ="Take Photo";
//                    if(result)
//                        cameraIntent();
//
//                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask ="Choose from Library";
//                    if(result)
//                        galleryIntent();
//
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void showCamera() {
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestCameraPermission();
        } else {
            // Camera permissions is already available, show the camera preview.
            selectImagePopup();
        }
        // END_INCLUDE(camera_permission)

    }

    private void selectImagePopup() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("=======inside=====onActivityResult=============================");
        String ur = null;
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//          //  String ur= fileUri.getPath();
//        }

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                ur = fileUri.getPath();
                System.out.println("========URI======================" + ur);
                image_absolute_path = fileUri.getPath();
                uploadProfileImageAsFile();
                //onCaptureImageResult(data);
            } else if (requestCode == SELECT_FILE) {
                System.out.println("========URI======================" + ur);
                onSelectFromGalleryResult(data);
            }

        }
    }


    private void onCaptureImageResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                Uri selectedImageUri = data.getData();

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                //  db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
                //  Ram.setProfileImageUri(selectedImageUri.toString());
                System.out.println("======================destination===================================" + destination.toString());
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

                ivImage.setImageBitmap(thumbnail);


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                // Toast.makeText(getContext(), "Permission Issue 02..",Toast.LENGTH_SHORT).show();
                // We've been denied once before. Explain why we need the permission, then ask again.
                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);


            } else {
                //  Toast.makeText(getContext(), "Permission Issue 03..",Toast.LENGTH_SHORT).show();
                // We've never asked. Just do it.
                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            Uri selectedImageUri = data.getData();

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            //  db.addProfileImageToDB(userid_ExistingUser, selectedImageUri.toString(), destination.toString());
            //  Ram.setProfileImageUri(selectedImageUri.toString());
            System.out.println("======================destination===================================" + destination.toString());
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

            ivImage.setImageBitmap(thumbnail);
            uploadProfileImageAsFile();
        }


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
        String[] proj = {MediaStore.Images.Media.DATA};

//        CursorLoader cursorLoader = new CursorLoader( getContext(),contentUri, proj, null, null, null);
//        Cursor cursor = cursorLoader.loadInBackground();
//
//        int column_index =
//                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //  data.getData();
        // Uri selectedImageUri = data.getData();
        //  String urii= getRealPathFromURI(data.getData());
        //   String uri=  getRealPathFromURI(getContext(),data.getData());
        //    System.out.println("============+++++++++++++++++++++++++++++++++++++======================2======="+uri);
        //    System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
        Cursor cursor;

        String[] projection = {MediaStore.MediaColumns.DATA};
        String selectedImagePath = null;

        cursor = getContext().getContentResolver().query(selectedImageUri, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            selectedImagePath = cursor.getString(column_index);
        }
        return selectedImagePath;
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis() + ".jpg");


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + System.currentTimeMillis() + ".jpg");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //  Ram.setHomeFragmentNumber("1");
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        data.getData();
        Uri selectedImageUri = data.getData();
        String urii = getRealPathFromURI(data.getData());
        String uri = getRealPathFromURI(getContext(), data.getData());
        System.out.println("============+++++++++++++++++++++++++++++++++++++======================2=======" + uri);
        System.out.println("==================selectedImageUri Gallery================================" + selectedImageUri.toString());
        Cursor cursor;


        String[] projection = {MediaStore.MediaColumns.DATA};
        String selectedImagePath = null;

//        Toast.makeText(this, selectedImageUri.toString(), Toast.LENGTH_SHORT)
//                .show();

        cursor = getContext().getContentResolver().query(selectedImageUri, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            selectedImagePath = cursor.getString(column_index);
            image_absolute_path = selectedImagePath;
            System.out.println("=============================" + image_absolute_path);

//            Toast.makeText(this, selectedImagePath, Toast.LENGTH_SHORT)
//                    .show();


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
            int targetW = ivImage.getWidth();
            int targetH = ivImage.getHeight();

            /* Figure out which way needs to be reduced less */
            int scaleFactor = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Set bitmap options to scale the image decode target */
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            options.inPurgeable = true;

            /* Decode the JPEG file into a Bitmap */
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            uploadProfileImageAsFile();
        } else {
            Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.camera);
            Bitmap circularBitmap = getRoundedCornerBitmap(myBitmap, 150);
            ivImage.setImageBitmap(circularBitmap);
        }
        //========================

    }

    private void uploadProfileImageAsFile() {

        if (Utility.isInternetAvailable(getContext())) {

            uploadProfileImage();

        } else {
            Toast.makeText(getContext(), "No internet connection. Can't upload profile image. ",
                    Toast.LENGTH_LONG).show();
            // finish();
        }

    }

    private void uploadProfileImage() {
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {

            File sourceFile = new File(image_absolute_path);

//            try {
//                compressedImageFile = new Compressor(getContext()).compressToFile(sourceFile);
//                compressedImageFile = new Compressor().compress(this, sourceFile);
            compressedImageFile = sourceFile;

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
                //  Bitmap bitmap = Bitmap.createScaledBitmap(capturedImage, width, height, true);
                // Extra parameters if you want to pass to server
                entity.addPart("userid", new StringBody(userid_ExistingUser));
                //   entity.addPart("prescription", new StringBody("png"));

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
                    System.out.println("============================" + responseString);
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

    //    private String getRealPathFromURI(String contentURI) {
//        Uri contentUri = Uri.parse(contentURI);
//        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
//        if (cursor == null) {
//            return contentUri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            return cursor.getString(index);
//        }
//    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String compressImageTodayNew(String imageUri) {
        Uri contentUri = Uri.parse(imageUri);
        String filePath = getRealPathFromURI(contentUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }
//====================================================================================

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message).setTitle("Image Uploaded Status")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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


    public int getOrientation5(Context context, Uri photoUri, String path) {
        int rotate = 0;
        try {
            //getContentResolver().notifyChange(photoUri, null);
            //File imageFile = new File(photoUri.getAbsolutePath());
            File imageFile = new File(path);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());


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

    private void requestCameraPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        // END_INCLUDE(camera_permission_request)
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
