package com.ayubo.life.ayubolife.health

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.NewDashboard.ProfileDashboardResponseData
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_om_page2_set_location.view.*
import kotlinx.android.synthetic.main.activity_om_page3_select_partner.*
import kotlinx.android.synthetic.main.activity_om_page3_select_partner.view.*
import kotlinx.android.synthetic.main.activity_omview_history.*
import kotlinx.android.synthetic.main.feed_back_popup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OMPage3SelectPartner : Fragment(),
    MedPartnerCardAdapter.MedPartnerCardItemClickListener {


    private var medPartnerCardAdapter: MedPartnerCardAdapter? = null

    lateinit var mainView: View;

    lateinit var changeFragmentInterface: OMPage3SelectPartnerNextButtonInterface;

    lateinit var prefManager: PrefManager;

    lateinit var oMCreatedOrderObj: OMCreatedOrderObj

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mainView = inflater.inflate(R.layout.activity_om_page3_select_partner, container, false);
        prefManager = PrefManager(requireContext());




        mainView.search_medicine_partner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                medPartnerCardAdapter!!.filter.filter(charSequence)
            }

            override fun afterTextChanged(charSequence: Editable?) {

            }
        });




        return mainView;

    }

    override fun onResume() {
        super.onResume()

//        oMCreatedOrderObj = OMCommon(context!!).retrieveFromDraftSingleton()
        oMCreatedOrderObj = OMCommon(requireContext()).retrieveFromCommonSingleton();


        setPartnerRecyclerview()
    }

    interface OMPage3SelectPartnerNextButtonInterface {
        fun onOMPage3SelectPartnerNextButtonClick(position: Int);

    }

    private fun setPartnerRecyclerview() {


        mainView.partnerPageProgressBar.visibility = View.VISIBLE

        val medPartnerCardItemsArrayList = ArrayList<OMPartner>()
        try {

            mainView.partner_recycler_view.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            val apiService: ApiInterface =
                ApiClient.getAzureApiClientV1ForOrderMedicine().create(ApiInterface::class.java);


            val getPartnersCall: Call<ProfileDashboardResponseData> = apiService.getPartners(
                AppConfig.APP_BRANDING_ID,
                prefManager.userToken
            );


            getPartnersCall.enqueue(object : Callback<ProfileDashboardResponseData> {
                override fun onResponse(
                    call: Call<ProfileDashboardResponseData>,
                    response: Response<ProfileDashboardResponseData>
                ) {
                    mainView.partnerPageProgressBar.visibility = View.GONE

                    if (response.isSuccessful) {

                        val omPartnerMainData: JsonArray =
                            Gson().toJsonTree(response.body()!!.data).asJsonArray;

                        if (omPartnerMainData.size() > 0) {

                            for (i in 0 until omPartnerMainData.size()) {
                                val partner = omPartnerMainData[i]

                                val oMPartner: OMPartner =
                                    Gson().fromJson(partner, OMPartner::class.java);


                                medPartnerCardItemsArrayList.add(oMPartner)


                            }

                            medPartnerCardAdapter =
                                MedPartnerCardAdapter(
                                    context!!,
                                    medPartnerCardItemsArrayList,
                                    false,
                                    true
                                )
                            try {
                                medPartnerCardAdapter!!.onOrderMedicineHistoryItemClickListener =
                                    this@OMPage3SelectPartner
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }


                            mainView.partner_recycler_view.adapter = medPartnerCardAdapter
                        }
                    }
                }

                override fun onFailure(p0: Call<ProfileDashboardResponseData>, p1: Throwable) {
                    p1.printStackTrace()
                    mainView.partnerPageProgressBar.visibility = View.GONE
                }


            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun medPartnerCardItemClick(medPartnerCardItem: OMPartner) {
        oMCreatedOrderObj.partner = medPartnerCardItem
        val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrderObj).asJsonObject;

        if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject)
        }
        oMCreatedOrderObj = OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject)

        updateOrder();
    }

    private fun updateOrder() {


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
            oMCreatedOrderObj.files,
            oMCreatedOrderObj.address,
            oMCreatedOrderObj.partner,
            oMCreatedOrderObj.payment
        )

        val jsonObject: JsonObject = Gson().toJsonTree(oMCreatedOrder).asJsonObject;


        if (oMCreatedOrderObj.status == null && (oMCreatedOrderObj.status != "Delivered" || oMCreatedOrderObj.status != "Cancelled")) {
            OMCommon(requireContext()).saveToDraftSingletonAndRetrieve(jsonObject)
        }
        oMCreatedOrderObj = OMCommon(requireContext()).saveToCommonSingletonAndRetrieve(jsonObject)


        changeFragmentInterface = context as (OMPage3SelectPartnerNextButtonInterface);
        changeFragmentInterface.onOMPage3SelectPartnerNextButtonClick(4)
    }


}