package com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate

import android.content.SharedPreferences
import android.net.Uri
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.janashakthionboarding.data.model.Expert
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY
import io.reactivex.Flowable
import okhttp3.MediaType
import javax.inject.Inject
import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import java.io.File


class MedicalUpdateVM @Inject constructor(val apiService: ApiService,
                                          val sharedPref: SharedPreferences) {


    //    userID:String,
    fun uploadMedicalReport(user: String, relateID: String, height_feet: Int, height_inches: Int, weight: Int, sys: Int, dia: Int,
                            images: List<ImageFileData>): Flowable<State> {

        val fileBodyList = ArrayList<MultipartBody.Part>()
        val reportIdList = ArrayList<MultipartBody.Part>()
        val reportDatesList = ArrayList<MultipartBody.Part>()


        images.forEach {
            fileBodyList.add(prepareFilePart("report_file[]", it.file))
            reportIdList.add(MultipartBody.Part.createFormData("report_id[]", it.id))
            reportDatesList.add(MultipartBody.Part.createFormData("report_date[]", it.date))
        }

        return apiService.uploadMedicalData(MAIN_URL_LIVE_HAPPY + "index.php?entryPoint=janashanthi_medical_summary", sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID,
                AppConfig.APPLICATION_TOKEN, createPartFromString(user), createPartFromString(relateID), createPartFromString(height_feet.toString()), createPartFromString(height_inches.toString()),
                createPartFromString(weight.toString()), createPartFromString(sys.toString()), createPartFromString(dia.toString()),
                fileBodyList, reportIdList, reportDatesList).map {
            if (it.isSuccessful && it.body() != null) {
                State(true, MSG_SUCCESS)
            } else {
                State(false, MSG_FAILED_REQUEST)
            }
        }.onErrorReturn {
            State(false, MSG_FAILED_REQUEST)
        }
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part {

        // create RequestBody instance from file
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile)
    }

    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
    }
}