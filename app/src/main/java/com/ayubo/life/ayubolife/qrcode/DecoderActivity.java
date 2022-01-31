package com.ayubo.life.ayubolife.qrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.QRWebViewActivity;
import com.ayubo.life.ayubolife.login.SplashScreen;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.Ram;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView.OnQRCodeReadListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class DecoderActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback, OnQRCodeReadListener {


    private ViewGroup mainLayout;
    ImageButton btn_backImgBtn;
    // private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private CheckBox flashlightCheckBox;
    private CheckBox enableDecodingCheckBox;
    private PointsOverlayView pointsOverlayView;
    LinearLayout lay_btnBack;
    Toast toastMessage;
    boolean isFirst = false;
    Handler mHandler;
    long oldTime;
    final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_decoder);
        System.out.println("===================");
        System.out.println("=======QR code scanner onCreate============");
        System.out.println("===================");
        Ram.setSideMenuItemNumber("25");
        isFirst = false;
        String error = getIntent().getStringExtra("error");
        if (error == null) {

        }
        if (error == "new") {
            Toast.makeText(DecoderActivity.this, "The QR code you scanned is not associated with ayubo.life", Toast.LENGTH_SHORT).show();
        }
        mainLayout = (ViewGroup) findViewById(R.id.main_layout);

        mHandler = new Handler();

        Date date = new Date();
        oldTime = date.getTime();

        checkFullPermission();


//    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
//
//    } else {
//      requestCameraPermission();
//    }
    }

    private void checkFullPermission() {
        if (ContextCompat
                .checkSelfPermission(DecoderActivity.this,
                        Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (DecoderActivity.this, Manifest.permission.CAMERA)) {

                Snackbar.make(DecoderActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 23)
                                    requestPermissions(
                                            new String[]{Manifest.permission.CAMERA},
                                            PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            initQRCodeReaderView();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // qrCodeReaderView.startCamera();
        System.out.println("===============onResume===called============");
        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //   qrCodeReaderView.stopCamera();
        System.out.println("===============onPause===called============");
        // isFirst=false;
        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == PERMISSIONS_MULTIPLE_REQUEST) {

                    initQRCodeReaderView();
                }

            }
        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_MULTIPLE_REQUEST) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed
    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        //   resultTextView.setText(text);
        // url.startsWith
        System.out.println("=======onQRCodeRead ===============");

        if (!isFirst) {
            isFirst = true;

            if (text.contains(ApiClient.BASE_URL)) {
                Intent intent = new Intent(getBaseContext(), QRWebViewActivity.class);
                System.out.println("===============Scanned===============" + text);
                intent.putExtra("URL", text);
                startActivity(intent);
            } else if (text.contains("https://devo.ayubo.life/")) {
                Intent intent = new Intent(getBaseContext(), QRWebViewActivity.class);
                System.out.println("===============Scanned===============" + text);
                intent.putExtra("URL", text);
                startActivity(intent);
            } else if (text.contains("https://livehappy.ayubo.life")) {
                Intent intent = new Intent(getBaseContext(), QRWebViewActivity.class);
                System.out.println("===============Scanned===============" + text);
                intent.putExtra("URL", text);
                startActivity(intent);
            } else {

                //  FirebaseAnalytics Adding
                Bundle bWorkout = new Bundle();
                bWorkout.putString("status", "unsuccessful");
                if (SplashScreen.firebaseAnalytics != null) {
                    SplashScreen.firebaseAnalytics.logEvent("QRCode_read", bWorkout);
                }

            }

        } else {
            System.out.println("===============No need to Scanne===============" + text);
        }

    }


    private void initQRCodeReaderView() {
        View content = getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);

        qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);
        //  resultTextView = (TextView) content.findViewById(R.id.result_text_view);
        flashlightCheckBox = (CheckBox) content.findViewById(R.id.flashlight_checkbox);
        enableDecodingCheckBox = (CheckBox) content.findViewById(R.id.enable_decoding_checkbox);
        pointsOverlayView = (PointsOverlayView) content.findViewById(R.id.points_overlay_view);

        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();

        flashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setTorchEnabled(isChecked);
            }
        });
        enableDecodingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setQRDecodingEnabled(isChecked);
            }
        });
        qrCodeReaderView.startCamera();
    }
}