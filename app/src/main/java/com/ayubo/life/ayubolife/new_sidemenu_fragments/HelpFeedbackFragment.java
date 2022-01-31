package com.ayubo.life.ayubolife.new_sidemenu_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.BarcodeScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFeedbackFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   String image_absolute_path;
    private static final int WRITE_EXTERNAL_STORAGE = 132;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private ImageView ivImage;
    private String userChoosenTask;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int REQUEST_PERMISSION_SETTING = 102;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   Button btn_add_image,btn_sendMail;
    EditText txt_msg;Uri selectedImageURIA;
    private OnFragmentInteractionListener mListener;

    public HelpFeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFeedbackFragment newInstance(String param1, String param2) {
        HelpFeedbackFragment fragment = new HelpFeedbackFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help_feedback,
                container, false);
        permissionStatus = getActivity().getSharedPreferences("permissionStatus",MODE_PRIVATE);

         txt_msg=(EditText)view.findViewById(R.id.txt_msg);
        btn_sendMail=(Button)view.findViewById(R.id.btn_sendmail);

        Log.i("TAG", "SERIAL: " + Build.SERIAL);
        Log.i("TAG","MODEL: " + Build.MODEL);
        Log.i("TAG","ID: " + Build.ID);
        Log.i("TAG","Manufacture: " + Build.MANUFACTURER);
        Log.i("TAG","brand: " + Build.BRAND);
        Log.i("TAG","type: " + Build.TYPE);
        Log.i("TAG","user: " + Build.USER);
        Log.i("TAG","BASE: " + Build.VERSION_CODES.BASE);
        Log.i("TAG","INCREMENTAL " + Build.VERSION.INCREMENTAL);
        Log.i("TAG","SDK  " + Build.VERSION.SDK);
        Log.i("TAG","BOARD: " + Build.BOARD);
        Log.i("TAG","BRAND " + Build.BRAND);
        Log.i("TAG","HOST " + Build.HOST);
        Log.i("TAG","FINGERPRINT: "+Build.FINGERPRINT);
        Log.i("TAG","Version Code: " + Build.VERSION.RELEASE);

        ivImage = (ImageView) view.findViewById(R.id.image_feedback);


        btn_add_image=(Button)view.findViewById(R.id.btn_add_image);
        btn_add_image.setVisibility(View.VISIBLE);

        btn_add_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
           //     galleryIntent();
                getPermissions();
            }
        });

        btn_sendMail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String message = txt_msg.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "indikas@wakensys.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Test mail from Ayubo.life");
                email.putExtra(Intent.EXTRA_TEXT, message);
                //need this to prompts email client only
                File F = new File(image_absolute_path);
                Uri uri = Uri.fromFile(F);


                email.putExtra(Intent.EXTRA_STREAM, uri);

                email.setType("image/png");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });

        return view;

    }
    private void galleryIntent()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Need external storage Permission");
                    builder.setMessage("This app needs external storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);

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
                    Toast.makeText(getContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("=================================="+resultCode);
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

        ivImage.setImageBitmap(thumbnail);
        btn_add_image.setVisibility(View.GONE);
    }

    public void getPermissions(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need external storage Permission");
                builder.setMessage("This app needs external storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Need external storage Permission");
                builder.setMessage("This app needs external storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getActivity(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
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
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            galleryIntent();
        }

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Uri selectedImageURI = data.getData();
        setProfileImage(selectedImageURI.toString());
        selectedImageURIA=selectedImageURI;
    }
    private void setProfileImage(String selectedImageUri){
        String selectedImagePath=null;

        Cursor cursor;
        String[] projection = {MediaStore.MediaColumns.DATA};

        Uri ur=Uri.parse(selectedImageUri);

        cursor = getActivity().getContentResolver().query(ur, projection, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);

           // Ram.setHomeFragmentNumber("1");
            System.out.println(selectedImagePath);
            image_absolute_path=selectedImagePath;
            System.out.println("============Path================="+image_absolute_path);

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
            options.inSampleSize = 5;
            options.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            //Ram.setProfileImageUri(selectedImageUri.toString());
           // Ram.setProfileImagePath(selectedImagePath);
            System.out.println("======================IMAGE 0=========================" + selectedImageUri.toString() + "   " + selectedImagePath);
            int orientation = getOrientation(getActivity(), Uri.parse(selectedImageUri), selectedImagePath);
            //=======================
            //  int orientation=getOrientation(this,selectedImageUri);

            System.out.println("============================orientation=======================================" + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 90) {
                matrix.postRotate(90);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                ivImage.setImageBitmap(bm);
            }
            if (orientation == 180) {
                matrix.postRotate(180);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                ivImage.setImageBitmap(bm);
            }
            if (orientation == 270) {
                matrix.postRotate(270);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                ivImage.setImageBitmap(bm);
            } else {
                ivImage.setImageBitmap(bm);
            }
            btn_add_image.setVisibility(View.GONE);
        }else{
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
