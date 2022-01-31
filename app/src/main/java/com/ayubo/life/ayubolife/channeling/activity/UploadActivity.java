package com.ayubo.life.ayubolife.channeling.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.book_videocall.BookVideoCallActivityVM;
import com.ayubo.life.ayubolife.book_videocall.model.BookVideoCallActivityMainResponseData;
import com.ayubo.life.ayubolife.channeling.adapter.ImageAdapter;
import com.ayubo.life.ayubolife.channeling.config.AppConfig;
import com.ayubo.life.ayubolife.channeling.model.Expert;
import com.ayubo.life.ayubolife.channeling.model.SoapBasicParams;
import com.ayubo.life.ayubolife.channeling.model.UploadDataBuilder;
import com.ayubo.life.ayubolife.channeling.model.VideoBooking;
import com.ayubo.life.ayubolife.channeling.util.FileUploadManager;
import com.ayubo.life.ayubolife.channeling.util.TimeFormatter;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.ayubo.life.ayubolife.rest.ApiInterface;
import com.ayubo.life.ayubolife.walk_and_win.WalkWinApiInterface;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends BaseActivity {

    //constants
    public static final String EXTRA_EXPERT_OBJECT = "expert_object";
    public static final String EXTRA_EXPERT_OBJECT_TIME = "expert_objectintime";
    public static final String EXTRA_DOCTOR_ID = "doctor_id";
    public static String EXTRA_SESSION_TIME = "session_time";
    public static String EXTRA_DOCTOR_PAYMENT = "doctor_payment";
    public static String EXTRA_APPOINTMENT_SOURCE = "appointment_source";
    private static final int MY_CAMERA_PERMISSION_CODE = 101;
    private static final int MY_READ_PERMISSION_CODE = 104;
    private static final int CAMERA_REQUEST = 102;
    private static final int BROWSE_REQUEST = 103;
    private static final String CHARSET_UTF8 = "UTF-8";
    private BookVideoCallActivityMainResponseData doctorObj;
    //instances
    private Expert expert;
    private ImageAdapter adapter;
    private VideoBooking booking;

    //primary data
    private List<Uri> images = new ArrayList<>();
    private String sessionTime, strINtime, userid, paymentAction, paymentMeta;
    private Date sessionTimeNew;
    //views
    private ProgressBar progressBar;
    private Button btnNext, txt_available_next_button;
    private EditText edtNote;
    // private CheckBox chkConsent;
    ImageButton img_backBtn;
    RelativeLayout back_layoutview;
    TextView txt_view_schedule, txt_view_profile;
    PrefManager pref;

    String isDoctorPaymentFree = "false";
    String appointment_source = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        isDoctorPaymentFree = "false";
        appointment_source = "";

//        Toolbar toolbar = findViewById(R.id.toolbar_upload);
//        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        progressBar = findViewById(R.id.prg_upload_progress);
        // btnNext = findViewById(R.id.txt_available_next_button);
        edtNote = findViewById(R.id.edt_upload_note);
        txt_view_profile = findViewById(R.id.txt_view_profile);

        img_backBtn = findViewById(R.id.img_backBtn);
        back_layoutview = findViewById(R.id.back_layoutview);
        txt_view_schedule = findViewById(R.id.txt_view_schedule);
        txt_available_next_button = findViewById(R.id.txt_available_next_button);
        pref = new PrefManager(this);
        userid = pref.getLoginUser().get("uid");

        setButtons();
        readExtra(getIntent());
        setRecyclerView();
        //   createAnAppointment();

//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            //    btnNext.setEnabled(false);
//                uploadImages();
//
//            }
//        });
    }


    private void setButtons() {
        txt_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAction(expert.getProfile().getAction(), expert.getProfile().getMeta());
            }
        });

        txt_available_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAnAppointment();
            }
        });

        txt_view_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, VideoSessionActivity.class);
                intent.putExtra(VideoSessionActivity.EXTRA_EXPERT_OBJECT, expert);
                intent.putExtra(VideoSessionActivity.EXTRA_DOCTOR_PAYMENT, isDoctorPaymentFree);
                intent.putExtra(VideoSessionActivity.EXTRA_APPOINTMENT_SOURCE, appointment_source);
                startActivity(intent);

            }
        });

        back_layoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View cameraButton = findViewById(R.id.img_upload_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
                    List<String> permissions = new ArrayList<>();
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        permissions.add(Manifest.permission.CAMERA);
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    requestPermissions(permissions.toArray(new String[permissions.size()]),
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    startCameraActivity();
                }
            }
        });

        View browseButton = findViewById(R.id.img_upload_browse);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_READ_PERMISSION_CODE);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, BROWSE_REQUEST);
                }
            }
        });
    }

    BookVideoCallActivityVM bookVideoCallActivityVM;


    private void readExtra(Intent intent) {
        if (intent.getExtras() != null && intent.getExtras().containsKey(EXTRA_EXPERT_OBJECT)) {
            expert = (Expert) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT);
            strINtime = (String) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT_TIME);
            isDoctorPaymentFree = "false";
            appointment_source = "";

            if (intent.getExtras().containsKey(EXTRA_DOCTOR_PAYMENT)) {
                isDoctorPaymentFree = (String) intent.getExtras().getSerializable(EXTRA_DOCTOR_PAYMENT);
            }

            if (intent.getExtras().containsKey(EXTRA_APPOINTMENT_SOURCE)) {
                appointment_source = (String) intent.getExtras().getSerializable(EXTRA_APPOINTMENT_SOURCE);
            }


            booking = new VideoBooking();
            booking.setExpert(expert);


            if (intent.getExtras().containsKey(EXTRA_SESSION_TIME)) {
                Bundle bundle = intent.getExtras();
                Object t1 = bundle.get(EXTRA_SESSION_TIME);
                System.out.println(t1);
                System.out.println(t1.toString());

//                sessionTime = intent.getExtras().getString(EXTRA_SESSION_TIME);
                sessionTime = t1.toString();
                sessionTimeNew = (Date) t1;


//                sessionTime = (String) intent.getExtras().getSerializable(EXTRA_SESSION_TIME);
            } else
                sessionTime = expert.getLocations().get(0).getNext_available();


            setExpertDetails();

            if (expert.getProfile().getAction() == null) {
                txt_view_profile.setVisibility(View.INVISIBLE);
            } else {
                txt_view_profile.setVisibility(View.VISIBLE);
            }

            if (expert.getProfile().getAction().isEmpty()) {
                txt_view_profile.setVisibility(View.INVISIBLE);
            } else {
                txt_view_profile.setVisibility(View.VISIBLE);
            }

