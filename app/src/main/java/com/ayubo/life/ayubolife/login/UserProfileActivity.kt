package com.ayubo.life.ayubolife.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage
import com.ayubo.life.ayubolife.insurances.Classes.RequestRemoveFileClaimImage
import com.ayubo.life.ayubolife.insurances.CommonApiInterface
import com.ayubo.life.ayubolife.login.model.CityDataInfo
import com.ayubo.life.ayubolife.login.model.NewUserProfileData
import com.ayubo.life.ayubolife.login.vm.UserProfileVM
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.activity.AutoCompleteRecordTypeAdapter
import com.ayubo.life.ayubolife.reports.activity.ReportTypeObject
import com.ayubo.life.ayubolife.reports.activity.UploadMediaReportActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY
import com.ayubo.life.ayubolife.utility.Utility
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_user_profile.*
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class UserProfileActivity : BaseActivity(), AutoCompleteRecordTypeAdapter.OnItemClickListener,
    AutoCompleteCityAdapter.OnItemClickListener {

    var strGender: String? = null
    var selectedCityId: String? = ""
    var selectedCityName: String? = ""
    var profileImageURL: String? = null
    var yearSelected: Int = 0
    lateinit var pref: PrefManager
    var genderArrayList = ArrayList<ReportTypeObject>();

    var EXTERNAL_STORAGE_PERMISSION_CONSTANT: Int = 100;
    var REQUEST_PERMISSION_SETTING: Int = 101;
    var REQUEST_CAMERA: Int = 0;
    var SELECT_FILE: Int = 125;

//    var isChangedData: Boolean = false;


    lateinit var permissionStatus: SharedPreferences;

    @Inject
    lateinit var userProfileVM: UserProfileVM

    var requestRemoveFileClaimImage: RequestRemoveFileClaimImage =
        RequestRemoveFileClaimImage("", "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        App.getInstance().appComponent.inject(this)

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE)

//        isChangedData = false;
        profileImageURL = ""


        initView()

        getProfile()

        setDropDownData()
    }

    private fun initView() {
        strGender = "5"
        pref = PrefManager(this)

        genderArrayList = ArrayList<ReportTypeObject>();
        genderArrayList.add(ReportTypeObject("1", "Male"))
        genderArrayList.add(ReportTypeObject("2", "Female"))

        setUpdateButtonDisableUI()

        if (pref.loginUser["mobile"].toString() != "") {
            image_view_for_mobile_icon.visibility = View.VISIBLE
            layout_for_mobile_no.text =
                pref.loginUser["KEY_COUNTRY_CODE"].toString() + pref.loginUser["mobile"].toString()
        }

        if (Constants.type == Constants.Type.LIFEPLUS) {
            label_for_nic.text = "NID Number"
            edit_text_for_nic.hint = "Enter NID here"
            submit_profile_data_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)
        }
        if (Constants.type == Constants.Type.AYUBO) {
            label_for_nic.text = "NIC Number"
            edit_text_for_nic.hint = "Enter NIC here"
        }
        if (Constants.type == Constants.Type.HEMAS) {
            label_for_nic.text = "NIC Number"
            edit_text_for_nic.hint = "Enter NIC here"
        }
        if (Constants.type == Constants.Type.SHESHELLS) {
            label_for_nic.text = "NID Number"
            edit_text_for_nic.hint = "Enter NID here"
        }




        img_calender.setOnClickListener {
            showAlert_Add(this@UserProfileActivity)
        }

        image_btn_for_back.setOnClickListener { finish() }


        layout_for_change_image.setOnClickListener {
            changeUserImage();
        }


        // text fields watcher
        edit_text_for_first_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_text_for_first_name.hasFocus())
                    setUpdateButtonEnableUI()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        edit_text_for_last_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_text_for_last_name.hasFocus())
                    setUpdateButtonEnableUI()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        edit_text_for_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_text_for_email.hasFocus())
                    setUpdateButtonEnableUI()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        edit_text_for_nic.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edit_text_for_nic.hasFocus())
                    setUpdateButtonEnableUI()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


    }

    private fun setUpdateButtonEnableUI() {
        submit_profile_data_btn.isEnabled = true
        if (Constants.type == Constants.Type.LIFEPLUS) {
            submit_profile_data_btn.setBackgroundResource(R.drawable.life_plus_gradient_rounded_button)
        } else {
            submit_profile_data_btn.setBackgroundResource(R.drawable.ayubo_life_gradient_rounded_button)
        }

    }

    private fun setUpdateButtonDisableUI() {
        submit_profile_data_btn.isEnabled = false
        submit_profile_data_btn.setBackgroundResource(R.drawable.radius_background_disable)
    }

    private fun changeUserImage() {
        if (ActivityCompat.checkSelfPermission(
                this@UserProfileActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@UserProfileActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                //Show Information about why you need the permission
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(this@UserProfileActivity);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");



                builder.setPositiveButton(
                    "Grant"
                ) { dialog, which ->
                    dialog.cancel();
                    ActivityCompat.requestPermissions(
                        this@UserProfileActivity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        EXTERNAL_STORAGE_PERMISSION_CONSTANT
                    );
                }



                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel(); };
                builder.show();
            } else if (permissionStatus.getBoolean(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    false
                )
            ) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(
                        this@UserProfileActivity
                    );
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");


                builder.setPositiveButton("Grant") { dialog, which ->
                    dialog.cancel();
                    val intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri: Uri = Uri.fromParts("package", packageName, null);
                    intent.data = uri;
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(
                        baseContext,
                        "Go to Permissions to Grant Storage",
                        Toast.LENGTH_LONG
                    ).show();
                };
                builder.setNegativeButton("Cancel") { dialog, which ->

                    dialog.cancel();


                }



                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(
                    this@UserProfileActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    EXTERNAL_STORAGE_PERMISSION_CONSTANT
                );
            }

            val editor: SharedPreferences.Editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            selectImagePopup();
        }
    }

    private fun selectImagePopup() {
        val intent: Intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.type = "image/*";
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {


            if (requestCode == SELECT_FILE) {

                if (data != null) {
                    onSelectFromGalleryResult(data);
                }

            } else if (requestCode == REQUEST_CAMERA) {
                if (data != null) {
                    onCaptureImageResult(data);
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                selectImagePopup();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@UserProfileActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    //Show Information about why you need the permission
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(this@UserProfileActivity);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel();

                        ActivityCompat.requestPermissions(
                            this@UserProfileActivity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            EXTERNAL_STORAGE_PERMISSION_CONSTANT
                        );

                    }


                    builder.setNegativeButton("Cancel") { dialog, which ->

                        dialog.cancel();

                    }



                    builder.show();
                } else {
                    Toast.makeText(baseContext, "Unable to get Permission", Toast.LENGTH_LONG)
                        .show();
                }
            }
        }
    }

    private fun onCaptureImageResult(intentData: Intent) {
        val currentAPIVersion: Int = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(
                    this@UserProfileActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                val selectedImageUri: Uri = intentData.data!!;

                val thumbnail: Bitmap = intentData.extras!!.get("data") as (Bitmap);
                val bytes: ByteArrayOutputStream = ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                val destination: File = File(
                    Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis().toString() + ".jpg"
                );
                image_view_for_user.setImageBitmap(thumbnail);
                uploadProfileImageToServer(thumbnail, selectedImageUri);


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@UserProfileActivity,
                    Manifest.permission.CAMERA
                )
            ) {
                // Toast.makeText(getContext(), "Permission Issue 02..",Toast.LENGTH_SHORT).show();
                // We've been denied once before. Explain why we need the permission, then ask again.
                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);


            } else {
                //  Toast.makeText(getContext(), "Permission Issue 03..",Toast.LENGTH_SHORT).show();
                // We've never asked. Just do it.
                // requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            val selectedImageUri: Uri = intentData.data!!;

            val thumbnail: Bitmap = intentData.extras!!.get("data") as (Bitmap);
            val bytes: ByteArrayOutputStream = ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            val destination: File = File(
                Environment.getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg"
            );

            image_view_for_user.setImageBitmap(thumbnail);
            uploadProfileImageToServer(thumbnail, selectedImageUri);
        }
    }

    private fun onSelectFromGalleryResult(data: Intent) {
        val selectedImageUri: Uri = data.data!!;
        val cursor: Cursor;

        val projection = arrayOf(MediaStore.MediaColumns.DATA);
        var selectedImagePath: String? = null;

        cursor = this@UserProfileActivity.contentResolver
            .query(selectedImageUri, projection, null, null, null)!!;

        if (cursor != null && cursor.count > 0) {

            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();

            selectedImagePath = cursor.getString(column_index);

            val bm: Bitmap;
            val options: BitmapFactory.Options = BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            val REQUIRED_SIZE: Int = 50;
            var scale: Int = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE
            )
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            //bm = BitmapFactory.decodeFile(selectedImagePath, options);

            //=========================
            val photoW: Int = options.outWidth;
            val photoH: Int = options.outHeight;
            val targetW: Int = image_view_for_user.width;
            val targetH: Int = image_view_for_user.height;

            /* Figure out which way needs to be reduced less */
            var scaleFactor: Int = 1;
            if ((targetW > 0) || (targetH > 0)) {
                scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            }

            /* Set bitmap options to scale the image decode target */
            options.inJustDecodeBounds = false;
            options.inSampleSize = scaleFactor;
            options.inPurgeable = true;
            val imgFile: File = File(selectedImagePath);
            /* Decode the JPEG file into a Bitmap */
            bm = BitmapFactory.decodeFile(imgFile.absolutePath, options);

            image_view_for_user.setImageBitmap(bm);



            uploadProfileImageToServer(bm, selectedImageUri);


        }
    }

    private fun uploadProfileImageToServer(bitmap: Bitmap, uri: Uri) {
        val mime: MimeTypeMap = MimeTypeMap.getSingleton();
        val type: String =
            "." + mime.getExtensionFromMimeType(contentResolver.getType(uri));
        val mImE: String? = contentResolver.getType(uri);

        val imageBase64String: String =
            UploadMediaReportActivity().getBaseString(bitmap);

        val uploadImageObject =
            RequestFileClaimImage("profile", mImE, type, imageBase64String)
        progressBar.visibility = View.VISIBLE
        UploadImageToServer().execute(uploadImageObject)

    }

    internal inner class UploadImageToServer : AsyncTask<RequestFileClaimImage, Void, Void>() {

        var requestFileClaimImage: RequestFileClaimImage? = null;
        override fun doInBackground(vararg params: RequestFileClaimImage?): Void? {
            requestFileClaimImage = params[0];

            val commonApiInterface: CommonApiInterface =
                ApiClient.getClient().create(CommonApiInterface::class.java);


            var uploadImageRequestCall: Call<InsuranceResponseDataObj> =
                commonApiInterface.uploadFileClaimImages(
                    AppConfig.APP_BRANDING_ID,
                    pref.userToken,
                    requestFileClaimImage
                );

            if (Constants.type == Constants.Type.HEMAS) {
                uploadImageRequestCall =
                    commonApiInterface.uploadFileClaimImagesForHemas(
                        AppConfig.APP_BRANDING_ID,
                        pref.userToken,
                        requestFileClaimImage
                    );
            }

            uploadImageRequestCall.enqueue(object :
                retrofit2.Callback<InsuranceResponseDataObj> {
                override fun onResponse(
                    call: Call<InsuranceResponseDataObj>,
                    response: Response<InsuranceResponseDataObj>
                ) {
                    if (response.isSuccessful) {
                        val uploadImageResponseDataObj: InsuranceResponseDataObj =
                            response.body()!!;
                        val uploadImageResponseDataJsonObject: JsonObject =
                            Gson().toJsonTree(uploadImageResponseDataObj.data).asJsonObject;

                        profileImageURL =
                            uploadImageResponseDataJsonObject.get("media").asJsonObject.get("URL").asString


                        val newUserProfileData = NewUserProfileData(
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            0,
                            "",
                            "",
                            "",
                            profileImageURL
                        )



                        subscription.add(userProfileVM.updateNewUserProfileImage(newUserProfileData)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                            .doOnTerminate { progressBar.visibility = View.GONE }
                            .doOnError { progressBar.visibility = View.GONE }
                            .subscribe({
                                if (it.isSuccess) {
                                    val data = userProfileVM.newUserProfileData

                                    pref.createUserProfile(
                                        "${data.first_name} ${data.last_name}",
                                        data.email,
                                        data.date_of_birth,
                                        data.gender.toString(),
                                        data.nic,
                                        data.phone_mobile,
                                        data.countrycode_c,
                                        data.image_url,
                                        data.id,
                                        data.user_name
                                    )

                                    pref.createLoginUser(
                                        data.id,
                                        "${data.first_name} ${data.last_name}",
                                        data.email,
                                        data.phone_mobile,
                                        pref.loginUser["hashkey"].toString(),
                                        data.image_url,
                                        data.countrycode_c
                                    )

                                    Toast.makeText(
                                        baseContext,
                                        "Profile image updated successfully",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                } else {

                                    showMessage(R.string.service_loading_fail)
                                }
                            }, {
                                progressBar.visibility = View.GONE
                                showMessage(R.string.service_loading_fail)
                            })
                        )

                    } else {
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(p0: Call<InsuranceResponseDataObj>, p1: Throwable) {
                    p1.printStackTrace()
                    progressBar.visibility = View.GONE
                }
            })

            return null;


        }


    }

    private fun getProfile() {
        subscription.add(userProfileVM.getAllCities().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    subscription.add(userProfileVM.getNewProfile().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                        .doOnTerminate { progressBar.visibility = View.GONE }
                        .doOnError { progressBar.visibility = View.GONE }
                        .subscribe({
                            if (it.isSuccess) {
                                //  Toast.makeText(this, R.string.profile_update_success, Toast.LENGTH_LONG).show()
                                //  finish()
                                val data = userProfileVM.newUserProfileData
                                edit_text_for_first_name.setText(data.first_name)
                                edit_text_for_last_name.setText(data.last_name)
                                edit_text_for_dob.setText(data.date_of_birth)
                                edit_text_for_email.setText(data.email)
                                edit_text_for_nic.setText(data.nic)
                                val name = data.first_name + " " + data.last_name;
                                profileImageURL = data.image_url;

                                if (profileImageURL == "") {
                                    profileImageURL = pref.loginUser["image_path"].toString();
                                }

                                if (profileImageURL!!.contains(".jpg") || profileImageURL!!.contains(
                                        ".jpeg"
                                    ) || profileImageURL!!.contains(
                                        ".png"
                                    )
                                ) {
                                    Glide.with(baseContext).load(profileImageURL)
                                        .into(image_view_for_user)
                                } else {

                                    if (pref.loginUser["image_path"].toString() != "" && !pref.loginUser["image_path"].toString()
                                            .contains(MAIN_URL_LIVE_HAPPY)
                                    ) {
                                        profileImageURL =
                                            MAIN_URL_LIVE_HAPPY + pref.loginUser["image_path"].toString();
                                    }

                                    Glide.with(baseContext).load(profileImageURL)
                                        .into(image_view_for_user)

                                }
                                pref.createUserProfile(
                                    name,
                                    data.email,
                                    data.date_of_birth,
                                    data.gender.toString(),
                                    data.nic,
                                    data.phone_mobile,
                                    data.countrycode_c,
                                    profileImageURL,
                                    data.id,
                                    data.user_name
                                )

                                pref.createLoginUser(
                                    data.id,
                                    name,
                                    data.email,
                                    data.phone_mobile,
                                    pref.loginUser["hashkey"].toString(),
                                    profileImageURL,
                                    data.countrycode_c
                                )

                                val genderObj =
                                    genderArrayList.find { gen -> gen.id == data.gender.toString() }

                                if (genderObj != null) {
                                    text_view_for_gender.text = genderObj.name
                                }

                                if (data.city != null) {

                                    val filteredcity =
                                        userProfileVM.cityDataInfoArrayList.filter { x -> x.id == data.city }
                                            .single()

                                    text_view_for_city.text = filteredcity.city

                                    selectedCityId = filteredcity.id
                                    selectedCityName = filteredcity.city
                                }


                                if (data.gender == 1) {
                                    strGender = "1"
                                } else {
                                    strGender = "2"
                                }

                                if (data.corporate_email_verification == true) {
                                    verified_linear_layout.visibility = View.VISIBLE
                                    verified_false_linear_layout.visibility = View.GONE
                                } else {
                                    verified_false_linear_layout.visibility = View.VISIBLE
                                    verified_linear_layout.visibility = View.GONE
                                }


                                editTextForCorporateEmail.setText(data.corporate_email)

                                setCityDropDownData(userProfileVM.cityDataInfoArrayList)

                            } else {
                                showMessage(R.string.service_loading_fail)
                            }
                        }, {
                            progressBar.visibility = View.GONE
                            showMessage(R.string.service_loading_fail)
                        })
                    )
                } else {
                    showMessage(R.string.service_loading_fail)
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage(R.string.service_loading_fail)
            })
        )


    }

    private fun setCityDropDownData(cityDataInfoArrayList: ArrayList<CityDataInfo>) {
        val auto_complete_text_view_city: AutoCompleteTextView =
            findViewById(R.id.auto_complete_text_view_city)
        val autoCompleteCityAdapter =
            AutoCompleteCityAdapter(this@UserProfileActivity, cityDataInfoArrayList)
        auto_complete_text_view_city.setAdapter(autoCompleteCityAdapter)

        autoCompleteCityAdapter.setOnItemClickListener(this@UserProfileActivity)


        main_layout_for_city.setOnClickListener {
            text_view_for_city.visibility = View.GONE
            auto_complete_text_view_city.visibility = View.VISIBLE
            auto_complete_text_view_city.showDropDown()
        }
    }

    private fun setDropDownData() {
        val editText: AutoCompleteTextView =
            findViewById<AutoCompleteTextView>(R.id.auto_complete_text_view_gender)
        val adapter: AutoCompleteRecordTypeAdapter =
            AutoCompleteRecordTypeAdapter(this@UserProfileActivity, genderArrayList)
        editText.setAdapter(adapter)
        adapter.setOnItemClickListener(this@UserProfileActivity)
        main_layout_for_gender.setOnClickListener {
            text_view_for_gender.visibility = View.GONE
            auto_complete_text_view_gender.visibility = View.VISIBLE
            auto_complete_text_view_gender.showDropDown()
        }
    }

    fun showAlert_Add(c: Context) {

        val dialogView: android.app.AlertDialog

        val builder = android.app.AlertDialog.Builder(c)
        val inflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutView = inflater.inflate(R.layout.alert_date_picker, null, false)

        builder.setView(layoutView)
        // alert_ask_go_back_to_map
        dialogView = builder.create()
        dialogView.setCancelable(false)
        val picker = layoutView.findViewById<View>(R.id.datePicker1) as DatePicker


        val btn_no = layoutView.findViewById<View>(R.id.btn_no) as TextView
        btn_no.setOnClickListener { dialogView.cancel() }
        val btn_yes = layoutView.findViewById<View>(R.id.btn_yes) as TextView
        btn_yes.setOnClickListener {
            dialogView.cancel()
            println("Selected Date: " + picker.dayOfMonth + "/" + (picker.month + 1) + "/" + picker.year)

            var month = ""
            if ((picker.month + 1) > 9) {
                month = (picker.month + 1).toString()
            } else {
                month = "0" + (picker.month + 1).toString()
            }
            val DateStr =
                picker.year.toString() + "-" + month + "-" + picker.dayOfMonth
            yearSelected = picker.year
            var date1: Date? = null
            try {
                date1 = SimpleDateFormat("yyyy/MM/dd").parse(DateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            setUpdateButtonEnableUI()
            edit_text_for_dob.setText(DateStr)
        }
        dialogView.show()
    }

    fun updateUser(view: View) {


        var strFirstName = edit_text_for_first_name.text.toString()
        var strLastName = edit_text_for_last_name.text.toString()
        val strBirthday = edit_text_for_dob.text.toString()
        val strEmail = edit_text_for_email.text.toString()
        val strCorporateEmail = editTextForCorporateEmail.text.toString()
        val strNic = edit_text_for_nic.text.toString()



        if (strFirstName.isEmpty()) {
            Toast.makeText(this, R.string.toast_firstname_empty, Toast.LENGTH_LONG).show()
            return
        }
        strFirstName = strFirstName.trim()
        if (!Utility.isAlpha(strFirstName)) {
            Toast.makeText(this, R.string.toast_firstname_onlycharacters, Toast.LENGTH_LONG)
                .show()
            return
        }

        if (strLastName.isEmpty()) {
            Toast.makeText(this, R.string.toast_lastname_empty, Toast.LENGTH_LONG).show()
            return
        }
        strLastName = strLastName.trim()
        if (!Utility.isAlpha(strLastName)) {
            Toast.makeText(this, R.string.toast_firstname_onlycharacters, Toast.LENGTH_LONG)
                .show()
            return
        }
        if (strBirthday.isEmpty()) {
            Toast.makeText(this, R.string.toast_birthday_empty, Toast.LENGTH_LONG).show()
            return
        }

        val year = Calendar.getInstance().get(Calendar.YEAR)
        if ((year - yearSelected) < 13) {
            Toast.makeText(this, R.string.toast_birthday_invalid, Toast.LENGTH_LONG).show()
            return
        }

        if (strEmail.isEmpty()) {
            Toast.makeText(this, R.string.toast_email_empty, Toast.LENGTH_LONG).show()
            return
        }

        if (strGender == "5") {
            Toast.makeText(this, R.string.toast_gender_empty, Toast.LENGTH_LONG).show()
            return
        }


//        if (strNic.isEmpty()) {
//            Toast.makeText(this, R.string.enter_identification, Toast.LENGTH_LONG).show()
//            return
//        }
//        if (AppHandler.isNotValidateNIC(strNic)) {
//            Toast.makeText(this, R.string.invalid_identification, Toast.LENGTH_LONG).show()
//            return
//        }

        val newUserProfileData = NewUserProfileData(
            pref.userProfile["KEY_ID"]!!.toString(),
            pref.userProfile["KEY_USER_NAME"].toString(),
            strFirstName,
            strLastName,
            strBirthday,
            strEmail,
            strGender!!.toInt(),
            strNic,
            pref.userProfile["KEY_MOBILE_NUMBER"].toString(),
            pref.userProfile["KEY_COUNTRY_CODE"].toString(),
            profileImageURL,
            selectedCityId,
            selectedCityName,
            strCorporateEmail
        )

        updateProfileNew(newUserProfileData)


    }

    private fun updateProfileNew(
        newUserProfileData: NewUserProfileData
    ) {

        subscription.add(userProfileVM.updateNewUserProfile(newUserProfileData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { progressBar.visibility = View.VISIBLE }
            .doOnTerminate { progressBar.visibility = View.GONE }
            .doOnError { progressBar.visibility = View.GONE }
            .subscribe({
                if (it.isSuccess) {
                    setUpdateButtonDisableUI()
                    requestRemoveFileClaimImage = RequestRemoveFileClaimImage("", "", "")
                    pref.createUserProfile(
                        "${newUserProfileData.first_name} ${newUserProfileData.last_name}",
                        newUserProfileData.email,
                        newUserProfileData.date_of_birth,
                        newUserProfileData.gender.toString(),
                        newUserProfileData.nic,
                        newUserProfileData.phone_mobile,
                        newUserProfileData.countrycode_c,
                        newUserProfileData.image_url,
                        newUserProfileData.id,
                        newUserProfileData.user_name
                    )

                    pref.createLoginUser(
                        newUserProfileData.id,
                        "${newUserProfileData.first_name} ${newUserProfileData.last_name}",
                        newUserProfileData.email,
                        newUserProfileData.phone_mobile,
                        pref.loginUser["hashkey"].toString(),
                        newUserProfileData.image_url,
                        newUserProfileData.countrycode_c
                    )

                    Toast.makeText(this, R.string.profile_update_success, Toast.LENGTH_LONG)
                        .show()
                    finish()

                } else {
                    showMessage(R.string.service_loading_fail)
                }
            }, {
                progressBar.visibility = View.GONE
                showMessage(R.string.service_loading_fail)
            })
        )
    }

    override fun onItemClick(reportTypeObject: ReportTypeObject) {
        text_view_for_gender.visibility = View.VISIBLE;
        auto_complete_text_view_gender.visibility = View.GONE;
        text_view_for_gender.text = reportTypeObject.name;
        auto_complete_text_view_gender.dismissDropDown()


        val genderObj =
            genderArrayList.find { gen -> gen.name == reportTypeObject.name }

        strGender = genderObj!!.id;
        setUpdateButtonEnableUI()

//        val imm: InputMethodManager =
//            baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager);
//        imm.hideSoftInputFromWindow(auto_complete_text_view_gender.windowToken, 0);
    }

    override fun onDestroy() {
        super.onDestroy()

//        if (isChangedData && !requestRemoveFileClaimImage.url.equals("")) {
//            removeImageFromServer().execute()
//        }

    }

    override fun onCityItemClick(cityDataInfo: CityDataInfo?) {
        text_view_for_city.visibility = View.VISIBLE
        auto_complete_text_view_city.visibility = View.GONE
        text_view_for_city.text = cityDataInfo!!.city
        auto_complete_text_view_city.dismissDropDown()
        selectedCityId = cityDataInfo.id
        selectedCityName = cityDataInfo.city
        setUpdateButtonEnableUI()
    }

//    internal inner class removeImageFromServer : AsyncTask<Void, Void, Void>() {
//        override fun doInBackground(vararg p0: Void?): Void? {
//            val insuranceApiInterface: InsuranceApiInterface =
//                ApiClient.getClient().create(InsuranceApiInterface::class.java)
//            var removeFileClaimImagesRequestCall: Call<InsuranceResponseDataObj> =
//                insuranceApiInterface.removeFileClaimImages(
//                    AppConfig.APP_BRANDING_ID,
//                    pref.userToken,
//                    requestRemoveFileClaimImage
//                );
//
//            if (Constants.type == Constants.Type.HEMAS) {
//                removeFileClaimImagesRequestCall =
//                    insuranceApiInterface.removeFileClaimImagesForHemas(
//                        AppConfig.APP_BRANDING_ID,
//                        pref.userToken,
//                        requestRemoveFileClaimImage
//                    );
//            }
//
//            removeFileClaimImagesRequestCall.enqueue(object :
//                retrofit2.Callback<InsuranceResponseDataObj> {
//                override fun onResponse(
//                    p0: Call<InsuranceResponseDataObj>,
//                    response: Response<InsuranceResponseDataObj>
//                ) {
//                    if (response.isSuccessful) {
//                        print(response)
//                    }
//                }
//
//                override fun onFailure(p0: Call<InsuranceResponseDataObj>, p1: Throwable) {
//
//                }
//
//
//            });
//            return null;
//        }
//    }

}
