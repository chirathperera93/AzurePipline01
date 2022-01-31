package com.ayubo.life.ayubolife.reports.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.config.AppConfig;
import com.ayubo.life.ayubolife.insurances.Adapters.UploadDocumentAdapter;
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj;
import com.ayubo.life.ayubolife.insurances.Classes.MyTaskParams;
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage;
import com.ayubo.life.ayubolife.insurances.Classes.RequestRemoveFileClaimImage;
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem;
import com.ayubo.life.ayubolife.insurances.CommonApiInterface;
import com.ayubo.life.ayubolife.prochat.base.BaseActivity;
import com.ayubo.life.ayubolife.prochat.ui.ProgressAyubo;
import com.ayubo.life.ayubolife.reports.model.AllRecordsMainResponse;
import com.ayubo.life.ayubolife.rest.ApiClient;
import com.bumptech.glide.Glide;
import com.flavors.changes.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class UploadMediaReportActivity extends BaseActivity implements AutoCompleteRecordTypeAdapter.OnItemClickListener {

    List<AllRecordsMainResponse.AllRecordsMember> memberList;
    UploadMediaMemberAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView member_list_recycler_view;
    String selectedUuserId;
    PrefManager prefManager;
    ProgressAyubo uploadMediaRecordProgressBar;
    ArrayList<ReportTypeObject> reportTypeList = null;
    AlertDialog dialog;
    LinearLayout reportTypeDropDownListLayout;
    TextView reportTypeDropDownTextView;
    AutoCompleteTextView reportAutoCompleteTextView;
    LinearLayout upload_media_report_recycler_view_layout;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public ArrayList<RequestFileClaimImage> imageArr;
    public ArrayList<String> upLoadedImageList;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager mLayoutManager;
    private UploadDocumentAdapter mUploadDocumentAdapter;
    private ArrayList<UploadDocumentItem> mUploadDocumentList;
    private LinearLayout reportDetailTypes;
    private ArrayList<ReportTypeItem> reportTypeItemArray;
    private ArrayList<ReportTypeMain> reportTypeItemArrayMain;
    private Button upload_media_report_submit_btn;
    private ImageView media_record_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media_record);
        member_list_recycler_view = (RecyclerView) findViewById(R.id.member_list_recycler_view);
        uploadMediaRecordProgressBar = (ProgressAyubo) findViewById(R.id.uploadMediaRecordProgressBar);
        reportTypeDropDownListLayout = (LinearLayout) findViewById(R.id.reportTypeDropDownListLayout);
        reportTypeDropDownTextView = (TextView) findViewById(R.id.reportTypeDropDownTextView);
        reportAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.reportAutoCompleteTextView);
        reportDetailTypes = (LinearLayout) findViewById(R.id.reportDetailTypes);
        upload_media_report_submit_btn = (Button) findViewById(R.id.upload_media_report_submit_btn);
        media_record_back_btn = (ImageView) findViewById(R.id.media_record_back_btn);


        selectedUuserId = "all";
        prefManager = new PrefManager(this);


        Intent i = getIntent();

        loadMemberView(
                (List<AllRecordsMainResponse.AllRecordsMember>) i.getSerializableExtra("memberList"),
                (List<Object>) i.getSerializableExtra("dataList"),
                i.getStringExtra("firstReportID"),
                i.getStringExtra("selectedUuserId")


        );
        uploadMediaRecordProgressBar.setVisibility(View.VISIBLE);
        getAllReportType();

        reportTypeDropDownListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openReportTypePopUp(reportTypeList);
                reportTypeDropDownTextView.setVisibility(View.GONE);
                reportAutoCompleteTextView.setVisibility(View.VISIBLE);
                reportAutoCompleteTextView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(reportAutoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
                reportAutoCompleteTextView.showDropDown();
            }
        });

        media_record_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mUploadDocumentList = new ArrayList<>();
        buildRecyclerView();
        setButtons();

    }

    void loadMemberView(List<AllRecordsMainResponse.AllRecordsMember> allRecordsMemberList, List<Object> dataList, String firstReportID, String selecteUuserId) {
        if ((memberList != null) && (memberList.size() > 0)) {
            memberList.clear();
        }

        memberList = allRecordsMemberList;


        adapter = new UploadMediaMemberAdapter(this, memberList, selecteUuserId);
        adapter.setOnClickFamilyMemberListener(new UploadMediaMemberAdapter.OnClickONFamilyMemberIcon() {
            @Override
            public void onSelectFamilyMember(AllRecordsMainResponse.AllRecordsMember obj) {


//                if (obj.getName().equals("Add")) {
//                    Intent in = new Intent(UploadMediaReportActivity.this, SelectFamilyMemberActivity.class);
//                    startActivity(in);
//                } else {
                selectedUuserId = obj.getId();
                for (int childCount = member_list_recycler_view.getChildCount(), i = 0; i < childCount; ++i) {
                    final RecyclerView.ViewHolder holder = member_list_recycler_view.getChildViewHolder(member_list_recycler_view.getChildAt(i));

                    final TextView txt_goalname = (TextView) holder.itemView.findViewById(R.id.txt_name_doctor_row);
                    final CircleImageView img_family_member_image = holder.itemView.findViewById(R.id.img_profile_doctor_row);

                    img_family_member_image.setBackgroundResource(0);
                    txt_goalname.setTextColor(getResources().getColor(R.color.reports_normal_text));
//                    }
                }


            }
        });
        linearLayoutManager = new LinearLayoutManager(UploadMediaReportActivity.this, LinearLayoutManager.VERTICAL, false);

        member_list_recycler_view.setLayoutManager(linearLayoutManager);
        member_list_recycler_view.setItemAnimator(new DefaultItemAnimator());
        member_list_recycler_view.setAdapter(adapter);


    }

    void getAllReportType() {
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<ReportsResponse> getAllReportTypesRequestCall = commonApiInterface.getAllReportTypes(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID);


        if (Constants.type == Constants.Type.HEMAS) {
            getAllReportTypesRequestCall = commonApiInterface.getAllReportTypesForHemas(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID);
        }


        getAllReportTypesRequestCall.enqueue(new retrofit2.Callback<ReportsResponse>() {

            @Override
            public void onResponse(Call<ReportsResponse> call, Response<ReportsResponse> response) {
                if (response.isSuccessful()) {
                    uploadMediaRecordProgressBar.setVisibility(View.GONE);
                    System.out.println(response);

                    if (response.isSuccessful()) {
                        ReportsResponse mainResponse = response.body();
                        JsonArray allReportTypesArray = new Gson().toJsonTree(mainResponse.getData()).getAsJsonArray();
                        System.out.println(allReportTypesArray);
                        // Spinner Drop down elements
                        reportTypeList = new ArrayList<ReportTypeObject>();

                        for (int i = 0; i < allReportTypesArray.size(); i++) {
                            JsonElement jsonElement = allReportTypesArray.get(i);
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            ReportTypeObject reportTypeObject = new ReportTypeObject(jsonObject.get("id").getAsString(), jsonObject.get("name").getAsString());
                            reportTypeList.add(reportTypeObject);

                        }
//                        reportTypeDropDownTextView.setText(reportTypeList.get(0).name);
                        AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.reportAutoCompleteTextView);
                        AutoCompleteRecordTypeAdapter adapter = new AutoCompleteRecordTypeAdapter(UploadMediaReportActivity.this, reportTypeList);
                        editText.setAdapter(adapter);
                        adapter.setOnItemClickListener(UploadMediaReportActivity.this);
//                        getReportTypeData(reportTypeList.get(0));
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportsResponse> call, Throwable t) {
                uploadMediaRecordProgressBar.setVisibility(View.GONE);
            }
        });
    }


    void getReportTypeData(ReportTypeObject reportTypeObject) {
        uploadMediaRecordProgressBar.setVisibility(View.VISIBLE);
        reportTypeDropDownTextView.setText(reportTypeObject.name);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<ReportByIdResponse> getReportFieldsByIdRequestCall = commonApiInterface.getReportFieldsById(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID, reportTypeObject.id);


        if (Constants.type == Constants.Type.HEMAS) {
            getReportFieldsByIdRequestCall = commonApiInterface.getReportFieldsByIdForHemas(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID, reportTypeObject.id);
        }


        getReportFieldsByIdRequestCall.enqueue(new retrofit2.Callback<ReportByIdResponse>() {

            @Override
            public void onResponse(Call<ReportByIdResponse> call, Response<ReportByIdResponse> response) {
                uploadMediaRecordProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    ReportByIdResponse mainResponse = response.body();
                    JsonObject reportFieldsObject = new Gson().toJsonTree(mainResponse.getData()).getAsJsonObject();
                    JsonArray jsonArray = reportFieldsObject.get("parameter").getAsJsonArray();

                    reportTypeItemArray = new ArrayList<ReportTypeItem>();
                    reportTypeItemArrayMain = new ArrayList<ReportTypeMain>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonElement jsonElement = jsonArray.get(i);
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        System.out.println(jsonObject);


                        String PanicMax = "";
                        String PanicMin = "";
                        String UOM = "";
                        String HasUoM = "";
                        if (jsonObject.get("PanicMax") == null) {
                            PanicMax = "";
                        } else {
                            PanicMax = jsonObject.get("PanicMax").getAsString();
                        }

                        if (jsonObject.get("PanicMin") == null) {
                            PanicMin = "";
                        } else {
                            PanicMin = jsonObject.get("PanicMin").getAsString();
                        }

                        if (jsonObject.get("UOM") == null) {
                            UOM = "";
                        } else {
                            UOM = jsonObject.get("UOM").getAsString();
                        }

                        if (jsonObject.get("HasUoM") == null) {
                            HasUoM = "";
                        } else {
                            HasUoM = jsonObject.get("HasUoM").getAsString();
                        }


                        ReportTypeItem reportTypeItem = new ReportTypeItem(
                                jsonObject.get("name").getAsString(),
                                jsonObject.get("Seq").getAsInt(),
                                PanicMax,
                                PanicMin,
                                UOM,
                                HasUoM,
                                jsonObject.get("ParameterType").getAsInt(),
                                jsonObject.get("ResultType").getAsString(),
                                jsonObject.get("IsNumericType").getAsDouble(),
                                jsonObject.get("id").getAsString(),
                                jsonObject.get("value").getAsString()


                        );
                        reportTypeItemArray.add(reportTypeItem);

                    }


                    ArrayList<String> imageStringArrayList = new ArrayList<String>();
                    ReportTypeMain reportTypeMain = new ReportTypeMain(
                            reportFieldsObject.get("name").getAsString(),
                            reportTypeItemArray,
                            reportFieldsObject.get("id").getAsString(),
                            imageStringArrayList
                    );

                    reportTypeItemArrayMain.add(reportTypeMain);

                    LayoutInflater inflater = LayoutInflater.from(UploadMediaReportActivity.this);
                    reportDetailTypes.removeAllViews();
                    for (ReportTypeItem reportTypeItem : reportTypeItemArrayMain.get(0).parameter) {

                        if (reportTypeItem.ParameterType != 9) {
                            View custLayout = inflater.inflate(R.layout.custom_layout_for_report_type, null, false);
                            TextView label = (TextView) custLayout.findViewById(R.id.report_type_label);
                            TextView value = (TextView) custLayout.findViewById(R.id.report_type_value);
                            TextView measurement = (TextView) custLayout.findViewById(R.id.report_type_measurement);

                            label.setText(reportTypeItem.name);
                            value.setText(reportTypeItem.value);
                            value.setHint(reportTypeItem.name);
                            measurement.setText(reportTypeItem.UOM);
                            value.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {


                                    System.out.println(editable);
                                    System.out.println(editable.toString());

                                    reportTypeItem.value = editable.toString();

                                }
                            });
                            reportDetailTypes.addView(custLayout);
                        }

                    }


                }


            }

            @Override
            public void onFailure(Call<ReportByIdResponse> call, Throwable t) {
                uploadMediaRecordProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public void buildRecyclerView() {
        RecyclerView upload_media_report_recycler_view = findViewById(R.id.upload_media_report_recycler_view);
        upload_media_report_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mUploadDocumentAdapter = new UploadDocumentAdapter(mUploadDocumentList, getBaseContext());
        upload_media_report_recycler_view.setLayoutManager(mLayoutManager);
        upload_media_report_recycler_view.setAdapter(mUploadDocumentAdapter);
        mUploadDocumentAdapter.setOnItemClickListener(new UploadDocumentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                if (mUploadDocumentList.size() == 0) {
                    upload_media_report_recycler_view_layout.setVisibility(View.GONE);
                }
            }
        });


