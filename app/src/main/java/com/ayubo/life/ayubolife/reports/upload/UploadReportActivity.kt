package com.ayubo.life.ayubolife.reports.upload


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.payment.*
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.reports.activity.AddReportForReviewActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewVM
import com.ayubo.life.ayubolife.reports.getareview.ReviewData
import com.ayubo.life.ayubolife.reports.getareview.ReviewExperts
import com.ayubo.life.ayubolife.utility.Ram
import com.ayubo.life.ayubolife.webrtc.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flavors.changes.Constants
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ask.*
import kotlinx.android.synthetic.main.activity_book_video_call.*
import kotlinx.android.synthetic.main.activity_enter_dialog_pin_number.progressBar
import kotlinx.android.synthetic.main.activity_upload_report.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class UploadReportActivity : BaseActivity(), UploadReportAdapter.OnItemClickListener {

    @Inject
    lateinit var uploadReportActivityVM: UploadReportActivityVM


    override fun onRemoveReport(pos: Int, item: AddedReportsEntity) {
        removeReport(pos, item)
    }


    var requestCode: String = "no"
    private val MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111
    private val MYPERMISSIONS_REQUEST_CAMERA_EXTERNAL_STORAGE = 113
    lateinit var contact_id: String
    var appointment_source: String = ""
    val REQUEST_IMAGE_CAPTURE = 11
    lateinit var selectedReportIDList: ArrayList<String>

    val REQUEST_CAMERA = 14

    val REQUEST_ACTIVITY_FOR_REPORTS_SELECTED = 12

    private val CAMERA = 2
    private val PERMISSION_REQUEST_CODE: Int = 101
    private val GALLERY = 1

    // var selectedReportsStr:String?=null
    private val READ_EXTERNAL_STORAGE = 102
    private var mCurrentPhotoPath: String? = null;
    private var imagesList = ArrayList<AddedReportsEntity>()
    private var imagesFiles = ArrayList<File>()
    private var imagesListFromGallery = ArrayList<Bitmap>()
    private var imagesListFromReports = ArrayList<Bitmap>()
    var reportBitmap: Bitmap? = null
    lateinit var reportsList: String


    private var isReportReviewFree: Boolean = false;

    @Inject
    lateinit var getAReviewVM: GetAReviewVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_report)
        App.getInstance().appComponent.inject(this)
        isReportReviewFree = false;
        readExtras()

        initUI()

    }


    private fun initUI() {
        selectedReportIDList = ArrayList<String>()


        reportBitmap = BitmapFactory.decodeResource(resources, R.drawable.pdf_icon_for_reports)

        img_open_report.setOnClickListener {
            val intent = Intent(this, AddReportForReviewActivity::class.java)
            startActivityForResult(intent, REQUEST_ACTIVITY_FOR_REPORTS_SELECTED)
        }

        img_open_gallery.setOnClickListener {
            onUploadClick()
        }

        img_open_camera.setOnClickListener {
            checkFullPermission()
        }
        bnt_review.setOnClickListener {

            if (imagesListFromGallery.isEmpty() && selectedReportIDList.isEmpty()) {
                Toast.makeText(
                    this@UploadReportActivity,
                    "Please select a report",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                uploadReports()
            }

        }


        // BACK BUTTON EVENT START
        val viewbackbutton = findViewById<View>(R.id.lay_back_button)
        val backLayout = viewbackbutton.findViewById<LinearLayout>(R.id.btn_backImgBtn_layout)
        val backButton = viewbackbutton.findViewById<ImageButton>(R.id.btn_backImgBtn)
        backLayout.setOnClickListener { finish() }
        backButton.setOnClickListener { finish() }
        // BACK BUTTON EVENT END

    }


    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + "/ayuboreports"
        )
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {

            wallpaperDirectory.mkdirs()
        }

        try {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .timeInMillis).toString() + ".jpg")
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    imagesListFromGallery.add(bitmap)
                    addImageToUI()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@UploadReportActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        if (requestCode == CAMERA) {
            val bitmap: Bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath)
            imagesListFromGallery.add(bitmap)
            addImageToUI()
        }
        if (requestCode == REQUEST_ACTIVITY_FOR_REPORTS_SELECTED) {
            selectedReportIDList = ArrayList<String>()

            if (Ram.getSelectedReportIDList() != null) {
                selectedReportIDList = Ram.getSelectedReportIDList()

                addImageToUI()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                    // takePicture()

                }
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                choosePhotoFromGallary()
            }
            MYPERMISSIONS_REQUEST_CAMERA_EXTERNAL_STORAGE -> {
                openCamera()
            }

            else -> {

            }
        }
    }

    private fun openCamera() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File = createFile()
        var photoURI: Uri

        if (Constants.type == Constants.Type.AYUBO) {
            photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.ayubolife", photoFile)
        } else if (Constants.type == Constants.Type.SHESHELLS) {
            photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.sheshells", photoFile)
        } else if (Constants.type == Constants.Type.LIFEPLUS) {
            photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.lifeplus", photoFile)
        } else if (Constants.type == Constants.Type.HEMAS) {
            photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.hemas", photoFile)
        } else {
            photoURI = FileProvider.getUriForFile(this, "com.ayubo.life.ayubolife", photoFile)
        }


        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(intent, CAMERA)

    }

    @Throws(IOException::class)
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun onUploadClick() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    "Please Grant Permissions",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    "ENABLE"
                ) {
                    if (Build.VERSION.SDK_INT >= 23)
                        requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }.show()
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MYPERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            //  onUploadClickWithPermission()
            choosePhotoFromGallary()
        }

    }

    private fun checkFullPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) + ContextCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                Snackbar.make(
                    this.findViewById(android.R.id.content),
                    "Please Grant Permissions",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(
                    "ENABLE"
                ) {
                    if (Build.VERSION.SDK_INT >= 23)
                        requestPermissions(
                            arrayOf(
                                Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                            ),
                            MYPERMISSIONS_REQUEST_CAMERA_EXTERNAL_STORAGE
                        )
                }.show()
            } else {
                // No explanation needed, we can request the permission.
                if (Build.VERSION.SDK_INT >= 23)
                    requestPermissions(
                        arrayOf(
                            Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                        ),
                        MYPERMISSIONS_REQUEST_CAMERA_EXTERNAL_STORAGE
                    )
            }
        } else {
            // write your logic code if permission already granted
            openCamera()

        }
    }

    private fun choosePhotoFromGallary() {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun removeReport(position: Int, item: AddedReportsEntity) {

        if ((position + 1) > (imagesListFromGallery.size)) {
            selectedReportIDList.removeAt((position) - imagesListFromGallery.size)
            Ram.setSelectedReportIDList(selectedReportIDList)
        } else {
            imagesListFromGallery.removeAt(position)
        }

        // imagesList.removeAt(position)

        addImageToUI()

    }

    private fun addImageToUI() {

        imagesList.clear()

        for (i in 0 until imagesListFromGallery.size) {
            imagesList.add(AddedReportsEntity(i.toString(), imagesListFromGallery[i]))
        }

        for (i in 0 until selectedReportIDList.size) {
            val icon = BitmapFactory.decodeResource(resources, R.drawable.pdf_icon_for_reports)
            imagesList.add(AddedReportsEntity(selectedReportIDList[i], icon))
        }


        reports_recyceview.layoutManager = LinearLayoutManager(this)
        reports_recyceview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val timelineAdapter = UploadReportAdapter(this, imagesList)
        timelineAdapter.onitemClickListener = this@UploadReportActivity

        reports_recyceview.apply {
            if (adapter == null) {
                reports_recyceview.layoutManager = layoutManager
                adapter = timelineAdapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun readExtras() {
        lateinit var consultant_name: String
        lateinit var profile_picture: String
        lateinit var speciality: String
        lateinit var review_charge: String

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_CONSULTANT_NAME)) {
            val meta: String = bundle.getSerializable(EXTRA_PATIENT_REPORT_REVIEW_META) as String
            if (meta.equals("")) {
                appointment_source = "";
                isReportReviewFree = false;
                consultant_name = bundle.getSerializable(EXTRA_CONSULTANT_NAME) as String
                profile_picture = bundle.getSerializable(EXTRA_CONSULTANT_PICTURE) as String
                contact_id = bundle.getSerializable(EXTRA_CONSULTANT_ID) as String
                review_charge = bundle.getSerializable(EXTRA_CONSULTANT_CHARGE) as String
                speciality = bundle.getSerializable(EXTRA_CONSULTANT_SPECT) as String

                val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                Glide.with(this@UploadReportActivity).load(profile_picture).apply(requestOptions)
                    .into(img_expert_image)
                txt_doctor_name.text = consultant_name
                txt_doctor_specs.text = speciality
            } else {
                System.out.println(meta)


                val doctorDetail: String = meta;
                val doctorId: String = doctorDetail.split(":")[0];
                val reportReviewPaymentDetail: String = doctorDetail.split(":")[1];
                appointment_source = doctorDetail.split(":")[2];


                isReportReviewFree = reportReviewPaymentDetail == "free"


                subscription.add(getAReviewVM.getExpertsForReviewNew("", doctorId).subscribeOn(
                    Schedulers.io()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                    .doOnTerminate { progressBar.visibility = View.GONE }
                    .doOnError { progressBar.visibility = View.GONE }
                    .subscribe({
                        if (it.isSuccess) {

                            val dataList: ArrayList<ReviewData> = getAReviewVM.askDataList!!

                            val reviewData: ReviewData = dataList.get(0);

                            val reviewExperts: ReviewExperts = reviewData.experts.get(0);

                            consultant_name = reviewExperts.consultant_name
                            speciality = reviewExperts.speciality
                            contact_id = reviewExperts.contact_id
                            review_charge = reviewExperts.review_charge
                            profile_picture = reviewExperts.profile_picture


                            val requestOptions =
                                RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                            Glide.with(this@UploadReportActivity).load(profile_picture)
                                .apply(requestOptions).into(img_expert_image)
                            txt_doctor_name.text = consultant_name
                            txt_doctor_specs.text = speciality
                            progressBar.visibility = View.GONE

//                        }
                        } else {
                            showMessage("Failed loading data")
                        }
                    }, {
                        showMessage("Failed loading data")
                    })
                )


            }

        }


        //   txt_doctor_fee.text=review_charge

    }

    companion object {
        fun startActivity(
            context: Activity,
            consultant_nam: String,
            profile_picture: String,
            contact_id: String,
            review_charge: String,
            speciality: String,
            meta: String
        ) {
            val intent = Intent(context, UploadReportActivity::class.java)
            intent.putExtra(EXTRA_CONSULTANT_NAME, consultant_nam)
            intent.putExtra(EXTRA_CONSULTANT_PICTURE, profile_picture)
            intent.putExtra(EXTRA_CONSULTANT_ID, contact_id)
            intent.putExtra(EXTRA_CONSULTANT_CHARGE, review_charge)
            intent.putExtra(EXTRA_CONSULTANT_SPECT, speciality)
            intent.putExtra(EXTRA_PATIENT_REPORT_REVIEW_META, meta)
            context.startActivity(intent)
        }
    }

    private fun uploadReports() {

        for (i in 0 until imagesListFromGallery.size) {
            val imageFile = File(saveImage(imagesListFromGallery[i]))
            imagesFiles.add(imageFile)
        }


        subscription.add(
            uploadReportActivityVM.uploadUserReports(
                contact_id,
                txt_add_notes_content.text.toString(),
                isReportReviewFree,
                appointment_source,
                imagesFiles,
                selectedReportIDList
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnTerminate { progressBar.visibility = View.GONE }
                .doOnError { progressBar.visibility = View.GONE }
                .subscribe({
                    if (it.isSuccess) {
                        val action = uploadReportActivityVM.dataRes!!.data.action
                        val meta = uploadReportActivityVM.dataRes!!.data.meta

                        processAction(action, meta);

//                        PaymentActivity.startActivity(this@UploadReportActivity, meta)
                        clearView()
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


    private fun clearView() {

        imagesList.clear()
        selectedReportIDList.clear()
        imagesListFromGallery.clear()
        Ram.setSelectedReportIDList(selectedReportIDList)
        txt_add_notes_content.text.clear()


    }
}
