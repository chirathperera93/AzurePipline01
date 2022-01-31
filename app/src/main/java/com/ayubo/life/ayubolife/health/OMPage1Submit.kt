package com.ayubo.life.ayubolife.health

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.Adapters.UploadDocumentAdapter
import com.ayubo.life.ayubolife.insurances.Classes.InsuranceResponseDataObj
import com.ayubo.life.ayubolife.insurances.Classes.MyTaskParams
import com.ayubo.life.ayubolife.insurances.Classes.RequestFileClaimImage
import com.ayubo.life.ayubolife.insurances.Classes.RequestRemoveFileClaimImage
import com.ayubo.life.ayubolife.insurances.GenericItems.UploadDocumentItem
import com.ayubo.life.ayubolife.insurances.CommonApiInterface
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.reports.activity.UploadMediaReportActivity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_om_page1_submit.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class OMPage1Submit(val isOpenPopUp: Boolean = false) : Fragment(),
    OrderMedicineHistoryAdapter.OrderMedicineHistoryItemClickListener {

    private var orderMedicineHistoryAdapter: OrderMedicineHistoryAdapter? = null
    var imageArr: ArrayList<RequestFileClaimImage> = ArrayList<RequestFileClaimImage>();
    var upLoadedImageList: ArrayList<String> = ArrayList<String>();
    var mLayoutManager: RecyclerView.LayoutManager? = null;
    var mUploadDocumentAdapter: UploadDocumentAdapter? = null;
    var mUploadDocumentList: ArrayList<UploadDocumentItem> =
        ArrayList<UploadDocumentItem>();
    lateinit var prefManager: PrefManager;

    lateinit var authorization: String;

    private var CAMERA_REQUEST: Int = 1888;
    private var MY_CAMERA_PERMISSION_CODE: Int = 100;


    lateinit var mainView: View;

    lateinit var changeFragmentInterface: ChangeFragmentInterface;
    lateinit var goToHistoryInterface: GoToHistoryInterface;
    lateinit var submitButtonInterface: SubmitButtonInterface;

    lateinit var oMCreatedOrderObj: OMCreatedOrderObj


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        super.onCreate(savedInstanceState)
        mainView = inflater.inflate(R.layout.activity_om_page1_submit, container, false);

        oMCreatedOrderObj = OMCommon(requireContext()).retrieveFromCommonSingleton()

        buildMediaRecyclerView();

        if (oMCreatedOrderObj.status == null) {
            if (oMCreatedOrderObj.files != null && oMCreatedOrderObj.files!!.size > 0) {

                print(isOpenPopUp)
                if (isOpenPopUp) {
                    openConfirmationDialog(requireContext());
                } else {
                    loadMediaFiles()
                }

            }

        } else {
            if (oMCreatedOrderObj.status.equals("Delivered") || oMCreatedOrderObj.status.equals("Cancelled")) {
//                oMCreatedOrderObj.status = null
//                val changedJsonObject: JsonObject =
//                    Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;
//                oMCreatedOrderObj =
//                    OMCommon(context!!).saveToCommonSingletonAndRetrieve(changedJsonObject)

                if (oMCreatedOrderObj.files != null) {
                    for (i in 0 until oMCreatedOrderObj.files!!.size) {
                        val mf: OMMediaFiles = oMCreatedOrderObj.files!![i]

                        upLoadedImageList.add(
                            mf.URL!!
                        );

                        GetImageFromServer().execute(
                            MyTaskParams(
                                mf.URL,
                                i,
                                mf.MediaName,
                                mf.MediaFolder,
                                mf.MediaType,
                                mf.note
                            )
                        );


                    }
                }
            }
        }

        initializeUI();

        getOrderMedicineData()



        return mainView;
    }

    private fun getOrderMedicineData() {
        mainView.submitMedicineProgressBar.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);


        val orderTypes: String = "All"