//


    }

    public void setButtons() {
        LinearLayout buttonGalleryImageInsert = findViewById(R.id.upload_media_report_get_from_gallery);
        LinearLayout buttonCameraImageInsert = findViewById(R.id.upload_media_report_take_a_snap);
        LinearLayout uploadMediaReportGetPdf = findViewById(R.id.upload_media_report_get_pdf);
        upload_media_report_recycler_view_layout = findViewById(R.id.upload_media_report_recycler_view_layout);

        buttonGalleryImageInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(UploadMediaReportActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadMediaReportActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        buttonCameraImageInsert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!checkPermissions(UploadMediaReportActivity.this)) {
                    ActivityCompat.requestPermissions(UploadMediaReportActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });


        uploadMediaReportGetPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(UploadMediaReportActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadMediaReportActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("application/pdf");
                startActivityForResult(intent, 101);
            }
        });

        upload_media_report_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reportTypeItemArrayMain.get(0).uploads = upLoadedImageList;
                System.out.println(reportTypeItemArrayMain);

                ReportTypeMain reportTypeMain = new ReportTypeMain(
                        reportTypeItemArrayMain.get(0).name,
                        reportTypeItemArrayMain.get(0).parameter,
                        reportTypeItemArrayMain.get(0).id,
                        reportTypeItemArrayMain.get(0).uploads);

                if (!selectedUuserId.equals("all") && !selectedUuserId.equals("") && selectedUuserId != null) {
                    saveUpdatedReportTypes(reportTypeMain);
                } else {
                    Toast.makeText(UploadMediaReportActivity.this, "Select at least one member", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    void saveUpdatedReportTypes(ReportTypeMain reportTypeItem) {
        uploadMediaRecordProgressBar.setVisibility(View.VISIBLE);
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<ReportByIdResponse> getAllReportTypesRequestCall = commonApiInterface.createRecord(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID, reportTypeItem);

        if (Constants.type == Constants.Type.HEMAS) {
            getAllReportTypesRequestCall = commonApiInterface.createRecordForHemas(prefManager.getUserToken(), AppConfig.APP_BRANDING_ID, reportTypeItem);
        }


        getAllReportTypesRequestCall.enqueue(new retrofit2.Callback<ReportByIdResponse>() {

            @Override
            public void onResponse(Call<ReportByIdResponse> call, Response<ReportByIdResponse> response) {
                uploadMediaRecordProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    System.out.println(response);
                    Toast.makeText(UploadMediaReportActivity.this, "Successfully uploaded your report", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ReportByIdResponse> call, Throwable t) {
                uploadMediaRecordProgressBar.setVisibility(View.GONE);
            }
        });
    }

    public static boolean checkPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final List<Bitmap> bitmapArrayList = new ArrayList<>();
        imageArr = new ArrayList<>();
        upLoadedImageList = new ArrayList<String>();

        if (data != null) {
            ClipData clipData = data.getClipData();
            if (requestCode == 1 && resultCode == RESULT_OK) {
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ContentResolver contentResolver = getContentResolver();
                            MimeTypeMap mime = MimeTypeMap.getSingleton();
                            String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                            String mImE = contentResolver.getType(imageUri);
                            String imageBase64String = getBaseString(bitmap);
                            imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                            bitmapArrayList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Uri imageUri = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ContentResolver contentResolver = getContentResolver();
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                        String mImE = contentResolver.getType(imageUri);
                        String imageBase64String = getBaseString(bitmap);
                        imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                        bitmapArrayList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = getImageUri(UploadMediaReportActivity.this, photo);
                ContentResolver contentResolver = getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                String mImE = contentResolver.getType(imageUri);
                String imageBase64String = getBaseString(photo);
                imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                bitmapArrayList.add(photo);
            }

            if (requestCode == 101 && resultCode == RESULT_OK) {
                if (clipData != null) {
                    System.out.println(clipData);
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri pdfUri = clipData.getItemAt(i).getUri();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(pdfUri);
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ContentResolver contentResolver = getContentResolver();
                            MimeTypeMap mime = MimeTypeMap.getSingleton();
                            String type = "." + mime.getExtensionFromMimeType(contentResolver.getType(pdfUri));
                            String mImE = contentResolver.getType(pdfUri);

                            String path = pdfUri.getPath();

                            System.out.println(path);

                            String imageBase64String = NewBase64(pdfUri.getPath());
                            imageArr.add(new RequestFileClaimImage("prescription", mImE, type, imageBase64String));
                            Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.ic_pdf);
                            bitmapArrayList.add(((BitmapDrawable) drawable).getBitmap());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            progressDialog = new ProgressDialog(UploadMediaReportActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading image...");
            progressDialog.show();
            new UploadMediaReportActivity.UploadImageToServer().execute(0);

            if (bitmapArrayList.size() > 0) {
                upload_media_report_recycler_view_layout.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (hasPermissionInManifest(UploadMediaReportActivity.this, android.provider.MediaStore.ACTION_IMAGE_CAPTURE) && requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UploadMediaReportActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null && declaredPermissions.length > 0) {
                for (String p : declaredPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    public String getBaseString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String NewBase64(String Path) {

        String encoded = "";
        try {
            File file = new File(Path);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(file);
            bos.close();
            oos.close();
            byte[] bytearray = bos.toByteArray();
            encoded = Base64.encodeToString(bytearray, Base64.DEFAULT);
        } catch (Exception ex) {

        }
        return encoded;
    }


    @Override
    public void onItemClick(ReportTypeObject reportTypeObject) {

        reportTypeDropDownTextView.setVisibility(View.VISIBLE);
        reportAutoCompleteTextView.setVisibility(View.GONE);
        reportTypeDropDownTextView.setText(reportTypeObject.name);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(reportAutoCompleteTextView.getWindowToken(), 0);


        getReportTypeData(reportTypeObject);

    }

    private class UploadImageToServer extends AsyncTask<Integer, String, Void> {
        int count;

        @Override
        protected Void doInBackground(Integer... params) {
            count = params[0];
            if (count < imageArr.size()) {
                publishProgress("GO");
                CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);


                Call<InsuranceResponseDataObj> uploadFileClaimImagesRequestCall = commonApiInterface.uploadFileClaimImages(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.getUserToken(),
                        imageArr.get(count)
                );

                if (Constants.type == Constants.Type.HEMAS) {
                    uploadFileClaimImagesRequestCall = commonApiInterface.uploadFileClaimImagesForHemas(
                            AppConfig.APP_BRANDING_ID,
                            prefManager.getUserToken(),
                            imageArr.get(count)
                    );
                }


                uploadFileClaimImagesRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {
                    @Override
                    public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                        if (response.isSuccessful()) {
                            InsuranceResponseDataObj insuranceResponseDataObj = response.body();
                            JsonObject insuranceResponseDataJsonObject = new Gson().toJsonTree(insuranceResponseDataObj.getData()).getAsJsonObject();
                            JsonObject insuranceResponseDataJsonObjectMedia = insuranceResponseDataJsonObject.get("media").getAsJsonObject();
                            upLoadedImageList.add(insuranceResponseDataJsonObjectMedia.get("URL").getAsString());
                            new UploadMediaReportActivity.GetImageFromServer().execute(new MyTaskParams(
                                            insuranceResponseDataJsonObjectMedia.get("URL").getAsString(),
                                            count,
                                            insuranceResponseDataJsonObjectMedia.get("MediaName").getAsString(),
                                            insuranceResponseDataJsonObjectMedia.get("MediaFolder").getAsString(),
                                            imageArr.get(count).getFile_contentType(),
                                            ""
                                    )
                            );
                            Toast toast = Toast.makeText(getApplicationContext(), "Uploaded image successfully", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                        publishProgress("STOP");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.servcer_not_available, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                publishProgress("STOP");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            if (text[0].equals("GO")) {
                progressDialog.show();
            } else {
                progressDialog.cancel();
            }
        }
    }

    public class GetImageFromServer extends AsyncTask<MyTaskParams, Void, Bitmap> {
        ProgressAyubo uploadMediaRecordProgressBar;
        private Bitmap image;
        private int count;
        private String imgStringURL;
        private String imgMediaName;
        private String imgMediaFolder;
        private String mediaType;

        protected void onPreExecute() {
            super.onPreExecute();
            uploadMediaRecordProgressBar = new ProgressAyubo(UploadMediaReportActivity.this);
            uploadMediaRecordProgressBar = findViewById(R.id.uploadMediaRecordProgressBar);
            uploadMediaRecordProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Bitmap doInBackground(MyTaskParams... params) {

            try {
                count = params[0].getCount();
                imgStringURL = params[0].getImageUrl();
                imgMediaName = params[0].getMediaName();
                imgMediaFolder = params[0].getMediaFolder();
                mediaType = params[0].getMediaType();
                URL url = new URL(params[0].getImageUrl().trim());
                URLConnection urlConnection = url.openConnection();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                image = BitmapFactory.decodeStream(urlConnection.getInputStream(), null, options);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            uploadMediaRecordProgressBar.setVisibility(View.GONE);
            if (result != null) {
                ImageView image = new ImageView(UploadMediaReportActivity.this);
                image.setImageBitmap(result);
                insertItem(0, result, imgStringURL, imgMediaName, imgMediaFolder, mediaType);
                new UploadMediaReportActivity.UploadImageToServer().execute(count + 1);
            } else {

                if (imgStringURL != null && imgMediaName != null && imgMediaFolder != null) {
                    ImageView image = new ImageView(UploadMediaReportActivity.this);
                    Glide.with(getBaseContext()).load(R.drawable.ic_pdf).into(image);
                    insertItem(0, result, imgStringURL, imgMediaName, imgMediaFolder, mediaType);
                    new UploadMediaReportActivity.UploadImageToServer().execute(count + 1);
                }

            }


        }
    }

    public void insertItem(int position, Bitmap imageView, String imgUrl, String mediaName, String mediaFolder, String mediaType) {
        mLayoutManager.scrollToPosition(0);
        mUploadDocumentList.add(position, new UploadDocumentItem(imgUrl, mediaName, mediaFolder, mediaType, ""));
        mUploadDocumentAdapter.notifyItemInserted(position);
    }

    public void changeItem(int position, String text) {
        mUploadDocumentAdapter.notifyItemChanged(position);
    }

    public void removeItem(int position) {
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Removing image...");
        progressDialog.show();
        CommonApiInterface commonApiInterface = ApiClient.getClient().create(CommonApiInterface.class);
        Call<InsuranceResponseDataObj> removeFileClaimImagesRequestCall = commonApiInterface.removeFileClaimImages(
                AppConfig.APP_BRANDING_ID,
                prefManager.getUserToken(),
                new RequestRemoveFileClaimImage(mUploadDocumentList.get(position).getImgUrl(), mUploadDocumentList.get(position).getMediaName(), mUploadDocumentList.get(position).getMediaFolder())
        );

        if (Constants.type == Constants.Type.HEMAS) {
            removeFileClaimImagesRequestCall = commonApiInterface.removeFileClaimImagesForHemas(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.getUserToken(),
                    new RequestRemoveFileClaimImage(mUploadDocumentList.get(position).getImgUrl(), mUploadDocumentList.get(position).getMediaName(), mUploadDocumentList.get(position).getMediaFolder())
            );
        }

        removeFileClaimImagesRequestCall.enqueue(new retrofit2.Callback<InsuranceResponseDataObj>() {

            @Override
            public void onResponse(Call<InsuranceResponseDataObj> call, Response<InsuranceResponseDataObj> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    for (Object str : upLoadedImageList) {
                        if (str.equals(mUploadDocumentList.get(position).getImgUrl())) {
                            upLoadedImageList.remove(upLoadedImageList.indexOf(str));
                            mUploadDocumentList.remove(position);
                            mUploadDocumentAdapter.notifyItemRemoved(position);
                            Toast toast = Toast.makeText(getApplicationContext(), "Image removed successfully", Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }

                }

                if (mUploadDocumentList.size() == 0) {
                    upload_media_report_recycler_view_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<InsuranceResponseDataObj> call, Throwable t) {
                progressDialog.cancel();
                Toast toast = Toast.makeText(getApplicationContext(), "Server error can't delete image", Toast.LENGTH_SHORT);
                toast.show();
                if (mUploadDocumentList.size() == 0) {
                    upload_media_report_recycler_view_layout.setVisibility(View.GONE);
                }
            }
        });


    }


}