//                sessionTime = (String) intent.getExtras().getSerializable(EXTRA_SESSION_TIME);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String doctorDetail = (String) intent.getExtras().getSerializable(EXTRA_DOCTOR_ID);
            String doctorId = doctorDetail.split(":")[0];
            String doctorPaymentDetail = doctorDetail.split(":")[1];
            appointment_source = doctorDetail.split(":")[2];


            if (doctorPaymentDetail.equals("free")) {
                isDoctorPaymentFree = "true";
            } else {
                isDoctorPaymentFree = "false";
            }
            PrefManager pref = new PrefManager(this);
            String userId = pref.getLoginUser().get("uid");

            String jsonStr = "{" +
                    "\"u\": \"" + userId + "\"," +
                    "\"d\": \"" + doctorId + "\"," +
                    "\"q\": \"" + "" + "\"" +
                    "}";


            WalkWinApiInterface getDoctorListData = ApiClient.getClient().create(WalkWinApiInterface.class);

            Call<DoctorsListResponse> doctorListDataRequestCall = getDoctorListData.getDoctorListsData(
                    com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID,
                    "videoCallSearch",
                    "JSON",
                    "JSON",
                    jsonStr
            );


            doctorListDataRequestCall.enqueue(new retrofit2.Callback<DoctorsListResponse>() {
                @Override
                public void onResponse(Call<DoctorsListResponse> call, Response<DoctorsListResponse> response) {
                    if (response.isSuccessful()) {
                        // successfully save

                        System.out.println(response);

                        ArrayList<Expert> expertArrayList = response.body().getData();
                        System.out.println(expertArrayList);

                        Object obj = expertArrayList.get(0);

                        JsonObject doctorObj = new Gson().toJsonTree(obj).getAsJsonObject();


                        List<Expert.Location> locationList = new ArrayList<>();


                        if (doctorObj.get("locations").getAsJsonArray() != null) {
                            for (int i = 0; i < doctorObj.get("locations").getAsJsonArray().size(); i++) {

                                JsonElement jsonElement = doctorObj.get("locations").getAsJsonArray().get(i);

                                Expert.Location location = new Expert.Location(
                                        jsonElement.getAsJsonObject().get("id").getAsString(),
                                        jsonElement.getAsJsonObject().get("name").getAsString(),
                                        jsonElement.getAsJsonObject().get("fee").getAsString(),
                                        jsonElement.getAsJsonObject().get("fee_value").getAsString(),
                                        jsonElement.getAsJsonObject().get("next_available").getAsString()
                                );

                                locationList.add(location);
                            }
                        }

                        Expert.Channel channel = new Expert.Channel(
                                doctorObj.get("channel").getAsJsonObject().get("enable").getAsInt(),
                                doctorObj.get("channel").getAsJsonObject().get("meta").getAsString());

                        Expert.Video video = new Expert.Video(
                                doctorObj.get("video").getAsJsonObject().get("enable").getAsInt(),
                                doctorObj.get("video").getAsJsonObject().get("meta").getAsString());

                        Expert.Review review = new Expert.Review(
                                doctorObj.get("review").getAsJsonObject().get("enable").getAsInt(),
                                doctorObj.get("review").getAsJsonObject().get("meta").getAsString());


                        Expert.Ask ask = new Expert.Ask(
                                doctorObj.get("ask").getAsJsonObject().get("enable").getAsInt(),
                                doctorObj.get("ask").getAsJsonObject().get("meta").getAsString());

                        Expert.Profile profile = new Expert.Profile(
                                doctorObj.get("profile").getAsJsonObject().get("action").getAsString(),
                                doctorObj.get("profile").getAsJsonObject().get("meta").getAsString());


                        Expert.Booking bookingObj = new Expert.Booking(
                                doctorObj.get("booking").getAsJsonObject().get("action").getAsString(),
                                doctorObj.get("booking").getAsJsonObject().get("meta").getAsString());

                        Expert expert1 = new Expert(
                                doctorObj.get("id").getAsString(),
                                doctorObj.get("title").getAsString(),
                                doctorObj.get("name").getAsString(),
                                doctorObj.get("speciality").getAsString(),
                                doctorObj.get("picture").getAsString(),
                                doctorObj.get("next").getAsString(),
                                locationList,
                                channel,
                                video,
                                review,
                                ask,
                                profile,
                                bookingObj

                        );

                        expert = expert1;
                        strINtime = (String) intent.getExtras().getSerializable(EXTRA_EXPERT_OBJECT_TIME);

                        booking = new VideoBooking();
                        booking.setExpert(expert);
                        setExpertDetails();

                        if (expert.getProfile().getAction() == null) {
                            txt_view_profile.setVisibility(View.INVISIBLE);
                        } else {
                            txt_view_profile.setVisibility(View.VISIBLE);
                        }

                        if (expert.getProfile().getAction().isEmpty()) {
                            txt_view_profile.setVisibility(View.INVISIBLE);
                        } else {
                            txt_view_profile.setVisibility(View.VISIBLE);
                        }
                        if (intent.getExtras().containsKey(EXTRA_SESSION_TIME))
                            sessionTime = intent.getExtras().getString(EXTRA_SESSION_TIME);
                        else
                            sessionTime = expert.getLocations().get(0).getNext_available();

                        progressBar.setVisibility(View.GONE);

                    } else {
                        // no result
                        System.out.println(response);

                        progressBar.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onFailure(Call<DoctorsListResponse> call, Throwable t) {
                    System.out.println(call);
                    progressBar.setVisibility(View.GONE);
                }
            });


        }
    }

    @SuppressLint("SimpleDateFormat")
    private void setExpertDetails() {
        ImageView imgExpert = findViewById(R.id.img_upload_doc);
        TextView txtExpertName = findViewById(R.id.txt_upload_name);
        TextView txtExpertSpeciality = findViewById(R.id.txt_upload_speciality);
        TextView txtTime = findViewById(R.id.txt_available_timeanddate);
        TextView txt_available_time = findViewById(R.id.txt_available_time);

        // TextView txtPrice = findViewById(R.id.txt_upload__price);

        System.out.println(sessionTime);

//        Date time = TimeFormatter.stringToDate(expert.getLocations().get(0).getNext_available(), "yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        if (sessionTime == null) {
            sessionTime = expert.getLocations().get(0).getNext_available();
            time = TimeFormatter.stringToDate(sessionTime, "yyyy-MM-dd HH:mm:ss");
        } else {

            try {

                if (sessionTimeNew != null)
                    time = sessionTimeNew;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sessionTimeNew = time;

        Glide.with(this).load(expert.getPicture()).into(imgExpert);
        txtExpertName.setText(expert.getName());
        txtExpertSpeciality.setText(expert.getSpeciality());
        txtTime.setText(TimeFormatter.millisecondsToString(time.getTime(), "EEEE, MMM dd, yyyy h:mm aa"));
        //  txtPrice.setText(expert.getLocations().get(0).getFee());
        txt_available_time.setText(strINtime);
        booking.setStartTime(time);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE || requestCode == MY_READ_PERMISSION_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                }
            }

            if (granted) {
                if (requestCode == MY_CAMERA_PERMISSION_CODE)
                    startCameraActivity();
                else if (requestCode == MY_READ_PERMISSION_CODE) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, BROWSE_REQUEST);
                }
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCameraActivity() {
        Intent cameraIntent = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Uri photoURI = null;
        // Continue only if the File was successfully created
        if (photoFile != null) {

            if (Constants.type == Constants.Type.AYUBO) {
                photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.ayubolife", photoFile);
            } else if (Constants.type == Constants.Type.SHESHELLS) {
                photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.sheshells", photoFile);
            } else if (Constants.type == Constants.Type.LIFEPLUS) {
                photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.lifeplus", photoFile);
            } else {
                photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.hemas", photoFile);
            }

            //  Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode != Activity.RESULT_OK)
                images.remove(images.size() - 1);
            adapter.notifyDataSetChanged();
        } else if (requestCode == BROWSE_REQUEST) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri imageUri = data.getData();
                    images.add(imageUri);
                    adapter.notifyDataSetChanged();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        else
//            return "";
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        else
//            return "";
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }

    public String getImagePathFromInputStreamUri(Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream);

                filePath = photoFile.getPath();

            } catch (FileNotFoundException e) {
                // log
            } catch (IOException e) {
                // log
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("======File Path=====", filePath.toString());
        return filePath;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private File createTemporalFileFrom(InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile();
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private File createTemporalFile() {
        String time = String.valueOf(System.currentTimeMillis());
        return new File(getExternalCacheDir(), time + ".jpg"); // context needed
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = TimeFormatter.millisecondsToString(Calendar.getInstance().getTime().getTime(), "yyyyMMdd_HHmmss");
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        images.add(Uri.fromFile(image));

        return image;
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_upload_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageAdapter(this, images);
        recyclerView.setAdapter(adapter);
    }

    private void createAnAppointment() {
        progressBar.setVisibility(View.VISIBLE);


        String url = ApiClient.BASE_URL + "custom/service/v7/rest.php";

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        String searchParams = new AppointmentParams().getSearchParams();

        Call<VideoSessionResponseData> call = apiService.getVideoCallSessions(
                url,
                com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID,
                "VideoCallTentativeBooking",
                "JSON",
                "JSON",
                searchParams
        );


        call.enqueue(new Callback<VideoSessionResponseData>() {

            @Override
            public void onResponse(Call<VideoSessionResponseData> call, Response<VideoSessionResponseData> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    JsonObject mainData =
                            new Gson().toJsonTree(response.body().getData()).getAsJsonObject();

                    booking.setPaymentLink(mainData.get("hnb_payment_link").getAsString());
                    booking.setAppointmentId(mainData.get("appointment_id").getAsString());
                    booking.setAmount(mainData.get("amount").getAsDouble());

                    paymentMeta = mainData.get("meta").getAsString();
                    paymentAction = "paynow";
//
//
                    if (mainData.has("action")) {
                        paymentAction = mainData.get("action").getAsString();
                    }


                    Integer c = adapter.getItemCount();

                    System.out.println(c);

                    if (c > 0) {
                        uploadImages();
                    }
                    processAction(paymentAction, paymentMeta);

                } else {
                    Toast.makeText(UploadActivity.this, "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VideoSessionResponseData> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UploadActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
                finish();
            }
        });


//        new DownloadData(this, new DownloadDataBuilder().init(AppConfig.URL_AYUBO_SOAP_REQUEST, 0, DownloadManager.POST_REQUEST).
//                setParams(AppHandler.getSoapRequestParams(AppConfig.METHOD_SOAP_VIDEO_APPIONTMENT, new AppointmentParams().getSearchParams())).
//                setType(AppConfig.SERVER_REQUEST_CONTENT_TYPE).setTimeout(AppConfig.SERVER_REQUEST_TIMEOUT)).
//                setOnDownloadListener(DashboardActivity.class.getSimpleName(), new DownloadData.DownloadListener() {
//                    @Override
//                    public void onDownloadSuccess(String response, int what, int code) {
//                        progressBar.setVisibility(View.GONE);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getInt("result") == 0) {
//                                if (booking != null) {
//                                    booking.setPaymentLink(jsonObject.getJSONObject("data").getString("hnb_payment_link"));
//                                    booking.setAppointmentId(jsonObject.getJSONObject("data").getString("appointment_id"));
//                                    booking.setAmount(jsonObject.getJSONObject("data").getDouble("amount"));
//
//                                    paymentMeta = jsonObject.getJSONObject("data").getString("meta");
//                                    paymentAction = "paynow";
//
//
//                                    if (jsonObject.getJSONObject("data").has("action")) {
//                                        paymentAction = jsonObject.getJSONObject("data").getString("action");
//                                    }
//
//
//                                    uploadImages();
//                                }
//                                return;
//                            } else {
//                                Toast.makeText(UploadActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
////                                finish();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Toast.makeText(UploadActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//
//                    @Override
//                    public void onDownloadFailed(String errorMessage, int what, int code) {
//                        Toast.makeText(UploadActivity.this, "Failed to create an appointment", Toast.LENGTH_LONG).show();
//                        finish();
//                    }
//                }).execute();
    }

    private void uploadImages() {
        progressBar.setVisibility(View.VISIBLE);

        new FileUploadManager(
                this,
                new UploadDataBuilder().init(AppConfig.URL_PHOTO_UPLOAD, 0)
                        .setMultipartEntityBuilder(
                                new UploadParams().getParams()
                        )
        ).setUploadListener(new FileUploadManager.UploadListener() {
            @Override
            public void onUploadSuccess(final String response, int what, int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (response.trim().toLowerCase().equals("done")) {

//                            processAction(paymentAction, paymentMeta);
//                            Intent intent;
//                            if (booking.getAmount() == 0)
//                                intent = new Intent(UploadActivity.this, ResultActivity.class);
//                            else
//                                intent = new Intent(UploadActivity.this, PayActivity.class);
//                            intent.putExtra(PayActivity.EXTRA_VIDEO_BOOKING_OBJECT, booking);
//                            startActivity(intent);
                        } else {

//                            processAction(paymentAction, paymentMeta);
                            //  Toast.makeText(UploadActivity.this, response, Toast.LENGTH_LONG).show();
                        }
                        //  btnNext.setEnabled(false);
                    }
                });
            }

            @Override
            public void onUploadFailed(String errorMessage, int what, int code) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        btnNext.setEnabled(true);
                        Toast.makeText(UploadActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).

                execute();


//        String url = AppConfig.URL_PHOTO_UPLOAD;
//
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//
//        Call<VideoSessionResponseData> call = apiService.videoCallFileUpload(
//                url,
//                com.ayubo.life.ayubolife.config.AppConfig.APP_BRANDING_ID,
//                "VideoCallTentativeBooking",
//                "JSON",
//                "JSON",
//                new UploadParams().getParams().toString()
//        );


    }

    class AppointmentParams extends SoapBasicParams {

        private String locationID;
        private String doctorID;
        private String start;


        AppointmentParams() {

            locationID = expert.getLocations().get(0).getId();
            doctorID = expert.getId();
            start = sessionTimeNew.toString();

        }


        public String getSearchParams() {
            JSONObject jsonObject = new JSONObject();
            PrefManager pref = new PrefManager(UploadActivity.this);
            user_id = pref.getLoginUser().get("uid");
            try {
                jsonObject.put("user_id", user_id);
                jsonObject.put("locationID", locationID);
                jsonObject.put("doctorID", doctorID);
                jsonObject.put("start", start);
                jsonObject.put("patientID", user_id);
                jsonObject.put("free_appointment", isDoctorPaymentFree);
                jsonObject.put("appointment_source", appointment_source);
//                jsonObject.put("token_key", token_key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
    }

    class UploadParams extends SoapBasicParams {
        private String note;
        private List<File> files;

        UploadParams() {

            note = edtNote.getText().toString();

            files = new ArrayList<>();
            for (Uri uri : images) {
                files.add(new File(getImagePathFromInputStreamUri(uri)));
            }
        }

        public MultipartEntityBuilder getParams() {
            MultipartEntityBuilder entityBuilder = null;
            try {
                entityBuilder = MultipartEntityBuilder.create();
                PrefManager pref = new PrefManager(UploadActivity.this);
                user_id = pref.getLoginUser().get("uid");

                if (booking != null)
                    entityBuilder.addPart("appointment_id", new StringBody(booking.getAppointmentId(), ContentType.TEXT_PLAIN));
                entityBuilder.addPart("user_id", new StringBody(user_id, ContentType.TEXT_PLAIN));


                int i = 0;

                MultipartBody.Part multipartBody = null;

                for (File file : files) {
//                    entityBuilder.addPart("picture[" + String.valueOf(i++) + "]", new FileBody(file));
                     entityBuilder.addPart("picture[" + String.valueOf(i++) + "]", new FileBody(file, ContentType.create("multipart/form-data")));

                    //    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    //   multipartBody = MultipartBody.Part.createFormData("file_input", "picture[" + String.valueOf(i++) + "]", requestFile);

                }
                entityBuilder.addPart("note", new StringBody(note, ContentType.TEXT_PLAIN));
                //   entityBuilder.addPart("file_input",multipartBody);


                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.setCharset(Charset.forName(CHARSET_UTF8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return entityBuilder;
        }
    }
}
