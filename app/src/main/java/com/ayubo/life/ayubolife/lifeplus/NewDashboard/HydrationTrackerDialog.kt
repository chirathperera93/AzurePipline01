package com.ayubo.life.ayubolife.lifeplus.NewDashboard

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.insurances.CommonApiInterface
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.payment.EXTRA_REPORT_ID
import com.ayubo.life.ayubolife.reports.activity.ReportByIdResponse
import com.ayubo.life.ayubolife.reports.activity.ReportTypeItem
import com.ayubo.life.ayubolife.reports.activity.ReportTypeMain
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.flavors.changes.Constants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.hydration_tracker_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HydrationTrackerDialog : Activity() {
    lateinit var appToken: String;
    lateinit var pref: PrefManager;

    var reportTypeItemArray: ArrayList<ReportTypeItem> = ArrayList<ReportTypeItem>();
    var reportTypeItemArrayMain: ArrayList<ReportTypeMain> = ArrayList<ReportTypeMain>();
//    lateinit var reportDetailTypes: LinearLayout;

    var upLoadedImageList: ArrayList<String> = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hydration_tracker_dialog)
        pref = PrefManager(baseContext)
        appToken = pref.userToken
        val displayMetrics: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext)

        val width = deviceScreenDimension.displayWidth
        val height = deviceScreenDimension.displayHeight

        window.setLayout(((width * .8).toInt()), ((height * .7).toInt()))


        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.attributes = params;
        window.setBackgroundDrawableResource(R.drawable.bg_round)

        new_dashboard_loading.visibility = View.VISIBLE

        setButton();
        readExtras()
    }

    fun readExtras() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(EXTRA_REPORT_ID)) {
            val meta: String = bundle.getSerializable(EXTRA_REPORT_ID) as String
            getReportById(meta.toInt()).execute()

        }
    }

    internal inner class getReportById(meta: Int) : AsyncTask<Void, Void, Void>() {

        val reportId = meta;

        override fun doInBackground(vararg params: Void?): Void? {
            val apiService: ApiInterface =
                    ApiClient.getReportsApiClientNewForHemas().create(ApiInterface::class.java);
            val call: Call<ReportByIdResponse> =
                    apiService.getReportById(AppConfig.APP_BRANDING_ID, appToken, reportId);


            call.enqueue(object : Callback<ReportByIdResponse> {
                override fun onResponse(
                        call: Call<ReportByIdResponse>,
                        response: Response<ReportByIdResponse>
                ) {
                    if (response.isSuccessful) {
                        new_dashboard_loading.visibility = View.GONE


                        val mainResponse: ReportByIdResponse? = response.body();


                        val reportFieldsObject: JsonObject =
                                Gson().toJsonTree(mainResponse?.getData()).getAsJsonObject();
                        val jsonArray: JsonArray =
                                reportFieldsObject.get("parameter").getAsJsonArray();



                        hydration_tracker_title.setText(reportFieldsObject.get("name").asString)
                        hydration_tracker_sub.setText(reportFieldsObject.get("description").asString)

                        upLoadedImageList = ArrayList<String>()
                        reportTypeItemArray = ArrayList<ReportTypeItem>();
                        reportTypeItemArrayMain = ArrayList<ReportTypeMain>();


                        for (item in jsonArray) {
                            val jsonElement: JsonElement = item;
                            val jsonObject: JsonObject = jsonElement.getAsJsonObject();

                            var PanicMax: String = "";
                            var PanicMin: String = "";
                            var UOM: String = "";
                            var HasUoM: String = "";

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


                            val reportTypeItem: ReportTypeItem = ReportTypeItem(
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

                        val imageStringArrayList: ArrayList<String> = ArrayList<String>();
                        val reportTypeMain: ReportTypeMain = ReportTypeMain(
                                reportFieldsObject.get("name").getAsString(),
                                reportTypeItemArray,
                                reportFieldsObject.get("id").getAsString(),
                                imageStringArrayList
                        );


                        reportTypeItemArrayMain.add(reportTypeMain);

                        val inflater: LayoutInflater = LayoutInflater.from(baseContext);
                        reportDetailTypes.removeAllViews();

                        for (reportTypeItem in reportTypeItemArrayMain.get(0).reportTypeItemArrayList) {
                            var reportTI: ReportTypeItem = reportTypeItem;

                            if (reportTI.parameterType != 9) {
                                val custLayout: View = inflater.inflate(
                                        R.layout.custom_layout_for_report_type,
                                        null,
                                        false
                                );
                                val label: TextView =
                                        custLayout.findViewById(R.id.report_type_label);
                                val value: TextView =
                                        custLayout.findViewById(R.id.report_type_value);
                                val measurement: TextView =
                                        custLayout.findViewById(R.id.report_type_measurement);

                                label.setText(reportTI.name);
                                value.setText(reportTI.value);
                                measurement.setText(reportTI.uom);

                                value.addTextChangedListener(object : TextWatcher {
                                    override fun beforeTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                    ) {

                                    }

                                    override fun onTextChanged(
                                            p0: CharSequence?,
                                            p1: Int,
                                            p2: Int,
                                            p3: Int
                                    ) {

                                    }

                                    override fun afterTextChanged(editable: Editable?) {
                                        System.out.println(editable);
                                        System.out.println(editable.toString());

                                        reportTI.value = editable.toString()


                                    }

                                })



                                reportDetailTypes.addView(custLayout);
                            }
                        }


                    } else {
                        new_dashboard_loading.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<ReportByIdResponse>, t: Throwable) {

                    t.printStackTrace()
                    new_dashboard_loading.visibility = View.GONE
                }

            });


            return null;
        }
    }

    fun setButton() {
        hydration_tracker_close_btn.setOnClickListener {
            finish()
        }

        hydration_tracker_update_btn.setOnClickListener {
            System.out.println(reportTypeItemArrayMain)


            new_dashboard_loading.visibility = View.VISIBLE;

            reportTypeItemArrayMain.get(0).uploads = upLoadedImageList;
            System.out.println(reportTypeItemArrayMain);

            val reportTypeMain: ReportTypeMain = ReportTypeMain(
                    reportTypeItemArrayMain.get(0).name,
                    reportTypeItemArrayMain.get(0).parameter,
                    reportTypeItemArrayMain.get(0).id,
                    reportTypeItemArrayMain.get(0).uploads
            );


            saveUpdatedReportTypes(reportTypeMain);


        }
    }


    private fun saveUpdatedReportTypes(reportTypeItem: ReportTypeMain) {
        val commonApiInterface: CommonApiInterface =
                ApiClient.getClient().create(CommonApiInterface::class.java);


        var getAllReportTypesRequestCall: Call<ReportByIdResponse> =
                commonApiInterface.createRecord(appToken, AppConfig.APP_BRANDING_ID, reportTypeItem);

        if (Constants.type == Constants.Type.HEMAS) {
            getAllReportTypesRequestCall = commonApiInterface.createRecordForHemas(
                    appToken,
                    AppConfig.APP_BRANDING_ID,
                    reportTypeItem
            );
        }


        getAllReportTypesRequestCall.enqueue(object : Callback<ReportByIdResponse> {
            override fun onResponse(
                    call: Call<ReportByIdResponse>,
                    response: Response<ReportByIdResponse>
            ) {


                new_dashboard_loading.visibility = View.GONE
                if (response.isSuccessful()) {
                    System.out.println(response);
                    Toast.makeText(
                            baseContext,
                            "Successfully uploaded your report",
                            Toast.LENGTH_LONG
                    ).show();
                    finish();
                }


            }

            override fun onFailure(call: Call<ReportByIdResponse>, t: Throwable) {
                new_dashboard_loading.visibility = View.GONE
            }


        });


//        getAllReportTypesRequestCall.enqueue(new retrofit2 . Callback < ReportByIdResponse >() {
//
//            @Override
//            public void onResponse(Call<ReportByIdResponse> call, Response<ReportByIdResponse> response) {
//                uploadMediaRecordProgressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    System.out.println(response);
//                    Toast.makeText(UploadMediaReportActivity.this, "Successfully uploaded your report", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReportByIdResponse> call, Throwable t) {
//                uploadMediaRecordProgressBar.setVisibility(View.GONE);
//            }
//        });
    }

}