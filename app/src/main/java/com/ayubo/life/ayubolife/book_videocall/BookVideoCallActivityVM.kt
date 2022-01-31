package com.ayubo.life.ayubolife.book_videocall

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.book_videocall.model.*
import com.ayubo.life.ayubolife.channeling.model.Expert
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import com.ayubo.life.ayubolife.rest.ApiClient
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class BookVideoCallActivityVM @Inject constructor(val apiService: ApiService,
                                                  val sharedPref: SharedPreferences
) {

    lateinit var newvideoCallList: ArrayList<Expert>
    var videoCallList: ArrayList<BookVideoCallActivityMainResponseData>? = null
    lateinit var todayDataList: ArrayList<BookVideoCallActivityMainResponseData>
    lateinit var futureDataList: ArrayList<BookVideoCallActivityMainResponseData>
    // val newvideoCallList: MutableList<Expert> = mutableListOf()


    fun convertDateToLong(date: String): Long {
        var result: Long = 0
        try {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm")
            result = df.parse(date).time


        } catch (e: Exception) {

            Log.e("-------Error-----", e.toString())
        }
        return result

    }


    private fun getDateAsString(s: Timestamp): String {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s.time)

            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getDoctorListsData(method: String, api_key: String, rest_data: String) = apiService.getDoctorListsData(ApiClient.BASE_URL + "custom/service/v7/rest.php", AppConfig.APP_BRANDING_ID, api_key, method, "JSON", "JSON", rest_data).map {
        if (it.isSuccessful && it.body() != null) {
            val mainResponse = it.body()

            val askDataListt = mainResponse!!.data


            val ssnewvideoCallList = askDataListt as ArrayList<Expert>
            newvideoCallList = ssnewvideoCallList


            todayDataList = ArrayList<BookVideoCallActivityMainResponseData>()
            futureDataList = ArrayList<BookVideoCallActivityMainResponseData>()
            //  val  videoCallList=ArrayList<Any>()
            videoCallList = ArrayList<BookVideoCallActivityMainResponseData>()
            var locationListt = ArrayList<Locations>()
            var isThereTodayAppointments = false
            var isThereFutureAppointments = false



            for (i in 0 until askDataListt.size) {

                val doctorData = askDataListt[i]
                Log.d("====d=====", doctorData.next)
                val timestamp = Timestamp(convertDateToLong(doctorData.next))
                //Getting today date string.........
                val currentTimestamp = System.currentTimeMillis()
                val timestampToday: Timestamp = Timestamp(currentTimestamp)
                val today: String = getDateAsString(timestampToday)
                val date1 = getDateAsString(timestamp)

                if (date1.compareTo(today) == 0) {
                    isThereTodayAppointments = true

                    for (n in 0 until doctorData.locations.size) {
                        val newLoca = Locations(doctorData.locations[n].id,
                                doctorData.locations[n].name,
                                doctorData.locations[n].fee,
                                doctorData.locations[n].fee_value.toInt(),
                                doctorData.locations[n].next_available)
                        locationListt.add(newLoca)
                    }

                    val newVideo = Video(doctorData.video.enable, doctorData.video.meta)
                    val newChann = Channel(doctorData.channel.enable, doctorData.channel.meta)
                    val newRev = Review(doctorData.review.enable, doctorData.review.meta)
                    val newAsk = Ask(doctorData.ask.enable, doctorData.ask.meta)

                    val newProfile = Profile(doctorData.profile.action, doctorData.profile.meta)
                    val newBooking = Booking(doctorData.booking.action, doctorData.booking.meta)

                    // doctorData.next
                    val newObj = BookVideoCallActivityMainResponseData(
                            doctorData.id,
                            doctorData.title,
                            doctorData.speciality,
                            doctorData.name,
                            doctorData.picture,
                            doctorData.next,
                            locationListt,
                            newVideo,
                            newChann,
                            newRev,
                            newAsk,
                            newProfile,
                            newBooking)

                    todayDataList.add(newObj)
                } else {
                    isThereFutureAppointments = true
                    for (n in 0 until doctorData.locations.size) {
                        val newLoca = Locations(doctorData.locations[n].id,
                                doctorData.locations[n].name,
                                doctorData.locations[n].fee,
                                doctorData.locations[n].fee_value.toInt(),
                                doctorData.locations[n].next_available)
                        locationListt.add(newLoca)
                    }

                    val newVideo = Video(doctorData.video.enable, doctorData.video.meta)
                    val newChann = Channel(doctorData.channel.enable, doctorData.channel.meta)
                    val newRev = Review(doctorData.review.enable, doctorData.review.meta)
                    val newAsk = Ask(doctorData.ask.enable, doctorData.ask.meta)
                    val newProfile = Profile(doctorData.profile.action, doctorData.profile.meta)
                    val newBooking = Booking(doctorData.booking.action, doctorData.booking.meta)

                    val newObj2 = BookVideoCallActivityMainResponseData(
                            doctorData.id,
                            doctorData.title,
                            doctorData.speciality,
                            doctorData.name,
                            doctorData.picture,
                            doctorData.next,
                            locationListt,
                            newVideo,
                            newChann,
                            newRev,
                            newAsk,
                            newProfile,
                            newBooking)

                    futureDataList.add(newObj2)
                }
            }


            if (isThereTodayAppointments) {
                //     videoCallList!!.add("Today")
                //      videoCallList!!.addAll(todayDataList)
                //val todayData= BookVideoCallListData("Today",todayDataList)
                videoCallList!!.addAll(todayDataList)
            }



            if (isThereFutureAppointments) {

                //    videoCallList!!.add("Next")
                //    videoCallList!!.addAll(futureDataList)

                // val futureData= BookVideoCallListData("Next",futureDataList)
                videoCallList!!.addAll(futureDataList)
            }



            State(true, MSG_SUCCESS)
        } else {
            State(false, MSG_FAILED_REQUEST)
        }
    }.onErrorReturn {
        Log.d("Erro.........", it.stackTrace.toString())
        State(false, MSG_FAILED_REQUEST)
    }

}