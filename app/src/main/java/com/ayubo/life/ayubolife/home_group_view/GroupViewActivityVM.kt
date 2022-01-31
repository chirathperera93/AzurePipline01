package com.ayubo.life.ayubolife.home_group_view

import android.content.SharedPreferences
import android.util.Log
import com.ayubo.life.ayubolife.ask.AskCustomData
import com.ayubo.life.ayubolife.ask.AskCustomObject
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.payment.model.OtherPaymentOptionsData
import com.ayubo.life.ayubolife.prochat.common.MSG_FAILED_REQUEST
import com.ayubo.life.ayubolife.prochat.common.MSG_SUCCESS
import com.ayubo.life.ayubolife.prochat.common.getAuthToken
import com.ayubo.life.ayubolife.prochat.data.model.State
import com.ayubo.life.ayubolife.prochat.data.sources.remote.ApiService
import javax.inject.Inject

class GroupViewActivityVM @Inject constructor(val apiService: ApiService,
                                              val sharedPref: SharedPreferences) {

    lateinit var dataList: GroupViewMainData
    var groupViewMainDataListNew: ArrayList<GroupViewMainData>? = null

    fun getAllGroupItems(store_group_items: String) = apiService.getAllGroupItems(sharedPref.getAuthToken(), AppConfig.APP_BRANDING_ID, store_group_items).map {
        if ((it.isSuccessful && it.body() != null) && (it.body()!!.result == 0)) {
            dataList = it.body()?.data as GroupViewMainData

            val categoryDataList: ArrayList<String>? = ArrayList<String>()
            var groupViewMainDataListDataList: ArrayList<GroupViewMainDataList>? = null
            groupViewMainDataListNew = ArrayList<GroupViewMainData>()


            for (c in 0 until dataList.list.size) {
                val data = dataList.list[c]
                if (categoryDataList!!.size > 0) {
                    if (!categoryDataList.contains(data.item_sub_category)) {
                        categoryDataList.add(data.item_sub_category)
                    }
                } else {
                    categoryDataList.add(data.item_sub_category)

                }
            }


            for (n in 0 until categoryDataList!!.size) {
                groupViewMainDataListDataList = ArrayList<GroupViewMainDataList>()
                val subCategoryName = categoryDataList[n]

                for (i in 0 until dataList.list.size) {
                    val data = dataList.list[i]
                    if (dataList.list[i].item_sub_category == subCategoryName) {


                        val viewMainData = GroupViewMainDataList(
                                data.title,
                                data.bg_img,
                                data.action,
                                data.meta,
                                data.item_sub_text,
                                data.item_sub_category,
                                data.item_short_description,
                                data.offer,
                                data.offer_text
                        )
                        groupViewMainDataListDataList.add(viewMainData)
                    }
                }

                val singleCategoryConsultList = GroupViewMainData(subCategoryName, groupViewMainDataListDataList)
                groupViewMainDataListNew!!.add(singleCategoryConsultList)
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