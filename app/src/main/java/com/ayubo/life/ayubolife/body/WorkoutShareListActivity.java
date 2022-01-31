package com.ayubo.life.ayubolife.body;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.common.SetDiscoverPage;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.utility.AndroidMultiPartEntity;
import com.ayubo.life.ayubolife.utility.LruBitmapCache;
import com.ayubo.life.ayubolife.utility.Ram;
import com.ayubo.life.ayubolife.utility.Utility;
import com.ayubo.life.ayubolife.webrtc.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class WorkoutShareListActivity extends AppCompatActivity {
    CustomAdapter adapter;
    ListView list;
    File newf;
    long totalSize = 0;
    Bitmap b = null;
    ProgressDialog prgDialog;
    String sMets_ToIntent, sCals_ToIntent, sDis_ToIntent, sDur_ToIntent, statusFromServiceAPI_db;
    ImageButton btn_backImgBtn;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    ArrayList<ShareEntity> data;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    Context globalContext;
    private Context _context;
    int pos;
    ShareEntity en;
    TextView btn_back_btn;
    String image_absolute_path, userid_ExistingUser, shareMessage, postType, message, communityID;
    ImageLoader imageLoader;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 101;
    int SELECT_FILE = 1;


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(globalContext);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    private void checkPermissionOpenSDCard() {
        if (ContextCompat
                .checkSelfPermission(WorkoutShareListActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (WorkoutShareListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(WorkoutShareListActivity.this.findViewById(android.R.id.content),
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
            newf = saveBitmap(b, "screnshot.png");


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    newf = saveBitmap(b, "screnshot.png");

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
        try {
            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {

                    newf = saveBitmap(b, "screnshot.png");

                }

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_share_list);

        list = (ListView) findViewById(R.id.health_track_list);
        HashMap<String, String> song = new HashMap<String, String>();

        b = Ram.getMapSreenshot();
        shareMessage = Ram.getShareMessage();

        checkPermissionOpenSDCard();

//        if (Build.VERSION.SDK_INT >= 23) {
//            checkPermissionOpenSDCard();
//        } else {
//            newf= persistImage(b,"screnshot.png");
//        }


        pref = new PrefManager(this);
        userid_ExistingUser = pref.getLoginUser().get("uid");
        prgDialog = new ProgressDialog(WorkoutShareListActivity.this);

        data = new ArrayList<ShareEntity>();

        Bitmap bitmap1 = BitmapFactory.decodeResource(WorkoutShareListActivity.this.getResources(), R.drawable.runnings);


        data.add(new ShareEntity("001", "Public", "Public", "Wakensys_Logo_400x400.jpg", "1", "ayubo.life", "image"));
        data.add(new ShareEntity("002", "Share external", "Share external", "Wakensys_Logo_400x401.jpg", "1", "Facebook,WhatsApp,Viber,Email...", "image"));

        if (Utility.isInternetAvailable(this)) {
            progressDialog = new ProgressDialog(WorkoutShareListActivity.this);
            progressDialog.show();
            progressDialog.setMessage("Loading..");
            updateOnlineWorkoutDetails task = new updateOnlineWorkoutDetails();
            task.execute(new String[]{ApiClient.BASE_URL_live});
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s = list.getItemAtPosition(position).toString();
                en = data.get(position);
                pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutShareListActivity.this);
                LayoutInflater inflater = (LayoutInflater) WorkoutShareListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layoutView = inflater.inflate(R.layout.alert_confirm_shareworkout, null, false);
                builder.setView(layoutView);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (pos == 0) {
                            communityID = "";
                            postType = "1";
                            message = Ram.getShareMessage();

                            if (newf != null) {
                                uploadProfileImage();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error in accessing SD card", Toast.LENGTH_LONG).show();
                            }


                        } else if (pos == 1) {
                            try {
                                Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sendIntent.setAction(Intent.ACTION_SEND);
                                String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), b, "ayubolife", null);
                                Uri bmpUri = Uri.parse(pathofBmp);
                                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share ayubo.life workout");
                                sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage);
                                sendIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                sendIntent.setType("image/png");
                                startActivity(Intent.createChooser(sendIntent, "Share ayubo.life"));
                            } catch (Exception e) {
                                System.out.println("Error " + e);
                            }
                        } else {
                            communityID = en.getId();
                            postType = "2";
                            message = Ram.getShareMessage();

                            if (newf != null) {
                                uploadProfileImage();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error in accessing SD card", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);

            }
        });


//        btn_back_btn=(TextView) findViewById(R.id.btn_back_btn);
//        btn_back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(WorkoutShareListActivity.this, StartWorkoutActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        btn_backImgBtn = (ImageButton) findViewById(R.id.btn_backImgBtn);
        btn_backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutShareListActivity.this, StartWorkoutActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WorkoutShareListActivity.this, StartWorkoutActivity.class);
        startActivity(intent);
        finish();
    }


    private class updateOnlineWorkoutDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            makePostRequest_updateOnlineWorkoutDetails();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (statusFromServiceAPI_db.equals("0")) {

                adapter = new CustomAdapter(data, WorkoutShareListActivity.this);
                list.setAdapter(adapter);

            } else if (statusFromServiceAPI_db.equals("999")) {

                Toast.makeText(WorkoutShareListActivity.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(WorkoutShareListActivity.this, "Internet connection error", Toast.LENGTH_SHORT).show();
            }
        }

        private void makePostRequest_updateOnlineWorkoutDetails() {
            //  prgDialog.show();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ApiClient.BASE_URL_live);
            httpPost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));


            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            String walk, run, jump, energy, cals, distance, dateOfBirth;

            pref = new PrefManager(WorkoutShareListActivity.this);
            userid_ExistingUser = pref.getLoginUser().get("uid");

            String jsonStr =
                    "{" +
                            "\"userid\": \"" + userid_ExistingUser + "\"" +
                            "}";

            nameValuePair.add(new BasicNameValuePair("method", "getCommunityListByUser"));
            nameValuePair.add(new BasicNameValuePair("input_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("response_type", "JSON"));
            nameValuePair.add(new BasicNameValuePair("rest_data", jsonStr));

            System.out.println("...........getCommunityListByUser............." + nameValuePair.toString());

            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int r = 0;

            String responseString = null;
            try {
                responseString = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                r = response.getStatusLine().getStatusCode();
                if (r == 200) {
                    try {

                        JSONObject jsonObj = new JSONObject(responseString);
                        statusFromServiceAPI_db = jsonObj.optString("result").toString();


                        if (statusFromServiceAPI_db.equals("0")) {
                            String dat = jsonObj.optString("data").toString();

                            JSONArray myListsAll = null;

                            try {
                                myListsAll = new JSONArray(dat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < myListsAll.length(); i++) {

                                JSONObject datajsonObj = null;
                                try {
                                    datajsonObj = (JSONObject) myListsAll.get(i);
                                    String id = datajsonObj.optString("id").toString();
                                    String name = datajsonObj.optString("name").toString();
                                    String community_type_c = datajsonObj.optString("community_type_c").toString();
                                    String company_logo_c = datajsonObj.optString("company_logo_c").toString();
                                    String company_enrolled_c = datajsonObj.optString("company_enrolled_c").toString();
                                    String no_members = datajsonObj.optString("no_members").toString();
                                    String logo_image = datajsonObj.optString("logo_image").toString();
                                    ShareEntity sen = new ShareEntity(id, name, community_type_c, company_logo_c, company_enrolled_c, no_members, logo_image);
                                    int s = data.size();
                                    data.add(sen);
                                    int ss = data.size();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        } else {
                            statusFromServiceAPI_db = "55";
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    statusFromServiceAPI_db = "999";
                }
            }

        }
    }


    public class CustomAdapter extends ArrayAdapter<ShareEntity> {

        private List<ShareEntity> dataSet;
        Context mContext;

        private class ViewHolder {
            TextView txtName, txt_details;
            ImageView btn_book;
            com.ayubo.life.ayubolife.utility.CircularNetworkImageView userImage;
        }

        public CustomAdapter(List<ShareEntity> moviesList, Context context) {
            super(context, R.layout.share_company_list_rawlayout, moviesList);
            this.dataSet = moviesList;
            this.mContext = context;

        }


        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            ShareEntity dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            CustomAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new CustomAdapter.ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.share_company_list_rawlayout, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.txt_details = (TextView) convertView.findViewById(R.id.details);
                viewHolder.btn_book = (ImageView) convertView.findViewById(R.id.btn_next);
                viewHolder.userImage = (com.ayubo.life.ayubolife.utility.CircularNetworkImageView) convertView.findViewById(R.id.share_com_list_image);
                //  com.ayubo.life.ayubolife.utility.CircularNetworkImageView imgProfile = (CircularNetworkImageView)vi.findViewById(R.id.user_pic);

                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (CustomAdapter.ViewHolder) convertView.getTag();
                result = convertView;
            }
            try {
                ShareEntity obj = dataSet.get(position);
                //  String imageURL="http://taprobanes.com/tickets/"+obj.getBanner();

                String imageURL = obj.getLogo_image();

                viewHolder.txtName.setText(obj.getName());


                if (imageLoader == null)
                    imageLoader = App.getInstance().getImageLoader();

                //    viewHolder.btn_book.setOnClickListener(this);


                if (obj.getName().equals("Public")) {
                    viewHolder.txt_details.setText(obj.getNo_members());
                    String uri = "@drawable/ayubo_wok";  // where myresource (without the extension) is the file
                    int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                    Drawable res = getResources().getDrawable(imageResource);
                    viewHolder.userImage.setBackground(res);
                } else if (obj.getName().equals("Share external")) {
                    try {
                        viewHolder.txt_details.setText(obj.getNo_members());
                        String uri = "@drawable/share_wok";  // where myresource (without the extension) is the file
                        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                        Drawable res = getResources().getDrawable(imageResource);
                        viewHolder.userImage.setBackground(res);
                    } catch (Exception e) {
                        System.out.println("Error " + e);
                    }
                } else {
                    String noofmem = obj.getNo_members() + " Members";
                    viewHolder.txt_details.setText(noofmem);
                    viewHolder.userImage.setImageUrl(imageURL, imageLoader);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }


    private void uploadProfileImage() {
        new UploadFileToServer().execute();
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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.setCancelable(false);
            prgDialog.setMessage("Sharing workout...");
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
            HttpPost httpost = new HttpPost(ApiClient.BASE_URL_entypoint + "submitTimelinePost");
            httpost.setHeader(new BasicHeader("app_id", AppConfig.APP_BRANDING_ID));

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                if (message == null) {
                    message = "";
                }
                if (postType == null) {
                    postType = "";
                }
                if (communityID == null) {
                    communityID = "";
                }
                // Adding file data to http body
                entity.addPart("pictureFile", new FileBody(newf));
                entity.addPart("message", new StringBody(message));
                entity.addPart("createdBy", new StringBody(userid_ExistingUser));
                entity.addPart("postType", new StringBody(postType));
                entity.addPart("communityID", new StringBody(communityID));
                entity.addPart("link", new StringBody(""));

                totalSize = entity.getContentLength();
                httpost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httpost);
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

                        Toast.makeText(WorkoutShareListActivity.this, "Workout shared on timeline", Toast.LENGTH_LONG).show();

//                        Intent intent = new Intent(WorkoutShareListActivity.this, NewHomeWithSideMenuActivity.class);
//                        Intent intent = new Intent(WorkoutShareListActivity.this, LifePlusProgramActivity.class);
//                        Intent intent = new Intent(WorkoutShareListActivity.this, NewDiscoverActivity.class);
                        Intent intent = new SetDiscoverPage().getDiscoverIntent(getBaseContext());
                        startActivity(intent);
                        finish();

                    } else {
                        System.out.println("Workout sharing error " + result);
                        Toast.makeText(WorkoutShareListActivity.this, "Workout sharing error", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(WorkoutShareListActivity.this, "Sharing error : " + result, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                Toast.makeText(WorkoutShareListActivity.this, "Sharing error : ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }

    }

    public static File resizeAndCompressImageBeforeSend(Context context, String filePath, String fileName) {
        final int MAX_IMAGE_SIZE = 200 * 200; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 100, 100);

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
        bmpPic.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
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


}