//        val orderTypes: String = "Processing,Delivered"

        val call: Call<ProfileDashboardResponseData> = apiService.getMyOrders(
            AppConfig.APP_BRANDING_ID,
            authorization,
            orderTypes,
            "2"
        );

        call.enqueue(object : Callback<ProfileDashboardResponseData> {

            override fun onResponse(
                call: Call<ProfileDashboardResponseData>,
                response: Response<ProfileDashboardResponseData>
            ) {

                if (response.isSuccessful) {
                    println(response)

                    val oMCreatedOrderObjList: ArrayList<OMCreatedOrderObj> =
                        ArrayList<OMCreatedOrderObj>()

                    val getOrdersMainData: JsonArray =
                        Gson().toJsonTree(response.body()!!.data).asJsonArray;

                    mainView.previous_orders_main.visibility = View.VISIBLE
                    try {

                        if (getOrdersMainData.size() == 0) {
                            mainView.main_empty_image.visibility = View.VISIBLE
                        } else {
                            mainView.main_empty_image.visibility = View.GONE
                            for (i in 0 until getOrdersMainData.size()) {
                                val order: JsonElement = getOrdersMainData.get(i)

                                val h =
                                    order.asJsonObject.get("partner").asJsonObject.get("payment_methods").asJsonArray[0].isJsonObject

                                val g =
                                    order.asJsonObject.get("payment").asJsonObject.get("selected_payment_type").isJsonObject
                                print(h)

                                if (h && !g) {
                                    val orderElement =
                                        Gson().fromJson(order, OMCreatedOrderObj::class.java);
                                    oMCreatedOrderObjList.add(orderElement)
                                }


                            }

                        }

                        setPreviousMedicines(oMCreatedOrderObjList)
                        mainView.submitMedicineProgressBar.visibility = View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    Toast.makeText(
                        context,
                        "There is an issue when loading data, Please contact admin.",
                        Toast.LENGTH_SHORT
                    ).show()
                    mainView.submitMedicineProgressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ProfileDashboardResponseData>, throwable: Throwable) {
                mainView.submitMedicineProgressBar.visibility = View.GONE
                throwable.printStackTrace()
            }

        })

    }

    private fun createNewOrder() {
//        mainView.submitMedicineProgressBar.visibility = View.VISIBLE


        print(oMCreatedOrderObj)

        val oMMediaFilesArrayList: ArrayList<OMMediaFiles> = ArrayList<OMMediaFiles>()

        for (mUploadDocument in mUploadDocumentList) {
            val oMMediaFiles: OMMediaFiles = OMMediaFiles(
                mUploadDocument.imgUrl,
                mUploadDocument.mediaName,
                mUploadDocument.mediaFolder,
                mUploadDocument.mediaType,
                mUploadDocument.mediaNote,
                false
            )

            oMMediaFilesArrayList.add(oMMediaFiles)
        }


        val oMCreatedOrder: OMCreatedOrderObj = OMCreatedOrderObj(
            null,
            AppConfig.APP_BRANDING_ID,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            oMMediaFilesArrayList,
            null,
            null,
            null
        )

        val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrder).asJsonObject;


        if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject)
        }

        OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject)


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun initializeUI() {
        prefManager = PrefManager(context);
        authorization = prefManager.userToken;

        mainView.take_a_snap.setOnClickListener {
            if (!checkPermissions(requireActivity())) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    100
                );
            } else {
                val cameraIntent: Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }

        mainView.upload_media_from_gallery.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 100
                );
                return@setOnClickListener;
            }

            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.type = "image/*";
            startActivityForResult(intent, 1);
        }

        mainView.upload_media_pdf.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 100
                );
                return@setOnClickListener;
            }

            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.type = "application/pdf";
            startActivityForResult(intent, 101);
        }

        mainView.type_your_order_main.setOnClickListener {
            openAddPrescriptionBottomSlider()
        }

