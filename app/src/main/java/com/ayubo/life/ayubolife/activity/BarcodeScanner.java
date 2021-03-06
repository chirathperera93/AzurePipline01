package com.ayubo.life.ayubolife.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.utility.Ram;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;

/**
 * Created by kvprasad on 10/3/2015.
 */
public class BarcodeScanner extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private PrefManager pref;
    private Button scanButton;
    private ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        pref = new PrefManager(this);
        initControls();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(BarcodeScanner.this, mCamera, previewCb,
                autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanButton = (Button) findViewById(R.id.ScanButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
//        Toast.makeText(BarcodeScanner.this, "Back pressed in Scanner onKeyDown",
//                Toast.LENGTH_SHORT).show();


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(BarcodeScanner.this, "Back pressed in Scanner onBackPressed",
//                Toast.LENGTH_SHORT).show();
        Ram.setSideMenuItemNumber("0");
//        Intent tempIntent = new Intent(this, NewHomeWithSideMenuActivity.class);
//        Intent tempIntent = new Intent(this, LifePlusProgramActivity.class);
//        Intent tempIntent = new Intent(getBaseContext(), NewDiscoverActivity.class);
        Intent tempIntent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
        startActivity(tempIntent);
        finish();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {

                    Log.i("<<<<<<Asset Code>>>>> ",
                            "<<<<Bar Code>>> " + sym.getData());
                    String scanResult = sym.getData().trim();
                    if (scanResult.equals(MAIN_URL_APPS)) {

                        Ram.setSideMenuItemNumber("8");
//                         Intent tempIntent = new Intent(BarcodeScanner.this, NewHomeWithSideMenuActivity.class);
//                        Intent tempIntent = new Intent(BarcodeScanner.this, LifePlusProgramActivity.class);
//                        Intent tempIntent = new Intent(getBaseContext(), NewDiscoverActivity.class);
                        Intent tempIntent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                        startActivity(tempIntent);
                        finish();

//                         pref.setQRLink("1");
//                         startActivity(new Intent(BarcodeScanner.this, WelcomeHome.class));
//                         Toast.makeText(BarcodeScanner.this, scanResult,
//                                 Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BarcodeScanner.this, "The QR code you scanned is not associated with ayubo.life",
                                Toast.LENGTH_SHORT).show();
                    }


                    barcodeScanned = true;

                    break;
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    private void showAlertDialog(String message) {

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }

}
