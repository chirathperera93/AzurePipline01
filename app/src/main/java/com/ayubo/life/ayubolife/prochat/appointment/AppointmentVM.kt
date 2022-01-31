package com.ayubo.life.ayubolife.prochat.appointment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.*
import com.ayubo.life.ayubolife.prochat.data.model.Conversation
import com.ayubo.life.ayubolife.prochat.data.model.OtherUser
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.prochat.data.sources.remote.requests.RequestChatAll
import com.ayubo.life.ayubolife.prochat.mp3recorder.Mp3Recorder
import com.ayubo.life.ayubolife.prochat.util.CommonUtils
import io.reactivex.Flowable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.*
import javax.inject.Inject


class AppointmentVM @Inject constructor(
    val apiService: ApiService,
    val sharedPref: SharedPreferences
) {

    var otherUser: OtherUser? = null
    private var mRecording = false
    private var mFile: File? = null
    //  private var patientID String

    var conversations: ArrayList<Conversation>? = ArrayList()
//    fun getSingleChat(appointmentId: String) =
//        apiService.getSingleChat(sharedPref.getAuthTokenWithBearer(), appointmentId)
//            .map {
//                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
//                    conversations = it.body()!!.data.conversations
//                    State(true, MSG_SUCCESS)
//                } else {
//                    State(false, MSG_FAILED_REQUEST)
//                }
//            }.onErrorReturn {
//                State(false, MSG_FAILED_REQUEST)
//            }
    //  sharedPref.getAuthTokenWithBearer()


    fun getChatAll(doctorID: String): Flowable<State>? {
        val token = sharedPref.getAuthToken()
        var patientID = sharedPref.getCurrentUser().userId

        return apiService.getChatAll(token, RequestChatAll(contact_id = doctorID))
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
                    conversations = it.body()!!.data.conversations
                    otherUser = it.body()!!.data.other_user

                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }
    }

    fun deleteChat(conversationIds: String): Flowable<State>? {

        return apiService.deleteSingleChat(
            sharedPref.getAuthToken(),
            AppConfig.APP_BRANDING_ID,
            conversationIds
        )
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.data != null) {
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                System.out.println(it)
                State(false, MSG_FAILED_REQUEST)
            }
    }

    fun getCurrentUser() = sharedPref.getCurrentUser()

    private var recordTask: Mp3Recorder? = null

    fun record(context: Context) {
        mFile = CommonUtils.getTempFile("audio", "mp3")

//        mMediaRecorder = MediaRecorder()
//        mMediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mMediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mMediaRecorder!!.setOutputFile(mFile!!.getAbsolutePath())
//        mMediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        mMediaRecorder!!.setAudioEncodingBitRate(128000)
//        mMediaRecorder!!.setAudioSamplingRate(44100)
//        mMediaRecorder!!.setAudioChannels(1)
//
//        try {
//            mMediaRecorder!!.prepare()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        mMediaRecorder!!.start()
//        mRecording = true
//        recordTask = AudioRecorderAyubo(context,mFile)
//        recordTask!!.startRecording()
        recordTask = Mp3Recorder(mFile)
        recordTask!!.startRecording()
    }

    fun isRecording(): Boolean {
        return mRecording
    }

    fun stopRecoding() {
        recordTask!!.stopRecording()
//        mRecording = false
//        mMediaRecorder!!.stop()
//        mMediaRecorder!!.release()
//        mMediaRecorder = null
    }

    fun getFile(): File? {
        return mFile
    }

    fun addChat(contact_id: String, file: File?, text: String): Flowable<State> {

        if (file == null) {
            return apiService.addChat(sharedPref.getAuthToken(), contact_id, text, "TEXT")
                .map {
                    if (it.isSuccessful && it.body() != null && it.body()!!.result == 0) {
                        conversations!!.add(it.body()!!.data)
                        State(true, MSG_SUCCESS)
                    } else {
                        State(false, MSG_FAILED_REQUEST)
                    }
                }.onErrorReturn {
                    State(false, MSG_FAILED_REQUEST)
                }
        } else {

            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            var multipartBody: MultipartBody.Part? = null

            multipartBody = MultipartBody.Part.createFormData("file_input", file.name, requestFile)

            return apiService.addChat(
                sharedPref.getAuthToken(),
                contact_id,
                multipartBody,
                text,
                "MEDIA"
            )
                .map {
                    if (it.isSuccessful && it.body() != null && it.body()!!.result == 0) {
                        conversations!!.add(it.body()!!.data)
                        State(true, MSG_SUCCESS)
                    } else {
                        State(false, MSG_FAILED_REQUEST)
                    }
                }.onErrorReturn {
                    State(false, MSG_FAILED_REQUEST)
                }
        }
    }

    fun download(url: String): Flowable<ResponseBody> {
        mFile = CommonUtils.getFile(url.getFileNameFromURL())
        return apiService.downloadFileWithDynamicUrlSync(url)
            .doOnNext { responseBody ->
                val downloaded = writeResponseBodyToDisk(responseBody, mFile!!)
            }
    }

    fun openNewTabWindow(urls: String, context: Context) {
        val uris = Uri.parse(urls)
        val defaultBrowser =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
        defaultBrowser.setData(uris);
        return context.startActivity(defaultBrowser)
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, futureStudioIconFile: File): Boolean {
        try {
            // todo change the file location/name according to your needs
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");
            //File futureStudioIconFile = FileUtil.getTempFile(name,"pdf");

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush()

                return true

            } catch (e: IOException) {
                e.printStackTrace()
                return false

            } finally {
                inputStream?.close()

                outputStream?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }


    fun updateStatus(appointmentId: String, action: String) =
        apiService.updateStatus(sharedPref.getAuthTokenWithBearer(), appointmentId, action)
            .map {
                if (it.isSuccessful && it.body() != null && it.body()!!.result == 0) {
                    State(true, MSG_SUCCESS)
                } else {
                    State(false, MSG_FAILED_REQUEST)
                }
            }.onErrorReturn {
                State(false, MSG_FAILED_REQUEST)
            }

}