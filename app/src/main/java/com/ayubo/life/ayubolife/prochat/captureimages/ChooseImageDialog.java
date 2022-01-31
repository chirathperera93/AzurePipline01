package com.ayubo.life.ayubolife.prochat.captureimages;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayubo.life.ayubolife.BuildConfig;
import com.ayubo.life.ayubolife.R;

import java.io.File;


/**
 * Created by dilan on 1/10/2016.
 */
public class ChooseImageDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ChooseImageD";

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111;
    private static final int REQUEST_CODE_PERMISSION_STORAGE = 1;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 2;
    private static final int REQUEST_CODE_PERMISSION_CAMERA_STORAGE = 3;

    private Uri mFileUri;
    private OnProfilePicFoundListener mClick;
    private File mediaFile;

    public static ChooseImageDialog newInstance(OnProfilePicFoundListener click) {
        ChooseImageDialog dialog = new ChooseImageDialog();
        dialog.mClick = click;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View view = inflater.inflate(R.layout.dialog_choose_image, container, false);

        view.findViewById(R.id.cancel).setOnClickListener(this);
        view.findViewById(R.id.take_picture).setOnClickListener(this);
        view.findViewById(R.id.gallery).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;

            case R.id.gallery:
                openGallery();
                break;

            case R.id.take_picture:
                startCamera();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // L.d(TAG, "onActivityResult requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + (data == null ? "null" : data.toString()));

        if (requestCode == REQUEST_GALLERY || requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mediaFile != null) {
                    mClick.onProfilePicFound(Uri.fromFile(mediaFile));
                } else {
                    if (data != null) {
                        mClick.onProfilePicFound(data.getData());

                    }
                }
                dismiss();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else if (requestCode == REQUEST_CODE_PERMISSION_CAMERA_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else if (requestCode == REQUEST_CODE_PERMISSION_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        }
    }


    private void startCamera() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_CAMERA_STORAGE);
                return;
            }

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
                return;
            }
        }
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mFileUri = getOutputMediaFile();
        addImageToGallery(mFileUri.getPath(), getContext());
        if (mFileUri != null) {
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
            intent1.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent1, REQUEST_CAMERA);
        } else {
            // L.d(TAG, "no file");
        }
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, filePath);

        try {
            context.getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.d("Choose Camera Dialog", " Permission is granted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_GALLERY);
        }

    }

    /**
     * Determine if we can use the SD Card to store the temporary file.  If not then use
     * the internal cache directory.
     *
     * @return the absolute path of where to store the file
     */
    public static File getTempDirectoryPath(Context ctx) {
        File cache = null;

        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + ctx.getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = ctx.getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        if (!cache.exists()) {
            cache.mkdirs();
        }

        return cache;
    }


    public Uri getOutputMediaFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name

        String imageStoragePath = dir + "/";
        mediaFile = new File(imageStoragePath + System.currentTimeMillis() + getString(R.string.app_name) + ".jpg");
        Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID, mediaFile);
        return photoURI;
    }

    private void addImageToGallery(File file) {
        MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public static void createDirectory(String filePath) {
        if (!new File(filePath).exists()) {
            new File(filePath).mkdirs();
        }
    }

}