//        mainView.submit_proceed_btn.setOnClickListener {
////            createNewOrder()
//        }

        mainView.view_history.setOnClickListener {
            goToHistoryInterface = context as (GoToHistoryInterface);
            goToHistoryInterface.onGoToHistoryInterfaceClick()
        }




        mainView.camera.setImageDrawable(tintImage(R.drawable.camera_action));
        mainView.file.setImageDrawable(tintImage(R.drawable.file_action));
        mainView.gallery.setImageDrawable(tintImage(R.drawable.image_action));

    }

    private fun tintImage(iconInt: Int): Drawable {
        val unwrappedDrawable: Drawable? =
            AppCompatResources.getDrawable(requireContext(), iconInt);
        val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable!!);
        DrawableCompat.setTint(wrappedDrawable, resources.getColor(R.color.theme_color));

        return wrappedDrawable
    }

    interface SubmitButtonInterface {
        fun onSubmitButtonClick(isButtonEnabled: Boolean);
    }


    interface ChangeFragmentInterface {
        fun onOMPage1SubmitNextButtonClick(position: Int);
        fun onMedicineHistoryOrderButtonClick(oMCreatedOrderObj: OMCreatedOrderObj);
    }


    interface GoToHistoryInterface {
        fun onGoToHistoryInterfaceClick();
    }


    private fun buildMediaRecyclerView() {

        upLoadedImageList = ArrayList<String>();
        mainView.uploaded_media_recycler_view.setHasFixedSize(true);
        mLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        mUploadDocumentAdapter = UploadDocumentAdapter(mUploadDocumentList, requireContext());
        mainView.uploaded_media_recycler_view.layoutManager = mLayoutManager;
        mainView.uploaded_media_recycler_view.adapter = mUploadDocumentAdapter;
        mUploadDocumentAdapter!!.setOnItemClickListener(object :
            UploadDocumentAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                changeItem(position, "Clicked");
            }

            override fun onDeleteClick(position: Int) {
                removeItem(position);
//                if (mUploadDocumentList.size == 0) {
//                    mainView.uploaded_media_recycler_view.visibility = View.GONE;
//                }
            }

        })
    }

    private fun setPreviousMedicines(oMCreatedOrderObjArrayList: ArrayList<OMCreatedOrderObj>) {
        mainView.previous_medicines_recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)



        orderMedicineHistoryAdapter =
            OrderMedicineHistoryAdapter(requireContext(), oMCreatedOrderObjArrayList, false, true)

        try {
            orderMedicineHistoryAdapter!!.onOrderMedicineHistoryItemClickListener =
                this@OMPage1Submit
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mainView.previous_medicines_recycler_view.visibility = View.VISIBLE
        mainView.previous_medicines_recycler_view.adapter = orderMedicineHistoryAdapter

    }

    fun changeItem(position: Int, text: String) {
        mUploadDocumentAdapter!!.notifyItemChanged(position);
    }

    fun removeItem(position: Int) {


        val cachedFile = mUploadDocumentList[position];

        print(cachedFile)


        if (cachedFile.mediaType != "text/plain") {
            mainView.submitMedicineProgressBar.visibility = View.VISIBLE
            val commonApiInterface: CommonApiInterface =
                ApiClient.getClient().create(CommonApiInterface::class.java);
            var removeFileClaimImagesRequestCall: Call<InsuranceResponseDataObj> =
                commonApiInterface.removeFileClaimImages(
                    AppConfig.APP_BRANDING_ID,
                    prefManager.userToken,
                    RequestRemoveFileClaimImage(
                        mUploadDocumentList[position]
                            .imgUrl,
                        mUploadDocumentList[position].mediaName,
                        mUploadDocumentList[position].mediaFolder
                    )
                );

            if (Constants.type == Constants.Type.HEMAS) {
                removeFileClaimImagesRequestCall =
                    commonApiInterface.removeFileClaimImagesForHemas(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.userToken,
                        RequestRemoveFileClaimImage(
                            mUploadDocumentList[position].imgUrl,
                            mUploadDocumentList[position].mediaName,
                            mUploadDocumentList[position].mediaFolder
                        )
                    );
            }

            removeFileClaimImagesRequestCall.enqueue(object :
                retrofit2.Callback<InsuranceResponseDataObj> {
                override fun onResponse(
                    call: Call<InsuranceResponseDataObj>,
                    response: Response<InsuranceResponseDataObj>
                ) {
                    mainView.submitMedicineProgressBar.visibility = View.GONE;
                    if (response.isSuccessful) {

                        for (str in upLoadedImageList) {
                            if (str == mUploadDocumentList[position].imgUrl) {

                                upLoadedImageList.removeAt(upLoadedImageList.indexOf(str));
                                mUploadDocumentList.removeAt(position);
                                mUploadDocumentAdapter!!.notifyItemRemoved(position);
                                val toast: Toast = Toast.makeText(
                                    context!!,
                                    "Image removed successfully",
                                    Toast.LENGTH_SHORT
                                );
                                toast.show();
                                if (mUploadDocumentList.size == 0) {

                                    createNewOrder();

//                                mainView.submit_proceed_btn.isEnabled = false;
//
//                                mainView.submit_proceed_btn.setBackgroundResource(R.drawable.radius_background_disable)

//                                mainView.uploaded_media_recycler_view.visibility = View.GONE;

                                    submitButtonInterface = context as (SubmitButtonInterface);
                                    submitButtonInterface.onSubmitButtonClick(false)

                                }
                                return;
                            }
                        }

                        if (mUploadDocumentList.size == 0) {
                            mainView.submitMedicineProgressBar.visibility = View.GONE;
                            mainView.uploaded_media_recycler_view.visibility = View.GONE;
                        }

                    }

                }

                override fun onFailure(call: Call<InsuranceResponseDataObj>, t: Throwable) {
                    mainView.submitMedicineProgressBar.visibility = View.GONE;
                    val toast: Toast = Toast.makeText(
                        context!!,
                        "Server error can't delete image",
                        Toast.LENGTH_SHORT
                    );
                    toast.show();
                    if (mUploadDocumentList.size == 0) {
                        mainView.uploaded_media_recycler_view.visibility = View.GONE;
                    }
                }


            });
        } else {
            mUploadDocumentList.removeAt(position);
            mUploadDocumentAdapter!!.notifyItemRemoved(position);
            val toast: Toast = Toast.makeText(
                requireContext(),
                "Prescription text removed successfully",
                Toast.LENGTH_SHORT
            );
            toast.show();
            if (mUploadDocumentList.size == 0) {

                createNewOrder();

                submitButtonInterface = context as (SubmitButtonInterface);
                submitButtonInterface.onSubmitButtonClick(false)

            }
            return;
        }


    }


    private fun openAddPrescriptionBottomSlider() {

        val bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.submit_medicine_bottom_sheet);
        bottomSheetDialog.show();
        val addButton = bottomSheetDialog.findViewById<Button>(R.id.add_btn)
        val medInfo = bottomSheetDialog.findViewById<EditText>(R.id.text_area_med_information)


        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager);
        imm.showSoftInput(medInfo, InputMethodManager.SHOW_IMPLICIT);

        addButton?.setOnClickListener {
            val info = medInfo?.text;
            val str = info.toString().replace(" ", "")
            if (str != "") {
                insertItem(0, "", "", "", "text/plain", info.toString())
                bottomSheetDialog.dismiss();
            }


        }

