package com.ayubo.life.ayubolife.health

import android.content.Context
import com.ayubo.life.ayubolife.activity.PrefManager
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * Created by Chirath Perera on 2021-08-01.
 */
class OMCommon(context: Context) {
    var baseContext = context;
    lateinit var prefManager: PrefManager;
    lateinit var oMCreatedOrderObj: OMCreatedOrderObj

    fun retrieveFromCommonSingleton(): OMCreatedOrderObj {
        prefManager = PrefManager(baseContext);
        val oMCreatedOrder = prefManager.orderMedicineCreatedOrderFromCommon
        oMCreatedOrderObj = Gson().fromJson(oMCreatedOrder, OMCreatedOrderObj::class.java);
        return oMCreatedOrderObj;
    }

    fun saveToCommonSingletonAndRetrieve(jsonObject: JsonObject): OMCreatedOrderObj {
        prefManager = PrefManager(baseContext);
        prefManager.setOrderMedicineCreatedOrderFromCommon(jsonObject)
        return retrieveFromCommonSingleton()
    }


    fun retrieveFromDraftSingleton(): OMCreatedOrderObj {
        prefManager = PrefManager(baseContext);
        val oMCreatedOrder = prefManager.orderMedicineCreatedOrder
        if (!oMCreatedOrder.equals("")) {
            oMCreatedOrderObj = Gson().fromJson(oMCreatedOrder, OMCreatedOrderObj::class.java);
        } else {
            oMCreatedOrderObj = OMCreatedOrderObj(
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
        }

        return oMCreatedOrderObj;
    }

    fun saveToDraftSingletonAndRetrieve(jsonObject: JsonObject): OMCreatedOrderObj {
        prefManager = PrefManager(baseContext);
        prefManager.setOrderMedicineCreatedOrder(jsonObject)
        return retrieveFromDraftSingleton()
    }


//    fun retrieveFromSingletonFromHistory(): OMCreatedOrderObj {
//        prefManager = PrefManager(baseContext);
//        val oMCreatedOrder = prefManager.orderMedicineCreatedOrderFromHistory
//        oMCreatedOrderObj = Gson().fromJson(oMCreatedOrder, OMCreatedOrderObj::class.java);
//        return oMCreatedOrderObj;
//    }
//
//    fun saveToSingletonAndRetrieveFromHistory(jsonObject: JsonObject): OMCreatedOrderObj {
//        prefManager = PrefManager(baseContext);
//        prefManager.setOrderMedicineCreatedOrderFromHistory(jsonObject)
//        return retrieveFromSingletonFromHistory()
//    }
}