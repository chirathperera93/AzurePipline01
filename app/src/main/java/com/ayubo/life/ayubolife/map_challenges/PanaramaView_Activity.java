package com.ayubo.life.ayubolife.map_challenges;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ayubo.life.ayubolife.R;

//import com.lespinside.simplepanorama.view.SphericalView;
//import com.panoramagl.utils.PLUtils;

public class PanaramaView_Activity extends AppCompatActivity {
    //  private SphericalView sphericalView;
    String URL;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panarama_view_);

        //   sphericalView = (SphericalView) findViewById(R.id.spherical_view);
        // sphericalView.setPanorama(PLUtils.getBitmap(this, R.raw.sunset_at_pier), false);

        URL = getIntent().getStringExtra("URL");
        System.out.println("========URL==================" + URL);
        // new DownloadImage().execute(URL);
        // SUPPORTED IMAGE SIZES ======.   2048 x 1024 , 1024 * 512 , 512 * 256
        progressBar = (ProgressBar) findViewById(R.id.progress);
        //    getImage2(url2);
        //  sphericalView.setKeyboardNavigationCluster(true);
        //  sphericalView.setElevation(90);
        //  sphericalView.setDefaultRotationSensibility(250);
        //  sphericalView.setInertiaEnabled(false);
        //   sphericalView.setDefaultYaw(120);
        //  sphericalView.setAccelerometerEnabled(true);
        //  sphericalView.setInertiaEnabled(false);
        //  sphericalView.setZoomEnabled(true);
        //  sphericalView.setAccelerometerEnabled(true);
        //  sphericalView.setInertiaEnabled(true);
        //  sphericalView.setZoomEnabled(true);
        // sphericalView.setResetEnabled(true);
        // sphericalView.getAnimation().setZAdjustment(120);
    }

    //    private void getImage2(String URL) {
//
//        try {
//            Glide
//                    .with(this).load(URL)
//                    .asBitmap()
//                    .fitCenter()
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                            progressBar.setVisibility(View.GONE);
//                            if(bitmap!=null){
//                                sphericalView.setPanorama(bitmap, true);
//                            }
//
//                        }
//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            // Do something.
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "Error "+e,
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void getImage(String URL) {


//
//        ImageRequest request = new ImageRequest(URL,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap bitmap) {
//                       // mImageView.setImageBitmap(bitmap);
//                        sphericalView.setPanorama(bitmap, true);
//                    }
//                }, 0, 0, null,
//                new Response.ErrorListener() {
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
//                     //   mImageView.setImageResource(R.drawable.image_load_error);
//                    }
//                });
//        App.getInstance().addToRequestQueue(request);
//       // MySingleton.getInstance(this).addToRequestQueue(request);
//    }
// Access the RequestQueue through your singleton class.


//    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
//        ProgressDialog mProgressDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
//            mProgressDialog = new ProgressDialog(PanaramaView_Activity.this);
//            // Set progressdialog title
//            mProgressDialog.setTitle("Please wait");
//            // Set progressdialog message
//            mProgressDialog.setMessage("Loading...");
//            mProgressDialog.setIndeterminate(false);
//            // Show progressdialog
//            mProgressDialog.show();
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... URL) {
//
//            String imageURL = URL[0];
//
//            Bitmap bitmap = null;
//            try {
//                // Download Image from URL
//                InputStream input = new java.net.URL(imageURL).openStream();
//                // Decode Bitmap
//                bitmap = BitmapFactory.decodeStream(input);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            // Set the bitmap into ImageView
//            //   image.setImageBitmap(result);
//            // Close progressdialog
//            sphericalView.setPanorama(result, true);
//            mProgressDialog.dismiss();
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        sphericalView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        sphericalView.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        sphericalView.onDestroy();
//    }
    }
}