//        val builder: BottomSheet.Builder =
//            BottomSheet.Builder(context!!, true);
//        builder
//            .setTitle("Type your order")
//            .setView(R.layout.submit_medicine_bottom_sheet)
//            .setFullWidth(false)
//            .show();
//
//        val addButton = builder.view.findViewById<Button>(R.id.add_btn)
//        val medInfo = builder.view.findViewById<EditText>(R.id.text_area_med_information)

//
//        val imm: InputMethodManager =
//            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager);
//        imm.showSoftInput(medInfo, InputMethodManager.SHOW_IMPLICIT);
//
//        addButton.setOnClickListener {
//            val info = medInfo.text;
//            println(info)
//
//            builder.dismiss();
//        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasPermissionInManifest(
                requireContext(),
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE
            ) && requestCode == MY_CAMERA_PERMISSION_CODE
        ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireContext(),
                    "camera permission granted",
                    Toast.LENGTH_LONG
                ).show();
                val cameraIntent: Intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG)
                    .show();
            }
        }
    }

    private fun hasPermissionInManifest(context: Context, permissionName: String): Boolean {
        val packageName: String = context.packageName;
        try {
            val packageInfo: PackageInfo = context.getPackageManager()
                .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            val declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null && declaredPermissions.size > 0) {
                for (p in declaredPermissions) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {

        }
        return false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmapArrayList: ArrayList<Bitmap> = ArrayList<Bitmap>();
        imageArr = ArrayList<RequestFileClaimImage>();

        if (data != null) {
            val clipData: ClipData? = data.clipData;
            if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
                if (clipData != null) {
                    for (i in 0 until clipData.getItemCount()) {


                        val imageUri: Uri = clipData.getItemAt(i).getUri();

                        try {
                            val inputStream: InputStream =
                                requireContext().contentResolver.openInputStream(imageUri)!!;
                            val bitmap = BitmapFactory.decodeStream(inputStream);
                            val contentResolver: ContentResolver = requireContext().contentResolver;
                            val mime: MimeTypeMap = MimeTypeMap.getSingleton();
                            val type: String =
                                "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                            val mImE: String? = contentResolver.getType(imageUri);


//                            val compress = ImageCompresser(context)
//                            val filePath = compress.compressImage(imageUri.toString(), 300F, 400F)
//                            val imgFile: File = File(imageUri.toString());
//                            val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath);

                            val imageBase64String: String =
                                UploadMediaReportActivity().getBaseString(bitmap);
                            imageArr.add(
                                RequestFileClaimImage(
                                    "ordermedicine",
                                    mImE,
                                    type,
                                    imageBase64String
                                )
                            );
                            bitmapArrayList.add(bitmap);
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    val imageUri: Uri = data.data!!;
                    try {
                        val inputStream: InputStream? =
                            requireContext().contentResolver.openInputStream(imageUri);
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream);
                        val contentResolver: ContentResolver = requireContext().contentResolver;
                        val mime: MimeTypeMap = MimeTypeMap.getSingleton();
                        val type: String =
                            "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                        val mImE: String? = contentResolver.getType(imageUri);


//                        val compress = ImageCompresser(context)
//                        val filePath = compress.compressImage(imageUri.toString(), 300F, 400F)
//                        val imgFile: File = File(imageUri.toString());


//                        val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath);

                        val imageBase64String: String =
                            UploadMediaReportActivity().getBaseString(bitmap);
                        imageArr.add(
                            RequestFileClaimImage(
                                "ordermedicine",
                                mImE,
                                type,
                                imageBase64String
                            )
                        );
                        bitmapArrayList.add(bitmap);
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace();
                    }
                }
            }

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                val photo: Bitmap = data.extras?.get("data") as (Bitmap);
                val imageUri: Uri =
                    UploadMediaReportActivity().getImageUri(requireContext(), photo);
                val contentResolver: ContentResolver = requireContext().contentResolver;
                val mime: MimeTypeMap = MimeTypeMap.getSingleton();
                val type: String =
                    "." + mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
                val mImE: String? = contentResolver.getType(imageUri);


//                val compress = ImageCompresser(context)
//                val filePath = compress.compressImage(imageUri.toString(), 300F, 400F)
//                val imgFile: File = File(imageUri.toString());
//                val myBitmap: Bitmap = BitmapFactory.decodeFile(imgFile.absolutePath);


                val imageBase64String: String = UploadMediaReportActivity().getBaseString(photo);
                imageArr.add(
                    RequestFileClaimImage(
                        "ordermedicine",
                        mImE,
                        type,
                        imageBase64String
                    )
                );
                bitmapArrayList.add(photo);
            }

            if (requestCode == 101 && resultCode == Activity.RESULT_OK) {


                if (data.data != null) {
                    println(data.data);

                    val pdfUri: Uri = data.data!!

                    val inputStream: InputStream? =
                        requireContext().contentResolver.openInputStream(pdfUri);
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    val contentResolver: ContentResolver = requireContext().contentResolver;
                    val mime: MimeTypeMap = MimeTypeMap.getSingleton();
                    val type: String =
                        "." + mime.getExtensionFromMimeType(contentResolver.getType(pdfUri));
                    val mImE: String? = contentResolver.getType(pdfUri);

                    val path: String? = pdfUri.path;

                    System.out.println(path);

                    val imageBase64String: String =
                        UploadMediaReportActivity().NewBase64(pdfUri.getPath());
                    imageArr.add(
                        RequestFileClaimImage(
                            "ordermedicine",
                            mImE,
                            type,
                            imageBase64String
                        )
                    );
                    val drawable: Drawable =
                        requireContext().applicationContext.resources
                            .getDrawable(R.drawable.ic_pdf);
                    bitmapArrayList.add((drawable as (BitmapDrawable)).bitmap);


//                    for (i in 0 until clipData!!.itemCount) {
//                        val pdfUri: Uri = clipData.getItemAt(i).getUri();
//
//                        try {
//                            val inputStream: InputStream? =
//                                requireContext().contentResolver.openInputStream(pdfUri);
////                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            val contentResolver: ContentResolver = requireContext().contentResolver;
//                            val mime: MimeTypeMap = MimeTypeMap.getSingleton();
//                            val type: String =
//                                "." + mime.getExtensionFromMimeType(contentResolver.getType(pdfUri));
//                            val mImE: String? = contentResolver.getType(pdfUri);
//
//                            val path: String? = pdfUri.path;
//
//                            System.out.println(path);
//
//                            val imageBase64String: String =
//                                UploadMediaReportActivity().NewBase64(pdfUri.getPath());
//                            imageArr.add(
//                                RequestFileClaimImage(
//                                    "ordermedicine",
//                                    mImE,
//                                    type,
//                                    imageBase64String
//                                )
//                            );
//                            val drawable: Drawable =
//                                requireContext().applicationContext.resources
//                                    .getDrawable(R.drawable.ic_pdf);
//                            bitmapArrayList.add((drawable as (BitmapDrawable)).bitmap);
//                        } catch (e: FileNotFoundException) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }


            mainView.submitMedicineProgressBar.visibility = View.VISIBLE
            UploadImageToServer().execute(0);

            if (bitmapArrayList.size > 0) {
                mainView.uploaded_media_recycler_view.visibility = View.VISIBLE;
            }


        }
    }

    private fun checkPermissions(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    internal inner class GetImageFromServer : AsyncTask<MyTaskParams, Void, String>() {
        var count: Int = 0;
        var imgStringURL: String = "";
        var imgMediaName: String = "";
        var imgMediaFolder: String = "";
        var mediaType: String = "";
        var mediaNote: String = "";


        override fun onPreExecute() {
            super.onPreExecute()
            mainView.submitMedicineProgressBar.visibility = View.VISIBLE;
        }

        override fun doInBackground(vararg params: MyTaskParams?): String {

            try {
                count = params[0]!!.count;
                imgStringURL = params[0]!!.imageUrl;
                imgMediaName = params[0]!!.mediaName;
                imgMediaFolder = params[0]!!.mediaFolder;
                mediaType = params[0]!!.mediaType;
                mediaNote = params[0]!!.mediaNote;

            } catch (e: IOException) {
                e.printStackTrace();
            }
            return imgStringURL;
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            mainView.submitMedicineProgressBar.visibility = View.GONE;
//            if (result != "") {
            insertItem(0, imgStringURL, imgMediaName, imgMediaFolder, mediaType, mediaNote);
            UploadImageToServer().execute(count + 1);
//            } else {

//                if (imgStringURL != null && imgMediaName != null && imgMediaFolder != null) {


//                    val image: ImageView = ImageView(context!!);
//                    Glide.with(context!!).load(R.drawable.ic_pdf).into(image);


//                insertItem(0, imgStringURL, imgMediaName, imgMediaFolder);
//                UploadImageToServer().execute(count + 1);
//                }

//            }
        }


    }

    fun insertItem(
        position: Int,
        imgUrl: String,
        mediaName: String,
        mediaFolder: String,
        type: String,
        note: String
    ) {

        mainView.uploaded_media_recycler_view.visibility = View.VISIBLE;
        mLayoutManager!!.scrollToPosition(0);
        mUploadDocumentList.add(
            position,
            UploadDocumentItem(
                imgUrl,
                mediaName,
                mediaFolder,
                type,
                note
            )
        );
        mUploadDocumentAdapter!!.notifyItemInserted(position);

        if (mUploadDocumentList.size > 0) {
            print(mUploadDocumentList)

            createNewOrder()

            submitButtonInterface = context as (SubmitButtonInterface);
            submitButtonInterface.onSubmitButtonClick(true)
        }
    }


    internal inner class UploadImageToServer : AsyncTask<Int, Void, Void>() {
        var count: Int = 0;

        override fun doInBackground(vararg params: Int?): Void? {
            count = params[0]!!;
            if (count < imageArr.size) {
//                mainView.submitMedicineProgressBar.visibility = View.VISIBLE
                val commonApiInterface: CommonApiInterface =
                    ApiClient.getClient().create(CommonApiInterface::class.java);


                var uploadFileClaimImagesRequestCall: Call<InsuranceResponseDataObj> =
                    commonApiInterface.uploadFileClaimImages(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.userToken,
                        imageArr[count]
                    );

                if (Constants.type == Constants.Type.HEMAS) {
                    uploadFileClaimImagesRequestCall =
                        commonApiInterface.uploadFileClaimImagesForHemas(
                            AppConfig.APP_BRANDING_ID,
                            prefManager.userToken,
                            imageArr[count]
                        );
                }


                uploadFileClaimImagesRequestCall.enqueue(object :
                    retrofit2.Callback<InsuranceResponseDataObj> {
                    override fun onResponse(
                        call: Call<InsuranceResponseDataObj>,
                        response: Response<InsuranceResponseDataObj>
                    ) {
                        if (response.isSuccessful) {
                            val insuranceResponseDataObj: InsuranceResponseDataObj =
                                response.body()!!;

                            val insuranceResponseDataJsonObject: JsonObject =
                                Gson().toJsonTree(
                                    insuranceResponseDataObj.data
                                ).asJsonObject;

                            val insuranceResponseDataJsonObjectMedia: JsonObject =
                                insuranceResponseDataJsonObject.get("media").asJsonObject;

                            upLoadedImageList.add(insuranceResponseDataJsonObjectMedia.get("URL").asString);




                            GetImageFromServer().execute(
                                MyTaskParams(
                                    insuranceResponseDataJsonObjectMedia.get("URL")
                                        .asString,
                                    count,
                                    insuranceResponseDataJsonObjectMedia.get("MediaName")
                                        .asString,
                                    insuranceResponseDataJsonObjectMedia.get("MediaFolder")
                                        .asString,
                                    imageArr[count].file_contentType,
                                    ""
                                )
                            );


                            val toast: Toast = Toast.makeText(
                                context!!.applicationContext,
                                "Uploaded image successfully",
                                Toast.LENGTH_LONG
                            );
                            toast.show();
                        }
                    }

                    override fun onFailure(p0: Call<InsuranceResponseDataObj>, p1: Throwable) {
                        mainView.submitMedicineProgressBar.visibility = View.GONE
                        val toast: Toast = Toast.makeText(
                            context!!.applicationContext,
                            R.string.servcer_not_available,
                            Toast.LENGTH_SHORT
                        );
                        toast.show();
                    }

                });
            } else {
                try {
                    mainView.submitMedicineProgressBar.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            return null
        }


    }

    override fun orderMedicineHistoryItemClick(commonHistoryItem: OMCreatedOrderObj) {


        if (commonHistoryItem.status.equals("Order Confirmed")
            ||
            commonHistoryItem.status.equals("Payment Completed")
            ||
            commonHistoryItem.status.equals("Dispatched")
            ||
            commonHistoryItem.status.equals("On the way")
            ||
            commonHistoryItem.status.equals("Processing")
        ) {
            val jsonObject: JsonObject = Gson().toJsonTree(commonHistoryItem).asJsonObject;
            OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
            val intent = Intent(requireContext(), OMPage5TrackOrder::class.java)
            startActivity(intent)
        } else if (commonHistoryItem.status.equals("Payment Updated")) {


            changeFragmentInterface = context as (ChangeFragmentInterface);
            changeFragmentInterface.onMedicineHistoryOrderButtonClick(commonHistoryItem)

//            processAction("payment", "prescriptionorder:${oMCreatedOrderObj.id}")

        } else {

            val changedObject = OMCreatedOrderObj(
                null,
                null,
                null,
                null,
                commonHistoryItem.status,
                null,
                null,
                null,
                null,
                commonHistoryItem.files,
                commonHistoryItem.address,
                commonHistoryItem.partner,
                commonHistoryItem.payment
            );
            val changedJsonObject: JsonObject = Gson().toJsonTree(changedObject).asJsonObject;
            oMCreatedOrderObj =
                OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(changedJsonObject)

            if (commonHistoryItem.status.equals("Delivered") || commonHistoryItem.status.equals("Cancelled")) {
//                oMCreatedOrderObj.status = null
//                val statusChangedJsonObject: JsonObject =
//                    Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;
//                oMCreatedOrderObj =
//                    OMCommon(context!!).saveToCommonSingletonAndRetrieve(statusChangedJsonObject)

                if (oMCreatedOrderObj.files != null) {
                    for (i in 0 until oMCreatedOrderObj.files!!.size) {
                        val mf: OMMediaFiles = oMCreatedOrderObj.files!![i]

                        upLoadedImageList.add(
                            mf.URL!!
                        );

                        GetImageFromServer().execute(
                            MyTaskParams(
                                mf.URL,
                                i,
                                mf.MediaName,
                                mf.MediaFolder,
                                mf.MediaType,
                                mf.note
                            )
                        );


                    }
                }
            } else {
                val jsonObject: JsonObject = Gson().toJsonTree(commonHistoryItem).asJsonObject;
                oMCreatedOrderObj =
                    OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
                val intent = Intent(requireContext(), OMMainPage::class.java)
                startActivity(intent)
            }


//            val intent = Intent(context!!, OMMainPage::class.java)
//            startActivity(intent)
        }

//        if (commonHistoryItem.status.equals("Processing")
//            || commonHistoryItem.status.equals("Payment Pending")
//            || commonHistoryItem.status.equals("Payment Completed")
//            || commonHistoryItem.status.equals("Order Confirmed")
//            || commonHistoryItem.status.equals("Delivered")
//            || commonHistoryItem.status.equals("Dispatched")
//            || commonHistoryItem.status.equals("Cancelled")
//        ) {
//
//
//
//
//            changeFragmentInterface = context as (ChangeFragmentInterface);
//            changeFragmentInterface.onOMPage1SubmitNextButtonClick(4)
//        }
    }

    override fun orderMedicineHistoryMainCardClick(commonHistoryItem: OMCreatedOrderObj) {
        print(commonHistoryItem)


        if (commonHistoryItem.status == "Payment Updated") {
            changeFragmentInterface = context as (ChangeFragmentInterface);
            changeFragmentInterface.onMedicineHistoryOrderButtonClick(commonHistoryItem)


        } else {
            val jsonObject: JsonObject = Gson().toJsonTree(commonHistoryItem).asJsonObject;
            OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject);
            changeFragmentInterface = context as (ChangeFragmentInterface);
            changeFragmentInterface.onOMPage1SubmitNextButtonClick(4)
        }


    }

    private fun openConfirmationDialog(c: Context) {
        val dialogView: android.app.AlertDialog;

        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(c);
        val inflater: LayoutInflater =
            c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        val layoutView: View = inflater.inflate(R.layout.new_common_alert_popup, null, false);
        builder.setView(layoutView);
        dialogView = builder.create();
        dialogView.setCancelable(false);
//        dialogView.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogView.show();

        val no_btn: TextView = layoutView.findViewById(R.id.no_btn) as (TextView);
        val yes_btn: Button = layoutView.findViewById(R.id.yes_btn) as (Button);
        no_btn.setOnClickListener {


            if (oMCreatedOrderObj.files != null) {
                removeFromServer()
            }


            val empty = OMCreatedOrderObj(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            );
            val emptyJsonObject: JsonObject = Gson().toJsonTree(empty).asJsonObject;


            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(emptyJsonObject)
            oMCreatedOrderObj =
                OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(emptyJsonObject)

            submitButtonInterface = context as (SubmitButtonInterface);
            submitButtonInterface.onSubmitButtonClick(false)

            dialogView.dismiss()
        }

        yes_btn.setOnClickListener {
            dialogView.dismiss()
//            changeFragmentInterface = context as (ChangeFragmentInterface);
//            changeFragmentInterface.onOMPage1SubmitNextButtonClick(2)

            loadMediaFiles()


        }


    }

    private fun loadMediaFiles() {
        println(oMCreatedOrderObj.files)

        val t = oMCreatedOrderObj.files

        print(t)
        if (oMCreatedOrderObj.files != null) {
            for (i in 0 until oMCreatedOrderObj.files!!.size) {
                val mf: OMMediaFiles = oMCreatedOrderObj.files!![i]

                upLoadedImageList.add(
                    mf.URL!!
                );

                insertItem(
                    0,
                    mf.URL!!,
                    mf.MediaName!!,
                    mf.MediaFolder!!,
                    mf.MediaType!!,
                    mf.note!!
                );


            }
        }
    }


    private fun removeFromServer() {
        for (i in 0 until oMCreatedOrderObj.files!!.size) {

            val mf: OMMediaFiles = oMCreatedOrderObj.files!![i]


            print(mf)


            if (mf.MediaType != "text/plain") {
                mainView.submitMedicineProgressBar.visibility = View.VISIBLE
                val commonApiInterface: CommonApiInterface =
                    ApiClient.getClient().create(CommonApiInterface::class.java);
                val removeFileClaimImagesRequestCall: Call<InsuranceResponseDataObj> =
                    commonApiInterface.removeFileClaimImages(
                        AppConfig.APP_BRANDING_ID,
                        prefManager.userToken,
                        RequestRemoveFileClaimImage(
                            mf.URL,
                            mf.MediaName,
                            mf.MediaFolder
                        )
                    );


                removeFileClaimImagesRequestCall.enqueue(object :
                    retrofit2.Callback<InsuranceResponseDataObj> {
                    override fun onResponse(
                        p0: Call<InsuranceResponseDataObj>,
                        response: Response<InsuranceResponseDataObj>
                    ) {
                        if (response.isSuccessful) {
                            mainView.submitMedicineProgressBar.visibility = View.GONE
                        }

                    }

                    override fun onFailure(p0: Call<InsuranceResponseDataObj>, p1: Throwable) {
                        mainView.submitMedicineProgressBar.visibility = View.GONE
                    }

                })
            } else {

            }


        }
    }
}



