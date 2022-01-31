package com.ayubo.life.ayubolife.reports.upload

import android.content.SharedPreferences
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.OtherPaymentOptionsData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.rest.ApiClient
import io.reactivex.Flowable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadReportActivityVM @Inject constructor(val apiService: ApiService,
                                                 val sharedPref: SharedPreferences) {
    var dataRes: ReportUploadResponse? = null
    lateinit var dataList: ArrayList<OtherPaymentOptionsData>

    fun uploadUserReports(
            doctor_id: String,
            notes: String,
            isReportReviewFree: Boolean,
            appointment_source: String,
            imagesList: List<File>,
            reportUrls: List<String>
    ): Flowable<State> {

        val report_image = ArrayList<MultipartBody.Part>()
        val report_file = ArrayList<MultipartBody.Part>()

//        reportUrls.forEach {
//            report_file.add(MultipartBody.Part.createFormData("report_id[]", it))
//        }
        imagesList.forEach {
            report_image.add(prepareFilePart("report_image[]", it))
        }
        for (i in 0 until reportUrls.size) {
            report_file.add(MultipartBody.Part.createFormData("report_id[]", reportUrls[i]))
        }

        return apiService.uploadUserReports(
                ApiClient.BASE_URL + "api.ayubo.life/public/api/ehr/v1/app/report_upload",
                sharedPref.getAuthToken(),
                AppConfig.APP_BRANDING_ID,
                createPartFromString(doctor_id),
                createPartFromString(notes),
                createPartFromString(isReportReviewFree.toString()),
                createPartFromString(appointment_source.toString()),
                report_image,
                report_file).map {
            if (it.isSuccessful && it.body() != null) {

                dataRes = it.body()

                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            State(false, MSG_FAILED_REQUEST)
        }
    }

    //    @Part report_image: List<MultipartBody.Part>,
//    @Part report_file: List<MultipartBody.Part>): Flowable<Response<ResponseBody>>
    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
    }


    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {

        // create RequestBody instance from file
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile)
    }
}